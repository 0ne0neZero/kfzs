package com.wishare.contract.apps.remote.finance.facade;

import com.google.common.collect.Lists;
import com.wishare.contract.apps.fo.contractset.BondPlanAmountF;
import com.wishare.contract.apps.fo.contractset.CollectionPlanPaymentInvoiceF;
import com.wishare.contract.apps.fo.contractset.ContractBondPlanCollectionF;
import com.wishare.contract.apps.fo.contractset.ContractBondPlanPaymentF;
import com.wishare.contract.apps.remote.clients.FinanceFeignClient;
import com.wishare.contract.apps.remote.clients.OrgFeignClient;
import com.wishare.contract.apps.remote.finance.enums.*;
import com.wishare.contract.apps.remote.finance.fo.AddPayBillRF;
import com.wishare.contract.apps.remote.finance.fo.AddPayDetailRF;
import com.wishare.contract.apps.remote.finance.vo.PayBillRV;
import com.wishare.contract.apps.remote.fo.AddGatherBillDetailF;
import com.wishare.contract.apps.remote.fo.AddGatherBillF;
import com.wishare.contract.apps.remote.fo.AddPayableBillRf;
import com.wishare.contract.apps.remote.vo.PayableBillDetailRv;
import com.wishare.contract.domains.consts.BondTypeEnum;
import com.wishare.contract.domains.consts.ContractNatureEnum;
import com.wishare.contract.domains.entity.contractset.ContractBondPlanE;
import com.wishare.contract.domains.entity.contractset.ContractCollectionPlanE;
import com.wishare.contract.domains.service.contractset.ContractBondPlanService;
import com.wishare.contract.domains.service.contractset.ContractCollectionPlanService;
import com.wishare.contract.domains.service.contractset.ContractConcludeService;
import com.wishare.contract.domains.vo.contractset.ContractDetailsV;
import com.wishare.starter.consts.Const;
import lombok.Setter;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xujian
 * @date 2023/2/3
 * @Description:
 */
@Service
@Slf4j
public class FinanceFacade {

    @Setter(onMethod_ = {@Autowired})
    private FinanceFeignClient financeFeignClient;

    @Setter(onMethod_ = {@Autowired})
    private OrgFeignClient orgFeignClient;

    @Setter(onMethod_ = {@Autowired})
    private ContractCollectionPlanService contractCollectionPlanService;

    @Setter(onMethod_ = {@Autowired})
    private ContractConcludeService contractConcludeService;

    @Setter(onMethod_ = {@Autowired})
    private ContractBondPlanService contractBondPlanService;

    /**
     * 调用财务中台批量新增收款单及收款单明细
     *
     * @param bondPlanAmountF
     * @param contractConclude            合同信息
     * @param contractBondPlanCollectionF
     */
    public void gatherAddBatch(BondPlanAmountF bondPlanAmountF, ContractDetailsV contractConclude, ContractBondPlanCollectionF contractBondPlanCollectionF) {
        //获取保证金计划信息
        ContractBondPlanE contractBondPlanE = contractBondPlanService.getById(bondPlanAmountF.getBondPlanId());

        //获取成本中心名称
        String costCenterName = orgFeignClient.getByFinanceId(contractBondPlanE.getCostId()).getNameCn();
        //获取费项名称
        String chargeItemName = financeFeignClient.chargeName(contractBondPlanE.getChargeItemId());

        Integer collectionMethod = contractBondPlanCollectionF.getCollectionMethod();

        AddGatherBillF addGatherBillF = generalGatherBill(contractConclude, collectionMethod, bondPlanAmountF.getCollectionAmount());
        List<AddGatherBillDetailF> gatherBillDetailves = generalGatherBillDetail(contractBondPlanE, contractConclude, costCenterName, chargeItemName, collectionMethod, bondPlanAmountF.getCollectionAmount());
        addGatherBillF.setAddGatherBillDetails(gatherBillDetailves);
        addGatherBillF.setSupCpUnitId(StringUtils.isBlank(bondPlanAmountF.getSupCpUnitId()) ? "default" : bondPlanAmountF.getSupCpUnitId());
        financeFeignClient.gatherAddBatch(Lists.newArrayList(addGatherBillF));
        log.info("保证金收款对接中台生成数据:{}",addGatherBillF.getOutBusId());
    }


    /**
     * 构建收款单明细
     *
     * @return
     */
    private List<AddGatherBillDetailF> generalGatherBillDetail(ContractBondPlanE contractBondPlanE, ContractDetailsV contractDetailsV, String costCenterName,
                                                               String chargeItemName, Integer collectionMethod, BigDecimal collectionAmount) {
        List<AddGatherBillDetailF> gatherBillDetailFList = Lists.newArrayList();
        AddGatherBillDetailF gatherBillDetailF = new AddGatherBillDetailF();
        gatherBillDetailF.setGatherType(0);//收款类型 0应收，1预收
        gatherBillDetailF.setRecBillId(null);//应收单id
        gatherBillDetailF.setRecBillNo(null);//应收单编号
        gatherBillDetailF.setCostCenterId(contractBondPlanE.getCostId());//成本中心id
        gatherBillDetailF.setCostCenterName(costCenterName);//成本中心名称
        gatherBillDetailF.setChargeItemId(contractBondPlanE.getChargeItemId());//费项id
        gatherBillDetailF.setChargeItemName(chargeItemName);//费项名称
        gatherBillDetailF.setCpOrgId(null);//收费组织id
        gatherBillDetailF.setCpOrgName(null);//收费组织名称
        gatherBillDetailF.setSupCpUnitId(contractDetailsV.getId().toString());//上级收费单元id(取合同id)
        gatherBillDetailF.setSupCpUnitName(contractDetailsV.getName());//上级收费单元名称
        gatherBillDetailF.setCpUnitId(contractBondPlanE.getId().toString());//收费单元id(取保证金id)
        gatherBillDetailF.setCpUnitName("保证金_".concat(contractBondPlanE.getId().toString()));//收费单元名称
        gatherBillDetailF.setPayChannel(GatherChannelEnum.getEnum(collectionMethod).getFinanceCode());//结算渠道 ALIPAY：支付宝，WECHATPAY:微信支付，CASH:现金，POS: POS机，UNIONPAY:银联，SWIPE: 刷卡，BANK:银行汇款，CARRYOVER:结转，CHEQUE: 支票 OTHER: 其他
        gatherBillDetailF.setPayWay(SettleWayEnum.线下.getCode());//结算方式(0线上，1线下)
        gatherBillDetailF.setPayAmount(collectionAmount.multiply(new BigDecimal("100")).longValue());//收款金额（单位：分）
        gatherBillDetailF.setPayerType(1);//收费对象类型
        gatherBillDetailF.setPayeePhone(null);//收款方手机号
        gatherBillDetailF.setPayTime(LocalDateTime.now());//收款时间
        gatherBillDetailF.setChargeStartTime(null);//收费开始时间
        gatherBillDetailF.setChargeEndTime(null);//收费结束时间

        //收款
        if (contractDetailsV.getContractNature() == ContractNatureEnum.收入.getCode()) {
            if (contractDetailsV.getBondType() != null) {
                if (contractDetailsV.getBondType() == BondTypeEnum.缴纳类.getCode()) {
                    gatherBillDetailF.setPayeeId(contractDetailsV.getPartyBId().toString());//收款人ID
                    gatherBillDetailF.setPayeeName(contractDetailsV.getPartyBName());//收款人名称
                    gatherBillDetailF.setPayerId(contractDetailsV.getPartyAId().toString());//付款人ID
                    gatherBillDetailF.setPayerName(contractDetailsV.getPartyAName());//付款人名称
                } else if (contractDetailsV.getBondType() == BondTypeEnum.收取类.getCode()) {
                    gatherBillDetailF.setPayeeId(contractDetailsV.getPartyBId().toString());//收款人ID
                    gatherBillDetailF.setPayeeName(contractDetailsV.getPartyBName());//收款人名称
                    gatherBillDetailF.setPayerId(contractDetailsV.getPartyAId().toString());//付款人ID
                    gatherBillDetailF.setPayerName(contractDetailsV.getPartyAName());//付款人名称
                }
            }

        } else if (contractDetailsV.getContractNature() == ContractNatureEnum.支出.getCode()) {
            if (contractDetailsV.getBondType() != null) {
                if (contractDetailsV.getBondType() == BondTypeEnum.缴纳类.getCode()) {
                    gatherBillDetailF.setPayeeId(contractDetailsV.getPartyAId().toString());//收款人ID
                    gatherBillDetailF.setPayeeName(contractDetailsV.getPartyAName());//收款人名称
                    gatherBillDetailF.setPayerId(contractDetailsV.getPartyBId().toString());//付款人ID
                    gatherBillDetailF.setPayerName(contractDetailsV.getPartyBName());//付款人名称
                } else if (contractDetailsV.getBondType() == BondTypeEnum.收取类.getCode()) {
                    gatherBillDetailF.setPayeeId(contractDetailsV.getPartyAId().toString());//收款人ID
                    gatherBillDetailF.setPayeeName(contractDetailsV.getPartyAName());//收款人名称
                    gatherBillDetailF.setPayerId(contractDetailsV.getPartyBId().toString());//付款人ID
                    gatherBillDetailF.setPayerName(contractDetailsV.getPartyBName());//付款人名称
                }
            }

        }

        gatherBillDetailFList.add(gatherBillDetailF);
        return gatherBillDetailFList;
    }

    /**
     * 构建收款单
     */
    private AddGatherBillF generalGatherBill(ContractDetailsV contractConclude, Integer collectionMethod, BigDecimal collectionAmount) {
        AddGatherBillF gatherBillF = new AddGatherBillF();
        gatherBillF.setOutBillNo(null);//外部账单编号
        gatherBillF.setOutBusNo(contractConclude.getContractNo());//外部业务单号（合同编号）
        gatherBillF.setOutBusId(contractConclude.getId().toString());//外部业务id（合同id）

        gatherBillF.setSbAccountId(null);//收款账号id
        gatherBillF.setStartTime(null);//账单开始时间
        gatherBillF.setEndTime(null);//账单结束时间
        gatherBillF.setPayChannel(GatherChannelEnum.getEnum(collectionMethod).getFinanceCode());//结算渠道 ALIPAY：支付宝，WECHATPAY:微信支付，CASH:现金，POS: POS机，UNIONPAY:银联，SWIPE: 刷卡，BANK:银行汇款，CARRYOVER:结转，CHEQUE: 支票 OTHER: 其他
        gatherBillF.setPayWay(SettleWayEnum.线下.getCode());//结算方式(0线上，1线下)
        gatherBillF.setTaxRateId(null);//税率id
        gatherBillF.setTaxRate(null);//税率
        gatherBillF.setTaxAmount(null);//税额
        gatherBillF.setDescription("合同保证金");//账单说明
        gatherBillF.setCurrency("CNY");//币种(货币代码)（CNY:人民币）
        gatherBillF.setTotalAmount(collectionAmount.multiply(new BigDecimal("100")).longValue());//账单金额（单位：分）

        gatherBillF.setSysSource(SysSourceEnum.合同系统.getCode());
        gatherBillF.setApprovedState(2);
        gatherBillF.setPayTime(LocalDateTime.now());//收款时间

        //收款
        if (contractConclude.getContractNature() == ContractNatureEnum.收入.getCode()) {
            if (contractConclude.getBondType() != null) {
                if (contractConclude.getBondType() == BondTypeEnum.缴纳类.getCode()) {
                    gatherBillF.setStatutoryBodyId(contractConclude.getPartyBId());//法定单位id
                    gatherBillF.setStatutoryBodyName(contractConclude.getPartyBName());//法定单位名称中文

                    gatherBillF.setPayeeId(contractConclude.getPartyBId().toString());//收款人ID
                    gatherBillF.setPayeeName(contractConclude.getPartyBName());//收款人名称
                    gatherBillF.setPayerId(contractConclude.getPartyAId().toString());//付款人ID
                    gatherBillF.setPayerName(contractConclude.getPartyAName());//付款人名称
                } else if (contractConclude.getBondType() == BondTypeEnum.收取类.getCode()) {
                    gatherBillF.setStatutoryBodyId(contractConclude.getPartyBId());//法定单位id
                    gatherBillF.setStatutoryBodyName(contractConclude.getPartyBName());//法定单位名称中文

                    gatherBillF.setPayeeId(contractConclude.getPartyBId().toString());//收款人ID
                    gatherBillF.setPayeeName(contractConclude.getPartyBName());//收款人名称
                    gatherBillF.setPayerId(contractConclude.getPartyAId().toString());//付款人ID
                    gatherBillF.setPayerName(contractConclude.getPartyAName());//付款人名称
                }
            }

        } else if (contractConclude.getContractNature() == ContractNatureEnum.支出.getCode()) {
            if (contractConclude.getBondType() != null) {
                if (contractConclude.getBondType() == BondTypeEnum.缴纳类.getCode()) {
                    gatherBillF.setStatutoryBodyId(contractConclude.getPartyAId());//法定单位id
                    gatherBillF.setStatutoryBodyName(contractConclude.getPartyAName());//法定单位名称中文

                    gatherBillF.setPayeeId(contractConclude.getPartyAId().toString());//收款人ID
                    gatherBillF.setPayeeName(contractConclude.getPartyAName());//收款人名称
                    gatherBillF.setPayerId(contractConclude.getPartyBId().toString());//付款人ID
                    gatherBillF.setPayerName(contractConclude.getPartyBName());//付款人名称
                } else if (contractConclude.getBondType() == BondTypeEnum.收取类.getCode()) {
                    gatherBillF.setStatutoryBodyId(contractConclude.getPartyAId());//法定单位id
                    gatherBillF.setStatutoryBodyName(contractConclude.getPartyAName());//法定单位名称中文

                    gatherBillF.setPayeeId(contractConclude.getPartyAId().toString());//收款人ID
                    gatherBillF.setPayeeName(contractConclude.getPartyAName());//收款人名称
                    gatherBillF.setPayerId(contractConclude.getPartyBId().toString());//付款人ID
                    gatherBillF.setPayerName(contractConclude.getPartyBName());//付款人名称
                }
            }
        }
        return gatherBillF;
    }



    /**
     * 保证金生成付款单和付款单明细
     */
    public void addContractBondPlanPayBill(BondPlanAmountF bondPlanAmountF, ContractDetailsV contractConclude, ContractBondPlanPaymentF contractBondPlanPaymentF) {
        //获取保证金计划信息
        ContractBondPlanE contractBondPlanE = contractBondPlanService.getById(bondPlanAmountF.getBondPlanId());

        //获取成本中心名称
        String costCenterName = orgFeignClient.getByFinanceId(contractBondPlanE.getCostId()).getNameCn();
        //获取费项名称
        String chargeItemName = financeFeignClient.chargeName(contractBondPlanE.getChargeItemId());

        Integer paymentMethod = contractBondPlanPaymentF.getPaymentMethod();
        BigDecimal paymentAmount = bondPlanAmountF.getCollectionAmount();

        AddPayBillRF addPayBillRF = generalContractBondPlanPayBill(contractConclude, contractBondPlanE, costCenterName, chargeItemName, paymentMethod, paymentAmount);
        List<AddPayDetailRF> addPayDetailRFS = generalContractBondPlanPayDetail(contractConclude, contractBondPlanE, costCenterName, chargeItemName, paymentMethod, paymentAmount);
        addPayBillRF.setAddPayDetailves(addPayDetailRFS);
        addPayBillRF.setSupCpUnitId(contractBondPlanPaymentF.getSupCpUnitId());
        financeFeignClient.addPayBill(addPayBillRF);
    }

    /**
     * 构建保证金明细
     *
     * @return
     */
    private List<AddPayDetailRF> generalContractBondPlanPayDetail(ContractDetailsV contractConclude, ContractBondPlanE contractBondPlanE, String costCenterName, String chargeItemName, Integer paymentMethod, BigDecimal paymentAmount) {
        List<AddPayDetailRF> addPayDetailRFList = Lists.newArrayList();

        AddPayDetailRF payDetailRF = new AddPayDetailRF();
        payDetailRF.setCostCenterId(contractBondPlanE.getCostId());//成本中心id
        payDetailRF.setCostCenterName(costCenterName);//成本中心名称
        payDetailRF.setChargeItemId(contractBondPlanE.getChargeItemId());//费项id
        payDetailRF.setChargeItemName(chargeItemName);//费项名称
        payDetailRF.setCpOrgId(null);//收费组织id
        payDetailRF.setCpOrgName(null);//收费组织名称
        payDetailRF.setSupCpUnitId(contractConclude.getId().toString());//上级收费单元id(取合同id)
        payDetailRF.setSupCpUnitName(contractConclude.getName());//上级收费单元名称
        payDetailRF.setCpUnitId(contractBondPlanE.getId().toString());//收费单元id(取付款计划id)
        payDetailRF.setCpUnitName("付款计划_".concat(contractBondPlanE.getId().toString()));//收费单元名称
        if(paymentMethod != null){
            //结算渠道 ALIPAY：支付宝，WECHATPAY:微信支付，CASH:现金，POS: POS机，UNIONPAY:银联，SWIPE: 刷卡，BANK:银行汇款，CARRYOVER:结转，CHEQUE: 支票 OTHER: 其他
            payDetailRF.setPayChannel(PayChannelEnum.getEnum(paymentMethod).getFinanceCode());
        }
        payDetailRF.setPayWay(SettleWayEnum.线下.getCode());//结算方式(0线上，1线下)
        payDetailRF.setOutPayNo(null);//外部支付编号（支付宝单号，银行流水号等）

        long payAmountLong = paymentAmount.multiply(new BigDecimal("100")).longValue();
        payDetailRF.setRecPayAmount(payAmountLong);//应付金额（单位：分）
        payDetailRF.setPayAmount(payAmountLong);//付款金额（单位：分）

        payDetailRF.setPayerType(1);//收费对象类型
        payDetailRF.setPayerId(contractConclude.getPartyBId().toString());//付款人id(乙方)
        payDetailRF.setPayerName(contractConclude.getPartyBName());//付款人名称
        payDetailRF.setPayeeId(contractConclude.getPartyAId().toString());//收款人id（甲方）
        payDetailRF.setPayeeName(contractConclude.getPartyAName());//收款人名称
        payDetailRF.setPayTime(LocalDateTime.now());//付款时间
        payDetailRF.setChargeStartTime(null);//收费开始时间
        payDetailRF.setChargeEndTime(null);//收费结束时间

        //付款
        if (contractConclude.getContractNature() == ContractNatureEnum.收入.getCode()) {
            if (contractConclude.getBondType() != null) {
                if (contractConclude.getBondType() == BondTypeEnum.缴纳类.getCode()) {
                    payDetailRF.setPayeeId(contractConclude.getPartyAId().toString());//收款人ID
                    payDetailRF.setPayeeName(contractConclude.getPartyAName());//收款人名称
                    payDetailRF.setPayerId(contractConclude.getPartyBId().toString());//付款人ID
                    payDetailRF.setPayerName(contractConclude.getPartyBName());//付款人名称
                } else if (contractConclude.getBondType() == BondTypeEnum.收取类.getCode()) {
                    payDetailRF.setPayeeId(contractConclude.getPartyAId().toString());//收款人ID
                    payDetailRF.setPayeeName(contractConclude.getPartyAName());//收款人名称
                    payDetailRF.setPayerId(contractConclude.getPartyBId().toString());//付款人ID
                    payDetailRF.setPayerName(contractConclude.getPartyBName());//付款人名称
                }
            }

        } else if (contractConclude.getContractNature() == ContractNatureEnum.支出.getCode()) {
            if (contractConclude.getBondType() != null) {
                if (contractConclude.getBondType() == BondTypeEnum.缴纳类.getCode()) {
                    payDetailRF.setPayeeId(contractConclude.getPartyBId().toString());//收款人ID
                    payDetailRF.setPayeeName(contractConclude.getPartyBName());//收款人名称
                    payDetailRF.setPayerId(contractConclude.getPartyAId().toString());//付款人ID
                    payDetailRF.setPayerName(contractConclude.getPartyAName());//付款人名称
                } else if (contractConclude.getBondType() == BondTypeEnum.收取类.getCode()) {
                    payDetailRF.setPayeeId(contractConclude.getPartyBId().toString());//收款人ID
                    payDetailRF.setPayeeName(contractConclude.getPartyBName());//收款人名称
                    payDetailRF.setPayerId(contractConclude.getPartyAId().toString());//付款人ID
                    payDetailRF.setPayerName(contractConclude.getPartyAName());//付款人名称
                }
            }
        }

        addPayDetailRFList.add(payDetailRF);
        return addPayDetailRFList;
    }

    /**
     * 构建保证金的付款单
     *
     * @return
     */
    private AddPayBillRF generalContractBondPlanPayBill(ContractDetailsV contractConclude, ContractBondPlanE contractBondPlanE, String costCenterName, String chargeItemName,
                                                        Integer collectionMethod, BigDecimal paymentAmount) {
        AddPayBillRF addPayBillRF = new AddPayBillRF();
        addPayBillRF.setOutBillNo(null);//外部账单编号
        addPayBillRF.setOutBusNo(contractConclude.getContractNo());//外部业务单号(存合同编号)
        addPayBillRF.setOutBusId(contractConclude.getId().toString());//外部业务id(存合同id)
        addPayBillRF.setCostCenterId(contractBondPlanE.getCostId().toString());//成本中心id
        addPayBillRF.setCostCenterName(costCenterName);//成本中心名称
        addPayBillRF.setStatutoryBodyId(contractConclude.getPartyAId());//法定单位id(付款的时候取甲方信息)
        addPayBillRF.setStatutoryBodyName(contractConclude.getPartyAName());//法定单位名称
        addPayBillRF.setChargeItemId(contractBondPlanE.getChargeItemId());//费项id
        addPayBillRF.setChargeItemName(chargeItemName);//费项名称
        addPayBillRF.setCpOrgId(null);//收费组织id
        addPayBillRF.setCpOrgName(null);//收费组织名称
        addPayBillRF.setSupCpUnitId(contractConclude.getId().toString());//上级收费单元id(取合同id)
        addPayBillRF.setSupCpUnitName(contractConclude.getName());//上级收费单元名称
        addPayBillRF.setCpUnitId(contractBondPlanE.getId().toString());//收费单元id(取付款计划id)
        addPayBillRF.setCpUnitName("付款计划_".concat(contractBondPlanE.getId().toString()));//收费单元名称
        addPayBillRF.setSbAccountId(null);//付款账号id
        addPayBillRF.setPnAccountId(null);//收款账号id
        addPayBillRF.setStartTime(null);//账单开始时间
        addPayBillRF.setEndTime(null);//账单结束时间
        addPayBillRF.setPayTime(LocalDateTime.now());//付款时间
        addPayBillRF.setPayChannel(PayChannelEnum.getEnum(collectionMethod).getFinanceCode());//结算渠道 ALIPAY：支付宝，WECHATPAY:微信支付，CASH:现金，POS: POS机，UNIONPAY:银联，SWIPE: 刷卡，BANK:银行汇款，CARRYOVER:结转，CHEQUE: 支票 OTHER: 其他
        addPayBillRF.setPayWay(SettleWayEnum.线下.getCode());//付款方式(0线上，1线下)
        addPayBillRF.setPayType(PayTypeEnum.普通付款.getCode());//付款类型：0普通付款，1退款付款
        addPayBillRF.setTaxRateId(null);//税率id
        addPayBillRF.setTaxRate(null);//税率
        addPayBillRF.setTaxAmount(null);//税额（需要转为分）
        addPayBillRF.setDescription(null);//账单说明
        addPayBillRF.setCurrency("CNY");//币种(货币代码)（CNY:人民币）
        addPayBillRF.setTotalAmount(paymentAmount.multiply(new BigDecimal("100")).longValue());//账单金额（单位：分）
        addPayBillRF.setDiscountAmount(null);//实付减免金额（单位：分）
        addPayBillRF.setPayeeId(contractConclude.getPartyAId().toString());//收款人ID（甲方）
        addPayBillRF.setPayeeName(contractConclude.getPartyAName());//收款人名称
        addPayBillRF.setPayerId(contractConclude.getPartyBId().toString());//付款人ID（乙方）
        addPayBillRF.setPayerName(contractConclude.getPartyBName());//付款人名称
        addPayBillRF.setSysSource(SysSourceEnum.合同系统.getCode());//系统来源 1收费系统，2合同系统，3民宿管理，101亿家优选系统，102BPM系统

        //付款
        if (contractConclude.getContractNature() == ContractNatureEnum.收入.getCode()) {
            if (contractConclude.getBondType() != null) {
                if (contractConclude.getBondType() == BondTypeEnum.缴纳类.getCode()) {
                    addPayBillRF.setStatutoryBodyId(contractConclude.getPartyBId());//法定单位id
                    addPayBillRF.setStatutoryBodyName(contractConclude.getPartyBName());//法定单位名称中文

                    addPayBillRF.setPayeeId(contractConclude.getPartyAId().toString());//收款人ID
                    addPayBillRF.setPayeeName(contractConclude.getPartyAName());//收款人名称
                    addPayBillRF.setPayerId(contractConclude.getPartyBId().toString());//付款人ID
                    addPayBillRF.setPayerName(contractConclude.getPartyBName());//付款人名称
                } else if (contractConclude.getBondType() == BondTypeEnum.收取类.getCode()) {
                    addPayBillRF.setStatutoryBodyId(contractConclude.getPartyBId());//法定单位id
                    addPayBillRF.setStatutoryBodyName(contractConclude.getPartyBName());//法定单位名称中文

                    addPayBillRF.setPayeeId(contractConclude.getPartyAId().toString());//收款人ID
                    addPayBillRF.setPayeeName(contractConclude.getPartyAName());//收款人名称
                    addPayBillRF.setPayerId(contractConclude.getPartyBId().toString());//付款人ID
                    addPayBillRF.setPayerName(contractConclude.getPartyBName());//付款人名称
                }
            }

        } else if (contractConclude.getContractNature() == ContractNatureEnum.支出.getCode()) {
            if (contractConclude.getBondType() != null) {
                if (contractConclude.getBondType() == BondTypeEnum.缴纳类.getCode()) {
                    addPayBillRF.setStatutoryBodyId(contractConclude.getPartyAId());//法定单位id
                    addPayBillRF.setStatutoryBodyName(contractConclude.getPartyAName());//法定单位名称中文

                    addPayBillRF.setPayeeId(contractConclude.getPartyBId().toString());//收款人ID
                    addPayBillRF.setPayeeName(contractConclude.getPartyBName());//收款人名称
                    addPayBillRF.setPayerId(contractConclude.getPartyAId().toString());//付款人ID
                    addPayBillRF.setPayerName(contractConclude.getPartyAName());//付款人名称
                } else if (contractConclude.getBondType() == BondTypeEnum.收取类.getCode()) {
                    addPayBillRF.setStatutoryBodyId(contractConclude.getPartyAId());//法定单位id
                    addPayBillRF.setStatutoryBodyName(contractConclude.getPartyAName());//法定单位名称中文

                    addPayBillRF.setPayeeId(contractConclude.getPartyBId().toString());//收款人ID
                    addPayBillRF.setPayeeName(contractConclude.getPartyBName());//收款人名称
                    addPayBillRF.setPayerId(contractConclude.getPartyAId().toString());//付款人ID
                    addPayBillRF.setPayerName(contractConclude.getPartyAName());//付款人名称
                }
            }
        }

        return addPayBillRF;
    }

    /**
     * 付款计划调用财务中台生成付款单及付款单明细
     */
    public void addPayBill(CollectionPlanPaymentInvoiceF collectionPlanPaymentInvoiceF) {
        //获取付款计划
        ContractCollectionPlanE contractCollectionPlanE = contractCollectionPlanService.getById(collectionPlanPaymentInvoiceF.getCollectionPlanId());
        //获取合同信息
        ContractDetailsV contractConclude = contractConcludeService.getContractConclude(collectionPlanPaymentInvoiceF.getContractId());

        Integer paymentMethod = collectionPlanPaymentInvoiceF.getPaymentMethod();
        BigDecimal paymentAmount = collectionPlanPaymentInvoiceF.getPaymentAmount();

        //获取成本中心名称
        String costCenterName = orgFeignClient.getByFinanceId(contractCollectionPlanE.getCostId()).getNameCn();
        //获取费项名称
        String chargeItemName = financeFeignClient.chargeName(contractCollectionPlanE.getChargeItemId());

        AddPayBillRF addPayBillRF = generalPayBillRF(contractConclude, contractCollectionPlanE, paymentMethod, paymentAmount, costCenterName, chargeItemName);
        List<AddPayDetailRF> addPayDetailRFS = generalAddPayDetailve(contractConclude, contractCollectionPlanE, costCenterName, chargeItemName, paymentMethod, paymentAmount);
        addPayBillRF.setAddPayDetailves(addPayDetailRFS);
        addPayBillRF.setSupCpUnitId(collectionPlanPaymentInvoiceF.getSupCpUnitId());
        PayBillRV payBillRV = financeFeignClient.addPayBill(addPayBillRF);
    }

    /**
     * 构建付款单明细数据
     *
     * @return
     */
    private List<AddPayDetailRF> generalAddPayDetailve(ContractDetailsV contractConclude, ContractCollectionPlanE contractCollectionPlanE, String costCenterName, String chargeItemName, Integer paymentMethod, BigDecimal paymentAmount) {
        List<AddPayDetailRF> addPayDetailRFList = Lists.newArrayList();

        AddPayDetailRF payDetailRF = new AddPayDetailRF();
        payDetailRF.setCostCenterId(contractCollectionPlanE.getCostId());//成本中心id
        payDetailRF.setCostCenterName(costCenterName);//成本中心名称
        payDetailRF.setChargeItemId(contractCollectionPlanE.getChargeItemId());//费项id
        payDetailRF.setChargeItemName(chargeItemName);//费项名称
        payDetailRF.setCpOrgId(null);//收费组织id
        payDetailRF.setCpOrgName(null);//收费组织名称
        payDetailRF.setSupCpUnitId(contractConclude.getId().toString());//上级收费单元id(取合同id)
        payDetailRF.setSupCpUnitName(contractConclude.getName());//上级收费单元名称
        payDetailRF.setCpUnitId(contractCollectionPlanE.getId().toString());//收费单元id(取付款计划id)
        payDetailRF.setCpUnitName("付款计划_".concat(contractCollectionPlanE.getId().toString()));//收费单元名称
        if(paymentMethod != null){
            payDetailRF.setPayChannel(PayChannelEnum.getEnum(paymentMethod).getFinanceCode());//结算渠道
        }
        payDetailRF.setPayWay(SettleWayEnum.线下.getCode());//结算方式(0线上，1线下)
        payDetailRF.setOutPayNo(null);//外部支付编号（支付宝单号，银行流水号等）

        long payAmountLong = paymentAmount.multiply(new BigDecimal("100")).longValue();
        payDetailRF.setRecPayAmount(payAmountLong);//应付金额（单位：分）
        payDetailRF.setPayAmount(payAmountLong);//付款金额（单位：分）

        payDetailRF.setPayerType(1);//收费对象类型
        payDetailRF.setPayerId(contractConclude.getPartyBId().toString());//付款人id(乙方)
        payDetailRF.setPayerName(contractConclude.getPartyBName());//付款人名称
        payDetailRF.setPayeeId(contractConclude.getPartyAId().toString());//收款人id（甲方）
        payDetailRF.setPayeeName(contractConclude.getPartyAName());//收款人名称
        payDetailRF.setPayTime(LocalDateTime.now());//付款时间
        payDetailRF.setChargeStartTime(null);//收费开始时间
        payDetailRF.setChargeEndTime(null);//收费结束时间
        addPayDetailRFList.add(payDetailRF);
        return addPayDetailRFList;
    }

    /**
     * 构建付款单数据
     *
     * @param contractConclude
     * @param contractCollectionPlanE
     * @param paymentMethod           付款方式
     * @param paymentAmount           付款金额
     * @param costCenterName          成本中心名称
     * @param chargeItemName
     * @return
     */
    private AddPayBillRF generalPayBillRF(ContractDetailsV contractConclude, ContractCollectionPlanE contractCollectionPlanE, Integer paymentMethod, BigDecimal paymentAmount, String costCenterName, String chargeItemName) {
        AddPayBillRF addPayBillRF = new AddPayBillRF();
        addPayBillRF.setOutBillNo(null);//外部账单编号
        addPayBillRF.setOutBusNo(contractConclude.getContractNo());//外部业务单号(存合同编号)
        addPayBillRF.setOutBusId(contractConclude.getId().toString());//外部业务id(存合同id)
        addPayBillRF.setCostCenterId(contractCollectionPlanE.getCostId().toString());//成本中心id
        addPayBillRF.setCostCenterName(costCenterName);//成本中心名称
        addPayBillRF.setStatutoryBodyId(contractConclude.getPartyAId());//法定单位id(付款的时候取甲方信息)
        addPayBillRF.setStatutoryBodyName(contractConclude.getPartyAName());//法定单位名称
        addPayBillRF.setChargeItemId(contractCollectionPlanE.getChargeItemId());//费项id
        addPayBillRF.setChargeItemName(chargeItemName);//费项名称
        addPayBillRF.setCpOrgId(null);//收费组织id
        addPayBillRF.setCpOrgName(null);//收费组织名称
        addPayBillRF.setSupCpUnitId(contractConclude.getId().toString());//上级收费单元id(取合同id)
        addPayBillRF.setSupCpUnitName(contractConclude.getName());//上级收费单元名称
        addPayBillRF.setCpUnitId(contractCollectionPlanE.getId().toString());//收费单元id(取付款计划id)
        addPayBillRF.setCpUnitName("付款计划_".concat(contractCollectionPlanE.getId().toString()));//收费单元名称
        addPayBillRF.setSbAccountId(null);//付款账号id
        addPayBillRF.setPnAccountId(null);//收款账号id
        addPayBillRF.setStartTime(null);//账单开始时间
        addPayBillRF.setEndTime(null);//账单结束时间
        addPayBillRF.setPayTime(LocalDateTime.now());//付款时间
        if(paymentMethod != null){
            //结算渠道 ALIPAY：支付宝，WECHATPAY:微信支付，CASH:现金，POS: POS机，UNIONPAY:银联，SWIPE: 刷卡，BANK:银行汇款，CARRYOVER:结转，CHEQUE: 支票 OTHER: 其他
            addPayBillRF.setPayChannel(PayChannelEnum.getEnum(paymentMethod).getFinanceCode());
        }
        addPayBillRF.setPayWay(SettleWayEnum.线下.getCode());//付款方式(0线上，1线下)
        addPayBillRF.setPayType(PayTypeEnum.普通付款.getCode());//付款类型：0普通付款，1退款付款
        addPayBillRF.setTaxRateId(getTaxRateId(contractCollectionPlanE.getTaxRateIdPath()));//税率id
        addPayBillRF.setTaxRate(contractCollectionPlanE.getTaxRate());//税率
        addPayBillRF.setTaxAmount(contractCollectionPlanE.getTaxAmount().multiply(new BigDecimal("100")).longValue());//税额（需要转为分）
        addPayBillRF.setDescription(null);//账单说明
        addPayBillRF.setCurrency("CNY");//币种(货币代码)（CNY:人民币）
        addPayBillRF.setTotalAmount(paymentAmount.multiply(new BigDecimal("100")).longValue());//账单金额（单位：分）
        addPayBillRF.setDiscountAmount(null);//实付减免金额（单位：分）
        addPayBillRF.setPayeeId(contractConclude.getPartyAId().toString());//收款人ID（甲方）
        addPayBillRF.setPayeeName(contractConclude.getPartyAName());//收款人名称
        addPayBillRF.setPayerId(contractConclude.getPartyBId().toString());//付款人ID（乙方）
        addPayBillRF.setPayerName(contractConclude.getPartyBName());//付款人名称
        addPayBillRF.setSysSource(SysSourceEnum.合同系统.getCode());//系统来源 1收费系统，2合同系统，3民宿管理，101亿家优选系统，102BPM系统
        return addPayBillRF;
    }

    /**
     * 获取税率id
     *
     * @param collectionPlanTaxRateId
     * @return
     */
    public Long getTaxRateId(String collectionPlanTaxRateId) {
        if(StringUtils.isNotBlank(collectionPlanTaxRateId)){
            List<Long> collect = Arrays.stream(collectionPlanTaxRateId.split(",")).map(Long::valueOf).collect(Collectors.toList());
            return collect.get(collect.size() - 1);
        }
        return null;
    }

    /**
     * 生成应付单
     */
    public PayableBillDetailRv addPayableBill(CollectionPlanPaymentInvoiceF collectionPlanPaymentInvoiceF) {
        //获取付款计划
        ContractCollectionPlanE contractCollectionPlanE = contractCollectionPlanService.getById(collectionPlanPaymentInvoiceF.getCollectionPlanId());
        //获取合同信息
        ContractDetailsV contractConclude = contractConcludeService.getContractConclude(collectionPlanPaymentInvoiceF.getContractId());

        Integer paymentMethod = collectionPlanPaymentInvoiceF.getPaymentMethod();
        BigDecimal paymentAmount = collectionPlanPaymentInvoiceF.getPaymentAmount();

        //获取成本中心名称
        String costCenterName = orgFeignClient.getByFinanceId(contractCollectionPlanE.getCostId()).getNameCn();
        //获取费项名称
        String chargeItemName = financeFeignClient.chargeName(contractCollectionPlanE.getChargeItemId());


        AddPayableBillRf payableBill = generalPayableBill(contractConclude, contractCollectionPlanE, paymentMethod, paymentAmount, costCenterName, chargeItemName);
        return financeFeignClient.addPayable(payableBill);
    }

    /**
     * 构建应付单
     *
     * @return
     */
    private AddPayableBillRf generalPayableBill(ContractDetailsV contractConclude, ContractCollectionPlanE contractCollectionPlanE, Integer paymentMethod,
                                                BigDecimal paymentAmount, String costCenterName, String chargeItemName) {
        AddPayableBillRf payableBill = new AddPayableBillRf();
        payableBill.setBillMethod(Const.State._2);//计费方式
        payableBill.setChargingArea(null);//计费面积(单位：m²)
        payableBill.setUnitPrice(null);//单价（单位：分）
        payableBill.setStartTime(null);//账单开始时间
        payableBill.setEndTime(null);//账单结束时间
        payableBill.setCostCenterId(contractCollectionPlanE.getCostId());//成本中心id
        payableBill.setCostCenterName(costCenterName);//成本中心名称
        payableBill.setPayerAccount(null);//付款方账户
        payableBill.setPayeeAccount(null);//收款方账户
        if(paymentMethod != null){
            payableBill.setSettleChannel(PayChannelEnum.getEnum(paymentMethod).getFinanceCode());//结算渠道（ALIPAY：支付宝，WECHATPAY:微信支付，CASH:现金，UNIONPAY:银联，BANK:银行汇款，OTHER: 其他）
        }
        payableBill.setStatutoryBodyId(contractConclude.getPartyAId());//法定单位id
        payableBill.setStatutoryBodyName(contractConclude.getPartyAName());//法定单位名称
        payableBill.setCommunityId(null);//项目ID
        payableBill.setCommunityName(null);//项目名称
        payableBill.setChargeItemId(contractCollectionPlanE.getChargeItemId());//费项id
        payableBill.setChargeItemName(chargeItemName);//费项名称
        payableBill.setRoomId(null);//房号ID
        payableBill.setRoomName(null);//房号名称
        payableBill.setPayerType(1);//付款方类型（0:业主，1开发商，2租客,4法定单位）
        payableBill.setPayeeType(1);//收款方类型（0:业主，1开发商，2租客,4法定单位）
        payableBill.setOutBillNo(null);//外部账单编号
        payableBill.setOutBusNo(contractConclude.getContractNo());//外部业务单号
        payableBill.setOutBusId(contractConclude.getId().toString());//外部业务id
        payableBill.setDescription(null);//账单说明
        payableBill.setCurrency("CNY");//币种(货币代码)（默认：CNY:人民币）
        payableBill.setTotalAmount(handleAmountToLong(paymentAmount));//账单金额
        payableBill.setPayeeId(contractConclude.getPartyAId().toString());//收款方ID
        payableBill.setPayeeName(contractConclude.getPartyAName());//收款方名称
        payableBill.setPayerId(contractConclude.getPartyBId().toString());//付款方ID
        payableBill.setPayerName(contractConclude.getPartyBName());//付款方名称
        payableBill.setSource("合同");//账单来源
        payableBill.setTaxRateId(getTaxRateId(contractCollectionPlanE.getTaxRateIdPath()));//税率id
        payableBill.setTaxRate(contractCollectionPlanE.getTaxRate());//税率
        payableBill.setOutBusId(null);//外部业务id
        payableBill.setAppNumber("CONTRACT_SYS");//应用编码
        payableBill.setSysSource(SysSourceEnum.合同系统.getCode());
        return payableBill;
    }

    /**
     * 将bigdecimal处理为分
     *
     * @param amount
     * @return
     */
    private Long handleAmountToLong(BigDecimal amount) {
        return amount.multiply(new BigDecimal("100")).longValue();
    }


}
