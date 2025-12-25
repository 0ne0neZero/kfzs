package com.wishare.finance.apps.service.yuanyang;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Lists;
import com.wishare.finance.apis.common.FinanceConstants;
import com.wishare.finance.apps.model.bill.vo.TransactInfoV;
import com.wishare.finance.apps.model.yuanyang.servicedata.BpmProcessData;
import com.wishare.finance.apps.model.yuanyang.vo.BusinessSyncVoucherV;
import com.wishare.finance.domains.bill.entity.Payer;
import com.wishare.finance.domains.bill.entity.Payee;
import com.wishare.finance.domains.bill.entity.Scene;
import com.wishare.finance.domains.bill.entity.TransactionBillOBV;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wishare.finance.apps.model.bill.fo.PayeeF;
import com.wishare.finance.apps.model.bill.fo.PayerF;
import com.wishare.finance.apps.model.bill.vo.BillTransactionV;
import com.wishare.finance.apps.model.bill.vo.ProcessVoucherResultV;
import com.wishare.finance.apps.model.yuanyang.fo.*;
import com.wishare.finance.apps.model.yuanyang.vo.ReimbursementSyncVoucherV;
import com.wishare.finance.domains.bill.command.TransactionCallbackCommand;
import com.wishare.finance.domains.bill.consts.enums.BillTransactInvoiceEnum;
import com.wishare.finance.domains.bill.consts.enums.BillTransactStateEnum;
import com.wishare.finance.domains.bill.consts.enums.BillTransactTypeEnum;
import com.wishare.finance.domains.bill.consts.enums.BillTransactVoucherStateEnum;
import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import com.wishare.finance.domains.bill.entity.*;
import com.wishare.finance.domains.bill.event.ReimburseCompletedEvent;
import com.wishare.finance.domains.bill.repository.TransactionOrderRepository;
import com.wishare.finance.domains.bill.service.BillPaymentDomainService;
import com.wishare.finance.domains.configure.accountbook.entity.AccountBookE;
import com.wishare.finance.domains.configure.accountbook.service.AccountBookDomainService;
import com.wishare.finance.domains.configure.businessunit.entity.BusinessUnitE;
import com.wishare.finance.domains.configure.businessunit.service.BusinessUnitDomainService;
import com.wishare.finance.domains.configure.chargeitem.entity.ChargeItemE;
import com.wishare.finance.domains.configure.chargeitem.entity.TaxRateE;
import com.wishare.finance.domains.configure.chargeitem.service.ChargeItemDomainService;
import com.wishare.finance.domains.configure.chargeitem.service.TaxRateDomainService;
import com.wishare.finance.domains.configure.organization.consts.enums.RecPayTypeEnum;
import com.wishare.finance.domains.configure.organization.entity.StatutoryBodyAccountE;
import com.wishare.finance.domains.configure.organization.service.StatutoryBodyAccountDomainService;
import com.wishare.finance.domains.configure.subject.consts.enums.AssisteItemTypeEnum;
import com.wishare.finance.domains.configure.subject.consts.enums.SubjectLeafStatusEnum;
import com.wishare.finance.domains.configure.subject.entity.AssisteItemOBV;
import com.wishare.finance.domains.configure.subject.entity.SubjectE;
import com.wishare.finance.domains.configure.subject.entity.SubjectMapUnitDetailE;
import com.wishare.finance.domains.configure.subject.repository.SubjectMapUnitDetailRepository;
import com.wishare.finance.domains.configure.subject.service.SubjectDomainService;
import com.wishare.finance.domains.invoicereceipt.aggregate.InvoiceA;
import com.wishare.finance.domains.invoicereceipt.consts.enums.*;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceE;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceReceiptDetailE;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceReceiptE;
import com.wishare.finance.domains.invoicereceipt.service.InvoiceDomainService;
import com.wishare.finance.domains.ncc.service.NccDomainService;
import com.wishare.finance.domains.report.enums.ChargeItemAttributeEnum;
import com.wishare.finance.domains.voucher.consts.enums.*;
import com.wishare.finance.domains.voucher.consts.enums.bpm.BusinessBillTypeEnum;
import com.wishare.finance.domains.voucher.consts.enums.bpm.ProcessStateEnum;
import com.wishare.finance.domains.voucher.entity.*;
import com.wishare.finance.domains.voucher.facade.VoucherFacade;
import com.wishare.finance.domains.voucher.service.BusinessBillDomainService;
import com.wishare.finance.domains.voucher.service.VoucherDomainService;
import com.wishare.finance.domains.voucher.service.VoucherTemplateDomainService;
import com.wishare.finance.domains.voucher.strategy.DefaultVoucherStrategy;
import com.wishare.finance.domains.voucher.strategy.bpm.assisteitem.BpmVoucherAssisteItemContext;
import com.wishare.finance.domains.voucher.strategy.core.VoucherBusinessBill;
import com.wishare.finance.domains.voucher.strategy.core.VoucherEntryLogicUtils;
import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.finance.infrastructure.event.EventLifecycle;
import com.wishare.finance.infrastructure.event.EventMessage;
import com.wishare.finance.infrastructure.remote.clients.base.ExternalClient;
import com.wishare.finance.infrastructure.remote.clients.base.OrgClient;
import com.wishare.finance.infrastructure.remote.fo.yonyounc.AddPersonF;
import com.wishare.finance.infrastructure.remote.fo.yonyounc.NccPersonJobF;
import com.wishare.finance.infrastructure.remote.vo.org.OrgFinanceRv;
import com.wishare.finance.infrastructure.support.ApiData;
import com.wishare.finance.infrastructure.support.thread.AppRunnable;
import com.wishare.finance.infrastructure.support.thread.AppThreadManager;
import com.wishare.finance.infrastructure.support.yuanyang.YuanYangSubjectProperties;
import com.wishare.finance.infrastructure.support.yuanyang.YuanYangTaxRateProperties;
import com.wishare.finance.infrastructure.utils.AmountUtils;
import com.wishare.finance.infrastructure.utils.DateTimeUtil;
import com.wishare.finance.infrastructure.utils.ErrorAssertUtils;
import com.wishare.finance.infrastructure.utils.ListUtils;
import com.wishare.finance.infrastructure.utils.NumberUtil;
import com.wishare.starter.Global;
import com.wishare.starter.beans.IdentityInfo;
import com.wishare.starter.exception.BizException;
import com.wishare.starter.helpers.RedisHelper;
import com.wishare.starter.utils.ErrorAssertUtil;
import com.wishare.starter.utils.ThreadLocalUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.wishare.finance.infrastructure.conts.ErrorMessage.PROCESS_PAY_SIZE_NOT_EQUAL;

/**
 * 远洋报销应用服务
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/3/15
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@EnableConfigurationProperties({YuanYangSubjectProperties.class, YuanYangTaxRateProperties.class})
@RefreshScope
public class ReimbursementAppService {

    @Value("${bpm.env:prod}")
    private String bpmEnv;

    public static final String REIMBURSE_CACHE_PREFIX = "reimburse:";

    private final OrgClient orgClient;
    private final TaxRateDomainService taxRateDomainService;
    private final VoucherDomainService voucherDomainService;
    private final InvoiceDomainService invoiceDomainService;
    private final SubjectDomainService subjectDomainService;
    private final VoucherTemplateDomainService voucherTemplateDomainService;
    private final ChargeItemDomainService chargeItemDomainService;
    private final BillPaymentDomainService billPaymentDomainService;
    private final AccountBookDomainService accountBookDomainService;
    private final YuanYangSubjectProperties yuanYangSubjectProperties;
    private final YuanYangTaxRateProperties yuanYangTaxRateProperties;
    private final VoucherFacade voucherFacade;

    private final BusinessUnitDomainService businessUnitDomainService;
    private final StatutoryBodyAccountDomainService statutoryBodyAccountDomainService;
    private final TransactionOrderRepository transactionOrderRepository;
    private final ExternalClient externalClient;
    private final BusinessBillDomainService businessBillDomainService;
    private final NccDomainService nccDomainService;

    private final SubjectMapUnitDetailRepository subjectMapUnitDetailRepository;

    @Value("${bpm.base_url:https://yj.oceanhomeplus.com}")
    private String BPM_URL;




    /**
     * 远洋BPM报销
     *
     * @param reimbursementF 报销流程入参
     * @return
     */
    @Transactional
    public BillTransactionV reimburse(ReimbursementF reimbursementF) {
        //SettleChannelEnum settleChannelEnum = SettleChannelEnum.valueOfByCode(reimbursementF.getPayChannel());
        //ErrorAssertUtil.isTrueThrow403(SettleChannelEnum.招商银企直连 == settleChannelEnum, ErrorMessage.PAYMENT_CHANNEL_NOT_SUPPORT, settleChannelEnum.getCode());
        //校验账簿
        AccountBookE accountBook = accountBookDomainService.getAccountBookByCode(reimbursementF.getAccountBookCode());
        ErrorAssertUtil.notNullThrow403(accountBook, ErrorMessage.ACCOUNT_BOOK_NO_EXISTS);
        //校验业务单元
        String businessUnitCode = accountBook.getCode().substring(0, accountBook.getCode().lastIndexOf("-"));
        BusinessUnitE businessUnit = businessUnitDomainService.geByCode(businessUnitCode);
        ErrorAssertUtil.notNullThrow403(businessUnit, ErrorMessage.BUSINESS_UNIT_NO_EXISTS);

        //校验业务单元下的银行账户


        ////根据付款账号获取现法定单位
        //List<StatutoryBodyAccountE> accounts = statutoryBodyAccountDomainService.getListByAccount(reimbursementF.getPayer().getPayerAccount());
        //ErrorAssertUtil.isTrueThrow403(CollectionUtils.isNotEmpty(accounts), ErrorMessage.PAYMENT_ACCOUNT_NOT_EXIST);
        //ErrorAssertUtil.isFalseThrow402(accounts.stream().map(StatutoryBodyAccountE::getStatutoryBodyId).distinct().collect(Collectors.toList()).size() > 1, ErrorMessage.PAYMENT_ACCOUNT_MULTI);

        List<String> chargeItemCodes = new ArrayList<>();
        List<String> subjectCodes = new ArrayList<>();
        for (ReimbursementDetailF reimbursementDetail : reimbursementF.getReimbursementDetails()) {
            chargeItemCodes.add(reimbursementDetail.getChargeItemCode());
            subjectCodes.add(reimbursementDetail.getSubjectCode());
        }
        //校验费项
        List<ChargeItemE> chargeItems = chargeItemDomainService.getByCodeList(chargeItemCodes);
        Map<String, ChargeItemE> chargeItemMap = new HashMap<>();
        for (ChargeItemE chargeItem : chargeItems) {
            chargeItemMap.put(chargeItem.getCode(), chargeItem);
        }
        List<String> unknownChargeItemCodes = chargeItemCodes.stream().filter(i -> !chargeItemMap.containsKey(i)).collect(Collectors.toList());
        if (!unknownChargeItemCodes.isEmpty()) {
            throw BizException.throw403("不存在编码为[" + String.join(",", unknownChargeItemCodes) + "] 的费项");
        }

        //校验并转换辅助核算税率
        if (CollectionUtils.isNotEmpty(reimbursementF.getReimbursementDetails()) && reimbursementF.isShare()) {
            for (ReimbursementDetailF reimbursementDetails : reimbursementF.getReimbursementDetails()) {
                List<ReimbursementAssisteItemF> assisteItems = reimbursementDetails.getAssisteItems();
                // 明细中有税额才需要校验税率
                if (reimbursementDetails.getTaxAmount() != null && reimbursementDetails.getTaxAmount() > 0) {
                    convertTaxRateCode(assisteItems);
                }
            }
        }
        if (CollectionUtils.isNotEmpty(reimbursementF.getInputTaxDetails()) && !reimbursementF.isShare()) {
            for (ReimbursementInputTaxF reimbursementInputTaxs : reimbursementF.getInputTaxDetails()) {
                List<ReimbursementAssisteItemF> assisteItems = reimbursementInputTaxs.getAssisteItems();
                // 明细中有税额才需要校验税率
                if (reimbursementInputTaxs.getTaxAmount() != null && reimbursementInputTaxs.getTaxAmount() > 0) {
                    convertTaxRateCode(assisteItems);
                }
            }
        }
        //校验科目
        subjectCodes.add(yuanYangSubjectProperties.getEmployeeReimbursement());
        subjectCodes.add(yuanYangSubjectProperties.getInputTax());
        subjectCodes.add(yuanYangSubjectProperties.getCalDeductInputTax());
        subjectCodes.add(yuanYangSubjectProperties.getBankAccount());
        subjectCodes.add(yuanYangSubjectProperties.getPettyCash());
        List<SubjectE> subjects = subjectDomainService.getSubjectsFullByCodes(subjectCodes);
        List<String> sysSubjectCodes = subjects.stream().map(SubjectE::getSubjectCode).collect(Collectors.toList());
        List<String> unknownSubjectCodes = subjectCodes.stream().filter(i -> !sysSubjectCodes.contains(i)).collect(Collectors.toList());
        if (!unknownSubjectCodes.isEmpty()) {
            throw BizException.throw403("不存在编码为[" + String.join(",", unknownSubjectCodes) + "] 的科目");
        }
        //支付信息
        BillTransactionV billTransactionV = new BillTransactionV();
        TransactionOrder transactionOrder = Global.mapperFacade.map(reimbursementF, TransactionOrder.class);
        transactionOrder.setAmount(reimbursementF.getPayableAmount());
        transactionOrder.setTransactionType(BillTransactTypeEnum.付款.getCode());
        transactionOrder.setBillParam(convertTransactionBillOBV(reimbursementF, null, LocalDateTime.now(), chargeItemMap));
        transactionOrder.setPayMethod(TransactionOrder.payMethod(reimbursementF.getPayWay(), reimbursementF.getPayChannel()));
        //1.发起支付
        try {
            billPaymentDomainService.transact(transactionOrder);
        } catch (BizException e) {
            billTransactionV.setBizTransactionNo(reimbursementF.getBizTransactionNo());
            billTransactionV.setTransactState(transactionOrder.getTransactState());
            billTransactionV.setInvoiceState(transactionOrder.getInvoiceState());
            billTransactionV.setVoucherState(transactionOrder.getVoucherState());
            billTransactionV.setErrCode("999999");
            billTransactionV.setErrMsg(e.getMessage());
            return billTransactionV;
        }

        Global.mapperFacade.map(transactionOrder, billTransactionV);
        BillTransactStateEnum transactState = BillTransactStateEnum.valueOfByCode(transactionOrder.getTransactState());
        if (BillTransactStateEnum.交易成功.equalsByCode(transactState.getCode())) {
            ReimburseCompensateF reimburseCompensateF = new ReimburseCompensateF()
                    .setReimbursementF(reimbursementF)
                    .setAccountBook(accountBook)
                    .setChargeItemMap(chargeItemMap)
                    .setSubjects(subjects)
                    .setTransactionNo(transactionOrder.getTransactionNo())
                    .setBusinessUnit(businessUnit);

            Voucher voucher = makerReimbursementVoucher(reimburseCompensateF.getReimbursementF(),
                    reimburseCompensateF.getAccountBook(), reimburseCompensateF.getBusinessUnit(),
                    reimburseCompensateF.getSubjects(), reimburseCompensateF.getChargeItemMap());
            BPMVoucherUtils.checkAmount(voucher);
            //异步处理入账和推凭
            AppThreadManager.execute(new AppRunnable() {
                @Override
                public void execute() {
                    doAfterReimburse(reimburseCompensateF, transactionOrder,
                            new TransactionCallbackCommand()
                                    .setMchOrderNo(transactionOrder.getTransactionNo())
                                    .setPayNo(transactionOrder.getPayNo())
                                    .setPayState(transactState.getCode())
                                    .setChanelTradeNo("")
                                    .setSuccessTime(transactionOrder.getSuccessTime()));
                }
            });
        } else if (BillTransactStateEnum.交易中.equalsByCode(transactState.getCode())) {
            //如果在交易中，将当前支付报销的信息放入缓存中，用于后续操作
            String key = REIMBURSE_CACHE_PREFIX + transactionOrder.getTransactionNo();
            String value = JSON.toJSONString(
                    ReimbursementCallbackF.formatReimburse(new ReimburseCompensateF()
                            .setReimbursementF(reimbursementF)
                            .setAccountBook(accountBook)
                            .setChargeItemMap(chargeItemMap)
                            .setSubjects(subjects)
                            .setTransactionNo(transactionOrder.getTransactionNo())
                            .setBusinessUnit(businessUnit)));
            log.info("银企直连交易中数据缓存:{}, value:{}", key, value);
            RedisHelper.set(key, value);
        } else {
            billTransactionV.setErrCode("999999");
        }
        return billTransactionV;
    }

    public void convertTaxRateCode(List<ReimbursementAssisteItemF> assisteItems) {
        if (CollectionUtils.isNotEmpty(assisteItems)) {
            boolean hasTaxAssisteItem = assisteItems.stream().anyMatch(item -> AssisteItemTypeEnum.增值税税率.equalsByCode(item.getType()));
            if (!hasTaxAssisteItem) {
                throw BizException.throw403(ErrorMessage.TAX_RATE_NOT_FIND_ERROR.msg());
            }
        } else {
            throw BizException.throw403(ErrorMessage.TAX_RATE_NOT_FIND_ERROR.msg());
        }
        for (ReimbursementAssisteItemF assisteItem : assisteItems) {
            if (AssisteItemTypeEnum.增值税税率.equalsByCode(assisteItem.getType())) {
                if (assisteItem.getCode().contains("%")) {
                    String rate = assisteItem.getCode().replaceAll("%", "");
                    ErrorAssertUtil.isTrueThrow403(NumberUtil.isNumeric(rate), ErrorMessage.TAX_RATE_ERROR);
                    //获取增值税税率
                    TaxRateE taxRate = taxRateDomainService.getByCategoryRate(yuanYangTaxRateProperties.getTaxCategoryCode(), new BigDecimal(rate));
                    ErrorAssertUtil.notNullThrow403(taxRate, ErrorMessage.TAX_RATE_ERROR);
                    assisteItem.setCode(taxRate.getCode());
                } else if (StringUtils.equals(assisteItem.getCode(), "计算扣除进项税额")) {
                    // do nothing
                } else {
                    throw BizException.throw403(ErrorMessage.TAX_RATE_CONTENT_ERROR.msg());
                }
            }
        }
    }

    /**
     * 支付报销支付完后置处理
     *
     * @param reimburseCompensateF 处理信息
     * @param transactionOrder     订单号
     * @param callbackCommand
     */
    public Boolean doAfterReimburse(ReimburseCompensateF reimburseCompensateF, TransactionOrder transactionOrder, TransactionCallbackCommand callbackCommand){
        boolean result;
        try {
            //2.入账
            transactionOrder = getTransactionOrderRetry(reimburseCompensateF.getTransactionNo());

            ErrorAssertUtil.notNullThrow403(transactionOrder, ErrorMessage.PAYMENT_TRADED_NOT_EXIST);
            transactionOrder.transactCallback(callbackCommand);
            Voucher voucher = null;
            List<Long> voucherIds = transactionOrder.getVoucherIds();
            if (transactionOrder.succeed() && CollectionUtils.isEmpty(voucherIds)) {
                PayBill payBill = billPaymentDomainService.enterTransactionPayBill(transactionOrder);
                transactionOrder.getBillParam().setBillId(payBill.getId());
                ReimbursementF reimbursementF = reimburseCompensateF.getReimbursementF();
                //OrgFinanceRv orgFinance = reimburseCompensateF.getOrgFinance();
                //3.推凭
                voucher = makerReimbursementVoucher(reimbursementF, reimburseCompensateF.getAccountBook(), reimburseCompensateF.getBusinessUnit(),
                        reimburseCompensateF.getSubjects(), reimburseCompensateF.getChargeItemMap());
                voucher = saveAndSyncVoucher(voucher, Objects.isNull(reimbursementF.getVoucherFlag()) ? 0 : reimbursementF.getVoucherFlag());
                transactionOrder.setVoucherIds(List.of(voucher.getId()));
                //4.添加凭证明细
                makeAndSaveVoucherDetail(payBill, voucher);
                transactionOrder.setVoucherState(getTransactVoucherState(voucher.getState()).getCode());
                //5.保存票据信息
                List<InvoiceA> invoiceAS = collectReimbursementInvoice(reimbursementF.getInvoices(), reimburseCompensateF.getBusinessUnit(), reimbursementF.getSysSource(),
                        transactionOrder.getBillParam());
                transactionOrder.setInvoiceIds(invoiceAS.stream().map(invoiceA -> invoiceA.getInvoiceReceipt().getId()).collect(Collectors.toList()));
                //6.推送票据
                if (VoucherStateEnum.成功.equalsByCode(voucher.getState())) {
                    //7.同步发票
                    boolean syncInvoiceResult = syncInvoice(invoiceAS, String.valueOf(voucher.getVoucherType()), voucher.getSyncSystemVoucherNo());
                    transactionOrder.setInvoiceState(syncInvoiceResult ? BillTransactInvoiceEnum.已开票.getCode() : BillTransactInvoiceEnum.开票异常.getCode());
                }
                billPaymentDomainService.updateTransaction(transactionOrder);
            }
            result =  true;
            //推送完成事件
            EventLifecycle.apply(EventMessage.builder().payload(new ReimburseCompletedEvent(transactionOrder, voucher)).build());
        } catch (Exception e) {
            log.error("BPM报销流程-异步处理入账和推凭异常", e);
            result = false;
        }
        return result;
    }


    /**
     * 差旅报销
     * @param travelReimbursementF
     * @return
     */
    public BillTransactionV travelReimburse(TravelReimbursementF travelReimbursementF) {

        //校验账簿
        AccountBookE accountBook = accountBookDomainService.getAccountBookByCode(travelReimbursementF.getAccountBookCode());
        ErrorAssertUtil.notNullThrow403(accountBook, ErrorMessage.ACCOUNT_BOOK_NO_EXISTS);
        //校验业务单元
        String businessUnitCode = accountBook.getCode().substring(0, accountBook.getCode().lastIndexOf("-"));
        BusinessUnitE businessUnit = businessUnitDomainService.geByCode(businessUnitCode);
        ErrorAssertUtil.notNullThrow403(businessUnit, ErrorMessage.BUSINESS_UNIT_NO_EXISTS);

        //校验业务单元下的银行账户

        //校验费项
        ChargeItemE chargeItem = chargeItemDomainService.getByCode(travelReimbursementF.getChargeItemCode());
        ErrorAssertUtil.notNullThrow403(chargeItem, ErrorMessage.REIMBURSE_CHARGE_ITEM_NOT_EXIST);

        //校验科目
        List<String> subjectCodes = new ArrayList<>();
        subjectCodes.add(travelReimbursementF.getSubjectCode());
        subjectCodes.add(yuanYangSubjectProperties.getInputTax());
        subjectCodes.add(yuanYangSubjectProperties.getCalDeductInputTax());
        subjectCodes.add(yuanYangSubjectProperties.getBankAccount());
        List<SubjectE> subjects = subjectDomainService.getSubjectsFullByCodes(subjectCodes);
        List<String> sysSubjectCodes = subjects.stream().map(SubjectE::getSubjectCode).collect(Collectors.toList());
        List<String> unknownSubjectCodes = subjectCodes.stream().filter(i -> !sysSubjectCodes.contains(i)).collect(Collectors.toList());
        if (!unknownSubjectCodes.isEmpty()) {
            throw BizException.throw403("不存在编码为[" + String.join(",", unknownSubjectCodes) + "] 的费项");
        }

        Map<String, TaxRateE> taxRateMap = new HashMap<>();

        //校验辅助核算税率
        if (CollectionUtils.isNotEmpty(travelReimbursementF.getInputTaxDetails())) {
            for (TravelReimbursementInputTaxF inputTaxDetail : travelReimbursementF.getInputTaxDetails()) {
                String rate = inputTaxDetail.getTaxRate().replaceAll("%", "");
                if (StringUtils.isNumeric(rate)){
                    //获取增值税税率
                    TaxRateE taxRate = taxRateDomainService.getByCategoryRate(yuanYangTaxRateProperties.getTaxCategoryCode(), new BigDecimal(rate));
                    ErrorAssertUtil.notNullThrow403(taxRate, ErrorMessage.TAX_RATE_ERROR);
                    taxRateMap.put(inputTaxDetail.getTaxRate(), taxRate);
                }
            }
        }

        //支付信息
        BillTransactionV billTransactionV = new BillTransactionV();
        TransactionOrder transactionOrder = Global.mapperFacade.map(travelReimbursementF, TransactionOrder.class);
        transactionOrder.setAmount(travelReimbursementF.getPayableAmount());
        transactionOrder.setTransactionType(BillTransactTypeEnum.付款.getCode());
        transactionOrder.setBillParam(convertTravelTransactionBillOBV(travelReimbursementF, null, LocalDateTime.now(), chargeItem));
        transactionOrder.setPayMethod(TransactionOrder.payMethod(travelReimbursementF.getPayWay(), travelReimbursementF.getPayChannel()));
        //1.发起支付
        try {
            billPaymentDomainService.transact(transactionOrder);
        } catch (BizException e) {
            billTransactionV.setBizTransactionNo(travelReimbursementF.getBizTransactionNo());
            billTransactionV.setTransactState(transactionOrder.getTransactState());
            billTransactionV.setInvoiceState(transactionOrder.getInvoiceState());
            billTransactionV.setVoucherState(transactionOrder.getVoucherState());
            billTransactionV.setErrCode("999999");
            billTransactionV.setErrMsg(e.getMessage());
            return billTransactionV;
        }

        Global.mapperFacade.map(transactionOrder, billTransactionV);
        BillTransactStateEnum transactState = BillTransactStateEnum.valueOfByCode(transactionOrder.getTransactState());
        if (BillTransactStateEnum.交易成功.equalsByCode(transactState.getCode())) {

            TravelReimburseCompensateF compensateF = new TravelReimburseCompensateF()
                    .setTransactionNo(transactionOrder.getTransactionNo())
                    .setTravelReimbursementF(travelReimbursementF)
                    .setAccountBook(accountBook)
                    .setChargeItem(chargeItem)
                    .setSubjects(subjects)
                    .setBusinessUnit(businessUnit)
                    .setTaxRateMap(taxRateMap);
            Voucher voucher = makerTravelReimbursementVoucher(travelReimbursementF, compensateF.getAccountBook(),
                    compensateF.getBusinessUnit(), compensateF.getSubjects(), compensateF.getChargeItem(), compensateF.getTaxRateMap());
            BPMVoucherUtils.checkAmount(voucher);
            //异步处理入账和推凭
            AppThreadManager.execute(new AppRunnable() {
                @Override
                public void execute() {
                    doAfterTravelReimburse(compensateF, transactionOrder, new TransactionCallbackCommand()
                            .setMchOrderNo(transactionOrder.getTransactionNo())
                            .setPayNo(transactionOrder.getPayNo())
                            .setPayState(transactState.getCode())
                            .setChanelTradeNo("")
                            .setSuccessTime(transactionOrder.getSuccessTime()));
                }
            });
        } else if (BillTransactStateEnum.交易中.equalsByCode(transactState.getCode())) {
            //如果在交易中，将当前支付报销的信息放入缓存中，用于后续操作
            String key = REIMBURSE_CACHE_PREFIX + transactionOrder.getTransactionNo();
            String value = JSON.toJSONString(ReimbursementCallbackF.formatTravelReimburse(new TravelReimburseCompensateF()
                    .setTransactionNo(transactionOrder.getTransactionNo())
                    .setTravelReimbursementF(travelReimbursementF)
                    .setAccountBook(accountBook)
                    .setChargeItem(chargeItem)
                    .setSubjects(subjects)
                    .setBusinessUnit(businessUnit)
                    .setTaxRateMap(taxRateMap)));
            log.info("银企直连交易中数据缓存:{}, value:{}", key, value);
            RedisHelper.set(key, value);
        } else {
            billTransactionV.setErrCode("999999");
        }
        return billTransactionV;
    }




    public TransactionOrder getTransactionOrderRetry(String transactionNo){
        TransactionOrder result = null;

        try {
            result = RetryUtils.transactionOrderRetryer.call(() -> {
                return billPaymentDomainService.getTransactionOrderByNo(transactionNo);
            });
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public BusinessBill getBusinessBillRetry(String businessId){
        BusinessBill result = null;

        try {
            result = RetryUtils.businessBillRetryer.call(() -> {
                BusinessBill businessBill = businessBillDomainService.getByBusinessId(businessId);
                return businessBill;
            });
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * 差旅报销支付完后置处理
     * @param compensateF
     * @param transactionOrder
     */
    public Boolean doAfterTravelReimburse(TravelReimburseCompensateF compensateF, TransactionOrder transactionOrder, TransactionCallbackCommand callbackCommand){
        boolean result;
        try {

            transactionOrder = getTransactionOrderRetry(compensateF.getTransactionNo());
            ErrorAssertUtil.notNullThrow403(transactionOrder, ErrorMessage.PAYMENT_TRADED_NOT_EXIST);
            transactionOrder.transactCallback(callbackCommand);
            List<Long> voucherIds = transactionOrder.getVoucherIds();
            TravelReimbursementF travelReimbursementF = compensateF.getTravelReimbursementF();
            if (transactionOrder.succeed() && CollectionUtils.isEmpty(voucherIds)) {
                //2.入账
                PayBill payBill = billPaymentDomainService.enterTransactionPayBill(transactionOrder);
                transactionOrder.getBillParam().setBillId(payBill.getId());
                //3.推凭
                Voucher voucher = makerTravelReimbursementVoucher(travelReimbursementF, compensateF.getAccountBook(),
                        compensateF.getBusinessUnit(), compensateF.getSubjects(), compensateF.getChargeItem(), compensateF.getTaxRateMap());
                voucher = saveAndSyncVoucher(voucher, Objects.isNull(travelReimbursementF.getVoucherFlag()) ? 0 : travelReimbursementF.getVoucherFlag());
                transactionOrder.setVoucherIds(List.of(voucher.getId()));
                //4.添加凭证明细
                makeAndSaveVoucherDetail(payBill, voucher);
                transactionOrder.setVoucherState(getTransactVoucherState(voucher.getState()).getCode());
                //5.保存票据信息
                List<InvoiceA> invoiceAS = collectReimbursementInvoice(travelReimbursementF.getInvoices(), compensateF.getBusinessUnit(), travelReimbursementF.getSysSource(),
                        transactionOrder.getBillParam());
                transactionOrder.setInvoiceIds(invoiceAS.stream().map(invoiceA -> invoiceA.getInvoiceReceipt().getId()).collect(Collectors.toList()));
                //6.推送票据
                if (VoucherStateEnum.成功.equalsByCode(voucher.getState())) {
                    //7.同步发票
                    boolean syncInvoiceResult = syncInvoice(invoiceAS, String.valueOf(voucher.getVoucherType()), voucher.getSyncSystemVoucherNo());
                    transactionOrder.setInvoiceState(syncInvoiceResult ? BillTransactInvoiceEnum.已开票.getCode() : BillTransactInvoiceEnum.开票异常.getCode());
                }
                billPaymentDomainService.updateTransaction(transactionOrder);
                //推送完成事件
                EventLifecycle.apply(EventMessage.builder().payload(new ReimburseCompletedEvent(transactionOrder, voucher)).build());
            }
            result = true;
        } catch (Exception e) {
            log.error("BPM差旅费报销流程-异步处理入账和推凭异常", e);
            result = false;
        }
        return result;
    }


    /**
     * 资金下拨
     * @param allocateFundsReimbursementF
     * @return
     */
    public BillTransactionV allocateFundsReimburse(AllocateFundsReimbursementF allocateFundsReimbursementF) {

        //校验账簿
        AccountBookE accountBook = accountBookDomainService.getAccountBookByCode(allocateFundsReimbursementF.getAccountBookCode());
        ErrorAssertUtil.notNullThrow403(accountBook, ErrorMessage.ACCOUNT_BOOK_NO_EXISTS);
        //根据付款账号获取现法定单位
        List<StatutoryBodyAccountE> accounts = statutoryBodyAccountDomainService.getListByAccount(allocateFundsReimbursementF.getPayer().getPayerAccount());
        ErrorAssertUtil.isTrueThrow403(CollectionUtils.isNotEmpty(accounts), ErrorMessage.PAYMENT_ACCOUNT_NOT_EXIST);
        ErrorAssertUtil.isFalseThrow402(accounts.stream().map(StatutoryBodyAccountE::getStatutoryBodyId).distinct().collect(Collectors.toList()).size() > 1, ErrorMessage.PAYMENT_ACCOUNT_MULTI);

        //校验法定单位
        OrgFinanceRv orgFinance = orgClient.getOrgFinanceById(accounts.get(0).getStatutoryBodyId());
        ErrorAssertUtil.notNullThrow403(orgFinance, ErrorMessage.REIMBURSE_STATUTORY_BODY_NO_EXISTS);

        //校验科目
        List<String> subjectCodes = new ArrayList<>();
        subjectCodes.add(yuanYangSubjectProperties.getCollectPay());
        subjectCodes.add(yuanYangSubjectProperties.getBankAccount());
        List<SubjectE> subjects = subjectDomainService.getSubjectsFullByCodes(subjectCodes);
        List<String> sysSubjectCodes = subjects.stream().map(SubjectE::getSubjectCode).collect(Collectors.toList());
        List<String> unknownSubjectCodes = subjectCodes.stream().filter(i -> !sysSubjectCodes.contains(i)).collect(Collectors.toList());
        if (!unknownSubjectCodes.isEmpty()) {
            throw BizException.throw403("不存在编码为[" + String.join(",", unknownSubjectCodes) + "] 的费项");
        }

        //支付信息
        BillTransactionV billTransactionV = new BillTransactionV();
        TransactionOrder transactionOrder = Global.mapperFacade.map(allocateFundsReimbursementF, TransactionOrder.class);

        transactionOrder.setTransactionType(BillTransactTypeEnum.付款.getCode());
        transactionOrder.setPayMethod(TransactionOrder.payMethod(allocateFundsReimbursementF.getPayWay(), allocateFundsReimbursementF.getPayChannel()));

        ArrayList<TransactionOrder> transactionOrders = new ArrayList<>();
        for (AllocateDetails allocateDetails : allocateFundsReimbursementF.getAllocateDetails()) {

            Payee payee = Global.mapperFacade.map(allocateDetails, Payee.class);
            //子订单信息
            TransactionOrder subTransactionOrder = Global.mapperFacade.map(transactionOrder, TransactionOrder.class);
            subTransactionOrder.setPayee(payee);
            subTransactionOrder.setTransactionType(BillTransactTypeEnum.付款.getCode());
            subTransactionOrder.setBizTransactionNo(allocateDetails.getAllocateBusinessId());
            subTransactionOrder.setAmount(allocateDetails.getAllocateAmount());
            subTransactionOrder.setBillParam(convertAllocateFundsTransactionBillOBV(allocateFundsReimbursementF, orgFinance, LocalDateTime.now()));

            transactionOrders.add(subTransactionOrder);
        }
        transactionOrder.setSubOrders(transactionOrders);
        //1.发起支付
        try {
            billPaymentDomainService.combineTransact(transactionOrder);
        } catch (BizException e) {
            billTransactionV.setBizTransactionNo(allocateFundsReimbursementF.getBizTransactionNo());
            billTransactionV.setTransactState(transactionOrder.getTransactState());
            billTransactionV.setInvoiceState(transactionOrder.getInvoiceState());
            billTransactionV.setVoucherState(transactionOrder.getVoucherState());
            billTransactionV.setErrCode("999999");
            billTransactionV.setErrMsg(e.getMessage());
            return billTransactionV;
        }

        Global.mapperFacade.map(transactionOrder, billTransactionV);
        for (TransactionOrder subOrder : transactionOrder.getSubOrders()) {
            if (BillTransactStateEnum.交易成功.equalsByCode(subOrder.getTransactState())) {
                String finalChargeItemNameJoin = "" +allocateFundsReimbursementF.getChargeTargetName();
                //异步处理入账和推凭
                AppThreadManager.execute(new AppRunnable() {
                    @Override
                    public void execute() {
                        doAfterAllocateFundsReimburse(new AllocateFundsCompensateF()
                                        .setAllocateFundsReimbursementF(allocateFundsReimbursementF)
                                        .setAccountBook(accountBook)
                                        .setOrgFinance(orgFinance)
                                        .setSubjects(subjects),
                                transactionOrder,
                                new TransactionCallbackCommand()
                                        .setMchOrderNo(subOrder.getTransactionNo())
                                        .setPayNo(subOrder.getPayNo())
                                        .setPayState(subOrder.getTransactState())
                                        .setChanelTradeNo("")
                                        .setSuccessTime(subOrder.getSuccessTime()));
                    }
                });
            } else if (BillTransactStateEnum.交易中.equalsByCode(subOrder.getTransactState())){
                RedisHelper.set(REIMBURSE_CACHE_PREFIX + transactionOrder.getTransactionNo(),
                        JSON.toJSONString(ReimbursementCallbackF.formatAllocateFunds(new AllocateFundsCompensateF()
                                .setTransactionNo(transactionOrder.getTransactionNo())
                                .setAllocateFundsReimbursementF(allocateFundsReimbursementF)
                                .setAccountBook(accountBook)
                                .setSubjects(subjects)
                                .setOrgFinance(orgFinance)
                        )));
            }
        }
        return billTransactionV;
    }

    /**
     * 资金下拨 支付后处理
     * @param allocateFundsCompensateF
     * @param transactionOrder
     * @param callbackCommand
     */
    public Boolean doAfterAllocateFundsReimburse(AllocateFundsCompensateF allocateFundsCompensateF, TransactionOrder transactionOrder, TransactionCallbackCommand callbackCommand) {
        boolean result;
        try {
            if (Objects.isNull(transactionOrder)){
                transactionOrder = billPaymentDomainService.getTransactionOrderByNo(allocateFundsCompensateF.getTransactionNo());
            }
            ErrorAssertUtil.notNullThrow403(transactionOrder, ErrorMessage.PAYMENT_TRADED_NOT_EXIST);
            transactionOrder.transactCallback(callbackCommand);
            PayBill payBill = billPaymentDomainService.enterTransactionPayBill(transactionOrder);

            transactionOrder.getBillParam().setBillId(payBill.getId());
            AllocateFundsReimbursementF allocateFundsReimbursementF = allocateFundsCompensateF.getAllocateFundsReimbursementF();
            AccountBookE accountBook = allocateFundsCompensateF.getAccountBook();
            OrgFinanceRv orgFinanceRv = allocateFundsCompensateF.getOrgFinanceRv();
            List<SubjectE> subjects = allocateFundsCompensateF.getSubjects();

            // 制作凭证
            List<Voucher> vouchers = makerAllocateFundsReimbursementVoucher(allocateFundsReimbursementF, transactionOrder, accountBook, orgFinanceRv, subjects);
            voucherDomainService.makeVouchers(vouchers);
            // 4.推凭
            for (Voucher voucher : vouchers) {
                saveAndSyncVoucher(voucher, 1);
                // 发布事件
                EventLifecycle.apply(EventMessage.builder().payload(new ReimburseCompletedEvent(transactionOrder, voucher)).build());
            }
            result = true;
        } catch (Exception e) {
            log.error("BPM资金上缴流程-异步处理入账和推凭异常", e);
            result = false;
        }
        return result;
    }
    /**
     * 资金上缴
     * @param turnoverFundsReimbursementF
     * @return
     */
    public BillTransactionV turnoverReimburse(TurnoverFundsReimbursementF turnoverFundsReimbursementF) {
        //校验账簿
        List<String> accountBookCodes = turnoverFundsReimbursementF.getTurnoverDetails().stream().map(TurnoverDetail::getAccountBookCode).collect(Collectors.toList());
        accountBookCodes.add(turnoverFundsReimbursementF.getSettleBookCode());
        List<AccountBookE> accountBooks = accountBookDomainService.getAccountBookByCodes(accountBookCodes);
        List<String> sysAccountBookCodes = accountBooks.stream().map(AccountBookE::getCode).collect(Collectors.toList());
        List<String> unknownAccountBookCodes = accountBookCodes.stream().filter(i -> !sysAccountBookCodes.contains(i)).collect(Collectors.toList());
        if (!unknownAccountBookCodes.isEmpty()) {
            throw BizException.throw403("不存在编码为[" + String.join(",", unknownAccountBookCodes) + "] 的账簿");
        }

        //根据付款账号获取现法定单位
        List<String> payerBankAccounts = turnoverFundsReimbursementF.getTurnoverDetails().stream().map(PayerF::getPayerAccount).collect(Collectors.toList());
        List<StatutoryBodyAccountE> accounts = statutoryBodyAccountDomainService.getListByAccounts(payerBankAccounts, List.of(RecPayTypeEnum.付款.getCode(), RecPayTypeEnum.收款付款.getCode()));
        List<String> sysAccounts = accounts.stream().map(StatutoryBodyAccountE::getBankAccount).collect(Collectors.toList());
        List<String> unknownAccounts = payerBankAccounts.stream().filter(i -> !sysAccounts.contains(i)).collect(Collectors.toList());
        if (!unknownAccounts.isEmpty()) {
            throw BizException.throw403("不存在账号为[" + String.join(",", unknownAccounts) + "] 的付款账号");
        }
        //校验收款账号
        String payeeAccount = turnoverFundsReimbursementF.getPayee().getPayeeAccount();
        List<StatutoryBodyAccountE> payeeBankAccounts = statutoryBodyAccountDomainService.getListByAccount(payeeAccount);
        if (CollectionUtils.isEmpty(payeeBankAccounts)){
            throw BizException.throw403("不存在账号为[" + payeeAccount + "] 的收款账号");
        }
        //校验法定单位
        List<String> sbCodes = accountBookCodes.stream().map(i -> i.substring(0, i.lastIndexOf("-"))).collect(Collectors.toList());
        List<OrgFinanceRv> orgFinances = orgClient.listByCodes(sbCodes);
        if (CollectionUtils.isEmpty(orgFinances)){
            throw BizException.throw403("不存在编号为[" + String.join(",", sbCodes) + "] 的法定单位");
        }

        List<String> sysOrgCodes = orgFinances.stream().map(OrgFinanceRv::getCode).collect(Collectors.toList());
        List<String> unknownOrgCodes = sysOrgCodes.stream().filter(i -> !sysOrgCodes.contains(i)).collect(Collectors.toList());
        if (!unknownOrgCodes.isEmpty()) {
            throw BizException.throw403("不存在编号为[" + String.join(",", unknownOrgCodes) + "] 的法定单位");
        }
        //校验科目
        List<String> subjectCodes = new ArrayList<>();
        subjectCodes.add(yuanYangSubjectProperties.getAllocateTurnover());
        subjectCodes.add(yuanYangSubjectProperties.getBankAccount());
        List<SubjectE> subjects = subjectDomainService.getSubjectsFullByCodes(subjectCodes);
        List<String> sysSubjectCodes = subjects.stream().map(SubjectE::getSubjectCode).collect(Collectors.toList());
        List<String> unknownSubjectCodes = subjectCodes.stream().filter(i -> !sysSubjectCodes.contains(i)).collect(Collectors.toList());
        if (!unknownSubjectCodes.isEmpty()) {
            throw BizException.throw403("不存在编码为[" + String.join(",", unknownSubjectCodes) + "] 的费项");
        }
        //交易信息
        BillTransactionV billTransactionV = new BillTransactionV();

        TransactionOrder transactionOrder = Global.mapperFacade.map(turnoverFundsReimbursementF, TransactionOrder.class);
        transactionOrder.setTransactionType(BillTransactTypeEnum.收款.getCode());

        Map<String, OrgFinanceRv> orgFinanceMap = ListUtils.toGroupSingleMap(orgFinances, OrgFinanceRv::getCode);
        LocalDateTime nowDateTime = LocalDateTime.now();
        String settleBookCode = turnoverFundsReimbursementF.getSettleBookCode();
        transactionOrder.setBillParam(convertTurnoverFundsTransactionBillOBV(turnoverFundsReimbursementF,
                orgFinanceMap.get(settleBookCode.substring(0, settleBookCode.lastIndexOf("-"))), nowDateTime));

        //String accountBookCode;
        ArrayList<TransactionOrder> transactionOrders = new ArrayList<>();
        for (TurnoverDetail turnoverDetail : turnoverFundsReimbursementF.getTurnoverDetails()) {
            //accountBookCode = turnoverDetail.getAccountBookCode();
            Payer payer = Global.mapperFacade.map(turnoverDetail, Payer.class);
            //子订单信息
            TransactionOrder subTransactionOrder = Global.mapperFacade.map(transactionOrder, TransactionOrder.class);
            subTransactionOrder.setPayer(payer);
            subTransactionOrder.setAmount(turnoverDetail.getTurnoverAmount());
            subTransactionOrder.setTransactionType(BillTransactTypeEnum.付款.getCode());
            subTransactionOrder.setBizTransactionNo(turnoverDetail.getTurnoverBusinessId());
            subTransactionOrder.setBillParam(convertTurnoverDetailTransactionBillOBV(turnoverFundsReimbursementF, turnoverDetail, null, nowDateTime));
            transactionOrders.add(subTransactionOrder);
        }
        transactionOrder.setSubOrders(transactionOrders);
        try {
            //1.发起支付
            billPaymentDomainService.combineTransact(transactionOrder);
            billTransactionV = Global.mapperFacade.map(transactionOrder, BillTransactionV.class);
        } catch (BizException e) {
            billTransactionV.setBizTransactionNo(turnoverFundsReimbursementF.getBizTransactionNo());
            billTransactionV.setTransactState(transactionOrder.getTransactState());
            billTransactionV.setInvoiceState(transactionOrder.getInvoiceState());
            billTransactionV.setVoucherState(transactionOrder.getVoucherState());
            billTransactionV.setErrCode("999999");
            billTransactionV.setErrMsg(e.getMessage());
        }
        // 如果交易成功则制作预制凭
        for (TransactionOrder subOrder : transactionOrder.getSubOrders()) {
            if (BillTransactStateEnum.交易成功.equalsByCode(subOrder.getTransactState())) {
                AppThreadManager.execute(new AppRunnable() {
                    @Override
                    public void execute() {
                        doAfterTurnoverReimburse(new TurnoverFundsCompensateF()
                                        .setTurnoverFundsReimbursementF(turnoverFundsReimbursementF)
                                        .setAccountBook(accountBooks)
                                        .setOrgFinance(orgFinances)
                                        .setSubjects(subjects),
                                transactionOrder,
                                new TransactionCallbackCommand()
                                        .setMchOrderNo(subOrder.getTransactionNo())
                                        .setPayNo(subOrder.getPayNo())
                                        .setPayState(subOrder.getTransactState())
                                        .setChanelTradeNo("")
                                        .setSuccessTime(subOrder.getSuccessTime()));
                    }
                });
            } else if (BillTransactStateEnum.交易中.equalsByCode(subOrder.getTransactState())) {
                //如果在交易中，将当前资金上缴的信息放入缓存中，用于后续操作
                RedisHelper.set(REIMBURSE_CACHE_PREFIX + subOrder.getTransactionNo(),
                        JSON.toJSONString(ReimbursementCallbackF.formatTurnoverFunds(new TurnoverFundsCompensateF()
                                .setTurnoverFundsReimbursementF(turnoverFundsReimbursementF)
                                .setSubjects(subjects)
                                .setOrgFinance(orgFinances)
                                .setSubjects(subjects)
                                .setTransactionNo(subOrder.getTransactionNo()))));
            }
        }
        return billTransactionV;
    }


    /**
     * 资金上缴 支付后处理
     * @param turnoverFundsCompensateF
     * @param transactionOrder
     * @param callbackCommand
     */
    public Boolean doAfterTurnoverReimburse(TurnoverFundsCompensateF turnoverFundsCompensateF, TransactionOrder transactionOrder, TransactionCallbackCommand callbackCommand){
        boolean result;
        List<AccountBookE> accountBooks = turnoverFundsCompensateF.getAccountBooks();
        List<OrgFinanceRv> orgFinances = turnoverFundsCompensateF.getOrgFinances();
        List<SubjectE> subjects = turnoverFundsCompensateF.getSubjects();
        TurnoverFundsReimbursementF turnoverFundsReimbursementF = turnoverFundsCompensateF.getTurnoverFundsReimbursementF();
        try {
            //2.入账
            //2.1 入一笔收款单
            if (Objects.isNull(transactionOrder)){
                transactionOrder = billPaymentDomainService.getTransactionOrderByNo(turnoverFundsCompensateF.getTransactionNo());
            }
            billPaymentDomainService.enterTransactionGatherBill(transactionOrder);
            //2.2 入多笔付款单
            List<TransactionOrder> subOrders = transactionOrder.getSubOrders();
            if (CollectionUtils.isNotEmpty(subOrders)){
                for (TransactionOrder subOrder : subOrders) {
                    PayBill payBill = billPaymentDomainService.enterTransactionPayBill(subOrder);
                    subOrder.getBillParam().setBillId(payBill.getId());
                }
                billPaymentDomainService.updateBatchTransaction(subOrders);
            }
            //3.生成预制凭证
            List<Voucher> vouchers = makerTurnoverReimburseVoucher(turnoverFundsReimbursementF, transactionOrder, accountBooks, orgFinances, subjects);
            voucherDomainService.makeVouchers(vouchers);
            // 4.推凭
            for (Voucher voucher : vouchers) {
                saveAndSyncVoucher(voucher, 1);
                // 发布事件
                EventLifecycle.apply(EventMessage.builder().payload(new ReimburseCompletedEvent(transactionOrder, voucher)).build());
            }
            result = true;
        } catch (Exception e) {
            log.error("BPM资金上缴流程-异步处理入账和推凭异常", e);
            result = false;
        }
        return result;
    }

    /**
     * 制作资金上缴凭证
     *
     * @param turnoverFundsReimbursementF 资金上缴流程信息
     * @param transactionOrder            交易单信息
     * @param accountBooks                账簿信息
     * @param orgFinances                 财务组织信息
     * @param subjects                    科目信息
     * @return
     */
    public List<Voucher> makerTurnoverReimburseVoucher(TurnoverFundsReimbursementF turnoverFundsReimbursementF, TransactionOrder transactionOrder,
                                                       List<AccountBookE> accountBooks, List<OrgFinanceRv> orgFinances, List<SubjectE> subjects){
        Map<String, AccountBookE> accountBookMap = ListUtils.toGroupSingleMap(accountBooks, AccountBookE::getCode);
        Map<String, OrgFinanceRv> orgFinanceMap = ListUtils.toGroupSingleMap(orgFinances, OrgFinanceRv::getCode);
        Map<String, TurnoverDetail> turnoverDetailMap = ListUtils.toGroupSingleMap(turnoverFundsReimbursementF.getTurnoverDetails(), TurnoverDetail::getTurnoverBusinessId);
        String accountBookCode;
        AccountBookE accountBook;
        OrgFinanceRv orgFinanceRv;
        TurnoverDetail turnoverDetail;
        List<Voucher> vouchers = new ArrayList<>();
        //资金上缴明细
        long payableAmount = 0;
        for (TransactionOrder subOrder : transactionOrder.getSubOrders()) {
            if (!BillTransactStateEnum.交易成功.equalsByCode(subOrder.getTransactState())){
                continue;
            }
            turnoverDetail = turnoverDetailMap.get(subOrder.getBizTransactionNo());
            accountBook = accountBookMap.get(turnoverDetail.getAccountBookCode());
            accountBookCode = accountBook.getCode();
            orgFinanceRv = orgFinanceMap.get(accountBookCode.substring(0, accountBookCode.lastIndexOf("-")));
            Voucher voucher = new Voucher();
            voucher.setMadeType(VoucherMakeTypeEnum.自动.getCode());
            voucher.setVoucherType(VoucherTypeEnum.记账凭证.getCode());
            voucher.setVoucherSource(VoucherSourceEnum.BPM推送.getCode());
            voucher.setSysSource(SysSourceEnum.BPM系统.getCode());
            voucher.setAccountBookId(accountBook.getId());
            voucher.setAccountBookCode(accountBook.getCode());
            voucher.setAccountBookName(accountBook.getName());
            voucher.setStatutoryBodyId(orgFinanceRv.getId());
            voucher.setStatutoryBodyCode(orgFinanceRv.getCode());
            voucher.setStatutoryBodyName(orgFinanceRv.getNameCn());
            voucher.setCostCenters(new ArrayList<>());
            voucher.setAmount(subOrder.getAmount());
            payableAmount += subOrder.getAmount();
            voucher.setFiscalPeriod(LocalDate.now());
            voucher.setBookkeepingDate(turnoverFundsReimbursementF.getAccountDate());
            voucher.setEvenType(VoucherEventTypeEnum.付款结算.getCode());
            //根据费项匹配科目信息
            Map<String, SubjectE> subjectMap = ListUtils.toGroupSingleMap(subjects, SubjectE::getSubjectCode);
            List<VoucherDetailOBV> details = new ArrayList<>();
            String month = DateTimeUtil.formatCNMonth(voucher.getFiscalPeriod());
            //默认上缴【业务信息-结算中心】往来款-付款成功日期
            String summary = "上缴" + turnoverFundsReimbursementF.getSettleCostName()+ "往来款-" + DateTimeUtil.formatCNDate(transactionOrder.getSuccessTime().toLocalDate());
            //借方 内部往来\上交下拨
            details.add(generateVoucherDetail(VoucherLoanTypeEnum.借方, subjectMap.get(yuanYangSubjectProperties.getAllocateTurnover()), subOrder.getAmount(), subOrder.getAmount(), summary,
                    List.of(new AssisteItemOBV(AssisteItemTypeEnum.项目, turnoverFundsReimbursementF.getSettleCostCode(), turnoverFundsReimbursementF.getSettleCostName()),
                            new AssisteItemOBV(AssisteItemTypeEnum.客商, orgFinanceRv.getCode(), orgFinanceRv.getNameCn()))));
            //贷方 银行存款
            details.add(generateVoucherDetail(VoucherLoanTypeEnum.贷方, subjectMap.get(yuanYangSubjectProperties.getBankAccount()), subOrder.getAmount(), subOrder.getAmount(),summary,
                    List.of(new AssisteItemOBV(AssisteItemTypeEnum.银行账户, subOrder.getPayer().getPayerAccount(), subOrder.getPayer().getBankNo()),
                            new AssisteItemOBV(AssisteItemTypeEnum.存款账户性质, "01", "活期"))));
            //填充现金流量信息
            voucherDomainService.putVoucherCashFlow(details, subjectMap);
            voucher.setDetails(details);
            voucher.setSyncSystem(1);
            voucher.setMakerId(turnoverFundsReimbursementF.getCashReviewerId());
            voucher.setMakerName(turnoverFundsReimbursementF.getCashReviewerName());
            voucher.setState(VoucherStateEnum.待同步.getCode());
            vouchers.add(voucher);
        }

        //收款方凭证
        accountBook = accountBookMap.get(turnoverFundsReimbursementF.getSettleBookCode());
        accountBookCode = accountBook.getCode();
        orgFinanceRv = orgFinanceMap.get(accountBookCode.substring(0, accountBookCode.lastIndexOf("-")));
        Voucher voucher = new Voucher();
        voucher.setMadeType(VoucherMakeTypeEnum.自动.getCode());
        voucher.setVoucherType(VoucherTypeEnum.记账凭证.getCode());
        voucher.setAccountBookId(accountBook.getId());
        voucher.setAccountBookCode(accountBook.getCode());
        voucher.setAccountBookName(accountBook.getName());
        voucher.setStatutoryBodyId(orgFinanceRv.getId());
        voucher.setStatutoryBodyCode(orgFinanceRv.getCode());
        voucher.setStatutoryBodyName(orgFinanceRv.getNameCn());
        voucher.setCostCenters(new ArrayList<>());
        voucher.setAmount(payableAmount);
        voucher.setBookkeepingDate(turnoverFundsReimbursementF.getAccountDate());
        voucher.setEvenType(VoucherEventTypeEnum.付款结算.getCode());
        //根据费项匹配科目信息
        Map<String, SubjectE> subjectMap = ListUtils.toGroupSingleMap(subjects, SubjectE::getSubjectCode);
        List<VoucherDetailOBV> details = new ArrayList<>();
        //默认收到【资金上缴明细-核算成本中心】上缴往来款-付款成功日期
        String summary = "收到" + turnoverFundsReimbursementF.getSettleCostName()+ "上缴往来款-" + DateTimeUtil.formatCNDate(transactionOrder.getSuccessTime().toLocalDate());
        //借方 内部往来\上交下拨
        details.add(generateVoucherDetail(VoucherLoanTypeEnum.借方, subjectMap.get(yuanYangSubjectProperties.getAllocateTurnover()), payableAmount, payableAmount, summary,
                List.of(new AssisteItemOBV(AssisteItemTypeEnum.项目, turnoverFundsReimbursementF.getSettleCostCode(), turnoverFundsReimbursementF.getSettleCostName()),
                        new AssisteItemOBV(AssisteItemTypeEnum.客商, orgFinanceRv.getCode(), orgFinanceRv.getNameCn()))));
        //贷方 银行存款
        details.add(generateVoucherDetail(VoucherLoanTypeEnum.贷方, subjectMap.get(yuanYangSubjectProperties.getBankAccount()), payableAmount, payableAmount, summary,
                List.of(new AssisteItemOBV(AssisteItemTypeEnum.银行账户, turnoverFundsReimbursementF.getPayee().getPayeeAccount(), turnoverFundsReimbursementF.getPayee().getPayeeBank()),
                        new AssisteItemOBV(AssisteItemTypeEnum.存款账户性质, "01", "活期"))));
        //填充现金流量信息
        voucherDomainService.putVoucherCashFlow(details, subjectMap);
        voucher.setDetails(details);
        voucher.setFiscalPeriod(LocalDate.now());
        voucher.setSyncSystem(1);
        voucher.setMakerId(turnoverFundsReimbursementF.getCashReviewerId());
        voucher.setMakerName(turnoverFundsReimbursementF.getCashReviewerName());
        voucher.setState(VoucherStateEnum.待同步.getCode());
        vouchers.add(voucher);
        return vouchers;
    }

    /**
     * 转换交易账单信息
     *
     * @param reimbursementF 报销流程入参
     * @param orgFinance     法定单位信息
     * @param payTime        付款时间
     * @param chargeItemMap  费项映射信息
     * @return 交易账单信息
     */
    public TransactionBillOBV convertTransactionBillOBV(ReimbursementF reimbursementF, OrgFinanceRv orgFinance, LocalDateTime payTime, Map<String, ChargeItemE> chargeItemMap) {
        TransactionBillOBV transactionBillOBV = new TransactionBillOBV();
        transactionBillOBV.setBillType(BillTypeEnum.付款单.getCode());
        transactionBillOBV.setOutBusNo(reimbursementF.getBizTransactionNo());
        if (Objects.nonNull(orgFinance)){
            transactionBillOBV.setStatutoryBodyId(orgFinance.getId());
            transactionBillOBV.setStatutoryBodyName(orgFinance.getNameCn());
        }
        transactionBillOBV.setStartTime(payTime);
        transactionBillOBV.setEndTime(payTime);
        transactionBillOBV.setPayTime(payTime);
        transactionBillOBV.setPayChannel(reimbursementF.getPayChannel());
        transactionBillOBV.setPayWay(reimbursementF.getPayWay());
        transactionBillOBV.setPayType(0);
        transactionBillOBV.setTaxAmount(reimbursementF.getTaxAmount());
        transactionBillOBV.setDescription("BPM支付报销");
        transactionBillOBV.setTotalAmount(reimbursementF.getPayableAmount());

        List<ReimbursementInvoiceF> invoices = reimbursementF.getInvoices();
        long priceTaxAmount = 0;
        if (CollectionUtils.isNotEmpty(invoices)) {
            priceTaxAmount = invoices.stream().mapToLong(ReimbursementInvoiceF::getPriceTaxAmount).sum();
        }
        transactionBillOBV.setInvoiceAmount(priceTaxAmount > reimbursementF.getPayableAmount() ? reimbursementF.getPayableAmount() : priceTaxAmount);
        PayeeF payee = reimbursementF.getPayee();
        if (Objects.nonNull(payee)) {
            transactionBillOBV.setPayeeId(payee.getPayeeId());
            transactionBillOBV.setPayeeName(payee.getPayeeName());
        }
        PayerF payer = reimbursementF.getPayer();
        if (Objects.nonNull(payer)) {
            transactionBillOBV.setPayerId(payer.getPayerId());
            transactionBillOBV.setPayerName(payer.getPayerName());
        }
        transactionBillOBV.setSysSource(reimbursementF.getSysSource());
        transactionBillOBV.setTransactionBillDetails(reimbursementF.getReimbursementDetails().stream().map(item -> {
            ChargeItemE chargeItem = chargeItemMap.get(item.getChargeItemCode());
            TransactionBillDetailOBV transactionBillDetailOBV = Global.mapperFacade.map(transactionBillOBV, TransactionBillDetailOBV.class);
            transactionBillDetailOBV.setRecPayAmount(item.getReimburseAmount());
            transactionBillDetailOBV.setPayAmount(transactionBillOBV.getTotalAmount());
            transactionBillDetailOBV.setPayTime(transactionBillOBV.getPayTime());
            transactionBillDetailOBV.setChargeItemId(chargeItem.getId());
            transactionBillDetailOBV.setChargeItemName(chargeItem.getName());
            transactionBillDetailOBV.setChargeStartTime(transactionBillOBV.getStartTime());
            transactionBillDetailOBV.setChargeEndTime(transactionBillOBV.getEndTime());
            return transactionBillDetailOBV;
        }).collect(Collectors.toList()));
        return transactionBillOBV;
    }

    /**
     * 转换交易账单信息
     *
     * @param travelReimbursementF 差旅费报销信息
     * @param orgFinance           法定单位
     * @param payTime              支付时间
     * @return
     */
    public TransactionBillOBV convertTravelTransactionBillOBV(TravelReimbursementF travelReimbursementF, OrgFinanceRv orgFinance,
                                                              LocalDateTime payTime, ChargeItemE chargeItem) {
        TransactionBillOBV transactionBillOBV = new TransactionBillOBV();
        transactionBillOBV.setBillType(BillTypeEnum.付款单.getCode());
        transactionBillOBV.setOutBusNo(travelReimbursementF.getBizTransactionNo());
        if (Objects.nonNull(orgFinance)){
            transactionBillOBV.setStatutoryBodyId(orgFinance.getId());
            transactionBillOBV.setStatutoryBodyName(orgFinance.getNameCn());
        }
        transactionBillOBV.setStartTime(payTime);
        transactionBillOBV.setEndTime(payTime);
        transactionBillOBV.setPayTime(payTime);
        transactionBillOBV.setPayChannel(travelReimbursementF.getPayChannel());
        transactionBillOBV.setPayWay(travelReimbursementF.getPayWay());
        transactionBillOBV.setPayType(0);
        transactionBillOBV.setDescription("BPM差旅费报销");
        transactionBillOBV.setTotalAmount(travelReimbursementF.getPayableAmount());
        transactionBillOBV.setTaxAmount(travelReimbursementF.getTaxAmount());
        List<ReimbursementInvoiceF> invoices = travelReimbursementF.getInvoices();
        long priceTaxAmount = 0;
        if (CollectionUtils.isNotEmpty(invoices)) {
            priceTaxAmount = invoices.stream().mapToLong(ReimbursementInvoiceF::getPriceTaxAmount).sum();
        }
        transactionBillOBV.setInvoiceAmount(priceTaxAmount > travelReimbursementF.getPayableAmount() ? travelReimbursementF.getPayableAmount() : priceTaxAmount);
        PayeeF payee = travelReimbursementF.getPayee();
        if (Objects.nonNull(payee)) {
            transactionBillOBV.setPayeeId(payee.getPayeeId());
            transactionBillOBV.setPayeeName(payee.getPayeeName());
        }
        PayerF payer = travelReimbursementF.getPayer();
        if (Objects.nonNull(payer)) {
            transactionBillOBV.setPayerId(payer.getPayerId());
            transactionBillOBV.setPayerName(payer.getPayerName());
        }
        transactionBillOBV.setSysSource(travelReimbursementF.getSysSource());

        TransactionBillDetailOBV transactionBillDetailOBV = Global.mapperFacade.map(transactionBillOBV, TransactionBillDetailOBV.class);
        transactionBillDetailOBV.setChargeItemId(chargeItem.getId());
        transactionBillDetailOBV.setChargeItemName(chargeItem.getName());
        transactionBillDetailOBV.setRecPayAmount(travelReimbursementF.getPayableAmount());
        transactionBillDetailOBV.setPayAmount(transactionBillOBV.getTotalAmount());
        transactionBillDetailOBV.setPayTime(transactionBillOBV.getPayTime());
        transactionBillDetailOBV.setChargeItemId(chargeItem.getId());
        transactionBillDetailOBV.setChargeItemName(chargeItem.getName());
        transactionBillDetailOBV.setChargeStartTime(transactionBillOBV.getStartTime());
        transactionBillDetailOBV.setChargeEndTime(transactionBillOBV.getEndTime());
        transactionBillOBV.setTransactionBillDetails(List.of(transactionBillDetailOBV));

        return transactionBillOBV;
    }

    public TransactionBillOBV convertAllocateFundsTransactionBillOBV(AllocateFundsReimbursementF allocateFundsReimbursementF, OrgFinanceRv orgFinance, LocalDateTime payTime) {
        TransactionBillOBV transactionBillOBV = new TransactionBillOBV();
        transactionBillOBV.setBillType(BillTypeEnum.付款单.getCode());
        transactionBillOBV.setOutBusNo(allocateFundsReimbursementF.getBizTransactionNo());
        transactionBillOBV.setStatutoryBodyId(orgFinance.getId());
        transactionBillOBV.setStatutoryBodyName(orgFinance.getNameCn());
        transactionBillOBV.setStartTime(payTime);
        transactionBillOBV.setEndTime(payTime);
        transactionBillOBV.setPayTime(payTime);
        transactionBillOBV.setPayChannel(allocateFundsReimbursementF.getPayChannel());
        transactionBillOBV.setPayWay(allocateFundsReimbursementF.getPayWay());
        transactionBillOBV.setPayType(0);
        transactionBillOBV.setDescription("BPM差旅费报销");
        transactionBillOBV.setTotalAmount(allocateFundsReimbursementF.getPayableAmount());
        transactionBillOBV.setTaxAmount(allocateFundsReimbursementF.getTaxAmount());
//        PayeeF payee = allocateFundsReimbursementF.getPayee();
//        if (Objects.nonNull(payee)) {
//            transactionBillOBV.setPayeeId(payee.getPayeeId());
//            transactionBillOBV.setPayeeName(payee.getPayeeName());
//        }
        PayerF payer = allocateFundsReimbursementF.getPayer();
        if (Objects.nonNull(payer)) {
            transactionBillOBV.setPayerId(payer.getPayerId());
            transactionBillOBV.setPayerName(payer.getPayerName());
        }
        transactionBillOBV.setSysSource(allocateFundsReimbursementF.getSysSource());

        TransactionBillDetailOBV transactionBillDetailOBV = Global.mapperFacade.map(transactionBillOBV, TransactionBillDetailOBV.class);
        transactionBillDetailOBV.setRecPayAmount(allocateFundsReimbursementF.getPayableAmount());
        transactionBillDetailOBV.setPayAmount(transactionBillOBV.getTotalAmount());
        transactionBillDetailOBV.setPayTime(transactionBillOBV.getPayTime());
        transactionBillDetailOBV.setChargeStartTime(transactionBillOBV.getStartTime());
        transactionBillDetailOBV.setChargeEndTime(transactionBillOBV.getEndTime());
        transactionBillOBV.setTransactionBillDetails(List.of(transactionBillDetailOBV));

        return transactionBillOBV;
    }

    /**
     * 获取资金上缴交易单信息
     * @param turnoverFundsReimbursementF
     * @param orgFinance
     * @param payTime
     * @return
     */
    public TransactionBillOBV convertTurnoverFundsTransactionBillOBV(TurnoverFundsReimbursementF turnoverFundsReimbursementF, OrgFinanceRv orgFinance, LocalDateTime payTime) {
        TransactionBillOBV transactionBillOBV = new TransactionBillOBV();
        transactionBillOBV.setBillType(BillTypeEnum.付款单.getCode());
        transactionBillOBV.setOutBusNo(turnoverFundsReimbursementF.getBizTransactionNo());
        transactionBillOBV.setStatutoryBodyId(orgFinance.getId());
        transactionBillOBV.setStatutoryBodyName(orgFinance.getNameCn());
        transactionBillOBV.setStartTime(payTime);
        transactionBillOBV.setEndTime(payTime);
        transactionBillOBV.setPayTime(payTime);
        transactionBillOBV.setPayChannel(turnoverFundsReimbursementF.getPayChannel());
        transactionBillOBV.setPayWay(turnoverFundsReimbursementF.getPayWay());
        transactionBillOBV.setPayType(0);
        transactionBillOBV.setDescription("BPM资金上缴");

        long totalAmount = turnoverFundsReimbursementF.getTurnoverDetails().stream().mapToLong(TurnoverDetail::getTurnoverAmount).sum();

        transactionBillOBV.setTotalAmount(totalAmount);
        //transactionBillOBV.setTaxAmount(turnoverFundsReimbursementF.getTaxAmount());
        PayeeF payee = turnoverFundsReimbursementF.getPayee();
        if (Objects.nonNull(payee)) {
            transactionBillOBV.setPayeeId(payee.getPayeeId());
            transactionBillOBV.setPayeeName(payee.getPayeeName());
        }
        //PayerF payer = turnoverFundsReimbursementF.getPayer();
        //if (Objects.nonNull(payer)) {
        //    transactionBillOBV.setPayerId(payer.getPayerId());
        //    transactionBillOBV.setPayerName(payer.getPayerName());
        //}
        transactionBillOBV.setSysSource(turnoverFundsReimbursementF.getSysSource());
        TransactionBillDetailOBV transactionBillDetailOBV = Global.mapperFacade.map(transactionBillOBV, TransactionBillDetailOBV.class);
        transactionBillDetailOBV.setRecPayAmount(totalAmount);
        transactionBillDetailOBV.setPayAmount(transactionBillOBV.getTotalAmount());
        transactionBillDetailOBV.setPayTime(transactionBillOBV.getPayTime());
        transactionBillDetailOBV.setChargeStartTime(transactionBillOBV.getStartTime());
        transactionBillDetailOBV.setChargeEndTime(transactionBillOBV.getEndTime());
        transactionBillOBV.setTransactionBillDetails(List.of(transactionBillDetailOBV));
        return transactionBillOBV;
    }

    /**
     * 获取资金上缴交易单明细信息
     * @param turnoverFundsReimbursementF
     * @param turnoverDetail
     * @param orgFinance
     * @param payTime
     * @return
     */
    public TransactionBillOBV convertTurnoverDetailTransactionBillOBV(TurnoverFundsReimbursementF turnoverFundsReimbursementF,
                                                                      TurnoverDetail turnoverDetail, OrgFinanceRv orgFinance, LocalDateTime payTime) {
        TransactionBillOBV transactionBillOBV = new TransactionBillOBV();
        transactionBillOBV.setBillType(BillTypeEnum.付款单.getCode());
        transactionBillOBV.setOutBusNo(turnoverFundsReimbursementF.getBizTransactionNo());
        if (Objects.nonNull(orgFinance)){
            transactionBillOBV.setStatutoryBodyId(orgFinance.getId());
            transactionBillOBV.setStatutoryBodyName(orgFinance.getNameCn());
        }
        transactionBillOBV.setStartTime(payTime);
        transactionBillOBV.setEndTime(payTime);
        transactionBillOBV.setPayTime(payTime);
        transactionBillOBV.setPayChannel(turnoverFundsReimbursementF.getPayChannel());
        transactionBillOBV.setPayWay(turnoverFundsReimbursementF.getPayWay());
        transactionBillOBV.setPayType(0);
        transactionBillOBV.setDescription("BPM资金上缴-"+ turnoverFundsReimbursementF.getTransactionTitle());
        transactionBillOBV.setTotalAmount(turnoverDetail.getTurnoverAmount());
        //transactionBillOBV.setTaxAmount(turnoverFundsReimbursementF.getTaxAmount());
        PayeeF payee = turnoverFundsReimbursementF.getPayee();
        if (Objects.nonNull(payee)) {
            transactionBillOBV.setPayeeId(payee.getPayeeId());
            transactionBillOBV.setPayeeName(payee.getPayeeName());
        }
        transactionBillOBV.setPayerId(turnoverDetail.getPayerId());
        transactionBillOBV.setPayerName(turnoverDetail.getPayerName());

        transactionBillOBV.setSysSource(turnoverFundsReimbursementF.getSysSource());
        TransactionBillDetailOBV transactionBillDetailOBV = Global.mapperFacade.map(transactionBillOBV, TransactionBillDetailOBV.class);
        transactionBillDetailOBV.setRecPayAmount(turnoverDetail.getTurnoverAmount());
        transactionBillDetailOBV.setPayAmount(transactionBillOBV.getTotalAmount());
        transactionBillDetailOBV.setPayTime(transactionBillOBV.getPayTime());
        transactionBillDetailOBV.setChargeStartTime(transactionBillOBV.getStartTime());
        transactionBillDetailOBV.setChargeEndTime(transactionBillOBV.getEndTime());
        transactionBillOBV.setTransactionBillDetails(List.of(transactionBillDetailOBV));
        return transactionBillOBV;
    }


    /**
     * 票据收票
     *
     * @param reimburseInvoices  报销发票信息
     * @param businessUnit       业务单元
     * @param sysSource          系统来源
     * @param transactionBillOBV 交易账单值对象
     */
    public List<InvoiceA> collectReimbursementInvoice(List<ReimbursementInvoiceF> reimburseInvoices, BusinessUnitE businessUnit,
                                                      Integer sysSource, TransactionBillOBV transactionBillOBV) {
        //1.录入发票
        List<InvoiceA> invoiceAS = new ArrayList<>();
        if (CollectionUtils.isEmpty(reimburseInvoices)) {
            return invoiceAS;
        }
        for (ReimbursementInvoiceF reimburseInvoice : reimburseInvoices) {
            InvoiceReceiptE invoiceReceipt = new InvoiceReceiptE();
            invoiceReceipt.setInvoiceReceiptNo(reimburseInvoice.getInvoiceNo());
            invoiceReceipt.setType(reimburseInvoice.getInvoiceType());
            invoiceReceipt.setTypeName(reimburseInvoice.getInvoiceTypeName());
            invoiceReceipt.setApplyTime(LocalDateTime.now());
            invoiceReceipt.setBillingTime(reimburseInvoice.getInvoiceDate());
            invoiceReceipt.setPriceTaxAmount(reimburseInvoice.getPriceTaxAmount());
            invoiceReceipt.setState(InvoiceReceiptStateEnum.开票成功.getCode());
            invoiceReceipt.setSysSource(sysSource);
            invoiceReceipt.setInvSource(InvSourceEnum.收入的发票.getCode());
            invoiceReceipt.setClaimStatus(InvoiceClaimStatusEnum.未认领.getCode());
            invoiceReceipt.setRemark(reimburseInvoice.getInvoiceContent());
            if (Objects.nonNull(businessUnit)){
                invoiceReceipt.setStatutoryBodyId(businessUnit.getId());
                invoiceReceipt.setStatutoryBodyName(businessUnit.getName());
            }
            invoiceReceipt.setClerk(reimburseInvoice.getClerk());
            InvoiceE invoice = new InvoiceE();
            invoice.setInvoiceType(InvoiceTypeEnum.蓝票.getCode());
            invoice.setInvoiceReceiptId(invoiceReceipt.getId());
            invoice.setInvoiceTitleType(Objects.nonNull(reimburseInvoice.getInvoiceTitleType()) ? reimburseInvoice.getInvoiceTitleType() : InvoiceTitleTypeEnum.企业.getCode());
            invoice.setInvoiceCode(reimburseInvoice.getInvoiceCode());
            invoice.setInvoiceNo(reimburseInvoice.getInvoiceNo());
            invoice.setNuonuoUrl(reimburseInvoice.getInvoiceUrl());
            List<InvoiceReceiptDetailE> invoiceReceiptDetails = new ArrayList<>();
            InvoiceReceiptDetailE invoiceReceiptDetail = new InvoiceReceiptDetailE();
            invoiceReceiptDetail.setInvoiceReceiptId(invoiceReceipt.getId());
            invoiceReceiptDetail.setBillId(Objects.isNull(transactionBillOBV) ? 0L : transactionBillOBV.getBillId());
            invoiceReceiptDetail.setBillNo(Objects.isNull(transactionBillOBV) ? "0" : transactionBillOBV.getBillNo());
            invoiceReceiptDetail.setGoodsName("BPM报销");
            invoiceReceiptDetail.setNum("1");
            invoiceReceiptDetail.setWithTaxFlag(1);
            invoiceReceiptDetail.setPrice(AmountUtils.toStringAmount(reimburseInvoice.getPriceTaxAmount()));
            invoiceReceiptDetail.setBillType(Objects.isNull(transactionBillOBV) ? BillTypeEnum.默认.getCode() : transactionBillOBV.getBillType());
            invoiceReceiptDetail.setInvoiceAmount(reimburseInvoice.getPriceTaxAmount());
            invoiceReceiptDetail.setPriceTaxAmount(invoiceReceipt.getPriceTaxAmount());
            invoiceReceiptDetails.add(invoiceReceiptDetail);
            invoiceAS.add(new InvoiceA(invoiceReceipt, invoice, invoiceReceiptDetails));
        }

        invoiceDomainService.collectBatchInvoice(invoiceAS);
        return invoiceAS;
    }

    /**
     * 同步发票信息
     *
     * @param invoiceAS
     * @param voucherType
     * @param voucherNo
     * @return
     */
    public boolean syncInvoice(List<InvoiceA> invoiceAS, String voucherType, String voucherNo) {
        try {
            invoiceDomainService.syncInvoiceToLingShuiTong(invoiceAS, voucherType, voucherNo);
        } catch (Exception e) {
            log.error("报销发票推送失败", e);
            return false;
        }
        return true;
    }


    /**
     * 制作报销凭证
     *
     * @param reimbursementF     报销信息
     * @param accountBook        账簿
     * @param businessUnit       业务单元
     * @param subjects           科目列表
     * @param chargeItemMap      费项信息
     */
    public Voucher makerReimbursementVoucher(ReimbursementF reimbursementF, AccountBookE accountBook, BusinessUnitE businessUnit,
                                             List<SubjectE> subjects, Map<String, ChargeItemE> chargeItemMap) {
        Voucher voucher = new Voucher();
        voucher.setMadeType(VoucherMakeTypeEnum.自动.getCode());
        voucher.setVoucherType(VoucherTypeEnum.记账凭证.getCode());
        voucher.setAccountBookId(accountBook.getId());
        voucher.setAccountBookCode(accountBook.getCode());
        voucher.setAccountBookName(accountBook.getName());
        if (Objects.nonNull(businessUnit)){
            voucher.setStatutoryBodyId(businessUnit.getId());
            voucher.setStatutoryBodyCode(businessUnit.getCode());
            voucher.setStatutoryBodyName(businessUnit.getName());
        }
        voucher.setVoucherSource(VoucherSourceEnum.BPM推送.getCode());
        voucher.setSysSource(SysSourceEnum.BPM系统.getCode());
        voucher.setCostCenters(new ArrayList<>());
        voucher.setBookkeepingDate(reimbursementF.getAccountDate());
        voucher.setEvenType(VoucherEventTypeEnum.付款结算.getCode());
        voucher.setFiscalPeriod(LocalDate.now());
        //根据费项匹配科目信息
        List<VoucherDetailOBV> details = new ArrayList<>();
        Map<String, SubjectE> subjectMap = new HashMap<>();
        for (SubjectE subject : subjects) {
            subjectMap.put(subject.getSubjectCode(), subject);
        }
        String month = DateTimeUtil.formatCNMonth(voucher.getFiscalPeriod());
        //默认摘要 “月份” + “费项集合（逗号分割）”
        String applicantName = reimbursementF.getApplicantName();
        applicantName = Objects.isNull(applicantName) ? "" : applicantName;
        String depart = getAssisteName(reimbursementF.getReimbursementDetails(), AssisteItemTypeEnum.部门);
        String community = getAssisteName(reimbursementF.getReimbursementDetails(), AssisteItemTypeEnum.项目);
        String summary = community + depart + applicantName + "报销" + month + getJoinChargeItemName(chargeItemMap.values());
        //报销明细分录
        details.addAll(getVoucherDetailsByReiDetails(reimbursementF.getReimbursementDetails(), subjectMap,
                chargeItemMap, summary, reimbursementF.isShare()));
        //进项税额分录，如果是否分摊为否则进项税额通过这里来取
        if (!reimbursementF.isShare()) {
            details.addAll(getVoucherDetailsByInoutTax(reimbursementF.getInputTaxDetails(), subjectMap, summary));
        }
        //备用金逻辑 2023-06-18
        ReimbursementPettyCashF pettyCash = reimbursementF.getPettyCash();
        if (Objects.nonNull(pettyCash)) {
            //添加银行存款分录
            if (pettyCash.getAmount() > 0){
                details.add(getVoucherDetailByBankAccount(VoucherLoanTypeEnum.借方, reimbursementF.getReimburseBank(), pettyCash.getAmount(), summary, subjectMap));
            }
            //备用金分录= 【财务信息->报销金额】 — 【财务信息->本次应付金额】 +【还款信息->现金还款】
            long pettyCashAmount = reimbursementF.getReimburseAmount() - reimbursementF.getPayableAmount() + pettyCash.getAmount();
            if(pettyCashAmount > 0){
                details.add(getVoucherDetailByPettyCash(pettyCash, subjectMap, summary, pettyCashAmount));
            }
        }
        if (reimbursementF.getPayableAmount() > 0){
            //银行存款
            details.add(getVoucherDetailByBankAccount(VoucherLoanTypeEnum.贷方, reimbursementF.getReimburseBank(), reimbursementF.getPayableAmount(), summary, subjectMap));
        }
        //员工报销
        //2023-03-30 远洋贺腾辉说去掉这个分录
        //details.addAll(getVoucherDetailByEmployeeReimbursement(reimbursementF.getReimbursePerson(), reimbursementF.getPayableAmount(), summary, subjectMap));

        //填充现金流量信息
        voucherDomainService.putVoucherCashFlow(details, subjectMap);
        SubjectE bankSubject = subjectMap.get(yuanYangSubjectProperties.getBankAccount());
        handleCashFlow(details, bankSubject);
        voucher.setDetails(details);
        voucher.setAmount(details.stream().mapToLong(VoucherDetailOBV::getCreditAmount).sum());
        voucher.setFiscalPeriod(LocalDate.now());
        voucher.setSyncSystem(1);
        voucher.setMakerId(reimbursementF.getMakerId());
        voucher.setMakerName(reimbursementF.getMakerName());
        voucher.setState(VoucherStateEnum.待同步.getCode());
        return voucher;
    }

    /**
     * 如果贷方只有银行存款的话，借方分录的现金流就取它的不含税金额加上它对应的税额。如果贷方除了银行存款还有别的非现金科目时，
     * 借方从上往下依次取它的不含税金额加上它对应的税额，同时和银行存款对比，如果大于银行存款了，该分录的现金流就取剩下的银行存款作为现金流。
     * @param details
     * @param bankSubject
     */
    public void handleCashFlow(List<VoucherDetailOBV> details, SubjectE bankSubject) {
        Map<String, List<VoucherDetailOBV>> loanGroup = details.stream().collect(Collectors.groupingBy(VoucherDetailOBV::getType));
        List<VoucherDetailOBV> creditDetails = loanGroup.get(VoucherLoanTypeEnum.贷方.getCode());
        List<VoucherDetailOBV> debitDetails = loanGroup.get(VoucherLoanTypeEnum.借方.getCode());
        // 贷方有银行存款且还有其他分录时
        if (CollectionUtils.size(creditDetails) > 1) {
            Optional<VoucherDetailOBV> optionalVoucherDetailOBV = creditDetails.stream()
                    .filter(detail -> bankSubject.getId().equals(detail.getSubjectId())).findFirst();
            if (optionalVoucherDetailOBV.isPresent()) {
                VoucherDetailOBV bankDetail = optionalVoucherDetailOBV.get();
                // 取银行存款与借方现金流进行比较
                long bankAmount = bankDetail.getOriginalAmount();
                for (VoucherDetailOBV debitDetail : debitDetails) {
                    List<CashFlowOBV> cashFlows = debitDetail.getCashFlows();
                    if (CollectionUtils.isNotEmpty(cashFlows)) {
                        CashFlowOBV cashFlowOBV = cashFlows.get(0);
                        long cashFlowAmount = cashFlowOBV.getMoney();
                        // 银行存款小于零时，则后面分录不再需要现金流
                        if (bankAmount <= 0) {
                            debitDetail.setCashFlows(new ArrayList<>());
                        } else if (bankAmount <= cashFlowAmount) {
                            // 银行存款小于分录现金流时，现金流金额改为银行存款金额
                            cashFlowOBV.setMoney(bankAmount);
                            bankAmount = 0;
                        } else {
                            // 正常情况，无需改动现金流金额，银行存款进行扣减继续与后续现金流比较
                            bankAmount = bankAmount - cashFlowAmount;
                        }
                    }
                }
            }
        }
    }

    /**
     * 获取辅助核算名称
     * @param reimbursementDetails
     * @return
     */
    public String getAssisteName(List<ReimbursementDetailF> reimbursementDetails, AssisteItemTypeEnum assisteItemTypeEnum) {
        if (CollectionUtils.isNotEmpty(reimbursementDetails)) {
            for (ReimbursementDetailF detailF : reimbursementDetails) {
                List<ReimbursementAssisteItemF> assisteItems = detailF.getAssisteItems();
                if (CollectionUtils.isNotEmpty(assisteItems)) {
                    for (ReimbursementAssisteItemF assisteItem : assisteItems) {
                        if (assisteItemTypeEnum.equalsByCode(assisteItem.getType())) {
                            return assisteItem.getName();
                        }
                    }
                }
            }
        }
        return "";
    }

    /**
     * 对现金流特殊处理
     * @param details
     * @return
     */
    public List<VoucherDetailOBV> editCashFlow(List<VoucherDetailOBV> details) {
        // 代收代付类别的现金流取负值
        details.forEach(detail -> {
            if (CollectionUtils.isNotEmpty(detail.getCashFlows())) {
                ChargeItemE chargeItemE = chargeItemDomainService.getById(detail.getChargeItemId());
                if(ChargeItemAttributeEnum.代收代付及其他.getCode().equals(chargeItemE.getAttribute())){
                    detail.getCashFlows().stream()
                            .filter(flow -> flow.getMoney() > 0)
                            .forEach(flow -> flow.setMoney(Math.negateExact(flow.getMoney())));
                }
            }
        });
        return details;
    }

    /**
     * 制作差旅费的凭证
     * @param travelReimbursementF
     * @param accountBook
     * @param businessUnit
     * @param subjects
     * @param chargeItem
     * @param taxRateMap
     * @return
     */
    public Voucher makerTravelReimbursementVoucher(TravelReimbursementF travelReimbursementF, AccountBookE accountBook, BusinessUnitE businessUnit,
                                                   List<SubjectE> subjects, ChargeItemE chargeItem, Map<String, TaxRateE> taxRateMap) {
        Voucher voucher = new Voucher();
        voucher.setMadeType(VoucherMakeTypeEnum.自动.getCode());
        voucher.setVoucherType(VoucherTypeEnum.记账凭证.getCode());
        voucher.setAccountBookId(accountBook.getId());
        voucher.setAccountBookCode(accountBook.getCode());
        voucher.setAccountBookName(accountBook.getName());
        if (Objects.nonNull(businessUnit)){
            voucher.setStatutoryBodyId(businessUnit.getId());
            voucher.setStatutoryBodyCode(businessUnit.getCode());
            voucher.setStatutoryBodyName(businessUnit.getName());
        }
        voucher.setVoucherSource(VoucherSourceEnum.BPM推送.getCode());
        voucher.setSysSource(SysSourceEnum.BPM系统.getCode());
        voucher.setCostCenters(new ArrayList<>());
        voucher.setFiscalPeriod(LocalDate.now());
        voucher.setBookkeepingDate(travelReimbursementF.getAccountDate());
        voucher.setEvenType(VoucherEventTypeEnum.付款结算.getCode());
        //根据费项匹配科目信息
        List<VoucherDetailOBV> details = new ArrayList<>();
        Map<String, SubjectE> subjectMap = new HashMap<>();
        for (SubjectE subject : subjects) {
            subjectMap.put(subject.getSubjectCode(), subject);
        }
        String month = DateTimeUtil.formatCNMonth(voucher.getFiscalPeriod());
        //默认摘要 “月份” + “费项集合（逗号分割）”
        String applicantName = travelReimbursementF.getApplicantName();
        applicantName = Objects.isNull(applicantName) ? "" : applicantName;
        String depart = Objects.isNull(travelReimbursementF.getOrgName()) ? "" : travelReimbursementF.getOrgName();
        String community = Objects.isNull(travelReimbursementF.getCostCenterName()) ? "" : travelReimbursementF.getCostCenterName();
        String summary = community + depart + applicantName + "报销" + month + chargeItem.getName();
        //报销分录
        if(travelReimbursementF.getReimburseNoRateAmount() > 0){
            details.add(generateReimburseVoucherDetail(travelReimbursementF.getCostCenterCode(), travelReimbursementF.getCostCenterName(),
                    travelReimbursementF.getOrgCode(), travelReimbursementF.getOrgName(),
                    travelReimbursementF.getBusinessTypeCode(), travelReimbursementF.getBusinessTypeName(),
                    travelReimbursementF.getReimburseNoRateAmount(), travelReimbursementF.getReimburseAmount(), summary, subjectMap.get(travelReimbursementF.getSubjectCode())));
        }
        //进项税额分录
        details.addAll(getVoucherDetailsByTravelInoutTax(travelReimbursementF.getCostCenterCode(), travelReimbursementF.getCostCenterName(),
                travelReimbursementF.getInputTaxDetails(), subjectMap, summary, taxRateMap));
        //银行存款
        if(travelReimbursementF.getPayableAmount() > 0){
            details.add(generateBankAccountVoucherDetail(travelReimbursementF.getPayer().getPayerAccount(), travelReimbursementF.getPayer().getPayerName(),
                    travelReimbursementF.getPayableAmount(), summary, subjectMap.get(yuanYangSubjectProperties.getBankAccount())));
        }

        //给所有分录添加费项
        details.forEach(detail -> {
            detail.setChargeItemId(chargeItem.getId());
            detail.setChargeItemCode(chargeItem.getCode());
            detail.setChargeItemName(chargeItem.getName());
        });

        //填充现金流量信息
        voucherDomainService.putVoucherCashFlow(details, subjectMap);
        voucher.setAmount(details.stream().mapToLong(VoucherDetailOBV::getCreditAmount).sum());
        voucher.setDetails(details);
        voucher.setFiscalPeriod(LocalDate.now());
        voucher.setSyncSystem(1);
        voucher.setMakerId(travelReimbursementF.getCashReviewerId());
        voucher.setMakerName(travelReimbursementF.getCashReviewerName());
        voucher.setState(VoucherStateEnum.待同步.getCode());
        return voucher;
    }

    /**
     * 资金下拨凭证制作
     * @param allocateFundsReimbursementF
     * @param transactionOrder
     * @param accountBook
     * @param orgFinance
     * @param subjects
     * @return
     */
    public List<Voucher> makerAllocateFundsReimbursementVoucher(AllocateFundsReimbursementF allocateFundsReimbursementF,TransactionOrder transactionOrder,
                                                                AccountBookE accountBook, OrgFinanceRv orgFinance,
                                                   List<SubjectE> subjects) {

        List<Voucher> vouchers = new ArrayList<>();
        List<TransactionOrder> subOrders = transactionOrder.getSubOrders();
        for (TransactionOrder subOrder : subOrders) {
            if (!BillTransactStateEnum.交易成功.equalsByCode(subOrder.getTransactState())){
                continue;
            }
            // 付款方凭证
            Voucher voucher = new Voucher();
            voucher.setMadeType(VoucherMakeTypeEnum.自动.getCode());
            voucher.setVoucherType(VoucherTypeEnum.记账凭证.getCode());
            voucher.setVoucherSource(VoucherSourceEnum.BPM推送.getCode());
            voucher.setSysSource(SysSourceEnum.BPM系统.getCode());
            voucher.setAccountBookId(accountBook.getId());
            voucher.setAccountBookCode(accountBook.getCode());
            voucher.setAccountBookName(accountBook.getName());
            voucher.setStatutoryBodyId(orgFinance.getId());
            voucher.setStatutoryBodyCode(orgFinance.getCode());
            voucher.setStatutoryBodyName(orgFinance.getNameCn());
            voucher.setCostCenters(new ArrayList<>());
            voucher.setAmount(allocateFundsReimbursementF.getPayableAmount());
            voucher.setFiscalPeriod(LocalDate.now());
            voucher.setBookkeepingDate(allocateFundsReimbursementF.getAccountDate());
            voucher.setEvenType(VoucherEventTypeEnum.付款结算.getCode());
            //根据费项匹配科目信息
            List<VoucherDetailOBV> details = new ArrayList<>();
            Map<String, SubjectE> subjectMap = new HashMap<>();
            for (SubjectE subject : subjects) {
                subjectMap.put(subject.getSubjectCode(), subject);
            }
            String month = DateTimeUtil.formatCNMonth(voucher.getFiscalPeriod());
            //默认摘要 “月份” + “费项集合（逗号分割）”
            String applicantName = allocateFundsReimbursementF.getApplicantName();
            String summary = "下拨" + allocateFundsReimbursementF.getCostCenterName() + "往来款-流水日期";
            //银行存款
            details.add(generateBankAccountVoucherDetail(allocateFundsReimbursementF.getPayer().getPayerAccount(), allocateFundsReimbursementF.getPayer().getPayerName(),
                    allocateFundsReimbursementF.getPayableAmount(), summary, subjectMap.get(yuanYangSubjectProperties.getBankAccount())));
            //填充现金流量信息
            voucherDomainService.putVoucherCashFlow(details, subjectMap);
            voucher.setDetails(details);
            voucher.setFiscalPeriod(LocalDate.now());
            voucher.setSyncSystem(1);
            voucher.setState(VoucherStateEnum.待同步.getCode());
            vouchers.add(voucher);

            // 收款方凭证
            Voucher voucherOfPayee = new Voucher();
            voucherOfPayee.setMadeType(VoucherMakeTypeEnum.自动.getCode());
            voucherOfPayee.setVoucherType(VoucherTypeEnum.记账凭证.getCode());
            voucherOfPayee.setAccountBookId(accountBook.getId());
            voucherOfPayee.setAccountBookCode(accountBook.getCode());
            voucherOfPayee.setAccountBookName(accountBook.getName());
            voucherOfPayee.setStatutoryBodyId(orgFinance.getId());
            voucherOfPayee.setStatutoryBodyCode(orgFinance.getCode());
            voucherOfPayee.setStatutoryBodyName(orgFinance.getNameCn());
            voucherOfPayee.setCostCenters(new ArrayList<>());
            voucherOfPayee.setAmount(allocateFundsReimbursementF.getPayableAmount());
            voucherOfPayee.setFiscalPeriod(LocalDate.now());
            voucherOfPayee.setBookkeepingDate(allocateFundsReimbursementF.getAccountDate());
            voucherOfPayee.setEvenType(VoucherEventTypeEnum.付款结算.getCode());
            //根据费项匹配科目信息
            List<VoucherDetailOBV> detailsOfPayee = new ArrayList<>();
            //银行存款
            details.add(generateBankAccountVoucherDetail(allocateFundsReimbursementF.getPayer().getPayerAccount(), allocateFundsReimbursementF.getPayer().getPayerName(),
                    allocateFundsReimbursementF.getPayableAmount(), summary, subjectMap.get(yuanYangSubjectProperties.getBankAccount())));
            //填充现金流量信息
            voucherDomainService.putVoucherCashFlow(details, subjectMap);
            voucherOfPayee.setDetails(details);
            voucherOfPayee.setFiscalPeriod(LocalDate.now());
            voucherOfPayee.setSyncSystem(1);
            voucherOfPayee.setState(VoucherStateEnum.待同步.getCode());
            vouchers.add(voucherOfPayee);
        }

        return vouchers;
    }


    /**
     * 保存且同步凭证
     *
     * @param voucher
     * @param voucherFlag
     * @return
     */
    public Voucher saveAndSyncVoucher(Voucher voucher, int voucherFlag) {
        Long voucherId = voucherDomainService.makeVoucher(voucher);
        if (voucherFlag == 1) {
            try {
                voucher = voucherDomainService.syncVoucher(voucherId, VoucherSystemEnum.用友NCC);
            } catch (Exception e) {
                log.error("远洋BPM报销凭证同步异常：", e);
            }
        }
        return voucher;
    }

    /**
     * 制作并保存业务单据信息
     *
     * @param payBill
     * @param voucher
     */
    public void makeAndSaveVoucherDetail(PayBill payBill, Voucher voucher) {
        VoucherBusinessBill businessBill = Global.mapperFacade.map(payBill, VoucherBusinessBill.class);
        businessBill.setBusinessBillId(payBill.getId());
        businessBill.setStatutoryBodyId(payBill.getStatutoryBodyId());
        businessBill.setStatutoryBodyName(payBill.getStatutoryBodyName());
        businessBill.setBusinessBillType(BillTypeEnum.付款单.getCode());
        businessBill.setTotalAmount(payBill.getTotalAmount());
        businessBill.setReceivableAmount(payBill.getTotalAmount());
        businessBill.setDeductibleAmount(0L);
        businessBill.setDiscountAmount(0L);
        businessBill.setActualPayAmount(payBill.getTotalAmount());
        businessBill.setTaxAmount(payBill.getTaxAmount());
        businessBill.setTaxRateId(payBill.getTaxRateId());
        businessBill.setTaxRate(payBill.getTaxRate());
        businessBill.setAccountYear(payBill.getAccountYear());
        businessBill.setAccountDate(payBill.getAccountDate());

        VoucherBusinessDetail detail = new VoucherBusinessDetail(voucher.getId(), payBill.getId(), payBill.getBillNo(),
                VoucherBusinessBillTypeEnum.付款单.getCode(), null, null, businessBill, null, null,null,null, voucher.getAccountBookId(),payBill.getPayChannel());
        voucherDomainService.saveBusinessDetail(List.of(detail));
    }

    /**
     * 根据进项税额信息获取分录详情
     *
     * @param inputTaxDetails 报销进项税额明细
     * @param subjectMap      科目编码Map
     * @param summary         摘要
     * @return 分录详情列表
     */
    private List<VoucherDetailOBV> getVoucherDetailsByInoutTax(List<ReimbursementInputTaxF> inputTaxDetails, Map<String, SubjectE> subjectMap, String summary) {
        List<VoucherDetailOBV> voucherDetails = new ArrayList<>();
        //进项税额
        if (CollectionUtils.isNotEmpty(inputTaxDetails)) {
            String subjectCode = null;
            for (ReimbursementInputTaxF inputTaxDetail : inputTaxDetails) {
                subjectCode = "计算扣除进项税额".equals(inputTaxDetail.getTaxRateDes()) ?
                        yuanYangSubjectProperties.getCalDeductInputTax() : yuanYangSubjectProperties.getInputTax();
                if(!new VoucherDetailOBV().doesTheAmountExist(inputTaxDetail.getTaxAmount())){
                    continue;
                }
                voucherDetails.add(convertVoucherDetail(VoucherLoanTypeEnum.借方, subjectMap.get(subjectCode), inputTaxDetail.getTaxAmount(), summary, inputTaxDetail.getAssisteItems()));
            }
        }
        return voucherDetails;
    }

    /**
     * 根据差旅费进项税额信息获取分录详情
     *
     * @param inputTaxDetails 报销进项税额明细
     * @param subjectMap      科目编码Map
     * @param summary         摘要
     * @return 分录详情列表
     */
    private List<VoucherDetailOBV> getVoucherDetailsByTravelInoutTax(String costCenterCode, String costCenterName,
                                                                     List<TravelReimbursementInputTaxF> inputTaxDetails,
                                                                     Map<String, SubjectE> subjectMap, String summary, Map<String, TaxRateE> taxRateMap) {
        List<VoucherDetailOBV> voucherDetails = new ArrayList<>();
        //进项税额
        if (CollectionUtils.isNotEmpty(inputTaxDetails)) {
            String subjectCode = null;
            List<AssisteItemOBV> assisteItems = null;
            for (TravelReimbursementInputTaxF inputTaxDetail : inputTaxDetails) {
                subjectCode = "计算扣除进项税额".equals(inputTaxDetail.getTaxRateDes()) ?
                        yuanYangSubjectProperties.getCalDeductInputTax() : yuanYangSubjectProperties.getInputTax();
                TaxRateE taxRateE = taxRateMap.get(inputTaxDetail.getTaxRate());
                assisteItems = new ArrayList<>();
                assisteItems.add(new AssisteItemOBV(AssisteItemTypeEnum.项目, costCenterCode, costCenterName));
                if (Objects.nonNull(taxRateE)){
                    assisteItems.add(new AssisteItemOBV(AssisteItemTypeEnum.增值税税率, taxRateE.getCode(), taxRateE.getRate() + "%"));
                }
                if(!new VoucherDetailOBV().doesTheAmountExist(inputTaxDetail.getTaxAmount())){
                    continue;
                }
                voucherDetails.add(generateVoucherDetail(VoucherLoanTypeEnum.借方, subjectMap.get(subjectCode),
                        inputTaxDetail.getTaxAmount(), inputTaxDetail.getTaxAmount(), summary, assisteItems));
            }
        }
        return voucherDetails;
    }


    /**
     * 根据报销明细获取分录详情
     *
     * @param reimbursementDetails 报销明细列表
     * @param subjectMap           科目编码Map
     * @param summary              摘要
     * @return 分录详情列表
     */
    private List<VoucherDetailOBV> getVoucherDetailsByReiDetails(List<ReimbursementDetailF> reimbursementDetails,
                                                                 Map<String, SubjectE> subjectMap,
                                                                 Map<String, ChargeItemE> chargeItemMap, String summary,
                                                                 boolean isShare) {
        List<VoucherDetailOBV> voucherDetails = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(reimbursementDetails)) {
            reimbursementDetails.forEach(item -> {
                ChargeItemE chargeItem = chargeItemMap.get(item.getChargeItemCode());
                if(!new VoucherDetailOBV().doesTheAmountExist(item.getExcTaxAmount())){
                    return;
                }
                VoucherDetailOBV voucherDetailOBV = convertVoucherDetail(VoucherLoanTypeEnum.借方, subjectMap.get(item.getSubjectCode()),
                        item.getExcTaxAmount(), summary, item.getAssisteItems());
                voucherDetailOBV.setChargeItemId(chargeItem.getId());
                voucherDetailOBV.setChargeItemCode(chargeItem.getCode());
                voucherDetailOBV.setChargeItemName(chargeItem.getName());
                voucherDetailOBV.setIncTaxAmount(item.getReimburseAmount());
                voucherDetails.add(voucherDetailOBV);
                if (isShare && item.getTaxAmount() != null && item.getTaxAmount() > 0) {
                    String subjectCode = null;
                    subjectCode = "计算扣除进项税额".equals(item.getTaxRateDes()) ?
                            yuanYangSubjectProperties.getCalDeductInputTax() : yuanYangSubjectProperties.getInputTax();
                    voucherDetails.add(convertVoucherDetail(VoucherLoanTypeEnum.借方, subjectMap.get(subjectCode), item.getTaxAmount(), summary, item.getAssisteItems()));

                }
                List<SubjectMapUnitDetailE> subjectMapUnitDetailES = subjectMapUnitDetailRepository.getChargeItemIdBySubjectMapRuleId(chargeItem.getId());
                if (CollectionUtils.isNotEmpty(subjectMapUnitDetailES)) {
                    SubjectE subjectOneE = subjectDomainService.getByCode("2141");
                    for (SubjectMapUnitDetailE subjectMapUnitDetailE : subjectMapUnitDetailES) {
                         if(subjectOneE.getId().equals(subjectMapUnitDetailE.getSubjectLevelOneId())){
                             SubjectE subjectE = subjectDomainService.getBySubjectLastId(subjectMapUnitDetailE.getSubjectLevelLastId());
                             if(Objects.nonNull(subjectE)){
                                 List<SubjectE> subjects = subjectDomainService.getSubjectsFullByCodes(Lists.newArrayList(subjectE.getSubjectCode()));

                                 subjectMap.put(subjects.get(0).getSubjectCode(),subjects.get(0));
                                 //借方
                                 voucherDetails.add(convertVoucherDetail(VoucherLoanTypeEnum.借方, subjects.get(0),
                                         item.getExcTaxAmount()+item.getTaxAmount(), summary, item.getAssisteItems()));
                                 //贷方
                                 voucherDetails.add(convertVoucherDetail(VoucherLoanTypeEnum.贷方, subjects.get(0),
                                         item.getExcTaxAmount()+item.getTaxAmount(), summary, item.getAssisteItems()));
                             }
                         }
                    }
                }
            });
        }
        return voucherDetails;
    }

    /**
     * 根据备用金明细获取分录详情
     *
     * @param pettyCash  备用金明细
     * @param subjectMap 科目编码Map
     * @param summary    摘要
     * @param amount 备用金金额 + 本次应付金额
     * @return 分录详情
     */
    private VoucherDetailOBV getVoucherDetailByPettyCash(ReimbursementPettyCashF pettyCash, Map<String, SubjectE> subjectMap, String summary, long amount) {
        return convertVoucherDetail(VoucherLoanTypeEnum.贷方, subjectMap.get(yuanYangSubjectProperties.getPettyCash()),
                amount, summary, pettyCash.getAssisteItems());
    }

    /**
     * 根据员工报销信息获取分录详情
     *
     * @param reimbursePerson 辅助核算项明细
     * @param amount          分录金额
     * @param summary         摘要
     * @param subjectMap      科目编码Map
     * @return 分录详情
     */
    private List<VoucherDetailOBV> getVoucherDetailByEmployeeReimbursement(ReimbursementPersonF reimbursePerson,
                                                                           Long amount, String summary, Map<String, SubjectE> subjectMap) {
        List<VoucherDetailOBV> res = new ArrayList<>();
        res.add(convertVoucherDetail(VoucherLoanTypeEnum.贷方, subjectMap.get(yuanYangSubjectProperties.getEmployeeReimbursement()), amount, summary, reimbursePerson.getAssisteItems()));
        res.add(convertVoucherDetail(VoucherLoanTypeEnum.借方, subjectMap.get(yuanYangSubjectProperties.getEmployeeReimbursement()), amount, summary, reimbursePerson.getAssisteItems()));
        return res;
    }

    /**
     * 根据银行存款获取分录详情
     *
     * @param loanType 借贷方向
     * @param reimburseBank 银行存款信息
     * @param amount        分录金额
     * @param summary       摘要
     * @param subjectMap    科目编码Map
     * @return 分录详情
     */
    private VoucherDetailOBV getVoucherDetailByBankAccount(VoucherLoanTypeEnum loanType, ReimbursementBankF reimburseBank, Long amount, String summary, Map<String, SubjectE> subjectMap) {
        return convertVoucherDetail(loanType, subjectMap.get(yuanYangSubjectProperties.getBankAccount()), amount, summary, reimburseBank.getAssisteItems());
    }

    /**
     * 生成银行账户分录
     * @param bankAccount 银行账户
     * @param bankName 银行账户名称
     * @param amount 金额
     * @param summary 摘要
     * @param subject 科目
     * @return 分录信息
     */
    private VoucherDetailOBV generateBankAccountVoucherDetail(String bankAccount, String bankName, Long amount, String summary, SubjectE subject) {
        return generateVoucherDetail(VoucherLoanTypeEnum.贷方, subject, amount, amount,summary,
                List.of(new AssisteItemOBV(AssisteItemTypeEnum.银行账户, bankAccount, bankName),
                        new AssisteItemOBV(AssisteItemTypeEnum.存款账户性质, "01", "活期")));
    }


    /**
     * 生成报销分录
     * @param costCenterCode 核算成本中心编码
     * @param costCenterName 核算成本中心名称
     * @param orgCode 部门编码
     * @param orgName 部门明湖曾
     * @param businessTypeCode 业务类型编码
     * @param businessTypeName 业务类型名称
     * @param amount 金额
     * @param summary 摘要
     * @param subject 科目
     * @return 分录明细
     */
    private VoucherDetailOBV generateReimburseVoucherDetail(String costCenterCode, String costCenterName, String orgCode, String orgName,
                                                            String businessTypeCode, String businessTypeName, Long amount,  long incTaxAmount,
                                                            String summary, SubjectE subject) {
        List<AssisteItemOBV> assisteItems = new ArrayList<>();
        assisteItems.add(new AssisteItemOBV(AssisteItemTypeEnum.项目, costCenterCode, costCenterName));
        assisteItems.add(new AssisteItemOBV(AssisteItemTypeEnum.部门, orgCode, orgName));
        assisteItems.add(new AssisteItemOBV(AssisteItemTypeEnum.业务类型, businessTypeCode, businessTypeName));
        return generateVoucherDetail(VoucherLoanTypeEnum.借方, subject, amount, incTaxAmount, summary, assisteItems);
    }

    /**
     * 生成分录信息
     *
     * @param loanType     借贷类型
     * @param subject      科目
     * @param amount       借贷金额
     * @param summary      摘要
     * @param assisteItems 辅助核算信息
     * @return
     */
    private VoucherDetailOBV generateVoucherDetail(VoucherLoanTypeEnum loanType, SubjectE subject, Long amount, long incTaxAmount,
                                                   String summary, List<AssisteItemOBV> assisteItems) {
        VoucherDetailOBV voucherDetailOBV = new VoucherDetailOBV();
        voucherDetailOBV.setType(loanType.getCode());
        voucherDetailOBV.setSubjectId(subject.getId());
        voucherDetailOBV.setSubjectCode(subject.getSubjectCode());
        voucherDetailOBV.setSubjectName(subject.getFullName());
        voucherDetailOBV.setOriginalAmount(amount);
        if (VoucherLoanTypeEnum.借方 == loanType) {
            voucherDetailOBV.setCreditAmount(0L);
            voucherDetailOBV.setDebitAmount(amount);
        } else {
            voucherDetailOBV.setCreditAmount(amount);
            voucherDetailOBV.setDebitAmount(0L);
        }
        voucherDetailOBV.setSummary(summary);
        voucherDetailOBV.setIncTaxAmount(incTaxAmount);
        voucherDetailOBV.setAssisteItems(assisteItems);
        return voucherDetailOBV;
    }


    /**
     * 分录详情转换
     *
     * @param loanType     借贷类型
     * @param subject      科目
     * @param amount       借贷金额
     * @param summary      摘要
     * @param assisteItems
     * @return
     */
    private VoucherDetailOBV convertVoucherDetail(VoucherLoanTypeEnum loanType, SubjectE subject, Long amount,
                                                  String summary, List<ReimbursementAssisteItemF> assisteItems) {
        VoucherDetailOBV voucherDetailOBV = new VoucherDetailOBV();
        voucherDetailOBV.setType(loanType.getCode());
        voucherDetailOBV.setSubjectId(subject.getId());
        voucherDetailOBV.setSubjectCode(subject.getSubjectCode());
        voucherDetailOBV.setSubjectName(subject.getFullName());
        voucherDetailOBV.setOriginalAmount(amount);
        if (VoucherLoanTypeEnum.借方 == loanType) {
            voucherDetailOBV.setCreditAmount(0L);
            voucherDetailOBV.setDebitAmount(amount);
        } else {
            voucherDetailOBV.setCreditAmount(amount);
            voucherDetailOBV.setDebitAmount(0L);
        }
        voucherDetailOBV.setSummary(summary);
        voucherDetailOBV.setAssisteItems(getReimbursementAssisteItemBySubject(subject, assisteItems));
        return voucherDetailOBV;
    }


    /**
     * 根据科目获取辅助核算信息
     *
     * @param subject                   科目
     * @param reimbursementAssisteItems 辅助核算明细
     * @return 辅助核算列表
     */
    private List<AssisteItemOBV> getReimbursementAssisteItemBySubject(SubjectE subject, List<ReimbursementAssisteItemF> reimbursementAssisteItems) {
        List<Integer> types = subject.convertWithAuxiliaryCount();
        Map<Integer, ReimbursementAssisteItemF> itemCollect = reimbursementAssisteItems.stream().
                collect(Collectors.toMap(ReimbursementAssisteItemF::getType, t -> t,(key1,key2) -> key1));
        if (CollectionUtils.isNotEmpty(types)) {
            return types.stream().map(type ->{
                AssisteItemTypeEnum assisteItemType = AssisteItemTypeEnum.valueOfByCode(type);
                AssisteItemOBV assisteItemOBV = new AssisteItemOBV();
                assisteItemOBV.setType(assisteItemType.getCode());
                if(itemCollect.containsKey(type)){
                    ReimbursementAssisteItemF item = itemCollect.get(type);
                    assisteItemOBV.setCode(item.getCode());
                    assisteItemOBV.setName(item.getName());
                }
                assisteItemOBV.setAscCode(assisteItemType.getAscCode());
                assisteItemOBV.setAscName(assisteItemType.getValue());
                return assisteItemOBV;
            }).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    /**
     * 获取推凭状态
     *
     * @param voucherState
     * @return
     */
    private BillTransactVoucherStateEnum getTransactVoucherState(int voucherState) {
        VoucherStateEnum voucherStateEnum = VoucherStateEnum.valueOfByCode(voucherState);
        switch (voucherStateEnum) {
            case 失败:
                return BillTransactVoucherStateEnum.推凭失败;
            case 成功:
                return BillTransactVoucherStateEnum.已推凭;
            case 同步中:
                return BillTransactVoucherStateEnum.推凭中;
            default:
                return BillTransactVoucherStateEnum.未推凭;
        }
    }

    /**
     * 获取推凭状态
     *
     * @param vouchers
     * @return
     */
    private BillTransactVoucherStateEnum getTransactVoucherState(List<Voucher> vouchers) {
        BillTransactVoucherStateEnum state = BillTransactVoucherStateEnum.已推凭;
        for (Voucher voucher : vouchers) {
            VoucherStateEnum voucherStateEnum = VoucherStateEnum.valueOfByCode(voucher.getState());
            switch (voucherStateEnum) {
                case 失败:
                    return BillTransactVoucherStateEnum.推凭失败;
                case 同步中:
                    return BillTransactVoucherStateEnum.推凭中;
                default:
                    return BillTransactVoucherStateEnum.未推凭;
            }
        }
        return state;
    }


    private String getJoinChargeItemName(Collection<ChargeItemE> chargeItems){
        return  String.join("、", chargeItems.stream().map(ChargeItemE::getName).collect(Collectors.toList()));
    }

    /**
     * 报销交易回调
     *
     * @param reimbursementCallbackF
     * @param callbackCommand 支付回调命令
     */
    public void transactCallback(ReimbursementCallbackF reimbursementCallbackF, TransactionCallbackCommand callbackCommand) {
        IdentityInfo identityInfo = new IdentityInfo();
        identityInfo.setTenantId(reimbursementCallbackF.getTenantId());
        ThreadLocalUtil.set("IdentityInfo", identityInfo);
        Boolean result = false;
        switch (ReimburseType.valueOf(reimbursementCallbackF.getType())){
            case REIMBURSE:
                ReimburseCompensateF reimbursement = reimbursementCallbackF.parseReimbursement();
                result = doAfterReimburse(reimbursement, null, callbackCommand);
                break;
            case TRAVEL:
                TravelReimburseCompensateF travelReimburse = reimbursementCallbackF.parseTravelReimburse();
                result = doAfterTravelReimburse(travelReimburse, null, callbackCommand);
                break;
            // 资金上缴
            case TURNOVER:
                TurnoverFundsCompensateF turnoverFunds = reimbursementCallbackF.parseTurnoverFunds();
                result = doAfterTurnoverReimburse(turnoverFunds, null, callbackCommand);
                break;
            // 资金下拨
            case ALLOCATE:
                AllocateFundsCompensateF allocateFunds = reimbursementCallbackF.parseAllocateFunds();
                result = doAfterAllocateFundsReimburse(allocateFunds,null,callbackCommand);
                break;
            case GENERAL:
                BusinessFundsCompensateF businessFunds = reimbursementCallbackF.parseBusinessFunds();
                result = doAfterGeneralBusiness(businessFunds, callbackCommand);
                break;
        }
        if (result) {
            RedisHelper.delete(ReimbursementAppService.REIMBURSE_CACHE_PREFIX + callbackCommand.getMchOrderNo());
        }
    }

    /**
     * 同步报销收费
     * @param bizTransactionNo
     * @return
     */
    public ReimbursementSyncVoucherV reimburseSyncVoucher(String bizTransactionNo) {
        //1.获取报销交易信息
        TransactionOrder transactionOrder = billPaymentDomainService.getTransactionOrderByBizNo(bizTransactionNo);
        ErrorAssertUtil.notNullThrow403(transactionOrder, ErrorMessage.PAYMENT_TRADED_NOT_EXIST);
        ErrorAssertUtil.isTrueThrow402(BillTransactStateEnum.交易成功.equalsByCode(transactionOrder.getTransactState()), ErrorMessage.PAYMENT_STATE_ERROR);
        //2.同步凭证信息
        List<Long> voucherIds = transactionOrder.getVoucherIds();
        if (CollectionUtils.isNotEmpty(voucherIds)){
            if (voucherIds.size() == 1){
                Long voucherId = voucherIds.get(0);
                Voucher voucher = voucherDomainService.syncVoucher(voucherId, VoucherSystemEnum.用友NCC);
                //从BPM流程的角度，单笔凭证下才会有发票，所以只要做单笔凭证下的发票同步即可
                if (VoucherStateEnum.成功.equalsByCode(voucher.getState())){
                    List<Long> invoiceIds = transactionOrder.getInvoiceIds();
                    if (CollectionUtils.isNotEmpty(invoiceIds)){
                        List<InvoiceA> invoiceAS = invoiceDomainService.getListByIds(invoiceIds);
                        //同步票据到进项发票系统
                        syncInvoice(invoiceAS, String.valueOf(voucher.getVoucherType()), voucher.getSyncSystemVoucherNo());
                    }
                    return new ReimbursementSyncVoucherV().setSuccess(true)
                            .addVoucherInfo(bizTransactionNo, voucher.getSyncSystemVoucherNo(), voucher.getVoucherNo());
                }
            }
        }
        return new ReimbursementSyncVoucherV().setSuccess(false);
    }

    /**
     * 同步报销收费
     * @param bizTransactionNo
     * @return
     */
    public BusinessSyncVoucherV businessSync(String bizTransactionNo) {
        //1.获取报销交易信息
        BusinessBill businessBill = businessBillDomainService.getByBusinessId(bizTransactionNo);
        ErrorAssertUtil.notNullThrow403(businessBill, ErrorMessage.PAYMENT_TRADED_NOT_EXIST);
        //2.同步凭证信息
        BusinessSyncVoucherV businessSyncVoucherV = new BusinessSyncVoucherV();
        List<Long> voucherIds = businessBill.getVoucherIds();
        if (CollectionUtils.isNotEmpty(voucherIds)){
            ProcessBankInfoF bankInfo = businessBill.getBankInfo();
            List<String> payIds;
            if (Objects.nonNull(bankInfo)) {
                List<ProcessBankPayInfoF> payBankInfos = bankInfo.getPayBankInfos();
                payIds = payBankInfos.stream().map(ProcessBankPayInfoF::getPayId).collect(Collectors.toList());
            } else {
                payIds = new ArrayList<>();
            }
            List<TransactionOrder> transactionOrders = transactionOrderRepository.getByBizTransactionNos(payIds);
            List<Voucher> vouchers = new ArrayList<>();
            for (Long voucherId : voucherIds) {
                Voucher voucher = voucherDomainService.syncVoucher(voucherId, VoucherSystemEnum.用友NCC);
                vouchers.add(voucher);
                List<String> matchedPayIds = transactionOrders.stream()
                        .filter(order -> order.getVoucherIds().contains(voucherId))
                        .map(TransactionOrder::getBizTransactionNo)
                        .collect(Collectors.toList());
                businessSyncVoucherV.addVoucherInfo(matchedPayIds, voucher.getSyncSystemVoucherNo(), voucher.getVoucherNo());
            }
            //从BPM流程的角度，单笔凭证下才会有发票，所以只要做单笔凭证下的发票同步即可
            if (vouchers.stream().allMatch(voucher -> VoucherStateEnum.成功.equalsByCode(voucher.getState()))){
                return businessSyncVoucherV.setBizTransactionNo(businessBill.getBusinessId()).setSuccess(true);
//                    List<Long> invoiceIds = transactionOrder.getInvoiceIds();
//                    if (CollectionUtils.isNotEmpty(invoiceIds)){
//                        List<InvoiceA> invoiceAS = invoiceDomainService.getListByIds(invoiceIds);
//                        //同步票据到进项发票系统
//                        syncInvoice(invoiceAS, String.valueOf(voucher.getVoucherType()), voucher.getSyncSystemVoucherNo());
//                    }
//                    return new ReimbursementSyncVoucherV().setSuccess(true).addVoucherInfo(bizTransactionNo, voucher.getSyncSystemVoucherNo());
            }

        }
        return businessSyncVoucherV.setBizTransactionNo(businessBill.getBusinessId()).setSuccess(false);
    }





    /**
     * BPM凭证流程统一入口
     * @param businessProcessHandleF BPM凭证流程统一入参
     * @return
     */
    @Transactional
    public ProcessVoucherResultV businessProcessHandle(BusinessProcessHandleF businessProcessHandleF) {
        // 入参校验
        BpmProcessData processData = checkParam(businessProcessHandleF);
        // 支付参数填充
        List<TransactionOrder> transactionOrders = new ArrayList<>();
        TransactionOrder transactionOrder = buildTransactionOrder(businessProcessHandleF, transactionOrders);
        // 业务单据数据处理
        handleBusinessBill(businessProcessHandleF, processData);
        //发起支付（可能没有支付）
        return initiatePaymentAndHandleResult(businessProcessHandleF, transactionOrder, transactionOrders, processData);
    }

    private ProcessVoucherResultV initiatePaymentAndHandleResult(BusinessProcessHandleF businessProcessHandleF,
                                                                 TransactionOrder transactionOrder,
                                                                 List<TransactionOrder> transactionOrders,
                                                                 BpmProcessData processData) {
        ProcessVoucherResultV processVoucherResultV = new ProcessVoucherResultV();
        processVoucherResultV.setBusinessId(businessProcessHandleF.getBusinessId());
        if (Objects.nonNull(transactionOrder)) {
            List<TransactionOrder> subOrders = transactionOrder.getSubOrders();
            try {
                billPaymentDomainService.multiTransact(transactionOrder);
            } catch (BizException e) {

                List<TransactInfoV> transactInfoVS = subOrders.stream()
                        .map(order -> new TransactInfoV().setBillNo(order.getTransactionNo())
                                .setTransactState(order.getTransactState()))
                        .collect(Collectors.toList());
                processVoucherResultV.setTransactInfos(transactInfoVS);
                processVoucherResultV.setVoucherState(transactionOrder.getVoucherState());
                processVoucherResultV.setErrCode("999999");
                processVoucherResultV.setErrMsg(e.getMessage());
                return processVoucherResultV;
            }
            Global.mapperFacade.map(transactionOrder, processVoucherResultV);
            List<TransactInfoV> transactInfoVS = transactionOrders.stream()
                    .map(order -> new TransactInfoV().setBillNo(order.getTransactionNo())
                            .setTransactState(order.getTransactState())
                            .setPayId(order.getBizTransactionNo())
                            .setMsg(order.getErrMsg()))
                    .collect(Collectors.toList());
            processVoucherResultV.setTransactInfos(transactInfoVS);
            boolean allFail = transactInfoVS.stream().allMatch(
                    transact -> BillTransactStateEnum.交易失败.equalsByCode(
                            transact.getTransactState()));
            if (allFail) {
                processVoucherResultV.setErrCode("999999");
                processVoucherResultV.setErrMsg(transactInfoVS.get(0).getMsg());
                processVoucherResultV.setState(ProcessStateEnum.失败.getCode());
            } else {
                processVoucherResultV.setState(ProcessStateEnum.成功.getCode());
            }
        } else {
            processVoucherResultV.setTransactInfos(new ArrayList<>());
            processVoucherResultV.setVoucherState(BillTransactVoucherStateEnum.未推凭.getCode());
            processVoucherResultV.setState(ProcessStateEnum.成功.getCode());
        }
        // 事务结束后执行
        TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
                    @Override
                    public void afterCommit() { // 监听事务提交完成
                        sendToMakeVoucher(transactionOrder, businessProcessHandleF, processData.getAccountBooks(), processData.getBusinessUnits());
                    }
                }
        );
        return processVoucherResultV;
    }

    private void handleBusinessBill(BusinessProcessHandleF businessProcessHandleF, BpmProcessData processData) {
        BusinessBill businessBill = Global.mapperFacade.map(businessProcessHandleF, BusinessBill.class);
        List<ProcessAccountBookF> accountBookFs = businessProcessHandleF.getAccountBooks();
        accountBookFs.forEach(ProcessAccountBookF::init);
        List<BusinessBillDetail> businessBillDetails = Global.mapperFacade.mapAsList(accountBookFs, BusinessBillDetail.class);
        // 预先制作凭证（有问题可以提前报错停止）
        makeGeneralVouchers(new BusinessFundsCompensateF()
                .setBusinessProcessHandleF(businessProcessHandleF)
                .setAccountBooks(processData.getAccountBooks())
                .setBusinessUnits(processData.getBusinessUnits()), new ArrayList<>(), businessBillDetails);
        businessBillDomainService.saveBillAndDetails(businessBill, businessBillDetails);
        middleHandle(businessProcessHandleF);
    }

    public void sendToMakeVoucher(TransactionOrder transactionOrder, BusinessProcessHandleF businessProcessHandleF,
                                  List<AccountBookE> accountBooks, List<BusinessUnitE> businessUnits) {
        AppThreadManager.execute(new AppRunnable() {
            @Override
            public void execute() {
                try {
                    if (Objects.isNull(transactionOrder)) {
                        doAfterGeneralBusiness(new BusinessFundsCompensateF()
                                        .setBusinessProcessHandleF(businessProcessHandleF)
                                        .setAccountBooks(accountBooks)
                                        .setBusinessUnits(businessUnits),
                                new TransactionCallbackCommand());
                        return;
                    }
                    for (TransactionOrder subOrder : transactionOrder.getSubOrders()) {
                        if (BillTransactStateEnum.交易成功.equalsByCode(subOrder.getTransactState())) {
                            //异步处理推凭
                            doAfterGeneralBusiness(new BusinessFundsCompensateF()
                                            .setBusinessProcessHandleF(businessProcessHandleF)
                                            .setTransactionNo(subOrder.getTransactionNo())
                                            .setAccountBooks(accountBooks)
                                            .setBusinessUnits(businessUnits),
//                                        .setSubjects(subjects),
                                    new TransactionCallbackCommand()
                                            .setMchOrderNo(subOrder.getTransactionNo())
                                            .setPayNo(subOrder.getPayNo())
                                            .setPayState(subOrder.getTransactState())
                                            .setChanelTradeNo("")
                                            .setSuccessTime(subOrder.getSuccessTime()));
                        } else if (BillTransactStateEnum.交易中.equalsByCode(subOrder.getTransactState())) {
                            log.info("交易key:{}", REIMBURSE_CACHE_PREFIX + subOrder.getTransactionNo());
                            RedisHelper.set(REIMBURSE_CACHE_PREFIX + subOrder.getTransactionNo(),
                                    JSON.toJSONString(ReimbursementCallbackF.formatBusinessFunds(new BusinessFundsCompensateF()
                                                    .setTransactionNo(subOrder.getTransactionNo())
                                                    .setBusinessProcessHandleF(businessProcessHandleF)
                                                    .setAccountBooks(accountBooks)
                                                    .setBusinessUnits(businessUnits)
                                            //                                .setSubjects(subjects)
                                    )));
                        }
                    }
                } catch (Exception e) {
                    log.error("异步处理推凭失败", e);
                }
            }
        });
    }

    public String getBusinessUintCode(String accountBookCode) {
        if (accountBookCode.lastIndexOf("-") != -1) {
            return accountBookCode.substring(0, accountBookCode.lastIndexOf("-"));
        } else {
            return accountBookCode;
        }
    }

    public void middleHandle(BusinessProcessHandleF businessProcessHandleF) {
        String billTypeCode = businessProcessHandleF.getBillTypeCode();
        if (BusinessBillTypeEnum.借款申请.equalsByCode(billTypeCode)) {
            List<ProcessAccountBookF> accountBooks = businessProcessHandleF.getAccountBooks();
            ProcessAccountBookF processAccountBookF = accountBooks.get(0);
            AddPersonF addPersonF = new AddPersonF();
            addPersonF.setPkPsndoc(null);
            addPersonF.setPkOrg(getBusinessUintCode(processAccountBookF.getAccountBookCode()));
            addPersonF.setCode(businessProcessHandleF.getApplicantCode());
            addPersonF.setName(businessProcessHandleF.getApplicant());
            addPersonF.setUsedName(null);
            addPersonF.setBirthdate(null);
            addPersonF.setSex(null);
            addPersonF.setIdtype(null);
            addPersonF.setId(null);
            addPersonF.setMnecode(null);
            addPersonF.setJoinWorkDate(null);
            addPersonF.setAddr(null);
            addPersonF.setOfficePhone(null);
            addPersonF.setHomePhone(null);
            addPersonF.setMobile(null);
            addPersonF.setEmail(null);
            addPersonF.setEnableState(2);
            addPersonF.setPkGroup("G");
            addPersonF.setDef1("01");
            NccPersonJobF nccPersonJobF = new NccPersonJobF();
            nccPersonJobF.setPkPsnJob(null);
            nccPersonJobF.setPkGroup("G");
            nccPersonJobF.setPkOrg(getBusinessUintCode(processAccountBookF.getAccountBookCode()));
            nccPersonJobF.setPsnCode(businessProcessHandleF.getApplicantCode());
            nccPersonJobF.setPkPsncl("01");
            List<ProcessChargeDetailF> chargeDetails = processAccountBookF.getChargeDetails();
            ProcessChargeDetailF processChargeDetailF = chargeDetails.get(0);
            nccPersonJobF.setPkDept(processChargeDetailF.getOrgCode());
            nccPersonJobF.setIsMainJob('Y');
            nccPersonJobF.setInDutyDate("2023-01-01");
            nccPersonJobF.setEndDutyDate(null);
            nccPersonJobF.setPkJob(null);
            nccPersonJobF.setWorkType(null);
            nccPersonJobF.setJobName(null);
            nccPersonJobF.setPkPost(null);
            List<NccPersonJobF> personJobFs = new ArrayList<>();
            personJobFs.add(nccPersonJobF);
            addPersonF.setPsnjob(personJobFs);
            nccDomainService.addNccPerson(addPersonF);
        }
    }

    public TransactionOrder buildTransactionOrder(BusinessProcessHandleF businessProcessHandleF, List<TransactionOrder> transactionOrders) {
        TransactionOrder transactionOrder = new TransactionOrder();
        ProcessBankInfoF bankInfo = businessProcessHandleF.getBankInfo();
        // 存在流程没有支付信息
        if (Objects.isNull(bankInfo)) {
            return null;
        }
        List<ProcessBankPayInfoF> payBankInfos = bankInfo.getPayBankInfos();
        for (int i = 0; i < payBankInfos.size(); i++) {
            ProcessBankPayInfoF payInfo = payBankInfos.get(i);
            Payee payee = new Payee();
            if (CollectionUtils.isNotEmpty(bankInfo.getPublicPayees())) {
                ProcessBankPublicF publicPayee = bankInfo.getPublicPayees().get(i);
                payee.setPayeeId(null);
                payee.setPayeeCode(publicPayee.getPayeeCode());
                payee.setPayeeName(publicPayee.getPayee());
                payee.setPayeePhone(null);
                payee.setPayeeAccount(publicPayee.getPayeeAccount());
                payee.setPayeeBank(publicPayee.getPayeeBank());
                payee.setBackType(null);
                payee.setBankNo(publicPayee.getPayeeUnionCode());
                payee.setReceivedAmount(publicPayee.getReceivedAmount());
            } else if (CollectionUtils.isNotEmpty(bankInfo.getPrivatePayees())) {
                ProcessBankPrivateF privatePayee = bankInfo.getPrivatePayees().get(i);
                payee.setPayeeId(null);
                payee.setPayeeCode(null);
                payee.setPayeeName(privatePayee.getPayee());
                payee.setPayeePhone(null);
                payee.setPayeeAccount(privatePayee.getPayeeAccount());
                payee.setPayeeBank(null);
                payee.setBackType(privatePayee.getPayeeBankType());
                payee.setBankNo(null);
                payee.setReceivedAmount(privatePayee.getReceivedAmount());
            }
            Payer payer = new Payer();
            payer.setPayerAccount(payInfo.getPayerAccount());
            payer.setPayerName(payInfo.getPayerName());
            Scene scene = new Scene();
            scene.setClientIp(businessProcessHandleF.getScene().getClientIp());
            scene.setDeviceId(businessProcessHandleF.getScene().getDeviceId());
            TransactionOrder subTransactionOrder = new TransactionOrder();
            subTransactionOrder.setPayNo(null);
            subTransactionOrder.setPayChannelNo(null);
            subTransactionOrder.setTransactState(1);

            subTransactionOrder.setTransactionTitle(businessProcessHandleF.getTitle());
            subTransactionOrder.setAttachParam(businessProcessHandleF.getAttachParam());
            subTransactionOrder.setNotifyUrl(businessProcessHandleF.getNotifyUrl());
            subTransactionOrder.setScene(scene);
            subTransactionOrder.setPayee(payee);
            subTransactionOrder.setSysSource(Integer.valueOf(businessProcessHandleF.getSysSource()));
            subTransactionOrder.setTransactionType(BillTransactTypeEnum.付款.getCode());
            ErrorAssertUtils.isTrueThrow300(StringUtils.isNotBlank(payInfo.getPayId()), "支付信息唯一标识id不能为空");
            subTransactionOrder.setBizTransactionNo(payInfo.getPayId());
            subTransactionOrder.setAmount(payee.getReceivedAmount());
            subTransactionOrder.setPayer(payer);
            subTransactionOrder.setPayChannel(payInfo.getPayChannel());
            subTransactionOrder.setPayWay(payInfo.getPayWay());
            subTransactionOrder.setPayMethod(payInfo.getPayMethod());
            subTransactionOrder.setVoucherIds(new ArrayList<>());
            // BPM 凭证不需要入账
            //subTransactionOrder.setBillParam();
            transactionOrders.add(subTransactionOrder);
        }
        transactionOrder.setSubOrders(transactionOrders);
        transactionOrder.setBizTransactionNo(businessProcessHandleF.getBusinessId());
        transactionOrder.setVoucherState(0);
        return transactionOrder;
    }

    private BpmProcessData checkParam(BusinessProcessHandleF businessProcessHandleF) {
        if (StrUtil.isNotBlank(businessProcessHandleF.getTitle()) && businessProcessHandleF.getTitle().length() >100){
            businessProcessHandleF.setTitle(businessProcessHandleF.getTitle().substring(0,99));
        }
        BpmProcessData processData = new BpmProcessData();
        List<ProcessAccountBookF> accountBookFs = businessProcessHandleF.getAccountBooks();
        // 校验账簿
        List<String> accountBookCodes = accountBookFs.stream().map(ProcessAccountBookF::getAccountBookCode).distinct().collect(Collectors.toList());
        List<AccountBookE> accountBooks = accountBookDomainService.getAccountBookByCodes(accountBookCodes);
        processData.setAccountBooks(accountBooks);
        List<String> sysAccountBookCodes = accountBooks.stream().map(AccountBookE::getCode).collect(Collectors.toList());
        List<String> unknownAccountBookCodes = accountBookCodes.stream().filter(i -> !sysAccountBookCodes.contains(i)).collect(Collectors.toList());
        if (!unknownAccountBookCodes.isEmpty()) {
            throw BizException.throw403("不存在编码为[" + String.join(",", unknownAccountBookCodes) + "] 的账簿");
        }
        // 校验业务单元
        List<String> sbCodes = accountBookCodes.stream().map(this::getBusinessUintCode).collect(Collectors.toList());
        List<BusinessUnitE> businessUnits = businessUnitDomainService.getByCodes(sbCodes);
        processData.setBusinessUnits(businessUnits);
        List<String> sysBusinessUnitCodes = businessUnits.stream().map(BusinessUnitE::getCode).collect(Collectors.toList());
        List<String> unKnownBusinessUnitCodes = sbCodes.stream().filter(i -> !sysBusinessUnitCodes.contains(i)).collect(Collectors.toList());
        if (!unKnownBusinessUnitCodes.isEmpty()) {
            throw BizException.throw403("不存在编码为[" + String.join(",", unKnownBusinessUnitCodes) + "] 的业务单元");
        }

        List<VoucherTemplate> templates = voucherTemplateDomainService.getBPMTemplateListByTypeCode(businessProcessHandleF.getBillTypeCode());
        if (CollectionUtils.isEmpty(templates)) {
            throw BizException.throw402("请先创建'" + businessProcessHandleF.getBillType() + "'的凭证模板");
        }
        ProcessBankInfoF bankInfo = businessProcessHandleF.getBankInfo();
        // 没有支付信息时不做校验
        if (Objects.nonNull(bankInfo)) {
            int paySize = CollectionUtils.size(bankInfo.getPayBankInfos());
            int publicPaySize = CollectionUtils.size(bankInfo.getPublicPayees());
            int privatePaySize = CollectionUtils.size(bankInfo.getPrivatePayees());
            if (paySize != publicPaySize && paySize != privatePaySize) {
                throw BizException.throw402(PROCESS_PAY_SIZE_NOT_EQUAL.msg());
            }
        }
        return processData;
    }

    /**
     * 通用流程 支付后处理
     * @param businessFundsCompensateF
     * @param callbackCommand
     */
    public Boolean doAfterGeneralBusiness(BusinessFundsCompensateF businessFundsCompensateF,
                                          TransactionCallbackCommand callbackCommand){
        log.info("BPM制作凭证回调参数：businessFundsCompensateF:{}, callbackCommand:{}",
                JSON.toJSONString(businessFundsCompensateF), JSON.toJSONString(callbackCommand));
        boolean result = false;
        BusinessProcessHandleF businessProcessHandleF = businessFundsCompensateF.getBusinessProcessHandleF();
        try {
            ProcessBankInfoF bankInfo = businessProcessHandleF.getBankInfo();
            boolean allSuccess = false;
            List<TransactionOrder> transactionOrders;
            if (Objects.nonNull(bankInfo)) {
                TransactionOrder transactionOrder = getTransactionOrderRetry(businessFundsCompensateF.getTransactionNo());
                transactionOrder.transactCallback(callbackCommand);
                billPaymentDomainService.updateTransaction(transactionOrder);
                List<String> bizNos = bankInfo.getPayBankInfos().stream().map(ProcessBankPayInfoF::getPayId).collect(Collectors.toList());
                transactionOrders = billPaymentDomainService.getTransactionOrderByBizNos(bizNos);
                allSuccess = isTransactionSuccessful(transactionOrders);
            } else {
                transactionOrders = new ArrayList<>();
                allSuccess = true;
            }
            if (allSuccess) {
                log.info("完全支付成功！！！！！");
                if (CollectionUtils.isEmpty(transactionOrders) || CollectionUtils.isEmpty(transactionOrders.get(0).getVoucherIds())) {
                    log.info("开始制作凭证");
                    BusinessBill businessBill = getBusinessBillRetry(businessProcessHandleF.getBusinessId());
                    List<BusinessBillDetail> businessBillDetails = businessBillDomainService.getDetailsByBusinessBillId(businessBill.getBillId());
                    // 制作凭证
                    List<Voucher> vouchers = makeGeneralVouchers(businessFundsCompensateF, transactionOrders, businessBillDetails);
                    List<Long> voucherIds = vouchers.stream().map(Voucher::getId).collect(Collectors.toList());
                    businessBill.setVoucherIds(voucherIds);
                    businessBillDomainService.update(businessBill, businessBillDetails);
                    List<Voucher> resultVoucher = new ArrayList<>();
                    for (Voucher voucher : vouchers) {
                        resultVoucher.add(saveAndSyncVoucher(voucher, businessProcessHandleF.getVoucherFlag()));
                    }
                    if (CollectionUtils.isNotEmpty(transactionOrders)) {
                        transactionOrders.forEach(order -> order.setVoucherState(getTransactVoucherState(resultVoucher.get(0).getState()).getCode()));
                        billPaymentDomainService.updateBatchTransaction(transactionOrders);
                    }
                    // 凭证制作完成推送消息
                    EventLifecycle.apply(EventMessage.builder().payload(new ReimburseCompletedEvent(transactionOrders,
                            resultVoucher, businessProcessHandleF.getBusinessId(), businessProcessHandleF.getBillTypeCode())).build());
                    // 凭证全部完成推送后推送票据
                    boolean voucherState = resultVoucher.stream().allMatch(voucher -> VoucherStateEnum.成功.equalsByCode(voucher.getState()));
                    if (voucherState) {
                        // 同步发票
//                        boolean syncInvoiceResult = syncInvoice(invoiceAS, String.valueOf(voucher.getVoucherType()), voucher.getSyncSystemVoucherNo());
//                        transactionOrder.setInvoiceState(syncInvoiceResult ? BillTransactInvoiceEnum.已开票.getCode() : BillTransactInvoiceEnum.开票异常.getCode());
                    }
                }
                result = true;
            }
        } catch (Exception e) {
            log.error("BPM通用流程-异步处理入账和推凭异常", e);
        }
        return result;
    }

    public boolean isTransactionSuccessful(List<TransactionOrder> transactionOrders) {
        boolean hasSuccess = false;
        for (TransactionOrder order : transactionOrders) {
            if (BillTransactStateEnum.交易成功.equalsByCode(order.getTransactState())) {
                hasSuccess = true;
            } else if (!BillTransactStateEnum.交易失败.equalsByCode(order.getInvoiceState())) {
                return false; // 存在其他状态，判断为失败
            }
        }
        return hasSuccess; // 全部成功或者部分成功部分失败
    }

    public List<Voucher> makeGeneralVouchers(BusinessFundsCompensateF businessFundsCompensateF,
                                             List<TransactionOrder> transactionOrders,
                                             List<BusinessBillDetail> businessBillDetails) {
        Map<String, BusinessBillDetail> detailMap = businessBillDetails.stream().collect(Collectors
                .toMap(BusinessBillDetail::getAccountDetailId, detail -> detail));
        Map<String, TransactionOrder> orderMap = transactionOrders.stream()
                .collect(Collectors.toMap(TransactionOrder::getBizTransactionNo, order -> order));
        BusinessProcessHandleF businessProcessHandleF = businessFundsCompensateF.getBusinessProcessHandleF();
        List<ProcessAccountBookF> accountBooks = businessProcessHandleF.getAccountBooks();
        Map<String, AccountBookE> accountBookMap = ListUtils.toGroupSingleMap(businessFundsCompensateF.getAccountBooks(), AccountBookE::getCode);
        List<BusinessUnitE> businessUnits = businessFundsCompensateF.getBusinessUnits();
        Map<String, BusinessUnitE> businessUnitMap = ListUtils.toGroupSingleMap(businessUnits, BusinessUnitE::getCode);
        List<Voucher> vouchers = new ArrayList<>();
        List<VoucherTemplate> templates = voucherTemplateDomainService.getBPMTemplateListByTypeCode(businessProcessHandleF.getBillTypeCode());
        Map<Integer, VoucherTemplate> templateMap = templates.stream().collect(Collectors.toMap(VoucherTemplate::getTemplateNum, template -> template));
        for (int i = 0; i < CollectionUtils.size(accountBooks); i++) {
            ProcessAccountBookF accountBook = accountBooks.get(i);
            VoucherTemplate template = templateMap.get(accountBook.getTemplateNum());
            BusinessUnitE businessUnitE = businessUnitMap.get(getBusinessUintCode(accountBook.getAccountBookCode()));
            Voucher voucher = makeBPMVoucher(businessProcessHandleF, template, accountBookMap.get(accountBook.getAccountBookCode()), accountBook, businessUnitE);
            // 校验每个分录行金额是否一致
            BPMVoucherUtils.checkAmount(voucher);
            vouchers.add(voucher);
            detailMap.get(accountBook.getAccountDetailId()).setVoucherId(voucher.getId());
            // 发票处理
            List<InvoiceA> invoiceAS = collectReimbursementInvoice(accountBook.getInvoices(), businessUnitE, SysSourceEnum.BPM系统.getCode(), null);
            // 回填凭证id、发票id至交易订单表
            List<String> payIds = accountBook.getPayIds();
            if (CollectionUtils.isNotEmpty(payIds)) {
                payIds.forEach(payId -> {
                    if (MapUtils.isNotEmpty(orderMap)) {
                        TransactionOrder order = orderMap.get(payId);
                        order.getVoucherIds().add(voucher.getId());
                        if (CollectionUtils.isNotEmpty(invoiceAS)) {
                            order.setInvoiceIds(invoiceAS.stream().map(invoiceA -> invoiceA.getInvoiceReceipt().getId()).collect(Collectors.toList()));
                        }
                    }
                });
            }
        }
        return vouchers;
    }


    public Voucher makeBPMVoucher(BusinessProcessHandleF businessProcessHandleF, VoucherTemplate template,
                                  AccountBookE book, ProcessAccountBookF accountBook, BusinessUnitE businessUnitE) {
        VoucherBook voucherBook = new VoucherBook();
        voucherBook.setBookCode(book.getCode());
        voucherBook.setBookName(book.getName());
        voucherBook.setBookId(book.getId());
        Voucher voucher = Voucher.create(VoucherEventTypeEnum.付款结算, voucherBook, template);
        voucher.setVoucherSource(VoucherSourceEnum.BPM推送.getCode());
        voucher.setSysSource(SysSourceEnum.BPM系统.getCode());
        voucher.setMakerName(businessProcessHandleF.getMakerName());
        voucher.setMakerId(businessProcessHandleF.getMakerCode());
        if (Objects.nonNull(businessUnitE)) {
            voucher.setStatutoryBodyId(businessUnitE.getId());
            voucher.setStatutoryBodyCode(businessUnitE.getCode());
            voucher.setStatutoryBodyName(businessUnitE.getName());
        }
        List<VoucherCostCenterOBV> costCenters = new ArrayList<>();
        voucher.setCostCenters(costCenters);
        List<SubjectE> subjects = new ArrayList<>();
        List<VoucherDetailOBV> details = new ArrayList<>();
        List<ProcessChargeDetailF> chargeDetails = accountBook.getChargeDetails();
        for (VoucherTemplateEntryOBV entry : template.getEntries()) {
            for (int i = 0; i < CollectionUtils.size(chargeDetails); i++) {
                VoucherDetailOBV voucherDetailOBV = createVoucherDetailOBV(entry, i, accountBook, businessProcessHandleF, details, subjects);
                if(voucherDetailOBV.doesTheAmountExist(voucherDetailOBV.getOriginalAmount())){
                    details.add(voucherDetailOBV);
                }
            }
        }
        Map<String, SubjectE> subjectMap = ListUtils.toGroupSingleMap(subjects, SubjectE::getSubjectCode);
        voucherDomainService.putVoucherCashFlow(details, subjectMap);
        DefaultVoucherStrategy defaultVoucherStrategy = new DefaultVoucherStrategy();
        voucher.setDetails(defaultVoucherStrategy.mergeDetails(details, template));
        voucher.setAmount(details.stream().mapToLong(VoucherDetailOBV::getCreditAmount).sum());
        voucher.init();
        return voucher;
    }

    // public static void main(String[] args) {
    //     List<VoucherTemplateFilterItem> filterItemList = JSONObject.parseObject("[{\"filterType\":\"org\",\"logicType\":\"contain\",\"deptCode\":[\"demoData\"],\"deptName\":[\"demoData\"],\"taxRateCode\":[\"demoData\"],\"taxRate\":[1]}]",
    //             new TypeReference<List<VoucherTemplateFilterItem>>() {
    //             });
    //
    //     ProcessChargeDetailF f = new ProcessChargeDetailF();f.setOrgCode("demoData");f.setTaxRateCode("dd");
    //     VoucherTemplateEntryOBV obv = new VoucherTemplateEntryOBV();
    //     FilterConditions conditions = new FilterConditions();
    //     conditions.setFilterItems(filterItemList);
    //     obv.setFilterConditions(conditions);
    //
    //     boolean main = isRightOrgOrTaxRate(f, obv);
    //     System.out.println("此次结果"+main);
    // }
    /** 同时满足 或者 模板未配置过滤项
     * @param chargeDetail 入参
     * @param template 模板
     * @return
     */
    private static boolean isRightOrgOrTaxRate(ProcessChargeDetailF chargeDetail, VoucherTemplateEntryOBV template){
        FilterConditions conditions = template.getFilterConditions();
        if (ObjectUtil.isNull(conditions)){
            return true;
        }
        List<VoucherTemplateFilterItem> list = conditions.getFilterItems();
        if (CollUtil.isEmpty(list)){
            log.info(FinanceConstants.BPM_VOUCHER_LOG+"未设置过滤项目跳过");
            return true;
        }
        String orgCode = chargeDetail.getOrgCode();
        BigDecimal taxRate = chargeDetail.getTaxRate();

        boolean checkOrg = checkOrg(orgCode, list);
        boolean rateResult = checkTaxRate(taxRate, list);

        log.info(FinanceConstants.BPM_VOUCHER_LOG+"部门过滤项目：orgCode-{},结果:{},税率入参:taxRate-{},结果：{}。入参：{}，模板过滤项:{}",
                orgCode,checkOrg,taxRate,rateResult,JSONObject.toJSONString(chargeDetail),JSONObject.toJSONString(list));
        return checkOrg && rateResult;
    }

    private static boolean checkOrg(String orgCode ,List<VoucherTemplateFilterItem> list){
        VoucherTemplateFilterItem right = null;
        for (VoucherTemplateFilterItem item : list) {
            if (FilterTypeEnum.org.equals(item.getFilterType())){
                right = item;
            }
        }
        if (ObjectUtil.isNull(right)){
            log.info(FinanceConstants.BPM_VOUCHER_LOG+"未设置部门过滤项");
            return true;
        }

        if (LogicTypeEnum.contain.equals(right.getLogicType())){
            return right.getDeptCode().contains(orgCode);
        }else if (LogicTypeEnum.noContain.equals(right.getLogicType())){
            return !right.getDeptCode().contains(orgCode);
        }else {
            return right.getDeptCode().get(0).equals(orgCode);
        }
    }
    /**
     * @param taxRate
     * @param list 模板里的过滤条件
     * @return
     */
    private static boolean checkTaxRate(BigDecimal taxRate ,List<VoucherTemplateFilterItem> list){
        VoucherTemplateFilterItem right = null;
        for (VoucherTemplateFilterItem item : list) {
            if (FilterTypeEnum.taxRate.equals(item.getFilterType())){
                right = item;
            }
        }

        if (ObjectUtil.isNull(right)){
            log.info(FinanceConstants.BPM_VOUCHER_LOG+"未设置税率过滤项");
            return true;
        }
        List<BigDecimal> temRateList = new ArrayList<>(right.getTaxRate().size());
        right.getTaxRate().forEach(v->{
            temRateList.add(v.setScale(2, RoundingMode.UP));
        });
        if (LogicTypeEnum.contain.equals(right.getLogicType())){
            if (Objects.isNull(taxRate)) {
                return false;
            }
            return temRateList.stream().anyMatch(temRate -> temRate.compareTo(taxRate) == 0);
        }else if (LogicTypeEnum.noContain.equals(right.getLogicType())){
            if (Objects.isNull(taxRate)) {
                return true;
            }
            return temRateList.stream().noneMatch(temRate -> temRate.compareTo(taxRate) == 0);
        }else {
            if (Objects.isNull(taxRate)) {
                return false;
            }
            return temRateList.get(0).compareTo(taxRate) == 0;
        }
    }


    /**
     * @param entry 模板
     * @param index index
     * @param accountBook 入参账套信息
     * @param businessProcessHandleF 统一入参
     * @param details
     * @param subjects
     * @return
     */
    public VoucherDetailOBV createVoucherDetailOBV(VoucherTemplateEntryOBV entry, int index, ProcessAccountBookF accountBook,
                                                   BusinessProcessHandleF businessProcessHandleF, List<VoucherDetailOBV> details,
                                                   List<SubjectE> subjects) {
        VoucherDetailOBV voucherDetail = new VoucherDetailOBV();
        voucherDetail.setType(entry.getType());
        ProcessChargeDetailF chargeDetail = accountBook.getChargeDetails().get(index);

        // 校验部门+税率 是否和模板中一致，若不一致，则不做此分录行
        if(ObjectUtil.isNotNull(entry.getFilterConditions())){
            boolean rate = isRightOrgOrTaxRate(chargeDetail, entry);
            log.info(FinanceConstants.BPM_VOUCHER_LOG+"是否跳过此分录行：{}",!rate);
            if (!rate){
                voucherDetail.setOriginalAmount(0L);
                return voucherDetail;
            }
        }
        // 科目添加
        SubjectE subject;
        subject = voucherFacade.getSubject(entry.getSubjectId());
        ChargeItemE chargeItem = getChargeItemId(entry, chargeDetail);
        if (Objects.isNull(subject) || SubjectLeafStatusEnum.叶子节点.getCode() != subject.getLeaf()) {
            // 如果凭证模板里面配置的是二级科目
            Long subjectId = entry.getSubjectId();
            if (subject.getLevel() > 1) {
                String path = subject.getPath();
                List<Long> paths = JSON.parseArray(path, Long.class);
                subjectId = paths.get(0);
            }
            subject = voucherFacade.getSubjectByChargeItem(subjectId, chargeItem.getId());
            if (Objects.isNull(subject)) {
                subject = voucherFacade.getSubjectByCode(entry.getSubjectCode());
                if (Objects.isNull(subject)){
                    log.error("未找到指定科目【" + entry.getSubjectName() + "（" + entry.getSubjectCode()+ "）】");
                    throw BizException.throw400("未找到指定科目【" + entry.getSubjectName() + "（" + entry.getSubjectCode()+ "）】");
                } else if (subject.getLeaf() != 1) {
                    log.error("未找到费项【{}】所关联的科目信息,科目id:{}", chargeItem.getName(), subjectId);
//                    throw BizException.throw400("未找到费项【" + chargeItem.getName() + "】所关联的科目信息");
                    voucherDetail.setOriginalAmount(0L);
                    return voucherDetail;
                }
            }
        }
        subjects.add(subject);
        voucherDetail.setSubjectId(subject.getId());
        voucherDetail.setSubjectCode(subject.getSubjectCode());
        voucherDetail.setSubjectName(subject.getFullName());
        voucherDetail.setCashFlowId(entry.getCashFlowId());
        // 金额添加
        Map<String, BigDecimal> logicAmountMap = VoucherEntryLogicUtils.parseAmountMap(accountBook, index,businessProcessHandleF.getBillTypeCode());
        voucherDetail.putAmount(entry.evalLogic(logicAmountMap, details));
        //  todo 含税金额添加，暂时直接取全部金额，后面要修改
        voucherDetail.setIncTaxAmount(voucherDetail.getOriginalAmount());
        // 摘要添加
        voucherDetail.setSummary(entry.generateSummary(businessProcessHandleF, accountBook, index));
        if (Objects.nonNull(chargeItem)) {
            voucherDetail.setChargeItemId(chargeItem.getId());
            voucherDetail.setChargeItemCode(chargeItem.getCode());
            voucherDetail.setChargeItemName(chargeItem.getName());
        }
        // 凭证明细中，设置场景业务数据ID
        //获取辅助核算信息
        List<VoucherTemplateEntryAssiste> entryAssisteItems = entry.getAssisteItems();
        List<AssisteItemOBV> assisteItems = new ArrayList<>();
        String auxiliaryCount = subject.getAuxiliaryCount();
        List<String> assisteItemCodes = new ArrayList<>();
        if (StringUtils.isNotBlank(auxiliaryCount) && !"null".equals(auxiliaryCount)) {
            assisteItemCodes = JSONArray.parseArray(auxiliaryCount, String.class);
        }
        if (CollectionUtils.isNotEmpty(assisteItemCodes)) {
            for (String assisteItemCode : assisteItemCodes) {
                entryAssisteItems.stream()
                        .filter(item -> StringUtils.equals(item.getAscCode(), assisteItemCode))
                        .findFirst()
                        .ifPresentOrElse(entryAssisteItem -> {
                            assisteItems.add(BpmVoucherAssisteItemContext
                                    .getStrategy(entryAssisteItem.getCode())
                                    .getByBus(entryAssisteItem, businessProcessHandleF, accountBook,
                                            index));
                        }, new AppRunnable() {
                            @Override
                            public void execute() {
                                AssisteItemOBV assisteItemOBV = new AssisteItemOBV();
                                assisteItemOBV.setAscCode(assisteItemCode);
                                assisteItemOBV.setAscName(AssisteItemTypeEnum.valueOfByAscCode(assisteItemCode).getValue());
                                assisteItemOBV.setType(AssisteItemTypeEnum.valueOfByAscCode(assisteItemCode).getCode());
                                assisteItems.add(assisteItemOBV);
                            }
                        });
            }
        }
        // 辅助核算添加
        voucherDetail.setAssisteItems(assisteItems);
        return voucherDetail;
    }

    public ChargeItemE getChargeItemId(VoucherTemplateEntryOBV entry, ProcessChargeDetailF chargeDetail) {
        List<VoucherTemplateEntryChargeItem> chargeItem = entry.getChargeItem();
        if (CollectionUtils.isNotEmpty(chargeItem)) {
            return chargeItemDomainService.getById(chargeItem.get(0).getId());
        }
        return chargeItemDomainService.getByCode(chargeDetail.getSecondChargeItemCode());
    }

    /**
     * 获取bpm跳转链接
     * @param voucherId
     * @return
     */
    public String getBPMLink(String voucherId) {
        log.info("用户id获取 {}",ApiData.API.getUserId().get());
        String businessId = externalClient.queryByBusinessId(ApiData.API.getUserId().get());
        if (bpmEnv.equals("test")) {
            businessId = "13900000001";
        }
        if(StringUtils.isBlank(businessId)){
            throw BizException.throw400("查询主数据编码不存在");
        }
        String sid = concludeUidBudget(businessId);
        if (StringUtils.isBlank(sid)) {
            throw BizException.throw400("未获取到BPM的sid");
        }
        BusinessBill businessBill = businessBillDomainService.getByVoucherId(voucherId);
        String processInstId = "";
        if (Objects.isNull(businessBill)) {
            TransactionOrder transactionOrder = transactionOrderRepository.getByVoucherId(voucherId);
            if (Objects.isNull(transactionOrder)) {
                throw BizException.throw400("未查询到对应BPM单据");
            }
            processInstId = transactionOrder.getBizTransactionNo();
        } else {
            processInstId = businessBill.getBusinessId();
        }
        return BPM_URL + "/r/w?sid=" + sid + "&cmd=CLIENT_BPM_FORM_MAIN_PAGE_OPEN&processInstId=" + processInstId
                + "&openState=2";
    }

    /**
     * 获取BPM sid
     * @return
     */
    public String concludeUidBudget(String businessId) {
        JSONObject result = new JSONObject();
        HttpURLConnection con = null;
        BufferedReader buffer = null;
        StringBuffer resultBuffer = null;
        try {
//            URL url = new URL("http://bpmcs6.oceanhomeplus.com:8087/rs/SidApi/getSid/13900000001/null");
            log.info("BPM获取url:{}",BPM_URL + "/rs/SidApi/getSid/"+businessId+"/null");
            URL url = new URL(BPM_URL + "/rs/SidApi/getSid/"+businessId+"/null");
            //得到连接对象
            con = (HttpURLConnection) url.openConnection();
            //设置请求类型
            con.setRequestMethod("GET");
            //主要就是这里，设置这样的请求头，Basic要和加密后的账号密码在一起，并且中间有空格
            con.setRequestProperty("Authorization","Basic "+ Base64.getUrlEncoder().encodeToString(("HTXT" + ":" + "HTXT").getBytes()));
            //设置请求需要返回的数据类型和字符集类型
            con.setRequestProperty("Content-Type", "application/json;charset=GBK");
            //允许写出
            con.setDoOutput(true);
            //允许读入
            con.setDoInput(true);
            //不使用缓存
            con.setUseCaches(false);
            //得到响应码
            int responseCode = con.getResponseCode();

            if(responseCode == HttpURLConnection.HTTP_OK){
                //得到响应流
                InputStream inputStream = con.getInputStream();
                //将响应流转换成字符串
                resultBuffer = new StringBuffer();
                String line;
                buffer = new BufferedReader(new InputStreamReader(inputStream, "GBK"));
                while ((line = buffer.readLine()) != null) {
                    resultBuffer.append(line);
                }
                String resultStr = resultBuffer.toString();
                log.info("BPM获取sid结果:{}", resultStr);
                result = JSONObject.parseObject(resultStr);
            }else {
                log.error("BPM获取sid失败,状态码" + responseCode);
            }
        } catch (Exception e) {
            log.error("BPM获取sid失败:", e);
        }
        String uidBudget = null;
        if(ObjectUtils.isNotEmpty(result) && result.containsKey("data")){
            uidBudget = result.getString("data");
        }
        return uidBudget;
    }
}
