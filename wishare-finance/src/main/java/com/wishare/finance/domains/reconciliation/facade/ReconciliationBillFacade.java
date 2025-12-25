package com.wishare.finance.domains.reconciliation.facade;

import com.wishare.finance.apps.model.bill.fo.QueryReconcileGroupF;
import com.wishare.finance.apps.model.bill.fo.ReconcileBatchF;
import com.wishare.finance.apps.model.bill.vo.*;
import com.wishare.finance.apps.service.bill.*;
import com.wishare.finance.domains.bill.command.ReconcileDimensionRuleQuery;
import com.wishare.finance.domains.bill.command.ReconcilePreconditionsQuery;
import com.wishare.finance.domains.bill.command.ReconcileQuery;
import com.wishare.finance.domains.bill.command.ReconcileGatherRefundQuery;
import com.wishare.finance.domains.bill.command.ReconcileRefundQuery;
import com.wishare.finance.domains.bill.command.UpdateReconcileCommand;
import com.wishare.finance.domains.bill.consts.enums.BillSharedingColumn;
import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import com.wishare.finance.domains.bill.dto.ReconciliationBillDto;
import com.wishare.finance.domains.bill.dto.ReconciliationGroupDto;
import com.wishare.finance.domains.bill.dto.ReconciliationRefundDto;
import com.wishare.finance.domains.bill.repository.BillReconciliationRepository;
import com.wishare.finance.domains.bill.service.ReceiptAmountUtils;
import com.wishare.finance.domains.invoicereceipt.command.invocing.GatherInvoiceQuery;
import com.wishare.finance.domains.invoicereceipt.command.invocing.PayInvoiceQuery;
import com.wishare.finance.domains.invoicereceipt.command.invocing.ReceivableInvoiceQuery;
import com.wishare.finance.domains.invoicereceipt.repository.InvoiceReceiptDetailRepository;
import com.wishare.finance.domains.reconciliation.entity.ReconcileDimensionRuleOBV;
import com.wishare.finance.domains.reconciliation.entity.ReconcileRuleE;
import com.wishare.finance.domains.reconciliation.entity.ReconciliationBillOBV;
import com.wishare.finance.domains.reconciliation.entity.ReconciliationBillRefundOBV;
import com.wishare.finance.domains.reconciliation.entity.ReconciliationDetailE;
import com.wishare.finance.domains.reconciliation.entity.ReconciliationInvoiceDetailOBV;
import com.wishare.finance.domains.reconciliation.entity.ReconciliationRecBillDetailOBV;
import com.wishare.finance.domains.reconciliation.entity.ReconciliationSettleDetailOBV;
import com.wishare.finance.infrastructure.conts.TableNames;
import com.wishare.finance.infrastructure.remote.fo.bill.ReconcileBatchRF;
import com.wishare.finance.infrastructure.remote.vo.bill.ReconciliationGroupRV;
import com.wishare.finance.infrastructure.support.mutiltable.MutilTableParam;
import com.wishare.finance.infrastructure.utils.RepositoryUtil;
import com.wishare.starter.Global;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.util.StringUtils;

/**
 * 对账账单防腐层
 *
 * @author dxclay
 * @version 1.0
 * @since 2022/10/13
 */

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ReconciliationBillFacade {

    /*
     *  理论上facade不允许调用APP层， 由于代码迁移，暂时对应
     */
    private final BillSettleAppService billSettleAppService;
    private final AdvanceBillAppService advanceBillAppService;
    private final PayableBillAppService payableBillAppService;
    private final ReceivableBillAppService receivableBillAppService;
    private final BillReconciliationAppService billReconciliationAppService;
    private final TemporaryChargeBillAppService temporaryChargeBillAppService;
    private final GatherBillAppService gatherBillAppService;

    private final SharedBillAppService sharedBillAppService;

    private final PayBillAppService payBillAppService;
    private final ReceiptAmountUtils receiptAmountUtils;
    /**
     * 账单对账资源库
     */
    private final BillReconciliationRepository billReconciliationRepository;
    /**
     * 票据详情信息
     */
    private final InvoiceReceiptDetailRepository invoiceReceiptDetailRepository;


    /**
     * 根据账单结算id列表获取支付信息值对象
     *
     * @param settleDetailIds 结算id列表
     * @return 结算信息
     */
    public List<ReconciliationSettleDetailOBV> getSettleDetails(List<Long> settleDetailIds, String supCpUnitId) {
        List<ReconciliationSettleDetailOBV> results = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(settleDetailIds)) {
            List<BillSettleV> billSettleS = billSettleAppService.getSettleByIds(settleDetailIds, supCpUnitId);
            if (CollectionUtils.isNotEmpty(billSettleS)) {
                results = Global.mapperFacade.mapAsList(billSettleS, ReconciliationSettleDetailOBV.class);
            }
        }
        return results;
    }

    /**
     * 根据账单结算id列表获取支付信息值对象
     *
     * @param billId 账单id
     * @return 结算详细
     */
    public List<ReconciliationSettleDetailOBV> getSettleDetailsByBillId(Long billId, String supCpUnitId) {
        if (Objects.nonNull(billId)) {
            List<BillSettleV> billSettleS = billSettleAppService.getSettleByBillId(billId, null,supCpUnitId);
            if (CollectionUtils.isNotEmpty(billSettleS)) {
                return Global.mapperFacade.mapAsList(billSettleS, ReconciliationSettleDetailOBV.class);
            }
        }
        return new ArrayList<>();
    }

    /**
     * 根据账单结算id列表获取支付信息值对象
     *
     * @param billIds 账单id列表
     * @return 支付详情信息
     */
    public List<ReconciliationSettleDetailOBV> getSettleDetailsByBillIds(List<Long> billIds, String supCpUnitId) {
        if (Objects.nonNull(billIds)) {
            List<BillSettleV> billSettleS = billSettleAppService.getSettleByBillIds(billIds, supCpUnitId);
            if (CollectionUtils.isNotEmpty(billSettleS)) {
                return Global.mapperFacade.mapAsList(billSettleS, ReconciliationSettleDetailOBV.class);
            }
        }
        return new ArrayList<>();
    }

    /**
     * 查询所需对账的维度分组
     *
     * @param reconcileRule 维度参数
     * @return 对账组列表
     */
    public List<ReconciliationGroupRV> getReconcileGroups(ReconcileRuleE reconcileRule) {
        ReconcileQuery reconcileQuery = new ReconcileQuery();
        // 获取数据可能为空，映射实体添加非空判断
        if (null != reconcileRule.getDimensionRule()){
            reconcileQuery.setDimensionRuleQuery(Global.mapperFacade.map(reconcileRule.getDimensionRule(), ReconcileDimensionRuleQuery.class));
        }
        // 获取数据可能为空，映射实体添加非空判断
        if (null != reconcileRule.getPreconditions()){
            reconcileQuery.setPreconditionsQueries(Global.mapperFacade.mapAsList(reconcileRule.getPreconditions(), ReconcilePreconditionsQuery.class));
        }
        reconcileQuery.setReconcileMode(reconcileRule.getReconcileMode());
        List<ReconciliationGroupDto> reconciliationGroup = billReconciliationRepository.getReconciliationGroup(reconcileQuery);
        return Global.mapperFacade.mapAsList(reconciliationGroup, ReconciliationGroupRV.class);
    }


    /**
     * 查询所需对账的维度分组
     *
     * @param dimensionRuleOBV 维度参数
     * @return 对账组列表
     */
    public List<ReconciliationGroupRV> getReconcileGroupsClear(ReconcileDimensionRuleOBV dimensionRuleOBV) {
        List<ReconciliationGroupDto> reconciliationGroup = billReconciliationAppService.getReconcileGroupsClear(Global.mapperFacade.map(dimensionRuleOBV, QueryReconcileGroupF.class));
        return Global.mapperFacade.mapAsList(reconciliationGroup, ReconciliationGroupRV.class);
    }

    /**
     * 获取对账账单对账账单值对象列表
     *
     * @param reconcileQuery 对账查询参数
     * @return 对账列表
     */
    public PageV<ReconciliationBillOBV> getReconciliationInvoiceBillPage(ReconcileQuery reconcileQuery) {
        String supCpUnitId = MutilTableParam.supCpUnitId.get();
        if(!StringUtils.hasText(supCpUnitId)) {
            throw new IllegalArgumentException("上级收费单元ID参数不能为空!");
        }
        String gatherBillName = sharedBillAppService.getShareTableName(supCpUnitId, TableNames.GATHER_BILL);
        String gatherDetailName = sharedBillAppService.getShareTableName(supCpUnitId, TableNames.GATHER_DETAIL);
        PageV<ReconciliationBillDto> reconciliationBillPage = billReconciliationRepository.getReconciliationInvoiceBill(reconcileQuery, gatherBillName, gatherDetailName, supCpUnitId);
        // 过滤掉 收款方式为结转和押金结转的收款金额
        receiptAmountUtils.handleNoCheckAmount(gatherDetailName,reconciliationBillPage);

        return RepositoryUtil.convertPage(reconciliationBillPage, ReconciliationBillOBV.class);
    }
    public PageV<ReconciliationBillOBV> getReconciliationPayBillPage(ReconcileQuery reconcileQuery) {
        String supCpUnitId = MutilTableParam.supCpUnitId.get();
        if(!StringUtils.hasText(supCpUnitId)) {
            throw new IllegalArgumentException("上级收费单元ID参数不能为空!");
        }
        String gatherBillName = sharedBillAppService.getShareTableName(supCpUnitId, TableNames.GATHER_BILL);
        String gatherDetailName = sharedBillAppService.getShareTableName(supCpUnitId, TableNames.GATHER_DETAIL);
        PageV<ReconciliationBillDto> reconciliationBillPage = billReconciliationRepository.getReconciliationPayBill(reconcileQuery, gatherBillName, gatherDetailName, supCpUnitId);

        return RepositoryUtil.convertPage(reconciliationBillPage, ReconciliationBillOBV.class);
    }
    public PageV<ReconciliationBillOBV> getReconciliationGatherBillPage(ReconcileQuery reconcileQuery) {
        String supCpUnitId = MutilTableParam.supCpUnitId.get();
        if(!StringUtils.hasText(supCpUnitId)) {
            throw new IllegalArgumentException("上级收费单元ID参数不能为空!");
        }
        String gatherBillName = sharedBillAppService.getShareTableName(supCpUnitId, TableNames.GATHER_BILL);
        String gatherDetailName = sharedBillAppService.getShareTableName(supCpUnitId, TableNames.GATHER_DETAIL);
        String receivableBillName = sharedBillAppService.getShareTableName(supCpUnitId, TableNames.RECEIVABLE_BILL);
        PageV<ReconciliationBillDto> reconciliationBillPage = billReconciliationRepository.getReconciliationGatherBill(reconcileQuery, gatherBillName, receivableBillName, gatherDetailName, supCpUnitId);


        // 过滤掉 收款方式为结转和押金结转的收款金额
        receiptAmountUtils.handleNoCheckAmount(gatherDetailName,reconciliationBillPage);

        return RepositoryUtil.convertPage(reconciliationBillPage, ReconciliationBillOBV.class);
    }

    public PageV<ReconciliationBillOBV> getFYReconciliationGatherBillPage(ReconcileQuery reconcileQuery) {
        String supCpUnitId = MutilTableParam.supCpUnitId.get();
        if(!StringUtils.hasText(supCpUnitId)) {
            throw new IllegalArgumentException("上级收费单元ID参数不能为空!");
        }
        String gatherBillName = sharedBillAppService.getShareTableName(supCpUnitId, TableNames.GATHER_BILL);
        String gatherDetailName = sharedBillAppService.getShareTableName(supCpUnitId, TableNames.GATHER_DETAIL);
        PageV<ReconciliationBillDto> reconciliationBillPage = billReconciliationRepository.getFYReconciliationGatherBill(reconcileQuery, gatherBillName, gatherDetailName, supCpUnitId);

        // 过滤掉 收款方式为结转和押金结转的收款金额
        receiptAmountUtils.handleNoCheckAmount(gatherDetailName,reconciliationBillPage);
        return RepositoryUtil.convertPage(reconciliationBillPage, ReconciliationBillOBV.class);
    }

    /**
     * 获取商户清分对账账单数据
     * @param reconcileQuery
     * @return
     */
    public PageV<ReconciliationBillOBV> getMerchantClearingReconciliationBillPage(ReconcileQuery reconcileQuery,String endDate, String beginDate) {
        PageV<ReconciliationBillDto> merchantClearingReconciliationBillPage = billReconciliationRepository.getMerchantClearingReconciliationBillPage(reconcileQuery,  endDate,  beginDate);
        return RepositoryUtil.convertPage(merchantClearingReconciliationBillPage, ReconciliationBillOBV.class);
    }
    /**
     * 更新账单对账信息
     * @param reconciliationDetailES
     * @return
     */
    public boolean updateReconcileBill(List<ReconciliationDetailE> reconciliationDetailES) {
        List<UpdateReconcileCommand> reconcileCommands = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(reconciliationDetailES)) {
            Map<Integer, List<ReconciliationDetailE>> billTypeDetailMap = reconciliationDetailES.stream().collect(Collectors.groupingBy(ReconciliationDetailE::getBillType));
            for (Map.Entry<Integer, List<ReconciliationDetailE>> entry : billTypeDetailMap.entrySet()) {
                Map<Integer, List<ReconciliationDetailE>> resMap = entry.getValue().stream().collect(Collectors.groupingBy(ReconciliationDetailE::getResult));
                for (Map.Entry<Integer, List<ReconciliationDetailE>> resEntry : resMap.entrySet()) {
                    List<ReconciliationDetailE> detailES = resEntry.getValue();
                    reconcileCommands.addAll(detailES.stream().map(i -> new UpdateReconcileCommand()
                            .setBillType(entry.getKey())
                            .setBillIds(detailES.stream().map(ReconciliationDetailE::getBillId).collect(Collectors.toList()))
                            .setReconcileMode(i.getReconcileMode())
                            .setReconcileState(i.getResult())).collect(Collectors.toList()));
                }
            }
        }
        return billReconciliationRepository.updateReconcileBill(reconcileCommands);
    }

    /**
     * 查询账单退款信息
     *
     * @param refundQuery
     * @return
     */
    public List<ReconciliationBillRefundOBV> getReconciliationBillGatherRefunds(ReconcileGatherRefundQuery refundQuery) {
        return Global.mapperFacade.mapAsList(billReconciliationRepository.getReconciliationBillGatherRefunds(refundQuery), ReconciliationBillRefundOBV.class);
    }

    /**
     * 查询账单付款退款信息
     * @param refundQuery
     * @return
     */
    public List<ReconciliationBillRefundOBV> getRefundInfos(ReconcileRefundQuery refundQuery){
        return Global.mapperFacade.mapAsList(billReconciliationRepository.getRefundInfos(refundQuery), ReconciliationBillRefundOBV.class);
    }

    /**
     * 根据收款单信息查询票据信息
     *
     * @param query
     * @return
     */
    public List<ReconciliationInvoiceDetailOBV> getInvoiceByGatherBillIds(GatherInvoiceQuery query) {
        String supCpUnitId = MutilTableParam.supCpUnitId.get();
        if(!StringUtils.hasText(supCpUnitId)) {
            throw new IllegalArgumentException("上级收费单元ID参数不能为空!");
        }
        String gatherDetailName = sharedBillAppService.getShareTableName(supCpUnitId, TableNames.GATHER_DETAIL);
        return Global.mapperFacade.mapAsList(invoiceReceiptDetailRepository.getByGatherBillIds(query, gatherDetailName), ReconciliationInvoiceDetailOBV.class);
    }

    /**
     * 根据应收账单id查询票据信息
     * @param query
     * @return
     */
    public List<ReconciliationInvoiceDetailOBV> getInvoiceByRecBillIds(ReceivableInvoiceQuery query) {
        String supCpUnitId = MutilTableParam.supCpUnitId.get();
        if(!StringUtils.hasText(supCpUnitId)) {
            throw new IllegalArgumentException("上级收费单元ID参数不能为空!");
        }
        return Global.mapperFacade.mapAsList(invoiceReceiptDetailRepository.getInvoiceByRecBillIds(query), ReconciliationInvoiceDetailOBV.class);
    }

    /**
     * 根据付款单信息查询票据信息
     *
     * @param query
     * @return
     */
    public List<ReconciliationInvoiceDetailOBV> getInvoiceByPayBillIds(PayInvoiceQuery query) {
        return Global.mapperFacade.mapAsList(invoiceReceiptDetailRepository.getByPayBillIds(query), ReconciliationInvoiceDetailOBV.class);
    }


    /**
     * 获取清分对账账单值对象列表
     *
     * @param pageF    查询参数
     * @param billType 账单类型
     * @return 对账列表
     */
    public PageV<ReconciliationBillOBV> getReconciliationClearPage(PageF<SearchF<?>> pageF, BillTypeEnum billType) {
        List<ReconciliationBillOBV> reconciliationBillOBVS = new ArrayList<>();
        switch (billType) {
            case 收款单:
                PageV<GatherBillV> gatherBillVPageV = gatherBillAppService.getPage(pageF);
                for (GatherBillV item : gatherBillVPageV.getRecords()) {
                    ReconciliationBillOBV reconciliationBillOBV = Global.mapperFacade.map(item, ReconciliationBillOBV.class);
                    reconciliationBillOBV.setActualAmount(item.getTotalAmount());
                    reconciliationBillOBV.setBillType(billType.getCode());
                    reconciliationBillOBV.setTradeTime(item.getGmtCreate());
                    reconciliationBillOBVS.add(reconciliationBillOBV);
                }
                break;
            case 付款单:
                PageV<PayBillV> payBillVPageV = payBillAppService.getPage(pageF);
                for (PayBillV item : payBillVPageV.getRecords()) {
                    ReconciliationBillOBV reconciliationBillOBV = Global.mapperFacade.map(item, ReconciliationBillOBV.class);
                    reconciliationBillOBV.setActualAmount(item.getTotalAmount());
                    reconciliationBillOBV.setBillType(billType.getCode());
                    reconciliationBillOBV.setTradeTime(item.getGmtCreate());
                    reconciliationBillOBVS.add(reconciliationBillOBV);
                }
                break;
            case 预收账单:
                PageV<AdvanceBillPageV> advanceBillPage = advanceBillAppService.getPage(pageF, AdvanceBillPageV.class);
                for (AdvanceBillPageV item : advanceBillPage.getRecords()) {
                    ReconciliationBillOBV reconciliationBillOBV = Global.mapperFacade.map(item, ReconciliationBillOBV.class);
                    reconciliationBillOBV.setActualAmount(item.getActualPayAmount());
                    reconciliationBillOBV.setBillType(billType.getCode());
                    reconciliationBillOBV.setTradeTime(item.getGmtCreate());
                    reconciliationBillOBVS.add(reconciliationBillOBV);
                }
                break;
            default:
                reconciliationBillOBVS = new ArrayList<>();
                break;
        }
        return PageV.of(pageF.getPageNum(), pageF.getPageSize(), reconciliationBillOBVS);
    }

    /**
     * 批量对账
     *
     * @param reconcileBatchRFS 批量对账
     * @param billType          账单类型
     * @return 结果true：正常 false：失败
     */
    public boolean reconcileBatch(List<ReconcileBatchRF> reconcileBatchRFS, BillTypeEnum billType) {
        List<ReconcileBatchF> reconcileBatchFS = Global.mapperFacade.mapAsList(reconcileBatchRFS, ReconcileBatchF.class);
        switch (billType) {
            case 应收账单:
                return receivableBillAppService.reconcileBatch(reconcileBatchFS);
            case 预收账单:
                return advanceBillAppService.reconcileBatch(reconcileBatchFS);
            case 临时收费账单:
                return temporaryChargeBillAppService.reconcileBatch(reconcileBatchFS);
            case 应付账单:
                return payableBillAppService.reconcileBatch(reconcileBatchFS);
            default:
                return false;
        }
    }


    /**
     * 根据收款获取应收账单
     * @param gatherId 收款/预收id
     * @return
     */
    public List<ReconciliationRecBillDetailOBV> getRecBillDetails(Long gatherId,String supCpUnitId) {
        return billReconciliationRepository.getRecBillDetails(gatherId, supCpUnitId);
    }


    public List<ReconciliationRecBillDetailOBV> getRecBillDetailsByBillNoList(List<String> billNoList, String supCpUnitId) {
        return billReconciliationRepository.getRecBillDetailsByBillNoList(billNoList, supCpUnitId);
    }
}
