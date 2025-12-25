package com.wishare.finance.apps.scheduler.invoicereceipt;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.google.common.collect.Lists;
import com.wishare.bizlog.BizLog;
import com.wishare.bizlog.content.Content;
import com.wishare.bizlog.content.ContentOption;
import com.wishare.bizlog.content.OptionStyle;
import com.wishare.bizlog.content.PlainTextDataItem;
import com.wishare.finance.apps.model.bill.vo.BillSimpleInfoV;
import com.wishare.finance.apps.model.invoice.invoice.dto.BillInvoiceDto;
import com.wishare.finance.apps.model.invoice.invoice.dto.CallBackV;
import com.wishare.finance.apps.model.invoice.invoice.dto.InvoiceInfoDto;
import com.wishare.finance.apps.model.invoice.nuonuo.vo.QueryInvoiceResultV;
import com.wishare.finance.apps.service.invoicereceipt.InvoiceAppService;
import com.wishare.finance.domains.mq.MessageSend;
import com.wishare.finance.domains.bill.aggregate.data.EnvData;
import com.wishare.finance.domains.bill.facade.BillFacade;
import com.wishare.finance.domains.bill.facade.MsgFacade;
import com.wishare.finance.domains.bill.service.BillDomainServiceOld;
import com.wishare.finance.domains.invoicereceipt.consts.enums.*;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.*;
import com.wishare.finance.domains.invoicereceipt.facade.InvoiceExternalService;
import com.wishare.finance.domains.invoicereceipt.repository.InvoiceReceiptDetailRepository;
import com.wishare.finance.domains.invoicereceipt.repository.InvoiceReceiptRepository;
import com.wishare.finance.domains.invoicereceipt.repository.InvoiceRedApplyRepository;
import com.wishare.finance.domains.invoicereceipt.repository.InvoiceRepository;
import com.wishare.finance.domains.invoicereceipt.service.InvoiceDomainService;
import com.wishare.finance.infrastructure.bizlog.LogAction;
import com.wishare.finance.infrastructure.bizlog.LogContext;
import com.wishare.finance.infrastructure.bizlog.LogObject;
import com.wishare.finance.infrastructure.conts.EnvConst;
import com.wishare.finance.infrastructure.remote.clients.base.OrgClient;
import com.wishare.finance.infrastructure.remote.clients.base.SpaceClient;
import com.wishare.finance.infrastructure.remote.enums.MessageCategoryEnum;
import com.wishare.finance.infrastructure.remote.enums.NuonuoInvoiceStatusEnum;
import com.wishare.finance.infrastructure.remote.enums.NuonuoRedApplyStatusEnum;
import com.wishare.finance.infrastructure.remote.fo.msg.NoticeCustomerD;
import com.wishare.finance.infrastructure.remote.fo.msg.NoticeCustomerItemDisplayD;
import com.wishare.finance.infrastructure.remote.vo.external.nuonuo.NuonuoRedApplyQueryV;
import com.wishare.finance.infrastructure.remote.vo.org.OrgTenantRv;
import com.wishare.finance.infrastructure.remote.vo.space.CommunityShortRV;
import com.wishare.finance.infrastructure.utils.AmountUtils;
import com.wishare.starter.Global;
import com.wishare.starter.beans.IdentityInfo;
import com.wishare.starter.consts.CacheConst;
import com.wishare.starter.exception.BizException;
import com.wishare.starter.helpers.RedisHelper;
import com.wishare.starter.utils.ThreadLocalUtil;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static com.wishare.finance.domains.invoicereceipt.consts.NuonuoConsts.ALREADY_RED_MSG;

/**
 * @author xujian
 * @date 2022/9/22
 * @Description:
 */
@Component
@Slf4j
public class InvoiceSchedule {

    @Value("${tenant.tag.name:#{null}}")
    private String tenantTagName;

    @Setter(onMethod_ = {@Autowired})
    private BillFacade billFacade;

    @Setter(onMethod_ = {@Autowired})
    private MsgFacade msgFacade;

    @Setter(onMethod_ = {@Autowired})
    private InvoiceExternalService invoiceExternalService;

    @Setter(onMethod_ = {@Autowired})
    private MessageSend messageSend;

    @Setter(onMethod_ = {@Autowired})
    private InvoiceDomainService invoiceDomainService;

    @Setter(onMethod_ = {@Autowired})
    private InvoiceRepository invoiceRepository;

    @Setter(onMethod_ = {@Autowired})
    private InvoiceRedApplyRepository invoiceRedApplyRepository;

    @Setter(onMethod_ = {@Autowired})
    private InvoiceReceiptRepository invoiceReceiptRepository;

    @Setter(onMethod_ = {@Autowired})
    private InvoiceReceiptDetailRepository invoiceReceiptDetailRepository;

    @Setter(onMethod_ = {@Autowired})
    private BillDomainServiceOld billDomainServiceOld;

    private final String YUAN_YANG = "yuanyang";

    @Setter(onMethod_ = {@Autowired})
    private OrgClient orgClient;
    @Setter(onMethod_ = {@Autowired})
    private SpaceClient spaceClient;

    /**
     * 最大重试次数
     */
    private final int retryMax = 6;

    @XxlJob("invoiceResultHandler")
    public void invoiceResultHandler() {
        log.info("开票中状态处理开始");
        //查询开票中的发票
        DateTime dateTime = DateUtil.offsetMonth(DateUtil.date(), -3);
        // 处理xxlJob的参数
        dateTime = getDateTimeByXxlJopParam(dateTime);
        List<InvoiceE> invoiceES = invoiceRepository.getInvoiceByState(
                Lists.newArrayList(InvoiceReceiptStateEnum.开票中.getCode(), InvoiceReceiptStateEnum.开票成功签章失败.getCode()), dateTime);
        if (CollectionUtils.isNotEmpty(invoiceES)) {
            for (InvoiceE invoiceE : invoiceES) {
                //补充身份标识
                IdentityInfo identityInfo = new IdentityInfo();
                identityInfo.setTenantId(invoiceE.getTenantId());
                ThreadLocalUtil.set("IdentityInfo", identityInfo);
                InvoiceReceiptE invoiceReceiptE = invoiceReceiptRepository.getById(invoiceE.getInvoiceReceiptId());
                List<QueryInvoiceResultV> invoiceResultRVS = null;
                //获取开票结果
                try {
                    if (invoiceReceiptE.getType() == InvoiceLineEnum.增值税电子发票.getCode()) {
                        invoiceResultRVS = invoiceExternalService.queryInvoiceResult(invoiceE.getTenantId(), invoiceE.getInvoiceSerialNum(), invoiceE.getSalerTaxNum(),invoiceE.getInvoiceReceiptId());
                    } else if ((invoiceReceiptE.getType().equals(InvoiceLineEnum.全电普票.getCode()) || invoiceReceiptE.getType().equals(InvoiceLineEnum.全电专票.getCode())) && StringUtils.isNotEmpty(invoiceE.getInvoiceSerialNum())) {
                        // 如果发票没有流水号说明还在红字确认单申请中，先跳过查询
                        invoiceResultRVS = invoiceExternalService.opeMplatformQueryInvoiceResult(invoiceE.getTenantId(), invoiceE.getInvoiceSerialNum(), invoiceE.getSalerTaxNum(), invoiceE.getInvoiceReceiptId(),invoiceE);
                    } else if (invoiceReceiptE.getType() == InvoiceLineEnum.增值税专用发票.getCode() && EnvConst.FANGYUAN.equals(EnvData.config)) {
                        invoiceResultRVS = invoiceExternalService.queryPaperInvoiceResult(invoiceE.getTenantId(), invoiceE.getInvoiceSerialNum(), invoiceE.getSalerTaxNum(),invoiceE.getInvoiceReceiptId());
                    }
                } catch (Exception e) {
                    XxlJobHelper.log("[获取诺诺发票结果]-[异常]-[{},异常发票id{}]", JSON.toJSON(e), invoiceReceiptE.getId());
                }

                //根据开票结果更新数据库信息
                if (invoiceResultRVS != null) {
                    try {
                        Global.ac.getBean(InvoiceAppService.class).updateInvoiceState(invoiceE, invoiceResultRVS, invoiceReceiptE);
                    } catch (Exception e) {
                        XxlJobHelper.log("[更新发票结果]-[异常]-[{},异常发票id{}]", JSON.toJSON(e), invoiceReceiptE.getId());
                    }
                }
            }
        }
        log.info("开票中状态处理结束");
    }


    public void reHandleInvoice(InvoiceE invoiceE) {
        IdentityInfo identityInfo = new IdentityInfo();
        identityInfo.setTenantId(invoiceE.getTenantId());
        ThreadLocalUtil.set("IdentityInfo", identityInfo);
        InvoiceReceiptE invoiceReceiptE = invoiceReceiptRepository.getById(invoiceE.getInvoiceReceiptId());
        if (!InvoiceReceiptStateEnum.开票失败.getCode().equals(invoiceReceiptE.getState())) {
            throw BizException.throw400("仅支持处理开票失败的发票");
        }
        List<InvoiceReceiptDetailE> details = invoiceReceiptDetailRepository.getByInvoiceReceipt(invoiceE.getInvoiceReceiptId());
        List<Long> billIds = details.stream().map(InvoiceReceiptDetailE::getBillId).collect(Collectors.toList());
        BillSimpleInfoV billSimpleInfoV = billDomainServiceOld.getBillSimpleInfoV(billIds, details.get(0).getBillType(), invoiceReceiptE.getCommunityId());
        Long canInvoiceAmount = billSimpleInfoV.getCanInvoiceAmount();
        Long priceTaxAmount = invoiceReceiptE.getPriceTaxAmount();
        if (canInvoiceAmount < priceTaxAmount) {
            throw BizException.throw400("发票金额" + priceTaxAmount + ", 可开票金额" + canInvoiceAmount + ",可开票金额不足");
        }
        List<QueryInvoiceResultV> invoiceResultRVS = null;
        //获取开票结果
        if (InvoiceLineEnum.增值税电子发票.getCode().equals(invoiceReceiptE.getType())) {
            invoiceResultRVS = invoiceExternalService.queryInvoiceResult(invoiceE.getTenantId(), invoiceE.getInvoiceSerialNum(), invoiceE.getSalerTaxNum(),invoiceE.getInvoiceReceiptId());
        } else if ((invoiceReceiptE.getType().equals(InvoiceLineEnum.全电普票.getCode()) || invoiceReceiptE.getType().equals(InvoiceLineEnum.全电专票.getCode())) && StringUtils.isNotEmpty(invoiceE.getInvoiceSerialNum())) {
            // 如果发票没有流水号说明还在红字确认单申请中，先跳过查询
            invoiceResultRVS = invoiceExternalService.opeMplatformQueryInvoiceResult(invoiceE.getTenantId(), invoiceE.getInvoiceSerialNum(), invoiceE.getSalerTaxNum(), invoiceE.getInvoiceReceiptId(),invoiceE);
        }
        //根据开票结果更新数据库信息
        if (invoiceResultRVS != null) {
            Integer invoiceState = NuonuoInvoiceStatusEnum.valueOfByCode(Integer.valueOf(invoiceResultRVS.get(0).getStatus())).getInvoicingState();
            if (InvoiceReceiptStateEnum.开票成功.getCode().equals(invoiceState)) {
                Global.ac.getBean(InvoiceAppService.class).updateInvoiceState(invoiceE, invoiceResultRVS, invoiceReceiptE);
            }
        }
    }

    /**
     * 轮询处理申请中的红字确认单
     */
    @XxlJob("invoiceRedApplyHandler")
    public void invoiceRedApplyHandler() {
        DateTime dateTime = DateUtil.offsetMonth(DateUtil.date(), -3);
        // 处理xxlJob的参数
        dateTime = getDateTimeByXxlJopParam(dateTime);

        List<InvoiceRedApplyE> applyList = invoiceRedApplyRepository.getApplyByStatus(Lists.newArrayList(NuonuoRedApplyStatusEnum.申请中),dateTime);
        if (CollectionUtils.isNotEmpty(applyList)) {
            applyList.forEach(apply -> {
                //补充身份标识
                IdentityInfo identityInfo = new IdentityInfo();
                identityInfo.setTenantId(apply.getTenantId());
                ThreadLocalUtil.set("IdentityInfo", identityInfo);
                try {
                    InvoiceE invoiceE = invoiceRepository.getByInvoiceReceiptId(apply.getInvoiceReceiptId());
                    List<NuonuoRedApplyQueryV> queryList = invoiceExternalService.electronInvoiceRedApplyQuery(apply, invoiceE);
                    NuonuoRedApplyQueryV queryV = queryList.get(0);
                    if(StringUtils.isBlank(queryV.getBillStatus())){
                        return;
                    }
                    NuonuoRedApplyStatusEnum applyStatusEnum = NuonuoRedApplyStatusEnum.valueOfByCode(queryV.getBillStatus());
                    switch (applyStatusEnum) {
                        case 无需确认:
                        case 购销双方已确认:
                            //临港使用，条件符合时重新查询
                            if("N".equals(queryV.getOpenStatus())){
                                return;
                            }
                            redReversal(invoiceE, queryV, apply);
                            break;
                        case 作废_确认后撤销:
                        case 作废_购方录入销方否认:
                        case 作废_销方录入购方否认:
                        case 申请失败:
                        case 作废_发起方已撤销:
                        case 作废_超72小时未确认:
                            handleFailResult(invoiceE, applyStatusEnum, apply,queryV.getErrMsg());
                            break;
                    }
                } catch (Exception e) {
                    log.error("查询红字确认单结果异常", e);
                    XxlJobHelper.log("[查询诺诺红字确认单结果]-[异常]-[{},异常单号id{}]", JSON.toJSON(e), apply.getId());
                }
            });
        }
    }

    /** 例：{"startTime":"2024-05-01 12:00:00"}
     * @param dateTime yyyy-MM-dd hh:mm:ss 字符串
     * @return
     */
    private static DateTime getDateTimeByXxlJopParam(DateTime dateTime) {
        try {
            String param = XxlJobHelper.getJobParam();
            XxlJobHelper.log("轮询处理申请中的红字确认单-xxlJob参数获取...[{}]",param);
            log.info("轮询处理申请中的红字确认单-xxlJob参数获取...：{}",param);
            if (StrUtil.isNotBlank(param)) {
                JSONObject params = JSONObject.parseObject(param);
                if (params != null && params.containsKey("startTime")) {
                    String paramsDate = params.getString("startTime");
                    dateTime = DateTime.of(paramsDate,"yyyy-MM-dd HH:mm:ss");
                }
            }
        }catch (Exception e){
            log.error("invoiceRedApplyHandler 轮询处理申请中的红字确认单-获取xxlJob参数异常，默认获取3个月内数据进行处理:{}",e.getMessage());
        }
        return dateTime;
    }

    /**
     * 发起红冲
     * @param invoiceE
     * @param queryV
     */
    @Transactional(rollbackFor = Exception.class)
    public void redReversal(InvoiceE invoiceE, NuonuoRedApplyQueryV queryV, InvoiceRedApplyE applyE) {
        String invoiceSerialNum = " ";
        try {
            // 处理百望已有红票
            if (queryV.getHasRedInvoice()) {
                this.handleHasRedInvoice(invoiceE, queryV, applyE);
                return;
            }
            if(EnvConst.LINGANG.equals(EnvData.config)){
                invoiceE.setInvoiceSerialNum(applyE.getInvoiceSerialNum());
            }
            invoiceSerialNum = invoiceExternalService.electronInvoiceRed(invoiceE, queryV);
        } catch (BizException bizException) {
            // 可能因为红冲超时导致之前已经成功红冲，会返回业务异常：确认单为已开具状态时不支持冲红
            if (!StringUtils.contains(bizException.getMessage(), ALREADY_RED_MSG)) {
                throw bizException;
            }
        }
        applyE.setStatus(queryV.getBillStatus());
        applyE.setBillUuid(queryV.getBillUuid());
        applyE.setDetail(queryV.getDetail());
        applyE.setRequestStatus(queryV.getRequestStatus());
        invoiceRedApplyRepository.updateById(applyE);
        invoiceE.setInvoiceSerialNum(invoiceSerialNum);
        invoiceRepository.updateById(invoiceE);
        invoiceReceiptRepository.setInvoiceReceiptState(invoiceE.getInvoiceReceiptId(), InvoiceReceiptStateEnum.开票中);
        // 设置原发票为红冲中
        invoiceReceiptRepository.setInvoiceReceiptState(invoiceE.getBlueInvoiceReceiptId(), InvoiceReceiptStateEnum.红冲中);
    }

    private void handleHasRedInvoice(InvoiceE invoiceE, NuonuoRedApplyQueryV queryV, InvoiceRedApplyE applyE) {
        InvoiceReceiptE invoiceReceiptE = invoiceReceiptRepository.getById(invoiceE.getInvoiceReceiptId());
        try {
            invoiceE.setInvoiceNo(queryV.getInvoiceNo());
            // 置空发票流水号，原来存储确认单流水号
            invoiceE.setInvoiceSerialNum("");
            List<QueryInvoiceResultV> invoiceResultVList = invoiceExternalService.opeMplatformQueryInvoiceResult("", "", invoiceE.getSalerTaxNum(), 0L, invoiceE);
            if (CollectionUtils.isEmpty(invoiceResultVList)) return;
            Global.ac.getBean(InvoiceAppService.class).updateInvoiceState(invoiceE, invoiceResultVList, invoiceReceiptE);
            // 更新申请单信息
            applyE.setStatus(queryV.getBillStatus());
            applyE.setBillUuid(queryV.getBillUuid());
            applyE.setDetail(queryV.getDetail());
            applyE.setRequestStatus(queryV.getRequestStatus());
            invoiceRedApplyRepository.updateById(applyE);
        } catch (Exception e) {
            XxlJobHelper.log("[更新发票结果]-[异常]-[{},异常发票id{}]", JSON.toJSON(e), invoiceReceiptE.getId());
            throw e;
        }
    }

    /**
     * 红字申请单申请失败处理
     *
     * @param invoiceE
     * @param applyStatusEnum
     * @param errMsg 可传空
     */
    @Transactional(rollbackFor = Exception.class)
    public void handleFailResult(InvoiceE invoiceE, NuonuoRedApplyStatusEnum applyStatusEnum, InvoiceRedApplyE applyE, String errMsg) {
        applyE.setStatus(applyStatusEnum.getCode());
        invoiceRedApplyRepository.updateById(applyE);
        invoiceReceiptRepository.setInvoiceReceiptState(invoiceE.getInvoiceReceiptId(), InvoiceReceiptStateEnum.开票失败);
        // 设置原来的发票恢复正常
        invoiceReceiptRepository.setInvoiceReceiptState(invoiceE.getBlueInvoiceReceiptId(), InvoiceReceiptStateEnum.开票成功);
        InvoiceReceiptE invoiceReceiptE = invoiceReceiptRepository.getById(invoiceE.getInvoiceReceiptId());
        LambdaUpdateWrapper<InvoiceE> invoiceUpdateWrapper = new LambdaUpdateWrapper<>();
        invoiceUpdateWrapper.set(InvoiceE::getFailReason, applyStatusEnum.getDes() + ": " + (StringUtils.isBlank(errMsg) ? "" : errMsg));
        invoiceUpdateWrapper.eq(InvoiceE::getInvoiceReceiptId, invoiceE.getInvoiceReceiptId());
        invoiceRepository.update(invoiceUpdateWrapper);
        // 设置原来的账单恢复正常
        List<InvoiceReceiptDetailE> invoiceReceiptDetailEs = invoiceReceiptDetailRepository.queryByInvoiceReceiptIds(Lists.newArrayList(invoiceE.getInvoiceReceiptId()));
        billFacade.handleBillStateFinishInvoice(invoiceReceiptDetailEs, false, invoiceReceiptE.getCommunityId());
    }



    /**
     * 异步推送开票信息
     *
     * @param invoiceReceiptE
     * @param invoiceE
     * @param invoiceReceiptDetailEList
     */
    @Retryable(value = {Exception.class}, maxAttempts = 3, backoff = @Backoff(delay = 5000))
    public void asynPushCallBack(InvoiceReceiptE invoiceReceiptE, InvoiceE invoiceE, List<InvoiceReceiptDetailE> invoiceReceiptDetailEList) {
        //获取回调地址
        String callBackUrl = invoiceReceiptE.getCallBackUrl();
        if (StringUtils.isNotBlank(callBackUrl)) {
            ExecutorService singleExecutor = Executors.newFixedThreadPool(1);
            singleExecutor.execute(() -> {
                List<BillInvoiceDto> billInvoiceDtos = Lists.newArrayList();
                for (InvoiceReceiptDetailE invoiceReceiptDetailE : invoiceReceiptDetailEList) {
                    BillInvoiceDto billInvoiceDto = new BillInvoiceDto();
                    billInvoiceDto.setId(invoiceReceiptE.getId());
                    billInvoiceDto.setBillingTime(invoiceReceiptE.getBillingTime());
                    billInvoiceDto.setBuyerTaxNum(invoiceE.getBuyerTaxNum());
                    billInvoiceDto.setClerk(invoiceReceiptE.getClerk());
                    billInvoiceDto.setCustomerId(invoiceReceiptE.getCustomerId());
                    billInvoiceDto.setCustomerName(invoiceReceiptE.getCustomerName());
                    billInvoiceDto.setCustomerPhone(invoiceReceiptE.getCustomerPhone());
                    billInvoiceDto.setPriceTaxAmount(invoiceReceiptDetailE.getPriceTaxAmount());
                    billInvoiceDto.setInvoiceCode(invoiceE.getInvoiceCode());
                    billInvoiceDto.setInvoiceNo(invoiceE.getInvoiceNo());
                    billInvoiceDto.setInvoiceTitleType(invoiceE.getInvoiceTitleType());
                    billInvoiceDto.setInvoiceType(invoiceE.getInvoiceType());
                    billInvoiceDto.setSalerTaxNum(invoiceE.getSalerTaxNum());
                    billInvoiceDto.setSalerTel(invoiceE.getSalerTel());
                    billInvoiceDto.setSalerAddress(invoiceE.getSalerAddress());
                    billInvoiceDto.setState(invoiceReceiptE.getState());
                    billInvoiceDto.setNuonuoUrl(invoiceE.getNuonuoUrl());
                    billInvoiceDto.setFailReason(invoiceE.getFailReason());
                    billInvoiceDto.setBillId(invoiceReceiptDetailE.getBillId());
                    billInvoiceDtos.add(billInvoiceDto);
                }
                try {
                    String result = HttpUtil.post(callBackUrl, JSON.toJSONString(billInvoiceDtos));
                    if (StringUtils.isNotBlank(result)) {
                        CallBackV callBackV = JSON.parseObject(result, CallBackV.class);
                        if (!callBackV.getResult()) {
                            throw BizException.throw400("调用第三方返回失败,发票主表id=" + invoiceReceiptE.getId());
                        }
                    }
                } catch (Exception e) {
                    log.error("推送开票信息异常：{}", e);
                    log.error("调用第三方返回失败,发票主表id=" + invoiceReceiptE.getId());
                    throw e;
                }

            });
            singleExecutor.shutdown();
        }

    }

    /**
     * 处理蓝票
     *
     * @param invoiceE
     * @param invoiceReceiptE
     * @param invoiceRes
     * @param invoiceState
     */
    public void handleBlueInvoice(InvoiceE invoiceE, InvoiceReceiptE invoiceReceiptE, List<QueryInvoiceResultV> invoiceRes, Integer invoiceState) {
        invoiceDomainService.updateInvoiceInfo(invoiceE, invoiceReceiptE, invoiceRes, invoiceState);
    }

    /**
     * 处理红票
     *
     * @param invoiceE
     * @param invoiceReceiptE
     * @param invoiceRes
     * @param invoiceState
     */
    public void handleRedInvoice(InvoiceE invoiceE, InvoiceReceiptE invoiceReceiptE, List<QueryInvoiceResultV> invoiceRes, Integer invoiceState) {
        //获取原蓝票数据
        InvoiceE invoiceEBeforeBlue = invoiceRepository.getByBlueInvoiceReceiptId(invoiceE.getBlueInvoiceReceiptId()).get(0);
        if (invoiceState == InvoiceReceiptStateEnum.开票成功.getCode()) {
            //蓝票开票金额
            List<InvoiceReceiptDetailE> invoiceReceiptDetailEBlueList = invoiceReceiptDetailRepository.getBillIdsByInvoiceReceiptId(invoiceEBeforeBlue.getInvoiceReceiptId());
            Long invoiceAmountBlue = invoiceReceiptDetailEBlueList.stream().mapToLong(InvoiceReceiptDetailE::getInvoiceAmount).sum();
            //红票开票金额
            List<InvoiceReceiptDetailE> invoiceReceiptDetailRedEList = invoiceReceiptDetailRepository.queryByInvoiceReceiptIds(Lists.newArrayList(invoiceE.getInvoiceReceiptId()));
            Long invoiceAmountRed = invoiceReceiptDetailRedEList.stream().mapToLong(InvoiceReceiptDetailE::getInvoiceAmount).sum();
            //根据蓝票获取已经红冲的金额
            Long invoiceAmountRedBefore = getInvoiceAmountRedBefore(invoiceEBeforeBlue.getInvoiceReceiptId());

            //原蓝票金额 + 当前红冲金额 + 已经红冲的金额
            Long redAmount = invoiceAmountBlue.longValue() + invoiceAmountRed.longValue() + invoiceAmountRedBefore.longValue();
            //更新原蓝票为已红冲或者部分红冲
            if (redAmount.longValue() == 0) {
                invoiceReceiptRepository.setInvoiceReceiptState(invoiceEBeforeBlue.getInvoiceReceiptId(), InvoiceReceiptStateEnum.已红冲);
            } else {
                invoiceReceiptRepository.setInvoiceReceiptState(invoiceEBeforeBlue.getInvoiceReceiptId(), InvoiceReceiptStateEnum.部分红冲);
            }
        } else {
            //开票失败，将原蓝票状态置为开票成功
            invoiceReceiptRepository.setInvoiceReceiptState(invoiceEBeforeBlue.getInvoiceReceiptId(), InvoiceReceiptStateEnum.开票成功);
        }
        invoiceDomainService.updateInvoiceInfo(invoiceE, invoiceReceiptE, invoiceRes, invoiceState);
    }

    /**
     * 根据蓝票id获取已经红冲的红票总金额
     *
     * @param invoiceReceiptId
     * @return
     */
    private Long getInvoiceAmountRedBefore(Long invoiceReceiptId) {
        List<InvoiceInfoDto> redInvoiceInfoBefores = invoiceRepository.getRedInvoiceInfo(invoiceReceiptId);
        Long invoiceAmountRedBeforeSum = 0L;
        if (CollectionUtils.isNotEmpty(redInvoiceInfoBefores)) {
            for (InvoiceInfoDto redInvoiceInfoBefore : redInvoiceInfoBefores) {
                List<InvoiceReceiptDetailE> invoiceReceiptDetailES = redInvoiceInfoBefore.getInvoiceReceiptDetailES();
                long nvoiceAmountRedBefore = invoiceReceiptDetailES.stream().mapToLong(InvoiceReceiptDetailE::getInvoiceAmount).sum();
                invoiceAmountRedBeforeSum = invoiceAmountRedBeforeSum + nvoiceAmountRedBefore;
            }
        }
        return invoiceAmountRedBeforeSum;
    }

    /**
     * 通知财务中心
     */
    public void handleResToAmpFinance(InvoiceReceiptE invoiceReceiptE, List<InvoiceReceiptDetailE> invoiceReceiptDetailEList, Integer invoiceState) {
        try {
            Boolean success = null;
            if (invoiceState == InvoiceReceiptStateEnum.开票成功.getCode()) {
                success = true;
            } else if (invoiceState == InvoiceReceiptStateEnum.开票失败.getCode() || invoiceState == InvoiceReceiptStateEnum.开票成功签章失败.getCode()) {
                success = false;
            }
            billFacade.handleBillStateFinishInvoice(invoiceReceiptDetailEList, success,invoiceReceiptE.getCommunityId());
            if (Boolean.TRUE.equals(success)) {
                //开票成功记日志
                if (CollectionUtils.isNotEmpty(invoiceReceiptDetailEList)) {
                    Map<Long, List<InvoiceReceiptDetailE>> detailMap = invoiceReceiptDetailEList.stream().collect(Collectors.groupingBy(InvoiceReceiptDetailE::getBillId));
                    detailMap.forEach((billId, details) -> {
                        long invoiceAmount = details.stream().mapToLong(InvoiceReceiptDetailE::getInvoiceAmount).sum();
                        BizLog.normal(String.valueOf(billId), LogContext.getOperator(), LogObject.账单, LogAction.开票,
                                new Content().option(new ContentOption<>(new PlainTextDataItem("发票号：" + invoiceReceiptE.getInvoiceReceiptNo(), true)))
                                        .option(new ContentOption<>(new PlainTextDataItem("开票金额：", false)))
                                        .option(new ContentOption<>(new PlainTextDataItem(AmountUtils.toStringAmount(invoiceAmount), false), OptionStyle.normal()))
                                        .option(new ContentOption<>(new PlainTextDataItem("元", false))));

                    });
                }
            }
        } catch (Exception e) {
            List<Long> billIds = invoiceReceiptDetailEList.stream().map(InvoiceReceiptDetailE::getBillId).collect(Collectors.toList());
            log.error("通知财务中心修改账单状态", e);
            XxlJobHelper.log("[通知财务中心修改账单状态]-[异常]-[{},{}]", "异常账单ids: " + JSON.toJSONString(billIds), e);
            throw e;
        }
    }

    /**
     * 根据所选择推送方式进行推送（异步）
     *
     * @param invoiceState
     * @param invoiceE
     * @param invoiceReceiptE
     * @param invoiceReceiptDetailEList
     * @param queryInvoiceResultRVList
     */
    public void pushModeMessage(Integer invoiceState, InvoiceE invoiceE, InvoiceReceiptE invoiceReceiptE, List<InvoiceReceiptDetailE> invoiceReceiptDetailEList, List<QueryInvoiceResultV> queryInvoiceResultRVList) {
        if (invoiceState == InvoiceReceiptStateEnum.开票成功.getCode() && (invoiceE.getPushState() == PushStateEnum.未推送.getCode() || invoiceE.getPushState() == null)) {
            if (invoiceReceiptE.getSysSource() == SysSourceEnum.收费系统.getCode()) {
                ExecutorService singleExecutor = Executors.newFixedThreadPool(1);
                singleExecutor.execute(() -> {
                    pushMessage(invoiceE, invoiceReceiptE, invoiceReceiptDetailEList, queryInvoiceResultRVList);
                });
                singleExecutor.shutdown();
            }
        }
    }

    /**
     * 开始推送
     *
     * @param invoiceE
     * @param invoiceReceiptE
     * @param invoiceReceiptDetailEList
     * @param queryInvoiceResultRVList
     */
    private void pushMessage(InvoiceE invoiceE, InvoiceReceiptE invoiceReceiptE, List<InvoiceReceiptDetailE> invoiceReceiptDetailEList, List<QueryInvoiceResultV> queryInvoiceResultRVList) {
        List<Integer> pushModes = JSON.parseArray(invoiceE.getPushMode(), Integer.class);
        for (Integer mode : pushModes) {
            switch (PushModeEnum.valueOfByCode(mode)) {
                case 不推送:
                    break;
                case 邮箱:
                    msgFacade.sendEmail(invoiceE.getEmail(), handleSubject(invoiceE, queryInvoiceResultRVList), handleChargeContent(invoiceE, invoiceReceiptE, invoiceReceiptDetailEList, queryInvoiceResultRVList), null);
                    break;
                case 手机:
                    if (EnvData.msgFlag) {
                        sendSms(invoiceE, invoiceReceiptE, invoiceReceiptDetailEList, queryInvoiceResultRVList);
                    } else {
                        log.info("InvoiceSchedule.pushMessage:{}", "不发送短信收据");
                    }
                    break;
                case 站内信:
                    //发送站内信（除远洋都需要）
                    if(!YUAN_YANG.equals(EnvData.config)){
                        //由于原本发票存储客户id逻辑有问题，故此处根据发票收据明细详情账单id、类型关联付款人id作为推送用户id
                        List<Long> billIds = new ArrayList<>();
                        billIds.add(invoiceReceiptDetailEList.get(0).getBillId());
                        log.info("账单id:{}，账单类型：{}",invoiceReceiptDetailEList.get(0).getBillId(),invoiceReceiptDetailEList.get(0).getBillType());
                        List<BillOjv> billOjvs = billFacade.getBillInfo(billIds, invoiceReceiptDetailEList.get(0).getBillType(), invoiceReceiptE.getCommunityId());
                        log.info("查询出账单集合为{}",JSON.toJSONString(billOjvs));
                        if (CollectionUtils.isEmpty(billOjvs)){
                            throw BizException.throw400("未查询到对应账单");
                        }
                        log.info("发送支付成功站内信,用户:{}",billOjvs.get(0).getPayerId());
                        sendTransactionMsg4C(billOjvs.get(0).getPayerId(),
                                invoiceReceiptE.getPriceTaxAmount(),
                                invoiceReceiptDetailEList,
                                invoiceE.getInvoiceReceiptId());
                    }
                    break;
            }
        }
        invoiceE.setPushState(PushStateEnum.已推送.getCode());
        invoiceRepository.updateById(invoiceE);
    }

    /**
     * 发送开票信息-C端
     */
    private void sendTransactionMsg4C(String payerId, Long amount, List<InvoiceReceiptDetailE> invoiceReceiptDetailEList, Long gatherBillId){
        try {
            //分转换为元
            double amountValue = amount/100.0;
            BigDecimal bigDecimal = new BigDecimal(amountValue);
            String format = new DecimalFormat(",###.00").format(bigDecimal);
            if(format.startsWith(".")){
                format = "0" + format;
            }
            NoticeCustomerD noticeCustomerD = new NoticeCustomerD();
            if(gatherBillId == -1){
                log.info("支付成功生成收款单返回id为空,payerId:{}",payerId);
            }else{
                Map<String,String> ext = new HashMap<>();
                String URL = MessageSend.INVOICE_DETAIL_URL + gatherBillId;
                ext.put("jumpUrl",URL);
                ext.put("tenantId",invoiceReceiptDetailEList.get(0).getTenantId());
                noticeCustomerD.setExt(JSONObject.toJSONString(ext));
            }

            String remarkValue = "";
            if (CollectionUtils.isNotEmpty(invoiceReceiptDetailEList)){
                if (invoiceReceiptDetailEList.size() == 1 ){
                    remarkValue = "开票商品：" + invoiceReceiptDetailEList.get(0).getGoodsName();
                }else {
                    remarkValue = "开票商品：" + invoiceReceiptDetailEList.get(0).getGoodsName()+"...";
                }
            }
            List<NoticeCustomerItemDisplayD> list = new ArrayList<>();
            list.add(new NoticeCustomerItemDisplayD("票据金额", "￥" + format, 1));
            list.add(new NoticeCustomerItemDisplayD("交易类型", "增值税电子发票", 2));
            list.add(new NoticeCustomerItemDisplayD("备注", remarkValue, 2));
            noticeCustomerD.setDisplayItems(list);
            noticeCustomerD.setUserId(List.of(payerId));
            noticeCustomerD.setNoticeCardType(1);
            noticeCustomerD.setNoticeCardTypeName("文本通知");
            noticeCustomerD.setCategory(MessageCategoryEnum.电子票据.name());
            noticeCustomerD.setCardCode(MessageSend.MSG_CATEGORY_CODE);
            noticeCustomerD.setModelCode(MessageSend.MSG_CATEGORY_CODE);
            noticeCustomerD.setModelCodeName(MessageSend.MSG_CATEGORY_CODE_NAME);
            noticeCustomerD.setIcon(MessageSend.ICON);
            messageSend.sendMessageToCustomer(noticeCustomerD);
        }catch (Exception e){
            log.info("开票发送站内信失败,billId:{},payerId:{}",gatherBillId,payerId);
        }

    }

    /**
     * 发送短信
     */
    private void sendSms(InvoiceE invoiceE, InvoiceReceiptE invoiceReceiptE, List<InvoiceReceiptDetailE> invoiceReceiptDetailEList, List<QueryInvoiceResultV> queryInvoiceResultRVList) {
        //roomName(多个拼接)
        List<BillOjv> billInfoList = new ArrayList<>();
        invoiceReceiptDetailEList.stream().collect(Collectors.groupingBy(InvoiceReceiptDetailE::getBillType)).forEach((billType, details) -> {
            billInfoList.addAll(billFacade.getBillInfo(details.stream().map(InvoiceReceiptDetailE::getBillId).collect(
                    Collectors.toList()), billType, invoiceReceiptE.getCommunityId()));
        });
        List<String> roomNameList = Lists.newArrayList();
        List<String> communityNameList = Lists.newArrayList();
        for (BillOjv billOjv : billInfoList) {
            roomNameList.add(billOjv.getRoomName());
            communityNameList.add(billOjv.getCommunityName());
        }
        String roomName = "";
        if (CollectionUtils.isNotEmpty(roomNameList)) {
            roomNameList = roomNameList.stream().distinct().collect(Collectors.toList());
            roomName = StringUtils.join(roomNameList, ",");
        }
        String tenantId = invoiceE.getTenantId();
        OrgTenantRv orgTenantRv = orgClient.tenantGetById(tenantId);
        String shortName = orgTenantRv.getShortName() == null ? "" : orgTenantRv.getShortName();
        CommunityShortRV communityInfo = spaceClient.getCommunityInfo(invoiceReceiptE.getCommunityId());
        String contactsPhone = communityInfo == null ? "" : communityInfo.getContactsPhone();
        //多张发票推送多条
        for (QueryInvoiceResultV invoiceRes : queryInvoiceResultRVList) {
            msgFacade.smsInvoice(invoiceE.getBuyerPhone(), shortName, roomName, invoiceRes.getPdfUrl(), contactsPhone);
        }
    }

    /**
     * 处理邮件主题
     *
     * @param invoiceE
     * @param invoiceResultRVList
     * @return
     */
    private String handleSubject(InvoiceE invoiceE, List<QueryInvoiceResultV> invoiceResultRVList) {
        List<String> invoiceNoList = invoiceResultRVList.stream().map(QueryInvoiceResultV::getInvoiceNo).collect(Collectors.toList());
        String invoiceNoListStr = StringUtils.join(invoiceNoList, ",");
        String subject = "您收到 " + invoiceResultRVList.size() + "张企业税号为【" + invoiceE.getSalerTaxNum() + "】开具的发票【发票号码：" + invoiceNoListStr + "】";
        return subject;
    }

    /**
     * 处理内容
     *
     * @param invoiceE
     * @param invoiceReceiptE
     * @param invoiceReceiptDetailEList
     * @param queryInvoiceResultRVList
     * @return
     */
    private String handleChargeContent(InvoiceE invoiceE, InvoiceReceiptE invoiceReceiptE, List<InvoiceReceiptDetailE> invoiceReceiptDetailEList, List<QueryInvoiceResultV> queryInvoiceResultRVList) {
        if (StringUtils.isNotBlank(tenantTagName) && tenantTagName.equalsIgnoreCase("yuanyang")) {
            //远洋指定内容
            return handleYuanyangContent(invoiceReceiptE, invoiceReceiptDetailEList, queryInvoiceResultRVList);
        }

        //慧享内容
        return handleHuixiangContent(invoiceReceiptE, invoiceReceiptDetailEList, queryInvoiceResultRVList);
    }

    /**
     * 慧享邮件正文内容
     *
     * @return
     */
    private String handleHuixiangContent(InvoiceReceiptE invoiceReceiptE, List<InvoiceReceiptDetailE> invoiceReceiptDetailEList, List<QueryInvoiceResultV> queryInvoiceResultRVList) {

        List<BillOjv> billInfoList = new ArrayList<>();
        invoiceReceiptDetailEList.stream().collect(Collectors.groupingBy(InvoiceReceiptDetailE::getBillType)).forEach((billType, details) -> {
            billInfoList.addAll(billFacade.getBillInfo(details.stream().map(InvoiceReceiptDetailE::getBillId).collect(
                    Collectors.toList()), billType, invoiceReceiptE.getCommunityId()));
        });
        String communityName = "";
        for (BillOjv billOjv : billInfoList) {
            communityName = communityName + "" + billOjv.getCommunityName();
        }

        StringBuilder invoiceStr = new StringBuilder();
        for (QueryInvoiceResultV invoiceRes : queryInvoiceResultRVList) {
            invoiceStr.append("发票代码: ").append(invoiceRes.getInvoiceCode()).append("  发票号码: ").append(invoiceRes.getInvoiceNo()).append(" ，您可以点击“ 电子普通发票下载“ （").append(invoiceRes.getPdfUrl()).append("）获取该发票文件，");
        }

        //获取租户简称
        String tenantShortName = getTenantShortName(invoiceReceiptE.getTenantId());

        String content = "尊敬的" + invoiceReceiptE.getCustomerName() + "：\n" +
                "\n" +
                tenantShortName + "已经为您开具" + "1" + "张电子普通发票，具体信息如下:\n" +
                "\n" +
                invoiceStr +
                "同时您也可以到物业服务中心或联系管家协助下载电子发票。\n" +
                "\n" +
                "电子普通发票是税务机关认可的有效收付款凭证，与纸质发票具有同等法律效力，可用于报销入账、售后维权等。\n" +
                "\n" +
                "感谢您对物业工作的支持与配合，祝生活愉快！\n" +
                "\n" +
                //项目名称
                communityName;
        return content;
    }

    /**
     * 根据租户id获取租户简称
     */
    private String getTenantShortName(String tenantId) {
        String tenantInfo = RedisHelper.getG(CacheConst.TENANT + tenantId);
        OrgTenantRv orgTenantRv = JSON.parseObject(tenantInfo, OrgTenantRv.class);
        if (orgTenantRv != null) {
            return orgTenantRv.getShortName();
        }
        return null;
    }

    /**
     * 远洋邮件正文内容
     */
    private String handleYuanyangContent(InvoiceReceiptE invoiceReceiptE, List<InvoiceReceiptDetailE> invoiceReceiptDetailEList, List<QueryInvoiceResultV> queryInvoiceResultRVList) {
        //roomName(多个拼接)
        List<BillOjv> billInfoList = new ArrayList<>();
        invoiceReceiptDetailEList.stream().collect(Collectors.groupingBy(InvoiceReceiptDetailE::getBillType)).forEach((billType, details) -> {
            billInfoList.addAll(billFacade.getBillInfo(details.stream().map(InvoiceReceiptDetailE::getBillId).collect(
                    Collectors.toList()), billType, invoiceReceiptE.getCommunityId()));
        });
        List<String> roomNameList = Lists.newArrayList();
        List<String> communityNameList = Lists.newArrayList();

        for (BillOjv billOjv : billInfoList) {
            roomNameList.add(billOjv.getRoomName());
            communityNameList.add(billOjv.getCommunityName());
        }
        String roomName = "";
        if (CollectionUtils.isNotEmpty(roomNameList)) {
            roomNameList = roomNameList.stream().distinct().collect(Collectors.toList());
            roomName = StringUtils.join(roomNameList, ",");
        }
        String communityName = "";
        if (CollectionUtils.isNotEmpty(communityNameList)) {
            communityNameList = communityNameList.stream().distinct().collect(Collectors.toList());
            communityName = StringUtils.join(communityNameList, ",");
        }

        StringBuilder invoiceStr = new StringBuilder();
        for (QueryInvoiceResultV invoiceRes : queryInvoiceResultRVList) {
            invoiceStr.append("发票代码: ").append(invoiceRes.getInvoiceCode()).append("  发票号码: ").append(invoiceRes.getInvoiceNo()).append(" ，您可以点击“ 电子普通发票下载“ （").append(invoiceRes.getPdfUrl()).append("）获取该发票文件，");
        }

        String content = "尊敬的" + roomName + "客户：\n" +
                "\n" +
                "远洋服务已经为您开具" + "1" + "张电子普通发票，具体信息如下:\n" +
                "\n" +
                invoiceStr +
                "同时您也可以到物业服务中心或联系管家协助下载电子发票。\n" +
                "\n" +
                "电子普通发票是税务机关认可的有效收付款凭证，与纸质发票具有同等法律效力，可用于报销入账、售后维权等。\n" +
                "\n" +
                "感谢您对物业工作的支持与配合，祝生活愉快！\n" +
                "\n" +
                //项目名称
                communityName;
        return content;
    }


    /**
     * 开票重试
     */
    private void invoiceRetryHandler(InvoiceE invoiceE) {
        String reNum = RedisHelper.get(invoiceE.getInvoiceSerialNum());
        int re = StringUtils.isBlank(reNum) ? 0 : Integer.valueOf(reNum);
        if (re <= retryMax) {
            invoiceExternalService.reInvoice(invoiceE.getTenantId(), invoiceE.getInvoiceSerialNum());
            RedisHelper.set(invoiceE.getInvoiceSerialNum(), String.valueOf(re));
            return;
        }
        //重试6次，开票失败，联系客服处理
        invoiceDomainService.updateInvoiceState(invoiceE, NuonuoInvoiceStatusEnum.开票失败.getInvoicingState());
    }
}
