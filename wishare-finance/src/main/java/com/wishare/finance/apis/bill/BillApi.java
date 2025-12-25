package com.wishare.finance.apis.bill;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wishare.finance.apps.model.bill.fo.*;
import com.wishare.finance.apps.model.bill.vo.*;
import com.wishare.finance.apps.service.bill.AllBillAppService;
import com.wishare.finance.apps.service.spacePermission.SpacePermissionAppService;
import com.wishare.finance.domains.bill.dto.*;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.BillOjv;
import com.wishare.finance.domains.voucher.entity.Voucher;
import com.wishare.finance.domains.voucher.entity.VoucherBusinessDetail;
import com.wishare.finance.domains.voucher.repository.mapper.VoucherBusinessDetailMapper;
import com.wishare.finance.domains.voucher.repository.mapper.VoucherInfoMapper;
import com.wishare.finance.infrastructure.easyexcel.BillData;
import com.wishare.finance.infrastructure.utils.ListPageUtils;
import com.wishare.starter.annotations.ApiLogCustom;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.exception.BizException;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.tools.starter.fo.search.SearchF;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 账单api
 *
 * @author yancao
 */
@Api(tags = {"账单API"})
@Validated
@RestController
@RequestMapping("/bill")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BillApi {

    private final AllBillAppService billAppService;
    private final VoucherBusinessDetailMapper voucherBusinessDetailMapper;
    private final VoucherInfoMapper voucherInfoMapper;

    private final SpacePermissionAppService spacePermissionAppService;

    @PostMapping("/count")
    @ApiOperation(value = "查询账单数量", notes = "查询账单数量")
    public Long queryBillCount(@Validated @RequestBody AllBillGroupQueryF queryF) {
        return billAppService.queryBillCount(queryF);
    }

    @PostMapping("/group")
    @ApiOperation(value = "分组分页查询账单列表", notes = "分组分页查询账单列表")
    public PageV<AllBillGroupDto> queryBillByGroup(@Validated @RequestBody AllBillGroupQueryF queryF) {
        PageV<AllBillGroupDto> dto=billAppService.queryBillByGroup(queryF);
        return dto;
    }

    @PostMapping("/group/byRule")
    @ApiOperation(value = "根据收费规则分组分页查询账单列表", notes = "根据收费规则分组分页查询账单列表")
    public PageV<AllBillGroupDto> groupByRule(@Validated @RequestBody AllBillGroupQueryF queryF) {
        return billAppService.groupByRule(queryF);
    }

    @PostMapping("/ignoreTenant/page")
    @ApiOperation(value = "分页查询账单列表", notes = "分页查询账单列表")
    public PageV<AllBillPageDto> queryBillByPage(@Validated @RequestBody AllBillGroupQueryF queryF) {
        return billAppService.queryBillByPage(queryF);
    }

    @PostMapping("/query/idList")
    @ApiOperation(value = "根据id集合查询账单", notes = "根据id集合查询账单")
    public List<AllBillPageDto> queryBillByIdList(@Validated @RequestBody List<AllBillQueryF> billQueryList) {
        return billAppService.queryBillByIdList(billQueryList);
    }

    @PostMapping("/query/outApprovedIdList")
    @ApiOperation(value = "根据外部审批标识查询账单", notes = "根据外部审批标识查询账单")
    public List<AllBillPageDto> queryBillByOutApprovedId(@Valid @RequestBody @ApiParam("外部审批标识id") @NotEmpty(message = "外部审批标识id不能为空") List<String> outApprovedIdList,
        @ApiParam("上级收费单元ID") @RequestParam(value = "supCpUnitId", required = false)String supCpUnitId) {
        return billAppService.queryBillByOutApprovedId(outApprovedIdList, supCpUnitId);
    }

    @PostMapping("/query/billAmount")
    @ApiOperation(value = "查询账单金额", notes = "查询账单金额")
    public BillAmountDto queryBillAmount(@Validated @RequestBody BillAmountQueryF form){
        return billAppService.queryBillAmount(form);
    }

    @PostMapping("/invalid/charge/page")
    @ApiOperation(value = "分页查询无效收费账单", notes = "分页查询无效收费账单")
    public PageV<AllBillPageVo> invalidChargePage(@Validated @RequestBody PageF<SearchF<?>> queryF) {
        return billAppService.invalidChargePage(queryF);
    }

    @PostMapping("/invalid/charge/statistics")
    @ApiOperation(value = "统计无效收费账单", notes = "统计无效收费账单")
    public BillTotalDto invalidChargeStatistics(@Validated @RequestBody PageF<SearchF<?>> queryF) {
        return billAppService.invalidChargeStatistics(queryF);
    }

    @PostMapping("/invalid/pay/page")
    @ApiOperation(value = "分页查询无效付费账单", notes = "分页查询无效付费账单")
    public PageV<AllBillPageVo> invalidPayPage(@Validated @RequestBody PageF<SearchF<?>> queryF) {
        return billAppService.invalidPayPage(queryF);
    }

    @PostMapping("/invalid/pay/statistics")
    @ApiOperation(value = "统计无效付费账单", notes = "统计无效付费账单")
    public BillTotalDto invalidPayStatistics(@Validated @RequestBody PageF<SearchF<?>> queryF) {
        return billAppService.invalidPayStatistics(queryF);
    }

    @PostMapping("/flow/contract/amount")
    @ApiOperation(value = "统计合同流水账单金额(用于流水认领)", notes = "统计合同流水账单金额(用于流水认领)")
    public FlowContractBillStatisticsDto flowContractAmount(@Validated @RequestBody PageF<SearchF<?>> queryF) {
        return billAppService.flowContractAmount(queryF);
    }

    @PostMapping("/flow/contract/page")
    @ApiOperation(value = "分页查询合同流水账单(用于流水认领)", notes = "分页查询合同流水账单(用于流水认领)")
    public PageV<AllBillPageDto> flowContractPage(@Validated @RequestBody PageF<SearchF<?>> queryF) {
        return billAppService.flowContractPage(queryF);
    }

    @PostMapping("/flow/contract/page/new")
    @ApiOperation(value = "分页查询合同流水账单(用于流水认领)-新模式", notes = "分页查询合同流水账单(用于流水认领)-新模式")
    public PageV<AllBillPageDto> flowContractPageNew(@Validated @RequestBody PageF<SearchF<?>> queryF) {
        return billAppService.flowContractPageNew(queryF);
    }


    // 新增接口, 中交流水认领 收款单查询接口， 中交专用

    @PostMapping("/flow/contract/page/new/zj")
    @ApiOperation(value = "分页查询收款单(用于流水认领)-中交专用", notes = "分页查询收款单(用于流水认领)-中交专用")
    public PageV<FlowBillPageDto> flowContractPageNewForZJ(@Validated @RequestBody PageF<SearchF<?>> queryF) {
        return billAppService.flowPageNewZJ(queryF);
    }


    @PostMapping("/flow/contract/idList")
    @ApiOperation(value = "根据id集合查询合同流水账单(用于流水认领)", notes = "根据id集合查询合同流水账单(用于流水认领)")
    public List<AllBillPageDto> flowContractIdList(@Validated @RequestBody List<Long> idList,
        @RequestParam(value = "supCpUnitId",required = false, defaultValue = "default") @ApiParam("上级收费单元id")  String supCpUnitId) {
        return billAppService.flowContractIdList(idList, supCpUnitId);
    }

    @PostMapping("/charge/group")
    @ApiOperation(value = "费项分组分页查询账单列表(用于业务信息)", notes = "费项分组分页查询账单列表(用于业务信息)")
    public PageV<ChargeBillGroupDto> queryChargeBillByGroup(@Validated @RequestBody PageF<SearchF<?>> queryF) {
        return billAppService.queryChargeBillByGroup(queryF);
    }

    @PostMapping("/charge/group/statistics")
    @ApiOperation(value = "统计费项分组分页查询账单列表(用于业务信息)", notes = "统计费项分组分页查询账单列表(用于业务信息)")
    public ChargeBillGroupStatisticsDto statisticsChargeBillByGroup(@Validated @RequestBody SearchF<?> queryF) {
        return billAppService.statisticsChargeBillByGroup(queryF);
    }

    @ApiOperation(value = "根据账单id获取账单详情(包含明细)", notes = "根据账单id获取账单详情(包含明细)")
    @PostMapping("/detail")
    @ApiLogCustom(switchClose = true)
    public BillDetailMoreV detailBill(@RequestBody @Validated BillDetailF form) {
        form.checkBillType();
        return billAppService.detailBill(form);
    }

    @ApiOperation(value = "根据账单id和类型获取账单信息", notes = "根据账单id和类型获取账单信息")
    @PostMapping("/batch/billInfo")
    public List<BillOjv> batchBillInfo(@RequestBody @Validated List<BillSearchF> form) {
        return billAppService.batchBillInfo(form);
    }

    @PostMapping("/batch/billInfo/page")
    @ApiOperation(value = "分页查询账单列表", notes = "分页查询账单列表")
    public PageV<BillData> batchBillInfoPage(@Validated @RequestBody PageF<SearchF<?>> queryF) {
        PageV<BillData> v = new PageV<>();
        v.setPageNum(queryF.getPageNum());v.setPageSize(queryF.getPageSize());
        List<VoucherBusinessDetail> list = getBusinessDetailListByVoucherId(queryF);
        v.setTotal(list.size());
        if (CollUtil.isEmpty(list)){
            return v;
        }


        List<VoucherBusinessDetail> page = ListPageUtils.page(list, (int) v.getPageNum(), (int) v.getPageSize());
        List<BillData> voList = billAppService.batchBillInfoExport(getBatchBillInfoParam(page));
        v.setRecords(voList);
        return v;
    }
    private List<BillSearchF> getBatchBillInfoParam(List<VoucherBusinessDetail> list){
        ArrayList<BillSearchF> dd = new ArrayList<>(list.size());
        list.forEach(d->{
            dd.add(BillSearchF.builder().billId(d.getBusinessBillId()).billType(d.getBusinessBillType()).supCpUnitId(d.getSupCpUnitId()).build());
        });
        return dd;
    }
    private List<VoucherBusinessDetail> getBusinessDetailListByVoucherId(PageF<SearchF<?>> queryF){
        if (ObjectUtil.isNull(queryF) ||  ObjectUtil.isNull(queryF.getConditions()) ||
                CollUtil.isEmpty(queryF.getConditions().getFields())){
            throw new BizException(403,"参数不合法");
        }
        List<Field> fields = queryF.getConditions().getFields();

        Long voucherId = Long.valueOf((String) fields.get(0).getValue());
        Voucher voucher = voucherInfoMapper.selectById(voucherId);
        if (ObjectUtil.isNull(voucher)){
            throw new BizException(403,"凭证不存在");
        }
        List<VoucherBusinessDetail> list = voucherBusinessDetailMapper.selectList(Wrappers.<VoucherBusinessDetail>lambdaQuery()
                .select(VoucherBusinessDetail::getBusinessBillId, VoucherBusinessDetail::getBusinessBillType, VoucherBusinessDetail::getSupCpUnitId)
                        .eq(VoucherBusinessDetail::getAccountBookId,voucher.getAccountBookId())
                .eq(VoucherBusinessDetail::getVoucherId, voucherId)
        );
        if (CollUtil.isEmpty(list)){
            return Collections.emptyList();
        }
        return list.stream().collect(Collectors.collectingAndThen(
                Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(VoucherBusinessDetail::getBusinessBillId))), ArrayList::new)
        );

    }
    @ApiOperation(value = "导出关联账单", notes = "导出关联账单")
    @PostMapping("/export")
    public void batchBillInfoExport(@RequestBody @Validated PageF<SearchF<?>> queryF, HttpServletResponse response) throws IOException {
        List<VoucherBusinessDetail> list = getBusinessDetailListByVoucherId(queryF);
        if (CollUtil.isEmpty(list)){
            throw new BizException(403,"无导出数据");
        }
        List<BillData> voList = billAppService.batchBillInfoExport(getBatchBillInfoParam(list));

        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");

        String fileName = URLEncoder.encode("导出关联账单", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), BillData.class).sheet("模板").doWrite(voList);
    }

    private static String F2Y(Long amount){
        if (ObjectUtil.isNull(amount)){
            return "";
        }
        return BigDecimal.valueOf(amount).divide(new BigDecimal(100)).toString();

    }

    @ApiOperation(value = "根据账单ids获取账单简息", notes = "根据账单ids获取账单简息")
    @PostMapping("/canInvoiceInfo")
    public BillSimpleInfoV getBillSimpleInfoV(@RequestBody @Validated CanInvoiceInfoF form){
        return billAppService.getBillSimpleInfoV(form);
    }

    @ApiOperation(value = "反交账", notes = "反交账")
    @PostMapping("/rehand")
    public boolean handReversal(@RequestParam("billId") Long billId, @RequestParam("billType") Integer billType,
        @RequestParam(value = "supCpUnitId", required = false, defaultValue = "default") @ApiParam("上级收费单元id")  String supCpUnitId){
        return billAppService.handReversal(billId, billType, supCpUnitId);
    }

    @PostMapping("/approve")
    @ApiOperation(value = "审核账单", notes = "审核账单")
    public Boolean approve(@Validated @RequestBody ApproveReceivableBillF approveReceivableBillF) {
        return billAppService.approve(approveReceivableBillF);
    }

    @PostMapping("/batch/approve")
    @ApiOperation(value = "批量审核账单", notes = "批量审核账单")
    public Boolean approve(@Validated @RequestBody ChangeBatchApproveReceivableBillF changeBatchApproveReceivableBillF) {
        return billAppService.batchApprove(changeBatchApproveReceivableBillF);
    }

    @PostMapping("/out/approve")
    @ApiOperation(value = "外部审核账单", notes = "外部审核账单")
    public Boolean outApprove(@Validated @RequestBody OutApproveBillF outApproveBillF) {
        return billAppService.outApprove(outApproveBillF);
    }

    @PostMapping("/apply")
    @ApiOperation(value = "发起审核申请", notes = "发起审核申请")
    public Boolean apply(@Validated @RequestBody BillApplyF billApplyF) {
        return billAppService.apply(billApplyF);
    }

    @PostMapping("/apply/batch")
    @ApiOperation(value = "批量发起审核申请", notes = "批量发起审核申请")
    public Boolean applyBatch(@Validated @RequestBody BillApplyBatchF billApplyBatchF) {
        return billAppService.applyBatch(billApplyBatchF);
    }

    @PostMapping("/reverse")
    @ApiOperation(value = "冲销", notes = "冲销")
    public Boolean reverse(@RequestBody @Validated ReverseF form){
        return billAppService.reverse(form);
    }

    @PostMapping("/page")
    @ApiOperation(value = "分页查询账单列表", notes = "分页查询账单列表")
    public PageV<BillPageInfoV> getPage(@Validated @RequestBody PageF<SearchF<?>> queryF) {
        PageF<SearchF<?>> queryPermissionF = spacePermissionAppService.getSpacePermissionV(queryF,"b");
        if(Objects.isNull(queryPermissionF)){
            return PageV.of(queryF);
        }
        return billAppService.getPage(queryF);
    }

    @PostMapping("/batch/settle")
    @ApiOperation(value = "批量结算", notes = "批量结算")
    public Boolean batchSettle(@Validated @RequestBody BatchSettleF form){
        return billAppService.batchSettle(form);
    }

    @PostMapping("/invoiceReceipt/amount")
    @ApiOperation(value = "获取账单的开票金额", notes = "获取账单的开票金额")
    public Long InvoiceReceiptAmount(@Validated @RequestBody InvoiceReceiptAmountF form){
         return billAppService.invoiceReceiptAmount(form);
    }

    @PostMapping("/out/approveByDeduction")
    @ApiOperation(value = "外部审核账单(减免)", notes = "外部审核账单(减免)")
    public Boolean outApproveByDeduction(@Validated @RequestBody OutApproveDeductionBillF outApproveDeductionBillF) {
        return billAppService.outApproveByDeduction(outApproveDeductionBillF);
    }


    @PostMapping("/apply/batchDeduction")
    @ApiOperation(value = "批量发起减免审核申请", notes = "批量发起减免审核申请")
    public ApplyBatchDeductionV applyBatchDeduction(@Validated @RequestBody ApplyBatchDeductionF applyBatchDeductionF) {
        return billAppService.applyBatchDeduction(applyBatchDeductionF);
    }

    @PostMapping("/out/outApproveByOperationId")
    @ApiOperation(value = "外部审核账单(减免)", notes = "外部审核账单(减免)")
    public Boolean outApproveByOperationId(@Validated @RequestBody OutApproveOperationF outApproveOperationF) {
        return billAppService.outApproveByOperationId(outApproveOperationF);
    }

    @PostMapping("/getChargePayBillGroupDetail")
    @ApiOperation(value = "根据账单id列表查询账单详情", notes = "根据账单id列表查询账单详情")
    List<AllBillGroupDto> getChargePayBillGroupDetail(@RequestBody AllBillGroupQueryF allBillGroupQueryF) {
        return billAppService.getChargePayBillGroupDetail(allBillGroupQueryF);
    }
}
