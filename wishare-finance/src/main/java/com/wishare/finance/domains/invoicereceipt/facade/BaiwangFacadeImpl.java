package com.wishare.finance.domains.invoicereceipt.facade;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.wishare.finance.apps.model.invoice.invoice.dto.InvoiceInfoDto;
import com.wishare.finance.apps.model.invoice.invoice.fo.InvoicePrintF;
import com.wishare.finance.apps.model.invoice.nuonuo.vo.InvoiceItemsV;
import com.wishare.finance.apps.model.invoice.nuonuo.vo.QueryInvoiceResultV;
import com.wishare.finance.domains.configure.chargeitem.dto.taxitem.TaxChargeItemD;
import com.wishare.finance.domains.invoicereceipt.aggregate.InvoiceRedA;
import com.wishare.finance.domains.invoicereceipt.consts.enums.InvoiceDetailTypeEnum;
import com.wishare.finance.domains.invoicereceipt.consts.enums.InvoiceLineEnum;
import com.wishare.finance.domains.invoicereceipt.consts.enums.WithTaxFlagEnum;
import com.wishare.finance.domains.invoicereceipt.dto.InvoiceDetailDto;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceE;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceReceiptDetailE;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceReceiptE;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceRedApplyE;
import com.wishare.finance.domains.invoicereceipt.repository.InvoiceReceiptDetailRepository;
import com.wishare.finance.domains.invoicereceipt.repository.InvoiceReceiptRepository;
import com.wishare.finance.domains.invoicereceipt.repository.InvoiceRepository;
import com.wishare.finance.domains.invoicereceipt.support.InvoiceDetailHelper;
import com.wishare.finance.domains.invoicereceipt.support.baiwang.BaiwangSupport;
import com.wishare.finance.infrastructure.remote.enums.NuonuoInvoiceStatusEnum;
import com.wishare.finance.infrastructure.remote.enums.NuonuoRedApplyStatusEnum;
import com.wishare.finance.infrastructure.remote.fo.external.baiwang.enums.*;
import com.wishare.finance.infrastructure.remote.fo.external.baiwang.request.CommonRequestParam;
import com.wishare.finance.infrastructure.remote.fo.external.baiwang.request.FormatCreateDataReqF;
import com.wishare.finance.infrastructure.remote.fo.external.baiwang.request.FormatCreateReqF;
import com.wishare.finance.infrastructure.remote.fo.external.baiwang.request.InvoiceDataReqF;
import com.wishare.finance.infrastructure.remote.fo.external.baiwang.request.InvoiceDetailF;
import com.wishare.finance.infrastructure.remote.fo.external.baiwang.request.InvoiceReqF;
import com.wishare.finance.infrastructure.remote.fo.external.baiwang.request.InvoiceSearchDataReqF;
import com.wishare.finance.infrastructure.remote.fo.external.baiwang.request.InvoiceSearchReqF;
import com.wishare.finance.infrastructure.remote.fo.external.baiwang.request.RedConfirmDetailReqF;
import com.wishare.finance.infrastructure.remote.fo.external.baiwang.request.RedConfirmReqF;
import com.wishare.finance.infrastructure.remote.fo.external.baiwang.request.RedConfirmSearchDataF;
import com.wishare.finance.infrastructure.remote.fo.external.baiwang.response.CommonResV;
import com.wishare.finance.infrastructure.remote.fo.external.baiwang.response.EInvoiceSearchResModelV;
import com.wishare.finance.infrastructure.remote.fo.external.baiwang.response.FormatCreateResV;
import com.wishare.finance.infrastructure.remote.fo.external.baiwang.response.InvoiceFailResV;
import com.wishare.finance.infrastructure.remote.fo.external.baiwang.response.InvoiceResModelV;
import com.wishare.finance.infrastructure.remote.fo.external.baiwang.response.InvoiceSearchResModelV;
import com.wishare.finance.infrastructure.remote.fo.external.baiwang.response.InvoiceSuccessResV;
import com.wishare.finance.infrastructure.remote.fo.external.baiwang.response.MonitorResV;
import com.wishare.finance.infrastructure.remote.fo.external.baiwang.response.RedConfirmResV;
import com.wishare.finance.infrastructure.remote.fo.external.baiwang.response.RedConfirmSearchResV;
import com.wishare.finance.infrastructure.remote.vo.external.nuonuo.NuonuoRedApplyQueryV;
import com.wishare.starter.exception.BizException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * @author: Linitly
 * @date: 2023/6/20 13:42
 * @descrption:
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ConditionalOnProperty(name = "invoice.supplier", havingValue = "baiwang")
public class BaiwangFacadeImpl extends InvoiceExternalAbService {

    private final BaiwangSupport baiwangSupport;

    private final InvoiceRepository invoiceRepository;

    private final InvoiceReceiptRepository invoiceReceiptRepository;

    private final InvoiceReceiptDetailRepository invoiceReceiptDetailRepository;

    // 默认限额
    public static final String DEFAULT_QUOTA = "999999.99";
    private static final String[] PARSE_PATTERNS = {"yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd"};

    /**
     * 增值税电子发票开票申请，蓝票/红票
     */
    @Override
    public String nuonuoBillingNew(InvoiceReceiptE invoiceReceiptE, InvoiceE invoiceE,
                                   List<InvoiceReceiptDetailE> invoiceReceiptDetailES, List<InvoiceDetailDto> invoiceDetailDtoList,
                                   String nuonuoCommunityId,
                                   Map<Long, List<TaxChargeItemD>> taxItemMapByChargeId, String tenantId,
                                   com.wishare.finance.domains.invoicereceipt.consts.enums.InvoiceTypeEnum invoiceTypeEnum) {
        // 获取通用请求参数
        CommonRequestParam reqParam = getReqParam(MethodEnum.发票开具接口标识);
        // 开票流水号
        String serialNo = UUID.randomUUID().toString().replaceAll("-", "");
        // 获取请求数据体，并补充
        InvoiceDataReqF dataReq = getCommonInvoiceDataReq(invoiceE, invoiceReceiptE, serialNo);
        this.appendDzInvoiceDataReq(dataReq);
        // 获取请求数据体详情
        List<InvoiceDetailF> invoiceDetailReq;
        // 获取请求体
        InvoiceReqF invoiceReq = getInvoiceReq(invoiceE);
        invoiceReq.setData(dataReq);
        switch (invoiceTypeEnum) {
            case 红票:
                appendRedInvoiceDataReq(dataReq, invoiceE, "", "");
                invoiceDetailReq = getRedInvoiceDetailReq(invoiceDetailDtoList, dataReq);
                break;
            case 蓝票:
            default:
                this.appendBlueInvoiceDataReq(dataReq);
                invoiceDetailReq = getBlueInvoiceDetailReq(invoiceDetailDtoList, dataReq);
                // 校验限额，蓝票才需要
                oneQuotaValid(InvoiceTypeCodeEnum.增值税电子发票, invoiceE.getMachineCode(), invoiceReq.getTaxNo(), dataReq.getInvoiceTotalPriceTax());
                break;
        }
        dataReq.setInvoiceDetailsList(invoiceDetailReq);
        // 请求
        CommonResV<InvoiceResModelV> resV = baiwangSupport.invoiceIssue(reqParam, invoiceReq);
        // 处理响应
        handleInvoiceResponse(resV);
        return serialNo;
    }

    @Override
    public String paperInvoices(InvoiceReceiptE invoiceReceiptE, InvoiceE invoiceE, List<InvoiceReceiptDetailE> invoiceReceiptDetailES, List<InvoiceDetailDto> invoiceDetailDtoList, String nuonuoCommunityId, Map<Long, List<TaxChargeItemD>> taxItemMapByChargeId, String tenantId, com.wishare.finance.domains.invoicereceipt.consts.enums.InvoiceTypeEnum invoiceTypeEnum, String billInfoNo) {
        return null;
    }

    @Override
    public String invoicesPrints(InvoicePrintF form, String tenantId) {
        return null;
    }

    /**
     * 查询 增值税电子发票
     * @param tenantId
     * @param serialNum 发票流水号
     * @param taxnum 销方税号
     * @return
     */
    @Override
    public List<QueryInvoiceResultV> queryInvoiceResult(String tenantId, String serialNum, String taxnum, Long orderNo) {
        // 获取通用请求参数
        CommonRequestParam reqParam = getReqParam(MethodEnum.发票查询接口标识);
        // 获取请求数据体
        InvoiceSearchDataReqF dataReqF = getInvoiceSearchDataReq(serialNum, "");
        // 获取请求体
        InvoiceSearchReqF reqF = getInvoiceSearchReq(taxnum);
        reqF.setData(dataReqF);
        // 请求
        CommonResV<List<InvoiceSearchResModelV>> resV = baiwangSupport.invoiceSearch(reqParam, reqF);
        // 转换为nuonuo响应体，返回结果为空说明没有查询到，返回null
        return CollectionUtils.isEmpty(resV.getModel()) ? null : this.invoiceSearchResultTransferToNuonuoResult(resV.getModel());
    }

    /**
     * 全电普票开票申请，蓝票
     */
    @Override
    public String opeMplatformBillingNew(InvoiceReceiptE invoiceReceiptE, InvoiceE invoiceE, List<InvoiceReceiptDetailE> invoiceReceiptDetailES,

                                         List<InvoiceDetailDto> invoiceDetailDtoList, String nuonuoCommunityId, Map<Long, List<TaxChargeItemD>> taxItemMapByChargeId, String tenantId) {
        // 开票流水号
        String serialNo = UUID.randomUUID().toString().replaceAll("-", "");
        // 获取请求数据体，并补充
        InvoiceDataReqF dataReq = getCommonInvoiceDataReq(invoiceE, invoiceReceiptE, serialNo);
        this.appendBlueInvoiceDataReq(dataReq);
        appendQdInvoiceDataReq(dataReq);
        // 获取请求数据体详情
        List<InvoiceDetailF> blueInvoiceDetailReq = this.getBlueInvoiceDetailReq(invoiceDetailDtoList, dataReq);
        dataReq.setInvoiceDetailsList(blueInvoiceDetailReq);
        // 获取请求体
        InvoiceReqF invoiceReq = getInvoiceReq(invoiceE);
        invoiceReq.setData(dataReq);
        // 校验限额，全电不支持调用该接口
//        oneQuotaValid(InvoiceTypeCodeEnum.全电发票_普通发票, invoiceE.getMachineCode(), invoiceReq.getTaxNo(), dataReq.getInvoiceTotalPriceTax());
        // 获取通用请求参数
        CommonRequestParam reqParam = getReqParam(MethodEnum.发票开具接口标识);
        // 请求
        CommonResV<InvoiceResModelV> resV = baiwangSupport.invoiceIssue(reqParam, invoiceReq);
        // 处理响应
        handleInvoiceResponse(resV);
        // 调用版式生成接口
        this.formatCreate(resV.getModel().getSuccess(), invoiceE.getSalerTaxNum());
        return serialNo;
    }

    /**
     * 查询 全电普票
     * @param tenantId
     * @param serialNum 发票流水号
     * @param taxnum 销方税号
     * @param orderNo
     * @return
     */
    @Override
    public List<QueryInvoiceResultV> opeMplatformQueryInvoiceResult(String tenantId, String serialNum, String taxnum, Long orderNo, InvoiceE invoiceE) {
        // 获取通用请求参数
        CommonRequestParam reqParam = this.getReqParam(MethodEnum.全电发票查询接口标识);
        // 获取请求数据体，这里查询时可能将发票号码放入了发票对象(已有红票时)
        InvoiceSearchDataReqF dataReq = this.getInvoiceSearchDataReq(serialNum, invoiceE.getInvoiceNo());
//        dataReq.setInvoiceTypeCode(InvoiceTypeCodeEnum.全电发票_普通发票.getCode());
        // 获取请求体
        InvoiceSearchReqF reqF = this.getInvoiceSearchReq(taxnum);
        reqF.setData(dataReq);
        // 请求
        CommonResV<List<EInvoiceSearchResModelV>> resV = baiwangSupport.qdInvoiceSearch(reqParam, reqF);
        // 处理响应
        // 转换为nuonuo响应体，返回结果为空说明没有查询到，返回null
        return CollectionUtils.isEmpty(resV.getModel()) ? null : this.qdInvoiceSearchResultTransferToNuonuoResult(resV.getModel());
    }

    @Override
    public String invoiceRedApply(InvoiceReceiptE invoiceReceiptE, InvoiceE invoiceE, List<InvoiceReceiptDetailE> invoiceReceiptDetailES, List<InvoiceDetailDto> invoiceDetailDtoList, String nuonuoCommunityId, Map<Long, List<TaxChargeItemD>> taxItemMapByChargeId, String tenantId, com.wishare.finance.domains.invoicereceipt.consts.enums.InvoiceTypeEnum invoiceTypeEnum) {
        return null;
    }


    /**
     * 全电普票 红冲申请单 申请
     * @param invoiceRedA
     * @return
     */
    @Override
    public String electronInvoiceRedApply(InvoiceRedA invoiceRedA) {
        // 获取通用请求参数
        CommonRequestParam reqParam = this.getReqParam(MethodEnum.红字确认单申请接口标识);
        // 获取请求数据体，并补充
        // 流水号
        String serialNo = UUID.randomUUID().toString().replaceAll("-", "");
        RedConfirmReqF dataReq = this.getRedConfirmReq(invoiceRedA.getInvoiceRedE(), invoiceRedA.getInvoiceReceiptRedE(), serialNo);
        // 获取请求数据体详情
        List<RedConfirmDetailReqF> redConfirmDetailList = this.getRedConfirmDetailList(invoiceRedA.getInvoiceDetailDtoList(), dataReq);
        dataReq.setRedConfirmDetailReqEntityList(redConfirmDetailList);
        // 请求
        CommonResV<List<RedConfirmResV>> resV = baiwangSupport.redConfirm(reqParam, dataReq);
        // 返回结果校验，一般不会出现这个问题，正确响应应该有值
        if (CollectionUtils.isEmpty(resV.getModel()) || StringUtils.isBlank(resV.getModel().get(0).getRedConfirmUuid())) {
            throw BizException.throw400("百望发票红字确认出错，响应信息为：" + JSON.toJSONString(resV));
        }
        // 如果是直接直接生成了红票，调用版式生成接口
        // 经过测试，这里直接调用会显示未找到对应发票，应该对方也是异步生成红票，先确定了红票号码，这里不处理
//        this.formatCreate(invoiceRedA.getInvoiceRedE().getSalerTaxNum(), resV.getModel());
        // 返回红字确认单UUID
        return resV.getModel().get(0).getRedConfirmUuid();
    }

    /**
     * 红字确认单查询
     * @param applyE
     * @param invoiceE
     * @return
     */
    @Override
    public List<NuonuoRedApplyQueryV> electronInvoiceRedApplyQuery(InvoiceRedApplyE applyE, InvoiceE invoiceE) {
        // 获取通用请求参数
        CommonRequestParam reqParam = getReqParam(MethodEnum.红字确认单查询接口标识);
        // 获取请求数据体
        RedConfirmSearchDataF dataReqF = getRedSearchDataReqF(invoiceE);
        // 请求
        CommonResV<List<RedConfirmSearchResV>> resV = baiwangSupport.redSearch(reqParam, dataReqF);
        // 转换为nuonuo响应体
        return CollectionUtils.isEmpty(resV.getModel()) ? Lists.newArrayList() : this.redSearchResultTransferToNuonuoResult(resV.getModel());
    }

    /**
     * 全电红票红冲
     * @param invoiceE
     * @param queryV
     * @return
     */
    @Override
    public String electronInvoiceRed(InvoiceE invoiceE, NuonuoRedApplyQueryV queryV) {
        // 获取其他数据(原蓝票数据，和原红冲的数据)
        List<InvoiceInfoDto> redInvoiceInfo = invoiceRepository.getRedInvoiceInfo(invoiceE.getBlueInvoiceReceiptId());
        // 之前的红冲数据去除红冲申请时保存的红票数据，也就是去除本次的红冲数据
        List<InvoiceInfoDto> redInvoiceInfoBefore = redInvoiceInfo.stream().filter(e -> !e.getId().equals(invoiceE.getId())).collect(Collectors.toList());
        InvoiceReceiptE invoiceReceiptE = invoiceReceiptRepository.getById(invoiceE.getBlueInvoiceReceiptId());
        InvoiceE blueInvoice = invoiceRepository.getByInvoiceReceiptId(invoiceE.getBlueInvoiceReceiptId());
        List<InvoiceReceiptDetailE> blueInvoiceDetailList = invoiceReceiptDetailRepository.queryByInvoiceReceiptIds(Lists.newArrayList(invoiceE.getBlueInvoiceReceiptId()));
        // 组装统一明细（全电暂时都是全部红冲）
        List<InvoiceDetailDto> invoiceDetailDtoList = InvoiceDetailHelper.allRedFlushToInvoiceDetailDtoList(blueInvoiceDetailList, redInvoiceInfoBefore);
        // 获取通用请求参数
        CommonRequestParam reqParam = this.getReqParam(MethodEnum.发票开具接口标识);
        // 开票流水号
        String serialNo = UUID.randomUUID().toString().replaceAll("-", "");
        // 获取请求数据体，并补充
        InvoiceDataReqF dataReq = this.getCommonInvoiceDataReq(invoiceE, invoiceReceiptE, serialNo);
        this.appendRedInvoiceDataReq(dataReq, invoiceE, queryV.getBillNo(), queryV.getBillUuid());
        // 补充原全电蓝票号码
        dataReq.setOriginalEinvoiceNo(blueInvoice.getInvoiceNo());
        this.appendQdInvoiceDataReq(dataReq);
        // 获取请求数据体详情
        List<InvoiceDetailF> redInvoiceDetailReq = this.getRedInvoiceDetailReq(invoiceDetailDtoList, dataReq);
        dataReq.setInvoiceDetailsList(redInvoiceDetailReq);
        // 获取请求体
        InvoiceReqF invoiceReq = this.getInvoiceReq(invoiceE);
        invoiceReq.setData(dataReq);
        // 请求
        CommonResV<InvoiceResModelV> resV = baiwangSupport.invoiceIssue(reqParam, invoiceReq);
        // 处理响应
        this.handleInvoiceResponse(resV);
        return serialNo;
    }




    /**
     * 版式生成（全电红字确认申请后）
     */
    private void formatCreate(String taxNo, List<RedConfirmResV> redConfirmResList) {
        for (RedConfirmResV resV : redConfirmResList) {
            // 如果不是无需双方确认状态，或者红字发票号码为空，不处理
            if (!RedConfirmStatusEnum.无需确认.getCode().equals(resV.getConfirmState()) || StringUtils.isBlank(resV.getRedInvoiceNo())) {
                continue;
            }
            // 获取通用请求参数
            CommonRequestParam reqParam = getReqParam(MethodEnum.全电版式文件接口标识);
            // 获取请求数据体
            FormatCreateDataReqF dataReqF = this.getFormatCreateData(resV.getRedInvoiceNo());
            // 请求体
            FormatCreateReqF reqF = new FormatCreateReqF();
            reqF.setTaxNo(taxNo);
            reqF.setData(dataReqF);
            baiwangSupport.asyncFormatCreate(reqParam, reqF);
        }
    }

    /**
     * 版式生成（全电蓝票申请后）
     */
    private void formatCreate(List<InvoiceSuccessResV> successResList, String taxNo) {
        for (InvoiceSuccessResV resV : successResList) {
            if (StringUtils.isBlank(resV.getEinvoiceNo())) {
                continue;
            }
            // 获取通用请求参数
            CommonRequestParam reqParam = getReqParam(MethodEnum.全电版式文件接口标识);
            // 获取请求数据体
            FormatCreateDataReqF dataReqF = this.getFormatCreateData(resV.getEinvoiceNo());
            // 请求体
            FormatCreateReqF reqF = new FormatCreateReqF();
            reqF.setTaxNo(taxNo);
            reqF.setData(dataReqF);
            baiwangSupport.asyncFormatCreate(reqParam, reqF);
        }
    }

    /**
     * 版式生成
     */
    private String formatCreate(EInvoiceSearchResModelV modelV) {
        if (!InvoiceStatusEnum.开具成功.getCode().equals(modelV.getInvoiceStatus())) {
            return "";
        }
        // 获取通用请求参数
        CommonRequestParam reqParam = getReqParam(MethodEnum.全电版式文件接口标识);
        // 获取请求数据体
        FormatCreateDataReqF dataReqF = this.getFormatCreateData(modelV);
        // 请求体
        FormatCreateReqF reqF = new FormatCreateReqF();
        reqF.setTaxNo(modelV.getSellerTaxNo());
        reqF.setData(dataReqF);
        // 请求
        CommonResV<FormatCreateResV> resV = baiwangSupport.formatCreate(reqParam, reqF);
        return (Objects.isNull(resV.getModel()) || StringUtils.isBlank(resV.getModel().getQueryData())) ? "" : resV.getModel().getQueryData();
    }

    /**
     * 获取版式文件生成接口请求体
     */
    private FormatCreateDataReqF getFormatCreateData(EInvoiceSearchResModelV modelV) {
        FormatCreateDataReqF dataReqF = new FormatCreateDataReqF();
        dataReqF.setSerialNo(StringUtils.isBlank(modelV.getSerialNo()) ? "" : modelV.getSerialNo());
        dataReqF.setEinvoiceNo(StringUtils.isBlank(modelV.getEinvoiceNo()) ? "" : modelV.getEinvoiceNo());
//        dataReqF.setInvoiceNo(modelV.getInvoiceNo());
        // 1 全电版式生成 其他代表税控发票生成
        dataReqF.setInvoiceIssueMode("1");
        return dataReqF;
    }

    /**
     * 获取版式文件生成接口请求体
     */
    private FormatCreateDataReqF getFormatCreateData(String einvoiceNo) {
        FormatCreateDataReqF dataReqF = new FormatCreateDataReqF();
        dataReqF.setEinvoiceNo(einvoiceNo);
        // 1 全电版式生成 其他代表税控发票生成
        dataReqF.setInvoiceIssueMode("1");
        return dataReqF;
    }

    /**
     * 监控信息查询，主要是限额判断
     */
    private BigDecimal monitorSearch(InvoiceTypeCodeEnum typeCodeEnum, String taxDiskNo, String taxNo) {
        // 获取通用请求参数
        CommonRequestParam reqParam = getReqParam(MethodEnum.监控信息查询接口标识);
        // 获取请求数据体
        InvoiceDataReqF dataReqF = new InvoiceDataReqF();
        dataReqF.setInvoiceTypeCode(typeCodeEnum.getCode());
        // 获取请求体
        InvoiceReqF reqF = new InvoiceReqF();
        reqF.setTaxNo(taxNo);
        reqF.setTaxDiskNo(taxDiskNo);
        reqF.setData(dataReqF);
        String oneQuota;
        try {
            // 请求
            CommonResV<MonitorResV> resV = baiwangSupport.monitorSearch(reqParam, reqF);
            // 处理响应
            oneQuota = (Objects.isNull(resV.getModel()) || StringUtils.isBlank(resV.getModel().getOneQuota())) ? DEFAULT_QUOTA : resV.getModel().getOneQuota();
        } catch (Exception e) {
            log.error("请求百望监控接口出错：", e);
            oneQuota = DEFAULT_QUOTA;
        }
        return new BigDecimal(oneQuota);
    }

    /**
     * 单票限额校验
     */
    private void oneQuotaValid(InvoiceTypeCodeEnum typeCodeEnum, String taxDiskNo, String taxNo, BigDecimal totalPrice) {
        BigDecimal quota = monitorSearch(typeCodeEnum, taxDiskNo, taxNo);
        if (totalPrice.compareTo(quota) >= 0) {
            // 总价大于等于限额，抛出异常
            throw BizException.throw400("您要开票的金额超过限制，请重新开票或联系管理员");
        }
    }

    /**
     * 发票开具通用请求体
     */
    public InvoiceReqF getInvoiceReq(InvoiceE invoiceE) {
        InvoiceReqF req = new InvoiceReqF();
        req.setTaxNo(invoiceE.getSalerTaxNum());
        req.setFormatGenerate(true);
        req.setTaxDiskNo(invoiceE.getMachineCode());
        return req;
    }

    /**
     * 全电发票(普通)补充
     */
    public void appendQdInvoiceDataReq(InvoiceDataReqF dataReq) {
        dataReq.setInvoiceTypeCode(InvoiceTypeCodeEnum.全电发票_普通发票.getCode());
        // 全电纸质发票标志，暂时设为否
        dataReq.setPaperInvoiceFlag("N");
    }

    /**
     * 增值税电子发票补充
     */
    private void appendDzInvoiceDataReq(InvoiceDataReqF dataReq) {
        dataReq.setInvoiceTypeCode(InvoiceTypeCodeEnum.增值税电子发票.getCode());
    }

    /**
     * 蓝票补充数据
     */
    private void appendBlueInvoiceDataReq(InvoiceDataReqF dataReq) {
        dataReq.setInvoiceType(InvoiceTypeEnum.蓝票.getCode());
    }

    /**
     * 红票补充数据
     */
    public void appendRedInvoiceDataReq(InvoiceDataReqF req, InvoiceE invoiceE, String redInfoNo, String redConfirmUuid) {
        req.setInvoiceType(InvoiceTypeEnum.红票.getCode());
        // 暂定全额开票
//        req.setTaxationLabel(TaxationLabelEnum.全额开票.getCode());
        // 红字确认单编号，目前我方是 增值税电子发票 和 全电普票，传入红字确认单编号
        req.setRedInfoNo(redInfoNo);
        req.setRedConfirmUuid(redConfirmUuid);
        req.setOriginalInvoiceCode(invoiceE.getInvoiceCode());
        req.setOriginalInvoiceNo(invoiceE.getInvoiceNo());
        req.setOriginalEinvoiceNo(invoiceE.getInvoiceNo());
        // 暂定"开票有误"原因
        req.setRedIssueReason("2");
    }

    /**
     * 开票结果响应处理
     */
    private void handleInvoiceResponse(CommonResV<InvoiceResModelV> resV) {
        // 处理响应
        if (Objects.nonNull(resV.getModel()) && !CollectionUtils.isEmpty(resV.getModel().getFail())) {
            BigDecimal failTotalPrice = resV.getModel().getFail().stream()
                    .map(InvoiceFailResV::getInvoiceTotalPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            if (failTotalPrice.compareTo(BigDecimal.ZERO) > 0) {
                // 大于0
                throw BizException.throw400("百望发票开具失败，失败金额大于0，响应结果为：" + JSON.toJSONString(resV));
            }
        }
    }

    /**
     * 获取发票开具请求数据体(通用)
     */
    public InvoiceDataReqF getCommonInvoiceDataReq(InvoiceE invoiceE, InvoiceReceiptE invoiceReceiptE, String serialNo) {
        InvoiceDataReqF req = new InvoiceDataReqF();
        req.setSerialNo(serialNo);
        // 设置购方信息
        req.setBuyerName(invoiceE.getBuyerName());
        req.setBuyerPhone(invoiceE.getBuyerTel());
        req.setBuyerTaxNo(invoiceE.getBuyerTaxNum());
        req.setBuyerAddress(invoiceE.getBuyerAddress());
        req.setBuyerBankAccount(invoiceE.getBuyerAccount());
        req.setBuyerEmail(invoiceE.getEmail());
        req.setBuyerTelphone(invoiceE.getBuyerTel());
        if (StringUtils.isNotBlank(invoiceE.getBuyerAccount())) {
            String[] bankNameAndAccount = invoiceE.getBuyerAccount().split(" ");
            req.setBuyerBankName(bankNameAndAccount[0]);
            req.setBuyerBankNumber(bankNameAndAccount.length > 1 ? bankNameAndAccount[1] : "");
        }
        if (StringUtils.isNotBlank(invoiceE.getBuyerAddress()) && StringUtils.isNotBlank(invoiceE.getBuyerTel())) {
            req.setBuyerAddressPhone(invoiceE.getBuyerAddress() + " " + invoiceE.getBuyerTel());
        }
        // 设置销方信息
        req.setSellerAddressPhone((StringUtils.isBlank(invoiceE.getSalerAddress()) ? "" : invoiceE.getSalerAddress())
                + " " +
                (StringUtils.isBlank(invoiceE.getSalerTel()) ? "" : invoiceE.getSalerTel()));
        if (StringUtils.isNotBlank(invoiceE.getSalerAccount())) {
            String[] bankNameAndAccount = invoiceE.getSalerAccount().split(" ");
            req.setSellerBankName(bankNameAndAccount[0]);
            req.setSellerBankNumber(bankNameAndAccount.length > 1 ? bankNameAndAccount[1] : "");
            req.setSellerBankAccount(invoiceE.getSalerAccount());
        }
        req.setSellerAddress(invoiceE.getSalerAddress());
        req.setSellerTelphone(invoiceE.getSalerTel());
        // 开票人？收款人？
        //  全电类开具时，根据终端及默
        //  认开票员进行匹配，如传入的开票人在终端授权下按传入值 开具
        //，如未查询到传入值对应的开票员则通过默认开票员进行开具
       if (!InvoiceLineEnum.全电普票.getCode().equals(invoiceReceiptE.getType())) {
           req.setDrawer(invoiceReceiptE.getClerk());
        }
        req.setPayee("");
        req.setChecker("");
        //
        req.setInvoiceSpecialMark(InvoiceSpecialMarkEnum.普通发票.getCode());
        req.setRemarks(invoiceReceiptE.getRemark());
        //

        return req;
    }

    /**
     * 获取发票详情（红票时）
     */
    public List<InvoiceDetailF> getRedInvoiceDetailReq(List<InvoiceDetailDto> invoiceDetailDtoList,
                                                       InvoiceDataReqF dataReq) {
        // 结果
        List<InvoiceDetailF> detailReqList = Lists.newArrayList();
        // 行号
        int num = 1;
        // 价税合计
        BigDecimal totalPriceTax = BigDecimal.ZERO;
        // 合计税额
        BigDecimal totalTax = BigDecimal.ZERO;
        // 外层data的含税标志
        PriceTaxMarkEnum priceTaxMarkEnum = PriceTaxMarkEnum.不含税;
        for (InvoiceDetailDto detailDto : invoiceDetailDtoList) {
            InvoiceDetailF invoiceDetailF = new InvoiceDetailF();
            invoiceDetailF.setGoodsLineNo(num);
            invoiceDetailF.setGoodsOriginalLineNo(detailDto.getBlueLineNo().toString());
            invoiceDetailF.setGoodsCode(detailDto.getGoodsCode());
            invoiceDetailF.setGoodsName(detailDto.getGoodsName());
            invoiceDetailF.setGoodsTaxRate(detailDto.getTaxRate().setScale(3, RoundingMode.DOWN));
            // 我方目前都是含税
            if (WithTaxFlagEnum.含税.getCode().equals(detailDto.getWithTaxFlag())) {
                // 设置含税标志
                priceTaxMarkEnum = PriceTaxMarkEnum.含税;
                invoiceDetailF.setPriceTaxMark(PriceTaxMarkEnum.含税.getCode());
            } else {
                // 无税率，不含税，不含税标识可以不设置值，默认
                invoiceDetailF.setPriceTaxMark(PriceTaxMarkEnum.不含税.getCode());
            }

            BigDecimal tax = BigDecimal.ZERO;
            BigDecimal priceTax = BigDecimal.ZERO;
            if (InvoiceDetailTypeEnum.部分红冲明细.getCode().equals(detailDto.getDetailType())) {
                tax = detailDto.getTax().setScale(2, RoundingMode.HALF_EVEN);
                priceTax = detailDto.getGoodsTotalPriceTax().setScale(2, RoundingMode.HALF_EVEN);
            } else if (InvoiceDetailTypeEnum.剩余红冲明细.getCode().equals(detailDto.getDetailType())) {
                BigDecimal redFlushedTotalPriceTax = detailDto.getRedFlushedTotalPriceTax().stream().reduce(BigDecimal.ZERO, (acc, i) -> acc.add(i.setScale(2, RoundingMode.HALF_EVEN)));
                // 原蓝票明细+已经红冲的金额，剩余的红冲金额，然后取负数
                priceTax = detailDto.getGoodsTotalPriceTax().setScale(2, RoundingMode.HALF_EVEN).add(redFlushedTotalPriceTax).negate();
                BigDecimal redFlushedTax = detailDto.getRedFlushedTax().stream().reduce(BigDecimal.ZERO, (acc, i) -> acc.add(i.setScale(2, RoundingMode.HALF_EVEN)));
                tax = detailDto.getTax().setScale(2, RoundingMode.HALF_EVEN).add(redFlushedTax).negate();
            }

            invoiceDetailF.setGoodsTotalTax(tax);
            invoiceDetailF.setGoodsPrice(priceTax.abs());
            invoiceDetailF.setGoodsTotalPrice(priceTax);
            // 累计信息添加
            totalTax = totalTax.add(tax);
            totalPriceTax = totalPriceTax.add(priceTax);
            detailReqList.add(invoiceDetailF);
            num++;
        }

        // 价税合计和税额之前已计算为负数，直接相减即可
        BigDecimal totalPrice = totalPriceTax.subtract(totalTax).setScale(2, RoundingMode.HALF_EVEN);
        // 根据不同的请求对象赋值总金额、税额、价税合计
        if (Objects.nonNull(dataReq)) {
            dataReq.setPriceTaxMark(priceTaxMarkEnum.getCode());
            dataReq.setInvoiceTotalTax(totalTax);
            dataReq.setInvoiceTotalPriceTax(totalPriceTax);
            dataReq.setInvoiceTotalPrice(totalPrice);
        }
        return detailReqList;
    }

    /**
     * 获取发票详情（多种情况）
     */
    public List<InvoiceDetailF> getBlueInvoiceDetailReq(List<InvoiceDetailDto> invoiceDetailDtoList,
                                                    InvoiceDataReqF dataReq) {

        // 结果
        List<InvoiceDetailF> detailReqList = Lists.newArrayList();
        // 价税合计
        BigDecimal totalPriceTax = BigDecimal.ZERO;
        // 合计税额
        BigDecimal totalTax = BigDecimal.ZERO;
        // 外层data的含税标志
        PriceTaxMarkEnum priceTaxMarkEnum = PriceTaxMarkEnum.不含税;
        for (InvoiceDetailDto detailDto : invoiceDetailDtoList) {
            InvoiceDetailF invoiceDetailF = new InvoiceDetailF();
            invoiceDetailF.setGoodsLineNo(detailDto.getBlueLineNo());
            invoiceDetailF.setGoodsCode(detailDto.getGoodsCode());
            invoiceDetailF.setGoodsName(detailDto.getGoodsName());
            invoiceDetailF.setGoodsTaxRate(detailDto.getTaxRate().setScale(3, RoundingMode.DOWN));
            BigDecimal tax = detailDto.getTax().setScale(2, RoundingMode.HALF_EVEN);
            invoiceDetailF.setGoodsTotalTax(tax);
            BigDecimal priceTax = detailDto.getGoodsTotalPriceTax().setScale(2, RoundingMode.HALF_EVEN);
            invoiceDetailF.setGoodsPrice(priceTax.abs());
            invoiceDetailF.setGoodsTotalPrice(priceTax);

            // 我方目前都是含税
            if (WithTaxFlagEnum.含税.getCode().equals(detailDto.getWithTaxFlag())) {
                // 设置含税标志
                priceTaxMarkEnum = PriceTaxMarkEnum.含税;
                invoiceDetailF.setPriceTaxMark(PriceTaxMarkEnum.含税.getCode());
            } else {
                // 无税率，不含税，不含税标识可以不设置值，默认
                invoiceDetailF.setPriceTaxMark(PriceTaxMarkEnum.不含税.getCode());
            }

            // 合计
            totalTax = totalTax.add(tax);
            totalPriceTax = totalPriceTax.add(priceTax);
            detailReqList.add(invoiceDetailF);
        }
        BigDecimal totalPrice = totalPriceTax.subtract(totalTax).setScale(2, RoundingMode.HALF_EVEN);
        // 根据不同的请求对象赋值总金额、税额、价税合计
        if (Objects.nonNull(dataReq)) {
            dataReq.setPriceTaxMark(priceTaxMarkEnum.getCode());
            dataReq.setInvoiceTotalTax(totalTax);
            dataReq.setInvoiceTotalPriceTax(totalPriceTax);
            dataReq.setInvoiceTotalPrice(totalPrice);
        }
        return detailReqList;
    }

    /**
     * 获取红字确认详情
     */
    private List<RedConfirmDetailReqF> getRedConfirmDetailList(List<InvoiceDetailDto> invoiceDetailDtoList, RedConfirmReqF dataReq) {
        List<RedConfirmDetailReqF> detailList = Lists.newArrayList();
        // 价税合计
        BigDecimal totalPrice = BigDecimal.ZERO;
        // 合计税额
        BigDecimal totalTax = BigDecimal.ZERO;
        // 行号
        int num = 1;
        for (InvoiceDetailDto detailDto : invoiceDetailDtoList) {
            RedConfirmDetailReqF detailReqF = new RedConfirmDetailReqF();
            detailReqF.setOriginalInvoiceDetailNo(detailDto.getBlueLineNo());
            detailReqF.setGoodsLineNo(num);
            detailReqF.setGoodsCode(detailDto.getGoodsCode());
            detailReqF.setGoodsName(detailDto.getGoodsName());
            detailReqF.setGoodsSimpleName(detailDto.getGoodsName());
            detailReqF.setProjectName(detailDto.getGoodsName());
            detailReqF.setGoodsTaxRate(detailDto.getTaxRate().setScale(3, RoundingMode.DOWN));
            BigDecimal tax = BigDecimal.ZERO;
            BigDecimal priceTax = BigDecimal.ZERO;
            if (InvoiceDetailTypeEnum.部分红冲明细.getCode().equals(detailDto.getDetailType())) {
                tax = detailDto.getTax().setScale(2, RoundingMode.HALF_EVEN);
                priceTax = detailDto.getGoodsTotalPriceTax().setScale(2, RoundingMode.HALF_EVEN);
            } else if (InvoiceDetailTypeEnum.剩余红冲明细.getCode().equals(detailDto.getDetailType())) {
                BigDecimal redFlushedTotalPriceTax = detailDto.getRedFlushedTotalPriceTax().stream().reduce(BigDecimal.ZERO, (acc, i) -> acc.add(i.setScale(2, RoundingMode.HALF_EVEN)));
                // 原蓝票明细+已经红冲的金额，剩余的红冲金额，然后取负数
                priceTax = detailDto.getGoodsTotalPriceTax().setScale(2, RoundingMode.HALF_EVEN).add(redFlushedTotalPriceTax).negate();
                BigDecimal redFlushedTax = detailDto.getRedFlushedTax().stream().reduce(BigDecimal.ZERO, (acc, i) -> acc.add(i.setScale(2, RoundingMode.HALF_EVEN)));
                tax = detailDto.getTax().setScale(2, RoundingMode.HALF_EVEN).add(redFlushedTax).negate();
            }

            detailReqF.setGoodsTotalTax(tax);
            BigDecimal price = priceTax.subtract(tax).setScale(2, RoundingMode.HALF_EVEN);
            detailReqF.setGoodsTotalPrice(price);
            // 累计信息添加
            totalTax = totalTax.add(tax);
            totalPrice = totalPrice.add(price);
            detailList.add(detailReqF);
            num++;
        }
        if (Objects.nonNull(dataReq)) {
            dataReq.setInvoiceTotalTax(totalTax);
            dataReq.setInvoiceTotalPrice(totalPrice);
        }
        return detailList;
    }

    /**
     * 获取百望通用请求参数体
     */
    public CommonRequestParam getReqParam(MethodEnum methodEnum) {
        CommonRequestParam requestParam = new CommonRequestParam();
        requestParam.setMethod(methodEnum.getValue());
        requestParam.setRequestId(UUID.randomUUID().toString());
        return requestParam;
    }

//    /**
//     * 获取税目信息
//     */
//    private Map<Long, List<TaxChargeItemD>> getTaxItemMapByChargeId(List<InvoiceReceiptDetailE> invoiceReceiptDetailES) {
//        Map<Long, List<TaxChargeItemD>> taxItemMapByChargeId = new HashMap<>();
//        if (CollectionUtils.isEmpty(invoiceReceiptDetailES)) {
//            return taxItemMapByChargeId;
//        }
//        List<Long> chargeItemIds = invoiceReceiptDetailES.stream().map(InvoiceReceiptDetailE::getChargeItemId).distinct().collect(Collectors.toList());
//        taxItemMapByChargeId = ampFinanceFacade.queryByChargeIdList(chargeItemIds);
//        if (CollectionUtils.isEmpty(taxItemMapByChargeId)) {
//            throw BizException.throw400("该批次费项未配置对应的税目编码");
//        }
//        return taxItemMapByChargeId;
//    }
//
//    /**
//     * 获取商品编码
//     */
//    private String getGoodsCode(InvoiceReceiptDetailE invoiceReceiptDetailE, Map<Long, List<TaxChargeItemD>> taxItemMapByChargeId) {
//        if (CollectionUtils.isEmpty(taxItemMapByChargeId)) {
//            return "";
//        }
//        List<TaxChargeItemD> taxChargeItemDS = taxItemMapByChargeId.get(invoiceReceiptDetailE.getChargeItemId());
//        if (CollectionUtils.isEmpty(taxChargeItemDS)) {
//            throw BizException.throw400(invoiceReceiptDetailE.getChargeItemName() + " 该费项未配置对应的税目编码");
//        }
//        TaxChargeItemD taxChargeItemRv = taxChargeItemDS.get(0);
//        if (taxChargeItemRv == null) {
//            throw BizException.throw400(invoiceReceiptDetailE.getChargeItemName() + " 该费项未配置对应的税目编码");
//        }
//        return taxChargeItemRv.getTaxItem().getCode();
//    }

    /**
     * 获取发票查询请求数据体
     */
    private InvoiceSearchDataReqF getInvoiceSearchDataReq(String serialNo, String invoiceNo) {
        InvoiceSearchDataReqF req = new InvoiceSearchDataReqF();
        req.setSerialNo(serialNo);
        // 查询全电时使用
        req.setEinvoiceNo(StringUtils.isBlank(invoiceNo) ? "" : invoiceNo);
        req.setQueryAll(true);
        return req;
    }

    /**
     * 获取发票查询请求体
     * @param taxNo 销方机构税号
     * @return
     */
    private InvoiceSearchReqF getInvoiceSearchReq(String taxNo) {
        InvoiceSearchReqF req = new InvoiceSearchReqF();
        req.setTaxNo(taxNo);
        return req;
    }

    /**
     * 获取红字申请表申请数据请求体
     */
    private RedConfirmReqF getRedConfirmReq(InvoiceE invoiceRedE, InvoiceReceiptE invoiceReceiptE, String serialNo) {
        RedConfirmReqF req = new RedConfirmReqF();
        req.setTaxNo(invoiceRedE.getSalerTaxNum());
        req.setRedConfirmSerialNo(serialNo);
        req.setEntryIdentity("01");
        req.setSellerTaxNo(invoiceRedE.getSalerTaxNum());
        req.setSellerTaxName(invoiceRedE.getSalerName());
        req.setBuyerTaxNo(invoiceRedE.getBuyerTaxNum());
        req.setBuyerTaxName(invoiceRedE.getBuyerName());
        req.setOriginInvoiceIsPaper("N");
        // 发票来源：有发票代码号码没有全电号码的就是增值税发票管理系统开出来的，有全电号码的就是电子发票服务平台开出来的
        if (StringUtils.isBlank(invoiceRedE.getInvoiceCode())) {
            // 没有发票代码，也就是电子发票平台的
            req.setInvoiceSource(BaiWangInvoiceSourceEnum.电子发票服务平台.getCode());
            req.setOriginalInvoiceNo(invoiceRedE.getInvoiceNo());
        } else {
            req.setInvoiceSource(BaiWangInvoiceSourceEnum.增值税发票管理系统.getCode());
            req.setOriginalPaperInvoiceCode(invoiceRedE.getInvoiceCode());
            req.setOriginalPaperInvoiceNo(invoiceRedE.getInvoiceNo());
        }
        req.setOriginInvoiceType(InvoiceTypeCodeEnum.全电发票_普通发票.getCode());
        // 蓝票开票日期
        req.setOriginInvoiceDate(Objects.isNull(invoiceReceiptE.getBillingTime()) ? null : DateUtil.format(invoiceReceiptE.getBillingTime(), "yyyy-MM-dd HH:mm:ss"));
        // 原蓝票金额
        this.appendBlueAmount(invoiceRedE.getBlueInvoiceReceiptId(), req);
        // 红冲原因：暂定开票有误
        req.setRedInvoiceLabel("01");
        return req;
    }

    /**
     * 补充原蓝票的总金额和税额
     */
    private void appendBlueAmount(Long blueInvoiceReceiptId, RedConfirmReqF req) {
        List<InvoiceReceiptDetailE> blueDetailList = invoiceReceiptDetailRepository.queryByInvoiceReceiptIds(Lists.newArrayList(blueInvoiceReceiptId));
        if (CollectionUtils.isEmpty(blueDetailList)) {
            req.setOriginInvoiceTotalPrice(BigDecimal.ZERO);
            req.setOriginInvoiceTotalTax(BigDecimal.ZERO);
            return;
        }
        // 原蓝票价税合计和税额
        BigDecimal originInvoiceTotalPrice = BigDecimal.ZERO;
        BigDecimal originInvoiceTotalTax = BigDecimal.ZERO;
        Map<String, List<InvoiceReceiptDetailE>> blueDetailMap = InvoiceDetailHelper.detailGroup(blueDetailList);
        for (Map.Entry<String, List<InvoiceReceiptDetailE>> entry : blueDetailMap.entrySet()) {
            InvoiceReceiptDetailE blueDetail = entry.getValue().get(0);
            // 价税合计，为实际金额*100
            long totalPriceTax = entry.getValue().stream().filter(Objects::nonNull).mapToLong(InvoiceReceiptDetailE::getInvoiceAmount).sum();
            // 税额
            BigDecimal tax = InvoiceDetailHelper.getTax(totalPriceTax, new BigDecimal(blueDetail.getTaxRate()), blueDetail.getWithTaxFlag(), 2);
            originInvoiceTotalTax = originInvoiceTotalTax.add(tax);
            BigDecimal priceTax = InvoiceDetailHelper.divideOneHundred(totalPriceTax, 2);
            originInvoiceTotalPrice = originInvoiceTotalPrice.add(priceTax.subtract(tax).setScale(2, RoundingMode.HALF_EVEN));
        }
        // 设置总金额和总税额
        req.setOriginInvoiceTotalPrice(originInvoiceTotalPrice.abs());
        req.setOriginInvoiceTotalTax(originInvoiceTotalTax.abs());
    }

    /**
     * 获取红字确认单查询请求数据体
     */
    private RedConfirmSearchDataF getRedSearchDataReqF(InvoiceE invoiceE) {
        RedConfirmSearchDataF dataF = new RedConfirmSearchDataF();
        // 百望红字确认单的UUID暂存在了发票的发票流水号中
        dataF.setRedConfirmUuid(invoiceE.getInvoiceSerialNum());
        dataF.setTaxNo(invoiceE.getSalerTaxNum());
        dataF.setSellerTaxNo(invoiceE.getSalerTaxNum());
        return dataF;
    }


    /**
     * 将百望全电查询请求体转换为nuonuo响应体
     */
    private List<QueryInvoiceResultV> qdInvoiceSearchResultTransferToNuonuoResult(List<EInvoiceSearchResModelV> models) {
        List<QueryInvoiceResultV> result = new ArrayList<>(models.size());
        for (EInvoiceSearchResModelV model : models) {
            QueryInvoiceResultV resultV = new QueryInvoiceResultV();
            this.invoiceStatusTransferToNuonuoStatus(model.getInvoiceStatus(), resultV);
            resultV.setSerialNo(model.getSerialNo());
            resultV.setOrderNo(model.getOrderNo());
            // 数电发票查询文档中没有返回图片地址，需要判断调用接口获取版式链接
            String eInvoiceUrl = this.formatCreate(model);
            resultV.setPdfUrl(eInvoiceUrl);
            resultV.setPictureUrl(eInvoiceUrl);
            // 开票日期由于红冲时无法使用没有时分秒的时间，使用第一个明细里的开票日期
            resultV.setInvoiceTime(strToTimestamp(model.getInvoiceDetailsList().get(0).getInvoiceDate()));
            // 这里发票代码、号码三个字段，都有可能有值，是由于发票来源不同
            resultV.setInvoiceCode(model.getInvoiceCode());
            resultV.setInvoiceNo(model.getInvoiceNo());
            if (StringUtils.isNotBlank(model.getEinvoiceNo())) {
                resultV.setInvoiceNo(model.getEinvoiceNo());
            }
            resultV.setExTaxAmount(Objects.isNull(model.getInvoiceTotalPrice()) ? null : model.getInvoiceTotalPrice().toString());
            resultV.setTaxAmount(Objects.isNull(model.getInvoiceTotalTax()) ? null : model.getInvoiceTotalTax().toString());
            resultV.setOrderAmount(Objects.isNull(model.getInvoiceTotalPriceTax()) ? null : model.getInvoiceTotalPriceTax().toString());
            resultV.setPayerName(model.getBuyerName());
            resultV.setPayerTaxNo(model.getBuyerTaxNo());
            resultV.setAddress(model.getBuyerAddress());
            resultV.setTelephone(model.getBuyerTelphone());
            resultV.setBankAccount(model.getBuyerBankName() + " " + model.getBuyerBankNumber());
            // 发票种类等
            resultV.setClerk(model.getDrawer());
            resultV.setSalerAccount(model.getSellerBankNumber());
            resultV.setSalerTel(model.getSellerTelphone());
            resultV.setSalerAddress(model.getSellerAddress());
            resultV.setSalerTaxNum(model.getSellerTaxNo());
            resultV.setSaleName(model.getSellerName());
            resultV.setRemark(model.getRemarks());
            //
            resultV.setProductOilFlag("0");
            resultV.setVehicleFlag("0");
            if (InvoiceSpecialMarkEnum.成品油.getCode().equals(model.getInvoiceSpecialMark())) {
                resultV.setProductOilFlag("1");
            } else if (InvoiceSpecialMarkEnum.机动车.getCode().equals(model.getInvoiceSpecialMark())) {
                resultV.setVehicleFlag("1");
            }
            resultV.setOldInvoiceNo(model.getOriginalEinvoiceNo());
            resultV.setListFlag(model.getInvoiceListMark());
            resultV.setPhone(model.getBuyerPhone());
            resultV.setNotifyEmail(model.getBuyerEmail());
            // 发票类型
            if (InvoiceTypeEnum.蓝票.getCode().equals(model.getInvoiceType())) {
                resultV.setInvoiceType(com.wishare.finance.domains.invoicereceipt.consts.enums.InvoiceTypeEnum.蓝票.getCode().toString());
            } else if (InvoiceTypeEnum.红票.getCode().equals(resultV.getInvoiceType())) {
                resultV.setInvoiceType(com.wishare.finance.domains.invoicereceipt.consts.enums.InvoiceTypeEnum.红票.getCode().toString());
            }
            resultV.setInvalidTime(model.getInvoiceInvalidDate());
            // 明细
            List<InvoiceItemsV> invoiceItemsVS = this.invoiceDetailTransferToNuonuo(model.getInvoiceDetailsList());
            resultV.setInvoiceItems(invoiceItemsVS);
            result.add(resultV);
        }
        return result;
    }



    /**
     * 将百望发票查询请求体转换为nuonuo响应体
     * @param models
     * @return
     */
    private List<QueryInvoiceResultV> invoiceSearchResultTransferToNuonuoResult(List<InvoiceSearchResModelV> models) {
        List<QueryInvoiceResultV> result = new ArrayList<>(models.size());
        for (InvoiceSearchResModelV model : models) {
            QueryInvoiceResultV resultV = new QueryInvoiceResultV();
            invoiceStatusTransferToNuonuoStatus(model.getInvoiceStatus(), resultV);
            resultV.setSerialNo(model.getSerialNo());
            resultV.setMachineCode(model.getMachineNo());
            // pdf地址->下载地址  图片地址-> 预览地址
            resultV.setPdfUrl(model.getEInvoiceUrl());
            resultV.setPictureUrl(model.getH5InvoiceUrl());
            // 适配以前的，转时间戳
            resultV.setInvoiceTime(strToTimestamp(model.getInvoiceDate()));
            resultV.setInvoiceCode(model.getInvoiceCode());
            resultV.setInvoiceNo(model.getInvoiceNo());
            resultV.setExTaxAmount(Objects.isNull(model.getInvoiceTotalPrice()) ? null : model.getInvoiceTotalPrice().toString());
            resultV.setTaxAmount(Objects.isNull(model.getInvoiceTotalTax()) ? null : model.getInvoiceTotalTax().toString());
            resultV.setOrderAmount(Objects.isNull(model.getInvoiceTotalPriceTax()) ? null : model.getInvoiceTotalPriceTax().toString());
            resultV.setPayerName(model.getBuyerName());
            resultV.setPayerTaxNo(model.getBuyerTaxNo());
            if (StringUtils.isNotBlank(model.getBuyerAddressPhone())) {
                String[] addressAndPhone = model.getBuyerAddressPhone().split(" ");
                resultV.setAddress(addressAndPhone[0]);
                resultV.setTelephone(addressAndPhone.length > 1 ? addressAndPhone[1] : "");
            }
            resultV.setBankAccount(model.getBuyerBankAccount());
            // 发票种类
//            resultV.setInvoiceKind("");
            resultV.setCheckCode(model.getInvoiceCheckCode());
            resultV.setQrCode(model.getInvoiceQrCode());
            resultV.setCipherText(model.getTaxControlCode());
            resultV.setPaperPdfUrl(model.getEInvoiceUrl());
            resultV.setClerk(model.getDrawer());
            resultV.setPayee(model.getPayee());
            resultV.setChecker(model.getChecker());
            if (StringUtils.isNotBlank(model.getSellerBankAccount())) {
                String[] bankAndAccount = model.getSellerBankAccount().split(" ");
                resultV.setSalerAccount(bankAndAccount.length > 1 ? bankAndAccount[1] : "");
            }
            if (StringUtils.isNotBlank(model.getSellerAddressPhone())) {
                String[] addressAndPhone = model.getSellerAddressPhone().split(" ");
                resultV.setSalerAddress(addressAndPhone[0]);
                resultV.setSalerTel(addressAndPhone.length > 1 ? addressAndPhone[1] : "");
            }
            resultV.setSaleName(model.getSellerName());
            resultV.setRemark(model.getRemarks());
            //
            resultV.setProductOilFlag("0");
            resultV.setVehicleFlag("0");
            if (InvoiceSpecialMarkEnum.成品油.getCode().equals(model.getInvoiceSpecialMark())) {
                resultV.setProductOilFlag("1");
            } else if (InvoiceSpecialMarkEnum.机动车.getCode().equals(model.getInvoiceSpecialMark())) {
                resultV.setVehicleFlag("1");
            }
            resultV.setOldInvoiceCode(model.getOriginalInvoiceCode());
            resultV.setOldInvoiceNo(model.getOriginalInvoiceNo());
            resultV.setListFlag(model.getInvoiceListMark());
            resultV.setPhone(model.getBuyerPhone());
            resultV.setNotifyEmail(model.getBuyerEmail());
            // 发票类型
            if (InvoiceTypeEnum.蓝票.getCode().equals(model.getInvoiceType())) {
                resultV.setInvoiceType(com.wishare.finance.domains.invoicereceipt.consts.enums.InvoiceTypeEnum.蓝票.getCode().toString());
            } else if (InvoiceTypeEnum.红票.getCode().equals(resultV.getInvoiceType())) {
                resultV.setInvoiceType(com.wishare.finance.domains.invoicereceipt.consts.enums.InvoiceTypeEnum.红票.getCode().toString());
            }
            resultV.setInvalidTime(model.getInvoiceInvalidDate());
            // 明细
            List<InvoiceItemsV> invoiceItemsVS = invoiceDetailTransferToNuonuo(model.getInvoiceDetailsList());
            resultV.setInvoiceItems(invoiceItemsVS);
            result.add(resultV);
        }
        return result;
    }


    /**
     * 发票商品明细转换为nuonuo的发票明细（发票查询结果）
     * @param details
     * @return
     */
    private List<InvoiceItemsV> invoiceDetailTransferToNuonuo(List<InvoiceDetailF> details) {
        List<InvoiceItemsV> result = new ArrayList<>();
        if (CollectionUtils.isEmpty(details)) {
            return result;
        }

        for (InvoiceDetailF detail : details) {
            InvoiceItemsV itemV = new InvoiceItemsV();
            itemV.setItemName(detail.getGoodsName());
            itemV.setItemNum(Objects.isNull(detail.getGoodsQuantity()) ? null : detail.getGoodsQuantity().toString());
            itemV.setItemUnit(detail.getGoodsUnit());
            itemV.setItemPrice(Objects.isNull(detail.getGoodsPrice()) ? null : detail.getGoodsPrice().toString());
            itemV.setItemTaxRate(Objects.isNull(detail.getGoodsTaxRate()) ? null : detail.getGoodsTaxRate().toString());
            itemV.setItemAmount(Objects.isNull(detail.getGoodsTotalPrice()) ? null : detail.getGoodsTotalPrice().toString());
            itemV.setItemTaxAmount(Objects.isNull(detail.getGoodsTotalTax()) ? null : detail.getGoodsTotalTax().toString());
            itemV.setItemSpec(detail.getGoodsSpecification());
            itemV.setItemCode(detail.getGoodsCode());
            itemV.setInvoiceLineProperty(detail.getInvoiceLineNature());
            itemV.setZeroRateFlag(detail.getFreeTaxMark());
            itemV.setFavouredPolicyName(detail.getVatSpecialManagement());
            result.add(itemV);
        }
        return result;
    }


    /**
     *
     * @param invoiceStatus
     * @param resultV
     */
    private void invoiceStatusTransferToNuonuoStatus(String invoiceStatus, QueryInvoiceResultV resultV) {
        if (InvoiceStatusEnum.开具成功.getCode().equals(invoiceStatus)) {
            // 开具成功
            resultV.setStatus(NuonuoInvoiceStatusEnum.开票完成.getCode().toString());
            resultV.setStatusMsg(NuonuoInvoiceStatusEnum.开票完成.getDes());
        } else if (InvoiceStatusEnum.空白发票作废.getCode().equals(invoiceStatus)) {
            resultV.setStatus(NuonuoInvoiceStatusEnum.发票已作废.getCode().toString());
            resultV.setStatusMsg(NuonuoInvoiceStatusEnum.发票已作废.getDes());
        } else if (InvoiceStatusEnum.已开发票作废.getCode().equals(invoiceStatus)) {
            resultV.setStatus(NuonuoInvoiceStatusEnum.发票已作废.getCode().toString());
            resultV.setStatusMsg(NuonuoInvoiceStatusEnum.发票已作废.getDes());
        } else if (InvoiceStatusEnum.正票全额红冲.getCode().equals(invoiceStatus) ||
                InvoiceStatusEnum.正票部分红冲.getCode().equals(invoiceStatus)) {
            resultV.setStatus(NuonuoInvoiceStatusEnum.发票已作废.getCode().toString());
            resultV.setStatusMsg(NuonuoInvoiceStatusEnum.发票已作废.getDes());
        } else {
            resultV.setStatus(NuonuoInvoiceStatusEnum.开票失败.getCode().toString());
            resultV.setStatusMsg(NuonuoInvoiceStatusEnum.开票失败.getDes());
        }
    }

    /**
     * 红字查询结果转换为nuonuo的响应体
     *
     * @return
     */
    private List<NuonuoRedApplyQueryV> redSearchResultTransferToNuonuoResult(List<RedConfirmSearchResV> models) {
        List<NuonuoRedApplyQueryV> result = new ArrayList<>(models.size());
        for (RedConfirmSearchResV model : models) {
            NuonuoRedApplyQueryV queryV = new NuonuoRedApplyQueryV();
            queryV.setBillNo(model.getRedConfirmNo());
            queryV.setBillUuid(model.getRedConfirmUuid());
            // 已开具红字发票标记转换
            if ("Y".equals(model.getAlreadyRedInvoiceFlag())) {
                queryV.setOpenStatus("1");
                // 标识已有百望红票
                queryV.setHasRedInvoice(true);
            } else if ("N".equals(model.getAlreadyRedInvoiceFlag())) {
                queryV.setOpenStatus("0");
            }
            // 转换状态
            this.redConfirmStatusTransferToNuonoStatus(model.getConfirmState(), queryV);
            // 详情
            if (Objects.nonNull(model.getElectricInvoiceDetails())) {
                queryV.setDetail(JSON.toJSONString(model.getElectricInvoiceDetails()));
            }
            queryV.setInvoiceNo(model.getRedInvoiceNo());
            result.add(queryV);
        }
        return result;
    }

    /**
     *
     * @param redConfirmStatus
     * @param queryV
     */
    private void redConfirmStatusTransferToNuonoStatus(String redConfirmStatus, NuonuoRedApplyQueryV queryV) {
        // 状态转换
        if (RedConfirmStatusEnum.无需确认.getCode().equals(redConfirmStatus)) {
            queryV.setBillStatus(NuonuoRedApplyStatusEnum.无需确认.getCode());
        } else if (RedConfirmStatusEnum.销方录入待购方确认.getCode().equals(redConfirmStatus)) {
            queryV.setBillStatus(NuonuoRedApplyStatusEnum.销方录入待购方确认.getCode());
        } else if (RedConfirmStatusEnum.购方录入待销方确认.getCode().equals(redConfirmStatus)) {
            queryV.setBillStatus(NuonuoRedApplyStatusEnum.购方录入待销方确认.getCode());
        } else if (RedConfirmStatusEnum.购销双方已确认.getCode().equals(redConfirmStatus)) {
            queryV.setBillStatus(NuonuoRedApplyStatusEnum.购销双方已确认.getCode());
        } else if (RedConfirmStatusEnum.作废_销方录入购方否认.getCode().equals(redConfirmStatus)) {
            queryV.setBillStatus(NuonuoRedApplyStatusEnum.作废_销方录入购方否认.getCode());
        } else if (RedConfirmStatusEnum.作废_购方录入销方否认.getCode().equals(redConfirmStatus)) {
            queryV.setBillStatus(NuonuoRedApplyStatusEnum.作废_购方录入销方否认.getCode());
        } else if (RedConfirmStatusEnum.作废_超72小时未确认.getCode().equals(redConfirmStatus)) {
            queryV.setBillStatus(NuonuoRedApplyStatusEnum.作废_超72小时未确认.getCode());
        } else if (RedConfirmStatusEnum.作废_发起方已撤销.getCode().equals(redConfirmStatus)) {
            queryV.setBillStatus(NuonuoRedApplyStatusEnum.作废_发起方已撤销.getCode());
        } else if (RedConfirmStatusEnum.作废_确认后撤销.getCode().equals(redConfirmStatus)) {
            queryV.setBillStatus(NuonuoRedApplyStatusEnum.作废_确认后撤销.getCode());
        } else if (RedConfirmStatusEnum.作废_异常凭证.getCode().equals(redConfirmStatus)) {
            queryV.setBillStatus(NuonuoRedApplyStatusEnum.申请失败.getCode());
        } else {
            // 未知情况
            queryV.setBillStatus(NuonuoRedApplyStatusEnum.申请失败.getCode());
        }
    }

    /**
     * 字符串转时间戳(13位)
     * @return
     */
    public static String strToTimestamp(String dateStr) {
        if (StringUtils.isBlank(dateStr)) {
            return "";
        }
        Date dateRes = null;
        try {
            dateRes = DateUtils.parseDate(dateStr, PARSE_PATTERNS);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return String.valueOf(dateRes.getTime());
    }
}
