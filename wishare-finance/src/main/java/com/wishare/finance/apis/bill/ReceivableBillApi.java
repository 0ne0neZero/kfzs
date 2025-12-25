package com.wishare.finance.apis.bill;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.wishare.finance.apis.common.DeveloperPayFilterCondition;
import com.wishare.finance.apps.model.bill.fo.*;
import com.wishare.finance.apps.model.bill.vo.*;
import com.wishare.finance.apps.model.configure.chargeitem.vo.ChargeItemV;
import com.wishare.finance.apps.service.bill.ReceivableBillAppService;
import com.wishare.finance.domains.bill.aggregate.data.EnvData;
import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import com.wishare.finance.domains.bill.dto.*;
import com.wishare.finance.domains.bill.entity.ReceivableBill;
import com.wishare.finance.infrastructure.conts.EnvConst;
import com.wishare.finance.infrastructure.support.tenant.WishareTenantContext;
import com.wishare.owl.util.Assert;
import com.wishare.starter.ThreadPoolManager;
import com.wishare.starter.annotations.ApiLogCustom;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.exception.BizException;
import com.wishare.starter.helpers.RedisHelper;
import com.wishare.starter.interfaces.ApiBase;
import com.wishare.starter.utils.ThreadLocalUtil;
import com.wishare.tools.starter.fo.search.SearchF;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = {"应收账单"})
@Validated
@RestController
@RequestMapping("/receivable")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class ReceivableBillApi implements ApiBase {

    private final ReceivableBillAppService receivableBillAppService;

    @PostMapping("/add")
    @ApiOperation(value = "新增应收账单", notes = "新增应收账单")
    public ReceivableBillDetailV add(@Validated @RequestBody ImportReceivableBillF addReceivableBillF){
       return receivableBillAppService.addBill(addReceivableBillF, ReceivableBillDetailV.class);
    }

    @PostMapping("/add/batch")
    @ApiOperation(value = "批量新增应收账单", notes = "批量新增应收账单")
    public List<ReceivableBillDetailV> addBatch(@Validated @RequestBody @Size(max = 1000, min = 1,
            message = "应收账单导入列表大小不允许，仅允许区间为[1,1000]") List<ImportReceivableBillF> addReceivableBillFList) {
        return receivableBillAppService.addBatchBill(addReceivableBillFList, ReceivableBillDetailV.class);
    }

    @PostMapping("/syncBatch/byCommunity")
    @ApiOperation(value = "按项目同步批量新增或更新应收账单")
    public Boolean syncBatchByCommunity(@Validated @RequestBody List<ReceivableBill> receivableBillList) {
        return receivableBillAppService.syncBatchByCommunity(receivableBillList);
    }

    @PostMapping("/sync/batch")
    @ApiOperation(value = "批量同步应收账单", notes = "批量同步应收账单")
    public Boolean addReceivableBatch(@Validated @RequestBody List<ReceivableBill> receivableBillList) {
        return receivableBillAppService.addReceivableBatch(receivableBillList);
    }

    @PostMapping("/syncBatch/updateByCommunity")
    @ApiOperation(value = "按项目同步批量更新应收账单", notes = "按项目同步批量更新应收账单")
    public Boolean syncBatchUpdateByCommunity(@Validated @RequestBody List<ReceivableBill> receivableBillList) {
        return receivableBillAppService.syncBatchUpdateByCommunity(receivableBillList);
    }

    @PostMapping("/sync/del")
    @ApiOperation(value = "批量删除收费清单", notes = "批量删除收费清单")
    public Boolean deleteBillById(@RequestHeader("tenantId") String tenantId,@RequestParam("communityId") String communityId,@RequestBody List<ReceivableBill> list) {
        return receivableBillAppService.deleteBillById(communityId,list);
    }

    @PostMapping("/add/periods")
    @ApiOperation(value = "批量新增周期性应收账单", notes = "批量新增周期性应收账单")
    public Boolean addPeriodBill(@Validated @RequestBody List<AddPeriodicReceivableBillF> createPeriodicReceivableBillList) {
        return receivableBillAppService.addBatchPeriodicReceivableBill(createPeriodicReceivableBillList);
    }

    @PostMapping("/import")
    @ApiOperation(value = "批量导入应收账单", notes = "批量导入应收账单")
    public List<ImportReceivableBillV> importBill(@Validated @RequestBody @Size(max = 1000, min = 1,
            message = "应收账单导入列表大小不允许，仅允许区间为[1,1000]") List<ImportReceivableBillF> importReceivableBillFS) {
        return receivableBillAppService.importBill(importReceivableBillFS, ImportReceivableBillV.class);
    }

    @PostMapping("/importRecord")
    @ApiOperation(value = "批量补录导入应收账单", notes = "批量补录导入应收账单")
    public List<ImportReceivableBillV> importRecordBill(@Validated @RequestBody @Size(max = 1000, min = 1,
            message = "应收账单补录导入列表大小不允许，仅允许区间为[1,1000]") List<ImportRecordBillF> importReceivableBillFS) {
        return receivableBillAppService.importRecordBill(importReceivableBillFS, ImportReceivableBillV.class);
    }

    @PostMapping("/approve/batch")
    @ApiOperation(value = "批量审核应收账单", notes = "批量审核应收账单")
    public Boolean approveBatch(@Validated @RequestBody ApproveBatchReceivableBillF approveBatchReceivableBillF) {
        return receivableBillAppService.approveBatch(approveBatchReceivableBillF, BillTypeEnum.应收账单.getCode());
    }

    @PostMapping("/approve")
    @ApiOperation(value = "审核应收账单", notes = "审核应收账单")
    public Boolean approve(@Validated @RequestBody ApproveReceivableBillF approveReceivableBillF) {
        return receivableBillAppService.approve(approveReceivableBillF);
    }

    @DeleteMapping("/{receivableBillId}")
    @ApiOperation(value = "删除应收账单", notes = "删除应收账单")
    public Boolean deleteById(@PathVariable("receivableBillId") @ApiParam("应收账单id") @NotNull(message = "应收账单id不能为空") Long receivableBillId
        ,@NotBlank(message = "上级收费单元id不能为空") @RequestParam("supCpUnitId") @ApiParam("上级收费单元id")  String supCpUnitId) {
        return receivableBillAppService.delete(receivableBillId, supCpUnitId);
    }

    @DeleteMapping("/delete/batch")
    @ApiOperation(value = "批量删除应收账单", notes = "批量删除应收账单")
    public BillBatchResultDto deleteBatch(@Validated @RequestBody DeleteBatchReceivableBillF deleteBatchReceivableBillF) {
        return receivableBillAppService.deleteBatch(deleteBatchReceivableBillF, BillTypeEnum.应收账单.getCode());
    }

    @PostMapping("/apply")
    @ApiOperation(value = "发起审核申请", notes = "发起审核申请")
    public Long apply(@Validated @RequestBody BillApplyF billApplyF) {
        return receivableBillAppService.apply(billApplyF);
    }

    @PostMapping("/getApplyInfo")
    @ApiOperation(value = "获取审核记录", notes = "获取审核记录")
    public List<BillApproveV> getApplyInfo(@Validated @RequestBody BillApplyInfoF billApplyInfoF) {
        return receivableBillAppService.getApplyInfo(billApplyInfoF);
    }


    @PostMapping("/updateApply")
    @ApiOperation(value = "更新审核记录", notes = "更新审核记录")
    public Long updateApply(@Validated @RequestBody BillApplyUpdateF billApplyF) {
        return receivableBillAppService.updateApply(billApplyF);
    }

    @PostMapping("/updateBatchApply")
    @ApiOperation(value = "批量更新审核记录", notes = "更新审核记录")
    public Boolean updateBatchApplyByIds(@Validated @RequestBody BillBatchApplyUpdateF billApplyF) {
        return receivableBillAppService.updateBatchApplyByIds(billApplyF);
    }

    @PostMapping("/history/approve")
    @ApiOperation(value = "根据查询条件获取历史审核记录", notes = "根据查询条件获取历史审核记录")
    public List<BillApproveV> approveHistory(@Validated @RequestBody ApproveHistoryF approveHistoryF) {
        return receivableBillAppService.approveHistory(approveHistoryF);
    }

    @PostMapping("/apply/batch")
    @ApiOperation(value = "批量发起审核申请", notes = "批量发起审核申请")
    public Boolean applyBatch(@Validated @RequestBody BillApplyBatchF billApplyBatchF) {
        return receivableBillAppService.applyBatch(billApplyBatchF);
    }

    @PostMapping("/deapprove")
    @ApiOperation(value = "反审核应收账单", notes = "反审核应收账单")
    public Boolean deapprove(@RequestParam("billId") @ApiParam("应收账单id") @NotNull(message = "应收账单id不能为空") Long receivableBillId
        ,@NotBlank(message = "上级收费单元id不能为空") @RequestParam("supCpUnitId") @ApiParam("上级收费单元id")  String supCpUnitId) {
        return receivableBillAppService.deapprove(receivableBillId,supCpUnitId);
    }

    @PostMapping("/freeze")
    @ApiOperation(value = "冻结应收账单", notes = "冻结应收账单")
    public Boolean freeze(@RequestParam("billId") @ApiParam("应收账单id") @NotNull(message = "应收账单id不能为空") Long receivableBillId
            ,@NotBlank(message = "上级收费单元id不能为空") @RequestParam("supCpUnitId") @ApiParam("上级收费单元id")  String supCpUnitId) {
        return receivableBillAppService.freeze(receivableBillId,supCpUnitId);
    }

    @PostMapping("/freeze/batch")
    @ApiOperation(value = "批量冻结应收账单", notes = "批量冻结应收账单")
    public Boolean freezeBatch(@Validated @RequestBody FreezeBatchF freezeBatchF) {
        return receivableBillAppService.freezeBatch(freezeBatchF, BillTypeEnum.应收账单.getCode());
    }

    @PostMapping("/unfreeze/batch")
    @ApiOperation(value = "批量解冻应收账单", notes = "批量解冻应收账单")
    public Boolean unfreezeBatch(@Validated @RequestBody UnFreezeBatchF unFreezeBatchF) {
        return receivableBillAppService.unfreezeBatch(unFreezeBatchF);
    }

    @PostMapping("/freeze/batchAddReason")
    @ApiOperation(value = "批量冻结应收账单并添加冻结原因", notes = "批量冻结应收账单并添加冻结原因")
    public Boolean freezeBatchAddReason(@Validated @RequestBody FreezeBatchF freezeBatchF,@RequestParam("type") @ApiParam("冻结类型") @NotNull(message = "冻结类型不能为空") Integer type) {
        return receivableBillAppService.freezeBatchAddReason(freezeBatchF, BillTypeEnum.应收账单.getCode(),type);
    }

    @PostMapping("/page")
    @ApiLogCustom(switchClose = true)
    @ApiOperation(value = "分页查询应收账单列表", notes = "分页查询应收账单列表")
    public PageV<ReceivableBillPageV> getPage(@Validated @RequestBody PageF<SearchF<?>> queryF) {
        return receivableBillAppService.getPage(queryF, ReceivableBillPageV.class);
    }

    @PostMapping("/common/page")
    @ApiLogCustom(switchClose = true)
    @ApiOperation(value = "绿城poc使用分页查询应收账单列表", notes = "绿城poc使用分页查询应收账单列表")
    public PageV<ReceivableBillPageV> getCommonPage(@Validated @RequestBody PageF<SearchF<?>> queryF) {
        if (EnvConst.YUANYANG.equals(EnvData.config)) {
            DeveloperPayFilterCondition.handleDeveloperPay(queryF);
        }

        return receivableBillAppService.getCommonPage(queryF);
    }

    @PostMapping("/count")
    @ApiOperation(value = "查询应收账单数量", notes = "查询应收账单数量")
    public Integer getCount(@Validated @RequestBody PageF<SearchF<?>> queryF) {
        return receivableBillAppService.queryBillCountByPage(queryF);
    }

    @GetMapping("/{receivableBillId}")
    @ApiLogCustom(switchClose = true)
    @ApiOperation(value = "查询应收账单详情", notes = "查询应收账单详情")
    public ReceivableBillDetailV getById(@PathVariable("receivableBillId") @ApiParam("应收账单id") @NotNull(message = "应收账单id不能为空") Long receivableBillId
            ,@NotBlank(message = "上级收费单元id不能为空") @RequestParam("supCpUnitId")  @ApiParam("上级收费单元id")  String supCpUnitId) {
        return WishareTenantContext.ignore(()-> receivableBillAppService.getById(receivableBillId, ReceivableBillDetailV.class,supCpUnitId));
    }

    @GetMapping("/getByChargeNcId")
    @ApiOperation(value = "查询应收账单详细信息", notes = "查询应收账单详细信息")
    public List<ReceivableBill> getByChargeNcId(@RequestHeader("tenantId") String tenantId ,@RequestParam("chargeNcId") String chargeNcId,@RequestParam(value = "communityId") String communityId) {
        return receivableBillAppService.getByChargeNcId(chargeNcId,communityId);
    }

    @GetMapping("/apply/detail")
    @ApiLogCustom(switchClose = true)
    @ApiOperation(value = "查询应收账单申请详情", notes = "查询应收账单申请详情")
    public ReceivableBillApplyDetailV getApplyDetailById(@RequestParam("billId") @ApiParam("应收账单id") @NotNull(message = "应收账单id不能为空") Long receivableBillId
            ,@RequestParam("supCpUnitId") @ApiParam("上级收费单元id")  String supCpUnitId) {
        return receivableBillAppService.getWithApproving(receivableBillId, ReceivableBillApplyDetailV.class,supCpUnitId);
    }

    @PostMapping("/groups")
    @ApiOperation(value = "分组分页查询应收账单列表", notes = "分组分页查询应收账单列表")
    public PageV<ReceivableBillGroupDetailDto> getGroupPage(@Validated @RequestBody PageF<SearchF<?>> queryF) {
        return receivableBillAppService.getGroupPage(queryF, 0, true);
    }

    @PostMapping("/approve/groups")
    @ApiOperation(value = "分组分页查询应收账单审核列表", notes = "分组分页查询应收账单审核列表")
    public PageV<ReceivableBillGroupDetailDto> getGroupApprovePage(@Validated @RequestBody PageF<SearchF<?>> queryF) {
        return receivableBillAppService.getGroupPage(queryF, 1, true);
    }

    @PostMapping("/delete/init")
    @ApiOperation(value = "根据查询条件删除初始审核账单", notes = "根据查询条件删除初始审核账单")
    public Boolean deleteInitBill(@Validated @RequestBody PageF<SearchF<?>> queryF) {
        return receivableBillAppService.deleteInitBill(queryF);
    }

    @PostMapping("/query/parentData")
    @ApiOperation(value = "分页获取父级节点数据", notes = "分页获取父级节点数据")
    public PageV<ReceivableBillGroupDetailDto> queryParentData(@Validated @RequestBody PageF<SearchF<?>> queryF) {
        return receivableBillAppService.queryParentData(queryF);
    }

    @PostMapping("/query/detailData")
    @ApiOperation(value = "分页获取应收账单导出明细数据", notes = "分页获取应收账单导出明细数据")
    public PageV<ReceivableBillGroupDetailDto> queryDetailData(@Validated @RequestBody PageF<SearchF<?>> queryF) {
        return receivableBillAppService.queryDetailData(queryF);
    }

    @GetMapping("/detail/{receivableBillId}")
    @ApiLogCustom(switchClose = true)
    @ApiOperation(value = "查询应收账单详情(包含明细)", notes = "查询应收账单详情(包含明细)")
    public ReceivableBillAllDetailV getAllDetail(@PathVariable("receivableBillId") @ApiParam("应收账单id") @NotNull(message = "应收账单id不能为空") Long receivableBillId
            ,@NotBlank(message = "上级收费单元id不能为空") @RequestParam("supCpUnitId") @ApiParam("上级收费单元id")  String supCpUnitId) {
        return receivableBillAppService.getAllDetail(receivableBillId, ReceivableBillAllDetailV.class,supCpUnitId);
    }

    @ApiOperation(value = "根据账单ids获取应收账单信息", notes = "根据账单ids获取应收账单信息")
    @ApiLogCustom(switchClose = true)
    @PostMapping("/batch/receivableBillInfo")
    public List<ReceivableBillMoreInfoDto> receivableBillInfo(@Validated @RequestBody ReceivableBillInfoF form){
        return receivableBillAppService.receivableBillInfo(form.getBillIds(), form.getSupCpUnitId());
    }

    @ApiOperation(value = "（通用）根据账单id集合获取应收账单信息", notes = "（通用）根据账单id集合获取应收账单信息")
    @ApiLogCustom(switchClose = true)
    @PostMapping("/billInfo/idList")
    public List<ReceivableBillDetailV> getBillInfoByIds(@Validated @RequestBody @NotEmpty(message = "账单id不能为空")List<Long> billIds
        ,@NotBlank(message = "上级收费单元id不能为空") @RequestParam("supCpUnitId") @ApiParam("上级收费单元id")  String supCpUnitId){
        return receivableBillAppService.getBillInfoByIds(billIds, ReceivableBillDetailV.class, supCpUnitId);
    }

    @ApiOperation(value = "根据账单ids获取应收账单结算详情", notes = "根据账单ids获取应收账单结算详情")
    @PostMapping("/settleDetail")
    public SettleDetailDto settleDetail(@Validated @RequestBody SettleDetailF form) {
        return receivableBillAppService.settleDetail(form.getBillIds(),form.getSupCpUnitId());
    }

    @ApiOperation(value = "根据账单ids获取结算记录", notes = "根据账单ids获取结算记录")
    @PostMapping("/getBillSettle")
    public List<BillSettleDto> getBillSettle(@Validated @RequestBody List<Long> billIds
        ,@NotBlank(message = "上级收费单元id不能为空") @RequestParam("supCpUnitId") @ApiParam("上级收费单元id")  String supCpUnitId){
        return receivableBillAppService.getBillSettle(billIds,supCpUnitId);
    }

    @ApiOperation(value = "批量结算", notes = "批量结算")
    @PostMapping("/settle/batch")
    public Boolean settleBatch(@Validated @RequestBody List<AddBillSettleF> form) {
        if (CollectionUtils.isEmpty(form)) {
            return false;
        }
        return receivableBillAppService.settleBatch(form) != null;
    }

    @ApiOperation(value = "批量结算(返回收款单id)", notes = "(返回收款单id)")
    @PostMapping("/settle/batchReturn")
    public Long settleBatchReturn(@Validated @RequestBody List<AddBillSettleF> form) {
        Assert.validate(() -> !CollectionUtils.isEmpty(form), () -> BizException.throw400("批量结算缺少传参"));
        return receivableBillAppService.settleBatch(form);
    }

    @ApiOperation(value = "补录批量结算(返回收款单id)", notes = "(返回收款单id)")
    @PostMapping("/settle/batchRecordReturn")
    public Map<String, List<RecordImportChargeBillV>> settleBatchRecordReturn(@Validated @RequestBody List<List<AddBillSettleF>> form) {
        return receivableBillAppService.settleBatchRecordReturn(form,"", getUserName().get());
    }

    @ApiOperation(value = "补录批量结算(获取缓存标识)", notes = "(返回收款单id)")
    @PostMapping("/settle/batchRecordReturnCache")
    public String batchRecordReturnCache(@Validated @RequestBody List<List<AddBillSettleF>> form,@RequestParam String pcId) {
        // 确保同批次不会重复
        //锁不存在则获取锁
        try (Jedis jedis = RedisHelper.jedisPool.getResource()){
            if (jedis.setnx(pcId+":unique", "180") != 1) {
                Thread.sleep(10000);
                return pcId+":unique";
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        // 增加过期时间 3分钟
        RedisHelper.setGAtExpire(pcId,3 * 60, Boolean.FALSE.toString());
        // 异步
        ThreadPoolManager.execute(ThreadLocalUtil.curIdentityInfo(), null,
                (e) -> {
                    Map<String, List<RecordImportChargeBillV>> stringListMap = receivableBillAppService.settleBatchRecordReturn(form, pcId, getUserName().get());
                    RedisHelper.setG(pcId + ":rps", JSON.toJSONString(stringListMap));
                    RedisHelper.setG(pcId, "true");
                });
        return pcId;
    }



    @ApiOperation(value = "批量发起开票", notes = "发起开票")
    @PostMapping("/invoice/batch")
    public Boolean invoiceBatch(@Validated @RequestBody List<Long> billIds,
            @NotBlank(message = "上级收费单元id不能为空") @RequestParam("supCpUnitId") @ApiParam("上级收费单元id")  String supCpUnitId){
        return receivableBillAppService.invoiceBatch(billIds,supCpUnitId,null);
    }

    @ApiOperation(value = "批量完成开票", notes = "发起开票")
    @PostMapping("/invoice/finish/batch")
    public Boolean finishInvoiceBatch(@Validated @RequestBody List<FinishInvoiceF> finishInvoiceFList) {
        return receivableBillAppService.finishInvoiceBatch(finishInvoiceFList);
    }

    @ApiOperation(value = "批量作废,红冲开票金额", notes = "批量作废开票金额")
    @PostMapping("/invoice/void/batch")
    public Boolean invoiceVoidBatch(@Validated @RequestBody List<InvoiceVoidBatchF> invoiceVoidBatchFList) {
        return receivableBillAppService.invoiceVoidBatch(invoiceVoidBatchFList);
    }

    @PostMapping("/hand/batch")
    @ApiOperation(value = "批量交账", notes = "批量交账")
    public Boolean bathHandAdvanceBill(@RequestBody BillHandBatchF billHandBatchF) {
        return receivableBillAppService.handBatch(billHandBatchF, BillTypeEnum.应收账单.getCode());
    }

    @PostMapping("/rehand")
    @ApiOperation(value = "反交账", notes = "反交账")
    public Boolean handReversal(@RequestParam("billId") @ApiParam("应收账单id") @NotNull(message = "应收账单id不能为空") Long receivableBillId
            ,@NotBlank(message = "上级收费单元id不能为空") @RequestParam("supCpUnitId") @ApiParam("上级收费单元id")  String supCpUnitId) {
        return receivableBillAppService.handReversal(receivableBillId,supCpUnitId);
    }

    @GetMapping("/onAccount")
    @ApiOperation(value = "挂账", notes = "挂账")
    public Boolean onAccount(@RequestParam("billId") @ApiParam("应收账单id") @NotNull(message = "应收账单id不能为空") Long receivableBillId
            ,@NotBlank(message = "上级收费单元id不能为空") @RequestParam("supCpUnitId") @ApiParam("上级收费单元id")  String supCpUnitId){
        return receivableBillAppService.onAccount(receivableBillId,supCpUnitId);
    }

    @GetMapping("/writeOff")
    @ApiOperation(value = "销账", notes = "销账")
    public Boolean writeOff(@RequestParam("billId") @ApiParam("应收账单id") @NotNull(message = "应收账单id不能为空") Long billId
            ,@NotBlank(message = "上级收费单元id不能为空") @RequestParam("supCpUnitId") @ApiParam("上级收费单元id")  String supCpUnitId){
        return receivableBillAppService.writeOff(billId,supCpUnitId);
    }

    @GetMapping("/reverse")
    @ApiOperation(value = "冲销", notes = "冲销")
    public Boolean reverse(@RequestParam("billId") @ApiParam("应收账单id") @NotNull(message = "应收账单id不能为空") Long receivableBillId,
                           @RequestParam("extField1") @ApiParam("冲销时如果需要生成新的应收传入：ReversedInitBill）") String extField1
            ,@NotBlank(message = "上级收费单元id不能为空") @RequestParam("supCpUnitId") @ApiParam("上级收费单元id")  String supCpUnitId){
        return receivableBillAppService.reverse(receivableBillId,extField1,supCpUnitId);
    }

    @GetMapping("/roback/reverse")
    @ApiOperation(value = "回滚冲销", notes = "回滚冲销")
    public Boolean robackReverse(@RequestParam("billId") @ApiParam("应收账单id") @NotNull(message = "应收账单id不能为空") Long receivableBillId
        ,@NotBlank(message = "上级收费单元id不能为空") @RequestParam("supCpUnitId") @ApiParam("上级收费单元id")  String supCpUnitId){
        return receivableBillAppService.robackReverse(receivableBillId, supCpUnitId);
    }

    @PostMapping("/list/inference")
    @ApiOperation(value = "获取账单推凭信息", notes = "获取账单推凭信息")
    public List<BillInferenceV> getBillInferenceInfo(@Validated @RequestBody BillInferenceF billInferenceF) {
        return receivableBillAppService.listInferenceInfo(billInferenceF);
    }

    @PostMapping("/list/inference/batch")
    @ApiOperation(value = "获取账单推凭信息", notes = "获取账单推凭信息")
    public List<BillInferenceV> getBillInferenceInfoByIds(@Validated @RequestBody BatchBillInferenceF batchBillInferenceF) {
        return receivableBillAppService.listInferenceInfoByIds(batchBillInferenceF);
    }

    @PostMapping("/page/inference")
    @ApiOperation(value = "获取账单推凭信息", notes = "获取账单推凭信息")
    public PageV<BillInferenceV> pageBillInferenceInfo(@Validated @RequestBody ListBillInferenceF form) {
        return receivableBillAppService.pageBillInferenceInfo(form, BillTypeEnum.应收账单);
    }

    @PostMapping("/bank/settle")
    @ApiOperation(value = "银行托收清结", notes = "银行托收清结")
    public Boolean approveBatch(@Validated @RequestBody BankSettleF bankSettleF) {
        return receivableBillAppService.bankSettle(bankSettleF);
    }

    @PostMapping("/reconcile/batch")
    @ApiOperation(value = "批量对账", notes = "批量对账")
    public Boolean reconcileBatch(@Validated @RequestBody List<ReconcileBatchF> reconcileBatchFS) {
        return receivableBillAppService.reconcileBatch(reconcileBatchFS);
    }

    @PostMapping("/page/tenant")
    @ApiOperation(value = "分页查询应收账单列表（无租户隔离）", notes = "分页查询应收账单列表（无租户隔离）")
    PageV<ReceivableBillPageV> getPageNoTenantLine(@Validated @RequestBody PageF<SearchF<?>> queryF) {
        return receivableBillAppService.getPageNoTenantLine(queryF, ReceivableBillPageV.class);
    }

    @PostMapping("/carryover")
    @ApiOperation(value = "账单结转", notes = "账单结转")
    public Boolean carryover(@Validated @RequestBody ReceivableBillCarryoverF billCarryoverF) {
        return receivableBillAppService.carryover(billCarryoverF);
    }

    @PostMapping("/adjust")
    @ApiOperation(value = "账单调整", notes = "账单调整")
    public Boolean adjust(@Validated @RequestBody ReceivableBillAdjustF billAdjustF) {
        return receivableBillAppService.adjust(billAdjustF);
    }

    @PostMapping("/refund")
    @ApiOperation(value = "账单退款", notes = "账单退款")
    public Boolean refund(@Validated @RequestBody ReceivableBillRefundF receivableBillRefundF) {
        return receivableBillAppService.refund(receivableBillRefundF);
    }

    @PostMapping("/invalid")
    @ApiOperation(value = "作废账单", notes = "作废账单")
    public Boolean invalid(@Validated @RequestBody ReceivableBillInvalidF receivableBillInvalidF) {
        return receivableBillAppService.invalid(receivableBillInvalidF);
    }

    @PostMapping("/invalid/batch")
    @ApiOperation(value = "批量作废", notes = "批量作废")
    public BillBatchResultDto invalidBatch(@Validated @RequestBody
                                               @ApiParam("账单id")
                                               @Size(max = 1000, min = 1, message = "id集合大小不允许，仅允许区间为[1,1000]") List<Long> billIdList
        ,@NotBlank(message = "上级收费单元id不能为空") @RequestParam("supCpUnitId") @ApiParam("上级收费单元id")  String supCpUnitId) {
        return receivableBillAppService.invalidBatch(billIdList, supCpUnitId);
    }

    @PostMapping("/receivableRooms")
    @ApiOperation(value = "查询应收收费信息", notes = "查询应收收费信息")
    public PageV<ReceivableRoomsV> receivableRooms(@RequestBody @Validated ReceivableRoomsPageF pageF) {
        return receivableBillAppService.receivableRooms(pageF);
    }

    @PostMapping("/queryCanAdvanceRooms")
    @ApiOperation(value = "查询可预收房间信息", notes = "查询可预收房间信息")
    public PageV<ReceivableRoomsDto> queryCanAdvanceRooms(@RequestBody @Validated ReceivableRoomsPageF pageF) {
        if(StringUtils.isBlank(pageF.getTenantId())){
            pageF.setTenantId(getTenantId().get());
        }
        return receivableBillAppService.queryCanAdvanceRooms(pageF);
    }

    @PostMapping("/receivableBills")
    @ApiOperation(value = "查询房间应收账单列表", notes = "查询房间应收账单列表")
    public List<ReceivableBillsV> receivableBills(@RequestBody @Validated ReceivableBillF queryF) {
        return receivableBillAppService.receivableBills(queryF);
    }

    @GetMapping("/info/billNo")
    @ApiOperation(value = "根据账单编号查询账单信息", notes = "根据账单编号查询账单信息")
    public BillOjV getBillInfoByBillNo(@RequestParam("billNo") @ApiParam("账单编号") @NotNull(message = "账单编号不能为空")String billNo,
                                       @NotBlank(message = "上级收费单元id不能为空") @RequestParam("supCpUnitId") @ApiParam("上级收费单元id")  String supCpUnitId ){
        return receivableBillAppService.getBillInfoByBillNo(billNo,supCpUnitId);
    }

    @PostMapping("/history")
    @ApiOperation(value = "分页查询应收账单列表（给第三方提供数据）", notes = "分页查询应收账单列表（给第三方提供数据）")
    public PageV<HistoryV> history(@RequestBody @Validated HistoryF queryF) {
        return receivableBillAppService.history(queryF);
    }

    @ApiOperation(value = "导出应收单", notes = "导出收款单")
    @PostMapping("/export")
    public void export(@RequestBody PageF<SearchF<?>> queryF, HttpServletResponse response) {
        receivableBillAppService.export(queryF, response);
    }

    @ApiOperation(value = "编辑应收账单", notes = "编辑应收账单")
    @PostMapping("/edit")
    public Boolean editRec(@Validated @RequestBody EditBillF editBillF) {
        return receivableBillAppService.editRec(editBillF);
    }



    @ApiOperation(value = "调整应收账单", notes = "编辑应收账单")
    @PostMapping("/editReading")
    public Boolean editBillDeviceReading(@Validated @RequestBody EditBillReadingF editBillReadingF) {
        return receivableBillAppService.editBillDeviceReading(editBillReadingF);
    }

    @ApiOperation(value = "银行签约对应编辑应收账单", notes = "银行签约对应编辑应收账单")
    @PostMapping("/editRecForBankSign")
    public Boolean editRecForBankSign(@Validated @RequestBody EditBillForBankSignF editBillF) {
        return receivableBillAppService.editRecForBankSign(editBillF);
    }

    @PostMapping("/query/maxEndTime")
    @ApiOperation(value = "查询应收账单最大账单结束时间", notes = "查询应收账单最大账单结束时间")
    public List<ReceivableMaxEndTimeV> queryMaxEndTime(@Validated @RequestBody ReceivableMaxEndTimeBillF maxEndTimeBillF) {
        return receivableBillAppService.queryMaxEndTime(maxEndTimeBillF);
    }

    @PostMapping("/query/IntervalBill")
    @ApiOperation(value = "查询区间账单信息")
    public List<ReceivableIntervalBillV> queryIntervalBill(@Validated @RequestBody ReceivableIntervalBillF query){
        return receivableBillAppService.queryIntervalBill(query);
    }


    @ApiOperation(value = "根据ID查询账单状态基础信息详情", notes = "根据ID查询账单状态基础信息详情")
    @PostMapping("/queryStatus")
    public BillStatusDetailVo statusDetailBill(@Validated @RequestBody BillDetailF billDetailF){
        if(StringUtils.isBlank(billDetailF.getSupCpUnitId())) {
            throw new IllegalArgumentException("上级收费单元ID不能为空!");
        }
        return receivableBillAppService.statusDetailBill(billDetailF);
    }

    @ApiOperation(value = "批量冻结功能(跳收)", notes = "批量冻结功能(跳收)")
    @PostMapping("/batch/jump")
    public Boolean batchJump(@Validated @RequestBody BillFreezeF freeze) {
        String tenantId = getTenantId().get();
        return receivableBillAppService.batchJump(freeze, tenantId);
    }

    @ApiOperation(value = "批量冻结功能(银行代扣)", notes = "批量冻结功能(银行代扣)")
    @PostMapping("/batch/bankWithHold")
    public Boolean batchBankWithHold(@Validated @RequestBody BillFreezeF freeze) {
        return receivableBillAppService.batchBankWithHold(freeze);
    }
    @ApiOperation(value = "批量冻结明细", notes = "批量冻结明细")
    @PostMapping("/getJump")
    public PageV<BillFreezeV> getJump(@RequestBody PageF<SearchF<?>> f) {
        return receivableBillAppService.getJump(f);
    }

    @PostMapping("/getReceivableBillByRoomIds")
    @ApiOperation(value = "获取房间号下所有应收账单", notes = "获取房间号下所有应收账单")
    public List<ReceivableBillDetailV> getReceivableBillByRoomIds(@RequestBody List<Long> roomIds) throws JsonProcessingException {
        return receivableBillAppService.getAdvanceBillByRoomIds(roomIds);
    }

    @PostMapping("/jump/record/page")
    @ApiOperation(value = "分页bpm跳收记录查询", notes = "分页bpm跳收记录查询")
    public PageV<JumpRecordPageV> jumpRecordPage(@Validated @RequestBody PageF<SearchF<?>> queryF) {
        return receivableBillAppService.jumpRecordPage(queryF, JumpRecordPageV.class);
    }

    @PostMapping("/getFreezeAmount")
    @ApiOperation(value = "计算冻结总额", notes = "计算冻结总额")
    public Long getFreezeAmount(@Validated @RequestBody ReceivableBillFreezeF form) {
        return receivableBillAppService.getFreezeAmount(form.getBillIds(), form.getSupCpUnitId());
    }

    @PostMapping("/relieveFreeze")
    @ApiOperation(value = "解除冻结", notes = "解除冻结")
    public Boolean relieveFreeze(@Validated @RequestBody ReceivableBillReFreezeF form) {
        return receivableBillAppService.relieveFreeze(form.getId(),form.getBillNos(), form.getSupCpUnitId());
    }

    @ApiOperation(value = "拆分应收账单", notes = "拆分应收账单")
    @PostMapping("/billSplit")
    Boolean billSplit(@Validated @RequestBody BillExecuteSplitF billExecuteSplitF) {
        return receivableBillAppService.billSplit(billExecuteSplitF);
    }

    @ApiOperation(value = "拆分应收账单", notes = "拆分应收账单")
    @PostMapping("/billSplitChargeItem")
    Boolean billSplit(@RequestBody List<ReceivableBillDetailV> splitBills) {
        return receivableBillAppService.billSplitChargeItem(splitBills);
    }

    @ApiOperation(value = "拆分(部分结算)账单", notes = "拆分(部分结算)账单")
    @PostMapping("/splitPartialSettlement")
    Boolean splitPartialSettlement(@Validated @RequestBody BillExecuteSplitF billExecuteSplitF) {
        return receivableBillAppService.splitPartialSettlement(billExecuteSplitF);
    }

    @PostMapping("/updateById")
    @ApiOperation(value = "更新帐单表", notes = "更新帐单表")
    Boolean updateById(@RequestHeader("tenantId") String tenantId, @Validated @RequestBody ReceivableBill receivableBill){
        return receivableBillAppService.updateById(receivableBill);
    }

    @PostMapping("/batchUpdateGatherBillById")
    @ApiOperation(value = "更新帐单主表", notes = "更新帐单主表")
    int batchUpdateGatherBillById(@RequestHeader("tenantId") String tenantId, @Validated @RequestBody List<UpdateGatherBillF> updateGatherBillF){
        return receivableBillAppService.batchUpdateGatherBillById(updateGatherBillF);
    }

    @PostMapping("/approve/queryPage")
    @ApiOperation(value = "分页查询应收审核账单列表", notes = "分页查询应收审核账单列表")
    public PageV<ReceivableBillGroupDetailDto> queryPageApprove(@Validated @RequestBody PageF<SearchF<?>> queryF) {
        return receivableBillAppService.queryPageApprove(queryF);
    }


    @PostMapping("/common/bill/page")
    @ApiLogCustom(switchClose = true)
    @ApiOperation(value = "通用分页查询应收账单列表", notes = "通用分页查询应收账单列表")
    public PageV<ReceivableBillPageV> queryCommonBillByPageField(@Validated @RequestBody PageF<SearchF<?>> queryF) {
        return receivableBillAppService.queryCommonBillByPageField(queryF);
    }

    @PostMapping("/charge/approve/page")
    @ApiLogCustom(switchClose = true)
    @ApiOperation(value = "分页查询变更审核应收账单列表", notes = "分页查询变更审核应收账单列表")
    public PageV<ReceivableBillPageV> getChangeApprovePage(@Validated @RequestBody PageF<SearchF<?>> queryF) {
        return receivableBillAppService.getChangeApprovePage(queryF, ReceivableBillPageV.class);
    }


    @PostMapping("/check/billTime/overlap")
    @ApiOperation(value = "检查应收账单时间是否重叠", notes = "检查应收账单时间是否重叠")
    public CheckBillTimeOverlapV checkBillTimeOverlap(
            @Validated @RequestBody CheckBillTimeOverlapF checkBillTimeOverlapF
    ) {
        return receivableBillAppService.checkBillTimeOverlap(checkBillTimeOverlapF);
    }

    @PostMapping("/batchCheck/billTime/overlap")
    @ApiOperation(value = "批量检查应收账单时间是否重叠", notes = "批量检查应收账单时间是否重叠")
    public List<CheckBillTimeOverlapV> batchCheckBillTimeOverlap(@Validated @RequestBody CheckBillTimeOverlapF checkBillTimeOverlapF) {
        return receivableBillAppService.batchCheckBillTimeOverlap(checkBillTimeOverlapF);
    }

    @ApiOperation(value = "根据合同ID获取合同报账单中对下计提非进行中（1待推送/3推送失败/5已驳回/6单据驳回/8制单失败）临时账单ID")
    @GetMapping("/getReceivableBillIdList")
    public List<String> getReceivableBillIdList(@RequestParam(value = "contractId")  String contractId,
                                                @RequestParam(value = "communityId")  String communityId){
        return receivableBillAppService.getReceivableBillIdList(contractId,communityId);
    }
    @ApiOperation(value = "根据临时账单ID删除临时账单及对应合同报账单中对下-计提")
    @GetMapping("/deleteReceivableAndHtBzd")
    public void deleteReceivableAndHtBzd(@RequestParam(value = "billIdList") List<String> billIdList,
                                                @RequestParam(value = "communityId")  String communityId){
        receivableBillAppService.deleteReceivableAndHtBzd(billIdList,communityId);
    }

    @ApiOperation(value = "[校验]根据临时账单ID获取对应报账单/合同报账单数据")
    @GetMapping("/getVoucherBillByReceivableId")
    public String getVoucherBillByReceivableId(@RequestParam(value = "billIdList") List<String> billIdList,
                                            @RequestParam(value = "communityId")  String communityId){
        return receivableBillAppService.getVoucherBillByReceivableId(billIdList,communityId);
    }
    @ApiOperation(value = "根据临时账单ID删除对应临时账单数据")
    @GetMapping("/deleteReceivableBillById")
    public void deleteReceivableBillById(@RequestParam(value = "billIdList") List<String> billIdList,
                                            @RequestParam(value = "communityId")  String communityId){
        receivableBillAppService.deleteReceivableBillById(billIdList,communityId);
    }

    @ApiOperation(value = "[校验]根据临时账单ID获取报账单数据-实签")
    @GetMapping("/getVoucherBillSq")
    public String getVoucherBillSq(@RequestParam(value = "billIdList") List<String> billIdList,
                                                               @RequestParam(value = "communityId")  String communityId){
        return receivableBillAppService.getVoucherBillSq(billIdList,communityId);
    }

    @ApiOperation(value = "根据临时账单ID还原对应临时账单数据及删除合同报账单")
    @PostMapping("/deleteReceivableBillAndVoucher")
    public void deleteReceivableBillAndVoucher(@RequestBody  List<ResultTemporaryChargeBillF> addTemporaryChargeBillFs){
        receivableBillAppService.deleteReceivableBillAndVoucher(addTemporaryChargeBillFs);
    }
}
