package com.wishare.finance.domains.bill.repository.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.domains.bill.command.ReconcileGatherRefundQuery;
import com.wishare.finance.domains.bill.command.ReconcileRefundQuery;
import com.wishare.finance.domains.bill.dto.ReconciliationBillDto;
import com.wishare.finance.domains.bill.dto.ReconciliationGroupDto;
import com.wishare.finance.domains.bill.dto.ReconciliationRefundDto;
import com.wishare.finance.domains.reconciliation.entity.ReconciliationRecBillDetailOBV;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 对账数据库映射接口
 *
 * @Author dxclay
 * @Date 2022/10/16
 * @Version 1.0
 */
@Mapper
public interface BillReconciliationMapper {

    /**
     * 查询所需对账的维度分组
     * @return
     */
    List<ReconciliationGroupDto> queryReconciliationGroup(@Param("groupFields") List<String> groupFields
        , @Param(Constants.WRAPPER)QueryWrapper<?> wrapper
        , @Param("reconcileMode") Integer reconcileMode
        , @Param("gatherBillName") String gatherBillName
        , @Param("gatherDetailName") String gatherDetailName
        , @Param("supCpUnitId") String supCpUnitId);

    List<ReconciliationGroupDto> queryMerchantClearingReconciliationGroup(@Param("groupFields") List<String> groupFields
        , @Param(Constants.WRAPPER)QueryWrapper<?> wrapper
        , @Param("reconcileMode") Integer reconcileMode
        , @Param("gatherBillName") String gatherBillName
        , @Param("gatherDetailName") String gatherDetailName, @Param("supCpUnitId") String supCpUnitId);


    /**
     * 查询对账账单分页列表
     * @param page
     * @param wrapper
     * @return
     */
    Page<ReconciliationBillDto> getReconciliationInvoiceBill(Page<ReconciliationBillDto> page
            , @Param(Constants.WRAPPER)QueryWrapper<?> wrapper
            , @Param("reconcileMode") Integer reconcileMode
            , @Param("gatherBillName") String gatherBillName
            , @Param("gatherDetailName") String gatherDetailName
            , @Param("supCpUnitId") String supCpUnitId
            , @Param("invoiceIds") List<Long> invoiceIds);

    /**
     * 通过发票id 查找应收单
     * @param page
     * @param wrapper
     * @param reconcileMode
     * @param gatherBillName
     * @param gatherDetailName
     * @param supCpUnitId
     * @param invoiceIds
     * @return
     */
    Page<ReconciliationBillDto> getReconciliationBillByInvoice(Page<ReconciliationBillDto> page
            , @Param(Constants.WRAPPER)QueryWrapper<?> wrapper
            , @Param("reconcileMode") Integer reconcileMode
            , @Param("gatherBillName") String gatherBillName
            , @Param("gatherDetailName") String gatherDetailName
            , @Param("supCpUnitId") String supCpUnitId
            , @Param("invoiceIds") List<Long> invoiceIds);


    Page<ReconciliationBillDto> getReconciliationPayBill(Page<ReconciliationBillDto> page
            , @Param(Constants.WRAPPER)QueryWrapper<?> wrapper
            , @Param("reconcileMode") Integer reconcileMode
            , @Param("gatherBillName") String gatherBillName
            , @Param("gatherDetailName") String gatherDetailName
            , @Param("supCpUnitId") String supCpUnitId
            , @Param("payBillIdList") List<Long> payBillIdList);



    List<ReconciliationBillDto> getGatherBillForOffLine(@Param("supCpUnitId") String supCpUnitId,
                                                        @Param("startTime") String startTime,
                                                        @Param("endTime") String endTime,
                                                        @Param("costCenterId") Long costCenterId);

    Page<ReconciliationBillDto> getReconciliationGatherBill(Page<ReconciliationBillDto> page
            , @Param(Constants.WRAPPER)QueryWrapper<?> wrapper
            , @Param("reconcileMode") Integer reconcileMode
            , @Param("gatherBillName") String gatherBillName
            , @Param("gatherDetailName") String gatherDetailName
            , @Param("supCpUnitId") String supCpUnitId
            , @Param("receivableBillName")String receivableBillName);

    Page<ReconciliationBillDto> getFYReconciliationGatherBill(Page<ReconciliationBillDto> page
            , @Param(Constants.WRAPPER)QueryWrapper<?> wrapper
            , @Param("reconcileMode") Integer reconcileMode
            , @Param("gatherBillName") String gatherBillName
            , @Param("gatherDetailName") String gatherDetailName
            , @Param("supCpUnitId") String supCpUnitId
            , @Param("gatherBillIdList") List<Long> gatherBillIdList);


    Page<ReconciliationBillDto> getMerchantClearingReconciliationBill(Page<ReconciliationBillDto> page
        , @Param(Constants.WRAPPER)QueryWrapper<?> wrapper
        , @Param("reconcileMode") Integer reconcileMode
        , @Param("gatherBillName") String gatherBillName
        , @Param("gatherDetailName") String gatherDetailName
         , @Param("endDate") String endDate
        , @Param("beginDate") String beginDate);


    /**
     * 查询账单付款退款信息
     * @param reconcileRefundQuery 账单退款查询信息
     * @return
     */
    List<ReconciliationRefundDto> getReconciliationBillGatherRefunds(@Param("refundQuery") ReconcileGatherRefundQuery reconcileRefundQuery,
                                                                     @Param("gatherDetailName") String gatherDetailName);

    /**
     * 查询账单付款退款信息
     * @param refundQuery 退款单查询信息
     * @return
     */
    List<ReconciliationRefundDto> getRefundInfos(@Param("refundQuery") ReconcileRefundQuery refundQuery,
                                                 @Param("gatherDetailName") String gatherDetailName);


    /**
     * 查询所需清分对账的维度分组
     *
     * @param groupFields
     * @return
     */
    List<ReconciliationGroupDto> getReconcileGroupsClear(@Param("groupFields") List<String> groupFields
        , @Param("gatherBillName") String gatherBillName
        , @Param("gatherDetailName") String gatherDetailName);

    /**
     * 更新预收
     * @param billIds
     * @return
     */
    int updateAdvance(@Param("billIds") List<Long> billIds, @Param("reconcileState") Integer reconcileState, @Param("reconcileMode") Integer reconcileMode);

    /**
     * 更新收款
     * @param billIds
     * @param reconcileState
     */
    int updateGather(@Param("billIds") List<Long> billIds, @Param("reconcileState") Integer reconcileState, @Param("reconcileMode") Integer reconcileMode,
                     @Param("supCpUnitId") String supCpUnitId);

    /**
     * 更新应收和临时
     * @param billIds
     * @param reconcileState
     * @return
     */
    int updateRec(@Param("billIds") List<Long> billIds, @Param("reconcileState") Integer reconcileState, @Param("reconcileMode") Integer reconcileMode,
    @Param("supCpUnitId") String supCpUnitId, @Param("receivableBillName") String receivableBillName, @Param("gatherDetailName") String gatherDetailName);


    int updateReceivableBill(@Param("ids") List<Long> ids, @Param("reconcileState") Integer reconcileState, @Param("reconcileMode") Integer reconcileMode,
                  @Param("supCpUnitId") String supCpUnitId);

    /**
     * 更新付款
     * @param billIds
     * @param reconcileState
     * @return
     */
    int updatePay(@Param("billIds") List<Long> billIds, @Param("reconcileState") Integer reconcileState, @Param("reconcileMode") Integer reconcileMode);

    /**
     * 根据收款获取应收账单
     * @param gatherId
     * @return
     */
    List<ReconciliationRecBillDetailOBV> getRecBillDetailsById(@Param("gatherId") Long gatherId,@Param("supCpUnitId")String supCpUnitId, @Param("gatherDetailName") String gatherDetailName);

    List<ReconciliationRecBillDetailOBV> getRecBillDetailsByBillNoList(@Param("list") List<String> billNoList,
                                                                       @Param("supCpUnitId") String supCpUnitId,
                                                                       @Param("gatherDetailName") String gatherDetailName);
}
