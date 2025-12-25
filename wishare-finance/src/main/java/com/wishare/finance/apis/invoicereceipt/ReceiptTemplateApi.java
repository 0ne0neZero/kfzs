package com.wishare.finance.apis.invoicereceipt;

import com.wishare.finance.apps.model.invoice.receipttemplate.fo.TemplateF;
import com.wishare.finance.apps.model.invoice.receipttemplate.vo.TemplateV;
import com.wishare.finance.apps.model.invoice.receipttemplate.vo.TemplateStyleV;
import com.wishare.finance.apps.model.invoice.receipttemplate.vo.TemplateTypeV;
import com.wishare.finance.apps.pushbill.validation.AddGroup;
import com.wishare.finance.apps.pushbill.validation.UpdateGroup;
import com.wishare.finance.apps.service.invoicereceipt.ReceiptTemplateAppService;
import com.wishare.finance.infrastructure.utils.TenantUtil;
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
 * @author: Linitly
 * @date: 2023/8/7 16:35
 * @descrption:
 */
@Api(tags = {"票据模板管理"})
@RestController
@RequestMapping("/receipt/template")
@RequiredArgsConstructor
public class ReceiptTemplateApi {

    private final ReceiptTemplateAppService receiptTemplateAppService;

    @GetMapping("/type/select")
    @ApiOperation(value = "获取票据模板类型", notes = "获取票据模板类型")
    public List<TemplateTypeV> typeSelect() {
        return receiptTemplateAppService.typeSelect();
    }

    @GetMapping("/style/select")
    @ApiOperation(value = "获取票据模板样式", notes = "获取票据模板样式")
    public List<TemplateStyleV> styleSelect() {
        return receiptTemplateAppService.styleSelect();
    }

    @PostMapping("/add")
    @ApiOperation(value = "新建票据模板", notes = "新建票据模板")
    public Long add(@RequestBody @Validated({AddGroup.class}) TemplateF templateF) {
        templateF.check();
        return receiptTemplateAppService.add(templateF);
    }

    @PostMapping("/update")
    @ApiOperation(value = "更新票据模板", notes = "更新票据模板")
    public Boolean update(@RequestBody @Validated({UpdateGroup.class}) TemplateF templateF) {
        templateF.check();
        return receiptTemplateAppService.update(templateF);
    }

    @PostMapping("/page/list")
    @ApiOperation(value = "分页获取票据模板列表", notes = "分页获取票据模板列表")
    public PageV<TemplateV> pageList(@Validated @RequestBody PageF<SearchF<?>> form) {
        return receiptTemplateAppService.pageList(form);
    }

    @GetMapping("/get/{id}")
    @ApiOperation(value = "获取票据模板", notes = "获取票据模板")
    public TemplateV get(@PathVariable Long id) {
        return receiptTemplateAppService.get(id);
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "删除票据模板", notes = "删除票据模板")
    public Boolean delete(@PathVariable Long id) {
        return receiptTemplateAppService.delete(id);
    }

    @GetMapping("/list/select")
    @ApiOperation(value = "获取可用的票据模板列表", notes = "获取可用的票据模板列表")
    public List<TemplateV> list() {
        return receiptTemplateAppService.list();
    }

    @GetMapping("/demo/url/{code}")
    @ApiOperation(value = "获取示例图片地址-传入样式code", notes = "获取示例图片地址-传入样式code")
    public String demoUrl(@PathVariable String code) {
        return receiptTemplateAppService.demoUrl(code);
    }
}

