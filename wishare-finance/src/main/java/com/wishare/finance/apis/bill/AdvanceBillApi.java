package com.wishare.finance.apis.bill;

import com.alibaba.fastjson.JSON;
import com.wishare.finance.apps.model.bill.fo.*;
import com.wishare.finance.apps.model.bill.vo.*;
import com.wishare.finance.apps.service.bill.AdvanceBillAppService;
import com.wishare.finance.apps.service.spacePermission.SpacePermissionAppService;
import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import com.wishare.finance.domains.bill.dto.AdvanceBillGroupDetailDto;
import com.wishare.finance.domains.bill.dto.BillAddBatchResultDto;
import com.wishare.finance.domains.bill.dto.BillBatchResultDto;
import com.wishare.finance.domains.bill.dto.BillSettleDto;
import com.wishare.finance.domains.bill.entity.AdvanceBill;
import com.wishare.starter.annotations.ApiLogCustom;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Objects;

/**
 * 预收账单api
 *
 * @author yancao
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/advance")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AdvanceBillApi {

    private final AdvanceBillAppService advanceBillAppService;

    private final SpacePermissionAppService spacePermissionAppService;

    @PostMapping("/add")
    @ApiOperation(value = "新增预收账单", notes = "新增预收账单")
    public Long add(@Validated @RequestBody AddAdvanceBillF addAdvanceBill) {
        return advanceBillAppService.addBill(addAdvanceBill, AdvanceBillDetailV.class).getId();
    }

    @PostMapping("/sync/add")
    @ApiOperation(value = "新增预收账单", notes = "新增预收账单")
    public Boolean addAdvanceBill(@Validated @RequestBody AdvanceBill advanceBill) {
        return advanceBillAppService.addAdvanceBill(advanceBill);
    }

    @PostMapping("/add/batch")
    @ApiOperation(value = "批量新增预收账单", notes = "批量新增预收账单")
    public List<BillAddBatchResultDto> addBatch(@Valid @RequestBody @Size(max = 1000, min = 1, message = "预收账单列表大小不允许，仅允许区间为[1,1000]") List<AddAdvanceBillF> addAdvanceBillList) {
        return advanceBillAppService.addBatchAdvanceBill(addAdvanceBillList);
    }

    @PostMapping("/pay/batch")
    @ApiOperation(value = "预收预缴批量新增预收账单", notes = "批量新增预收账单")
    public List<AdvanceBillAllDetailV> payBatch(@Valid @RequestBody @Size(max = 1000, min = 1, message = "预收账单列表大小不允许，仅允许区间为[1,1000]") List<AddAdvanceBillF> addAdvanceBillList) {
        return advanceBillAppService.payBatchAdvanceBill(addAdvanceBillList);
    }

    @PostMapping("/approve/batch")
    @ApiOperation(value = "批量审核预收账单", notes = "批量审核预收账单")
    public Boolean approveBatch(@Validated @RequestBody ApproveBatchAdvanceBillF approveBatchAdvanceBillF) {
        return advanceBillAppService.approveBatch(approveBatchAdvanceBillF, BillTypeEnum.预收账单.getCode());
    }

    @PostMapping("/approve")
    @ApiOperation(value = "审核预收账单", notes = "审核预收账单")
    public Boolean approve(@Validated @RequestBody ApproveAdvanceBillF approveAdvanceBillF) {
        return advanceBillAppService.approve(approveAdvanceBillF);
    }

    @PostMapping("/apply")
    @ApiOperation(value = "发起审核申请", notes = "发起审核申请")
    public Long apply(@Validated @RequestBody BillApplyF billApplyF) {
        return advanceBillAppService.apply(billApplyF);
    }

    @PostMapping("/updateApply")
    @ApiOperation(value = "更新审核记录", notes = "更新审核记录")
    public Long updateApply(@Validated @RequestBody BillApplyUpdateF billApplyF) {
        return advanceBillAppService.updateApply(billApplyF);
    }

    @PostMapping("/history/approve")
    @ApiOperation(value = "根据查询条件获取历史审核记录", notes = "根据查询条件获取历史审核记录")
    public List<BillApproveV> approveHistory(@Validated @RequestBody ApproveHistoryF approveHistoryF) {
        return advanceBillAppService.approveHistory(approveHistoryF);
    }
    @PostMapping("/apply/batch")
    @ApiOperation(value = "批量发起审核申请", notes = "批量发起审核申请")
    public Boolean applyBatch(@Validated @RequestBody BillApplyBatchF billApplyBatchF) {
        return advanceBillAppService.applyBatch(billApplyBatchF);
    }

    @DeleteMapping("/{advanceBillId}")
    @ApiOperation(value = "删除预收账单", notes = "删除预收账单")
    public Boolean deleteById(@PathVariable("advanceBillId") @ApiParam("预收账单id") @NotNull(message = "预收账单id不能为空") Long advanceBillId
        ,@RequestParam(value = "supCpUnitId", required = false) @ApiParam("上级收费单元id")  String supCpUnitId) {
        return advanceBillAppService.delete(advanceBillId, supCpUnitId);
    }

    @DeleteMapping("/delete/batch")
    @ApiOperation(value = "批量删除预收账单", notes = "批量删除预收账单")
    public BillBatchResultDto deleteBatch(@Validated @RequestBody DeleteBatchAdvanceBillF deleteBatchAdvanceBillF) {
        return advanceBillAppService.deleteBatch(deleteBatchAdvanceBillF, BillTypeEnum.预收账单.getCode());
    }

    @PostMapping("/deapprove")
    @ApiOperation(value = "反审核预收账单", notes = "反审核预收账单")
    public Boolean deapprove(@RequestParam("billId") @ApiParam("预收账单id") @NotNull(message = "预收账单id不能为空") Long advanceBillId
        ,@RequestParam(value = "supCpUnitId", required = false) @ApiParam("上级收费单元id")  String supCpUnitId) {
        return advanceBillAppService.deapprove(advanceBillId,supCpUnitId);
    }

    @PostMapping("/freeze")
    @ApiOperation(value = "冻结预收账单", notes = "冻结预收账单")
    public Boolean freeze(@RequestParam("billId") @ApiParam("预收账单id") @NotNull(message = "预收账单id不能为空") Long advanceBillId
        ,@RequestParam(value = "supCpUnitId", required = false) @ApiParam("上级收费单元id")  String supCpUnitId) {
        return advanceBillAppService.freeze(advanceBillId,supCpUnitId);
    }

    @PostMapping("/freeze/batch")
    @ApiOperation(value = "批量冻结预收账单", notes = "批量冻结预收账单")
    public Boolean freezeBatch(@Validated @RequestBody FreezeBatchF freezeBatchF) {
        return advanceBillAppService.freezeBatch(freezeBatchF, BillTypeEnum.预收账单.getCode());
    }

    @GetMapping("/apply/detail")
    @ApiOperation(value = "查询预收账单申请详情", notes = "查询预收账单申请详情")
    public AdvanceBillApplyDetailV queryApplyById(@RequestParam("billId") @ApiParam("预收账单id") @NotNull(message = "预收账单id不能为空") Long advanceBillId
        ,@RequestParam(value = "supCpUnitId", required = false) @ApiParam("上级收费单元id")  String supCpUnitId) {
        return advanceBillAppService.getWithApproving(advanceBillId, AdvanceBillApplyDetailV.class,supCpUnitId);
    }

    @PostMapping("/freeze/batchAddReason")
    @ApiOperation(value = "批量冻结应收账单并添加冻结原因", notes = "批量冻结应收账单并添加冻结原因")
    public Boolean freezeBatchAddReason(@Validated @RequestBody FreezeBatchF freezeBatchF,
                                        @RequestParam("type") @ApiParam("冻结类型") @NotNull(message = "冻结类型不能为空") Integer type) {
        return advanceBillAppService.freezeBatchAddReason(freezeBatchF, BillTypeEnum.预收账单.getCode(),type);
    }

    @GetMapping("/{advanceBillId}")
    @ApiOperation(value = "查询预收账单详情", notes = "查询预收账单详情")
    public AdvanceBillDetailV queryById(@PathVariable("advanceBillId") @ApiParam("预收账单ID") Long advanceBillId
        ,@RequestParam(value = "supCpUnitId", required = false) @ApiParam("上级收费单元id")  String supCpUnitId) {
        return advanceBillAppService.getById(advanceBillId, AdvanceBillDetailV.class,supCpUnitId);
    }

    @PostMapping("/page")
    @ApiLogCustom(switchClose = true)
    @ApiOperation(value = "分页查询预收账单列表", notes = "分页查询预收账单列表")
    public PageV<AdvanceBillPageV> getPage(@Validated @RequestBody PageF<SearchF<?>> queryF) {
        PageF<SearchF<?>> queryPermissionF = spacePermissionAppService.getSpacePermissionV(queryF,"b");
        if(Objects.isNull(queryPermissionF)){
            return PageV.of(queryF);
        }
        return advanceBillAppService.getPage(queryF, AdvanceBillPageV.class);
    }

    @PostMapping("/groups")
    @ApiLogCustom(switchClose = true)
    @ApiOperation(value = "分组分页查询预收账单列表", notes = "分组分页查询预收账单列表")
    public PageV<AdvanceBillGroupDetailDto> getGroupPage(@Validated @RequestBody PageF<SearchF<?>> queryF) {
        return advanceBillAppService.getGroupPage(queryF, 0, true);
    }

    @PostMapping("/approve/groups")
    @ApiOperation(value = "分组分页查询预收账单审核列表", notes = "分组分页查询预收账单审核列表")
    public PageV<AdvanceBillGroupDetailDto> queryGroupApprovePage(@Validated @RequestBody PageF<SearchF<?>> queryF) {
        return advanceBillAppService.getGroupPage(queryF, 1, true);
    }

    @PostMapping("/query/detailData")
    @ApiOperation(value = "分页获取预收账单导出明细数据", notes = "分页获取预收账单导出明细数据")
    public PageV<AdvanceBillGroupDetailDto> queryDetailData(@Validated @RequestBody PageF<SearchF<?>> queryF) {
        return advanceBillAppService.queryDetailData(queryF);
    }

    @PostMapping("/query/parentData")
    @ApiOperation(value = "分页获取预收账单父级节点数据", notes = "分页获取预收账单父级节点数据")
    public PageV<AdvanceBillGroupDetailDto> queryParentData(@Validated @RequestBody PageF<SearchF<?>> queryF) {
        return advanceBillAppService.queryParentData(queryF);
    }

    @GetMapping("/detail/{advanceBillId}")
    @ApiOperation(value = "查询预收账单详情(包含明细)", notes = "查询预收账单详情(包含明细)")
    public AdvanceBillAllDetailV detailByAdvanceBillId(@PathVariable("advanceBillId") @ApiParam("预收账单id") @NotNull(message = "预收账单id不能为空") Long advanceBillId) {
        return advanceBillAppService.getAllDetail(advanceBillId, AdvanceBillAllDetailV.class, null);
    }

    @ApiOperation(value = "批量发起开票", notes = "发起开票")
    @PostMapping("/invoice/batch")
    public Boolean invoiceBatch(@Validated @RequestBody List<Long> billIds){
        return advanceBillAppService.invoiceBatch(billIds, null,null);
    }

    @ApiOperation(value = "批量完成开票", notes = "发起开票")
    @PostMapping("/invoice/finish/batch")
    public Boolean finishInvoiceBatch(@Validated @RequestBody List<FinishInvoiceF> finishInvoiceFList){
        return advanceBillAppService.finishInvoiceBatch(finishInvoiceFList);
    }

    @ApiOperation(value = "批量作废,红冲开票金额", notes = "批量作废开票金额")
    @PostMapping("/invoice/void/batch")
    public Boolean invoiceVoidBatch(@Validated @RequestBody List<InvoiceVoidBatchF> invoiceVoidBatchFList){
        return advanceBillAppService.invoiceVoidBatch(invoiceVoidBatchFList);
    }

    @ApiOperation(value = "根据账单ids获取结算记录", notes = "根据账单ids获取结算记录")
    @PostMapping("/getBillSettle")
    public List<BillSettleDto> getBillSettle(@Validated @RequestBody List<Long> billIds,
        @NotBlank(message = "上级收费单元ID(项目ID)不能为空") @ApiParam("上级收费单元ID") @RequestParam("supCpUnitId")String supCpUnitId){
        return advanceBillAppService.getBillSettle(billIds,supCpUnitId);
    }

    @PostMapping("/hand/batch")
    @ApiOperation(value = "批量交账", notes = "批量交账")
    public Boolean bathHandAdvanceBill(@RequestBody BillHandBatchF billHandBatchF) {
        return advanceBillAppService.handBatch(billHandBatchF, BillTypeEnum.预收账单.getCode());
    }

    @PostMapping("/reconcile/batch")
    @ApiOperation(value = "批量对账", notes = "批量对账")
    public Boolean reconcileBatch(@Validated @RequestBody List<ReconcileBatchF> reconcileBatchFS) {
        return advanceBillAppService.reconcileBatch(reconcileBatchFS);
    }

    @PostMapping("/rehand")
    @ApiOperation(value = "反交账", notes = "反交账")
    public Boolean handReversal(@RequestParam("billId") @ApiParam("预收账单id") @NotNull(message = "预收账单id不能为空") Long advanceBillId) {
        return advanceBillAppService.handReversal(advanceBillId,null);
    }

    @PostMapping("/query/idList")
    @ApiOperation(value = "根据预收账单id集合查询账单", notes = "根据预收账单id集合查询账单")
    public List<AdvanceBillDetailV> queryByIdList(@RequestBody List<Long> idList,
        @RequestParam(value = "supCpUnitId",required = false) @ApiParam("上级收费单元id")  String supCpUnitId) {
        return advanceBillAppService.queryByIdList(idList,AdvanceBillDetailV.class,supCpUnitId);
    }

    @GetMapping("/reverse")
    @ApiOperation(value = "冲销", notes = "冲销")
    public Boolean reverse(@RequestParam("billId") @ApiParam("预收账单id") @NotNull(message = "预收账单id不能为空") Long advanceBillId,
                           @RequestParam("extField1") @ApiParam("冲销时如果需要生成新的应收传入：ReversedInitBill）") String extField1){
        return advanceBillAppService.reverse(advanceBillId,extField1);
    }

    @GetMapping("/roback/reverse")
    @ApiOperation(value = "回滚冲销", notes = "回滚冲销")
    public Boolean robackReverse(@RequestParam("billId") @ApiParam("预收账单id") @NotNull(message = "预收账单id不能为空") Long advanceBillId,
                                 @NotBlank(message = "上级收费单元id不能为空") @RequestParam("supCpUnitId") @ApiParam("上级收费单元id")  String supCpUnitId){
        return advanceBillAppService.robackReverse(advanceBillId,supCpUnitId);
    }

    @PostMapping("/list/inference")
    @ApiOperation(value = "获取账单推凭信息", notes = "获取账单推凭信息")
    public List<BillInferenceV> getBillInferenceInfo(@RequestBody BillInferenceF billInferenceF) {
        return advanceBillAppService.listInferenceInfo(billInferenceF);
    }

    @PostMapping("/list/inference/batch")
    @ApiOperation(value = "获取账单推凭信息", notes = "获取账单推凭信息")
    public List<BillInferenceV> getBillInferenceInfoByIds(@RequestBody BatchBillInferenceF batchBillInferenceF) {
        return advanceBillAppService.listInferenceInfoByIds(batchBillInferenceF);
    }

    @PostMapping("/page/inference")
    @ApiOperation(value = "获取账单推凭信息", notes = "获取账单推凭信息")
    public PageV<BillInferenceV> pageBillInferenceInfo(@RequestBody ListBillInferenceF form) {
        return advanceBillAppService.pageBillInferenceInfo(form, BillTypeEnum.预收账单);
    }

    @PostMapping("/page/tenant")
    @ApiLogCustom(switchClose = true)
    @ApiOperation(value = "分页查询预收账单列表（无租户隔离）", notes = "分页查询预收账单列表（无租户隔离）")
    PageV<AdvanceBillPageV> getPageNoTenantLine(@Validated @RequestBody PageF<SearchF<?>> queryF) {
        return advanceBillAppService.getPageNoTenantLine(queryF, AdvanceBillPageV.class);
    }

    @ApiOperation(value = "（通用）根据账单id集合获取预收账单信息", notes = "（通用）根据账单id集合获取预收账单信息")
    @PostMapping("/billInfo/idList")
    public List<AdvanceBillDetailV> getBillInfoByIds(@Validated @RequestBody @NotEmpty(message = "账单id不能为空")List<Long> billIds
        ,@RequestParam(value = "supCpUnitId", required = false) @ApiParam("上级收费单元id")  String supCpUnitId){
        return advanceBillAppService.getBillInfoByIds(billIds, AdvanceBillDetailV.class,supCpUnitId);
    }

    @PostMapping("/carryover")
    @ApiOperation(value = "账单结转", notes = "账单结转")
    public Boolean carryover(@Validated @RequestBody AdvanceBillCarryoverF advanceBillCarryoverF) {
        return advanceBillAppService.carryover(advanceBillCarryoverF);
    }

    @PostMapping("/adjust")
    @ApiOperation(value = "账单调整", notes = "账单调整")
    public Boolean adjust(@Validated @RequestBody AdvanceBillAdjustF advanceBillAdjustF) {
        return advanceBillAppService.adjust(advanceBillAdjustF);
    }

    @PostMapping("/refund")
    @ApiOperation(value = "账单退款", notes = "账单退款")
    public Boolean refund(@Validated @RequestBody AdvanceBillRefundF advanceBillRefundF){
        return advanceBillAppService.refund(advanceBillRefundF);
    }

    @PostMapping("/invalid")
    @ApiOperation(value = "作废账单", notes = "作废账单")
    public Boolean invalid(@Validated @RequestBody AdvanceBillInvalidF advanceBillInvalidF) {
        return advanceBillAppService.invalid(advanceBillInvalidF);
    }

    @PostMapping("/invalid/batch")
    @ApiOperation(value = "批量作废", notes = "批量作废")
    public BillBatchResultDto invalidBatch(@Validated @RequestBody @ApiParam("账单id") @NotEmpty(message = "应收账单id不能为空") List<Long> billIdList) {
        return advanceBillAppService.invalidBatch(billIdList, null);
    }

    @ApiOperation(value = "导出预收单", notes = "导出预收单")
    @PostMapping("/export")
    public void export(@RequestBody PageF<SearchF<?>> queryF, HttpServletResponse response) {
        advanceBillAppService.export(queryF, response);
    }

    @PostMapping("/query/maxEndTime")
    @ApiOperation(value = "查询预收账单最大账单结束时间", notes = "查询预收账单最大账单结束时间")
    public AdvanceMaxEndTimeV queryMaxEndTime(@Validated @RequestBody AdvanceMaxEndTimeBillF maxEndTimeBillF) {
        return advanceBillAppService.queryMaxEndTime(maxEndTimeBillF);
    }

    @GetMapping("/list/roomIds")
    @ApiOperation(value = "获取当前项目下存在可结转预收账单的房间", notes = "获取当前项目下存在可结转预收账单的房间")
    public List<Long> getAdvanceRoomIds(@RequestParam("communityId") @ApiParam("项目id")
                                                              @NotBlank(message = "项目id不能为空") String communityId,
                                        @RequestParam("chargeItemId") @ApiParam("费项id")
                                        @NotNull(message = "费项id不能为空")Long chargeItemId) {
        return advanceBillAppService.getAdvanceRoomIds(communityId, chargeItemId);
    }

    @PostMapping("/getAdvanceBillByRoomIds")
    @ApiOperation(value = "获取房间号下所有预收账单", notes = "获取房间号下所有预收账单")
    public List<AdvanceBillDetailV> getAdvanceBillByRoomIds(@Validated @RequestBody @NotEmpty(message = "房产id不能为空") List<Long> roomIds) {
        return advanceBillAppService.getAdvanceBillByRoomIds(roomIds);
    }

    @PostMapping("/getAdvanceBillTotalMoney")
    @ApiOperation(value = "获取预收总金额", notes = "获取预收总金额")
    public AdvanceBillTotalMoneyV getAdvanceBillTotalMoney(@Validated  @RequestParam @NotBlank(message = "付款人id不能为空") String payerId,@Validated  @RequestParam @NotBlank(message = "项目id不能为空")String communityId) {
        return advanceBillAppService.getAdvanceBillTotalMoney(payerId, communityId);
    }

    @PostMapping("/unfreeze/batch")
    @ApiOperation(value = "批量解冻临时账单", notes = "批量解冻临时账单")
    public Boolean unfreezeBatch(@Validated @RequestBody UnFreezeBatchF unFreezeBatchF) {
        return advanceBillAppService.unfreezeBatch(unFreezeBatchF);
    }

    @PostMapping("/approve/queryPage")
    @ApiLogCustom(switchClose = true)
    @ApiOperation(value = "分页查询预收账单列表", notes = "分页查询预收账单列表")
    public PageV<AdvanceBillGroupDetailDto> queryAdvancePageApprove(@Validated @RequestBody PageF<SearchF<?>> queryF) {
        return advanceBillAppService.queryPageApprove(queryF);
    }

    @PostMapping("/delete/init")
    @ApiOperation(value = "根据查询条件删除初始审核预收账单", notes = "根据查询条件删除初始审核预收账单")
    public Boolean deleteInitBill(@Validated @RequestBody PageF<SearchF<?>> queryF) {
        return advanceBillAppService.deleteInitBill(queryF);
    }

}
