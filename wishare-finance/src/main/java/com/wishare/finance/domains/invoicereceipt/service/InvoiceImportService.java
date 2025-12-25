package com.wishare.finance.domains.invoicereceipt.service;

import com.alibaba.fastjson.JSON;
import com.wishare.finance.apps.template.BlueInvoiceImportT;
import com.wishare.finance.domains.cache.CaffeineCacheService;
import com.wishare.finance.domains.imports.ExcelCheckDataService;
import com.wishare.finance.domains.imports.entity.ExcelSheet;
import com.wishare.finance.domains.invoicereceipt.aggregate.InvoiceBlueA;
import com.wishare.finance.domains.invoicereceipt.consts.enums.InvoiceTitleTypeEnum;
import com.wishare.finance.domains.invoicereceipt.repository.InvoiceReceiptRepository;
import com.wishare.finance.domains.invoicereceipt.repository.InvoiceRepository;
import com.wishare.finance.domains.report.enums.InvoiceLineEnum;
import com.wishare.finance.infrastructure.remote.vo.space.CommunityShortRV;
import com.wishare.finance.infrastructure.utils.NumberUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class InvoiceImportService extends ExcelCheckDataService<BlueInvoiceImportT> {

    /**
     * 支持的票据类型
     */
    public static Set<String> invoiceType;

    static {
        Set<String> invoiceType = new HashSet<>();
        invoiceType.add(InvoiceLineEnum.增值税电子发票.getDes());
        invoiceType.add(InvoiceLineEnum.全电普票.getDes());
        invoiceType.add(InvoiceLineEnum.全电专票.getDes());
        invoiceType.add(InvoiceLineEnum.增值税普通发票.getDes());
        invoiceType.add(InvoiceLineEnum.增值税专用发票.getDes());
        invoiceType.add(InvoiceLineEnum.定额发票.getDes());
        InvoiceImportService.invoiceType = Collections.unmodifiableSet(invoiceType);
    }

    private final CaffeineCacheService cacheService;
    private final InvoiceDomainService invoiceDomainService;
    private final InvoiceRepository invoiceRepository;
    private final InvoiceReceiptRepository invoiceReceiptRepository;

    public List<BlueInvoiceImportT> saveSuccessData(List<BlueInvoiceImportT> successList) {
        List<BlueInvoiceImportT> errorList = new ArrayList<>();
        if (CollectionUtils.isEmpty(successList)) {
            return errorList;
        }
        for (BlueInvoiceImportT invoiceImport : successList) {
            try {
                InvoiceBlueA invoiceBlueA = new InvoiceBlueA(invoiceImport);
                invoiceReceiptRepository.save(invoiceBlueA.getInvoiceReceiptE());
                invoiceRepository.save(invoiceBlueA.getInvoiceE());
            } catch (Exception e) {
                log.error("发票号码：{}, 补录异常！", invoiceImport.getInvoiceNo(), e);
                invoiceImport.setErrMsg("补录异常:" + e.getMessage());
                errorList.add(invoiceImport);
            }
        }
        return errorList;
    }

    public String validRecordChoiceInfo(BlueInvoiceImportT obj, ExcelSheet excelSheet) {
        //1. 必填项校验
        if (!checkImportParam(obj)){
            return "未填必填项";
        }
        if (obj.getPriceTaxAmount().compareTo(BigDecimal.ZERO) < 0) {
            return "价税合计金额小于0";
        }
        if (obj.getTaxAmount().compareTo(BigDecimal.ZERO) < 0) {
            return "税额小于0";
        }
        if (obj.getBillingTime() != null && LocalDateTime.now().isBefore(obj.getBillingTime())) {
            return "开票时间不能大于当前时间";
        }
//        String taxRate = obj.getTaxRate();
//        if (!NumberUtil.isValidTaxRate(taxRate)) {
//            return "税率格式错误";
//        }
        if (!invoiceType.contains(obj.getInvoiceType())) {
            return "不支持的票据类型";
        }
        String invoiceTitleType = obj.getInvoiceTitleType();
        if (StringUtils.isNotBlank(invoiceTitleType)) {
            Integer invoiceTitleTypeCode = InvoiceTitleTypeEnum.getCodeByDes(invoiceTitleType);
            if (Objects.isNull(invoiceTitleTypeCode)) {
                return "发票抬头错误";
            }
        } else {
            obj.setInvoiceTitleType(InvoiceTitleTypeEnum.个人.getDes());
        }
        // 查询当前项目是否存在
        Map<String, CommunityShortRV> communityNameMap = cacheService.getCommunityNameMap();
        CommunityShortRV communityShortV = communityNameMap.get(obj.getCommunityName());
        if (Objects.isNull(communityShortV)){
            return "该项目不存在";
        }
        obj.setCommunityId(communityShortV.getId());
        String invoiceNo = obj.getInvoiceNo();
        long invoiceCount = invoiceDomainService.countByInvoiceNo(invoiceNo);
        if (invoiceCount > 0) {
            return "发票号码已存在";
        }
        return "";
    }

    private boolean checkImportParam(BlueInvoiceImportT obj) {
        log.info("开始检查必填项：导入行信息:{}", JSON.toJSONString(obj));
        // 项目名称不可为空
        if (Objects.isNull(obj) || StringUtils.isBlank(obj.getCommunityName())){
            return false;
        }
        if (StringUtils.isAnyBlank(obj.getInvoiceNo(), obj.getInvoiceType()) ||
                (ObjectUtils.anyNull(obj.getPriceTaxAmount(), obj.getTaxAmount())) ||
                (!InvoiceLineEnum.定额发票.getDes().equals(obj.getInvoiceType()) && StringUtils.isBlank(obj.getClerk()) && obj.getBillingTime() == null)) {
            return false;
        }
        return true;
    }

}
