package com.wishare.finance.apps.service.bill;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.wishare.finance.apps.model.bill.fo.*;
import com.wishare.finance.apps.model.bill.vo.*;
import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import com.wishare.finance.domains.bill.dto.BillBatchResultDto;
import com.wishare.finance.domains.bill.dto.BillGroupDetailDto;
import com.wishare.finance.domains.bill.dto.ImportBillDto;
import com.wishare.finance.domains.bill.entity.AdvanceBill;
import com.wishare.finance.domains.bill.entity.Bill;
import com.wishare.finance.domains.bill.entity.ReceivableBill;
import com.wishare.finance.domains.bill.service.BillDomainService;
import com.wishare.finance.infrastructure.utils.WrapperUtils;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import org.apache.poi.ss.formula.functions.T;

import javax.validation.constraints.Size;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * 账单应用服务
 *
 * @param <DS> 领域服务
 * @param <B>  账单
 * @Author dxclay
 * @Date 2022/9/23
 * @Version 1.0
 */
public interface BillAppService<DS extends BillDomainService, B extends Bill> {


    BillTypeEnum getBillType();

    /**
     * 新增账单
     *
     * @param addBillF
     * @return
     */
    <T, F> T addBill(F addBillF, Class<T> tClass);

    /**
     * 批量新增账单
     *
     * @param addBillFS 账单列表
     * @param <F>
     * @return
     */
    <T, F extends AddBillF> List<T> addBatchBill(List<F> addBillFS, Class<T> tClass);

     <F extends UpdateBillF> boolean updateBatchBill(List<F> updateBillFS);

    /**
     * 导入账单
     *
     * @param addBillFS 账单列表
     * @param <F>
     * @return
     */
    <T extends ImportBillDto, F> List<T> importBill(List<F> addBillFS, Class<T> tClass);

    /**
     * 补录导入账单
     *
     * @param addBillFS 账单列表
     * @param <F>
     * @return
     */
    <T extends ImportBillDto, F> List<T> importRecordBill(List<F> addBillFS, Class<T> tClass);

    /**
     * 审核账单
     *
     * @param approveBillF
     * @return
     */
    boolean approve(ApproveBillF approveBillF);

    /**
     * 批量审核账单
     *
     * @param approveBatchF
     * @return
     */
    boolean approveBatch(ApproveBatchBillF approveBatchF, Integer billType);

    /**
     * 删除账单
     *
     * @param billId
     * @return
     */
    boolean delete(Long billId, String supCpUnitId);

    /**
     * 批量删除账单
     *
     * @param deleteBatchBillF
     * @return
     */
    BillBatchResultDto deleteBatch(DeleteBatchBillF deleteBatchBillF, Integer billType);

    /**
     * 获取审核记录
     * @param billApplyInfoF
     * @return
     */
    List<BillApproveV> getApplyInfo(BillApplyInfoF billApplyInfoF);

    /**
     * 申请
     *
     * @param billApplyF
     * @return
     */
    Long apply(BillApplyF billApplyF);

    /**
     * 更新审核记录
     *
     * @param billApplyF
     * @return
     */
    Long updateApply(BillApplyUpdateF billApplyF);

    /**
     * 批量更新审核记录
     * @param billApplyF billApplyF
     * @return {@link Boolean}
     */
    Boolean updateBatchApplyByIds(BillBatchApplyUpdateF billApplyF);

    /**
     * 根据查询条件获取历史审核记录
     *
     * @param approveHistoryF
     * @return
     */
    List<BillApproveV> approveHistory(ApproveHistoryF approveHistoryF);

    /**
     * @param billApplyBatchF
     * @return
     */
    boolean applyBatch(BillApplyBatchF billApplyBatchF);

    /**
     * 反审核
     *
     * @param billId
     * @return
     */
    boolean deapprove(Long billId, String supCpUnitId);

    /**
     * 冻结账单
     *
     * @param billId
     * @return
     */
    boolean freeze(Long billId, String supCpUnitId);

    /**
     * 批量冻结账单
     *
     * @param freezeBatchF
     * @return
     */
    boolean freezeBatch(FreezeBatchF freezeBatchF, Integer billType);

    /**
     * 批量冻结账单
     *
     * @param freezeBatchF
     * @return
     */
    boolean freezeBatchAddReason(FreezeBatchF freezeBatchF, Integer billType, Integer freezeType);

    /**
     * 账单退款
     *
     * @param billRefundF
     * @param <T>
     * @return
     */
    <T extends BillRefundF> boolean refund(T billRefundF);

    /**
     * 分页查询
     *
     * @param queryF
     * @return
     */
    <T extends BillPageV> PageV<T> getPage(PageF<SearchF<?>> queryF, Class<T> tClass);

    <T extends BillPageV> PageV<T> getChangeApprovePage(PageF<SearchF<?>> queryF, Class<T> tClass);


    /**
     * 分页查询审核账单信息
     *
     * @param queryF
     * @param tClass
     * @param <T>
     * @return
     */
    <T extends BillPageV> PageV<T> getPageWithApprove(PageF<SearchF<?>> queryF, Class<T> tClass);

    /**
     * 查询账单信息
     *
     * @param billId
     * @param <T>
     * @return
     */
    <T extends BillDetailV> T getById(Long billId, Class<T> tClass, String supCpUnitId);

    /**
     * 查询审核中详情
     *
     * @param billId
     * @param tClass
     * @param <T>
     * @return
     */
    <T extends BillApplyDetailV> T getWithApproving(Long billId, Class<T> tClass, String supCpUnitId);

    /**
     * 查询分页信息
     *
     * @param queryF
     * @param type   0:一般列表  1：审核列表
     * @param <T>
     * @return
     */
    <T extends BillGroupDetailDto> PageV<T> getGroupPage(PageF<SearchF<?>> queryF, int type, boolean loadChildren);

    /**
     * 查询账单所有详情（包括审核、退款、调整、结转）
     *
     * @param billId
     * @param tClass
     * @param <T>
     * @return
     */
    <T extends BillAllDetailV> T getAllDetail(Long billId, Class<T> tClass, String supCpUnitId);

    Boolean handBatch(BillHandBatchF billHandBatchF, Integer billType);

    List<BillHandV> listBillHand(List<Long> billIds, String supCpUnitId);

    /**
     * 根据账单id集合查询账单
     *
     * @param billIds id集合
     * @param tClass  class
     * @return List
     */
    <T> List<T> queryByIdList(List<Long> billIds, Class<T> tClass, String supCpUnitId);

    Boolean handReversal(Long billId, String supCpUnitId);

    /**
     * 分页获取导出账单明细数据
     *
     * @param queryF queryF
     * @param <T>    <T>
     * @return PageV
     */
    <T extends BillGroupDetailDto> PageV<T> queryDetailData(PageF<SearchF<?>> queryF);

    /**
     * 分页获取导出账单明细数据
     *
     * @param queryF queryF
     * @param <T>    <T>
     * @return PageV
     */
    <T extends BillGroupDetailDto> PageV<T> queryParentData(PageF<SearchF<?>> queryF);

    /**
     * 根据单个账单获取账单推凭信息
     *
     * @param billInferenceF
     * @return
     */
    List<BillInferenceV> listInferenceInfo(BillInferenceF billInferenceF);

    /**
     * 根据单个账单获取账单推凭信息
     *
     * @param batchBillInferenceF
     * @return
     */
    List<BillInferenceV> listInferenceInfoByIds(BatchBillInferenceF batchBillInferenceF);

    /**
     * 根据条件批量获取账单推凭信息（分页）
     *
     * @param form
     * @return
     */
    PageV<BillInferenceV> pageBillInferenceInfo(ListBillInferenceF form, BillTypeEnum billTypeEnum);

    /**
     * 批量对账
     *
     * @param reconcileBatchFS
     * @return
     */
    boolean reconcileBatch(List<ReconcileBatchF> reconcileBatchFS);

    /**
     * 分页查询（无租户隔离）
     *
     * @param queryF
     * @return
     */
    <T extends BillPageV> PageV<T> getPageNoTenantLine(PageF<SearchF<?>> queryF, Class<T> tClass);

    /**
     * 批量修改账单的推凭状态
     *
     * @param billIds
     * @return
     */
    boolean inferBatch(List<Long> billIds);

    /**
     * 账单结转
     *
     * @param billCarryoverF 结转参数
     * @return Boolean
     */
    <T extends BillCarryoverF> Boolean carryover(T billCarryoverF);

    /**
     * 账单调整（不进行审核操作，直接进行调整）
     *
     * @param billAdjustF 调整参数
     * @return Boolean
     */
    <T extends BillAdjustF> Boolean adjust(T billAdjustF);

    /**
     * 根据账单id集合获取账单信息
     *
     * @param billIds 账单id集合
     * @param tClass  返回的类class
     * @param <T>     <T>
     * @return List
     */
    <T> List<T> getBillInfoByIds(List<Long> billIds, Class<T> tClass, String supCpUnitId);

    /**
     * 根据查询条件获取应收和临时账单信息
     *
     * @param tClass  返回的类class
     * @param <T>     <T>
     * @return List
     */
    <T> List<T> getConditionList(TemporaryBillF temporaryBillF, Class<T> tClass, String supCpUnitId);

    /**
     * 账单作废
     *
     * @param billInvalidF 账单作废入参
     * @return Boolean
     */
    <T extends BillInvalidF> Boolean invalid(T billInvalidF);

    /**
     * 批量作废
     *
     * @param billIdList 账单id集合
     * @return BillBatchResultDto
     */
    BillBatchResultDto invalidBatch(List<Long> billIdList, String supCpUnitId);

    /**
     * 根据账单编号查询账单信息
     *
     * @param billNo 账单编号
     * @return {@link BillOjV}
     */
    BillOjV getBillInfoByBillNo(String billNo, String supCpUnitId);

    /**
     * @param billApplyBatchF
     * @return
     */
    ApplyBatchDeductionV applyBatchDeduction(BillApplyBatchF billApplyBatchF);

    /**
     * 按项目同步批量新增或更新应收账单
     *
     * @param receivableBillList
     * @return
     */
    Boolean syncBatchByCommunity(List<ReceivableBill> receivableBillList);

    Boolean addReceivableBatch(List<ReceivableBill> receivableBillList);

    Boolean addAdvanceBill(AdvanceBill advanceBill);

    /**
     * 按项目同步批量更新应收账单
     *
     * @param receivableBillList
     * @return
     */
    Boolean syncBatchUpdateByCommunity(List<ReceivableBill> receivableBillList);


    Boolean updateBillSettleStatusInfoByIds(List<Long> billIds,String supCpUnitId,Integer status);
}
