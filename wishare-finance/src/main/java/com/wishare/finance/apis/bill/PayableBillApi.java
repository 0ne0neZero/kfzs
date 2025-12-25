package com.wishare.finance.apis.bill;

import com.wishare.finance.apps.model.bill.fo.AddBillSettleF;
import com.wishare.finance.apps.model.bill.fo.AddPayableBillF;
import com.wishare.finance.apps.model.bill.fo.ApproveBatchPayableBillF;
import com.wishare.finance.apps.model.bill.fo.ApprovePayableBillF;
import com.wishare.finance.apps.model.bill.fo.BillApplyBatchF;
import com.wishare.finance.apps.model.bill.fo.BillApplyF;
import com.wishare.finance.apps.model.bill.fo.BillInferenceF;
import com.wishare.finance.apps.model.bill.fo.DeleteBatchPayableBillF;
import com.wishare.finance.apps.model.bill.fo.ListBillInferenceF;
import com.wishare.finance.apps.model.bill.fo.PayableBillAdjustF;
import com.wishare.finance.apps.model.bill.fo.PayableBillCarryoverF;
import com.wishare.finance.apps.model.bill.fo.PayableBillInvalidF;
import com.wishare.finance.apps.model.bill.fo.PayableBillRefundF;
import com.wishare.finance.apps.model.bill.fo.ReconcileBatchF;
import com.wishare.finance.apps.model.bill.vo.BatchBillInferenceF;
import com.wishare.finance.apps.model.bill.vo.BillInferenceV;
import com.wishare.finance.apps.model.bill.vo.PayableBillAllDetailV;
import com.wishare.finance.apps.model.bill.vo.PayableBillDetailV;
import com.wishare.finance.apps.model.bill.vo.PayableBillPageV;
import com.wishare.finance.apps.service.bill.PayableBillAppService;
import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import com.wishare.finance.domains.bill.dto.BillBatchResultDto;
import com.wishare.finance.infrastructure.support.tenant.WishareTenantContext;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import javax.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 应付账款api
 *
 * @author yancao
 */
@Api(tags = {"应付账单"})
@Validated
@RestController
@RequestMapping("/payable")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class PayableBillApi {

    private final PayableBillAppService payableBillAppService;

    @PostMapping("/add")
    @ApiOperation(value = "新增应付账单", notes = "新增应付账单")
    public PayableBillDetailV add(@RequestBody AddPayableBillF addPayableBillF){
        return payableBillAppService.addBill(addPayableBillF, PayableBillDetailV.class);
    }

    @PostMapping("/add/batch")
    @ApiOperation(value = "批量新增应付账单", notes = "批量新增应付账单")
    public List<PayableBillDetailV> addBatch(@Validated @RequestBody @Size(max = 1000, min = 1,
            message = "应付单导入列表大小不允许，仅允许区间为[1,1000]")List<AddPayableBillF> addPayableBillList) {
        payableBillAppService.setCustomerInfo(addPayableBillList);
        return payableBillAppService.addBatchBill(addPayableBillList, PayableBillDetailV.class);
    }

    @PostMapping("/page")
    @ApiOperation(value = "分页查询应付账单列表", notes = "分页查询应付账单列表")
    public PageV<PayableBillPageV> getPage(@Validated @RequestBody PageF<SearchF<?>> queryF) {
        return payableBillAppService.getPage(queryF, PayableBillPageV.class);
    }

    @GetMapping("/detail/{billId}")
    @ApiOperation(value = "查询应付账单详情(包含明细)", notes = "查询应付账单详情(包含明细)")
    public PayableBillAllDetailV getAllDetail(@PathVariable("billId") @ApiParam("应收账单id") @NotNull(message = "应收账单id不能为空") Long billId) {
        return WishareTenantContext.ignore(()->payableBillAppService.getAllDetail(billId, PayableBillAllDetailV.class,null));
    }

    @PostMapping("/approve/batch")
    @ApiOperation(value = "批量审核应付账单", notes = "批量审核应付账单")
    public Boolean approveBatch(@Validated @RequestBody ApproveBatchPayableBillF approveBatchPayableBillF) {
        return payableBillAppService.approveBatch(approveBatchPayableBillF, BillTypeEnum.应付账单.getCode());
    }

    @PostMapping("/approve")
    @ApiOperation(value = "审核应付账单", notes = "审核应付账单")
    public Boolean approve(@Validated @RequestBody ApprovePayableBillF approvePayableBillF) {
        return payableBillAppService.approve(approvePayableBillF);
    }

    @DeleteMapping("/{payableBillId}")
    @ApiOperation(value = "删除应收账单", notes = "删除应收账单")
    public Boolean deleteById(@PathVariable("payableBillId") @ApiParam("应付账单id") @NotNull(message = "应收账单id不能为空") Long payableBillId) {
        return payableBillAppService.delete(payableBillId, null);
    }

    @DeleteMapping("/delete/batch")
    @ApiOperation(value = "批量删除应付账单", notes = "批量删除应付账单")
    public BillBatchResultDto deleteBatch(@Validated @RequestBody DeleteBatchPayableBillF deleteBatchPayableBillF) {
        return payableBillAppService.deleteBatch(deleteBatchPayableBillF, BillTypeEnum.应付账单.getCode());
    }

    @PostMapping("/apply")
    @ApiOperation(value = "发起审核申请", notes = "发起审核申请")
    public Long apply(@Validated @RequestBody BillApplyF billApplyF) {
        return payableBillAppService.apply(billApplyF);
    }

    @PostMapping("/apply/batch")
    @ApiOperation(value = "批量发起审核申请", notes = "批量发起审核申请")
    public Boolean applyBatch(@Validated @RequestBody BillApplyBatchF billApplyBatchF) {
        return payableBillAppService.applyBatch(billApplyBatchF);
    }

    @PostMapping("/deapprove")
    @ApiOperation(value = "反审核应付账单", notes = "反审核应付账单")
    public Boolean deapprove(@RequestParam("billId") @ApiParam("应付账单id") @NotNull(message = "应付账单id不能为空") Long payableBillId
        ,@RequestParam("supCpUnitId") @ApiParam("上级收费单元id")  String supCpUnitId) {
        return payableBillAppService.deapprove(payableBillId,supCpUnitId);
    }

    @ApiOperation(value = "批量结算", notes = "批量结算")
    @PostMapping("/settle/batch")
    public Boolean settleBatch(@Validated @RequestBody List<AddBillSettleF> form) {
        return payableBillAppService.settleBatch(form) != null;
    }

    @PostMapping("/reconcile/batch")
    @ApiOperation(value = "批量对账", notes = "批量对账")
    public Boolean reconcileBatch(@Validated @RequestBody List<ReconcileBatchF> reconcileBatchFS) {
        return payableBillAppService.reconcileBatch(reconcileBatchFS);
    }

    @PostMapping("/page/tenant")
    @ApiOperation(value = "分页查询应付账单列表（无租户隔离）", notes = "分页查询应付账单列表（无租户隔离）")
    PageV<PayableBillPageV> getPageNoTenantLine(@Validated @RequestBody PageF<SearchF<?>> queryF) {
        return payableBillAppService.getPageNoTenantLine(queryF, PayableBillPageV.class);
    }

    @ApiOperation(value = "（通用）根据账单id集合获取应付账单信息", notes = "（通用）根据账单id集合获取应付账单信息")
    @PostMapping("/billInfo/idList")
    public List<PayableBillDetailV> getBillInfoByIds(@Validated @RequestBody @NotEmpty(message = "账单id不能为空")List<Long> billIds
        ,@RequestParam(value = "supCpUnitId", required = false) @ApiParam("上级收费单元id")  String supCpUnitId){
        return payableBillAppService.getBillInfoByIds(billIds, PayableBillDetailV.class,supCpUnitId);
    }

    @GetMapping("/reverse")
    @ApiOperation(value = "冲销", notes = "冲销")
    public Boolean reverse(@RequestParam("billId") @ApiParam("应收账单id") @NotNull(message = "应收账单id不能为空") Long payableBillId,
                           @RequestParam("extField1") @ApiParam("冲销时如果需要生成新的应收传入：ReversedInitBill）") String extField1){
        return payableBillAppService.reverse(payableBillId,extField1);
    }

    @PostMapping("/carryover")
    @ApiOperation(value = "账单结转", notes = "账单结转")
    public Boolean carryover(@Validated @RequestBody PayableBillCarryoverF payableBillCarryoverF) {
        return payableBillAppService.carryover(payableBillCarryoverF);
    }

    @PostMapping("/adjust")
    @ApiOperation(value = "账单调整", notes = "账单调整")
    public Boolean adjust(@Validated @RequestBody PayableBillAdjustF payableBillAdjustF) {
        return payableBillAppService.adjust(payableBillAdjustF);
    }

    @PostMapping("/refund")
    @ApiOperation(value = "账单退款", notes = "账单退款")
    public Boolean refund(@Validated @RequestBody PayableBillRefundF payableBillRefundF){
        return payableBillAppService.refund(payableBillRefundF);
    }

    @PostMapping("/invalid")
    @ApiOperation(value = "作废账单", notes = "作废账单")
    public Boolean invalid(@Validated @RequestBody PayableBillInvalidF payableBillInvalidF) {
        return payableBillAppService.invalid(payableBillInvalidF);
    }

    @PostMapping("/invalid/batch")
    @ApiOperation(value = "批量作废", notes = "批量作废")
    public BillBatchResultDto invalidBatch(@Validated @RequestBody @ApiParam("账单id") @NotEmpty(message = "应收账单id不能为空") List<Long> billIdList) {
        return payableBillAppService.invalidBatch(billIdList, null);
    }

    @ApiOperation(value = "导出应付单", notes = "导出应付单")
    @PostMapping("/export")
    public void export(@RequestBody PageF<SearchF<?>> queryF, HttpServletResponse response) {
        payableBillAppService.export(queryF, response);
    }

    @PostMapping("/page/inference")
    @ApiOperation(value = "获取账单推凭信息", notes = "获取账单推凭信息")
    public PageV<BillInferenceV> pageBillInferenceInfo(@RequestBody ListBillInferenceF form) {
        return payableBillAppService.pageBillInferenceInfo(form, BillTypeEnum.应收账单);
    }

    @PostMapping("/list/inference")
    @ApiOperation(value = "获取账单推凭信息", notes = "获取账单推凭信息")
    public List<BillInferenceV> getBillInferenceInfo(@RequestBody BillInferenceF billInferenceF) {
        return payableBillAppService.listInferenceInfo(billInferenceF);
    }

    @PostMapping("/list/inference/batch")
    @ApiOperation(value = "获取账单推凭信息", notes = "获取账单推凭信息")
    public List<BillInferenceV> getBillInferenceInfoByIds(@RequestBody BatchBillInferenceF batchBillInferenceF) {
        return payableBillAppService.listInferenceInfoByIds(batchBillInferenceF);
    }
}
