package com.wishare.finance.domains.bill.repository.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.apps.model.bill.fo.AdvanceMaxEndTimeBillF;
import com.wishare.finance.apps.model.bill.vo.*;
import com.wishare.finance.apps.model.bill.vo.AdvanceMaxEndTimeV;
import com.wishare.finance.domains.bill.command.BillDiscountTotalQuery;
import com.wishare.finance.domains.bill.dto.AdvanceBillGroupDto;
import com.wishare.finance.domains.bill.dto.AdvanceBillTotalDto;
import com.wishare.finance.domains.bill.dto.BillApproveTotalDto;
import com.wishare.finance.domains.bill.dto.BillDiscountTotalDto;
import com.wishare.finance.domains.bill.dto.BillTotalDto;
import com.wishare.finance.domains.bill.dto.RoomBillTotalDto;
import com.wishare.finance.domains.bill.entity.AdvanceBill;
import com.wishare.finance.domains.voucher.strategy.core.VoucherBusinessBill;
import com.wishare.finance.domains.voucher.support.fangyuan.strategy.core.PushBusinessBill;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 预收账单mapper
 *
 * @author yancao
 */
@Mapper
public interface AdvanceBillMapper extends BaseMapper<AdvanceBill> {

    /**
     * 分组分页查询账单分组信息
     * @param page
     * @param queryWrapper
     * @return
     */
    IPage<AdvanceBillGroupDto> pageWithGroup(Page<Object> page, @Param(Constants.WRAPPER) QueryWrapper<?> queryWrapper);

    /**
     * 分组分页查询审核分组信息
     * @param page
     * @param queryWrapper
     * @return
     */
    IPage<AdvanceBillGroupDto> pageWithGroupByApprove(Page<Object> page, @Param(Constants.WRAPPER) QueryWrapper<?> queryWrapper);

    /**
     * 分组分页查询账单分组个数
     * @param queryWrapper
     * @return
     */
    int countWithGroup(@Param(Constants.WRAPPER) QueryWrapper<?> queryWrapper);

    /**
     * 分组分页查询审核分组个数
     * @param queryWrapper
     * @return
     */
    int countWithGroupByApprove(@Param(Constants.WRAPPER) QueryWrapper<?> queryWrapper);

    /**
     * 根据检索条件统计账单退款金额总数
     *
     * @param queryWrapper
     * @return
     */
    BillTotalDto statisticsBillRefund(@Param(Constants.WRAPPER) QueryWrapper<?> queryWrapper);
    BillTotalDto statisticsBillRefund2(@Param(Constants.WRAPPER) QueryWrapper<?> queryWrapper);
    /**
     * 根据账单ids批量获取应收账单信息（忽略租户隔离）
     *
     * @param billIds
     * @return
     */
    List<AdvanceBill> getByIdsNoTenant(@Param("billIds") List<Long> billIds,@Param("supCpUnitId")String supCpUnitId);

    /**
     *  修正开票状态
     * 【注】此处定时任务触发，暂时无法解析上下文的租户id
     * @param billEList
     * @return
     */
    @InterceptorIgnore(tenantLine = "on")
    Boolean updateInvoiceState(@Param("billEList") List<AdvanceBill> billEList);

    /**
     * 根据检索条件统计账单金额总数
     * @param queryWrapper queryWrapper
     * @return AdvanceBillTotalDto
     */
    AdvanceBillTotalDto queryTotal(@Param(Constants.WRAPPER) QueryWrapper<?> queryWrapper);

    /**
     * 根据审核状态查询账单合计
     * @return
     */
    List<BillApproveTotalDto> queryApproveTotal(@Param(Constants.WRAPPER) QueryWrapper<?> wrapper,
                                                @Param("billTable")String shareTableName);

    /**
     * 统计账单合计查询
     * @param query
     * @return
     */
    BillDiscountTotalDto queryDiscountTotal(@Param("query") BillDiscountTotalQuery query);

    /**
     * 分页查询预收账单信息
     *
     * @param page page
     * @param wrapper wrapper
     * @return IPage
     */
    IPage<AdvanceBill> queryBillByPage(Page<Object> page, @Param(Constants.WRAPPER) QueryWrapper<?> wrapper);

    /**
     * 获取预收账单个数
     *
     * @param wrapper wrapper
     * @return long
     */
    long countBill(@Param(Constants.WRAPPER) QueryWrapper<?> wrapper);

    /**
     * 获取交账信息列表
     * @param billIds
     * @return
     */
    List<BillHandV> listBillHand(@Param("billIds") List<Long> billIds,@Param("supCpUnitId")String supCpUnitId);

    /**
     * 根据条件获取初始审核账单
     *
     * @param queryWrapper queryWrapper
     * @return List
     */
    IPage<AdvanceBill> listByInitialBill(Page<Object> page, @Param(Constants.WRAPPER)QueryWrapper<?> queryWrapper);

    /**
     * 获取账单审核统计
     *
     * @param wrapper wrapper
     * @return BillTotalDto
     */
    BillTotalDto queryBillReviewTotal(
            @Param(Constants.WRAPPER)QueryWrapper<?> wrapper,
            @Param("billTable")String shareTableName
    );

    /**
     * 查询账单推凭信息
     * @param of
     * @param queryModel
     * @return
     */
    Page<BillInferenceV> queryBillInferenceByPage(Page<Object> of, @Param(Constants.WRAPPER) QueryWrapper<?> queryModel);

    /**
     * 分页查询预收账单信息
     *
     * @param page page
     * @param wrapper wrapper
     * @return IPage
     */
    @InterceptorIgnore(tenantLine = "on")
    IPage<AdvanceBill> queryBillByPageNoTenantLine(Page<Object> page, @Param(Constants.WRAPPER) QueryWrapper<?> wrapper);

    /**
     * 联合账单调整信息查询账单推凭信息
     * @param of
     * @param queryModel
     * @return
     */
    Page<BillInferenceV> queryAdjustBillInferenceByPage(Page<Object> of, @Param(Constants.WRAPPER) QueryWrapper<?> queryModel);

    /**
     * 查询冲销作废事件对应的账单
     * @param of
     * @param queryModel
     * @return
     */
    Page<BillInferenceV> queryOffBillInferenceByPage(Page<Object> of, @Param(Constants.WRAPPER) QueryWrapper<?> queryModel);

    /**
     * 查询结账销账事件对应的账单
     * @param of
     * @param queryModel
     * @return
     */
    Page<BillInferenceV> queryCloseBillInferenceByPage(Page<Object> of, @Param(Constants.WRAPPER) QueryWrapper<?> queryModel);

    /**
     * 查询应收计提事件对应的账单
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
    List<AdvanceBill> listByIdsNotTenantId(@Param(value = "billIdList") List<Long> billIdList);

    /**
     * 根据房号统计金额
     *
     * @param roomIdList 房号id
     * @return List
     */
    List<RoomBillTotalDto> roomBills(List<Long> roomIdList);

    /**
     * 根据房号和费项统计减免总额
     *
     * @param roomIdList 房号id集合
     * @param chargeItemIdList 费项id集合
     * @param currentYear 是否统计当年
     * @return List
     */
    List<RoomBillTotalDto> roomChargeBills(List<Long> roomIdList, List<Long> chargeItemIdList, boolean currentYear);

    /**
     * 查询凭证业务单据信息
     *
     * @param wrapper
     * @param tableName
     * @return
     */
    List<VoucherBusinessBill> listVoucherBillByQuery(@Param(Constants.WRAPPER) QueryWrapper<?> wrapper,
            @Param("voucherEventType") int voucherEventType,
            @Param("special") boolean special,
            @Param("tableName") String tableName);

    /**
     * 查询凭证业务已银行对账单据信息
     *
     * @param wrapper
     * @param tableName
     * @return
     */
    List<VoucherBusinessBill> listVoucherBankBillByQuery(@Param(Constants.WRAPPER) QueryWrapper<?> wrapper, @Param("tableName") String tableName);

    /**
     * 查询凭证业务已流水认领单据信息
     *
     * @param wrapper
     * @param tableName
     * @return
     */
    List<VoucherBusinessBill> listVoucherClaimBillByQuery(@Param(Constants.WRAPPER) QueryWrapper<?> wrapper, @Param("tableName") String tableName);

    /**
     * 查询凭证业务预收单已退款单据信息
     *
     * @param wrapper
     * @param tableName
     * @return
     */
    List<VoucherBusinessBill> listVoucherRefundBillByQuery(@Param(Constants.WRAPPER) QueryWrapper<?> wrapper,
                                                           @Param("sceneType") Integer sceneType,
                                                           @Param("businessBillType") Integer businessBillType, @Param("tableName") String tableName);

    /**
     * 查询预收账单凭证业务已冲销单据信息
     *
     * @param wrapper
     * @param tableName
     * @return
     */
    List<VoucherBusinessBill> listCancellationVoucherBillByQuery(@Param(Constants.WRAPPER) QueryWrapper<?> wrapper,
                                                                 @Param("sceneType") int sceneType,
                                                                 @Param("businessBillType") int businessBillType, @Param("tableName") String tableName);

    /**
     * @param wrapper
     * @param tableName
     * @param eventType 事件类型，如收款结算，预收应收核销
     * @param chargeAgainstEventType 冲销事件类型
     * @return
     */
    List<VoucherBusinessBill> chargeAgainstBillList(@Param(Constants.WRAPPER) QueryWrapper<?> wrapper, @Param("tableName") String tableName, @Param("eventType") int eventType, @Param("chargeAgainstEventType")  int chargeAgainstEventType);

    /**
     * 查询预收账单
     *
     * @param gatherWrapper
     * @param tableName
     * @return
     */
    List<VoucherBusinessBill> listAdvanceBillByQuery(@Param(Constants.WRAPPER) QueryWrapper<?> gatherWrapper
                                                , @Param("businessBillType") Integer businessBillType,
                                                     @Param("change") boolean change, @Param("tableName") String tableName);


    /**
     * 查询应收预收冲销单据信息
     *
     * @param wrapper
     * @param tableName
     * @return
     */
    List<VoucherBusinessBill> listVoucherReversedBillByQuery(@Param(Constants.WRAPPER) QueryWrapper<?> wrapper, @Param("tableName") String tableName);

    AdvanceMaxEndTimeV queryMaxEndTime(@Param("param") AdvanceMaxEndTimeBillF maxEndTimeBillF);

    /**
     * 修改制作凭证成功的预收账单为已推凭状态
     */
    @InterceptorIgnore(tenantLine = "true")
    int updateInferenceState(@Param("list") List<Long> idList, @Param("inferenceState") Integer inferenceState);

    List<Long> getAdvanceRoomIds(@Param("communityId") String communityId,
                                 @Param("chargeItemId") Long chargeItemId);

    List<PushBusinessBill> getAdvanceCarryDownBillList(@Param(Constants.WRAPPER)QueryWrapper<?> wrappers);
    List<PushBusinessBill> getCollectionTransferDownBillList(@Param(Constants.WRAPPER)QueryWrapper<?> wrappers);

    @InterceptorIgnore(tenantLine = "true")
    int updateSbAccountId(@Param("sbAccountId") Long sbAccountId, @Param("list") List<Long> idList);

    List<PushBusinessBill> getAdvanceBillList(@Param(Constants.WRAPPER)QueryWrapper<?> queryWrapper);

    void updateBatchApprovedStateById(@Param("list")List<Long> advanceBillIds, @Param("approvedState")int approvedState);

    AdvanceBillTotalMoneyV getAdvanceBillTotalMoney(@Param("payerId")String payerId,@Param("communityId")String communityId);

    List<Long> getAdvanceBillIds(@Param(Constants.WRAPPER)QueryWrapper<?> queryWrapper);

    List<PushBusinessBill> reconciliationVerificationBillList(@Param(Constants.WRAPPER)QueryWrapper<?> wrapper);

    List<PushBusinessBill> getAdvanceBillCarryoverList(@Param("billId")Long billId);

    IPage<AdvanceBill> getPageNotApprove(
            IPage<?> page, @Param(Constants.WRAPPER) QueryWrapper<?> query, @Param("billTable")String billTable
                                         );

    long countPageNotApprove(@Param(Constants.WRAPPER) QueryWrapper<?> query, @Param("billTable")String billTable);

    /**
     * 方圆
     * 获取款项调整作废的报账单
     * @param wrapper
     * @return
     */
    List<PushBusinessBill> getPaymentAdjustmentReversedBusinessBills(@Param(Constants.WRAPPER)QueryWrapper<?> wrapper);

    List<PushBusinessBill> getPaymentAdjustmentInvalidBusinessBills(@Param(Constants.WRAPPER)QueryWrapper<?> wrapper);


    /**
     * 根据roomIds 修改成本中心信息
     *
     * @param roomIds        房间ids
     * @param supCpUnitId    项目id
     * @param costCenterId   成本中心id
     * @param costCenterName 成本中心名称
     */
    void updateCostMsgByRoomIds(@Param("list") List<String> roomIds,
            @Param("supCpUnitId") String supCpUnitId, @Param("costCenterId") Long costCenterId,
            @Param("costCenterName") String costCenterName);

    /**
     * 根据项目id 修改成本中心信息
     *
     * @param supCpUnitId    项目id
     * @param costCenterId   成本中心id
     * @param costCenterName 成本中心名称
     */
    void updateCostMsgBySupCpUnitId(
            @Param("supCpUnitId") String supCpUnitId, @Param("costCenterId") Long costCenterId,
            @Param("costCenterName") String costCenterName);

    /** 查询临时账单的应收计提且收款结算的预收应收核销
     * @param wrapper
     * @param tableName
     * @return
     */
    List<VoucherBusinessBill> listVoucherReversedTempBillByQuery(@Param(Constants.WRAPPER) QueryWrapper<?> wrapper, @Param("tableName") String tableName);
}
