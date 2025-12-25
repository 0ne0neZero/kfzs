package com.wishare.finance.apps.service.voucher;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wishare.finance.apps.model.configure.accountbook.dto.AccountbookDTO;
import com.wishare.finance.apps.model.voucher.fo.*;
import com.wishare.finance.apps.model.voucher.vo.BillPostedStatusV;
import com.wishare.finance.apps.model.voucher.vo.SyncBatchVoucherResultV;
import com.wishare.finance.apps.model.voucher.vo.VoucherV;
import com.wishare.finance.apps.service.configure.accountbook.AccountBookAppService;
import com.wishare.finance.apps.service.voucher.eventinference.DefaultVoucherInferenceAppService;
import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import com.wishare.finance.domains.configure.businessunit.entity.BusinessUnitE;
import com.wishare.finance.domains.configure.businessunit.service.BusinessUnitDomainService;
import com.wishare.finance.domains.configure.subject.consts.enums.AssisteItemTypeEnum;
import com.wishare.finance.domains.configure.subject.entity.AssisteItemOBV;
import com.wishare.finance.domains.ncc.service.NccDomainService;
import com.wishare.finance.domains.voucher.consts.enums.VoucherEventTypeEnum;
import com.wishare.finance.domains.voucher.consts.enums.VoucherLoanTypeEnum;
import com.wishare.finance.domains.voucher.consts.enums.VoucherMakeTypeEnum;
import com.wishare.finance.domains.voucher.consts.enums.VoucherSystemEnum;
import com.wishare.finance.domains.voucher.consts.enums.VoucherTypeEnum;
import com.wishare.finance.domains.voucher.entity.Voucher;
import com.wishare.finance.domains.voucher.entity.VoucherDetailOBV;
import com.wishare.finance.domains.voucher.entity.VoucherE;
import com.wishare.finance.domains.voucher.model.SupItem;
import com.wishare.finance.domains.voucher.model.VoucherDetail;
import com.wishare.finance.domains.voucher.service.VoucherDomainService;
import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.finance.infrastructure.remote.clients.base.OrgClient;
import com.wishare.finance.infrastructure.remote.vo.org.OrgFinanceRv;
import com.wishare.finance.infrastructure.utils.AmountUtils;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.interfaces.ApiBase;
import com.wishare.starter.utils.ErrorAssertUtil;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: pgq
 * @since: 2022/10/25 10:02
 * @version: 1.0.0
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class VoucherAppService implements ApiBase {

    private final VoucherDomainService voucherDomainService;

    private final DefaultVoucherInferenceAppService defaultVoucherInferenceAppService;

    private final AccountBookAppService accountBookAppService;

    private final NccDomainService nccDomainService;

    private final BusinessUnitDomainService businessUnitDomainService;

    private final OrgClient orgClient;

    /**
     * 获取推凭明细 （分页）
     * @param form
     * @return
     */
    public PageV<VoucherV> queryPage(PageF<SearchF<?>> form) {

        return voucherDomainService.queryPage(form);
    }

    /**
     * 根据推凭记录获取推凭明细
     * @param form
     * @param recordId
     * @return
     */
    public PageV<VoucherV> queryPageByRecordId(PageF<SearchF<?>> form, Long recordId) {
        return voucherDomainService.queryPageByRecordId(form, recordId);
    }

    /**
     * 按条件统计已经推凭的金额
     *
     * @param form
     * @return
     */
    public Long staticVoucherAmount(PageF<SearchF<?>> form) {
        return voucherDomainService.staticVoucherAmount(form);
    }

    /**
     * 保存预制凭证
     * @param voucher
     * @return
     */
    public Long save(PrefabricationVoucherSaveF voucher) {

        Voucher voucherInfo = new Voucher();
        voucherInfo.setMadeType(VoucherMakeTypeEnum.手动.getCode());
        voucherInfo.setVoucherType(VoucherTypeEnum.记账凭证.getCode());
        //查询账簿
        AccountbookDTO accountBook = accountBookAppService.detailAccountBook(voucher.getAccountBookId());
        ErrorAssertUtil.notNullThrow403(accountBook, ErrorMessage.ACCOUNT_BOOK_NO_EXISTS);
        voucherInfo.setAccountBookId(accountBook.getId());
        voucherInfo.setAccountBookCode(accountBook.getCode());
        voucherInfo.setAccountBookName(accountBook.getName());
        BusinessUnitE businessUnitE = businessUnitDomainService.getById(accountBook.getStatutoryBodyId());
        ErrorAssertUtil.notNullThrow403(businessUnitE, ErrorMessage.BUSINESS_UNIT_NO_EXISTS);
        voucherInfo.setStatutoryBodyId(businessUnitE.getId());
        voucherInfo.setStatutoryBodyCode(businessUnitE.getCode());
        voucherInfo.setStatutoryBodyName(businessUnitE.getName());
        voucherInfo.setCostCenters(new ArrayList<>());
        long creditAmount = 0;
        long debitAmount = 0;
        for (VoucherDetail detail : voucher.getDetails()) {
            creditAmount += AmountUtils.toLong(detail.getCreditAmount());
            debitAmount += AmountUtils.toLong(detail.getDebitAmount());
        }
        ErrorAssertUtil.isTrueThrow403(creditAmount == debitAmount, ErrorMessage.VOUCHER_AMOUNT_ERROR);
        voucherInfo.setAmount(creditAmount);
        voucherInfo.setDetails(voucher.getDetails().stream().map(item -> {
            VoucherDetailOBV voucherDetailOBV = new VoucherDetailOBV();
            voucherDetailOBV.setSubjectId(item.getSubjectId());
            voucherDetailOBV.setSubjectCode(item.getSubjectCode());
            voucherDetailOBV.setSubjectName(item.getSubjectName());
            voucherDetailOBV.setSummary(item.getRuleRemark());
            voucherDetailOBV.setCreditAmount(AmountUtils.toLong(item.getCreditAmount()));
            voucherDetailOBV.setDebitAmount(AmountUtils.toLong(item.getDebitAmount()));
            voucherDetailOBV.setType(voucherDetailOBV.getCreditAmount() > 0 ? VoucherLoanTypeEnum.贷方.getCode() : VoucherLoanTypeEnum.借方.getCode());
            voucherDetailOBV.setOriginalAmount(voucherDetailOBV.getCreditAmount() + voucherDetailOBV.getDebitAmount());
            //无费项，无法匹配到现金流量，默认填空
            voucherDetailOBV.setCashFlows(new ArrayList<>());
            List<SupItem> supItems = item.getSupItems();
            if (CollectionUtils.isNotEmpty(supItems)){
                voucherDetailOBV.setAssisteItems(supItems.stream().map(i->{
                    AssisteItemOBV assisteItemOBV = new AssisteItemOBV();
                    assisteItemOBV.setCode(i.getValue());
                    assisteItemOBV.setName(i.getValueName());
                    assisteItemOBV.setType(AssisteItemTypeEnum.valueOfByAscCode(i.getValue()).getCode());
                    assisteItemOBV.setAscCode(i.getCode());
                    assisteItemOBV.setAscName(i.getName());
                    return assisteItemOBV;
                }).collect(Collectors.toList()));
            }
            return voucherDetailOBV;
        }).collect(Collectors.toList()));
        voucherInfo.setFiscalPeriod(voucher.getTime());
        voucherInfo.setEvenType(VoucherEventTypeEnum.手动生成.getCode());
        voucherInfo.setMakerId(voucher.getMakerId());
        voucherInfo.setMakerName(voucher.getMaker());
        voucherInfo.setSyncSystem(voucher.getSystemCode());
        return voucherDomainService.makeVoucher(voucherInfo);
    }

    /**
     * 构造voucher的detail
     * @param voucher
     * @return
     */
    private String generateDetail(PrefabricationVoucherSaveF voucher) {
        List<JSONObject> list = new ArrayList<>();
        for (VoucherDetail voucherDetail : voucher.getDetails()) {
            JSONObject detail = new JSONObject();
            if (voucherDetail.getDebitAmount().compareTo(BigDecimal.ZERO) == 0) {
                detail.put("type", "icredit");
                detail.put("typeName", "借");
                detail.put("amount", voucherDetail.getCreditAmount().multiply(new BigDecimal(100)));
                detail.put("localCredit", voucherDetail.getCreditAmount().multiply(new BigDecimal( 100)));
            } else {
                detail.put("type", "debit");
                detail.put("typeName", "贷");
                detail.put("amount", voucherDetail.getDebitAmount().multiply(new BigDecimal( 100)));
                detail.put("localDebit", voucherDetail.getDebitAmount().multiply(new BigDecimal( 100)));
            }
            detail.put("ruleRemark", voucherDetail.getRuleRemark());
            detail.put("subjectCode", voucherDetail.getSubjectCode());
            detail.put("subjectName", voucherDetail.getSubjectName());
            detail.put("current", "人民币");
            detail.put("supItemName", voucherDetail.getSubjectName());

            List<String> auxiliaryCount = new ArrayList<>();
            StringJoiner joiner = new StringJoiner(";");
            for (SupItem supItem : voucherDetail.getSupItems()) {
                joiner.add("【" + supItem.getName() + "】 " + supItem.getValueName());
                auxiliaryCount.add(supItem.getCode());
            }
            detail.put("supItemName", joiner.toString());
            detail.put("auxiliaryCount", JSON.toJSON(auxiliaryCount));
            list.add(detail);
        }
        return JSON.toJSONString(list);
    }


    /**
     * 根据名称获取辅助核算项具体数据
     * @param name
     * @param code
     * @return
     */
    public List<?> getSupValues(String name, String code) {
        return nccDomainService.getSupValues(name, code, getTenantId().get());
    }

    /**
     * 批量同步
     * @param voucherBatchInferF
     * @return
     */
    public Map<String, Object> batchInfer(VoucherBatchInferF voucherBatchInferF) {
        List<VoucherE> list = voucherDomainService.listById(voucherBatchInferF.getVoucherIds());

        return defaultVoucherInferenceAppService.inferPrefabricationVoucher(list);
    }

    /**
     * 批量同步凭证
     * @param syncBatchVoucherF
     * @return
     */
    @Transactional
    public SyncBatchVoucherResultV syncBatchVoucher(SyncBatchVoucherF syncBatchVoucherF) {
        return voucherDomainService.syncBatchVoucher(syncBatchVoucherF.getVoucherIds(), VoucherSystemEnum.valueOfByCode(syncBatchVoucherF.getVoucherSystem()));
    }

    @Transactional
    public Map<String,Object> cancelBatch(List<Long> voucherIds) {
        return voucherDomainService.cancelBatch(voucherIds);
    }

    /**
     * 查询账单是否推凭
     * @param checkPostedStatusF
     * @return
     */
    public BillPostedStatusV checkPostedStatus(CheckPostedStatusF checkPostedStatusF) {
        return voucherDomainService.checkPostedStatus(checkPostedStatusF.getId(), BillTypeEnum.valueOfByCode(checkPostedStatusF.getBillType()));
    }

    /**
     * 设置凭证明细
     * @param setDetailsF 凭证明细信息
     * @return
     */
    public Boolean setDetails(SetDetailsF setDetailsF) {
        return voucherDomainService.setDetails(setDetailsF);
    }
}
