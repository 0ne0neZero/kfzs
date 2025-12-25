package com.wishare.finance.apis.invoicereceipt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wishare.finance.apps.model.invoice.invoice.dto.*;
import com.wishare.finance.apps.model.invoice.invoice.fo.*;
import com.wishare.finance.apps.model.invoice.invoice.vo.*;
import com.wishare.finance.apps.model.invoice.nuonuo.fo.InvoiceCallbackF;
import com.wishare.finance.apps.model.invoice.nuonuo.vo.InvoiceCallbackResultV;
import com.wishare.finance.apps.service.expensereport.ExpenseReportAppService;
import com.wishare.finance.apps.service.invoicereceipt.InvoiceAppService;
import com.wishare.finance.domains.bill.service.InvoiceFlowMonitorService;
import com.wishare.finance.domains.invoicereceipt.dto.InvoiceSendDto;
import com.wishare.finance.domains.invoicereceipt.repository.InvoiceRepository;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import com.wishare.finance.infrastructure.remote.fo.external.baiwangjinshuiyun.request.BuildingServiceInfoF;
import com.wishare.finance.infrastructure.remote.vo.invoice.ValidReverseGatherBillRV;
import com.wishare.finance.infrastructure.support.lock.LockHelper;
import com.wishare.finance.infrastructure.support.lock.Locker;
import com.wishare.finance.infrastructure.support.lock.LockerEnum;
import com.wishare.finance.infrastructure.utils.IdempotentUtil;
import com.wishare.finance.infrastructure.utils.TenantUtil;
import com.wishare.starter.annotations.ApiLogCustom;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.stream.Collectors;

import static com.wishare.finance.infrastructure.conts.ErrorMessage.INVOICE_RECORD_REPEAT;
import static com.wishare.finance.infrastructure.utils.IdempotentUtil.IdempotentEnum.RE_RECORD_INVOICE;

/**
 * @author xujian
 * @date 2022/9/20
 * @Description:
 */
@Api(tags = {"发票管理"})
@RestController
@RequestMapping("/invoice")
@RequiredArgsConstructor
@Slf4j
public class InvoiceApi {

    private final InvoiceRepository invoiceRepository;
    private final InvoiceAppService invoiceAppService;

    private final InvoiceFlowMonitorService invoiceFlowMonitorService;

    @PostMapping("/page")
    @ApiLogCustom(switchClose = true)
    @ApiOperation(value = "分页查询开票列表", notes = "分页查询开票列表")
    public PageV<InvoicePageV> queryPage(@Validated @RequestBody PageF<SearchF<?>> form) {
        return invoiceAppService.queryPage(form);
    }

    @PostMapping("/listByBillIds")
    @ApiOperation(value = "根据条件查询开票列表", notes = "根据条件查询开票列表")
    public List<BillInvoiceDto> listByBillIds(@Validated @RequestBody InvoiceListF form) {
        return invoiceAppService.listByBillIds(form);
    }

    @GetMapping("/quota/listGroupByAmount")
    @ApiLogCustom(switchClose = true)
    @ApiOperation(value = "获取当前项目下以开票金额分组的定额发票列表", notes = "获取当前项目下以开票金额分组的定额发票列表")
    public List<InvoiceGroupByAmountV> quotaListGroupByAmount(@RequestParam("communityId") @ApiParam("项目id")
                                                                  @NotBlank(message = "项目id不能为空") String communityId) {
        return invoiceAppService.quotaListGroupByAmount(communityId);
    }

    @PostMapping("/quota/bindBill")
    @ApiLogCustom(switchClose = true)
    @ApiOperation(value = "将定额发票关联到一个账单并完成缴费和认领", notes = "将定额发票关联到一个账单并完成缴费和认领")
    public void quotaBindBill(@Validated @RequestBody QuotaInvoiceBindF form) {
        invoiceAppService.quotaBindBill(form);
    }

    /**
     * 增值税普通发票
     * 增值税专用用发票
     * 增值税电子发票
     * 全电普票
     * 全电专票
     * @param form
     * @return
     */
    @PostMapping("/asyncInvoiceBatch")
    @ApiOperation(value = "开具发票(蓝票)异步", notes = "开具发票(蓝票)异步")
    public void asyncInvoiceBatch(@Validated @RequestBody InvoiceBatchF form) {
        invoiceAppService.asyncInvoiceBatch(form);
    }

    /**
     * 增值税普通发票
     * 增值税专用用发票
     * 增值税电子发票
     * 全电普票
     * 全电专票
     * @param form
     * @return
     */
    @PostMapping("/invoiceBatch")
    @ApiOperation(value = "开具发票(蓝票)", notes = "开具发票(蓝票)")
    public Long invoiceBatch(@Validated @RequestBody InvoiceBatchF form) {
        //若中交则进行开收据
        if(TenantUtil.bf2()){
            //先开收据再开票
            return invoiceFlowMonitorService.doFlow(form);
        }
        return invoiceAppService.invoiceBatch(form);
    }

    @PostMapping("/buildingServiceInfo")
    @ApiLogCustom(switchClose = true)
    @ApiOperation(value = "获取建筑服务类开票相关信息", notes = "获取建筑服务类开票相关信息")
    public BuildingServiceInfoF buildingServiceInfo(@Validated @RequestBody InvoiceBatchF form) {

        BuildingServiceInfoF vo = invoiceAppService.buildingServiceInfo(form);
        log.info("获取建筑服务类开票相关信息：{}", JSONObject.toJSONString(vo.getServiceInfoF()));
        return vo;
    }

    @PostMapping("/getValidInvoiceIdsByGatherBillIds")
    @ApiLogCustom(switchClose = true)
    @ApiOperation(value = "根据收款单ids获取当前收款单开票成功和部分红冲的发票", notes = "根据收款单ids获取当前收款单开票成功和部分红冲的发票")
    public List<ValidReverseGatherBillRV> getValidInvoiceIdsByGatherBillIds(
            @Validated @RequestBody @NotEmpty(message = "收款单id列表不能为空")List<Long> gatherBillIds) {

        List<ValidReverseGatherBillRV> voList = invoiceAppService.getValidInvoiceIdsByGatherBillIds(gatherBillIds);
        log.info("根据收款单ids获取当前收款单开票成功和部分红冲的发票：{}", JSONObject.toJSONString(voList));
        return voList;
    }

    @GetMapping("/getValidInvoiceIdsByBillId")
    @ApiLogCustom(switchClose = true)
    @ApiOperation(value = "根据账单id获取当前账单开票成功和部分红冲的发票", notes = "根据账单id获取当前账单开票成功和部分红冲的发票")
    public List<Long> getValidInvoiceIdsByBillId(@RequestParam("billId") @ApiParam("账单id")
                                                                         @NotNull(message = "账单id不能为空") Long billId) {

        List<Long> voList = invoiceAppService.getValidInvoiceIdsByBillId(billId);
        log.info("根据账单id获取当前账单开票成功和部分红冲的发票：{}", JSONObject.toJSONString(voList));
        return voList;
    }



    /**
     * 增值税普通发票
     * 增值税专用用发票
     * 增值税电子发票
     * 全电普票
     * 全电专票
     * @param form
     * @return 返回的是电子收据的账号
     * @see InvoiceApi#invoiceBatch(InvoiceBatchF)
     */
    @PostMapping("/invoiceBatch/v1")
    @ApiOperation(value = "先开收据再开开具发票(蓝票)", notes = "当前接口目前仅只有收费系统中交环境调用")
    public Long invoiceBatchV1(@Validated @RequestBody InvoiceBatchF form) {
        return invoiceFlowMonitorService.doFlow(form);
    }

    @PostMapping("/invoiceBatchRed")
    @ApiOperation(value = "开具发票(红票)", notes = "开具发票(红票)")
    public Boolean invoiceBatchRed(@Validated @RequestBody InvoiceBatchRedF form) {
        try (LockHelper ignored = LockHelper.tryLock(Locker.of(LockerEnum.INVOICE_BATCH_LOCK, JSON.toJSONString(form.getInvoiceReceiptId())))){
            return invoiceAppService.invoiceBatchRed(form);
        }
    }

    @PostMapping("/statistics")
    @ApiOperation(value = "统计发票信息", notes = "统计发票信息")
    public InvoiceStatisticsDto statistics(@Validated @RequestBody PageF<SearchF<?>> form) {
        return invoiceAppService.statistics(form);
    }

    @PostMapping("/page/detail")
    @ApiOperation(value = "分页查询收费发票和收据列表(用于流水领用)", notes = "分页查询发票和收据列表(用于流水领用)")
    public PageV<ChargeInvoiceAndReceiptV> queryDetailPage(@Validated @RequestBody PageF<SearchF<?>> form) {
        return invoiceAppService.queryDetailPage(form, 1, ChargeInvoiceAndReceiptV.class);
    }

    @PostMapping("/page/detail/new")
    @ApiOperation(value = "分页查询收费发票和收据列表(用于流水领用)-新模式", notes = "分页查询发票和收据列表(用于流水领用)-新模式")
    public PageV<ChargeInvoiceAndReceiptV> queryDetailPageNew(@Validated @RequestBody PageF<SearchF<?>> form) {
        return invoiceAppService.queryDetailPageNew(form, ChargeInvoiceAndReceiptV.class);
    }

    @PostMapping("/page/contract")
    @ApiOperation(value = "分页查询合同发票和收据列表(用于流水领用)", notes = "分页查询合同发票和收据列表(用于流水领用)")
    public PageV<ContractInvoiceAndReceiptV> queryContractPage(@Validated @RequestBody PageF<SearchF<?>> form) {
        return invoiceAppService.queryDetailPage(form, 2, ContractInvoiceAndReceiptV.class);
    }

    @PostMapping("/statistics/amount")
    @ApiOperation(value = "查询发票和收据合计金额(用于流水领用)", notes = "查询发票和收据合计金额(用于流水领用)")
    public InvoiceAndReceiptStatisticsV statisticsAmount(@Validated @RequestBody PageF<SearchF<?>> form) {
        return invoiceAppService.statisticsAmount(form);
    }

    @GetMapping("/voidInvoice")
    @ApiOperation(value = "作废发票", notes = "作废发票")
    public Boolean voidInvoice(@RequestParam("invoiceReceiptId") @ApiParam("票据id") @NotNull(message = "票据id") Long invoiceReceiptId) {
        try (LockHelper ignored = LockHelper.tryLock(Locker.of(LockerEnum.INVOICE_BATCH_LOCK, JSON.toJSONString(invoiceReceiptId)))){
            return invoiceAppService.voidInvoice(invoiceReceiptId);
        }
    }

    @PostMapping("/invoiceChild/list")
    @ApiOperation(value = "获取发票子表数据", notes = "获取发票子表数据")
    public List<InvoiceChildDto> invoiceChildList(@Validated @RequestBody InvoiceChildF form ){
        return invoiceAppService.invoiceChildList(form);
    }

    @GetMapping("/getNuonuoInvoiceUrl")
    @ApiOperation(value = "获取发票地址", notes = "获取发票地址")
    public NuonuoInvoiceInfoDto getNuonuoInvoiceUrl(@RequestParam("invoiceReceiptId") @ApiParam("票据id") @NotNull(message = "票据id") Long invoiceReceiptId) {
        return invoiceAppService.getNuonuoInvoiceUrl(invoiceReceiptId);
    }

    @PostMapping("/invoiceBatchBlue")
    @ApiOperation(value = "开具蓝票(无校检)", notes = "开具蓝票(无校检)")
    public Long invoiceBatchBlue(@Validated @RequestBody InvoiceBatchBlueF form) {
        form.checkParam();
        return invoiceAppService.invoiceBatchBlue(form);
    }

    @PostMapping("/collect")
    @ApiOperation(value = "统一收票", notes = "统一收票")
    public InvoiceCollectionV collectInvoice(@Validated @RequestBody InvoiceCollectionF invoiceCollectionF) {
        invoiceAppService.collectInvoice(invoiceCollectionF);
        return new InvoiceCollectionV(IdentifierFactory.generateNSUUID(), 2, null);
    }

    @GetMapping("/query/collect")
    @ApiOperation(value = "查询收票结果", notes = "查询收票结果")
    public InvoiceCollectionInfoV getCollectIncvoice(@ApiParam("收票id") @RequestParam("cellectId") String cellectId) {
        return new InvoiceCollectionInfoV();
    }

    @GetMapping("/{invoiceId}")
    @ApiOperation(value = "根据发票id获取发票信息", notes = "根据发票id获取发票信息")
    public InvoiceDto getById(@ApiParam("票据id") @PathVariable("invoiceId") Long invoiceId) {
        return invoiceRepository.getByInvoiceId(invoiceId);
    }

    @GetMapping("/microApp/{invoiceIdForDetailList}/{tenantId}")
    @ApiOperation(value = "根据发票id获取发票信息以及发票明细列表", notes = "根据发票id获取发票信息以及发票明细列表")
    public InvoiceAndDetailListDto getByIdForEntityAndDetail(@ApiParam("票据id") @PathVariable("invoiceIdForDetailList") Long invoiceIdForDetailList,
                                                             @ApiParam("租户id") @PathVariable("tenantId") String tenantId) {
        return invoiceRepository.getByIdForEntityAndDetail(invoiceIdForDetailList,tenantId);
    }

    @PostMapping("/invoices")
    @ApiOperation(value = "根据发票id列表获取发票信息", notes = "根据发票id列表获取发票信息")
    public List<InvoiceDto> getByIds(@ApiParam("票据id列表") @RequestBody @Size(max = 1000, message = "票据id查询不能大于1000") List<Long> invoiceIds) {
        return invoiceRepository.getByInvoiceIds(invoiceIds);
    }

    /*-----------录入进项发票----------*/
    @PostMapping("/enterInvoice")
    @ApiOperation(value = "录入进项发票", notes = "录入进项发票")
    public Boolean enterInvoice(@Validated @RequestBody EnterInvoiceF form) {
        return invoiceAppService.enterInvoice(form);
    }

    @ApiOperation("发送发票")
    @PostMapping("/send")
    public List<InvoiceSendDto> sendInvoice(@Validated @RequestBody InvoiceSendF invoiceSendF){
        return invoiceAppService.sendInvoice(invoiceSendF);
    }

    @ApiOperation("重新处理更新发票状态")
    @PostMapping("/reHandleInvoice")
    public String reHandleInvoice(@RequestBody ReHandleInvoiceF reHandleInvoiceF) {
        invoiceAppService.reHandleInvoice(reHandleInvoiceF);
        return "success";
    }

    @ApiOperation("诺诺开票回调")
    @PostMapping("/scan")
    public InvoiceCallbackResultV scan(InvoiceCallbackF invoiceCallbackF) {
        return invoiceAppService.scan(invoiceCallbackF);
    }

    @ApiOperation(value = "获取发票备注信息", notes = "获取发票备注信息")
    @PostMapping("/getInvoiceRemarkInfo")
    public String getInvoiceRemarkInfo(@Validated @RequestBody InvoiceRemarkF form){
        return  invoiceAppService.getInvoiceRemarkInfo(form);
    }

    @ApiOperation(value = "根据收款明细id获取发票备注信息", notes = "根据收款明细id获取发票备注信息")
    @PostMapping("/getGatherInvoiceRemarkInfo")
    public String getGatherInvoiceRemarkInfo(@Validated @RequestBody InvoiceRemarkF form) {
        return invoiceAppService.getGatherInvoiceRemarkInfo(form);
    }

    @PostMapping("/claimPage")
    @ApiOperation(value = "补录发票认领分页查询", notes = "补录发票认领分页查询")
    public PageV<InvoiceClaimPageDto> claimPage(@Validated @RequestBody PageF<SearchF<?>> form) {
        return invoiceAppService.claimPage(form);
    }

    @PostMapping("/claimInvoice")
    @ApiOperation(value = "补录发票认领", notes = "补录发票认领")
    public Boolean claimInvoice(@Validated @RequestBody ClaimInvoiceF claimInvoiceF) {
        String invoiceReceiptId = String.valueOf(claimInvoiceF.getInvoiceReceiptId());
        String ids = claimInvoiceF.getBillIds().stream().map(String::valueOf).collect(Collectors.joining(","));
        IdempotentUtil.setIdempotent(invoiceReceiptId + ids, RE_RECORD_INVOICE, 3, INVOICE_RECORD_REPEAT);
        return invoiceAppService.claimInvoice(claimInvoiceF);
    }

    @ApiOperation(value = "删除发票", notes = "删除发票")
    @DeleteMapping("/delete/{invoiceReceiptId}")
    public Boolean deleteInvoice(@PathVariable("invoiceReceiptId") Long invoiceReceiptId){
        return invoiceAppService.deleteInvoice(invoiceReceiptId);
    }
    @ApiOperation(value = "纸质发票打印:获取打印信息", notes = "纸质发票打印:获取打印信息")
    @PostMapping("/getInvoicePrintInfo")
    public String getInvoicePrintF(@Validated @RequestBody InvoicePrintF form) {
        return invoiceAppService.getInvoicePrintF(form);
    }

    @ApiOperation(value = "发票预览: 获取发票预览信息", notes = "发票预览: 获取发票预览信息")
    @PostMapping("/getInvoicePreviewV")
    public InvoicePreviewV getInvoicePreviewV(@RequestBody InvoicePreviewF invoicePreviewF){
        return invoiceAppService.getInvoicePreviewV(invoicePreviewF);
    }

    @ApiOperation(value = "批量下载发票", notes = "批量下载发票")
    @PostMapping("/batch/download/zip")
    public void batchDownloadZip(@RequestBody PageF<SearchF<?>> queryF, HttpServletResponse response) {
        invoiceAppService.batchDownloadZip(queryF, response);
    }

    @ApiOperation(value = "发票文件下载", notes = "发票文件下载")
    @PostMapping("/downloadInvoice")
    public void getDownloadInvoice(HttpServletResponse response,@RequestParam String invoiceReceiptNo){
        invoiceAppService.getDownloadInvoice(invoiceReceiptNo,response);
    }
    @PostMapping("/expenseReport/{invoiceReceiptId}")
    @ApiOperation(value = "开票计提", notes = "开票计提")
    public Boolean expenseReport(@PathVariable("invoiceReceiptId") @ApiParam("票据id") Long invoiceReceiptId) {
        return invoiceAppService.expenseReport(invoiceReceiptId);
    }

    @PostMapping("/expenseReportBatch")
    @ApiOperation(value = "批量开票计提", notes = "批量开票计提")
    public Boolean expenseReportBatch(@RequestBody InvoiceExpenseReportBatchF invoiceExpenseReportBatchF) {
        return invoiceAppService.expenseReportBatch(invoiceExpenseReportBatchF);
    }

}
