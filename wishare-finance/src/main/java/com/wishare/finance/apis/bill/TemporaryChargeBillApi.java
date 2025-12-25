package com.wishare.finance.apis.bill;

import com.wishare.finance.apis.common.DeveloperPayFilterCondition;
import com.wishare.finance.apps.model.bill.fo.*;
import com.wishare.finance.apps.model.bill.vo.*;
import com.wishare.finance.apps.service.bill.TemporaryChargeBillAppService;
import com.wishare.finance.apps.service.configure.chargeitem.ChargeItemAppService;
import com.wishare.finance.domains.bill.aggregate.data.EnvData;
import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import com.wishare.finance.domains.bill.consts.enums.SettleChannelEnum;
import com.wishare.finance.domains.bill.dto.*;
import com.wishare.finance.domains.bill.entity.PayInfo;
import com.wishare.finance.domains.configure.chargeitem.entity.ChargeItemE;
import com.wishare.finance.domains.voucher.consts.enums.VoucherBillCustomerTypeEnum;
import com.wishare.finance.domains.voucher.support.fangyuan.service.BillRuleDomainService;
import com.wishare.finance.infrastructure.conts.CarryoverConst;
import com.wishare.finance.infrastructure.conts.EnvConst;
import com.wishare.finance.infrastructure.remote.clients.base.ContractClient;
import com.wishare.finance.infrastructure.remote.vo.contract.ContractPaymentVO;
import com.wishare.starter.annotations.ApiLogCustom;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Api(tags = {"临时收费账单"})
@Validated
@RestController
@RequestMapping("/temporary")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class TemporaryChargeBillApi {

    private final TemporaryChargeBillAppService temporaryChargeBillAppService;

    private final ChargeItemAppService chargeItemAppService;

    private final ContractClient contractClient;

    @PostMapping("/add")
    @ApiOperation(value = "新增临时收费", notes = "新增临时收费")
    public TemporaryChargeBillDetailV add(@RequestBody @Validated AddTemporaryChargeBillF addTemporaryChargeBillF){
        if(StringUtils.isBlank(addTemporaryChargeBillF.getSupCpUnitId())) {
            throw new IllegalArgumentException("上级收费单元ID不能为空!");
        }
        return temporaryChargeBillAppService.addBill(addTemporaryChargeBillF, TemporaryChargeBillDetailV.class);
    }

    @PostMapping("/add/batch")
    @ApiOperation(value = "批量新增临时收费账单", notes = "批量新增临时收费账单")
    public List<TemporaryChargeBillDetailV> addBatch(@Valid @RequestBody @Size(max = 1000, min = 1, message = "临时收费账单列表大小不允许，仅允许区间为[1,1000]") List<AddTemporaryChargeBillF> addTemporaryChargeBillFs) {
        addTemporaryChargeBillFs.forEach(v -> {
            if(StringUtils.isBlank(v.getSupCpUnitId())) {
                throw new IllegalArgumentException("上级收费单元ID不能为空!");
            }
        });
        return temporaryChargeBillAppService.addBatchBill(addTemporaryChargeBillFs, TemporaryChargeBillDetailV.class);
    }

    @PostMapping("/decoration/add/batch")
    @ApiOperation(value = "装修新增临时收费账单", notes = "装修新增临时收费账单")
    public List<TemporaryChargeBillDetailV> decorationAddBatch(@Valid @RequestBody @Size(max = 1000, min = 1, message = "临时收费账单列表大小不允许，仅允许区间为[1,1000]") List<AddTemporaryChargeBillF> addTemporaryChargeBillFs) {
        addTemporaryChargeBillFs.forEach(v -> {
            if(StringUtils.isBlank(v.getSupCpUnitId())) {
                throw new IllegalArgumentException("上级收费单元ID不能为空!");
            }
        });
        return temporaryChargeBillAppService.addBatchBill(addTemporaryChargeBillFs, TemporaryChargeBillDetailV.class);
    }

    @PostMapping("/update/batch")
    @ApiOperation(value = "批量新增临时收费账单", notes = "批量新增临时收费账单")
    public boolean updateBatch(@Valid @RequestBody @Size(max = 1000, min = 1, message = "临时收费账单列表大小不允许，仅允许区间为[1,1000]") List<UpdateTemporaryChargeBillF> updateTemporaryChargeBillFs) {
        updateTemporaryChargeBillFs.forEach(v -> {
            if(StringUtils.isBlank(v.getSupCpUnitId())) {
                throw new IllegalArgumentException("上级收费单元ID不能为空!");
            }
        });
        return temporaryChargeBillAppService.updateBatchBill(updateTemporaryChargeBillFs);
    }

    @PostMapping("/import/batch")
    @ApiOperation(value = "批量导入临时收费账单", notes = "批量导入临时收费账单")
    public List<ImportTemporaryChargeBillV> importBill(@Validated @RequestBody @Size(max = 1000, min = 1,
            message = "临时收费账单列表大小不允许，仅允许区间为[1,1000]") List<ImportTemporaryChargeBillF> importTemporaryChargeBillFS) {
        return temporaryChargeBillAppService.importBill(importTemporaryChargeBillFS, ImportTemporaryChargeBillV.class);
    }

    @PostMapping("/approve/batch")
    @ApiOperation(value = "批量审核临时收费账单", notes = "批量审核临时收费账单")
    public List<Long> approveBatch(@Validated @RequestBody ApproveBatchTemporaryChargeBillF approveBatchTemporaryChargeBillF) {
        return temporaryChargeBillAppService.approveBatchReturnIds(approveBatchTemporaryChargeBillF, BillTypeEnum.临时收费账单.getCode());
    }

    @PostMapping("/approve")
    @ApiOperation(value = "审核临时收费账单", notes = "审核临时收费账单")
    public Boolean approve(@Validated @RequestBody ApproveTemporaryChargeBillF approveTemporaryChargeBillF) {
        return temporaryChargeBillAppService.approve(approveTemporaryChargeBillF);
    }

    @DeleteMapping("/{temporaryChargeBillId}")
    @ApiOperation(value = "删除临时收费账单", notes = "删除临时收费账单")
    public Boolean deleteById(@PathVariable("temporaryChargeBillId") @ApiParam("临时收费账单id") @NotNull(message = "临时收费账单id不能为空") Long temporaryChargeBillId
            ,@RequestParam("supCpUnitId") @ApiParam("上级收费单元id")  String supCpUnitId) {
        return temporaryChargeBillAppService.delete(temporaryChargeBillId,supCpUnitId);
    }

    @PostMapping("/reference")
    @ApiOperation(value = "设置引用临时收费账单", notes = "设置引用临时收费账单")
    public Boolean reference(@Validated @RequestBody ReferenceBillF referenceBillF) {
        return temporaryChargeBillAppService.reference(referenceBillF);
    }


    @DeleteMapping("/delete/batch")
    @ApiOperation(value = "批量删除临时收费账单", notes = "批量删除临时收费账单")
    public BillBatchResultDto deleteBatch(@Validated @RequestBody DeleteBatchTemporaryChargeBillF deleteBatchTemporaryChargeBillF) {
        return temporaryChargeBillAppService.deleteBatch(deleteBatchTemporaryChargeBillF, BillTypeEnum.临时收费账单.getCode());
    }

    @PostMapping("/apply")
    @ApiOperation(value = "发起审核申请", notes = "发起审核申请")
    public Boolean apply(@Validated @RequestBody BillApplyF billApplyF) {
        temporaryChargeBillAppService.apply(billApplyF);
        return true;
    }

    @PostMapping("/updateApply")
    @ApiOperation(value = "更新审核记录", notes = "更新审核记录")
    public Long updateApply(@Validated @RequestBody BillApplyUpdateF billApplyF) {
        return temporaryChargeBillAppService.updateApply(billApplyF);
    }

    @PostMapping("/history/approve")
    @ApiOperation(value = "根据查询条件获取历史审核记录", notes = "根据查询条件获取历史审核记录")
    public List<BillApproveV> approveHistory(@Validated @RequestBody ApproveHistoryF approveHistoryF) {
        return temporaryChargeBillAppService.approveHistory(approveHistoryF);
    }

    @PostMapping("/apply/batch")
    @ApiOperation(value = "批量发起审核申请", notes = "批量发起审核申请")
    public Boolean applyBatch(@Validated @RequestBody BillApplyBatchF billApplyBatchF) {
        if(StringUtils.isBlank(billApplyBatchF.getSupCpUnitId())) {
            throw new IllegalArgumentException("上级收费单元ID不能为空!");
        }
        return temporaryChargeBillAppService.applyBatch(billApplyBatchF);
    }

    @PostMapping("/deapprove")
    @ApiOperation(value = "反审核临时收费账单", notes = "反审核临时收费账单")
    public Boolean deapprove(@RequestParam("billId") @ApiParam("临时收费账单id") @NotNull(message = "临时收费账单id不能为空") Long temporaryChargeBillId
        ,@RequestParam("supCpUnitId") @ApiParam("上级收费单元id")  String supCpUnitId) {
        return temporaryChargeBillAppService.deapprove(temporaryChargeBillId,supCpUnitId);
    }

    @PostMapping("/freeze")
    @ApiOperation(value = "冻结临时收费账单", notes = "冻结临时收费账单")
    public Boolean freeze(@RequestParam("billId") @ApiParam("临时收费账单id") @NotNull(message = "临时收费账单id不能为空") Long temporaryChargeBillId
            ,@RequestParam("supCpUnitId") @ApiParam("上级收费单元id")  String supCpUnitId) {
        return temporaryChargeBillAppService.freeze(temporaryChargeBillId,supCpUnitId);
    }

    @PostMapping("/freeze/batch")
    @ApiOperation(value = "批量冻结临时收费账单", notes = "批量冻结临时收费账单")
    public Boolean freezeBatch(@Validated @RequestBody FreezeBatchF freezeBatchF) {
        return temporaryChargeBillAppService.freezeBatch(freezeBatchF, BillTypeEnum.临时收费账单.getCode());
    }

    @PostMapping("/unfreeze/batch")
    @ApiOperation(value = "批量解冻临时账单", notes = "批量解冻临时账单")
    public Boolean unfreezeBatch(@Validated @RequestBody UnFreezeBatchF unFreezeBatchF) {
        return temporaryChargeBillAppService.unfreezeBatch(unFreezeBatchF);
    }

    @GetMapping("/apply/detail")
    @ApiOperation(value = "查询临时收费账单申请详情", notes = "查询临时收费账单申请详情")
    public TemporaryChargeBillApplyDetailV getApplyDetailById(@RequestParam("billId") @ApiParam("临时收费账单id") @NotNull(message = "临时收费账单id不能为空") Long receivableBillId
        ,@RequestParam("supCpUnitId") @ApiParam("上级收费单元id")  String supCpUnitId) {
        return temporaryChargeBillAppService.getWithApproving(receivableBillId, TemporaryChargeBillApplyDetailV.class, supCpUnitId);
    }

    @GetMapping("/{temporaryChargeBillId}")
    @ApiOperation(value = "查询临时收费账单详情", notes = "查询临时收费账单详情")
    public TemporaryChargeBillDetailV queryById(@PathVariable("temporaryChargeBillId") @ApiParam("临时收费账单ID") Long temporaryChargeBillId
        ,@RequestParam("supCpUnitId") @ApiParam("上级收费单元id")  String supCpUnitId) {
        return temporaryChargeBillAppService.getById(temporaryChargeBillId, TemporaryChargeBillDetailV.class, supCpUnitId);
    }

    @PostMapping("/page")
    @ApiLogCustom(switchClose = true)
    @ApiOperation(value = "分页查询临时收费账单列表", notes = "分页查询临时收费账单列表")
    public PageV<TemporaryChargeBillPageV> queryPage(@Validated @RequestBody PageF<SearchF<?>> queryF) {
        if (EnvConst.YUANYANG.equals(EnvData.config)) {
            DeveloperPayFilterCondition.handleDeveloperPay(queryF);
        }
        PageV<TemporaryChargeBillPageV> page = temporaryChargeBillAppService.getPage(queryF, TemporaryChargeBillPageV.class);
        if (page!=null&& CollectionUtils.isNotEmpty(page.getRecords())){
            List<ChargeItemE> chargeItems = Lists.newArrayList();
            if (EnvConst.LINGANG.equals(EnvData.config)) {
                List<TemporaryChargeBillPageV> records = page.getRecords();
                List<Long> itemIds = records.stream().map(TemporaryChargeBillPageV::getChargeItemId).collect(Collectors.toList());
                chargeItems = chargeItemAppService.getNameByIds(itemIds);
            }

            //获取来源为“合同”的收入方信息
            Map<String, List<ContractPaymentVO>> contractPaymentVOMap = new HashMap<>();
            List<String> contractIds = page.getRecords().stream().filter(x->"收入合同".equals(x.getSource()) || "支出合同".equals(x.getSource()))
                    .map(TemporaryChargeBillPageV::getExtField6).collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(contractIds)){
                contractPaymentVOMap = contractClient.getContractPaymentList(contractIds).stream()
                        .collect(Collectors.groupingBy(ContractPaymentVO::getContractId));
            }

            for (TemporaryChargeBillPageV record : page.getRecords()) {
                StringBuilder stringBuilder = new StringBuilder("");
                if (record!=null&&CollectionUtils.isNotEmpty(record.getPayInfos())){
                    for (PayInfo payInfo : record.getPayInfos()) {
                        stringBuilder.append(SettleChannelEnum.valueNameOfByCode(payInfo.getPayChannel())).append(",");
                    }
                }
                String string = stringBuilder.toString();
                if (string.length()==0){
                    record.setPayInfosString(string);
                }else {
                    record.setPayInfosString(string.substring(0,string.length()-1));
                }
                /** 是否暂估收入 */
                record.setEstimatedMark(CarryoverConst.BILL_ESTIMATED_INCOME.equals(record.getExtField4()) ? "是" : "否");
                if (record.getPayerType() != null) {
                    record.setPayerTypeStr(VoucherBillCustomerTypeEnum.valueOfByCode(record.getPayerType()).getValue());
                }
                if (EnvConst.LINGANG.equals(EnvData.config) && CollectionUtils.isNotEmpty(chargeItems)){
                    Long chargeItemId = record.getChargeItemId();
                    if(Objects.nonNull(chargeItemId)){
                        List<ChargeItemE> collect = chargeItems.stream().filter(item -> chargeItemId.equals(item.getId())).collect(Collectors.toList());
                        if(CollectionUtils.isNotEmpty(collect)){
                            record.setChargeItemName(collect.get(0).getName());
                        }
                    }
                }

                if("收入合同".equals(record.getSource()) || "支出合同".equals(record.getSource())){
                    List<ContractPaymentVO> contractPaymentVOS = contractPaymentVOMap.get(record.getExtField6());
                    if (CollectionUtils.isNotEmpty(contractPaymentVOS)) {
                        ContractPaymentVO contractPayment = contractPaymentVOS.get(0);
                        record.setPayerType(null);
                        record.setPayerTypeStr("往来单位");
                        record.setContactName(StringUtils.isEmpty(contractPayment.getPayeeName()) ? contractPayment.getIncomeName() : contractPayment.getPayeeName());
                    }
                }
            }
        }
        return page;
    }

    @PostMapping("/approve/page")
    @ApiLogCustom(switchClose = true)
    @ApiOperation(value = "分页查询临时收费账单审核列表", notes = "分页查询临时收费账单审核列表")
    public PageV<TemporaryChargeBillPageV> queryApprovePage(@Validated @RequestBody PageF<SearchF<?>> queryF) {
        return temporaryChargeBillAppService.queryApprovePage(queryF);
    }

    @GetMapping("/detail/{temporaryChargeBillId}")
    @ApiOperation(value = "查询临时收费账单详情(包含明细)", notes = "查询临时收费账单详情(包含明细)")
    public TemporaryChargeBillAllDetailV getAllDetail(@PathVariable("temporaryChargeBillId") @ApiParam("临时收费账单id") @NotNull(message = "临时收费账单id不能为空") Long temporaryChargeBillId
            ,@RequestParam("supCpUnitId") @ApiParam("上级收费单元id")  String supCpUnitId) {
        return temporaryChargeBillAppService.getAllDetail(temporaryChargeBillId, TemporaryChargeBillAllDetailV.class,supCpUnitId);
    }

    @ApiOperation(value = "根据账单ids获取临时收费信息", notes = "根据账单ids获取临时收费信息")
    @ApiLogCustom(switchClose = true)
    @PostMapping("/batch/tempChargeBillInfo")
    public List<TempChargeBillMoreInfoDto> tempChargeBillInfo(@Validated @RequestBody ReceivableBillInfoF form){
        return temporaryChargeBillAppService.tempChargeBillInfo(form.getBillIds(), form.getSupCpUnitId());
    }

    @ApiOperation(value = "（通用）根据账单id集合获取临时账单信息", notes = "（通用）根据账单id集合获取临时账单信息")
    @PostMapping("/billInfo/idList")
    public List<TemporaryChargeBillDetailV> getBillInfoByIds(@Validated @RequestBody @NotEmpty(message = "账单id不能为空")List<Long> billIds
        ,@RequestParam("supCpUnitId") @ApiParam("上级收费单元id")  String supCpUnitId){
        return temporaryChargeBillAppService.getBillInfoByIds(billIds, TemporaryChargeBillDetailV.class,supCpUnitId);
    }

    @ApiOperation(value = "根据账单ids获取临时账单结算详情", notes = "根据账单ids获取临时收费结算详情")
    @PostMapping("/settleDetail")
    public SettleDetailDto settleDetail(@Validated @RequestBody SettleDetailF form) {
        return temporaryChargeBillAppService.settleDetail(form.getBillIds(), form.getSupCpUnitId());
    }

    @PostMapping("/page/exportData")
    @ApiOperation(value = "分页查询临时收费导出账单列表", notes = "分页查询临时收费导出账单列表")
    public PageV<TempChargeBillExportDto> queryExportDataPage(@Validated @RequestBody PageF<SearchF<?>> queryF) {
        return temporaryChargeBillAppService.queryExportDataPage(queryF);
    }

    @ApiOperation(value = "批量结算", notes = "批量结算")
    @PostMapping("/settle/batch")
    public Boolean settleBatch(@Validated @RequestBody List<AddBillSettleF> form) {
        return temporaryChargeBillAppService.settleBatch(form) != null;
    }

    @ApiOperation(value = "批量结算(返回收款单id)", notes = "批量结算(返回收款单id)")
    @PostMapping("/settle/batchReturn")
    public Long settleBatchReturn(@Validated @RequestBody List<AddBillSettleF> form) {
        return temporaryChargeBillAppService.settleBatch(form);
    }

    @ApiOperation(value = "根据账单ids获取结算记录", notes = "根据账单ids获取结算记录")
    @PostMapping("/getBillSettle")
    public List<BillSettleDto> getBillSettle(@Validated @RequestBody List<Long> billIds
        ,@NotBlank(message = "上级收费单元id 不能为空!") @RequestParam("supCpUnitId") @ApiParam("上级收费单元id")  String supCpUnitId){
        return temporaryChargeBillAppService.getBillSettle(billIds, supCpUnitId);
    }

    @ApiOperation(value = "批量发起开票", notes = "发起开票")
    @PostMapping("/invoice/batch")
    public Boolean invoiceBatch(@Validated @RequestBody List<Long> billIds
            ,@NotBlank(message = "上级收费单元id 不能为空!") @RequestParam("supCpUnitId") @ApiParam("上级收费单元id")  String supCpUnitId){
        return temporaryChargeBillAppService.invoiceBatch(billIds,supCpUnitId,null);
    }

    @ApiOperation(value = "批量完成开票", notes = "发起开票")
    @PostMapping("/invoice/finish/batch")
    public Boolean finishInvoiceBatch(@Validated @RequestBody List<FinishInvoiceF> finishInvoiceFList){
        return temporaryChargeBillAppService.finishInvoiceBatch(finishInvoiceFList);
    }

    @ApiOperation(value = "批量作废,红冲开票金额", notes = "批量作废开票金额")
    @PostMapping("/invoice/void/batch")
    public Boolean invoiceVoidBatch(@Validated @RequestBody List<InvoiceVoidBatchF> invoiceVoidBatchFList){
        return temporaryChargeBillAppService.invoiceVoidBatch(invoiceVoidBatchFList);
    }

    @PostMapping("/hand/batch")
    @ApiOperation(value = "批量交账", notes = "批量交账")
    public Boolean bathHandAdvanceBill( @Validated @RequestBody BillHandBatchF billHandBatchF) {
        return temporaryChargeBillAppService.handBatch(billHandBatchF, BillTypeEnum.临时收费账单.getCode());
    }

    @PostMapping("/reconcile/batch")
    @ApiOperation(value = "批量对账", notes = "批量对账")
    public Boolean reconcileBatch(@Validated @RequestBody List<ReconcileBatchF> reconcileBatchFS) {
        return temporaryChargeBillAppService.reconcileBatch(reconcileBatchFS);
    }

    @PostMapping("/rehand")
    @ApiOperation(value = "反交账", notes = "反交账")
    public Boolean handReversal(@RequestParam("billId") @ApiParam("临时收费账单id") @NotNull(message = "临时收费账单id不能为空") Long temporaryChargeBillId
            ,@NotBlank(message = "上级收费单元id 不能为空!") @RequestParam("supCpUnitId") @ApiParam("上级收费单元id")  String supCpUnitId) {
        return temporaryChargeBillAppService.handReversal(temporaryChargeBillId,supCpUnitId);
    }

    @PostMapping("/query/idList")
    @ApiOperation(value = "根据临时账单id集合查询账单", notes = "根据临时账单id集合查询账单")
    public List<TemporaryChargeBillDetailV> queryByIdList(@RequestBody List<Long> idList
            ,@NotBlank(message = "上级收费单元id 不能为空!") @RequestParam("supCpUnitId") @ApiParam("上级收费单元id")  String supCpUnitId) {
        return temporaryChargeBillAppService.queryByIdList(idList,TemporaryChargeBillDetailV.class,supCpUnitId);
    }

    @GetMapping("/reverse")
    @ApiOperation(value = "冲销", notes = "冲销")
    public Boolean reverse(@RequestParam("billId") @ApiParam("临时账单id") @NotNull(message = "临时账单id不能为空") Long temporaryChargeBillId,
                           @RequestParam("extField1") @ApiParam("冲销时如果需要生成新的应收传入：ReversedInitBill）") String extField1
            ,@NotBlank(message = "上级收费单元id 不能为空!") @RequestParam("supCpUnitId") @ApiParam("上级收费单元id")  String supCpUnitId){
        return temporaryChargeBillAppService.reverse(temporaryChargeBillId,extField1,supCpUnitId);
    }

    @GetMapping("/roback/reverse")
    @ApiOperation(value = "回滚冲销", notes = "回滚冲销")
    public Boolean robackReverse(@RequestParam("billId") @ApiParam("临时账单id") @NotNull(message = "临时账单id不能为空") Long temporaryChargeBillId
            ,@NotBlank(message = "上级收费单元id 不能为空!")@RequestParam("supCpUnitId") @ApiParam("上级收费单元id")  String supCpUnitId){
        return temporaryChargeBillAppService.robackReverse(temporaryChargeBillId,supCpUnitId);
    }

    @PostMapping("/list/inference")
    @ApiOperation(value = "获取账单推凭信息", notes = "获取账单推凭信息")
    public List<BillInferenceV> getBillInferenceInfo( @Validated @RequestBody BillInferenceF billInferenceF) {
        return temporaryChargeBillAppService.listInferenceInfo(billInferenceF);
    }

    @PostMapping("/list/inference/batch")
    @ApiOperation(value = "获取账单推凭信息", notes = "获取账单推凭信息")
    public List<BillInferenceV> getBillInferenceInfoByIds(@Validated @RequestBody BatchBillInferenceF batchBillInferenceF) {
        return temporaryChargeBillAppService.listInferenceInfoByIds(batchBillInferenceF);
    }

    @PostMapping("/page/inference")
    @ApiOperation(value = "获取账单推凭信息", notes = "获取账单推凭信息")
    public PageV<BillInferenceV> pageBillInferenceInfo(@Validated @RequestBody ListBillInferenceF form) {
        return temporaryChargeBillAppService.pageBillInferenceInfo(form, BillTypeEnum.临时收费账单);
    }

    @PostMapping("/page/tenant")
    @ApiOperation(value = "分页查询应收账单列表（无租户隔离）", notes = "分页查询应收账单列表（无租户隔离）")
    PageV<TemporaryChargeBillPageV> getPageNoTenantLine(@Validated @RequestBody PageF<SearchF<?>> queryF) {
        return temporaryChargeBillAppService.getPageNoTenantLine(queryF, TemporaryChargeBillPageV.class);
    }

    @PostMapping("/carryover")
    @ApiOperation(value = "账单结转", notes = "账单结转")
    public Boolean carryover(@Validated @RequestBody TemporaryChargeBillCarryoverF temporaryChargeBillCarryoverF) {
        return temporaryChargeBillAppService.carryover(temporaryChargeBillCarryoverF);
    }

    @PostMapping("/adjust")
    @ApiOperation(value = "账单调整", notes = "账单调整")
    public Boolean adjust(@Validated @RequestBody TemporaryChargeBillAdjustF temporaryChargeBillAdjustF) {
        return temporaryChargeBillAppService.adjust(temporaryChargeBillAdjustF);
    }

    @PostMapping("/refund")
    @ApiOperation(value = "账单退款", notes = "账单退款")
    public Boolean refund(@Validated @RequestBody TemporaryChargeBillRefundF temporaryChargeBillRefundF){
        return temporaryChargeBillAppService.refund(temporaryChargeBillRefundF);
    }

    @PostMapping("/invalid")
    @ApiOperation(value = "作废账单", notes = "作废账单")
    public Boolean invalid(@Validated @RequestBody TemporaryChargeBillInvalidF temporaryChargeBillInvalidF) {
        return temporaryChargeBillAppService.invalid(temporaryChargeBillInvalidF);
    }

    @PostMapping("/invalid/batch")
    @ApiOperation(value = "批量作废", notes = "批量作废")
    public BillBatchResultDto invalidBatch(@Validated @RequestBody @ApiParam("账单id") @NotEmpty(message = "应收账单id不能为空") List<Long> billIdList
            ,@RequestParam("supCpUnitId") @ApiParam("上级收费单元id")  String supCpUnitId) {
        return temporaryChargeBillAppService.invalidBatch(billIdList,supCpUnitId);
    }

    @GetMapping("/dealData")
    @ApiOperation(value = "处理临时账单数据", notes = "处理临时账单数据")
    public Boolean dealData() {
        return temporaryChargeBillAppService.dealData();
    }

    @PostMapping("/deduct")
    @ApiOperation(value = "临时账单扣款", notes = "临时账单扣款")
    Boolean deduct(@Validated @RequestBody BillDeductionF deductionF){
        return temporaryChargeBillAppService.deduct(deductionF);
    }

    @PostMapping("/delete/init")
    @ApiOperation(value = "根据查询条件删除初始审核临时账单", notes = "根据查询条件删除初始审核临时账单")
    public Boolean deleteInitBill(@Validated @RequestBody PageF<SearchF<?>> queryF) {
        return temporaryChargeBillAppService.deleteInitBill(queryF);
    }

    @GetMapping("/list/roomIds")
    @ApiOperation(value = "获取当前项目下存在可结转预收账单的房间", notes = "获取当前项目下存在可结转预收账单的房间")
    public List<Long> getBillRoomIds(@RequestParam("communityId") @ApiParam("项目id")
                                        @NotBlank(message = "项目id不能为空") String communityId,
                                        @RequestParam("chargeItemId") @ApiParam("费项id")
                                        @NotNull(message = "费项id不能为空")Long chargeItemId) {
        return temporaryChargeBillAppService.getBillRoomIds(communityId, chargeItemId);
    }

}
