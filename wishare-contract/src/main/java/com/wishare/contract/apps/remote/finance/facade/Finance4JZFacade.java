package com.wishare.contract.apps.remote.finance.facade;

import com.alibaba.fastjson.JSONObject;
import com.wishare.contract.apps.fo.revision.pay.bill.ContractSettlementsBillF;
import com.wishare.contract.apps.fo.revision.pay.bill.ContractSettlementsFundF;
import com.wishare.contract.apps.remote.clients.FinanceFeignClient;
import com.wishare.contract.apps.remote.clients.OrgFeignClient;
import com.wishare.contract.apps.remote.finance.fo.AddPayBillRF;
import com.wishare.contract.apps.remote.finance.fo.AddPayDetailRF;
import com.wishare.contract.apps.remote.fo.*;
import com.wishare.contract.apps.remote.vo.ReceivableBillDetailRv;
import com.wishare.contract.apps.remote.vo.ReceivableDeleteRv;
import com.wishare.contract.domains.entity.revision.bond.CollectBondRelationBillE;
import com.wishare.contract.domains.entity.revision.bond.RevisionBondCollectE;
import com.wishare.contract.domains.entity.revision.bond.pay.PayBondRelationBillE;
import com.wishare.contract.domains.entity.revision.bond.pay.RevisionBondPayE;
import com.wishare.contract.domains.entity.revision.income.ContractIncomeConcludeE;
import com.wishare.contract.domains.entity.revision.income.ContractIncomeConcludeProfitLossE;
import com.wishare.contract.domains.entity.revision.income.ContractIncomePlanConcludeE;
import com.wishare.contract.domains.entity.revision.income.ContractIncomeSettlementConcludeE;
import com.wishare.contract.domains.entity.revision.income.settdetails.ContractIncomeSettDetailsE;
import com.wishare.contract.domains.entity.revision.pay.ContractPayConcludeE;
import com.wishare.contract.domains.entity.revision.pay.ContractPayConcludeProfitLossE;
import com.wishare.contract.domains.entity.revision.pay.ContractPayPlanConcludeE;
import com.wishare.contract.domains.entity.revision.pay.ContractPaySettlementConcludeE;
import com.wishare.contract.domains.entity.revision.pay.bill.ContractSettlementsBillE;
import com.wishare.contract.domains.entity.revision.pay.settdetails.ContractPaySettDetailsE;
import com.wishare.contract.domains.mapper.revision.income.ContractIncomeConcludeMapper;
import com.wishare.contract.domains.mapper.revision.pay.ContractPayConcludeMapper;
import com.wishare.contract.domains.mapper.revision.pay.bill.ContractSettlementsBillMapper;
import com.wishare.contract.domains.service.contractset.ContractBondCollectionDetailService;
import com.wishare.contract.domains.service.revision.bond.RevisionBondCollectService;
import com.wishare.contract.domains.service.revision.bond.pay.RevisionBondPayService;
import com.wishare.contract.domains.service.revision.income.ContractIncomeConcludeService;
import com.wishare.contract.domains.service.revision.income.ContractIncomePlanConcludeService;
import com.wishare.contract.domains.service.revision.income.ContractIncomeSettlementConcludeService;
import com.wishare.contract.domains.service.revision.income.settdetails.ContractIncomeSettDetailsService;
import com.wishare.contract.domains.service.revision.pay.ContractPayConcludeService;
import com.wishare.contract.domains.service.revision.pay.ContractPayPlanConcludeService;
import com.wishare.contract.domains.service.revision.pay.ContractPaySettlementConcludeService;
import com.wishare.contract.domains.service.revision.pay.settdetails.ContractPaySettDetailsService;
import com.wishare.starter.exception.BizException;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 功能解释
 *
 * @author 龙江锋
 * @date 2023/9/24 10:46
 */
@Service
@Slf4j
public class Finance4JZFacade {


    @Setter(onMethod_ = {@Autowired})
    private FinanceFeignClient financeFeignClient;

    @Setter(onMethod_ = {@Autowired})
    private OrgFeignClient orgFeignClient;

    @Setter(onMethod_ = {@Autowired})
    private ContractIncomeSettlementConcludeService contractIncomeSettlementConcludeService;

    @Setter(onMethod_ = {@Autowired})
    private ContractIncomePlanConcludeService contractIncomePlanConcludeService;

    @Setter(onMethod_ = {@Autowired})
    private ContractIncomeConcludeMapper contractIncomeConcludeService;

    @Setter(onMethod_ = {@Autowired})
    private ContractIncomeSettDetailsService contractIncomeSettDetailsService;

    @Setter(onMethod_ = {@Autowired})
    private RevisionBondCollectService revisionBondCollectService;

    @Setter(onMethod_ = {@Autowired})
    @Lazy
    private ContractPaySettlementConcludeService contractPaySettlementConcludeService;

    @Setter(onMethod_ = {@Autowired})
    private ContractPayPlanConcludeService contractPayPlanConcludeService;

    @Setter(onMethod_ = {@Autowired})
    private ContractPayConcludeMapper contractPayConcludeService;

    @Setter(onMethod_ = {@Autowired})
    private ContractPaySettDetailsService contractPaySettDetailsService;

    @Setter(onMethod_ = {@Autowired})
    private ContractBondCollectionDetailService contractBondCollectionDetailService;

    @Setter(onMethod_ = {@Autowired})
    private RevisionBondPayService revisionBondPayService;

    @Setter(onMethod_ = {@Autowired})
    private ContractSettlementsBillMapper contractSettlementsBillMapper;


    public void gatherAddBatch4Settlement(ContractSettlementsFundF contractSettlementsFundF){
        ContractIncomeSettlementConcludeE settlementConcludeE = contractIncomeSettlementConcludeService.getById(contractSettlementsFundF.getSettlementId());
        if(settlementConcludeE != null){
            ContractIncomeConcludeE concludeE = contractIncomeConcludeService.selectById(settlementConcludeE.getContractId());
            // todo 我也不知道为什么这么写，价值是什么?这个字段没用，已删除，后面再看看怎么个事儿
            ContractIncomePlanConcludeE planConcludeE = contractIncomePlanConcludeService.getById(settlementConcludeE.getContractId());
            ContractIncomeSettDetailsE settDetailsE = contractIncomeSettDetailsService.getById(settlementConcludeE.getId());
            AddGatherBillF addGatherBillF = new AddGatherBillF();
            addGatherBillF.setOutBusId(concludeE.getId());
            addGatherBillF.setOutBusNo(concludeE.getContractNo());
            addGatherBillF.setSupCpUnitId(concludeE.getCommunityId());
            addGatherBillF.setSysSource(2);
            addGatherBillF.setPayeeId(concludeE.getOurPartyId());
            addGatherBillF.setPayeeName(concludeE.getOurParty());
            addGatherBillF.setPayerId(concludeE.getOppositeOneId());
            addGatherBillF.setPayerName(concludeE.getOppositeOne());
            addGatherBillF.setCurrency(concludeE.getCurrency());
            addGatherBillF.setDescription(settDetailsE.getRemark());
            addGatherBillF.setTaxAmount(planConcludeE.getTaxAmount().longValue());
            addGatherBillF.setTaxRate(new BigDecimal(planConcludeE.getTaxRate()));
            addGatherBillF.setTaxRateId(planConcludeE.getTaxRateId());
            addGatherBillF.setTotalAmount(planConcludeE.getReceiptAmount().longValue());
            AddGatherBillDetailF addGatherBillDetailF = new AddGatherBillDetailF();
            addGatherBillDetailF.setPayeeId(concludeE.getOurPartyId());
            addGatherBillDetailF.setPayeeName(concludeE.getOurParty());
            addGatherBillDetailF.setPayerId(concludeE.getOppositeOneId());
            addGatherBillDetailF.setPayerName(concludeE.getOppositeOne());
            addGatherBillDetailF.setChargeEndTime(settDetailsE.getGmtCreate());
            addGatherBillDetailF.setPayAmount(settDetailsE.getAmount().longValue());
            addGatherBillDetailF.setSupCpUnitId(concludeE.getCommunityId());
            addGatherBillDetailF.setSupCpUnitName(concludeE.getCommunityName());
            addGatherBillDetailF.setCpOrgId(concludeE.getOrgId());
            addGatherBillDetailF.setCpOrgName(concludeE.getOrgName());
            addGatherBillDetailF.setChargeItemId(Long.getLong(planConcludeE.getChargeItem()));
            addGatherBillDetailF.setChargeStartTime(planConcludeE.getPlannedCollectionTime().atStartOfDay());
            addGatherBillF.setAddGatherBillDetails(Arrays.asList(addGatherBillDetailF));
            financeFeignClient.gatherAddBatch(Arrays.asList(addGatherBillF));
        }



    }

    public void gatherAddBatch4Bond(CollectBondRelationBillE collectBondRelationBillE){
        RevisionBondCollectE bondE = revisionBondCollectService.getById(collectBondRelationBillE.getBondId());
        ContractIncomeConcludeE concludeE = contractIncomeConcludeService.selectById(bondE.getContractId());
        AddGatherBillF addGatherBillF = new AddGatherBillF();
        addGatherBillF.setOutBusId(concludeE.getId());
        addGatherBillF.setOutBusNo(concludeE.getContractNo());
        addGatherBillF.setSupCpUnitId(concludeE.getCommunityId());
        addGatherBillF.setSysSource(2);
        addGatherBillF.setPayeeId(concludeE.getOurPartyId());
        addGatherBillF.setPayeeName(concludeE.getOurParty());
        addGatherBillF.setPayerId(concludeE.getOppositeOneId());
        addGatherBillF.setPayerName(concludeE.getOppositeOne());
        addGatherBillF.setCurrency(concludeE.getCurrency());
        addGatherBillF.setDescription(bondE.getRemark());
        AddGatherBillDetailF addGatherBillDetailF = new AddGatherBillDetailF();
        addGatherBillDetailF.setPayeeId(concludeE.getOurPartyId());
        addGatherBillDetailF.setPayeeName(concludeE.getOurParty());
        addGatherBillDetailF.setPayerId(concludeE.getOppositeOneId());
        addGatherBillDetailF.setPayerName(concludeE.getOppositeOne());
        addGatherBillDetailF.setChargeEndTime(bondE.getGmtCreate());
        addGatherBillDetailF.setPayAmount(collectBondRelationBillE.getAmount().longValue());
        addGatherBillDetailF.setSupCpUnitId(concludeE.getCommunityId());
        addGatherBillDetailF.setSupCpUnitName(concludeE.getCommunityName());
        addGatherBillDetailF.setCpOrgId(concludeE.getOrgId());
        addGatherBillDetailF.setCpOrgName(concludeE.getOrgName());
        addGatherBillDetailF.setChargeItemName(collectBondRelationBillE.getChargeItem());
        addGatherBillDetailF.setChargeStartTime(collectBondRelationBillE.getDealDate().atStartOfDay());
        addGatherBillF.setAddGatherBillDetails(Arrays.asList(addGatherBillDetailF));
        financeFeignClient.gatherAddBatch(Arrays.asList(addGatherBillF));
    }

    public void payBillAdd4Settlement(ContractSettlementsFundF contractSettlementsFundF){
        ContractPaySettlementConcludeE settlementConcludeE = contractPaySettlementConcludeService.getById(contractSettlementsFundF.getSettlementId());
        if(settlementConcludeE != null){
            ContractPayConcludeE concludeE = contractPayConcludeService.selectById(settlementConcludeE.getContractId());
            ContractPayPlanConcludeE planConcludeE = contractPayPlanConcludeService.getById(settlementConcludeE.getContractPlanId());
            ContractPaySettDetailsE settDetailsE = contractPaySettDetailsService.getById(settlementConcludeE.getId());
            AddPayBillRF addPayBillRF = new AddPayBillRF();
            addPayBillRF.setOutBusId(concludeE.getId());
            addPayBillRF.setOutBusNo(concludeE.getContractNo());
            addPayBillRF.setSupCpUnitId(concludeE.getCommunityId());
            addPayBillRF.setSysSource(2);
            addPayBillRF.setPayerId(concludeE.getOurPartyId());
            addPayBillRF.setPayerName(concludeE.getOurParty());
            addPayBillRF.setPayeeId(concludeE.getOppositeOneId());
            addPayBillRF.setPayeeName(concludeE.getOppositeOne());
            addPayBillRF.setCurrency(concludeE.getCurrency());
            addPayBillRF.setStatutoryBodyName(concludeE.getOurParty());
            addPayBillRF.setDescription(settDetailsE.getRemark());
            addPayBillRF.setTaxAmount(planConcludeE.getTaxAmount().longValue());
            addPayBillRF.setTaxRate(new BigDecimal(planConcludeE.getTaxRate()));
            addPayBillRF.setTaxRateId(planConcludeE.getTaxRateId());
            addPayBillRF.setTotalAmount(planConcludeE.getSettlementAmount().longValue());
            addPayBillRF.setPayType(0);
            addPayBillRF.setPayWay(0);
            AddPayDetailRF addPayDetailRF = new AddPayDetailRF();
            addPayDetailRF.setSupCpUnitId(concludeE.getCommunityId());
            addPayDetailRF.setPayerId(concludeE.getOurPartyId());
            addPayDetailRF.setPayerName(concludeE.getOurParty());
            addPayDetailRF.setPayeeId(concludeE.getOppositeOneId());
            addPayDetailRF.setPayeeName(concludeE.getOppositeOne());
            addPayDetailRF.setPayerType(0);
            addPayDetailRF.setPayWay(0);
            addPayDetailRF.setSupCpUnitId("default");
            addPayDetailRF.setRecPayAmount(settDetailsE.getAmount().longValue());
            addPayDetailRF.setPayTime(settDetailsE.getGmtCreate());
            addPayDetailRF.setChargeEndTime(settDetailsE.getGmtCreate());
            addPayDetailRF.setChargeItemName(settDetailsE.getChargeItem());
            addPayDetailRF.setChargeStartTime(settDetailsE.getGmtCreate());
            financeFeignClient.addPayBill(addPayBillRF);
        }
    }

    public void payBillAdd4BondPay(PayBondRelationBillE payBondRelationBillE){
        RevisionBondPayE revisionBondPayE = revisionBondPayService.getById(payBondRelationBillE.getBondId());
        if(revisionBondPayE != null){
            ContractPayConcludeE concludeE = contractPayConcludeService.selectById(revisionBondPayE.getContractId());
            AddPayBillRF addPayBillRF = new AddPayBillRF();
            addPayBillRF.setOutBusId(concludeE.getId());
            addPayBillRF.setOutBusNo(concludeE.getContractNo());
            addPayBillRF.setSupCpUnitId(concludeE.getCommunityId());
            addPayBillRF.setSysSource(2);
            addPayBillRF.setPayerId(concludeE.getOurPartyId());
            addPayBillRF.setPayerName(concludeE.getOurParty());
            addPayBillRF.setPayeeId(concludeE.getOppositeOneId());
            addPayBillRF.setPayeeName(concludeE.getOppositeOne());
            addPayBillRF.setCurrency(concludeE.getCurrency());
            addPayBillRF.setStatutoryBodyName(concludeE.getOurParty());
            addPayBillRF.setDescription(revisionBondPayE.getRemark());
            addPayBillRF.setTotalAmount(payBondRelationBillE.getAmount().longValue());
            addPayBillRF.setPayType(0);
            addPayBillRF.setPayWay(0);
            AddPayDetailRF addPayDetailRF = new AddPayDetailRF();
            addPayDetailRF.setSupCpUnitId(concludeE.getCommunityId());
            addPayDetailRF.setPayerId(concludeE.getOurPartyId());
            addPayDetailRF.setPayerName(concludeE.getOurParty());
            addPayDetailRF.setPayeeId(concludeE.getOppositeOneId());
            addPayDetailRF.setPayeeName(concludeE.getOppositeOne());
            addPayDetailRF.setPayerType(0);
            addPayDetailRF.setPayWay(0);
            addPayDetailRF.setSupCpUnitId("default");
            addPayDetailRF.setRecPayAmount(payBondRelationBillE.getAmount().longValue());
            addPayDetailRF.setPayTime(payBondRelationBillE.getGmtCreate());
            addPayDetailRF.setChargeEndTime(payBondRelationBillE.getGmtCreate());
            addPayDetailRF.setChargeItemName(payBondRelationBillE.getChargeItem());
            addPayDetailRF.setChargeStartTime(payBondRelationBillE.getGmtCreate());
            financeFeignClient.addPayBill(addPayBillRF);
        }
    }

    public void receiptBatch4Bond(CollectBondRelationBillE collectBondRelationBillE){
        RevisionBondCollectE bondE = revisionBondCollectService.getById(collectBondRelationBillE.getBondId());
        ContractIncomeConcludeE concludeE = contractIncomeConcludeService.selectById(bondE.getContractId());
        ReceiptBatchRf receiptBatchRf = new ReceiptBatchRf();
        receiptBatchRf.setBillIds(Arrays.asList(Long.getLong(bondE.getId())));
        receiptBatchRf.setBillType(2);
        receiptBatchRf.setInvSource(1);
        receiptBatchRf.setPaymentTime(collectBondRelationBillE.getDealDate().atStartOfDay());
        receiptBatchRf.setPriceTaxAmount(collectBondRelationBillE.getAmount().longValue());
        receiptBatchRf.setRemark(collectBondRelationBillE.getRemark());
        receiptBatchRf.setType(5);
        financeFeignClient.receiptBatch(receiptBatchRf);
    }

    public void invoiceBatch4IncomeSettlement(ContractSettlementsBillF contractSettlementsBillF){
        ContractSettlementsBillE billE = contractSettlementsBillMapper.selectById(contractSettlementsBillF.getId());
//        ContractIncomeConcludeE concludeE = contractIncomeConcludeService.getById(contractSettlementsBillF.getContractId());
        InvoiceBatchRf invoiceBatchRf = new InvoiceBatchRf();
        invoiceBatchRf.setBillIds(Arrays.asList(Long.getLong(contractSettlementsBillF.getSettlementId())));
        invoiceBatchRf.setBillType("0".equals(contractSettlementsBillF.getType())?1:4);
        invoiceBatchRf.setBuyerName(contractSettlementsBillF.getTitle());
        invoiceBatchRf.setBuyerTaxNum(contractSettlementsBillF.getCreditCode());
        invoiceBatchRf.setClerk(billE.getCreatorName());
        invoiceBatchRf.setInvSource(1);
        invoiceBatchRf.setInvoiceCode(billE.getBillCode());
        invoiceBatchRf.setInvoiceNo(billE.getBillNum());
        invoiceBatchRf.setInvoiceType(1);
        invoiceBatchRf.setInvoiceType(Integer.valueOf(billE.getBillType()));
        invoiceBatchRf.setPriceTaxAmount(billE.getAmount().longValue());
        InvoiceBillAmountRf invoiceBillAmountRf = new InvoiceBillAmountRf();
        invoiceBillAmountRf.setBillId(Long.getLong(billE.getSettlementId()));
        invoiceBillAmountRf.setInvoiceAmount(billE.getAmount().intValue());
        invoiceBatchRf.setInvoiceBillAmounts(Arrays.asList(invoiceBillAmountRf));
        financeFeignClient.invoiceBatch(invoiceBatchRf);
    }


    /**
     * /receivable/delete/batch 财务中台收入账单批量删除
     * @param billIds
     */
    public void dealDelFinanceIncome(List<Long> billIds) {
        try {
            if (CollectionUtils.isNotEmpty(billIds)) {
                DeleteReceivableBillRf deleteReceivableBillRf = new DeleteReceivableBillRf();
                deleteReceivableBillRf.setBillIds(billIds);
                deleteReceivableBillRf.setSupCpUnitId("default");
                log.info("请求财务中台账单批量删除参数========" + JSONObject.toJSONString(deleteReceivableBillRf));

                ReceivableDeleteRv receivableDeleteRv = financeFeignClient.receivabledelete(deleteReceivableBillRf);
                log.info("请求财务中台账单批量删除响应========" + JSONObject.toJSONString(receivableDeleteRv));

                if (null == receivableDeleteRv || receivableDeleteRv.getFailCount() > 0 || receivableDeleteRv.getSuccessCount() != billIds.size()) {
                    throw BizException.throw400("/receivable/delete/batch财务中台账单删除失败");
                }
            }
        } catch (Exception e) {
            log.info("财务中台账单批量删除失败" + e);
            throw BizException.throw400("/receivable/delete/batch财务中台账单删除失败");
        }
    }

    /**
     * /payable/delete/batch 财务中台成本账单批量删除
     * @param billIds
     */
    public void dealDelFinancePay(List<Long> billIds) {
        try {
            if (CollectionUtils.isNotEmpty(billIds)) {
                DeleteReceivableBillRf deleteReceivableBillRf = new DeleteReceivableBillRf();
                deleteReceivableBillRf.setBillIds(billIds);
                deleteReceivableBillRf.setSupCpUnitId("default");
                log.info("请求财务中台账单批量删除参数========" + JSONObject.toJSONString(deleteReceivableBillRf));

                ReceivableDeleteRv receivableDeleteRv = financeFeignClient.payabledelete(deleteReceivableBillRf);
                log.info("请求财务中台账单批量删除响应========" + JSONObject.toJSONString(receivableDeleteRv));

                if (null == receivableDeleteRv || receivableDeleteRv.getFailCount() > 0 || receivableDeleteRv.getSuccessCount() != billIds.size()) {
                    throw BizException.throw400("/receivable/delete/batch财务中台账单删除失败");
                }
            }
        } catch (Exception e) {
            log.info("财务中台账单批量删除失败" + e);
            throw BizException.throw400("/receivable/delete/batch财务中台账单删除失败");
        }
    }

    /**
     * /receivable/add/batch 财务中台收入账单批量新增
     * @param incomePlanConcludeES
     */
    public List<ReceivableBillDetailRv> dealAddFinanceIncome(List<ContractIncomeConcludeProfitLossE> incomePlanConcludeES) {
        try {
            ContractIncomeConcludeE concludeE = contractIncomeConcludeService.selectById(incomePlanConcludeES.get(0).getContractId());
            if(null != concludeE) {
                List<AddReceivableBillRf> receivableBillRf = new ArrayList<>();
                incomePlanConcludeES.forEach(concludv -> {
                    AddReceivableBillRf addBillRf = new AddReceivableBillRf();
                    //APPid不用传，去除必填
//                    addBillRf.setAppId();
                    //计费方式默认传8- 固定金额
                    addBillRf.setBillMethod(8);
                    //费项id

                    try {
                        if(StringUtils.isNotEmpty(concludv.getChargeItemId())) {
                            List<Long> list =(JSONObject.parseArray(concludv.getChargeItemId(), Long.class));
                            if(CollectionUtils.isNotEmpty(list)) {
                                addBillRf.setChargeItemId(list.get(list.size()-1));
                            }
                        }
                    } catch (Exception e) {
                        log.info("财务中台账单批量新增费项id转换失败" + e);
                    }

                    //费项名称
                    addBillRf.setChargeItemName(concludv.getChargeItem());
                    //账单说明
                    addBillRf.setDescription("账单说明");
                    //账单结束时间
                    addBillRf.setEndTime(LocalDateTime.now());

                    //发票类型
//                    addBillRf.setInvoiceType();

                    //账单来源
                    addBillRf.setSource("合同损益收入");

                    //账单开始时间
                    addBillRf.setStartTime(LocalDateTime.now());

                    //账单金额
                    addBillRf.setTotalAmount(concludv.getPlannedCollectionAmount().multiply(new BigDecimal(100)).longValue());


                    //项目ID
                    addBillRf.setCommunityId(StringUtils.isNotEmpty(concludeE.getCommunityId()) ? concludeE.getCommunityId() : "default");

                    //项目名称
                    addBillRf.setCommunityName(StringUtils.isNotEmpty(concludeE.getCommunityName()) ? concludeE.getCommunityName() : "合同");

                    //上级收费单元
                    addBillRf.setSupCpUnitId(StringUtils.isNotEmpty(concludeE.getCommunityId()) ? concludeE.getCommunityId() : "default");

                    //合同id
                    addBillRf.setExtField6(concludeE.getId());


                    receivableBillRf.add(addBillRf);
                });
                log.info("请求财务中台收入账单批量新增入参========" + JSONObject.toJSONString(receivableBillRf));
                List<ReceivableBillDetailRv> response =  financeFeignClient.addBatch(receivableBillRf);
                log.info("请求财务中台收入账单批量新增响应========" + JSONObject.toJSONString(response));
                return response;
            }

        } catch (Exception e) {
            log.info("财务中台账单批量新增失败" + e);
            throw BizException.throw400("/receivable/add/batch财务中台账单批量新增失败");
        }
        return new ArrayList<>();
    }


    /**
     * /payable/add/batch 财务中台成本账单批量新增
     * @param payConcludeProfitLossES
     */
    public List<ReceivableBillDetailRv> dealAddFinancePay(List<ContractPayConcludeProfitLossE> payConcludeProfitLossES) {
        try {
            ContractPayConcludeE concludeE = contractPayConcludeService.selectById(payConcludeProfitLossES.get(0).getContractId());
            if(null != concludeE) {
                List<AddReceivableBillRf> receivableBillRf = new ArrayList<>();
                payConcludeProfitLossES.forEach(concludv -> {
                    AddReceivableBillRf addBillRf = new AddReceivableBillRf();
                    //APPid不用传，去除必填
//                    addBillRf.setAppId();
                    //计费方式默认传8- 固定金额
                    addBillRf.setBillMethod(8);
                    //费项id

                    try {
                        if(StringUtils.isNotEmpty(concludv.getChargeItemId())) {
                            List<Long> list =(JSONObject.parseArray(concludv.getChargeItemId(), Long.class));
                            if(CollectionUtils.isNotEmpty(list)) {
                                addBillRf.setChargeItemId(list.get(list.size()-1));
                            }
                        }
                    } catch (Exception e) {
                        log.info("财务中台账单批量新增费项id转换失败" + e);
                    }

                    //费项名称
                    addBillRf.setChargeItemName(concludv.getChargeItem());
                    //账单说明
                    addBillRf.setDescription("账单说明");
                    //账单结束时间
                    addBillRf.setEndTime(LocalDateTime.now());

                    //发票类型
//                    addBillRf.setInvoiceType();

                    //账单来源
                    addBillRf.setSource("合同损益成本");

                    //账单开始时间
                    addBillRf.setStartTime(LocalDateTime.now());

                    //账单金额
                    addBillRf.setTotalAmount(concludv.getPlannedCollectionAmount().longValue());


                    //项目ID
                    addBillRf.setCommunityId(StringUtils.isNotEmpty(concludeE.getCommunityId()) ? concludeE.getCommunityId() : "default");

                    //项目名称
                    addBillRf.setCommunityName(StringUtils.isNotEmpty(concludeE.getCommunityName()) ? concludeE.getCommunityName() : "合同");

                    //上级收费单元
                    addBillRf.setSupCpUnitId(StringUtils.isNotEmpty(concludeE.getCommunityId()) ? concludeE.getCommunityId() : "default");


                    addBillRf.setAppName("合同");
                    addBillRf.setAppId("default");

                    //合同id
                    addBillRf.setExtField6(concludeE.getId());


                    receivableBillRf.add(addBillRf);
                });
                log.info("请求财务中台成本账单批量新增入参========" + JSONObject.toJSONString(receivableBillRf));
                List<ReceivableBillDetailRv> response =  financeFeignClient.addPayableBatch(receivableBillRf);
                log.info("请求财务中台成本账单批量新增响应========" + JSONObject.toJSONString(response));
                return response;
            }

        } catch (Exception e) {
            log.info("财务中台账单批量新增失败" + e);
            throw BizException.throw400("/payable/add/batch财务中台账单批量新增失败");
        }
        return new ArrayList<>();
    }


}
