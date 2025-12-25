package com.wishare.finance.apis.voucher;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.apps.model.voucher.fo.VoucherRuleF;
import com.wishare.finance.apps.model.voucher.vo.VoucherRuleEventV;
import com.wishare.finance.apps.model.voucher.vo.OldVoucherRuleV;
import com.wishare.finance.apps.service.voucher.OldVoucherRuleAppService;
import com.wishare.finance.domains.voucher.entity.VoucherRuleE;
import com.wishare.starter.Global;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.Optional;

/**
 * @description: 费项 推凭方案api
 * @author: pgq
 * @since: 2022/10/10 11:59
 * @version: 1.0.0
 */
@Api(tags = {"凭证管理推凭规则"})
@RestController
@RequestMapping("/old/voucher/rule")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class OldVoucherRuleApi {

    private final OldVoucherRuleAppService oldVoucherRuleAppService;

    @PostMapping("/page/{eventType}/{chargeItemId}")
    @ApiOperation(value = "获取规则列表", notes = "获取规则列表")
    public PageV<OldVoucherRuleV> page(@Validated @RequestBody PageF<SearchF<?>> form,
                                       @PathVariable("eventType") @ApiParam("触发事件的类型") @NotNull(message = "触发事件的类型不能为空") Integer eventType,
                                       @PathVariable("chargeItemId") @ApiParam("费项id") @NotNull(message = "费项id不能为空") Long chargeItemId) {
        Page<VoucherRuleE> page = oldVoucherRuleAppService.page(form, eventType, chargeItemId);
        if (Optional.ofNullable(page).isEmpty()) {
            return new PageV<>();
        }
        return PageV.of(form, page.getTotal(), Global.mapperFacade.mapAsList(page.getRecords(), OldVoucherRuleV.class));
    }

    @PostMapping("/page/{eventType}")
    @ApiOperation(value = "获取规则列表  新：2023-01-05版本", notes = "获取规则列表  新：2023-01-05版本")
    public PageV<OldVoucherRuleV> pageByEventType(@Validated @RequestBody PageF<SearchF<?>> form,
                                                  @PathVariable("eventType") @ApiParam("触发事件的类型") @NotNull(message = "触发事件的类型不能为空") Integer eventType) {
        Page<VoucherRuleE> page = oldVoucherRuleAppService.pageByEventType(form, eventType);
        if (Optional.ofNullable(page).isEmpty()) {
            return new PageV<>();
        }
        return PageV.of(form, page.getTotal(), Global.mapperFacade.mapAsList(page.getRecords(), OldVoucherRuleV.class));
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "获取凭证规则详情", notes = "获取凭证规则详情")
    public OldVoucherRuleV queryById(@PathVariable("id") @ApiParam("凭证规则id") @NotNull(message = "凭规则证id不能为空") Long id) {

        return oldVoucherRuleAppService.queryById(id);
    }

    @PostMapping("/add")
    @ApiOperation(value = "新增凭证规则", notes = "新增凭证规则")
    public Long add(@Validated @RequestBody VoucherRuleF voucherRuleF) {
        return oldVoucherRuleAppService.add(voucherRuleF);
    }

    @PostMapping("/update")
    @ApiOperation(value = "更新凭证规则", notes = "更新凭证规则")
    public Boolean update(@Validated @RequestBody VoucherRuleF voucherRuleF) {

        return oldVoucherRuleAppService.edit(voucherRuleF);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除凭证规则", notes = "删除凭证")
    public Boolean deleteById(@PathVariable("id") @ApiParam("凭证规则id") @NotNull(message = "凭规则证id不能为空") Long id) {
        return oldVoucherRuleAppService.deleteById(id);
    }

    @PostMapping("/enable/{id}/{disabled}")
    @ApiOperation(value = "禁/启用凭证规则", notes = "禁/启用凭证规则")
    public Boolean disabledById(@PathVariable("id") @ApiParam("推凭规则id") @NotNull(message = "凭规则证id不能为空") Long id,
        @PathVariable("disabled") @ApiParam("禁/启用状态： 0启用， 1禁用") @NotNull(message = "状态不能为空") Integer disabled) {
        return oldVoucherRuleAppService.enableById(id, disabled);
    }

    @PostMapping("/sort/{id}/{sortNum}")
    @ApiOperation(value = "拖动排序", notes = "拖动排序")
    public Boolean sortById(@PathVariable("id") @ApiParam("拖动的推凭规则的id") @NotNull(message = "凭规则证id不能为空") Long id,
        @PathVariable("sortNum") @ApiParam("拖动后数据的排序") @NotNull(message = "状态不能为空") Integer sortNum) {
        return oldVoucherRuleAppService.sortById(id, sortNum);
    }

    @PostMapping("/run/{id}")
    @ApiOperation(value = "手动运行推凭规则", notes = "手动运行推凭规则")
    public Boolean runInference(@PathVariable("id") @ApiParam("运行的推凭规则的id") @NotNull(message = "凭规则证id不能为空") Long id) {
        return oldVoucherRuleAppService.runInferenceByRule(id);
    }

    @PostMapping("/rule/list")
    @ApiOperation(value = "获取规则（新）2023-1-2", notes = "获取规则（新）2023-1-2")
    public List<VoucherRuleEventV> listRule() {
        return oldVoucherRuleAppService.listRule();
    }

}
