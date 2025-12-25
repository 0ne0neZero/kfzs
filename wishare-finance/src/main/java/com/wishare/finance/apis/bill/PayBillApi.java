package com.wishare.finance.apis.bill;

import com.wishare.finance.apps.model.bill.fo.AddPayBillF;
import com.wishare.finance.apps.model.bill.fo.ApproveBatchPayBillF;
import com.wishare.finance.apps.model.bill.fo.ApprovePayBillF;
import com.wishare.finance.apps.model.bill.fo.BillApplyBatchF;
import com.wishare.finance.apps.model.bill.fo.BillApplyF;
import com.wishare.finance.apps.model.bill.fo.BillInferenceF;
import com.wishare.finance.apps.model.bill.fo.DeleteBatchPayBillF;
import com.wishare.finance.apps.model.bill.fo.ListBillInferenceF;
import com.wishare.finance.apps.model.bill.fo.PayBillCarryoverF;
import com.wishare.finance.apps.model.bill.fo.PayBillInvalidF;
import com.wishare.finance.apps.model.bill.vo.BatchBillInferenceF;
import com.wishare.finance.apps.model.bill.vo.BillInferenceV;
import com.wishare.finance.apps.model.bill.vo.PayBillDetailV;
import com.wishare.finance.apps.model.bill.vo.PayBillV;
import com.wishare.finance.apps.service.bill.PayBillAppService;
import com.wishare.finance.apps.service.spacePermission.SpacePermissionAppService;
import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import com.wishare.finance.domains.bill.dto.BillBatchResultDto;
import com.wishare.finance.infrastructure.support.tenant.WishareTenantContext;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Objects;

/**
 * 付款单api
 *
 * @author yancao
 */
@Api(tags = {"付款单"})
@Validated
@RestController
@RequestMapping("/payBill")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class PayBillApi {

    private final PayBillAppService payBillAppService;

    private final SpacePermissionAppService spacePermissionAppService;

    @PostMapping("/add")
    @ApiOperation(value = "新增付款单", notes = "新增应付账单")
    public PayBillV add(@Validated @RequestBody AddPayBillF addPayBillF) {
        return payBillAppService.addBill(addPayBillF);
    }

    @PostMapping("/add/batch")
    @ApiOperation(value = "批量新增付款单", notes = "批量新增付款单")
    public List<PayBillV> addBatch(@Validated @RequestBody @Size(max = 1000, min = 1,
            message = "付款单导入列表大小不允许，仅允许区间为[1,1000]")List<AddPayBillF> addPayBillListF) {
        return payBillAppService.addBatch(addPayBillListF);
    }

    @PostMapping("/apply")
    @ApiOperation(value = "发起审核申请", notes = "发起审核申请")
    public Boolean apply(@Validated @RequestBody BillApplyF billApplyF) {
        return payBillAppService.apply(billApplyF);
    }

    @PostMapping("/apply/batch")
    @ApiOperation(value = "批量发起审核申请", notes = "批量发起审核申请")
    public Boolean applyBatch(@Validated @RequestBody BillApplyBatchF billApplyBatchF) {
        return payBillAppService.applyBatch(billApplyBatchF);
    }

    @PostMapping("/approve")
    @ApiOperation(value = "审核付款单", notes = "审核付款单")
    public Boolean approve(@Validated @RequestBody ApprovePayBillF approvePayBillF) {
        return payBillAppService.approve(approvePayBillF);
    }

    @PostMapping("/approve/batch")
    @ApiOperation(value = "批量审核应付账单", notes = "批量审核应付账单")
    public Boolean approveBatch(@Validated @RequestBody ApproveBatchPayBillF approveBatchPayBillF) {
        return payBillAppService.approveBatch(approveBatchPayBillF);
    }

    @DeleteMapping("/{payBillId}")
    @ApiOperation(value = "删除付款单", notes = "删除付款单")
    public Boolean deleteById(@PathVariable("payBillId") @ApiParam("付款单id") @NotNull(message = "付款单id不能为空") Long payBillId) {
        return payBillAppService.delete(payBillId);
    }

    @DeleteMapping("/delete/batch")
    @ApiOperation(value = "批量删除应付账单", notes = "批量删除应付账单")
    public BillBatchResultDto deleteBatch(@Validated @RequestBody DeleteBatchPayBillF deleteBatchPayBillF) {
        return payBillAppService.deleteBatch(deleteBatchPayBillF);
    }

    @GetMapping("/{payBillId}")
    @ApiOperation(value = "查询付款单信息", notes = "查询付款单信息")
    public PayBillV queryById(@PathVariable("payBillId") @ApiParam("付款单id") @NotNull(message = "付款单id不能为空") Long payBillId) {
        return payBillAppService.queryById(payBillId);
    }

    @GetMapping("/detail/{payBillId}")
    @ApiOperation(value = "查询付款单详情(包含明细)", notes = "查询付款单详情(包含明细)")
    public PayBillDetailV getAllDetail(@PathVariable("payBillId") @ApiParam("付款单id") @NotNull(message = "付款单id不能为空") Long payBillId) {
        return WishareTenantContext.ignore(()->payBillAppService.queryDetailById(payBillId));
    }

    @PostMapping("/list")
    @ApiOperation(value = "根据id集合获取付款单", notes = "根据id集合获取付款单")
    public List<PayBillV> queryByIdList(@Valid @RequestBody @NotEmpty(message = "账单id不能为空") List<Long> payBillIdList) {
        return payBillAppService.queryByIdList(payBillIdList);
    }

    @PostMapping("/page")
    @ApiOperation(value = "分页查询已审核付款单列表", notes = "分页查询已审核付款单列表")
    public PageV<PayBillV> queryPage(@Validated @RequestBody PageF<SearchF<?>> queryF) {
        PageF<SearchF<?>> queryPermissionF = spacePermissionAppService.getSpacePermissionV(queryF,"b");
        if(Objects.isNull(queryPermissionF)){
            return PageV.of(queryF);
        }
        return payBillAppService.getApprovedPage(queryF);
    }

    @PostMapping("/approve/page")
    @ApiOperation(value = "分页查询未审核付款单列表", notes = "分页查询未审核付款单列表")
    public PageV<PayBillV> queryApprovePage(@Validated @RequestBody PageF<SearchF<?>> queryF) {
        return payBillAppService.queryNotApprovedPage(queryF);
    }

    @PostMapping("/carryover")
    @ApiOperation(value = "付款单结转", notes = "付款单结转")
    public Boolean carryover(@Validated @RequestBody PayBillCarryoverF billCarryoverF) {
        return payBillAppService.carryover(billCarryoverF);
    }

    @PostMapping("/invalid")
    @ApiOperation(value = "付款单作废", notes = "付款单作废")
    public Boolean invalid(@Validated @RequestBody PayBillInvalidF payBillInvalidF) {
        return payBillAppService.invalid(payBillInvalidF);
    }

    @GetMapping("/reverse/{payBillId}")
    @ApiOperation(value = "付款单冲销", notes = "付款单冲销")
    public Boolean reverse(@PathVariable("payBillId") @ApiParam("付款单id") @NotNull(message = "付款单id不能为空") Long payBillId) {
        return payBillAppService.reverse(payBillId);
    }

    @ApiOperation(value = "导出收款单", notes = "导出收款单")
    @PostMapping("/export")
    public void export(@RequestBody PageF<SearchF<?>> queryF, HttpServletResponse response) {
        payBillAppService.export(queryF, response);
    }

    @PostMapping("/page/inference")
    @ApiOperation(value = "获取账单推凭信息", notes = "获取账单推凭信息")
    public PageV<BillInferenceV> pageBillInferenceInfo(@RequestBody ListBillInferenceF form) {
        return payBillAppService.pageBillInferenceInfo(form, BillTypeEnum.付款单);
    }

    @PostMapping("/list/inference")
    @ApiOperation(value = "获取账单推凭信息", notes = "获取账单推凭信息")
    public List<BillInferenceV> getBillInferenceInfo(@RequestBody BillInferenceF billInferenceF) {
        return payBillAppService.listInferenceInfo(billInferenceF);
    }

    @PostMapping("/list/inference/batch")
    @ApiOperation(value = "获取账单推凭信息", notes = "获取账单推凭信息")
    public List<BillInferenceV> getBillInferenceInfoByIds(@Validated @RequestBody BatchBillInferenceF batchBillInferenceF) {
        return payBillAppService.listInferenceInfoByIds(batchBillInferenceF);
    }
}
