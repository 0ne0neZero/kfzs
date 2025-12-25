package com.wishare.finance.apis.invoicereceipt;

import java.io.IOException;

import com.alibaba.fastjson.JSON;
import com.wishare.finance.apps.model.invoice.invoice.dto.DeveloperPayV;
import com.wishare.finance.apps.model.invoice.invoice.dto.InvoiceReceiptDto;
import com.wishare.finance.apps.model.invoice.invoice.dto.ReceiptDetailDto;
import com.wishare.finance.apps.model.invoice.invoice.dto.ReceiptStatisticsDto;
import com.wishare.finance.apps.model.invoice.invoice.fo.*;
import com.wishare.finance.apps.model.invoice.invoice.dto.ReceiptVDto;
import com.wishare.finance.apps.model.invoice.invoice.vo.InvoiceReceiptDetailV;
import com.wishare.finance.apps.model.invoice.invoice.vo.ReceiptV;
import com.wishare.finance.apps.model.signature.EsignResultV;
import com.wishare.finance.apps.service.spacePermission.SpacePermissionAppService;
import com.wishare.finance.apps.service.strategy.receipt.ReceiptTenantYY;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceReceiptE;
import com.wishare.finance.domains.invoicereceipt.service.InvoiceReceiptDomainService;
import com.wishare.finance.apps.service.invoicereceipt.ReceiptAppService;
import com.wishare.finance.domains.invoicereceipt.service.InvoiceDomainService;
import com.wishare.finance.infrastructure.support.lock.LockHelper;
import com.wishare.finance.infrastructure.support.lock.Locker;
import com.wishare.finance.infrastructure.support.lock.LockerEnum;
import com.wishare.finance.infrastructure.utils.IdempotentUtil;
import com.wishare.finance.infrastructure.utils.TenantUtil;
import com.wishare.starter.Global;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.wishare.finance.infrastructure.conts.ErrorMessage.RE_VOID_RECEIPT;
import static com.wishare.finance.infrastructure.utils.IdempotentUtil.IdempotentEnum.VOID_RECEIPT;

/**
 * @author xujian
 * @date 2022/9/23
 * @Description:
 */
@Api(tags = {"收据管理"})
@RestController
@RequestMapping("/receipt")
@RequiredArgsConstructor
public class ReceiptApi {

    private final ReceiptAppService receiptAppService;

    private final InvoiceDomainService invoiceDomainService;

    private final SpacePermissionAppService spacePermissionAppService;

    /**
     * 纸质发票
     * 电子收据
     * @param form
     * @return
     */
    @PostMapping("/receiptBatch")
    @ApiOperation(value = "开具收据", notes = "开具收据")
    public Long invoiceBatch(@Validated @RequestBody ReceiptBatchF form) {
        try (LockHelper ignored = LockHelper.tryLock(Locker.of(LockerEnum.INVOICE_BATCH_LOCK, String.valueOf(JSON.toJSONString(form.getBillIds()).hashCode())))){
            form.checkParam();
            return receiptAppService.invoiceBatch(form);
        }
    }

    @PostMapping("/developerPay/receiptBatch")
    @ApiOperation(value = "开发代付凭据", notes = "开发代付凭据")
    public DeveloperPayV invoiceBatchDeveloperPay(@Validated @RequestBody ReceiptBatchF form) {

        return receiptAppService.invoiceBatchDeveloperPay(form);

    }

    /**
     *
     * @param form
     * @return
     */
    @PostMapping("/receiptSend")
    @ApiOperation(value = "收据下发")
    public String receiptSend(@Validated @RequestBody ReceiptSendF form) {
        return receiptAppService.receiptSend(form);
    }


    /**
     * 当前接口只开放中交
     * @param type 1 开  2作废
     * @param id
     * @return
     */
    @GetMapping("/eSignReceipt/{type}/{id}")
    @ApiOperation(value = "e签宝贝签署文件", notes = "e签宝贝签署文件")
    public Boolean eSignReceipt(@PathVariable("type")Integer type,@PathVariable("id")Long id) {
        try (LockHelper ignored = LockHelper.tryLock(Locker.of(LockerEnum.E_SIGN_RECEIPT_LOCK,String.valueOf(id)))){
            receiptAppService.eSignReceipt(type,id);
        }
        return true;
    }



    /**
     * 轮询获取签章信息
     * 涉及到轮询 可以借助redis提高速度
     * @return
     */
    @GetMapping("/querySignResult/{invoiceReceiptId}")
    @ApiOperation(value = "获取申请签章结果信息", notes = "案例146584896242184")
    public EsignResultV querySignResult(@PathVariable("invoiceReceiptId") @ApiParam("票据表id") @NotNull(message = "预收账单id不能为空") Long invoiceReceiptId) {
        return receiptAppService.querySignResult(invoiceReceiptId);
    }

    /**
     * 轮询获取作废结果
     * 涉及到轮询 可以借助redis提高速度
     * @return
     */
    @GetMapping("/queryVoidResult/{invoiceReceiptId}")
    @ApiOperation(value = "获取作废签章结果信息", notes = "案例146584896242184")
    public EsignResultV voidSignResult(@PathVariable("invoiceReceiptId") @ApiParam("票据表id") @NotNull(message = "预收账单id不能为空") Long invoiceReceiptId) {
        return receiptAppService.queryVoidResult(invoiceReceiptId);
    }


    @PostMapping("/page")
    @ApiOperation(value = "分页查询收据列表", notes = "分页查询收据列表")
    public PageV<ReceiptV> queryPage(@Validated @RequestBody PageF<SearchF<?>> form) {
        PageF<SearchF<?>> queryPermissionF = spacePermissionAppService.getComminutyAndRoomV(form,"ird");
        if(Objects.isNull(queryPermissionF)){
            return PageV.of(form);
        }
        return receiptAppService.queryPage(form);
    }


    @PostMapping("/getReceiptByGatherDetailIds")
    @ApiOperation(value = "根据收款明细ids获取对应收据发票信息", notes = "根据收款明细ids获取对应收据发票信息")
    public InvoiceReceiptDto getReceiptByGatherDetailIds(@RequestBody List<Long> gatherDetailIds,@RequestParam("supCpUnitId") String supCpUnitId) {
        return receiptAppService.getReceiptByGatherDetailIds(gatherDetailIds,supCpUnitId);
    }





    @PostMapping("/statistics")
    @ApiOperation(value = "统计收据信息", notes = "统计收据信息")
    public ReceiptStatisticsDto statistics(@Validated @RequestBody PageF<SearchF<?>> form) {
        PageF<SearchF<?>> queryPermissionF = spacePermissionAppService.getComminutyAndRoomV(form,"ird");
        if(Objects.isNull(queryPermissionF)){
            return new ReceiptStatisticsDto();
        }
        return receiptAppService.statistics(form);
    }

    @GetMapping("/voidReceipt")
    @ApiOperation(value = "作废收据（回收）", notes = "作废收据")
    public Boolean voidReceipt(@RequestParam("invoiceReceiptId") @ApiParam("票据id") @NotNull(message = "票据id") Long invoiceReceiptId
            , @NotBlank(message = "上级收费单元id不能为空") @RequestParam("supCpUnitId") @ApiParam("上级收费单元id") String supCpUnitId) {
        IdempotentUtil.setIdempotent(invoiceReceiptId + supCpUnitId, VOID_RECEIPT, 5, RE_VOID_RECEIPT);

        return receiptAppService.voidReceipt(invoiceReceiptId, supCpUnitId);
    }


    /**
     * 作废收据 目前针对中交e签宝
     *
     * @param form
     * @return
     */
    @PostMapping("/voidReceiptV")
    @ApiOperation(value = "作废收据(e签宝作废)", notes = "把收据作废")
    public Boolean voidReceiptV(@Validated @RequestBody ReceiptVoidF form) {
        return receiptAppService.voidReceiptV(form);
    }


    @PostMapping("/detail")
    @ApiOperation(value = "获取收据详情", notes = "获取收据详情")
    public ReceiptDetailDto detail(@Validated @RequestBody ReceiptDetailF form){
        ReceiptDetailDto detail = receiptAppService.detail(form);
        if(TenantUtil.bf2()){
            List<InvoiceReceiptDetailV> invoiceReceiptDetail = detail.getInvoiceReceiptDetail();
            if(!CollectionUtils.isEmpty(invoiceReceiptDetail)){
                invoiceReceiptDetail.forEach(e->{
                    if(StringUtils.isBlank(e.getExpensePeriod())){
                        e.setExpensePeriod("预存");
                        e.setExpensePeriodList(Collections.singletonList("预存"));
                    }
                });
            }
        }
        return detail;
    }


    @PostMapping("/query/invoiceReceiptId/{billId}")
    @ApiOperation(value = "根据账单id获取收据主表id", notes = "根据账单id获取收据主表id")
    public Long getInvoiceReceiptIdByBillId(@PathVariable("billId") Long billId){
        return invoiceDomainService.getInvoiceReceiptIdByBillId(billId);
    }



    @PostMapping("/edit")
    @ApiOperation(value = "编辑收据", notes = "编辑收据")
    public Boolean editReceipt(@Validated @RequestBody EditReceiptF form){
        return receiptAppService.editReceipt(form);
    }

    @PostMapping("/repairReceipt")
    @ApiOperation(value = "修复收据错乱数据", notes = "修复收据错乱数据")
    public Boolean repairReceipt(){
        return receiptAppService.repairReceipt();
    }

    @PostMapping("/updateBillPayTime")
    @ApiOperation(value = "修复票据缴费时间", notes = "修复票据缴费时间")
    public Boolean updateBillPayTime(){
        return receiptAppService.updateBillPayTime();
    }


    @GetMapping("/queryByInvoiceReceiptId")
    @ApiOperation(value = "根据收据主表id查询收据信息", notes = "根据收据主表id查询收据信息")
    public ReceiptVDto queryByInvoiceReceiptId(Long invoiceReceiptId){
        return receiptAppService.queryByInvoiceReceiptId(invoiceReceiptId);
    }


    @GetMapping("/redirect")
    @ApiOperation(value = "重定向控制签署文件是否失效", notes = "当前接口只提供给远洋使用")
    public void redirect(@RequestParam("content") String content, HttpServletResponse response) throws IOException {
        String url = Global.ac.getBean(ReceiptTenantYY.class).eSignUrl(content);
        if(StringUtils.isBlank(url)){
            // 设置字符编码和内容类型
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/plain; charset=UTF-8");
            // 链接失效，返回提示信息
            response.getWriter().write("当前文件已失效");
        }else{
            // 重定向到指定的URL
            response.sendRedirect(url);
        }
    }

    @ApiOperation(value = "收据文件下载", notes = "收据文件下载")
    @PostMapping("/downloadReceipt")
    public void getDownloadInvoice(HttpServletResponse response,@RequestParam Long id){
        receiptAppService.getDownloadReceipt(id,response);
    }

    @ApiOperation(value = "批量下载收据", notes = "批量下载收据")
    @PostMapping("/batch/download/zip")
    public void batchDownloadZip(@RequestBody PageF<SearchF<?>> queryF, HttpServletResponse response) {
        receiptAppService.batchDownloadZip(queryF, response);
    }
}
