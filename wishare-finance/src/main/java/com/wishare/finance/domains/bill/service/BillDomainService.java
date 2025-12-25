package com.wishare.finance.domains.bill.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wishare.finance.apps.model.bill.fo.*;
import com.wishare.finance.apps.model.bill.fo.BillDetailF;
import com.wishare.finance.apps.model.bill.fo.EditBillF;
import com.wishare.finance.apps.model.bill.fo.EditBillForBankSignF;
import com.wishare.finance.apps.model.bill.fo.ListBillInferenceF;
import com.wishare.finance.apps.model.bill.vo.*;
import com.wishare.finance.domains.bill.aggregate.BillGatherA;
import com.wishare.finance.domains.bill.command.*;
import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import com.wishare.finance.domains.bill.dto.*;
import com.wishare.finance.domains.bill.entity.AdvanceBill;
import com.wishare.finance.domains.bill.entity.Bill;
import com.wishare.finance.domains.bill.entity.BillApproveE;
import com.wishare.finance.domains.bill.entity.ReceivableBill;
import com.wishare.owl.util.Assert;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.exception.BizException;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.tools.starter.fo.search.SearchF;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * 账单领域服务接口
 *
 * @Author dxclay
 * @Date 2022/9/21
 * @Version 1.0
 */
public interface BillDomainService<B extends Bill> {

    Class<B> getBillClass();


    /**
     * 批量入账
     *
     * @param enterCommands
     * @return
     */
    List<BillUnitaryEnterResultDto> enterBatch(List<BillUnitaryEnterCommand> enterCommands, String supCpUnitId);

    /**
     * 批量新增
     * @return
     */
    boolean saveBatch(List<AddBillCommand<B>> bills);


    boolean updateBatch(List<UpdateBillCommand<B>> bills);

    /**
     * 导入账单
     *
     * @param importBillCommands
     * @return
     */
    <T extends ImportBillDto> List<T> importBill(List<ImportBillCommand<B>> importBillCommands);


    /**
     * 补录导入账单
     *
     * @param importBillCommands
     * @return
     */
    <T extends ImportBillDto> List<T> importRecordBill(List<ImportBillCommand<B>> importBillCommands);

    /**
     * 新增账单
     * @param bill
     * @return
     */
    boolean save(AddBillCommand<B> bill);

    /**
     * 获取审核记录
     * @param billApplyInfoF billApplyInfoF
     * @return {@link BillApproveE}
     */
    List<BillApproveV> getApplyInfo(BillApplyInfoF billApplyInfoF);

    /**
     * 审核
     * @return
     */
    boolean approve(ApproveCommand command);

    /**
     * 批量审核
     * @return
     */
    List<Long> approveBatch(BatchApproveBillCommand command, Integer billType);

    /**
     * 申请审核
     * @return
     */
    Long apply(BillApplyCommand applyInfo);

    /**
     * 更新账单审核记录
     *
     * @param billApproveId
     * @param outApproveId
     * @return
     */
    Long updateApply(Long billApproveId, String outApproveId, String supCpUnitId);

    /**
     * 批量更新账单审核记录
     *
     * @param billApproveIds 审批IDS
     * @param outApproveId   外部审批编号
     * @return
     */
    Boolean updateBatchApplyByIds(List<Long> billApproveIds, String outApproveId, String supCpUnitId);

    /**
     * 根据查询条件获取历史审核记录
     *
     * @param billIds
     * @param outApproveId
     * @return
     */
    List<BillApproveE> approveHistory(List<Long> billIds, String outApproveId, String supCpUnitId);

    /**
     * 批量寝申请审核
     * @param billApplyBatchCommand
     * @return
     */
    boolean applyBatch(BillApplyBatchCommand<?> billApplyBatchCommand);

    /**
     * 删除
     * @return
     */
    boolean delete(Long bid,String supCpUnitId);

    /**
     * 批量删除
     * @return
     */
    BillBatchResultDto deleteBatch(DeleteBatchBillCommand command, Integer billType);

    /**
     * 反审核
     * @return
     */
    boolean deapprove(Long bid,String supCpUnitId);

    /**
     * 冻结
     * @param bid
     * @return
     */
    boolean freeze(Long bid,String supCpUnitId);

    boolean unfreezeBatch(UnFreezeBatchF unFreezeBatchF);

    /**
     * 批量冻结
     * @param command
     * @return
     */
    boolean freezeBatch(FreezeBatchBillCommand command, Integer billType);

    /**
     * 批量冻结
     * @param command
     * @return
     */
    boolean freezeBatchAddReason(FreezeBatchBillCommand command, Integer billType,Integer freezeType);
    /**
     * 批量反审核
     * @return
     */
    boolean deapproveBatch();

    /**
     * 交账
     * @return
     */
    boolean handAccount();

    /**
     * 挂账
     * @return
     */
    boolean onAccount(Long billId,String supCpUnitId);

    /**
     * 销账
     * @return
     */
    boolean writeOff(Long billId,String supCpUnitId);

    /**
     * 开票
     * @return
     */
    boolean invoice();

    /**
     * 批量开票
     * @return
     */
    boolean invoiceBatch(List<Long> billIds,String supCpUnitId, Map<Long,Integer> billIdsMap);

    /**
     * 完成开票
     * @return
     */
    boolean finishInvoice();

    /**
     * 批量完成开票
     *
     * @param finishInvoiceCommands
     * @return
     */
    boolean finishInvoiceBatch(List<FinishInvoiceCommand> finishInvoiceCommands);

    /**
     * 重新计算账单开票状态
     * @param billId
     */
    void reSetBillInvoiceState(Long billId, String supCpUnitId);

    /**
     * 批量作废红冲开票金额
     *
     * @param invoiceVoidBatchFList
     * @return
     */
    boolean invoiceVoidBatch(List<FinishInvoiceCommand> invoiceVoidBatchFList,String supCpUnitId);


    /**
     * 冲销
     * @return
     */
    boolean reverse(Long billId,String extField1,String supCpUnitId);

    /**
     * 回滚冲销
     *
     * @param
     * @return
     */
    boolean robackReverse(Long billId,String supCpUnitId);

    /**
     * 作废
     * @return
     */
    boolean invalid(InvalidCommand command);

    /**
     * 退款
     * @return
     */
    Boolean refund(RefundCommand command);

    /**
     * 结算
     *
     * @param command
     * @return
     */
    Long settle(AddBillSettleCommand command);


    /**
     * 批量结算
     *
     * @param commands
     * @return
     */
    Long settleBatch(List<AddBillSettleCommand> commands);

    BillGatherA<B> settleImportBatch(List<AddBillSettleCommand> commands, List<B> billList);

    /**
     * 根据账单ids获取结算记录
     *
     * @param billIds
     * @return
     */
    List<BillSettleDto> getBillSettle(List<Long> billIds, BillTypeEnum billTypeEnum, String supCpUnitId);

    /**
     * 核销
     * @return
     */
    boolean verify();

    /**
     * 根据id账单数据
     * @param bid
     */
    B getById(Long bid,String supCpUnitId );

    /**
     * 查询审核中详情
     * @param bid
     * @return
     */
    BillApproveDetailDto getWithApproving(Long bid,String supCpUnitId);

    /**
     * 根据id查询账单全部信息， 谨慎使用
     * @return
     */
    BillDetailDto getDetailById(Long bid,String supCpUnitId);

    /**
     * 分页查询
     * @return
     */
    IPage<B> getPage(PageF<SearchF<?>> query);

    /**
     * 分页查询审核账单
     * @param query
     * @return
     */
    IPage<B> getPageWithApprove(PageF<SearchF<?>> query);

    /**
     * 分页查询审核账单列表
     * @return
     */
    IPage<B> getApprovePage(PageF<SearchF<?>> query);

    /**
     * 查询分页信息
     * @param queryF
     * @param type   0:一般列表  1：审核列表
     * @param loadChildren  是否加载子项
     * @return
     * @param <T>
     */
    default <T extends BillGroupDetailDto> PageV<T> getGroupPage(PageF<SearchF<?>> queryF, int type, boolean loadChildren){
        return PageV.of(queryF);
    }

    /**
     * 根据id列表查询账单列表
     * @return
     */
    @Deprecated
    List<B> getList(List<Long> bids);


    /**
     * 根据id列表查询账单列表-项目级别
     * @return
     */
    List<B> getList(List<Long> bids, String supCpUnitId);


    List<B> getConditionList(TemporaryBillF temporaryBillF, String supCpUnitId);

    /**
     * 根据规则查询账单列表
     * @return
     */
    List<B> getList(PageF<SearchF<?>> query, Integer billType);



    /**
     * 获取审核构造器
     * @param query
     * @param queryWrapper
     * @return
     */
    default QueryWrapper<?> getApproveWrapper(PageF<SearchF<?>> query, QueryWrapper<?> queryWrapper){
        List<Integer> operateTypeValue = (List<Integer>) query.getConditions().getSpecialMap().get("operate_type");
        if (CollectionUtils.isNotEmpty(operateTypeValue) ){
            if (operateTypeValue.contains(0)) {
                queryWrapper.and(wrapper -> wrapper.in("ba.operate_type", operateTypeValue).or().isNull("ba.operate_type"));
            }else {
                queryWrapper.in("ba.operate_type", operateTypeValue);
            }
        }
        queryWrapper.eq("b.deleted", 0);
        queryWrapper.in("b.approved_state", List.of(0,1));//审核分组加上已审核过滤
        return queryWrapper;
    }

    /**
     * 批量交账
     * @param map
     * @return
     */
    Boolean handBatch(BatchHandBillCommand map, Integer billType);

    /**
     * 查询统计数据（暂时方法，后期优化）
     * @param query
     * @return
     */
    default BillTotalDto queryTotal(StatisticsBillTotalQuery query){
        return new BillTotalDto();
    }

    /**
     * 获取交账信息
     * @param billIds
     * @return
     */
    List<BillHandV> listBillHand(List<Long> billIds,String supCpUnitId);

    /**
     * 反交账
     * @param billId
     * @return
     */
    Boolean handReversal(Long billId,String supCpUnitId);

    /**
     * 获取审核账单统计数据（分初始和变更）
     *
     * @param query query
     * @param billIds billIds
     * @return BillTotalDto
     */
    BillTotalDto queryBillReviewTotal(PageF<SearchF<?>> query, List<Long> billIds, String supCpUnitId);

    /**
     * 获取账单推凭信息
     * @param map
     * @return
     */
    List<BillInferenceV> listInferenceInfo(BillInferenceQuery map);

    /**
     * 获取账单推凭信息
     * @param map
     * @return
     */
    List<BillInferenceV> listInferenceInfoByIds(BatchBillInferenceQuery map);

    /**
     * 根据条件批量获取账单推凭信息（分页）
     * @param form
     * @return
     */
    PageV<BillInferenceV> pageBillInferenceInfo(ListBillInferenceF form, BillTypeEnum billTypeEnum);

    /**
     * 批量对账
     * @param reconcileBatchCommands
     * @return
     */
    boolean reconcileBatch(List<ReconcileBatchCommand> reconcileBatchCommands);

    /**
     * 分页查询（去除租户隔离）
     * @param queryF
     * @return
     */
    IPage<B> getPageNoTenantLine(PageF<SearchF<?>> queryF);

    /**
     * 批量修改推凭状态
     * @param billIds
     * @return
     */
    boolean inferBatch(List<Long> billIds);

    /**
     * 账单结转
     *
     * @return Boolean
     */
    Boolean carryover(CarryoverCommand carryoverCommand);

    /**
     * 账单调整
     *
     * @param command 调整命令
     * @return Boolean
     */
    Boolean adjust(AdjustCommand command);


    BillBatchResultDto invalidBatch(List<Long> billIdList,String supCpUnitId);


    /**
     * 编辑账单
     * @param editBillF
     * @return
     */
    Boolean editBill(EditBillF editBillF);

    /**
     * 编辑账单
     * @param editBillF
     * @return
     */
    Boolean editRecForBankSign(EditBillForBankSignF editBillF);

    /**
     * 根据账单编号查询账单信息
     * @param billNo 账单编号
     * @return
     */
    BillOjV getBillInfoByBillNo(String billNo,String supCpUnitId);

    /**
     * 查询账单状态
     * @param billDetailF
     * @return
     */
    BillStatusDetailVo statusDetailBill(BillDetailF billDetailF);

    /**
     * 批量申请审核
     * @param billApplyBatchCommand
     * @return
     */
    ApplyBatchDeductionV applyBatchDeduction(BillApplyBatchCommand<?> billApplyBatchCommand);

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

    /**
     * 更新
     * @param billIds
     * @param supCpUnitId
     * @return
     */
    Boolean updateBillInfoByIds(List<Long> billIds,String supCpUnitId,Integer status);


    void setAdjustInfo(List<ReceivableBillPageV> list);
}
