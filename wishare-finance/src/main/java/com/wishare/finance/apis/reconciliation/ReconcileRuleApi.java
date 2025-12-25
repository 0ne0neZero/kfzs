package com.wishare.finance.apis.reconciliation;

import com.wishare.finance.apps.model.reconciliation.fo.AddReconcileRuleF;
import com.wishare.finance.apps.model.reconciliation.fo.UpdateReconcileRuleF;
import com.wishare.finance.apps.model.reconciliation.vo.ReconcileRuleBaseV;
import com.wishare.finance.apps.model.reconciliation.vo.ReconcileRuleV;
import com.wishare.finance.apps.service.reconciliation.ReconcileRuleAppService;
import com.wishare.finance.domains.reconciliation.enums.ReconcileModeEnum;
import com.wishare.finance.domains.reconciliation.repository.ReconcileRuleRepository;
import com.wishare.starter.Global;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 对账规则
 *
 * @Author dxclay
 * @Date 2022/10/12
 * @Version 1.0
 */
@Api(tags = {"对账规则"})
@RestController
@RequestMapping("/reconcile/rule")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ReconcileRuleApi {

    private final ReconcileRuleRepository reconcileRuleRepository;
    private final ReconcileRuleAppService reconcileRuleAppService;

    @PostMapping("/add")
    @ApiOperation(value = "新增对账规则", notes = "新增对账规则")
    public Long add(@Validated @RequestBody AddReconcileRuleF addReconcileRuleF) {
        return reconcileRuleAppService.add(addReconcileRuleF);
    }

    @PostMapping("/update")
    @ApiOperation(value = "更新对账规则", notes = "更新对账规则")
    public boolean update(@Validated @RequestBody UpdateReconcileRuleF updateReconcileRuleF) {
        return reconcileRuleAppService.update(updateReconcileRuleF);
    }

    @GetMapping("/{reconcileId}")
    @ApiOperation(value = "查询对账规则", notes = "查询对账规则")
    public ReconcileRuleV query(@PathVariable("reconcileId") String reconcileId) {
        return Global.mapperFacade.map(reconcileRuleRepository.getById(reconcileId), ReconcileRuleV.class);
    }

    @GetMapping("/query/mode")
    @ApiOperation(value = "根据对账模式查询对账规则", notes = "根据对账模式查询对账规则")
    public ReconcileRuleV getByMode(@RequestParam("reconcileMode") Integer reconcileMode) {
        return Global.mapperFacade.map(reconcileRuleAppService.getOrAddRuleByMode(ReconcileModeEnum.valueOfByCode(reconcileMode).getCode()), ReconcileRuleV.class);
    }

    @GetMapping("/query/list")
    @ApiOperation(value = "查询对账规则列表", notes = "查询对账规则列表")
    public List<ReconcileRuleV> getList() {
        return Global.mapperFacade.mapAsList(reconcileRuleRepository.getListByTenant(), ReconcileRuleV.class);
    }

    @GetMapping("/query/tree")
    @ApiOperation(value = "查询对账规则树", notes = "查询对账规则树")
    public List<ReconcileRuleBaseV> getTree() {
        return Global.mapperFacade.mapAsList(reconcileRuleAppService.getTreeByTenant(), ReconcileRuleBaseV.class);
    }

}
