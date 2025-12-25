package com.wishare.finance.domains.reconciliation;

import com.wishare.finance.domains.reconciliation.entity.ReconciliationBillOBV;
import com.wishare.finance.domains.reconciliation.entity.ReconciliationBillRefundOBV;
import com.wishare.finance.domains.reconciliation.entity.ReconciliationDetailE;
import com.wishare.finance.domains.reconciliation.entity.ReconciliationE;
import com.wishare.finance.domains.reconciliation.entity.ReconciliationFlowDetailOBV;
import com.wishare.finance.domains.reconciliation.entity.ReconciliationInvoiceDetailOBV;
import com.wishare.finance.domains.reconciliation.entity.ReconciliationSettleDetailOBV;
import com.wishare.finance.domains.reconciliation.enums.ReconcileResultEnum;
import com.wishare.finance.domains.reconciliation.enums.ReconcileRunStateEnum;
import com.wishare.finance.domains.reconciliation.service.ReconciliationDomainService;
import com.wishare.starter.Global;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 对账单聚合
 *
 * @Author dxclay
 * @Date 2022/10/26
 * @Version 1.0
 */
@Getter
@Setter
public class ReconciliationA extends ReconciliationE {

    /**
     * 账单明细
     */
    private List<ReconciliationBillOBV> bills;

    /**
     * 结算详细
     */
    //private List<ReconciliationSettleDetailOBV> settleDetails;

    /**
     * 退款详情
     */
    private List<ReconciliationBillRefundOBV> refundDetails;

    /**
     * 流水详情
     */
    private List<ReconciliationFlowDetailOBV> flowDetails;

    /**
     * 发票明细
     */
    private List<ReconciliationInvoiceDetailOBV> invoiceDetails;


    /**
     * 对账详情
     */
    private List<ReconciliationDetailE> reconciliationDetails;

    public ReconciliationA() {
    }

    public ReconciliationA(List<ReconciliationBillOBV> bills, List<ReconciliationSettleDetailOBV> settleDetails,
                           List<ReconciliationFlowDetailOBV> flowDetails, List<ReconciliationInvoiceDetailOBV> invoiceDetails) {
        this.bills = bills;
        //this.settleDetails = settleDetails;
        this.flowDetails = flowDetails;
        this.invoiceDetails = invoiceDetails;
    }



    /**
     * 对账
     */
    public void reconcile() {

        if (CollectionUtils.isEmpty(bills)) return;
        setBillCount(getBillCount() + bills.size());
        //Map<Long, List<ReconciliationSettleDetailOBV>> settleMap = getSettleMap();
        Map<Long, List<ReconciliationInvoiceDetailOBV>> invoiceMap = getInvoiceMap();
        Map<Long, List<ReconciliationFlowDetailOBV>> flowMap = getFlowMap();
        Map<Long, List<ReconciliationBillRefundOBV>> refundMap = getRefundMap();
        List<ReconciliationSettleDetailOBV> settleDetailOBVS;
        List<ReconciliationInvoiceDetailOBV> invoiceDetailOBVS;
        List<ReconciliationFlowDetailOBV> flowDetailOBVS;
        List<ReconciliationBillRefundOBV> refundOBVS;
        for (ReconciliationBillOBV bill : bills) {
            setActualTotal(bill.getReceivableAmount() + getActualTotal());
            ReconciliationDetailE reconciliationDetailE = Global.mapperFacade.map(bill, ReconciliationDetailE.class);
            reconciliationDetailE.setId(null);
            reconciliationDetailE.setBillId(bill.getId());
            reconciliationDetailE.setCostCenterId(bill.getCostCenterId() == null ? "" : bill.getCostCenterId());
            reconciliationDetailE.setReconciliationId(getId());
            reconciliationDetailE.setReconcileTime(getReconcileTime());
            reconciliationDetailE.setSysSource(bill.getSysSource());
            reconciliationDetailE.setRefundAmount(bill.getRefundAmount());
            reconciliationDetailE.setSysSource(bill.getSysSource());
            reconciliationDetailE.setReconcileMode(getReconcileMode());
            reconciliationDetailE.setCarriedAmount(bill.getCarriedAmount());

            //设置结算数据
            reconciliationDetailE.setActualAmount(bill.getReceivableAmount());

            //退款信息
            refundOBVS = refundMap.get(bill.getId());
            List<Long> refundIds = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(refundOBVS)) {
                long actRefundAmount = 0;
                for (ReconciliationBillRefundOBV refundOBV : refundOBVS) {
                    refundIds.add(refundOBV.getId());
                    actRefundAmount += refundOBV.getRefundAmount();
                }
                reconciliationDetailE.setActRefundAmount(actRefundAmount);
                reconciliationDetailE.setRefundIds(refundIds);
            }

            //设置票据数据
            invoiceDetailOBVS = invoiceMap.get(bill.getId());
            List<Long> invoiceIds = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(invoiceDetailOBVS)) {
                long mInvoiceTotal = 0;
                long mReceiptTotal = 0;
                for (ReconciliationInvoiceDetailOBV invoiceDetailOBV : invoiceDetailOBVS) {
                    invoiceIds.add(invoiceDetailOBV.getId());
                    mInvoiceTotal += invoiceDetailOBV.getInvoiceAmount();
                    mReceiptTotal += invoiceDetailOBV.getReceiptAmount();
                }
                reconciliationDetailE.setInvoiceAmount(mInvoiceTotal);
                reconciliationDetailE.setReceiptAmount(mReceiptTotal);
                reconciliationDetailE.setInvoiceIds(invoiceIds);
                setInvoiceTotal(mInvoiceTotal + getInvoiceTotal());
            }

            //设置流水数据
            // 如果发票数据为空就时收款单认领 取收款单数据
            if (null != invoiceIds && invoiceIds.size() > 0) {
                flowDetailOBVS = getFlowDetailOBVS(invoiceIds, flowMap);
            } else {
                List<Long> billIds = new ArrayList<>();
                billIds.add(bill.getId());
                flowDetailOBVS = getFlowDetailOBVS(billIds, flowMap);
            }
            if (CollectionUtils.isNotEmpty(flowDetailOBVS)) {
                long flowTotal = 0;
                List<Long> flowIds = new ArrayList<>();
                for (ReconciliationFlowDetailOBV flowDetailOBV : flowDetailOBVS) {
                    flowIds.add(flowDetailOBV.getId());
                    flowTotal += flowDetailOBV.getFlowAmount(); //合计认领金额
                }
                reconciliationDetailE.setFlowIds(flowIds);
                reconciliationDetailE.setFlowClaimAmount(flowTotal);
                setFlowClaimTotal(getFlowClaimTotal() + flowTotal);
            }

            reconciliationDetailE.setResult(ReconcileResultEnum.已核对.getCode());
            setBalanceCount(getBalanceCount() + 1);
//            reconciliationDetailE.doReconcileResult(); //执行对账结果
//            if (ReconcileResultEnum.已核对.equalsByCode(reconciliationDetailE.getResult())){
//                setBalanceCount(getBalanceCount() + 1);
//                setResult(ReconcileResultEnum.已核对.getCode());
//            }else {
//                if (getBalanceCount() > 0){
//                    setResult(ReconcileResultEnum.部分核对.getCode());
//                }else {
//                    setResult(ReconcileResultEnum.核对失败.getCode());
//                }
//            }
            //添加对账明细
            reconciliationDetailE.setReceivableAmount(bill.getNoReductionAmount());
            addReconciliationDetail(reconciliationDetailE);
        }


//        if (CollectionUtils.isEmpty(flowDetails)) {
//            return;
//        }
//        setBillCount(getBillCount() + flowDetails.size());
//
//        for (ReconciliationFlowDetailOBV flowDetail : flowDetails) {
//            setFlowClaimTotal(getFlowClaimTotal() + flowDetail.getFlowAmount());
//            Map<Long, List<ReconciliationBillOBV>> billMap = getBillMap();
//            //  账单认领 获取账单
//            if (flowDetail.getClaimType().equals("2")){
//                List<ReconciliationBillOBV> reconciliationBillOBVS = billMap.get(flowDetail.getInvoiceId());
//                ReconciliationDetailE reconciliationDetailE = Global.mapperFacade.map(reconciliationBillOBVS.get(0), ReconciliationDetailE.class);
//                reconciliationDetailE.setId(null);
//                reconciliationDetailE.setBillId(flowDetail.getInvoiceId());
//                reconciliationDetailE.setReconciliationId(getId());
//                reconciliationDetailE.setReconcileTime(getReconcileTime());
//                reconciliationDetailE.setSysSource(reconciliationBillOBVS.get(0).getSysSource());
//                reconciliationDetailE.setRefundAmount(reconciliationBillOBVS.get(0).getRefundAmount());
//                reconciliationDetailE.setSysSource(reconciliationBillOBVS.get(0).getSysSource());
//                reconciliationDetailE.setReconcileMode(getReconcileMode());
//                reconciliationDetailE.setCarriedAmount(reconciliationBillOBVS.get(0).getCarriedAmount());
//                reconciliationDetailE.setResult(ReconcileResultEnum.已核对.getCode());
//                setBalanceCount(getBalanceCount() + 1);
//                setResult(ReconcileResultEnum.已核对.getCode());
//                // 添加流水信息
//                List<Long> flowIds = new ArrayList<>();
//                flowIds.add(flowDetail.getId());
//                reconciliationDetailE.setFlowIds(flowIds);
//                reconciliationDetailE.setFlowClaimAmount(flowDetail.getFlowAmount());
//
//                //添加对账明细
//                addReconciliationDetail(reconciliationDetailE);
//
//            } else {
//                // 发票认领
//                // 发票id
//                Long invoiceId = flowDetail.getInvoiceId();
//                Map<Long, List<ReconciliationBillOBV>> billInvoiceMap = getBillInvoiceMap();
//                List<ReconciliationBillOBV> reconciliationBillOBVS = billInvoiceMap.get(invoiceId);
//                ReconciliationDetailE reconciliationDetailE = Global.mapperFacade.map(reconciliationBillOBVS.get(0), ReconciliationDetailE.class);
//                reconciliationDetailE.setBillId(reconciliationBillOBVS.get(0).getId());
//                reconciliationDetailE.setId(null);
//                reconciliationDetailE.setReconciliationId(getId());
//                reconciliationDetailE.setReconcileTime(getReconcileTime());
//                reconciliationDetailE.setSysSource(reconciliationBillOBVS.get(0).getSysSource());
//                reconciliationDetailE.setRefundAmount(reconciliationBillOBVS.get(0).getRefundAmount());
//                reconciliationDetailE.setSysSource(reconciliationBillOBVS.get(0).getSysSource());
//                reconciliationDetailE.setReconcileMode(getReconcileMode());
//                reconciliationDetailE.setCarriedAmount(reconciliationBillOBVS.get(0).getCarriedAmount());
//                reconciliationDetailE.setResult(ReconcileResultEnum.已核对.getCode());
//                setBalanceCount(getBalanceCount() + 1);
//                setResult(ReconcileResultEnum.已核对.getCode());
//
//                // 添加流水信息
//                List<Long> flowIds = new ArrayList<>();
//                flowIds.add(flowDetail.getId());
//                reconciliationDetailE.setFlowIds(flowIds);
//                reconciliationDetailE.setFlowClaimAmount(flowDetail.getFlowAmount());
//                // 发票Id 查询发票信息
//
//                // 添加发票信息
//                List<Long> invoiceIds = new ArrayList<>();
//                Map<Long, List<ReconciliationInvoiceDetailOBV>> invoiceMap = getInvoiceMap();
//                List<ReconciliationInvoiceDetailOBV> reconciliationInvoiceDetailOBVS = invoiceMap.get(invoiceId);
//                if (CollectionUtils.isNotEmpty(reconciliationInvoiceDetailOBVS)){
//                    long mInvoiceTotal = 0;
//                    long mReceiptTotal = 0;
//                    for (ReconciliationInvoiceDetailOBV invoiceDetailOBV : reconciliationInvoiceDetailOBVS) {
//                        invoiceIds.add(invoiceDetailOBV.getId());
//                        mInvoiceTotal += invoiceDetailOBV.getInvoiceAmount();
//                        mReceiptTotal += invoiceDetailOBV.getReceiptAmount();
//                    }
//                    reconciliationDetailE.setInvoiceAmount(mInvoiceTotal);
//                    reconciliationDetailE.setReceiptAmount(mReceiptTotal);
//                    reconciliationDetailE.setInvoiceIds(invoiceIds);
//                    setInvoiceTotal(mInvoiceTotal + getInvoiceTotal());
//                }
//
//                //添加对账明细
//                addReconciliationDetail(reconciliationDetailE);
//            }
//
//        }


    }

    /**
     * 添加对账明细
     *
     * @param reconciliationDetail
     */
    public void addReconciliationDetail(ReconciliationDetailE reconciliationDetail) {
        if (Objects.isNull(reconciliationDetails)) {
            reconciliationDetails = new ArrayList<>();
        }
        reconciliationDetails.add(reconciliationDetail);
    }

    /**
     * 账单id为维度，获取结算map
     * @return
     */
    //private Map<Long, List<ReconciliationSettleDetailOBV>> getSettleMap(){
    //    return CollectionUtils.isNotEmpty(settleDetails) ? settleDetails.stream().collect(
    //            Collectors.groupingBy(ReconciliationSettleDetailOBV::getBillId)) : new HashMap<>();
    //}

    /**
     * 账单id为维度，获取开票map
     *
     * @return
     */
    private Map<Long, List<ReconciliationInvoiceDetailOBV>> getInvoiceMap() {
        return CollectionUtils.isNotEmpty(invoiceDetails) ? invoiceDetails.stream().collect(
                Collectors.groupingBy(ReconciliationInvoiceDetailOBV::getBillId)) : new HashMap<>();
    }


    /**
     * 账单id为维度，获取开票map
     *
     * @return
     */
    private Map<Long, List<ReconciliationFlowDetailOBV>> getFlowMap() {
        return CollectionUtils.isNotEmpty(flowDetails) ? flowDetails.stream().collect(
                Collectors.groupingBy(ReconciliationFlowDetailOBV::getInvoiceId)) : new HashMap<>();
    }

    /**
     * 账单id为维度，获取退款map
     *
     * @return
     */
    private Map<Long, List<ReconciliationBillRefundOBV>> getRefundMap() {
        return CollectionUtils.isNotEmpty(refundDetails) ? refundDetails.stream().collect(
                Collectors.groupingBy(ReconciliationBillRefundOBV::getBillId)) : new HashMap<>();
    }

    public Map<Long, List<ReconciliationBillOBV>> getBillMap() {
        return CollectionUtils.isNotEmpty(bills) ? bills.stream().collect(
                Collectors.groupingBy(ReconciliationBillOBV::getId)) : new HashMap<>();
    }

    /**
     * 根据账单发票id分组
     *
     * @return
     */
    public Map<Long, List<ReconciliationBillOBV>> getBillInvoiceMap() {
        return CollectionUtils.isNotEmpty(bills) ? bills.stream().collect(
                Collectors.groupingBy(ReconciliationBillOBV::getInvoiceId)) : new HashMap<>();
    }

    /**
     * @param billId
     * @param settleMap
     * @return
     */
    private List<Long> getSettleIdsAndRemove(Long billId, Map<Long, List<ReconciliationSettleDetailOBV>> settleMap) {
        List<ReconciliationSettleDetailOBV> reconciliationSettleDetailOBVS = settleMap.get(billId);
        return CollectionUtils.isNotEmpty(reconciliationSettleDetailOBVS) ? reconciliationSettleDetailOBVS.stream().map(
                ReconciliationSettleDetailOBV::getBillId).collect(Collectors.toList()) : new ArrayList<>();
    }

    /**
     * 根据票据列表获取流水信息
     *
     * @param invoiceIds
     * @param flowMap
     * @return
     */
    private List<ReconciliationFlowDetailOBV> getFlowDetailOBVS(List<Long> invoiceIds, Map<Long, List<ReconciliationFlowDetailOBV>> flowMap) {
        List<ReconciliationFlowDetailOBV> flowDetailOBVS = new ArrayList<>();
        for (Long invoiceId : invoiceIds) {
            List<ReconciliationFlowDetailOBV> flowDetails = flowMap.get(invoiceId);
            if (CollectionUtils.isNotEmpty(flowDetails)) {
                flowDetailOBVS.addAll(flowDetails);
            }
        }
        return flowDetailOBVS;
    }


    /**
     * 清除对账信息
     */
    public void clear() {
        bills = null;
        refundDetails = null;
        flowDetails = null;
        invoiceDetails = null;
        reconciliationDetails = null;
    }

    public void finish() {
        if (getBillCount().equals(getBalanceCount())) {
            setResult(ReconcileResultEnum.已核对.getCode());
        } else if (getBalanceCount() > 0) {
            setResult(ReconcileResultEnum.部分核对.getCode());
        } else if (getBalanceCount().equals(0)) {
            setResult(ReconcileResultEnum.核对失败.getCode());
        }
        setState(ReconcileRunStateEnum.已完成.getCode());
    }

    public void error() {
        setResult(ReconcileResultEnum.核对失败.getCode());
        setState(ReconcileRunStateEnum.已完成.getCode());
    }
}
