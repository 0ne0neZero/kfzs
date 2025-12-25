package com.wishare.finance.apis.configure.organization;

import com.wishare.finance.apps.model.configure.organization.fo.AddStatutoryInvoiceConfF;
import com.wishare.finance.apps.model.configure.organization.fo.StatutoryInvoiceConfListF;
import com.wishare.finance.apps.model.configure.organization.fo.UpdateStatutoryInvoiceConfF;
import com.wishare.finance.apps.model.configure.organization.vo.StatutoryInvoiceConfV;
import com.wishare.finance.apps.service.configure.organization.StatutoryInvoiceConfAppService;
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
 * @date 2022/11/1
 * @Description:
 */
@Api(tags = {"电子开票设置"})
@RestController
@RequestMapping("/statutoryInvoiceConf")
@RequiredArgsConstructor
public class StatutoryInvoiceConfApi {

    private final StatutoryInvoiceConfAppService statutoryInvoiceConfAppService;

    @ApiOperation(value = "新增", notes = "新增")
    @PostMapping("/add")
    public Long addStatutoryInvoiceConf(@RequestBody @Validated AddStatutoryInvoiceConfF form) {
        return statutoryInvoiceConfAppService.addStatutoryInvoiceConf(form);
    }

    @ApiOperation(value = "修改", notes = "修改")
    @PostMapping("/update")
    public Long updateStatutoryInvoiceConf(@RequestBody @Validated UpdateStatutoryInvoiceConfF form) {
        return statutoryInvoiceConfAppService.updateStatutoryInvoiceConf(form);
    }

    @ApiOperation(value = "删除", notes = "删除")
    @DeleteMapping("/delete/{id}")
    public Boolean deleteStatutoryInvoiceConf(@PathVariable("id") Long id) {
        return statutoryInvoiceConfAppService.deleteStatutoryInvoiceConf(id);
    }


    @PostMapping("/page")
    @ApiOperation(value = "分页查询", notes = "分页查询")
    public PageV<StatutoryInvoiceConfV> queryPage(@Validated @RequestBody PageF<SearchF<?>> form) {
        return statutoryInvoiceConfAppService.queryPage(form);
    }

    @PostMapping("/list")
    @ApiOperation(value = "列表查询", notes = "列表查询")
    public List<StatutoryInvoiceConfV> queryList(@Validated @RequestBody StatutoryInvoiceConfListF form) {
        return statutoryInvoiceConfAppService.queryList(form);
    }

    @GetMapping("/get/{id}")
    @ApiOperation(value = "诺诺token查询", notes = "诺诺token查询")
    public String queryGet(@PathVariable("id") Long id) {
        return statutoryInvoiceConfAppService.queryGet(id);
    }


    @GetMapping("/queryClerkList/{statutoryBodyId}")
    @ApiOperation(value = "根据法定单位查询开票人列表", notes = "根据法定单位查询开票人列表")
    public List<String> queryClerkList(@PathVariable("statutoryBodyId") Long statutoryBodyId) {
        return  statutoryInvoiceConfAppService.queryClerkList(statutoryBodyId);
    }

}
