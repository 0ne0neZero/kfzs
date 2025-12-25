package com.wishare.finance.domains.reconciliation.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.apps.model.reconciliation.vo.ReconciliationClearStatisticsV;
import com.wishare.finance.apps.model.reconciliation.vo.ReconciliationStatisticsV;
import com.wishare.finance.domains.bill.command.ReconcileQuery;
import com.wishare.finance.domains.bill.command.ReconcileGatherRefundQuery;
import com.wishare.finance.domains.bill.command.ReconcileRefundQuery;
import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import com.wishare.finance.domains.bill.repository.BillReconciliationRepository;
import com.wishare.finance.domains.invoicereceipt.command.invocing.GatherInvoiceQuery;
import com.wishare.finance.domains.invoicereceipt.command.invocing.PayInvoiceQuery;
import com.wishare.finance.domains.invoicereceipt.command.invocing.ReceivableInvoiceQuery;
import com.wishare.finance.domains.reconciliation.command.FlowsInvoiceQuery;
import com.wishare.finance.domains.reconciliation.dto.FlowReconciliationDetailDto;
import com.wishare.finance.domains.reconciliation.entity.*;
import com.wishare.finance.domains.reconciliation.enums.ReconcileModeEnum;
import com.wishare.finance.domains.reconciliation.facade.ReconciliationBillFacade;
import com.wishare.finance.domains.reconciliation.repository.FlowDetailRepository;
import com.wishare.finance.domains.reconciliation.repository.ReconciliationDetailRepository;
import com.wishare.finance.domains.reconciliation.repository.ReconciliationRepository;
import com.wishare.finance.infrastructure.remote.vo.bill.ReconciliationGroupRV;
import com.wishare.starter.Global;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 对账领域服务
 *
 * @Author dxclay
 * @Date 2022/10/13
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ReconciliationDomainService {

    private final FlowDetailRepository flowDetailRepository;
    private final ReconciliationBillFacade reconciliationBillFacade;
    private final ReconciliationRepository reconciliationRepository;
    private final BillReconciliationRepository billReconciliationRepository;
    private final ReconciliationDetailRepository reconciliationDetailRepository;

    /**
     * 分页查询对账单列表
     * @param pageF
     * @return
     */
    public Page<ReconciliationE> reconciliationPage(PageF<SearchF<ReconciliationE>> pageF){
        QueryWrapper<ReconciliationE> queryWrapper = pageF.getConditions().getQueryModel();
        queryWrapper.eq("reconcile_mode", ReconcileModeEnum.账票流水对账.getCode());
        queryWrapper.orderByDesc("reconcile_time");
        return reconciliationRepository.page(Page.of(pageF.getPageNum(), pageF.getPageSize()), queryWrapper);
    }

    /**
     * 分页查询对账单列表
     * @param pageF
     * @return
     */
    public Page<ReconciliationE> reconciliationPageClear(PageF<SearchF<ReconciliationE>> pageF){
        QueryWrapper<ReconciliationE> queryWrapper = pageF.getConditions().getQueryModel();
        queryWrapper.eq("reconcile_mode", ReconcileModeEnum.商户清分对账.getCode());
        // 对账列表 按照时间排序
        queryWrapper.orderByDesc("reconcile_time");
        return reconciliationRepository.page(Page.of(pageF.getPageNum(), pageF.getPageSize()), queryWrapper);
    }

    /**
     * 分页查询对账详情列表
     * @param pageF
     * @return
     */
    public Page<ReconciliationDetailE> reconciliationDetailPage(PageF<SearchF<ReconciliationDetailE>> pageF) {
        QueryWrapper<ReconciliationDetailE> queryModel = pageF.getConditions().getQueryModel();
        queryModel.orderByDesc("trade_time");
        queryModel.orderByDesc("id");
        return reconciliationDetailRepository.page(Page.of(pageF.getPageNum(), pageF.getPageSize()), queryModel);
    }
    public Page<ReconciliationDetailE> reconciliationDetailPageYuan(PageF<SearchF<ReconciliationDetailE>> pageF) {
        QueryWrapper<ReconciliationDetailE> queryModel = pageF.getConditions().getQueryModel();

        return reconciliationDetailRepository.reconciliationDetailPageYuan(Page.of(pageF.getPageNum(), pageF.getPageSize()), queryModel);
    }
    public Page<ReconciliationDetailE> reconciliationDetailPageNoYuan(PageF<SearchF<ReconciliationDetailE>> pageF) {
        QueryWrapper<ReconciliationDetailE> queryModel = pageF.getConditions().getQueryModel();
        return reconciliationDetailRepository.reconciliationDetailPageNoYuan(Page.of(pageF.getPageNum(), pageF.getPageSize()), queryModel);
    }

    /**
     * 根据账单id查询账单对账详情
     * @param billId
     * @param reconciliationId
     * @return
     */
    public ReconciliationDetailE getDetailByBillId(Long billId, Long reconciliationId){
        return reconciliationDetailRepository.getByBillId(billId, reconciliationId);
    }

    /**
     * 根据收款获取应收账单
     * @param gatherId 收款/预收id
     * @return
     */
    public List<ReconciliationRecBillDetailOBV> getRecBillDetailsById(Long gatherId,String supCpUnitId){
        return reconciliationBillFacade.getRecBillDetails(gatherId, supCpUnitId);
    }
    public List<ReconciliationRecBillDetailOBV> getRecBillDetailsByBillNoList(List<String> billNoList,String supCpUnitId){
        return reconciliationBillFacade.getRecBillDetailsByBillNoList(billNoList, supCpUnitId);
    }



    /**
     * 根据支付id列表获取支付信息值对象列表
     * @param settleDetailIds
     * @return
     */
    public List<ReconciliationSettleDetailOBV> getSettleDetailOBVs(List<Long> settleDetailIds , String supCpUnitId){
        return reconciliationBillFacade.getSettleDetails(settleDetailIds, supCpUnitId);
    }

    /**
     * 根据支付id列表获取支付信息值对象列表
     * @param billId
     * @return
     */
    public List<ReconciliationSettleDetailOBV> getSettleDetailByBillId(Long billId, String supCpUnitId){
        return reconciliationBillFacade.getSettleDetailsByBillId(billId, supCpUnitId);
    }

    /**
     * 根据支付id列表获取支付信息值对象列表
     * @param billIds
     * @return
     */
    public List<ReconciliationSettleDetailOBV> getSettleDetailByBillIds(List<Long> billIds, String supCpUnitId){
        return reconciliationBillFacade.getSettleDetailsByBillIds(billIds, supCpUnitId);
    }

    /**
     * 获取对账分组
     * @param reconcileRule 对账骨规则
     * @return
     */
    public List<ReconciliationGroupRV> getReconcileGroups(ReconcileRuleE reconcileRule){
        return reconciliationBillFacade.getReconcileGroups(reconcileRule);
    }

    /**
     * 获取清分对账分组
     * @param dimensionRule
     * @return
     */
    public List<ReconciliationGroupRV> getReconcileGroupsClear(ReconcileDimensionRuleOBV dimensionRule){
        return reconciliationBillFacade.getReconcileGroupsClear(dimensionRule);
    }

    /**
     * 获取账单值对象
     * @param reconcileQuery 对账查询信息
     * @return
     */
    public PageV<ReconciliationBillOBV> getReconciliationInvoiceBillPage(ReconcileQuery reconcileQuery){
        return reconciliationBillFacade.getReconciliationInvoiceBillPage(reconcileQuery);
    }
    public PageV<ReconciliationBillOBV> getReconciliationPayBillPage(ReconcileQuery reconcileQuery){
        return reconciliationBillFacade.getReconciliationPayBillPage(reconcileQuery);
    }

    public PageV<ReconciliationBillOBV> getReconciliationGatherBillPage(ReconcileQuery reconcileQuery){
        return reconciliationBillFacade.getReconciliationGatherBillPage(reconcileQuery);
    }

    /**
     * 方圆
     * @param reconcileQuery
     * @return
     */
    public PageV<ReconciliationBillOBV> getFYReconciliationGatherBillPage(ReconcileQuery reconcileQuery){
        return reconciliationBillFacade.getFYReconciliationGatherBillPage(reconcileQuery);
    }
    /**
     * 获取商户清分对账账单数据
     * @param reconcileQuery
     * @return
     */
    public PageV<ReconciliationBillOBV> getMerchantClearingReconciliationBillPage(ReconcileQuery reconcileQuery, String endDate, String beginDate){
         return reconciliationBillFacade.getMerchantClearingReconciliationBillPage(reconcileQuery,  endDate,  beginDate);
    }
    /**
     * 退款单信息
     * @param refundQuery
     * @return
     */
    public List<ReconciliationBillRefundOBV> getReconciliationGatherRefunds(ReconcileGatherRefundQuery refundQuery){
        return reconciliationBillFacade.getReconciliationBillGatherRefunds(refundQuery);
    }

    /**
     * 查询账单付款退款信息
     * @param refundQuery
     * @return
     */
    public List<ReconciliationBillRefundOBV> getRefundInfos(ReconcileRefundQuery refundQuery){
        return reconciliationBillFacade.getRefundInfos(refundQuery);
    }

    /**
     * 根据收款信息查询票据信息
     * @param query
     * @return
     */
    public List<ReconciliationInvoiceDetailOBV> getInvoiceByGatherBillIds(GatherInvoiceQuery query){
        return reconciliationBillFacade.getInvoiceByGatherBillIds(query);
    }
    /**
     * 根据应收账单id查询票据信息
     * @param query
     * @return
     */
    public List<ReconciliationInvoiceDetailOBV> getInvoiceByRecBillIds(ReceivableInvoiceQuery query){
        return reconciliationBillFacade.getInvoiceByRecBillIds(query);
    }



    /**
     * 根据付款信息查询票据信息
     * @param query
     * @return
     */
    public List<ReconciliationInvoiceDetailOBV> getInvoiceByPayBillIds(PayInvoiceQuery query){
        return reconciliationBillFacade.getInvoiceByPayBillIds(query);
    }

    /**
     * 根据票据查询流水信息
     * @param query
     * @return
     */
    public List<ReconciliationFlowDetailOBV> getReconciliationFlows(FlowsInvoiceQuery query){
        return Global.mapperFacade.mapAsList(flowDetailRepository.getReconciliationFlows(query.getInvoiceIds(), query.getFlowClaimDetailStatus()), ReconciliationFlowDetailOBV.class);
    }


    public List<FlowReconciliationDetailDto> getNewReconciliationFlows(String supCpUnitId){
        return flowDetailRepository.getNewReconciliationFlows(supCpUnitId);
    }

    public  int updateFlowClaimRecordFlag(List<ReconciliationFlowDetailOBV> list){
        return  flowDetailRepository.updateFlowClaimRecordFlag(list);
    }

    public  int updateReconcileFlag(Long flowClaimRecordId,Integer reconcileFlag){
        return flowDetailRepository.updateReconcileFlag(flowClaimRecordId, reconcileFlag);
    }
    public List<FlowClaimDetailE> getFlowClaimDetailByInvoiceIds(List<ReconciliationFlowDetailOBV> list){
        return flowDetailRepository.getFlowClaimDetailByInvoiceIds(list);
    }
    public List<FlowClaimDetailE> getByFlowClaimRecordId(List<Long> list){
        return flowDetailRepository.getByFlowClaimRecordId(list);
    }

    /**
     * 获取清分账单值对象
     * @param searchFPageV
     * @param billType
     * @return
     */
    public PageV<ReconciliationBillOBV> getReconciliationClearPage(PageF<SearchF<?>> searchFPageV, BillTypeEnum billType){
        return reconciliationBillFacade.getReconciliationClearPage(searchFPageV, billType);
    }

    /**
     * 添加对账单
     * @return
     */
    public boolean addReconciliation(ReconciliationE reconciliation){
        return reconciliationRepository.save(reconciliation);
    }

    /**
     * 更新对账单
     * @return
     */
    public boolean updateReconciliation(ReconciliationE reconciliation){
        return reconciliationRepository.updateById(reconciliation);
    }

    /**
     * 批量对账
     * @param reconciliationDetailES
     * @return
     */
    public boolean reconcileBatch(List<ReconciliationDetailE> reconciliationDetailES){
        boolean res = reconciliationDetailRepository.saveBatch(reconciliationDetailES);
        if (res){
            //Map<Integer, List<Long>> resultMap = reconciliationDetailES.stream().collect(Collectors.groupingBy(ReconciliationDetailE::getResult,
            //        Collectors.mapping(ReconciliationDetailE::getBillId, Collectors.toList())));
            //List<ReconcileBatchRF> reconcileBatchRFS = new ArrayList<>();
            //resultMap.forEach((k, v) ->{
            //    reconcileBatchRFS.add(new ReconcileBatchRF().setResult(k == 1).setBillIds(v));
            //});
            res = reconciliationBillFacade.updateReconcileBill(reconciliationDetailES);
        }
        return res;
    }

    /**
     * 批量保存
     *
     * @param reconciliationDetailEList
     * @return
     */
    public boolean addBatch(List<ReconciliationDetailE> reconciliationDetailEList) {
       return reconciliationDetailRepository.saveBatch(reconciliationDetailEList);
    }

    public ReconciliationDetailE getReconciliationDetailById(String reconcileDetailId) {
        return reconciliationDetailRepository.getById(reconcileDetailId);
    }

    public ReconciliationClearStatisticsV getReconciliationClearStatistics(PageF<SearchF<ReconciliationE>> pageF){
        QueryWrapper<ReconciliationE> queryWrapper = pageF.getConditions().getQueryModel();
        queryWrapper.eq("reconcile_mode", ReconcileModeEnum.商户清分对账.getCode());
        queryWrapper.eq("deleted", 0);
        // 对账列表 按照时间排序
        queryWrapper.orderByDesc("reconcile_time");
        return reconciliationRepository.getReconciliationClearStatistics(queryWrapper);
    }

    public ReconciliationClearStatisticsV reconciliationClearDetailStatistics(PageF<SearchF<ReconciliationDetailE>> pageF){
        QueryWrapper<ReconciliationDetailE> queryWrapper = pageF.getConditions().getQueryModel();
        queryWrapper.eq("reconcile_mode", ReconcileModeEnum.商户清分对账.getCode());
        queryWrapper.eq("deleted", 0);
        // 对账列表 按照时间排序
        queryWrapper.orderByDesc("reconcile_time");
        return reconciliationDetailRepository.reconciliationClearDetailStatistics(queryWrapper);
    }


    public ReconciliationStatisticsV getReconciliationStatistics(PageF<SearchF<ReconciliationE>> pageF){
        QueryWrapper<ReconciliationE> queryWrapper = pageF.getConditions().getQueryModel();
        queryWrapper.eq("reconcile_mode", ReconcileModeEnum.账票流水对账.getCode());
        // 对账列表 按照时间排序
        queryWrapper.orderByDesc("reconcile_time");
        return reconciliationRepository.getReconciliationStatistics(queryWrapper);
    }

    public ReconciliationStatisticsV reconciliationDetailStatistics(PageF<SearchF<ReconciliationDetailE>> pageF){
        QueryWrapper<ReconciliationDetailE> queryWrapper = pageF.getConditions().getQueryModel();
        queryWrapper.eq("reconcile_mode", ReconcileModeEnum.账票流水对账.getCode());
        queryWrapper.eq("deleted", 0);
        // 对账列表 按照时间排序
        queryWrapper.orderByDesc("reconcile_time");
        return reconciliationDetailRepository.reconciliationDetailStatistics(queryWrapper);
    }
    public List<ReconciliationDetailE> getByBillIds(List<Long> billIds){
        return reconciliationDetailRepository.getByBillIds(billIds);
    }


    public List<ReconciliationDetailE> getByBillIdsAndResult(List<Long> billIds){
        return reconciliationDetailRepository.getByBillIdsAndResult(billIds);
    }

    public List<ReconciliationDetailE> getByBillIdsAndReconciliationId(List<Long> billIds, Long reconciliationId ){
        return reconciliationDetailRepository.getByBillIdsAndReconciliationId(billIds, reconciliationId);
    }

    public List<ReconciliationDetailE> getByReconciliationId(Long reconciliationId){
        return reconciliationDetailRepository.getByReconciliationId(reconciliationId);
    }
}
