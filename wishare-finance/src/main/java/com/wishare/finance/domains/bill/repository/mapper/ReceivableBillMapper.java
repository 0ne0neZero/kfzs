package com.wishare.finance.domains.bill.repository.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.apps.model.bill.fo.ReceivableIntervalBillF;
import com.wishare.finance.apps.model.bill.fo.ReceivableMaxEndTimeBillF;
import com.wishare.finance.apps.model.bill.fo.UpdateGatherBillF;
import com.wishare.finance.apps.model.bill.vo.*;
import com.wishare.finance.apps.model.third.BillInfoResponse;
import com.wishare.finance.apps.model.third.QueryBillReq;
import com.wishare.finance.domains.bill.command.BillDiscountTotalQuery;
import com.wishare.finance.domains.bill.dto.*;
import com.wishare.finance.domains.bill.entity.ReceivableBill;
import com.wishare.finance.domains.voucher.strategy.core.VoucherBusinessBill;
import com.wishare.finance.domains.voucher.support.fangyuan.strategy.core.PushBusinessBill;
import com.wishare.finance.domains.voucher.support.zhongjiao.strategy.core.PushZJBusinessBill;
import com.wishare.finance.domains.voucher.support.zhongjiao.strategy.core.PushZJBusinessBillForSettlement;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Mapper
public interface ReceivableBillMapper extends BaseMapper<ReceivableBill> {

    /**
     * 分组分页查询
     *
     * @param page
     * @param query
     * @return
     */
    IPage<ReceivableBillGroupDto> pageWithGroup(IPage<?> page, @Param(Constants.WRAPPER) QueryWrapper<?> query);

    /**
     * 分组分页查询count
     *
     * @param query
     * @return
     */
    Integer pageWithGroupCount(@Param(Constants.WRAPPER) QueryWrapper<?> query);

    /**
     * 分组分页查询审核账单信息
     *
     * @param of
     * @param approveWrapper
     * @return
     */
    IPage<ReceivableBillGroupDto> pageWithGroupByApprove(Page<Object> of, @Param(Constants.WRAPPER) QueryWrapper<?> approveWrapper);


    /**
     * 查询初始审核待删除账单id
     *
     * @param approveWrapper
     * @return
     */
    List<Long> initBillIds(@Param(Constants.WRAPPER) QueryWrapper<?> approveWrapper);


    IPage<ReceivableBillGroupDto> approvePageWithGroup(Page<Object> of, @Param(Constants.WRAPPER) QueryWrapper<?> approveWrapper);

    /**
     * 分组分页查询审核账单信息count
     *
     * @param approveWrapper
     * @return
     */
    Integer pageWithGroupByApproveCount(@Param(Constants.WRAPPER) QueryWrapper<?> approveWrapper);


    /**
     * 分页查询账单审核信息
     *
     * @param page
     * @param query
     * @return
     */
    IPage<ReceivableBill> getPageWithApprove(IPage<?> page, @Param(Constants.WRAPPER) QueryWrapper<?> query);

    /**
     * 根据检索条件统计账单退款金额总数
     *
     * @param queryWrapper
     * @return
     */
    BillTotalDto statisticsBillRefund(@Param(Constants.WRAPPER) QueryWrapper<?> queryWrapper);

    BillTotalDto statisticsBillRefund2(@Param(Constants.WRAPPER) QueryWrapper<?> queryWrapper);

    /**
     * 催缴欠缴账单统计
     *
     * @param queryWrapper
     * @return
     */
    BillTotalDto call(@Param(Constants.WRAPPER) QueryWrapper<?> queryWrapper);

    /**
     * 催缴欠缴账单统计
     * @param queryWrapper
     * @return
     */
    List<BillTotalDto> callGroupByRoomAndItem(@Param(Constants.WRAPPER) QueryWrapper<?> queryWrapper);

    /**
     * 根据账单ids批量获取应收账单信息（忽略租户隔离）
     *
     * @param billIds
     * @return
     */
    List<ReceivableBill> getByIdsNoTenant(@Param("billIds") List<Long> billIds, @Param("supCpUnitId") String supCpUnitId);

    /**
     * 修正开票状态
     * 【注】此处定时任务触发，暂时无法解析上下文的租户id
     *
     * @param billEList
     * @return
     */
    @InterceptorIgnore(tenantLine = "on")
    Boolean updateInvoiceState(@Param("billEList") List<ReceivableBill> billEList);

    /**
     * 根据检索条件统计账单金额总数
     *
     * @param wrapper
     * @return
     */
    ReceivableBillTotalDto queryTotal(@Param("ew") QueryWrapper wrapper);

    /**
     * 根据检索条件统计账单金额总数
     *
     * @param wrapper
     * @return
     */
    ReceivableBillTotalDto queryApproveInfoTotal(@Param(Constants.WRAPPER) QueryWrapper<?> wrapper,@Param("tblName") String tblName,@Param("tblName2") String tblName2);

    /**
     * 获取交账信息列表
     *
     * @param billIds
     * @return
     */
    List<BillHandV> listBillHand(@Param("billIds") List<Long> billIds, @Param("supCpUnitId") String supCpUnitId);

    /**
     * 根据审核状态查询账单合计
     *
     * @return
     */
    List<BillApproveTotalDto> queryApproveTotal(@Param(Constants.WRAPPER) QueryWrapper<?> wrapper);

    List<BillApproveTotalDto> queryApproveTotalNew(@Param(Constants.WRAPPER) QueryWrapper<?> wrapper);

    List<BillApproveTotalNewDto> queryApproveBillTotal(@Param(Constants.WRAPPER) QueryWrapper<?> wrapper,
                                                 @Param("tableName") String tableName);

    List<BillApproveTotalNewDto> queryApproveUnionBillTotal(@Param(Constants.WRAPPER) QueryWrapper<?> wrapper,
            @Param("tableName") String tableName,@Param("tblName2") String tblName2);

    Boolean updateSignByRoomIds(
            @Param("roomIds") List<String> roomIds,
            @Param("sign") Integer sign,
            @Param("tableName") String tableName);

    /**
     * 统计账单合计查询
     *
     * @param query
     * @return
     */
    BillDiscountTotalDto queryDiscountTotal(@Param("query") BillDiscountTotalQuery query);

    /**
     * 分页查询临时账单信息
     *
     * @param page    page
     * @param wrapper wrapper
     * @return IPage
     */
    default IPage<ReceivableBill> queryBillByPage(Page<Object> page, @Param(Constants.WRAPPER) QueryWrapper<?> wrapper) {return queryBillByPage(page, wrapper,"");};

    /**
     * 分页查询临时账单信息
     *
     * @param page    page
     * @param wrapper wrapper
     * @param tblName tblName
     * @return IPage
     */
    IPage<ReceivableBill> queryBillByPage(Page<Object> page, @Param(Constants.WRAPPER) QueryWrapper<?> wrapper, String tblName);

    /**
     * 分页查询账单信息及审核信息
     *
     * @param page    page
     * @param wrapper wrapper
     * @param tblName tblName
     * @param tblName tblName2
     * @return IPage
     */
    IPage<ReceivableBill> queryBillApproveByPage(Page<Object> page, @Param(Constants.WRAPPER) QueryWrapper<?> wrapper,@Param("tblName") String tblName,@Param("tblName2") String tblName2);

    /**
     * 查询账单数量，唯一性校验使用
     *
     * @param wrapper wrapper
     * @return Integer
     */
    Integer queryBillCountByPage(@Param(Constants.WRAPPER) QueryWrapper<?> wrapper);
    Integer customerQueryBillCountByPage(@Param(Constants.WRAPPER) QueryWrapper<?> wrapper,  String tblName);
    List<ReceivableBill> customerQueryBillByPage(@Param(Constants.WRAPPER) QueryWrapper<?> wrapper, String tblName, Long offset, Long pageSize);

    /**
     * 获取所有初始审核应收账单
     *
     * @param wrapper wrapper
     * @return List
     */
    IPage<ReceivableBill> listByInitialBill(Page<Object> page, @Param(Constants.WRAPPER) QueryWrapper<?> wrapper);

    /**
     * 获取账单审核统计
     *
     * @param wrapper wrapper
     * @return BillTotalDto
     */
    BillTotalDto queryBillReviewTotal(@Param(Constants.WRAPPER) QueryWrapper<?> wrapper);

    /**
     * 分页查询预收账单信息
     *
     * @param page    page
     * @param wrapper wrapper
     * @return IPage
     */
    @InterceptorIgnore(tenantLine = "on")
    IPage<ReceivableBill> queryBillByPageNoTenantLine(Page<Object> page, @Param(Constants.WRAPPER) QueryWrapper<?> wrapper);

    /**
     * 查询账单推凭信息
     *
     * @param of
     * @param queryModel
     * @return
     */
    Page<BillInferenceV> queryBillInferenceByPage(Page<Object> of, @Param(Constants.WRAPPER) QueryWrapper<?> queryModel);

    /**
     * 联合账单调整信息查询账单推凭信息
     *
     * @param of
     * @param queryModel
     * @return
     */
    Page<BillInferenceV> queryAdjustBillInferenceByPage(Page<Object> of, @Param(Constants.WRAPPER) QueryWrapper<?> queryModel);

    /**
     * 查询冲销作废事件对应的账单
     *
     * @param of
     * @param queryModel
     * @return
     */
    Page<BillInferenceV> queryOffBillInferenceByPage(Page<Object> of, @Param(Constants.WRAPPER) QueryWrapper<?> queryModel);

    /**
     * 查询结账销账事件对应的账单
     *
     * @param of
     * @param queryModel
     * @return
     */
    Page<BillInferenceV> queryCloseBillInferenceByPage(Page<Object> of, @Param(Constants.WRAPPER) QueryWrapper<?> queryModel);

    /**
     * 查询应收计提事件对应的账单
     *
     * @param of
     * @param queryModel
     * @return
     */
    Page<BillInferenceV> queryAccrualBillInferenceByPage(Page<Object> of, @Param(Constants.WRAPPER) QueryWrapper<?> queryModel);

    /**
     * 根据id查询应收账单
     *
     * @param billIdList 账单id
     * @return List
     */
    @InterceptorIgnore(tenantLine = "on")
    List<ReceivableBill> listByIdsNotTenantId(@Param(value = "billIdList") List<Long> billIdList, @Param("supCpUnitId") String supCpUnitId);


    /**
     * 根据id查询应收账单
     *
     * @param billIdList 账单id
     * @return List
     */
    @InterceptorIgnore(tenantLine = "on")
    List<ReceivableBill> listBillsByIdsNotTenantId(@Param(value = "billIdList") List<Long> billIdList, @Param("supCpUnitId") String supCpUnitId);

    /**
     * 根据房号统计金额
     *
     * @param roomIdList 房号id
     * @return List
     */
    List<RoomBillTotalDto> roomBills(@Param(value = "roomIdList") List<Long> roomIdList, @Param("supCpUnitId") String supCpUnitId);

    /**
     * 根据房号和费项统计减免总额
     *
     * @param roomIdList       房号id集合
     * @param chargeItemIdList 费项id集合
     * @param currentYear      是否统计当年
     * @return List
     */
    List<RoomBillTotalDto> roomChargeBills(List<Long> roomIdList, List<Long> chargeItemIdList, boolean currentYear, @Param("supCpUnitId") String supCpUnitId);

    /**
     * 查询应收收费信息
     *
     * @param page         分页参数
     * @param queryWrapper 查询参数
     * @return ReceivableBillApplyDetailV
     */

    Page<ReceivableRoomsDto> receivableRooms(Page<Object> page, @Param(Constants.WRAPPER) QueryWrapper<?> queryWrapper);

    Integer receivableRoomsCount(@Param(Constants.WRAPPER) QueryWrapper<?> queryWrapper);

    @InterceptorIgnore(tenantLine = "on")
    Page<ReceivableRoomsDto> queryCanAdvanceRooms(Page<Object> page, @Param(Constants.WRAPPER) QueryWrapper<?> queryWrapper,
                                                  @Param("ew1") QueryWrapper<?> queryWrapper1,String tenantId);

    /**
     * 查询房间应收账单列表
     *
     * @param state         账单状态
     * @param roomId        房号id
     * @param communityId   项目id
     * @param payState      缴费状态
     * @param targetObjIds  收费对象id
     * @param chargeItemIds 费项id
     * @return List
     */
    List<ReceivableBillsDto> receivableBills(@Param(value = "state") Integer state,
                                             @Param(value = "roomId") String roomId,
                                             @Param(value = "communityId") String communityId,
                                             @Param(value = "payState") List<Integer> payState,
                                             @Param(value = "targetObjIds") List<String> targetObjIds,
                                             @Param(value = "chargeItemIds") List<String> chargeItemIds,
                                             @Param(value = "roomIds") List<String> roomIds);

    /**
     * 获取应收单导出数据
     *
     * @param queryWrapper 查询条件
     * @return List
     */
    List<ReceivableBill> queryExportData(@Param(Constants.WRAPPER) QueryWrapper<?> queryWrapper);

    Page<ReceivableBill> pageByWrapper(Page page, @Param(Constants.WRAPPER) QueryWrapper<?> queryWrapper);

    List<VoucherBusinessBill> selectListByQuery(@Param(Constants.WRAPPER) QueryWrapper<?> wrapper,
                                                @Param("businessBillType") Integer businessBillType,
                                                @Param("sceneType") Integer sceneType, @Param("tableName") String tableName);

    List<ReceivableMaxEndTimeV> queryMaxEndTime(@Param("param") ReceivableMaxEndTimeBillF maxEndTimeBillF);


    List<ReceivableIntervalBillV> queryIntervalBill(@Param("param") ReceivableIntervalBillF query);



    List<VoucherBusinessBill> reducedListByQuery(@Param(Constants.WRAPPER) QueryWrapper<?> wrapper,
                                                 @Param("businessBillType") Integer businessBillType,
                                                 @Param("sceneType") Integer sceneType, @Param("tableName") String tableName);

    /**
     * 查询凭证应收业务账单开票单据信息
     *
     * @param wrapper
     * @param tableName
     * @return
     */
    List<VoucherBusinessBill> listVoucherInvoiceReceiptBillByQuery(@Param(Constants.WRAPPER) QueryWrapper<?> wrapper,
                                                                   @Param("sceneType") Integer sceneType,
                                                                   @Param("businessBillType") Integer businessBillType, @Param("tableName") String tableName);

    List<VoucherBusinessBill> queryCancellationBillList(@Param(Constants.WRAPPER) QueryWrapper<?> wrapper,
                                                        @Param("sceneType") int sceneType,
                                                        @Param("businessBillType") int businessBillType, @Param("tableName") String tableName);

    List<VoucherBusinessBill> queryCancellationSen(@Param(Constants.WRAPPER) QueryWrapper<?> wrapper,
                                                   @Param("tableName") String tableName);
    List<VoucherBusinessBill> chargeAgainstBillList(@Param(Constants.WRAPPER) QueryWrapper<?> wrapper, @Param("tableName") String tableName ,@Param("eventType") int eventType, @Param("chargeAgainstEventType")  int chargeAgainstEventType);


    List<VoucherBusinessBill> receivableListByQuery(@Param(Constants.WRAPPER)QueryWrapper<?> advanceWrapper
                                                  , @Param("businessBillType") Integer businessBillType, @Param("change") boolean change, @Param("tableName") String tableName);

    /**
     * 查询凭证业务收款单已退款单据信息
     *
     * @param wrapper
     * @param tableName
     * @return
     */
    List<VoucherBusinessBill> listVoucherRefundBillByQuery(@Param(Constants.WRAPPER) QueryWrapper<?> wrapper,
                                                           @Param("sceneType") Integer sceneType,
                                                           @Param("businessBillType") Integer businessBillType, @Param("tableName") String tableName);

    /**
     * 修改账单状态为已推凭，由于定时应收计提后续可能使用，可能不需要过滤租户
     */
    @InterceptorIgnore(tenantLine = "true")
    void updateInferenceState(@Param("list") List<Long> idList,
                              @Param("inferenceState") Integer inferenceState,
                              @Param("supCpUnitId") String supCpUnitId);

    /**
     * 临时单，在应付计提和收款结算时，只有结算的临时单才符合要求
     * 查询部分结算/已结算的临时单
     */
    List<VoucherBusinessBill> tempBillListByQuery(@Param(Constants.WRAPPER) QueryWrapper<?> wrapper,
                                                  @Param("businessBillType") Integer businessBillType,
                                                  @Param("sceneType") Integer sceneType, @Param("tableName") String tableName);

    List<VoucherBusinessBill> moneyCarriedForwardList(@Param(Constants.WRAPPER) QueryWrapper<?> wrapper,
                                                      @Param("sceneType") Integer sceneType, @Param("tableName") String tableName);

    void updateBillCostType(@Param("list") List<Long> idList,
                            @Param("billCostType") Integer billCostType);

    List<PushBusinessBill> listPushReceivableBillByQuery(@Param(Constants.WRAPPER) QueryWrapper<?> wrapper);

    List<PushBusinessBill> getReceivableCarryDownBillList(@Param(Constants.WRAPPER) QueryWrapper<?> wrapper);
    List<PushBusinessBill> getCollectionTransferReceivableBillList(@Param(Constants.WRAPPER) QueryWrapper<?> wrapper);

    @InterceptorIgnore(tenantLine = "true")
    int updateSbAccountId(@Param("sbAccountId") Long sbAccountId, @Param("list") List<Long> idList, @Param("supCpUnitId") String supCpUnitId);

    List<PushBusinessBill> getCollectionTransferBillQuery(@Param(Constants.WRAPPER) QueryWrapper<?> wrapper);

    List<PushBusinessBill> arrearsProvisionBillList(@Param(Constants.WRAPPER) QueryWrapper<?> wrapper);

    List<PushBusinessBill> badBillConfirmBilList(@Param(Constants.WRAPPER) QueryWrapper<?> wrappers);

    void updateBatchApprovedStateById(@Param("list") List<Long> billIds, @Param("approvedState") int approvedState);

    void updateProvisionVoucherPushingStatusById(@Param("list") List<Long> receivableBillIds,
                                                 @Param("supCpUnitId") String supCpUnitId,
                                                 @Param("status") Integer status);

    void updateRelatedBillStatusOnPayCost(@Param("list") List<Long> receivableBillIds,
                                          @Param("supCpUnitId") String supCpUnitId,
                                          @Param("status") Integer status);

    void updateRelatedBillStatusOnPayIncome(@Param("list") List<Long> receivableBillIds,
                                          @Param("supCpUnitId") String supCpUnitId,
                                          @Param("status") Integer status);



    /**
     * 分页查询跳收记录
     *
     * @param page    page
     * @param wrapper wrapper
     * @return IPage
     */
    IPage<JumpRecordDto> jumpRecordPage(Page<Object> page, @Param(Constants.WRAPPER) QueryWrapper<?> wrapper);

    List<Long> getReceivableBillIds(@Param(Constants.WRAPPER) QueryWrapper<?> queryWrapper);

    ReceivableAndTemporaryBillTotalV queryTotalMoney(@Param(Constants.WRAPPER) QueryWrapper<?> putLogicDeleted);

    ReceivableAndTemporaryBillTotalV queryTotalMoneyInfo(@Param("billIds") List<Long> billIds,@Param("communityId") String communityId
    , @Param("cutTime") LocalDateTime cutTime,@Param("condition")Boolean condition);

    List<Long> getRoomIds(@Param(Constants.WRAPPER) QueryWrapper<?> queryWrapper);

    List<PushBusinessBill> reconciliationVerificationBillList(@Param(Constants.WRAPPER) QueryWrapper<?> wrappers);

    List<PushBusinessBill> getPaymentAdjustmentReversedBusinessBills(@Param(Constants.WRAPPER) QueryWrapper<?> wrappers);

    List<PushBusinessBill> getPaymentAdjustmentInvalidBusinessBills(@Param(Constants.WRAPPER) QueryWrapper<?> wrappers);

    List<PushZJBusinessBill> revenueRecognitionBillList(@Param(Constants.WRAPPER) QueryWrapper<?> wrappers);

    List<PushZJBusinessBillForSettlement> revenueRecognitionBillListForSettlement(@Param(Constants.WRAPPER) QueryWrapper<?> wrappers);

    List<PushZJBusinessBillForSettlement> revenueRecognitionBillListForSettlementOnReverse(@Param(Constants.WRAPPER) QueryWrapper<?> wrappers);

    List<PushZJBusinessBillForSettlement> revenueRecognitionBillListForPayIncome(@Param(Constants.WRAPPER) QueryWrapper<?> wrappers);

    List<PushZJBusinessBillForSettlement> revenueRecognitionBillListForPayIncomeOnReverse(@Param(Constants.WRAPPER) QueryWrapper<?> wrappers);

    List<PushZJBusinessBill> billAdjustBillList(@Param(Constants.WRAPPER) QueryWrapper<?> wrappers);

    List<PushZJBusinessBill> billAdjustBillListOnOldPayer(@Param(Constants.WRAPPER) QueryWrapper<?> wrappers);

    List<PushZJBusinessBill> billAdjustBillListOnNewPayer(@Param(Constants.WRAPPER) QueryWrapper<?> wrappers);

    List<PushZJBusinessBill> reverseBillList(@Param(Constants.WRAPPER) QueryWrapper<?> wrappers);

    List<ReceivableBill> getByChargeNcId(@Param("chargeNcId") String chargeNcId, @Param("communityId") String communityId);

    void deleteBillById(@Param("communityId") String communityId,@Param("list") List<ReceivableBill> list);

    List<PushBusinessBill> getReceivableCarryDownTwoBillList(@Param(Constants.WRAPPER) QueryWrapper<?> queryWrapper);
    List<PushBusinessBill> voidInvoiceReceivableBillByQuery(@Param(Constants.WRAPPER)QueryWrapper<?> wrapper);

    IPage<ReceivableBill> getPageNotApprove(IPage<?> page, @Param(Constants.WRAPPER) QueryWrapper<?> query);

    long countPageNotApprove(@Param(Constants.WRAPPER) QueryWrapper<?> query);

    List<PushBusinessBill> getVoucherBillDetail(@Param(Constants.WRAPPER)QueryWrapper<?> wrapper);

    int batchUpdateGatherBillById(@Param("list") List<UpdateGatherBillF> updateGatherBillFList);

    boolean batchFreezeBillByIds(@Param("ids") List<Long> ids,@Param("type") Integer type,
                             @Param("tableName") String supCpUnitId);

    boolean batchUnFreezeBillByIds(@Param("ids") List<Long> ids, @Param("tableName") String supCpUnitId);

    List<ReceivableBill> getBillOverdueDetail(@Param("list") List<Long> billIds, @Param("supCpUnitId") String supCpUnitId);

    IPage<UnInvoiceReceivableBillDto> unInvoiceBillPage(Page<Object> page, @Param(Constants.WRAPPER) QueryWrapper<?> queryWrapper);

    /** 查询 方圆 推送状态
     * @param billNo billNo
     * @return
     */
    Integer getVoucherStatus(@Param("billNo") String billNo);


    /**
     * 根据roomIds修改成本中心
     * @param roomIds
     * @param supCpUnitId
     * @param costCenterId
     * @param costCenterName
     */
    void updateCostMsgByRoomIds(@Param("billType")Integer billType,@Param("list") List<String> roomIds,
            @Param("supCpUnitId") String supCpUnitId, @Param("costCenterId") Long costCenterId,
            @Param("costCenterName") String costCenterName,@Param("receivableBillName")String receivableBillName);

    /**
     * 根据项目+账单类型 修改成本中心
     *
     * @param billType           账单类型
     * @param supCpUnitId        项目id
     * @param costCenterId       成本中心id
     * @param costCenterName     成本中心名称
     * @param receivableBillName 分表表名
     */
    void updateCostMsgBySupCpUnitId(@Param("billType")Integer billType,
            @Param("supCpUnitId") String supCpUnitId, @Param("costCenterId") Long costCenterId,
            @Param("costCenterName") String costCenterName,@Param("receivableBillName")String receivableBillName);



    Set<Long> moneyCarriedForwardSet(@Param("communityId") String communityId, @Param("list") Collection<Long> collect);

    List<PushZJBusinessBillForSettlement> revenueRecognitionBillListForSQ(@Param(Constants.WRAPPER) QueryWrapper<?> wrappers);


    List<PushZJBusinessBillForSettlement> revenueRecognitionBillListForPaySettlement(@Param(Constants.WRAPPER) QueryWrapper<?> wrappers);

    String getBusinessTypeCode(@Param("billId")Long billId,
                               @Param("projectId") String projectId);

    void updateCertainStatusOnVoucherBillApprovedV2(@Param("communityId") String communityId,
                                                    @Param("billIdList") List<Long> billIdList,
                                                    @Param("eventType") Integer eventType,
                                                    @Param("status") Integer status);

    List<PushZJBusinessBillForSettlement> queryMxBysettlementIdList(@Param(Constants.WRAPPER) QueryWrapper<?> wrappers);

    void updatePayAppVoucherPushingStatusById(@Param("list") List<Long> receivableBillIds,
                                              @Param("supCpUnitId") String supCpUnitId,
                                              @Param("status") Integer status);
    Page<BillInfoResponse> queryBillInfo(Page<BillInfoResponse> page,
                                         @Param("communityId") String communityId,
                                         @Param("queryBillReq") QueryBillReq queryBillReq,
                                         @Param("receivableBillName") String receivableBillName,
                                         @Param("sysSource") Integer sysSource,
                                         @Param("attribute") Integer attribute);

    //根据合同ID获取合同报账单中对下计提非进行中（1待推送/3推送失败/5已驳回/6单据驳回/8制单失败）临时账单ID
    List<String> getReceivableBillIdList(@Param("contractId")String contractId,
                               @Param("communityId") String communityId);
    //根据临时账单ID获取有无对下结算单计提
    List<String> getdeleteReceivableDxList(@Param("billIdList")List<String> billIdList,
                               @Param("communityId") String communityId);

    void deleteReceivable(@Param("billIdList") List<String> billIdList,
                              @Param("communityId") String communityId);


    //根据临时账单ID获取报账单数据
    List<Map<Integer, String>> getVoucherBillByReceivableId(@Param("communityId") String communityId, @Param("billIdList") List<String> billIdList);
    //根据临时账单ID获取合同报账单数据
    List<Map<String, Object>> getVoucherBillDxByReceivableId(@Param("communityId") String communityId, @Param("billIdList") List<String> billIdList);
    //根据临时账单ID删除对应临时账单
    void deleteReceivableBillById(@Param("communityId") String communityId, @Param("billIdList") List<String> billIdList);
    //根据临时账单ID获取报账单数据-实签
    List<Map<String, String>> getVoucherBillSq(@Param("communityId") String communityId, @Param("billIdList") List<String> billIdList);

    Boolean restoreBill(@Param("id") Long id, @Param("supCpUnitId") String supCpUnitId, @Param("taxAmountNew") BigDecimal taxAmountNew);
    String getVoucherBillList(@Param("billIdList") List<Long> billIdList,
                               @Param("communityId") String communityId);
}

