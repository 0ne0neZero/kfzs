package com.wishare.finance.domains.bill.repository.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.apps.model.bill.vo.BillHandV;
import com.wishare.finance.apps.model.bill.vo.BillInferenceV;
import com.wishare.finance.domains.bill.command.BillDiscountTotalQuery;
import com.wishare.finance.domains.bill.dto.BillApproveTotalDto;
import com.wishare.finance.domains.bill.dto.BillDiscountTotalDto;
import com.wishare.finance.domains.bill.dto.BillTotalDto;
import com.wishare.finance.domains.bill.dto.ReceivableBillTotalDto;
import com.wishare.finance.domains.bill.dto.RoomBillTotalDto;
import com.wishare.finance.domains.bill.dto.TempChargeBillExportDto;
import com.wishare.finance.domains.bill.dto.TemporaryBillTotalDto;
import com.wishare.finance.domains.bill.entity.ReceivableBill;
import com.wishare.finance.domains.bill.entity.TemporaryChargeBill;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TemporaryChargeBillMapper extends BaseMapper<TemporaryChargeBill> {

    /**
     * 分页获取临时账单导出数据
     *
     * @param page page
     * @param wrapper wrapper
     * @return IPage
     */
    IPage<TempChargeBillExportDto> queryExportDataPage(IPage<TempChargeBillExportDto> page, @Param("ew") QueryWrapper wrapper);

    /**
     * 分页获取临时账单导出数据
     *
     * @param page page
     * @param wrapper wrapper
     * @return IPage
     */
    IPage<TempChargeBillExportDto> queryInitialApprovedExportDataPage(IPage<TempChargeBillExportDto> page, @Param("ew") QueryWrapper wrapper);

    /**
     * 分页查询账单审核信息
     * @param page
     * @param query
     * @return
     */
    IPage<TemporaryChargeBill> getPageWithApprove(IPage<?> page, @Param(Constants.WRAPPER) QueryWrapper<?> query);

    /**
     * 根据检索条件统计账单退款金额总数
     *
     * @param queryWrapper
     * @return
     */
    BillTotalDto statisticsBillRefund(@Param(Constants.WRAPPER) QueryWrapper<?> queryWrapper,@Param("supCpUnitId") String supCpUnitId);

    BillTotalDto statisticsBillRefund2(@Param(Constants.WRAPPER)QueryWrapper<?> queryWrapper,@Param("supCpUnitId") String supCpUnitId);

    /**
     * 根据账单ids批量获取应收账单信息（忽略租户隔离）
     *
     * @param billIds
     * @return
     */
    List<TemporaryChargeBill> getByIdsNoTenant(@Param("billIds") List<Long> billIds,@Param("supCpUnitId")String supCpUnitId);

    /**
     *  修正开票状态
     * 【注】此处定时任务触发，暂时无法解析上下文的租户id
     * @param billEList
     * @return
     */
    @InterceptorIgnore(tenantLine = "on")
    Boolean updateInvoiceState(@Param("billEList")List<TemporaryChargeBill> billEList);

    /**
     * 根据检索条件统计账单金额总数
     * @param wrapper
     * @return
     */
    TemporaryBillTotalDto queryTotal(@Param("ew") QueryWrapper wrapper);

    /**
     * 根据检索条件统计账单金额总数
     *
     * @param wrapper
     * @return
     */
    TemporaryBillTotalDto queryApproveInfoTotal(@Param(Constants.WRAPPER) QueryWrapper<?> wrapper,@Param("tblName") String tblName,@Param("tblName2") String tblName2);

    /**
     * 根据审核状态查询账单合计
     * @return
     */
    List<BillApproveTotalDto> queryApproveTotal(@Param(Constants.WRAPPER) QueryWrapper<?> wrapper);

    /**
     * 统计账单合计查询
     * @param query
     * @return
     */
    BillDiscountTotalDto queryDiscountTotal(@Param("query") BillDiscountTotalQuery query);

    /**
     * 分页查询临时账单信息
     *
     * @param page page
     * @param wrapper wrapper
     * @return IPage
     */
    IPage<TemporaryChargeBill> queryBillByPage(Page<Object> page, @Param(Constants.WRAPPER) QueryWrapper<?> wrapper);

    /**
     * 分页查询账单信息及审核信息
     *
     * @param page    page
     * @param wrapper wrapper
     * @param tblName tblName
     * @return IPage
     */
    IPage<TemporaryChargeBill> queryBillApproveByPage(Page<Object> page, @Param(Constants.WRAPPER) QueryWrapper<?> wrapper, @Param("tblName") String tblName,
            @Param("tblName2")String tblName2);

    /**
     * 获取临时账单个数
     *
     * @param wrapper wrapper
     * @return long
     */
    long countBill(@Param(Constants.WRAPPER) QueryWrapper<?> wrapper);

    /**
     * 获取临时账单审核信息个数
     *
     * @param wrapper wrapper
     * @return long
     */
    long countApproveBill(@Param(Constants.WRAPPER) QueryWrapper<?> wrapper,@Param("tblName") String tblName,
            @Param("tblName2")String tblName2);

    /**
     * 获取交账信息列表
     * @param billIds
     * @return
     */
    List<BillHandV> listBillHand(@Param("billIds") List<Long> billIds,@Param("supCpUnitId")String supCpUnitId);
    /**
     * 获取所有初始审核应收账单
     *
     * @param queryWrapper queryWrapper
     * @return List
     */
    IPage<TemporaryChargeBill> listByInitialBill(Page<Object> page, @Param(Constants.WRAPPER)QueryWrapper<?> queryWrapper);

    /**
     * 获取账单审核统计
     *
     * @param wrapper wrapper
     * @return BillTotalDto
     */
    BillTotalDto queryBillReviewTotal(@Param(Constants.WRAPPER)QueryWrapper<?> wrapper);
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
    IPage<TemporaryChargeBill> queryBillByPageNoTenantLine(Page<Object> page, @Param(Constants.WRAPPER) QueryWrapper<?> wrapper);

    /**
     * 分页查询账单审核审核列表
     * @param page
     * @param query
     * @return
     */
    IPage<TemporaryChargeBill> getPageNotApprove(IPage<?> page, @Param(Constants.WRAPPER) QueryWrapper<?> query);

    /**
     * 获取临时账单审核未审核个数
     * @param query
     * @return long
     */
    long countNotApprove(@Param(Constants.WRAPPER) QueryWrapper<?> query);

    /**
     * 获取临时账单审核个数
     * @param query
     * @return long
     */
    long countWithApprove(@Param(Constants.WRAPPER) QueryWrapper<?> query);

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
    List<TemporaryChargeBill> listByIdsNotTenantId(@Param(value = "billIdList") List<Long> billIdList,@Param("supCpUnitId")String supCpUnitId);

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
     * 获取临时账单数据
     * @return List
     */
    @InterceptorIgnore(tenantLine = "on")
    List<TemporaryChargeBill> getDealDate();

    List<Long> getTemporaryBillIds(@Param(Constants.WRAPPER) QueryWrapper<?> temporaryBillQueryWrapper);


    List<Long> initBillIds(@Param(Constants.WRAPPER) QueryWrapper<?> approveWrapper);

    List<Long> getBillRoomIds(@Param("communityId") String communityId,
                                 @Param("chargeItemId") Long chargeItemId);


    Boolean updateBatch(@Param("tblName") String tblName,@Param("billList") List<TemporaryChargeBill> billList);

    Boolean updateBatchStatus(@Param("tblName") String receivableBillName, @Param("ids") List<Long> ids, @Param("billEventType") Integer billEventType, @Param("flag") int flag);
}
