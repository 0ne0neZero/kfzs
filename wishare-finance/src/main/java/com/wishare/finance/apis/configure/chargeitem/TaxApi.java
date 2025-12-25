package com.wishare.finance.apis.configure.chargeitem;

import com.wishare.finance.apps.model.configure.chargeitem.fo.AddTaxCategoryF;
import com.wishare.finance.apps.model.configure.chargeitem.fo.AddTaxRateF;
import com.wishare.finance.apps.model.configure.chargeitem.fo.GetTaxCategoryF;
import com.wishare.finance.apps.model.configure.chargeitem.fo.SyncTaxRateF;
import com.wishare.finance.apps.model.configure.chargeitem.fo.UpdateTaxCategoryF;
import com.wishare.finance.apps.model.configure.chargeitem.fo.UpdateTaxRateF;
import com.wishare.finance.apps.model.configure.chargeitem.vo.*;
import com.wishare.finance.apps.service.configure.chargeitem.TaxAppService;
import com.wishare.finance.domains.configure.chargeitem.entity.BPMDeptE;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = {"税种税率管理"})
@RestController
@RequestMapping("/tax")
@RequiredArgsConstructor
public class TaxApi {
    private final TaxAppService taxAppService;

    @ApiOperation(value = "新增税种信息", notes = "新增税种信息")
    @PostMapping("/category/add")
    public Long addTaxCategory(@RequestBody @Validated AddTaxCategoryF form) {
        return taxAppService.addTaxCategory(form);
    }

    @ApiOperation(value = "修改税种信息", notes = "修改税种信息")
    @PostMapping("/category/update")
    public Long updateTaxCategory(@RequestBody @Validated UpdateTaxCategoryF form) {
        return taxAppService.updateTaxCategory(form);
    }

    @ApiOperation(value = "删除税种信息", notes = "删除税种信息")
    @DeleteMapping("/category/{id}")
    public Boolean deleteTaxCategory(@PathVariable("id") Long id) {
        return taxAppService.deleteTaxCategory(id);
    }

    @ApiOperation(value = "根据id获取税种信息", notes = "根据id获取税种信息")
    @GetMapping("/category/{id}")
    public TaxCategoryV getTaxCategoryById(@PathVariable("id") Long id) {
        return taxAppService.getTaxCategoryById(id);
    }

    @ApiOperation(value = "获取税种信息列表", notes = "获取税种信息列表")
    @PostMapping("/category/list")
    public List<TaxCategoryV> taxCategoryList(@RequestBody @Validated GetTaxCategoryF form, @RequestParam(value = "chargeItemId",required = false) Long chargeItemId) {
        List<TaxCategoryV> result = taxAppService.taxCategoryList(form, chargeItemId);
        return result;
    }

    @ApiOperation(value = "获取税种信息树形图", notes = "获取税种信息树形图")
    @PostMapping("/category/tree")
    public List<TaxCategoryTree> taxCategoryTrees(@RequestBody @Validated GetTaxCategoryF form) {
        List<TaxCategoryTree> taxCategoryTrees = taxAppService.taxCategoryTrees(form);
        return taxCategoryTrees;
    }

    @ApiOperation(value = "新增税率信息", notes = "新增税率信息")
    @PostMapping("/rate/add")
    public Long addTaxRate(@RequestBody @Validated AddTaxRateF form) {
        return taxAppService.addTaxRate(form);
    }

    @ApiOperation(value = "修改税率信息", notes = "新增税率信息")
    @PostMapping("/rate/update")
    public Long updateTaxRate(@RequestBody @Validated UpdateTaxRateF form) {
        return taxAppService.updateTaxRate(form);
    }

    @ApiOperation(value = "删除税率信息", notes = "删除税率信息")
    @DeleteMapping("/rate/{id}")
    public Boolean deleteTaxRate(@PathVariable("id") Long id) {
        return taxAppService.deleteTaxRate(id);
    }

    @ApiOperation(value = "根据税率id获取税率详情", notes = "根据税率id获取税率详情")
    @GetMapping("/rate/detail/{id}")
    public TaxRateDetailV rateDetail(@PathVariable("id") Long id) {
        return taxAppService.rateDetail(id);
    }

    @ApiOperation(value = "同步税种税率", notes = "同步税种税率")
    @PostMapping("/sync")
    public Boolean syncTaxRate(@RequestBody List<SyncTaxRateF> syncTaxRateFS) {
        return taxAppService.syncTaxRate(syncTaxRateFS);
    }

    @ApiOperation(value = "获取BPM过滤项税率列表", notes = "获取BPM过滤项税率列表")
    @GetMapping("/BPM/list")
    public List<TaxRateV> BPMFilterTaxList() {
        return taxAppService.BPMFilterTaxList();
    }

    @ApiOperation(value = "获取BPM过滤项部门列表", notes = "获取BPM过滤项部门列表")
    @GetMapping("/BPM/deptList")
    public List<BPMDeptV> BPMFilterDeptList() {
        return taxAppService.BPMFilterDeptList();
    }

}
