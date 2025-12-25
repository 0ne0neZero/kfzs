package com.wishare.finance.domains.reconciliation;

import com.wishare.finance.domains.bill.aggregate.data.EnvData;
import com.wishare.finance.domains.bill.support.BillSerialNumberFactory;
import com.wishare.finance.domains.reconciliation.entity.ReconciliationBillOBV;
import com.wishare.finance.domains.reconciliation.entity.ReconciliationDetailE;
import com.wishare.finance.domains.reconciliation.entity.ReconciliationE;
import com.wishare.finance.domains.reconciliation.entity.ReconciliationYinlianE;
import com.wishare.finance.domains.reconciliation.enums.ReconcileModeEnum;
import com.wishare.finance.domains.reconciliation.enums.ReconcileResultEnum;
import com.wishare.finance.domains.reconciliation.enums.ReconcileRunStateEnum;
import com.wishare.finance.infrastructure.conts.EnvConst;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import com.wishare.finance.infrastructure.utils.AmountUtils;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author xujian
 * @date 2023/2/20
 * @Description:
 */
@Getter
@Setter
public class ReconciliationClearA extends ReconciliationE {

    /**
     * 银联对账列表
     */
    private List<ReconciliationYinlianE> reconciliationYinlianEList;

    /**
     * 我方需要对账的账单列表
     */
    private List<ReconciliationBillOBV> reconciliationBillOBVList;

    /**
     * 对账详情
     */
    private List<ReconciliationDetailE> reconciliationDetails;


    /**
     * 执行对账
     */
    public void reconcile() {
        //构造以流水为维度的记录
        Map<String, List<ReconciliationYinlianE>> reconciliationYinlianEMap;
        if (EnvConst.YUANYANG.equals(EnvData.config)){
            reconciliationYinlianEMap = reconciliationYinlianEList.stream().collect(Collectors.groupingBy(ReconciliationYinlianE::getSearchReferenceNo));
        }else {
            reconciliationYinlianEMap = reconciliationYinlianEList.stream().collect(Collectors.groupingBy(ReconciliationYinlianE::getSeqId));
        }
        for (ReconciliationBillOBV reconciliationBillOBV : reconciliationBillOBVList) {
            setActualTotal(reconciliationBillOBV.getActualAmount() + getActualTotal());
            List<ReconciliationYinlianE> reconciliationYinlianES;
            if (EnvConst.YUANYANG.equals(EnvData.config)){
                reconciliationYinlianES = reconciliationYinlianEMap.get(reconciliationBillOBV.getBankFlowNo());
            }else {
                reconciliationYinlianES  = reconciliationYinlianEMap.get(reconciliationBillOBV.getTradeNo());
            }
            ReconciliationYinlianE reconciliationYinlianE = null;
            if (EnvConst.YUANYANG.equals(EnvData.config)){
                if (CollectionUtils.isNotEmpty(reconciliationYinlianES)) {
                    reconciliationYinlianE = reconciliationYinlianEMap.get(reconciliationBillOBV.getBankFlowNo()).get(0);
                    setFlowClaimTotal(getFlowClaimTotal() + AmountUtils.toLong(reconciliationYinlianE.getTradeAmount()));
                    setCommission(getCommission() + AmountUtils.toLong(reconciliationYinlianE.getCommission()));
                }
            } else {
                if (CollectionUtils.isNotEmpty(reconciliationYinlianES)) {
                    reconciliationYinlianE = reconciliationYinlianEMap.get(reconciliationBillOBV.getTradeNo()).get(0);
                    setFlowClaimTotal(getFlowClaimTotal() + AmountUtils.toLong(reconciliationYinlianE.getTradeAmount()));
                    setCommission(getCommission() + AmountUtils.toLong(reconciliationYinlianE.getCommission()));
                }
            }
            //添加对账详情
            ReconciliationDetailE detailE = new ReconciliationDetailE();
            detailE.setReconcileMode(ReconcileModeEnum.商户清分对账.getCode());
            detailE.setReconciliationId(this.getId());
            detailE.setStatutoryBodyId(reconciliationBillOBV.getStatutoryBodyId());
            detailE.setStatutoryBodyName(reconciliationBillOBV.getStatutoryBodyName());
            detailE.setCostCenterId(reconciliationBillOBV.getCostCenterId());
            detailE.setCostCenterName(reconciliationBillOBV.getCostCenterName());
            detailE.setChargeItemId(reconciliationBillOBV.getChargeItemId());
            detailE.setChargeItemName(reconciliationBillOBV.getChargeItemName());
            detailE.setBillId(reconciliationBillOBV.getId());
            detailE.setBillNo(reconciliationBillOBV.getBillNo());
            detailE.setBillType(reconciliationBillOBV.getBillType());
            detailE.setReceivableAmount(reconciliationBillOBV.getNoReductionAmount());
            detailE.setActualAmount(reconciliationBillOBV.getReceivableAmount());
            detailE.setSysSource(reconciliationBillOBV.getSysSource());
            detailE.setTradeTime(reconciliationBillOBV.getTradeTime());
            detailE.setPayWay(reconciliationBillOBV.getPayWay());
            detailE.setPayChannel(reconciliationBillOBV.getPayChannel());
            detailE.setReconcileTime(this.getReconcileTime());
            if (reconciliationYinlianE != null) {
                detailE.setCommission(handleComission(reconciliationYinlianE.getCommission()));
                detailE.setChannelPayWay(reconciliationYinlianE.getTradeChannel());
                detailE.setChannelMid(reconciliationYinlianE.getMid());
                if (EnvConst.YUANYANG.equals(EnvData.config)){
                    detailE.setChannelSeqId(reconciliationYinlianE.getSearchReferenceNo());
                }else {
                    detailE.setChannelSeqId(reconciliationYinlianE.getSeqId());
                }
                detailE.setChannelReconState(null);
                detailE.setBankSeqId(null);
                //带两位小数的金额
                detailE.setChannelTradeAmount(AmountUtils.toLong(reconciliationYinlianE.getTradeAmount()));
            }
            detailE.doReconcileResult();
            if (ReconcileResultEnum.已核对.equalsByCode(detailE.getResult())){
                setBalanceCount(getBalanceCount() + 1L);
            }
            setBillCount(getBillCount() + 1L);
            addReconciliationDetail(detailE);
        }
    }

    /**
     *
     * @param commission
     * @return
     */
    private Long handleComission(String commission) {
        if (StringUtils.isNotBlank(commission)) {
            return new BigDecimal(commission).multiply(new BigDecimal(100)).longValue();
        }
        return 0L;
    }

    /**
     * 添加对账明细
     * @param reconciliationDetail
     */
    public void addReconciliationDetail(ReconciliationDetailE reconciliationDetail){
        if (Objects.isNull(reconciliationDetails)){
            reconciliationDetails = new ArrayList<>();
        }
        reconciliationDetails.add(reconciliationDetail);
    }

    /**
     * 执行对账结果
     *
     * @param actualAmountBigDecimal
     * @param tradeAmountBigDecimal
     * @return
     */
    private Integer doReconcileResult(BigDecimal actualAmountBigDecimal, BigDecimal tradeAmountBigDecimal) {
        return tradeAmountBigDecimal.compareTo(actualAmountBigDecimal) == 0 ? ReconcileResultEnum.已核对.getCode() : ReconcileResultEnum.核对失败.getCode();
    }

    public void finish() {
        if (getBillCount().equals(getBalanceCount())){
            setResult(ReconcileResultEnum.已核对.getCode());
        }else if (getBalanceCount() > 0){
            setResult(ReconcileResultEnum.部分核对.getCode());
        } else if (getBalanceCount().equals(0)){
            setResult(ReconcileResultEnum.核对失败.getCode());
        } else {
            setResult(ReconcileResultEnum.核对失败.getCode());
        }
        setState(ReconcileRunStateEnum.已完成.getCode());
    }

    public void error() {
        setResult(ReconcileResultEnum.核对失败.getCode());
        setState(ReconcileRunStateEnum.已完成.getCode());
    }


}
