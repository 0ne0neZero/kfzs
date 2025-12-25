package com.wishare.finance.apis.invoicereceipt;

import com.wishare.finance.apps.model.invoice.invoice.dto.InvoiceReceiveDto;
import com.wishare.finance.apps.model.invoice.invoice.fo.GetInvoiceReceiptNoF;
import com.wishare.finance.apps.model.invoice.invoice.fo.InvoiceBookListF;
import com.wishare.finance.apps.model.invoice.invoicebook.dto.ReceiveDetailDto;
import com.wishare.finance.apps.model.invoice.invoicebook.fo.AddInvoiceBookF;
import com.wishare.finance.apps.model.invoice.invoicebook.fo.InvoiceReceiveF;
import com.wishare.finance.apps.model.invoice.invoicebook.vo.InvoiceBookV;
import com.wishare.finance.apps.service.invoicereceipt.InvoiceBookAppService;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author xujian
 * @date 2022/8/31
 * @Description:
 */
@Api(tags = {"发票购领管理"})
@RestController
@RequestMapping("/invoiceBook")
@RequiredArgsConstructor
public class InvoiceBookApi {

    private final InvoiceBookAppService invoiceBookAppService;

    @ApiOperation(value = "新增票本", notes = "新增票本")
    @PostMapping("/add")
    public Long addInvoiceBook(@RequestBody @Validated AddInvoiceBookF form) {
        form.check();
        return invoiceBookAppService.addInvoiceBook(form);
    }

    @ApiOperation(value = "票本领用", notes = "申请领用审核")
    @PostMapping("/receive")
    public Boolean receive(@RequestBody @Validated InvoiceReceiveF form) {
        return invoiceBookAppService.receive(form);
    }

    @PostMapping("/page")
    @ApiOperation(value = "分页查询票本列表", notes = "分页查询票本列表")
    public PageV<InvoiceBookV> queryPage(@Validated @RequestBody PageF<SearchF<?>> form) {
        return invoiceBookAppService.queryPage(form);
    }

    @PostMapping("/list")
    @ApiOperation(value = "根据条件查询票本列表", notes = "根据条件查询票本列表")
    public List<InvoiceBookV> querylist(@Validated @RequestBody InvoiceBookListF form) {
        return invoiceBookAppService.querylist(form);
    }

    @ApiOperation(value = "删除票本", notes = "删除票本")
    @DeleteMapping("/delete/{id}")
    public Boolean deleteInvoiceBook(@PathVariable("id") Long id) {
        return invoiceBookAppService.deleteInvoiceBook(id);
    }

    @ApiOperation(value = "根据票本id获取票本详情", notes = "根据id获取票本详情")
    @GetMapping("/detail/{id}")
    public InvoiceBookV detailInvoiceBook(@PathVariable("id") Long id) {
        InvoiceBookV result = invoiceBookAppService.detailInvoiceBook(id);
        return result;
    }

    @PostMapping("/receive/page")
    @ApiOperation(value = "分页查询发票领用列表", notes = "分页查询发票领用列表")
    public PageV<InvoiceReceiveDto> receiveQueryPage(@Validated @RequestBody PageF<SearchF<?>> form) {
        return invoiceBookAppService.receiveQueryPage(form);
    }


    @ApiOperation(value = "根据票本id获取票本领用详情", notes = "根据票本id获取票本领用详情")
    @GetMapping("/receive/detail/{id}")
    public ReceiveDetailDto receiveDetail(@PathVariable("id") Long id) {
        ReceiveDetailDto result = invoiceBookAppService.receiveDetail(id);
        return result;
    }


    @ApiOperation(value = "获取可领用发票号", notes = "获取可领用发票号")
    @PostMapping("/getInvoiceReceiptNo")
    public List<String> getInvoiceReceiptNo(@Validated @RequestBody GetInvoiceReceiptNoF form) {
        List<String> result = invoiceBookAppService.getInvoiceReceiptNo(form);
        return result;
    }

}
