package com.wishare.finance.apis.bill;

import com.wishare.finance.apps.model.bill.fo.AddGatherBillF;
import com.wishare.finance.apps.model.bill.fo.ApproveBatchGatherBillF;
import com.wishare.finance.apps.model.bill.fo.ApproveGatherBillF;
import com.wishare.finance.apps.model.bill.fo.BillApplyBatchF;
import com.wishare.finance.apps.model.bill.fo.BillApplyF;
import com.wishare.finance.apps.model.bill.fo.BillFlagF;
import com.wishare.finance.apps.model.bill.fo.BillInferenceF;
import com.wishare.finance.apps.model.bill.fo.GatherApplyUpdateF;
import com.wishare.finance.apps.model.bill.fo.GatherBillCarryoverF;
import com.wishare.finance.apps.model.bill.fo.GatherBillInvalidF;
import com.wishare.finance.apps.model.bill.fo.GatherCollectF;
import com.wishare.finance.apps.model.bill.fo.GatherInvoiceF;
import com.wishare.finance.apps.model.bill.fo.GetPaySourceByBillNoF;
import com.wishare.finance.apps.model.bill.fo.ListBillInferenceF;
import com.wishare.finance.apps.model.bill.fo.PayInvoiceListF;
import com.wishare.finance.apps.model.bill.fo.PayListF;
import com.wishare.finance.apps.model.bill.vo.BatchBillInferenceF;
import com.wishare.finance.apps.model.bill.vo.BillInferenceV;
import com.wishare.finance.apps.model.bill.vo.BillSimpleInfoV;
import com.wishare.finance.apps.model.bill.vo.GatherBillDetailV;
import com.wishare.finance.apps.model.bill.vo.GatherBillIgnoreV;
import com.wishare.finance.apps.model.bill.vo.GatherBillV;
import com.wishare.finance.apps.model.bill.vo.GatherDetailV;
import com.wishare.finance.apps.model.bill.vo.GetPaySourceByBillNoV;
import com.wishare.finance.apps.model.invoice.invoice.dto.InvoiceDto;
import com.wishare.finance.apps.model.invoice.invoice.vo.GatherInvoiceRefreshV;
import com.wishare.finance.apps.service.bill.GatherBillAppService;
import com.wishare.finance.apps.service.spacePermission.SpacePermissionAppService;
import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import com.wishare.finance.domains.bill.dto.GatherAndPayStatisticsDto;
import com.wishare.finance.domains.bill.dto.PayListDto;
import com.wishare.finance.domains.bill.entity.GatherBill;
import com.wishare.finance.domains.bill.entity.GatherDetail;
import com.wishare.finance.domains.bill.service.GatherBillDomainService;
import com.wishare.finance.domains.refund.GatherDetailInfoF;
import com.wishare.finance.infrastructure.support.tenant.WishareTenantContext;
import com.wishare.starter.annotations.ApiLogCustom;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
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
@Api(tags = {"收款单"})
@Validated
@RestController
@RequestMapping("/gather")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GatherBillApi {

    private final GatherBillAppService gatherBillAppService;

    private final SpacePermissionAppService spacePermissionAppService;
    private final GatherBillDomainService billDomainService;

    @PostMapping("/add/batch")
    @ApiOperation(value = "批量新增收款单", notes = "批量新增收款单")
    public List<GatherBillV> addBatch(@Validated @RequestBody @Size(max = 1000, min = 1,
            message = "收款单导入列表大小不允许，仅允许区间为[1,1000]") List<AddGatherBillF> addGatherBillFList) {
        return gatherBillAppService.addBatch(addGatherBillFList);
    }

    @PostMapping("/add/sync/batch")
    @ApiOperation(value = "批量同步收款单", notes = "批量同步收款单")
    public Boolean addGatherBatch(@Validated @RequestBody List<GatherBill> gatherBillList) {
        return gatherBillAppService.addGatherBatch(gatherBillList);
    }

    @PostMapping("/add/detail/batch")
    @ApiOperation(value = "批量同步收款单子表", notes = "批量同步收款单子表")
    public Boolean addGatherDetailBatch(@Validated @RequestBody List<GatherDetail> gatherDetailList) {
         return gatherBillAppService.addGatherDetailBatch(gatherDetailList);
    }


    @GetMapping("/get/gatherBill")
    @ApiOperation(value = "获取收款单", notes = "获取收款单")
    public List<GatherBill> getByOutBusId(@RequestHeader("tenantId")String tenantId,@RequestParam(value = "hncId") String hncId,@RequestParam(value = "communityId") String communityId){
        return gatherBillAppService.getByOutBusId(hncId,communityId);
    }

    @GetMapping("/{gatherBillId}")
    @ApiLogCustom(switchClose = true)
    @ApiOperation(value = "查询收款单信息", notes = "查询收款单信息")
    public GatherBillV queryById(@PathVariable("gatherBillId") @ApiParam("收款单id") @NotNull(message = "收款单id不能为空") Long gatherBillId,
        @NotBlank(message = "上级收费单元ID(项目ID)不能为空") @ApiParam("上级收费单元ID") @RequestParam("supCpUnitId")String supCpUnitId) {
        return gatherBillAppService.queryById(gatherBillId,supCpUnitId);
    }

    @GetMapping("/detail/{gatherBillId}")
    @ApiOperation(value = "查询收款单详情(包含明细)", notes = "查询收款单详情(包含明细)")
    public GatherBillDetailV getAllDetail(@PathVariable("gatherBillId") @ApiParam("收单id") @NotNull(message = "收单id不能为空") Long gatherBillId,
        @NotBlank(message = "上级收费单元ID(项目ID)不能为空") @ApiParam("上级收费单元ID") @RequestParam("supCpUnitId")String supCpUnitId) {
        return WishareTenantContext.ignore(()->gatherBillAppService.queryDetailById(gatherBillId, supCpUnitId));
    }

    @PostMapping("/list")
    @ApiOperation(value = "根据id集合获取收款单", notes = "根据id集合获取收款单")
    public List<GatherBillV> queryByIdList(@Valid @RequestBody @NotEmpty(message = "账单id不能为空") List<Long> gatherBillIdList,
        @NotBlank(message = "上级收费单元ID(项目ID)不能为空") @ApiParam("上级收费单元ID") @RequestParam("supCpUnitId")String supCpUnitId) {
        return gatherBillAppService.queryByIdList(gatherBillIdList, supCpUnitId);
    }

    @PostMapping("/page")
    @ApiLogCustom(switchClose = true)
    @ApiOperation(value = "分页查询已审核收款单列表", notes = "分页查询已审核收款单列表")
    public PageV<GatherBillV> queryPage(@Validated @RequestBody PageF<SearchF<?>> queryF) {
        PageF<SearchF<?>> queryPermissionF = spacePermissionAppService.getSpacePermissionV(queryF,"b");
        if(Objects.isNull(queryPermissionF)){
            return PageV.of(queryF);
        }
        return gatherBillAppService.getApprovedPage(queryF);
    }

    @PostMapping("/bill/queryPage")
    @ApiOperation(value = "分页查询收款记录", notes = "分页查询收款记录")
    public PageV<GatherBillV> billQueryPage(@Validated @RequestBody PageF<SearchF<?>> form) {
        return gatherBillAppService.billQueryPage(form);
    }

    @PostMapping("/statistics")
    @ApiOperation(value = "统计收付款记录", notes = "统计收付款记录")
    public GatherAndPayStatisticsDto statistics(@RequestBody SearchF<?> form) {
        return gatherBillAppService.statistics(form);
    }
    @PostMapping("/queryInvoiceDetailByGatherBillIds")
    @ApiOperation(value = "根据收款单id查询开票明细", notes = "根据收款单id查询开票明细")
    public List<InvoiceDto> queryInvoiceDetailByGatherBillIds(@Valid @RequestBody @NotEmpty(message = "收款单id不能为空") List<Long> gatherBillIds) {
        return gatherBillAppService.queryInvoiceDetailByGatherBillIds(gatherBillIds);
    }

    @PostMapping("/approve/page")
    @ApiOperation(value = "分页查询未审核收款单列表", notes = "分页查询未审核收款单列表")
    public PageV<GatherBillV> queryApprovePage(@Validated @RequestBody PageF<SearchF<?>> queryF) {
        return gatherBillAppService.queryNotApprovedPage(queryF);
    }

    @PostMapping("/carryover")
    @ApiOperation(value = "收款单结转", notes = "收款单结转")
    public Boolean carryover(@Validated @RequestBody GatherBillCarryoverF billCarryoverF) {
        return gatherBillAppService.carryover(billCarryoverF);
    }

    @PostMapping("/invalid")
    @ApiOperation(value = "收款单作废", notes = "收款单作废")
    public Boolean invalid(@Validated @RequestBody GatherBillInvalidF billInvalidF) {
        return gatherBillAppService.invalid(billInvalidF);
    }

    @PostMapping("/updateApplyInfo")
    @ApiOperation(value = "收款单审核失败更新状态", notes = "收款单审核失败更新状态")
    public Boolean updateApplyInfo(@Validated @RequestBody GatherApplyUpdateF gatherApplyUpdateF) {
        return gatherBillAppService.updateApplyInfo(gatherApplyUpdateF);
    }

    @GetMapping("/reverse/{gatherBillId}")
    @ApiOperation(value = "收款单冲销", notes = "收款单冲销")
    public Boolean reverse(@PathVariable("gatherBillId") @ApiParam("收款单id") @NotNull(message = "收款单id不能为空") Long gatherBillId) {
        return gatherBillAppService.reverse(gatherBillId);
    }

    @PostMapping("/payList")
    @ApiOperation(value = "查询历史缴费记录", notes = "查询历史缴费记录")
    public PageV<PayListDto> payList(@RequestBody @Validated PayListF queryF) {
        return gatherBillAppService.payList(queryF);
    }

    @PostMapping("/payInvoiceList")
    @ApiOperation(value = "查询开票收款单列表", notes = "查询开票收款单列表")
    public PageV<PayListDto> payInvoiceList(@RequestBody @Validated PayInvoiceListF queryF) {
        return gatherBillAppService.payInvoiceList(queryF);
    }

    @ApiOperation(value = "导出收款单", notes = "导出收款单")
    @PostMapping("/export")
    public void export(@RequestBody PageF<SearchF<?>> queryF, HttpServletResponse response) {
        gatherBillAppService.export(queryF, response);
    }

    @PostMapping("/page/inference")
    @ApiOperation(value = "获取账单推凭信息", notes = "获取账单推凭信息")
    public PageV<BillInferenceV> pageBillInferenceInfo( @Validated @RequestBody ListBillInferenceF form) {
        return gatherBillAppService.pageBillInferenceInfo(form, BillTypeEnum.收款单);
    }

    @PostMapping("/list/inference")
    @ApiOperation(value = "获取账单推凭信息", notes = "获取账单推凭信息")
    public List<BillInferenceV> getBillInferenceInfo(@Validated @RequestBody BillInferenceF billInferenceF) {
        return gatherBillAppService.listInferenceInfo(billInferenceF);
    }

    @PostMapping("/list/inference/batch")
    @ApiOperation(value = "获取账单推凭信息", notes = "获取账单推凭信息")
    public List<BillInferenceV> getBillInferenceInfoByIds(@Validated @RequestBody BatchBillInferenceF batchBillInferenceF) {
        return gatherBillAppService.listInferenceInfoByIds(batchBillInferenceF);
    }

    @PostMapping("/apply")
    @ApiOperation(value = "发起审核申请", notes = "发起审核申请")
    public Long apply(@Validated @RequestBody BillApplyF billApplyF) {
        return gatherBillAppService.apply(billApplyF);
    }

    @PostMapping("/batchApply")
    @ApiOperation(value = "批量发起审核申请", notes = "批量发起审核申请")
    public List<Long> batchApply(@Validated @RequestBody BillApplyBatchF billApplyBatchF) {
        return gatherBillAppService.applyBatch(billApplyBatchF);
    }

    @PostMapping("/approve")
    @ApiOperation(value = "审核收款账单", notes = "审核应收账单")
    public Boolean approve(@Validated @RequestBody ApproveGatherBillF approveGatherBillF) {
        return gatherBillAppService.approve(approveGatherBillF);
    }

    @PostMapping("/approve/batch")
    @ApiOperation(value = "批量审核收款单", notes = "批量审核收款单")
    public Boolean approveBatch(@Validated @RequestBody ApproveBatchGatherBillF approveBatchGatherBillF) {
        return gatherBillAppService.approveBatch(approveBatchGatherBillF);
    }


    @ApiOperation(value = "查询收款单详情")
    @PostMapping("/queryGatherDetail")
    public List<GatherDetailInfoF> queryGatherDetail(@RequestBody List<Long> gatherBillIds, @RequestParam String supCpUnitId){
        return billDomainService.queryGatherDetail(gatherBillIds,supCpUnitId);
    }

    @PostMapping("/handleData")
    @ApiOperation(value = "处理数据", notes = "处理数据")
    public Boolean handleData(@NotBlank(message = "上级收费单元ID(项目ID)不能为空") @ApiParam("上级收费单元ID") @RequestParam("supCpUnitId")String supCpUnitId) {
        return gatherBillAppService.handleData(supCpUnitId);
    }

    @PostMapping("/ignoreTenant/bill/queryPage")
    @ApiOperation(value = "分页查询收款记录(收款单维度)", notes = "分页查询收款记录(收款单维度)")
    PageV<GatherBillIgnoreV> queryPageGatherBillIgnore(@RequestBody PageF<SearchF<?>> form){
        return gatherBillAppService.queryPageGatherBillIgnore(form);
    }

    @PostMapping("/gatherDetail/queryPage")
    @ApiOperation(value = "分页查询收款明细记录", notes = "分页查询收款明细记录")
    PageV<GatherDetailV> queryPageGatherDetail(@RequestBody PageF<SearchF<?>> form){
        return gatherBillAppService.queryPageGatherDetail(form);
    }

    @PostMapping("/collect")
    @ApiOperation(value = "收款单其他账户代收")
    public Boolean collect(@Validated @RequestBody GatherCollectF gatherCollectF) {
        return gatherBillAppService.collect(gatherCollectF);
    }

    @PostMapping("/getPaySourceByBillNo")
    @ApiOperation(value = "根据账单编号获取支付来源", notes = "根据账单编号获取支付来源")
    List<GetPaySourceByBillNoV> getPaySourceByBillNo(@Validated @RequestBody GetPaySourceByBillNoF getPaySourceByBillNoF){
        return gatherBillAppService.getPaySourceByBillNo(getPaySourceByBillNoF);
    }

    @PostMapping("/updateGatherInvoiceStatus")
    @ApiOperation(value = "更新收款单及明细开票状态", notes = "更新收款单及明细开票状态")
    public Boolean updateInvoiceStatus(String supCpUnitId){
        gatherBillAppService.updateGatherInvoiceStatus(supCpUnitId);
        return true;
    }

    @PostMapping("/updateAllGatherInvoiceStatus")
    @ApiOperation(value = "更新收款单及明细开票状态(忽略租户)", notes = "更新收款单及明细开票状态(忽略租户)")
    public GatherInvoiceRefreshV updateAllInvoiceStatus(){
        return gatherBillAppService.updateAllGatherInvoiceStatus();
    }

    @PostMapping("/deleteGatherInvoiceStatusCache")
    @ApiOperation(value = "清除更新收款单及明细开票状态缓存", notes = "清除更新收款单及明细开票状态缓存")
    public void deleteGatherInvoiceStatusCache(){
        gatherBillAppService.deleteGatherInvoiceStatusCache();
    }

    @PostMapping("/gatherDetailList")
    @ApiOperation(value = "根据收款明细id集合获取收款信息列表", notes = "根据收款明细id集合获取收款信息列表")
    public List<GatherDetailV> gatherDetailList(@Valid @RequestBody @NotEmpty(message = "收款明细id不能为空") List<Long> gatherDetailIds,
                                           @NotBlank(message = "上级收费单元ID(项目ID)不能为空") @ApiParam("上级收费单元ID") @RequestParam("supCpUnitId")String supCpUnitId) {
        return gatherBillAppService.gatherDetailList(gatherDetailIds, supCpUnitId);
    }

    @ApiOperation(value = "查询收款单可开票金额", notes = "查询收款单可开票金额")
    @PostMapping("/canInvoiceInfo")
    public BillSimpleInfoV canInvoiceInfo(@Validated @RequestBody GatherInvoiceF gatherInvoiceF){
        return gatherBillAppService.canInvoiceInfo(gatherInvoiceF);
    }

    @PostMapping("/getGatherDetails")
    @ApiOperation(value = "根据收款明细ids获取对应收款明细信息", notes = "根据收款明细ids获取对应收款明细信息")
    public List<GatherDetailV> getGatherDetails(@NotEmpty(message = "收款明细id不能为空") @RequestParam("gatherDetailIds") List<Long> gatherDetailIds,
                                                @NotBlank(message = "上级收费单元ID(项目ID)不能为空") @RequestParam("supCpUnitId")String supCpUnitId) {
        return gatherBillAppService.getGatherDetails(gatherDetailIds, supCpUnitId);
    }

    @ApiOperation(value = "根据账单编号列表获取对应收款明细信息", notes = "根据账单编号列表获取对应收款明细信息")
    @PostMapping("/getGatherDetailsByRecIds")
    List<GatherDetailV> getGatherDetailsByRecIds(@NotEmpty(message = "账单编号不能为空") @RequestParam("receivableBillIds") List<Long> receivableBillIds,
                                                 @NotBlank(message = "上级收费单元ID(项目ID)不能为空") @RequestParam("supCpUnitId") String supCpUnitId){
        return gatherBillAppService.getGatherDetailsByRecIds(receivableBillIds, supCpUnitId);
    }


    @PostMapping("/deleteGatherBillDetails")
    @ApiOperation(value = "方圆迁移数据删除收款单接口", notes = "方圆迁移数据删除收款单接口")
    public Boolean deleteGatherBillDetails(@NotEmpty(message = "收款明细id不能为空") @RequestParam("gatherDetailIds") List<Long> gatherDetailIds,
                                                @NotBlank(message = "上级收费单元ID(项目ID)不能为空") @RequestParam("supCpUnitId")String supCpUnitId) {
        return gatherBillAppService.deleteGatherBillDetails(gatherDetailIds, supCpUnitId);
    }


    @GetMapping("getGatherBillId/{advanceBillId}")
    @ApiOperation(value = "查询收款单id", notes = "查询收款单id")
    public Long getGatherBillId(@PathVariable("advanceBillId") @ApiParam("预收账单ID") Long advanceBillId
            ,@RequestParam(value = "supCpUnitId", required = false) @ApiParam("上级收费单元id")  String supCpUnitId) {
        return gatherBillAppService.getGatherBillId(advanceBillId,supCpUnitId);
    }
    // 新增收款单标记推送财务 取消标记财务云接口

    @ApiOperation(value = "收款单 添加标记 取消标记接口", notes = "收款单 添加标记 取消标记接口")
    @PostMapping("/editGatherBillFlag")
    public Boolean editGatherBillFlag(@Validated @RequestBody BillFlagF billFlagF){

        return gatherBillAppService.editGatherBillFlag(billFlagF);
    }


}
