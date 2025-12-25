package com.wishare.contract.apps.service.contractset;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wishare.bizlog.BizLog;
import com.wishare.bizlog.content.Content;
import com.wishare.bizlog.content.PlainTextDataItem;
import com.wishare.bizlog.content.UrlLinkDataItem;
import com.wishare.bizlog.entity.BizObject;
import com.wishare.bizlog.operator.Operator;
import com.wishare.contract.apps.fo.contractset.*;
import com.wishare.contract.apps.remote.clients.FinanceFeignClient;
import com.wishare.contract.apps.remote.clients.OrgFeignClient;
import com.wishare.contract.apps.remote.component.OrgEnhanceComponent;
import com.wishare.contract.apps.remote.finance.facade.FinanceFacade;
import com.wishare.contract.apps.remote.fo.*;
import com.wishare.contract.apps.remote.vo.*;
import com.wishare.contract.domains.consts.ContractSetConst;
import com.wishare.contract.domains.consts.contractset.ContractConcludeFieldConst;
import com.wishare.contract.domains.entity.contractset.*;
import com.wishare.contract.domains.enums.BusinessTypeEnum;
import com.wishare.contract.domains.enums.LogActionEnum;
import com.wishare.contract.domains.service.contractset.ContractBondPlanService;
import com.wishare.contract.domains.service.contractset.ContractConcludeService;
import com.wishare.contract.domains.vo.contractset.*;
import com.wishare.contract.infrastructure.utils.FileStorageUtils;
import com.wishare.contract.infrastructure.utils.MsgFacade;
import com.wishare.owl.enhance.IOwlApiBase;
import com.wishare.starter.Global;
import com.wishare.starter.beans.IdentityInfo;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.exception.BizException;
import com.wishare.starter.interfaces.ApiBase;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.tools.starter.fo.search.SearchF;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

import javax.json.Json;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 合同保证金计划信息
 * </p>
 *
 * @author wangrui
 * @since 2022-09-09
 */
@Service
@Slf4j
public class ContractBondPlanAppService implements IOwlApiBase {

    @Setter(onMethod_ = {@Autowired})
    private ContractBondPlanService contractBondPlanService;
    @Setter(onMethod_ = {@Autowired})
    private ContractConcludeService contractConcludeService;
    @Setter(onMethod_ = {@Autowired})
    private OrgFeignClient orgFeignClient;
    @Setter(onMethod_ = {@Autowired})
    private FinanceFeignClient ampFinanceFeignClient;
    @Setter(onMethod_ = {@Autowired})
    private ContractBondCollectionDetailAppService contractBondCollectionDetailAppService;
    @Setter(onMethod_ = {@Autowired})
    private ContractBondPaymentDetailAppService contractBondPaymentDetailAppService;
    @Setter(onMethod_ = {@Autowired})
    private FileStorageUtils fileStorageUtils;
    @Setter(onMethod_ = {@Autowired})
    private ContractBondCarryoverDetailAppService contractBondCarryoverDetailAppService;
    @Setter(onMethod_ = {@Autowired})
    private MsgFacade msgFacade;
    @Setter(onMethod_ = {@Autowired})
    private FinanceFeignClient feignClient;
    @Setter(onMethod_ = {@Autowired})
    private ContractBondReceiptDetailAppService contractBondReceiptDetailAppService;

    @Setter(onMethod_ = {@Autowired})
    private OrgEnhanceComponent orgEnhanceComponent;

    @Setter(onMethod_ = {@Autowired})
    private FinanceFacade financeFacade;

    public List<ContractBondPlanV> listContractBondPlan(ContractBondPlanF contractBondPlanF) {
        List<ContractBondPlanV> bondPlanVList = contractBondPlanService.listContractBondPlan(contractBondPlanF);
        if (CollectionUtils.isEmpty(bondPlanVList)) {
            return null;
        }
        for (ContractBondPlanV contractBondPlanV : bondPlanVList) {
            //费项
            if (null != ampFinanceFeignClient.chargeName(contractBondPlanV.getChargeItemId())) {
                contractBondPlanV.setChargeItemName(ampFinanceFeignClient.chargeName(contractBondPlanV.getChargeItemId()));
            }
            //成本中心
            if (null != orgFeignClient.getByFinanceId(contractBondPlanV.getCostId())) {
                contractBondPlanV.setCostName(orgFeignClient.getByFinanceId(contractBondPlanV.getCostId()).getNameCn());
            }
            //责任部门
            if (null != orgFeignClient.getByOrgId(contractBondPlanV.getOrgId())) {
                contractBondPlanV.setOrgName(orgFeignClient.getByOrgId(contractBondPlanV.getOrgId()).getOrgName());
            }
        }
        return bondPlanVList;
    }

    public List<Long> contractBondPlanBillIds(Long contractId,
                                              Integer bondType,
                                              Integer paymentStatus,
                                              Integer refundStatus,
                                              Integer bidBond) {

        return contractBondPlanService.contractBondPlanBillIds(contractId, bondType, paymentStatus, refundStatus, bidBond);
    }


//    @GlobalTransactional
    @Transactional(rollbackFor = {Exception.class})
    public void saveContractBondPlan(List<ContractBondPlanSaveF> contractBondPlanF) {
        if (contractBondPlanF.isEmpty()) {
            return;
        }
        // 先获取此合同下所有保证金计划，用于删除临时账单
        Long contractId = contractBondPlanF.get(0).getContractId();
        ContractBondPlanF contractBondPlanF1 = new ContractBondPlanF();
        contractBondPlanF1.setContractId(contractId);
        List<ContractBondPlanV> contractBondPlanDeleteS = contractBondPlanService.listContractBondPlan(contractBondPlanF1);
        // 删除临时账单
        List<Long> temporaryBillIds = contractBondPlanDeleteS.stream().
                map(ContractBondPlanV::getBillId).filter(item -> item != null).collect(Collectors.toList());
        if (!temporaryBillIds.isEmpty()) {
            deleteBatch(temporaryBillIds);
        }
//        // 解除账单关联（招投标保证金
//        List<Long> bidBondBillIds = contractBondPlanDeleteS.stream().filter(ContractBondPlanV::getBidBond).
//                map(ContractBondPlanV::getBillId).filter(Objects::nonNull).collect(Collectors.toList());
//        if (!bidBondBillIds.isEmpty()) {
//            reference(bidBondBillIds.get(0), 0);
//        }
//        for (ContractBondPlanSaveF bondPlanSaveF : contractBondPlanF) {
//            ContractDetailsV contractDetailsV = contractConcludeService.getContractConclude(bondPlanSaveF.getContractId());
//            if (ContractSetConst.SUPPLEMENT.equals(contractDetailsV.getSigningMethod()) ||
//                    ContractSetConst.STOP.equals(contractDetailsV.getSigningMethod())) {
//                //补充或终止合同
//                contractBondPlanService.deleteBondPlan(contractDetailsV.getPid());
//            }
//        }
        contractBondPlanService.deleteBondPlan(contractId);
        for (ContractBondPlanSaveF contractBondPlanSaveF : contractBondPlanF) {
            contractBondPlanService.saveContractBondPlan(contractBondPlanSaveF);
        }
    }

    public void save(ContractBondPlanSaveF contractBondPlanF) {
        contractBondPlanService.saveContractBondPlan(contractBondPlanF);
    }

    /**
     * 删除临时账单
     */
    public BillDeleteBatchResultDto deleteBatch(List<Long> billIds) {
        DeleteBatchBillRf deleteBatchBillRf = new DeleteBatchBillRf();
        deleteBatchBillRf.setBillIds(billIds);
        //-- TODO supUnitId default
        deleteBatchBillRf.setSupCpUnitId("default");
        return ampFinanceFeignClient.temporaryDeleteBatch(deleteBatchBillRf);
    }

    public void updateBillId(Long id, Long billId) {
        contractBondPlanService.updateBillId(id, billId);
    }

    public void removeContractBondPlan(Long id) {
        contractBondPlanService.removeContractBondPlan(id);
    }

    public void deleteBondPlan(Long contractId) {
        contractBondPlanService.deleteBondPlan(contractId);
    }

    public PageV<ContractBondPlanPageV> pageContractBondPlan(PageF<SearchF<?>> form) {
        //组织权限隔离,超管都能看
        if(!isAdminCurUser()) {
            Set<String> orgIdList = orgEnhanceComponent.getChildrenOrgListByOrgId(Global.mapperFacade.mapAsList(orgIds(), String.class));
            List<Field> fields = form.getConditions().getFields();
            Field f = new Field();
            f.setName("cc." + ContractConcludeFieldConst.BELONG_ORG_ID);
            f.setValue(new ArrayList<>(orgIdList));
            f.setMethod(15);
            fields.add(f);
        }

        IPage<ContractBondPlanPageV> pageList = contractBondPlanService.pageContractBondPlan(form, curIdentityInfo().getTenantId());
        pageList.getRecords().forEach(item -> {
            //费项
            if (null != ampFinanceFeignClient.chargeName(item.getChargeItemId())) {
                item.setChargeItemName(ampFinanceFeignClient.chargeName(item.getChargeItemId()));
            }
            //成本中心
            if (null != orgFeignClient.getByFinanceId(item.getCostId())) {
                item.setCostName(orgFeignClient.getByFinanceId(item.getCostId()).getNameCn());
            }
        });
        return PageV.of(form, pageList.getTotal(), pageList.getRecords());
    }


    public ContractBondPlanSumV pageContractBondPlanSum(PageF<SearchF<?>> form) {
        return contractBondPlanService.pageContractBondPlanSum(form, curIdentityInfo().getTenantId());
    }

    @Transactional(rollbackFor = {Exception.class})
    public void batchCollection(ContractBondPlanCollectionF form) {
        IdentityInfo identityInfo = curIdentityInfo();
        LocalDateTime now = LocalDateTime.now();
        Integer bondType = form.getBondType();
        String receiptVoucher = "";
        String receiptVoucherName = "";
        ContractDetailsV contractConclude = contractConcludeService.getContractConclude(form.getContractId());
        if (Objects.isNull(contractConclude)) {
            return;
        }
        // 收款凭证文件集处理
        if (form.getReceiptVoucherFileVos() != null && !form.getReceiptVoucherFileVos().isEmpty()) {
            receiptVoucher = fileStorageUtils.batchSubmitFile(
                    form.getReceiptVoucherFileVos(), null, form.getContractId(), identityInfo.getTenantId());
            receiptVoucherName = fileStorageUtils.batchSubmitName(form.getReceiptVoucherFileVos());
        }
        String finalReceiptVoucher = receiptVoucher;
        String finalReceiptVoucherName = receiptVoucherName;
        List<ContractBondCollectionDetailE> bondCollectionDetailES = new ArrayList<>();
        for (BondPlanAmountF item : form.getBondPlanAmountFList()) {
            ContractBondPlanE contractBondPlanE = contractBondPlanService.getById(item.getBondPlanId());
            if (bondType == 0) {
                // 收取类  先收再退  在此处为收
                // 中台账单结算 因为没有临时账单了，所以不需要对临时账单进行结算
                //temporarySettleBatch(item, contractConclude, form.getCollectionMethod(), form.getRemark());
                BigDecimal paymentAmount = contractBondPlanE.getPaymentAmount().add(item.getCollectionAmount());
                contractBondPlanE.setPaymentAmount(paymentAmount);
                if (paymentAmount.compareTo(contractBondPlanE.getLocalCurrencyAmount()) < 0) {
                    contractBondPlanE.setPaymentStatus(ContractSetConst.PART_PAYMENT);
                } else {
                    contractBondPlanE.setPaymentStatus(ContractSetConst.ALL_PAYMENT);
                }
                // 若已收大于已退  退款款状态未部分退
                if (paymentAmount.compareTo(contractBondPlanE.getRefundAmount()) > 0 &&
                        contractBondPlanE.getRefundAmount().compareTo(new BigDecimal("0.00")) > 0) {
                    contractBondPlanE.setRefundStatus(ContractSetConst.PART_REFUND);
                }
            } else {
                // 缴纳类  先付再收  在此处为收
                // 中台账单退款
                //temporaryRefund(item, contractConclude, form.getCollectionMethod(), form.getRemark());
                BigDecimal refundAmount = contractBondPlanE.getRefundAmount().add(item.getCollectionAmount());
                contractBondPlanE.setRefundAmount(refundAmount);
                if (refundAmount.compareTo(contractBondPlanE.getPaymentAmount()) < 0) {
                    contractBondPlanE.setRefundStatus(ContractSetConst.PART_REFUND);
                } else {
                    contractBondPlanE.setRefundStatus(ContractSetConst.ALL_REFUND);
                }
            }
            // 追加收款明细
            ContractBondCollectionDetailF collectionDetailF = Global.mapperFacade.map(form, ContractBondCollectionDetailF.class);
            collectionDetailF.setBondPlanId(item.getBondPlanId());
            collectionDetailF.setCollectionAmount(item.getCollectionAmount());
            collectionDetailF.setReceiptVoucher(finalReceiptVoucher);
            collectionDetailF.setReceiptVoucherName(finalReceiptVoucherName);
            ContractBondCollectionDetailE collectionDetailE = contractBondCollectionDetailAppService.saveBondCollectionDetail(collectionDetailF);

            bondCollectionDetailES.add(collectionDetailE);
            //  更新保证金计划
            contractBondPlanE.setOperator(identityInfo.getUserId());
            contractBondPlanE.setOperatorName(identityInfo.getUserName());
            contractBondPlanE.setGmtModify(now);
            contractBondPlanService.updateById(contractBondPlanE);

            //调用中台生成收款单及明细
            financeFacade.gatherAddBatch(item, contractConclude, form);
        }
        List<String> numbers = bondCollectionDetailES.stream().map(ContractBondCollectionDetailE::getCollectionCode).collect(Collectors.toList());
        collectionLog(form.getBondPlanAmountFList(),form.getContractId(),numbers,form.getBondType(),1);
    }

    /**
     * 收款动态日志
     */
    public void collectionLog(List<BondPlanAmountF> list,Long contractId,List<String> numbers,Integer type,Integer plan) {
        BigDecimal amount = new BigDecimal("0.00");
        String amountName = null;
        String codeName = null;
        String objName = "";
        for (BondPlanAmountF bondPlanAmountF : list) {
            amount = amount.add(bondPlanAmountF.getCollectionAmount());
        }
        //收款
        if (plan == 1) {
            amountName = "保证金收款金额：";
            codeName = "保证金收款明细：";
            objName = BusinessTypeEnum.保证金收款.getName();
        }
        //付款/退款
        if(plan == 2) {
            if(type == 1){
                amountName = "申请付款金额：";
                codeName = "申请付款明细：";
                objName = BusinessTypeEnum.保证金付款.getName();
            }else if(type == 2){
                amountName = "申请退款金额：";
                codeName = "申请退款明细：";
                objName = BusinessTypeEnum.保证金退款.getName();
            }else{
                amountName = "申请扣款金额：";
                codeName = "申请扣款明细：";
                objName = BusinessTypeEnum.保证金扣款.getName();
            }
        }
        String finalObjName = objName;
        normalLog(contractId.toString(),finalObjName,amountName,amount.toString(),codeName, StringUtils.join(numbers, ","));
    }

    /**
     *收付款日志方法
     */
    public void normalLog(String contractId,String finalObjName,String amountName,String amount,String codeName,String code) {
        BizLog.normal(contractId, new Operator(userId(), userName()),
                new BizObject() {
                    @Override
                    public String getObjName() {
                        return finalObjName;
                    }
                },
                LogActionEnum.发起,
                new Content().option(new PlainTextDataItem(amountName + amount, true))
                        .option(new UrlLinkDataItem(codeName+code, false, ""))
        );
    }

//    @GlobalTransactional
    @Transactional(rollbackFor = {Exception.class})
    public void batchPayment(ContractBondPlanPaymentF form) {
        IdentityInfo identityInfo = curIdentityInfo();
        LocalDateTime now = LocalDateTime.now();
        Integer bondType = form.getBondType();
        ContractDetailsV contractConclude = contractConcludeService.getContractConclude(form.getContractId());
        if (Objects.isNull(contractConclude)) {
            return;
        }
        List<ContractBondPaymentDetailE> bondPaymentDetailES = new ArrayList<>();
        form.getBondPlanAmountFList().forEach(item -> {
            ContractBondPlanE contractBondPlanE = contractBondPlanService.getById(item.getBondPlanId());
            if (bondType == 0) {
                // 收取类  先收再退  在此处为退
                // 中台账单退款
                //temporaryRefund(item, contractConclude, form.getPaymentMethod(), form.getRemark());

                BigDecimal refundAmount = contractBondPlanE.getRefundAmount().add(item.getCollectionAmount());
                BigDecimal deductionAmount = contractBondPlanE.getDeductionAmount();
                //退款+扣款大于已收款金额paymentAmount,则不能扣款
                BigDecimal totalRefundAmount = deductionAmount.add(refundAmount);
                BigDecimal paymentAmount = contractBondPlanE.getPaymentAmount();
                if(paymentAmount.compareTo(totalRefundAmount) >= 0){
                    contractBondPlanE.setRefundAmount(refundAmount);
                    if (refundAmount.compareTo(contractBondPlanE.getPaymentAmount()) < 0) {
                        contractBondPlanE.setRefundStatus(ContractSetConst.PART_REFUND);
                    } else {
                        contractBondPlanE.setRefundStatus(ContractSetConst.ALL_REFUND);
                    }
                }else{
                    log.error("退款+扣款大于已收款金额，参数contractBondPlanE：{},CollectionAmount:{}",JSON.toJSONString(contractBondPlanE),item.getCollectionAmount());
                }
            } else {
                // 缴纳类  先付再收  在此处为付  20221103修改  先推送账单，再付款
                // 推送账单
                ContractBondPlanV contractBondPlanV = Global.mapperFacade.map(contractBondPlanE, ContractBondPlanV.class);
                //  判断是否推送过
                if (Objects.nonNull(contractBondPlanV) && Objects.nonNull(contractBondPlanV.getBillId()) && contractBondPlanV.getBillId() == 0) {
                    List<TemporaryChargeBillPageV> temporaryChargeBillPageVS = pushTemporaryBill(contractBondPlanV, contractConclude);
                    // 设置账单id
                    Long billId = temporaryChargeBillPageVS.get(0).getId();
                    item.setBillId(billId);
                    contractBondPlanE.setBillId(billId);
                }
                // 中台账单结算(由于不生成临时账单，故去除这步)
                //temporarySettleBatch(item, contractConclude, form.getPaymentMethod(), form.getRemark());
                BigDecimal paymentAmount = contractBondPlanE.getPaymentAmount().add(item.getCollectionAmount());
                contractBondPlanE.setPaymentAmount(paymentAmount);
                if (paymentAmount.compareTo(contractBondPlanE.getLocalCurrencyAmount()) < 0) {
                    contractBondPlanE.setPaymentStatus(ContractSetConst.PART_PAYMENT);
                } else {
                    contractBondPlanE.setPaymentStatus(ContractSetConst.ALL_PAYMENT);
                }
                // 若已付大于已收  收款状态未部分收
                if (paymentAmount.compareTo(contractBondPlanE.getRefundAmount()) > 0 &&
                        contractBondPlanE.getRefundAmount().compareTo(new BigDecimal("0.00")) > 0) {
                    contractBondPlanE.setRefundStatus(ContractSetConst.PART_REFUND);
                }
            }
            // 追加付/退款明细
            ContractBondPaymentDetailF contractBondPaymentDetailF = Global.mapperFacade.map(form, ContractBondPaymentDetailF.class);
            contractBondPaymentDetailF.setBondPlanId(item.getBondPlanId());
            contractBondPaymentDetailF.setPaymentAmount(item.getCollectionAmount());
            contractBondPaymentDetailF.setAuditStatus(0);
            ContractBondPaymentDetailE bondPaymentDetailE = contractBondPaymentDetailAppService.saveBondPaymentDetail(contractBondPaymentDetailF);
            bondPaymentDetailES.add(bondPaymentDetailE);
            //  更新保证金计划
            contractBondPlanE.setOperator(identityInfo.getUserId());
            contractBondPlanE.setOperatorName(identityInfo.getUserName());
            contractBondPlanE.setGmtModify(now);
            contractBondPlanService.updateById(contractBondPlanE);

            financeFacade.addContractBondPlanPayBill(item,contractConclude,form);
        });
        List<String> numbers = bondPaymentDetailES.stream().map(ContractBondPaymentDetailE::getPaymentNumber).collect(Collectors.toList());
        collectionLog(form.getBondPlanAmountFList(),form.getContractId(),numbers,form.getType(),2);
    }

    /**
     * 收取类保证金收款/缴纳类保证金付款(账单结算)
     */
//    @GlobalTransactional
    @Transactional(rollbackFor = {Exception.class})
    public void temporarySettleBatch(BondPlanAmountF bondPlanAmountF, ContractDetailsV contractConclude, Integer method, String remark) {
        Long merchantId = 0L;
        String merchantName = "";
        Long statutoryBodyId = 0L;
        String statutoryBodyName = "";
        LocalDateTime now = LocalDateTime.now();
        AddBillSettleRf addBillSettleRf = new AddBillSettleRf();
        addBillSettleRf.setSettleTime(now);
        addBillSettleRf.setBillId(bondPlanAmountF.getBillId());
        addBillSettleRf.setSettleAmount(bondPlanAmountF.getCollectionAmount().multiply(new BigDecimal("100")).longValue());
        addBillSettleRf.setPayAmount(bondPlanAmountF.getCollectionAmount().multiply(new BigDecimal("100")).longValue());
        if (contractConclude.getContractNature().equals(ContractSetConst.INCOME)) {
            // 收入类合同，甲方为客商，乙方法定单位
            merchantId = contractConclude.getPartyAId();
            merchantName = contractConclude.getPartyAName();
            statutoryBodyId = contractConclude.getPartyBId();
            statutoryBodyName = contractConclude.getPartyBName();
        }
        if (contractConclude.getContractNature().equals(ContractSetConst.PAY)) {
            //支出类合同，甲方为法定单位，乙方客商
            merchantId = contractConclude.getPartyBId();
            merchantName = contractConclude.getPartyBName();
            statutoryBodyId = contractConclude.getPartyAId();
            statutoryBodyName = contractConclude.getPartyAName();
        }
        // 收费对象类型（0:业主，1开发商，2租客，3客商，4法定单位）  收付款方id  名称
        if ((contractConclude.getContractNature().equals(ContractSetConst.INCOME) && contractConclude.getBondType() == 0) ||
                (contractConclude.getContractNature().equals(ContractSetConst.PAY) && contractConclude.getBondType() == 1)) {
            // 收入类合同， 收取类保证金
            // 支出类合同， 缴纳类保证金
            // 收方法定单位   付方客商
            addBillSettleRf.setPayerType(3);
            addBillSettleRf.setPayeeId(statutoryBodyId.toString());
            addBillSettleRf.setPayeeName(statutoryBodyName);
            addBillSettleRf.setPayerId(merchantId.toString());
            addBillSettleRf.setPayerName(merchantName);
        }
        if ((contractConclude.getContractNature().equals(ContractSetConst.INCOME) && contractConclude.getBondType() == 1) ||
                (contractConclude.getContractNature().equals(ContractSetConst.PAY) && contractConclude.getBondType() == 0)) {
            // 收入类合同， 缴纳类保证金
            // 支出类合同， 收取类保证金
            // 收方客商   付方法定单位
            addBillSettleRf.setPayerType(4);
            addBillSettleRf.setPayeeId(merchantId.toString());
            addBillSettleRf.setPayeeName(merchantName);
            addBillSettleRf.setPayerId(statutoryBodyId.toString());
            addBillSettleRf.setPayerName(statutoryBodyName);
        }
        addBillSettleRf.setSettleWay(1);
        if (contractConclude.getBondType() == 0) {
            if (method.equals(ContractSetConst.COLLECTION_METHOD_CASH)) {
                addBillSettleRf.setSettleChannel("CASH");
            } else if (method.equals(ContractSetConst.COLLECTION_METHOD_ALIPAY)){
                addBillSettleRf.setSettleChannel("ALIPAY");
            } else if (method.equals(ContractSetConst.COLLECTION_METHOD_WECHAT)) {
                addBillSettleRf.setSettleChannel("WECHATPAY");
            } else {
                addBillSettleRf.setSettleChannel("OTHER");
            }
        }
        if (contractConclude.getBondType() == 1) {
            if (method.equals(ContractSetConst.PAYMENT_METHOD_CASH)) {
                addBillSettleRf.setSettleChannel("CASH");
            } else if (method.equals(ContractSetConst.PAYMENT_METHOD_BANK)) {
                addBillSettleRf.setSettleChannel("BANK");
            } else {
                addBillSettleRf.setSettleChannel("OTHER");
            }
        }
        addBillSettleRf.setRemark(remark);
        log.info("临时账单收款：" + JSON.toJSONString(addBillSettleRf));
        ampFinanceFeignClient.temporarySettleBatch(Collections.singletonList(addBillSettleRf));
    }

    /**
     * 收取类保证金退款/缴纳类保证金收款(账单退款)
     */
    public void temporaryRefund(BondPlanAmountF bondPlanAmountF, ContractDetailsV contractConclude, Integer method, String remark) {
        BillRefundRf billRefundRf = new BillRefundRf();
        billRefundRf.setBillId(bondPlanAmountF.getBillId());
        billRefundRf.setRefundAmount(bondPlanAmountF.getCollectionAmount().multiply(new BigDecimal("100")).longValue());
        billRefundRf.setRefundWay(1);
        billRefundRf.setRefunderId(curIdentityInfo().getUserId());
        billRefundRf.setRefunderName(curIdentityInfo().getUserName());
        // 收费对象类型（0:业主，1开发商，2租客，3客商，4法定单位）  收付款方id  名称
        if ((contractConclude.getContractNature().equals(ContractSetConst.INCOME) && contractConclude.getBondType() == 0) ||
                (contractConclude.getContractNature().equals(ContractSetConst.PAY) && contractConclude.getBondType() == 1)) {
            // 收入类合同， 收取类保证金
            // 支出类合同， 缴纳类保证金
            // 收方法定单位   付方客商
            billRefundRf.setRefunderType(3);
        }
        if ((contractConclude.getContractNature().equals(ContractSetConst.INCOME) && contractConclude.getBondType() == 1) ||
                (contractConclude.getContractNature().equals(ContractSetConst.PAY) && contractConclude.getBondType() == 0)) {
            // 收入类合同， 缴纳类保证金
            // 支出类合同， 收取类保证金
            // 收方客商   付方法定单位
            billRefundRf.setRefunderType(4);
        }

        if (method.equals(ContractSetConst.PAYMENT_METHOD_CASH)) {
            billRefundRf.setRefundChannel("CASH");
        } else if (method.equals(ContractSetConst.PAYMENT_METHOD_BANK)) {
            billRefundRf.setRefundChannel("BANK");
        } else {
            billRefundRf.setRefundChannel("OTHER");
        }
        billRefundRf.setRemark(remark);
        log.info("临时账单退款：" + JSON.toJSONString(billRefundRf));
        ampFinanceFeignClient.temporaryRefund(billRefundRf);
    }

    public List<TemporaryChargeBillPageV> pushTemporaryBill(ContractBondPlanV addTemporaryChargeBillF, ContractDetailsV contractDetailsV) {
        List<TemporaryChargeBillPageV> temporaryChargeBillPageVS = temporaryAddBatch(Collections.singletonList(addTemporaryChargeBillF), contractDetailsV);
        // 审核账单
        ApproveBatchTemporaryChargeBillRf temporaryChargeBillRf = new ApproveBatchTemporaryChargeBillRf();
        temporaryChargeBillRf.setBillIds(temporaryChargeBillPageVS.stream().map(TemporaryChargeBillPageV::getId).collect(Collectors.toList()));
        temporaryChargeBillRf.setApproveState(2);
        //-- TODO supUnitId default
        temporaryChargeBillRf.setSupCpUnitId("default");
        Boolean aBoolean = ampFinanceFeignClient.temporaryApproveBatch(temporaryChargeBillRf);
        // 设置账单id
        temporaryChargeBillPageVS.forEach(item -> {
            contractBondPlanService.updateBillId(Long.valueOf(item.getExtField1()), item.getId());
        });
        return temporaryChargeBillPageVS;
    }

    /**
     * 批量新增临时收费账单
     */
    public List<TemporaryChargeBillPageV> temporaryAddBatch(List<ContractBondPlanV> addTemporaryChargeBillFs, ContractDetailsV contractDetailsV) {
        List<AddTemporaryChargeBillRf> addTemporaryChargeBillRfs = new ArrayList<>();
        String payType = "";
        int payerType = 0;
        Long statutoryBodyId = 0L;
        String statutoryBodyName = "";
        Long merchantId = 0L;
        String merchantName = "";
        // 收款方ID
        String payeeId = "";
        // 收款方名称
        String payeeName = "";
        // 付款方ID
        String payerId = "";
        // 付款方名称
        String payerName = "";
        // 收付类型
        if (contractDetailsV.getBondType() == 0) {
            payType = "0";
        } else {
            payType = "1";
        }

        // 法定单位id 名称  客商id  名称
        if (contractDetailsV.getContractNature().equals(ContractSetConst.INCOME)) {
            //收入类合同，甲方为客商，乙方法定单位
            statutoryBodyId = contractDetailsV.getPartyBId();
            merchantId = contractDetailsV.getPartyAId();
            if (null != orgFeignClient.getByFinanceId(contractDetailsV.getPartyBId())) {
                statutoryBodyName = orgFeignClient.getByFinanceId(contractDetailsV.getPartyBId()).getNameCn();
            }
            if (null != orgFeignClient.queryById(contractDetailsV.getPartyAId())) {
                merchantName = orgFeignClient.queryById(contractDetailsV.getPartyAId()).getName();
            }
        }
        if (contractDetailsV.getContractNature().equals(ContractSetConst.PAY)) {
            //支出类合同，甲方为法定单位，乙方客商
            statutoryBodyId = contractDetailsV.getPartyAId();
            merchantId = contractDetailsV.getPartyBId();
            if (null != orgFeignClient.getByFinanceId(contractDetailsV.getPartyAId())) {
                statutoryBodyName = orgFeignClient.getByFinanceId(contractDetailsV.getPartyAId()).getNameCn();
            }
            if (null != orgFeignClient.queryById(contractDetailsV.getPartyBId())) {
                merchantName = orgFeignClient.queryById(contractDetailsV.getPartyBId()).getName();
            }
        }

        // 收费对象类型（0:业主，1开发商，2租客，3客商，4法定单位）  收付款方id  名称
        if ((contractDetailsV.getContractNature().equals(ContractSetConst.INCOME) && contractDetailsV.getBondType() == 0) ||
                (contractDetailsV.getContractNature().equals(ContractSetConst.PAY) && contractDetailsV.getBondType() == 1)) {
            // 收入类合同， 收取类保证金
            // 支出类合同， 缴纳类保证金
            // 收方法定单位   付方客商
            payerType = 3;
            payeeId = statutoryBodyId.toString();
            payeeName = statutoryBodyName;
            payerId = merchantId.toString();
            payerName = merchantName;
        }
        if ((contractDetailsV.getContractNature().equals(ContractSetConst.INCOME) && contractDetailsV.getBondType() == 1) ||
                (contractDetailsV.getContractNature().equals(ContractSetConst.PAY) && contractDetailsV.getBondType() == 0)) {
            // 收入类合同， 缴纳类保证金
            // 支出类合同， 收取类保证金
            // 收方客商   付方法定单位
            payerType = 4;
            payeeId = merchantId.toString();
            payeeName = merchantName;
            payerId = statutoryBodyId.toString();
            payerName = statutoryBodyName;
        }
        Integer finalPayerType = payerType;
        Long finalStatutoryBodyId = statutoryBodyId;
        String finalStatutoryBodyName = statutoryBodyName;
        String finalPayType = payType;
        String finalPayeeId = payeeId;
        String finalPayeeName = payeeName;
        String finalPayerId = payerId;
        String finalPayerName = payerName;
        addTemporaryChargeBillFs.forEach(item -> {
            AddTemporaryChargeBillRf temporaryChargeBill = new AddTemporaryChargeBillRf();
            // 法定单位id 名称
            temporaryChargeBill.setStatutoryBodyId(finalStatutoryBodyId);
            temporaryChargeBill.setStatutoryBodyName(finalStatutoryBodyName);
            // 费项id 名称 类型
            temporaryChargeBill.setChargeItemId(item.getChargeItemId());
            ChargeItemRv chargeItemRv = ampFinanceFeignClient.chargeGetById(item.getChargeItemId());
            if (Objects.nonNull(chargeItemRv)) {
                temporaryChargeBill.setChargeItemName(chargeItemRv.getName());
                temporaryChargeBill.setChargeItemType(chargeItemRv.getType());
            }
            // 收付类型
            temporaryChargeBill.setPayType(finalPayType);
            // 发票类型
            temporaryChargeBill.setInvoiceType("[3]");
            //收费对象类型
            temporaryChargeBill.setPayerType(finalPayerType);
            // 账单来源
            temporaryChargeBill.setSource(ContractSetConst.CONTRACT_APP_BOND_PLAN_NAME);
            // 应用id
            temporaryChargeBill.setAppId(ContractSetConst.CONTRACTAPPID);
            // 系统来源
            temporaryChargeBill.setSysSource(2);
            // 应用名称
            temporaryChargeBill.setAppName(ContractSetConst.CONTRACT_APP_BOND_PLAN_NAME);
            // 成本中心id 名称
            temporaryChargeBill.setCostCenterId(item.getCostId());
            OrgFinanceRv orgFinanceRv = orgFeignClient.getByFinanceId(item.getCostId());
            if (Objects.nonNull(orgFinanceRv)) {
                temporaryChargeBill.setCostCenterName(orgFinanceRv.getNameCn());
            }
            // 收付款方id  名称
            temporaryChargeBill.setPayeeId(finalPayeeId);
            temporaryChargeBill.setPayeeName(finalPayeeName);
            temporaryChargeBill.setPayerId(finalPayerId);
            temporaryChargeBill.setPayerName(finalPayerName);
            // 外部业务单号  20221123改为合同编号
//            temporaryChargeBill.setOutBusNo(item.getId().toString());
            temporaryChargeBill.setOutBusNo(contractDetailsV.getContractNo());
            // 20221123改为合同id
            temporaryChargeBill.setOutBusId(contractDetailsV.getId().toString());
            // 20221123改为保证金计划id
            temporaryChargeBill.setExtField1(item.getId().toString());
            // 账单说明
            temporaryChargeBill.setDescription(ContractSetConst.CONTRACT_APP_BOND_PLAN_NAME);
            // 账单金额
            temporaryChargeBill.setTotalAmount(item.getLocalCurrencyAmount().multiply(new BigDecimal("100")).longValue());
            //单价（单位：分）
            temporaryChargeBill.setUnitPrice(item.getLocalCurrencyAmount().multiply(new BigDecimal("100")).longValue());
            //财务中台新加业务，计费方式默认传8（固定金额）
            temporaryChargeBill.setBillMethod(ContractSetConst.BILL_METHOD_GDJE);
            //-- TODO supUnitId default
            temporaryChargeBill.setSupCpUnitId("default");
            addTemporaryChargeBillRfs.add(temporaryChargeBill);
        });
        log.info("新增临时账单：" + JSONObject.toJSONString(addTemporaryChargeBillRfs));
        return ampFinanceFeignClient.temporaryAddBatch(addTemporaryChargeBillRfs);
    }

    @Transactional(rollbackFor = {Exception.class})
    public void carryover(ContractBondPlanCarryoverF from) {
        LocalDateTime now = LocalDateTime.now();
        ContractDetailsV contractConclude = contractConcludeService.selectById(from.getContractId());
        BigDecimal bidBondAmount = contractConclude.getBidBondAmount();
        Long contractBondPlanId = from.getContractBondPlanId();
        if (from.getCarryoverAmount().compareTo(bidBondAmount) != 0) {
            throw BizException.throw400("招投标保证金金额与被结转保证金金额不一致，结转失败");
        }
        if (contractConclude.getBondType() == 1) {
            // 缴纳类  先生成临时账单 再结算  后结转
            ContractBondPlanE contractBondPlanE = contractBondPlanService.getById(contractBondPlanId);
            if (Objects.isNull(contractBondPlanE)) {
                throw BizException.throw400("未找到被结转保证金计划，结转失败");
            }
            ContractBondPlanV contractBondPlanV = Global.mapperFacade.map(contractBondPlanE, ContractBondPlanV.class);
            pushTemporaryBill(contractBondPlanV, contractConclude);
        }
        // 收取类  直接结算  再结转
        long carryoverAmount = bidBondAmount.multiply(new BigDecimal("100")).longValue();
        ContractBondPlanE contractBondPlanE = contractBondPlanService.getById(contractBondPlanId);
        // 收款（结算） // 20221201改为获取已结算的  不需要结算
//        BondPlanAmountF bondPlanAmountF = new BondPlanAmountF();
//        bondPlanAmountF.setBillId(contractConclude.getBidBondBillId());
//        bondPlanAmountF.setBondPlanId(contractBondPlanId);
//        bondPlanAmountF.setCollectionAmount(bidBondAmount);
//        temporarySettleBatch(bondPlanAmountF, contractConclude, -1, from.getRemark());
        if (contractConclude.getBondType() == 0) {
            // 收款明细
            ContractBondCollectionDetailF collectionDetailF = new ContractBondCollectionDetailF();
            collectionDetailF.setContractId(contractConclude.getId());
            collectionDetailF.setBondPlanId(contractBondPlanId);
            collectionDetailF.setCollectionAmount(bidBondAmount);
            collectionDetailF.setCollectionTime(now);
            collectionDetailF.setRemark(from.getRemark());
            contractBondCollectionDetailAppService.saveBondCollectionDetail(collectionDetailF);
        } else {
            // 付款明细
            ContractBondPaymentDetailF contractBondPaymentDetailF = new ContractBondPaymentDetailF();
            contractBondPaymentDetailF.setContractId(contractConclude.getId());
            contractBondPaymentDetailF.setBondPlanId(contractBondPlanId);
            contractBondPaymentDetailF.setPaymentAmount(bidBondAmount);
            contractBondPaymentDetailF.setApplyPaymentTime(now);
            contractBondPaymentDetailF.setType(1);
            contractBondPaymentDetailF.setRemark(from.getRemark());
            contractBondPaymentDetailAppService.saveBondPaymentDetail(contractBondPaymentDetailF);
        }

        // 结转
        TemporaryChargeBillCarryoverRf carryoverRf = new TemporaryChargeBillCarryoverRf();
        carryoverRf.setCarriedBillId(contractConclude.getBidBondBillId());
        carryoverRf.setCarryoverAmount(carryoverAmount);
        carryoverRf.setCarryoverType(1);
        carryoverRf.setRemark(from.getRemark());
        carryoverRf.setAdvanceCarried(0);
        TemporaryChargeBillCarryoverRf.CarryoverDetail carryoverDetail = new BillCarryoverRf.CarryoverDetail();
        carryoverDetail.setBillType(3);
        carryoverDetail.setTargetBillId(contractBondPlanE.getBillId());
        carryoverDetail.setCarryoverAmount(carryoverAmount);
        carryoverRf.setCarryoverDetail(Collections.singletonList(carryoverDetail));
        //-- TODO supUnitId default
        carryoverRf.setSupCpUnitId("default");
        log.info("结转：" + JSONObject.toJSONString(carryoverRf));
        if (!ampFinanceFeignClient.temporaryCarryover(carryoverRf)) {
            throw BizException.throw400("中台结转失败");
        }
        // 更新保证金计划
        contractBondPlanE.setSettleTransferStatus(1);
        contractBondPlanE.setSettleTransferAmount(bidBondAmount);
        contractBondPlanE.setPaymentStatus(2);
        contractBondPlanE.setPaymentAmount(bidBondAmount);
        contractBondPlanService.updateById(contractBondPlanE);
        // 生成结转明细
        ContractBondCarryoverDetailF contractBondCarryoverDetailF = new ContractBondCarryoverDetailF();
        contractBondCarryoverDetailF.setContractId(from.getContractId());
        contractBondCarryoverDetailF.setBondPlanId(from.getContractBondPlanId());
        contractBondCarryoverDetailF.setBidBondBillNo(contractConclude.getBidBondBillNo());
        contractBondCarryoverDetailF.setBidBondAmount(from.getCarryoverAmount());
        contractBondCarryoverDetailF.setCarryoverTime(now);
        contractBondCarryoverDetailF.setRemark(from.getRemark());
        contractBondCarryoverDetailAppService.saveBondPlanCarryoverDetail(contractBondCarryoverDetailF);
        //动态记录
        BizLog.normal(from.getContractId().toString(), new Operator(userId(), userName()),
                new BizObject() {
                    @Override
                    public String getObjName() {
                        return BusinessTypeEnum.保证金结转.getName();
                    }
                },
                LogActionEnum.发起,
                new Content().option(new PlainTextDataItem("申请结转金额：" + from.getCarryoverAmount().toString(), false))
        );
    }

    @Transactional(rollbackFor = {Exception.class})
    public ContractReceiptResultV receipt(BondPlanReceiptF from) {
        ContractReceiptResultV resultV = new ContractReceiptResultV();
        // 保证金计划更新
        ContractBondPlanE contractBondPlanE = contractBondPlanService.getById(from.getBondPlanId());
        // 中台开收据
        if(ObjectUtils.isEmpty(contractBondPlanE.getBillId())){//账单id为空
            throw BizException.throw400("账单id为空，无法收据");
        }
        contractBondPlanE.setReceiptAmount(contractBondPlanE.getReceiptAmount().add(from.getInvoiceAmount()));
        contractBondPlanService.updateById(contractBondPlanE);
        Long receiptId = contractBondReceiptDetailAppService.receiptBatch(contractBondPlanE.getBillId(), from.getInvoiceAmount(), from.getSupCpUnitId());
        ReceiptDetailRf receiptDetailRf = new ReceiptDetailRf();
        receiptDetailRf.setInvoiceReceiptId(receiptId);
        receiptDetailRf.setSupCpUnitId(from.getSupCpUnitId());
        ReceiptDetailRv receiptDetailRv = contractBondReceiptDetailAppService.receiptDetail(receiptDetailRf);
        // 追加收据明细
        ContractBondReceiptDetailF receiptDetailF = Global.mapperFacade.map(from, ContractBondReceiptDetailF.class);
        receiptDetailF.setReceiptId(receiptId);
        receiptDetailF.setInvoiceNumber(receiptDetailRv.getReceiptNo());
        receiptDetailF.setInvoiceTime(LocalDateTime.now());
        receiptDetailF.setInvocieStatus(1);
        ContractBondReceiptDetailE bondReceiptDetailE = contractBondReceiptDetailAppService.saveBondReceiptDetail(receiptDetailF);

        resultV.setFinanceReceiptDetailId(receiptId);
        resultV.setReceiptDetailId(bondReceiptDetailE.getId());
        BizLog.normal(from.getContractId().toString(), new Operator(userId(), userName()),
                new BizObject() {
                    @Override
                    public String getObjName() {
                        return BusinessTypeEnum.保证金收据.getName();
                    }
                },
                LogActionEnum.发起,
                new Content().option(new PlainTextDataItem("收据金额：" + from.getInvoiceAmount().toString(), true))
                        .option(new PlainTextDataItem("收据明细：" + bondReceiptDetailE.getInvoiceNumber(), false))
        );
        return resultV;

    }


    @Transactional(rollbackFor = {Exception.class})
    public void batchDeduction(ContractBondPlanDeductionF form) {
        IdentityInfo identityInfo = curIdentityInfo();
        LocalDateTime now = LocalDateTime.now();
        Integer bondType = form.getBondType();
        ContractDetailsV contractConclude = contractConcludeService.getContractConclude(form.getContractId());
        if (Objects.isNull(contractConclude)) {
            return;
        }
        List<ContractBondPaymentDetailE> bondPaymentDetailES = new ArrayList<>();
        form.getBondPlanAmountFList().forEach(item -> {
            ContractBondPlanE contractBondPlanE = contractBondPlanService.getById(item.getBondPlanId());
            BigDecimal refundAmount = contractBondPlanE.getRefundAmount();
            BigDecimal deductionAmount = contractBondPlanE.getDeductionAmount().add(item.getCollectionAmount());
            //退款+扣款大于已收款金额paymentAmount,则不能扣款
            BigDecimal totalRefundAmount = deductionAmount.add(refundAmount);
            BigDecimal paymentAmount = contractBondPlanE.getPaymentAmount();
            if(paymentAmount.compareTo(totalRefundAmount) >= 0){
                contractBondPlanE.setDeductionAmount(deductionAmount);
                // 追加付/退款明细
                ContractBondPaymentDetailF contractBondPaymentDetailF = Global.mapperFacade.map(form, ContractBondPaymentDetailF.class);
                contractBondPaymentDetailF.setBondPlanId(item.getBondPlanId());
                contractBondPaymentDetailF.setPaymentAmount(item.getCollectionAmount());
                contractBondPaymentDetailF.setAuditStatus(0);
                ContractBondPaymentDetailE bondPaymentDetailE = contractBondPaymentDetailAppService.saveBondPaymentDetail(contractBondPaymentDetailF);
                bondPaymentDetailES.add(bondPaymentDetailE);
                //  更新保证金计划
                contractBondPlanE.setOperator(identityInfo.getUserId());
                contractBondPlanE.setOperatorName(identityInfo.getUserName());
                contractBondPlanE.setGmtModify(now);
                contractBondPlanService.updateById(contractBondPlanE);

//            financeFacade.addContractBondPlanPayBill(item,contractConclude,form);
            }else{
                log.error("扣款+退款大于已收款金额，参数contractBondPlanE：{},CollectionAmount:{}",JSON.toJSONString(contractBondPlanE),item.getCollectionAmount());
            }

        });
        List<String> numbers = bondPaymentDetailES.stream().map(ContractBondPaymentDetailE::getPaymentNumber).collect(Collectors.toList());
        List<BondPlanDeductionAmountF> bondPlanAmountFList = form.getBondPlanAmountFList();
        List<BondPlanAmountF> bondPlanAmountFS = Global.mapperFacade.mapAsList(bondPlanAmountFList, BondPlanAmountF.class);
        collectionLog(bondPlanAmountFS,form.getContractId(),numbers,form.getType(),2);
    }

}
