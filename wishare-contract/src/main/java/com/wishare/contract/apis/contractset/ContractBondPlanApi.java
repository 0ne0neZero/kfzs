package com.wishare.contract.apis.contractset;

import com.wishare.contract.apps.fo.contractset.*;
import com.wishare.contract.apps.remote.fo.ReceiptDetailRf;
import com.wishare.contract.apps.remote.vo.ReceiptDetailRv;
import com.wishare.contract.apps.service.contractset.ContractBondPlanAppService;
import com.wishare.contract.domains.vo.contractset.ContractBondPlanPageV;
import com.wishare.contract.domains.vo.contractset.ContractBondPlanSumV;
import com.wishare.contract.domains.vo.contractset.ContractBondPlanV;
import com.wishare.contract.domains.vo.contractset.ContractReceiptResultV;
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
 * <p>
 * 合同保证金计划信息
 * </p>
 *
 * @author wangrui
 * @since 2022-09-09
 */
@RequiredArgsConstructor
@RestController
@Api(tags = {"合同保证金计划信息"})
@RequestMapping("/contractBondPlan")
public class ContractBondPlanApi {

    private final ContractBondPlanAppService contractBondPlanAppService;

    @ApiOperation(value = "保证金列表", notes = "保证金列表", response = ContractBondPlanV.class)
    @PostMapping("/list")
    public List<ContractBondPlanV> listContractBondPlan(@RequestBody ContractBondPlanF contractBondPlanF) {
        return contractBondPlanAppService.listContractBondPlan(contractBondPlanF);
    }

    @ApiOperation(value = "新增/更新保证金计划", notes = "新增/更新保证金计划")
    @PostMapping("/save")
    public void saveContractBondPlan(@Validated @RequestBody List<ContractBondPlanSaveF> contractBondPlanF) {
        contractBondPlanAppService.saveContractBondPlan(contractBondPlanF);
    }

    @ApiOperation(value = "删除保证金计划", notes = "删除保证金计划")
    @DeleteMapping("/remove")
    public void removeContractBondPlan(@RequestParam("id") Long id) {
        contractBondPlanAppService.removeContractBondPlan(id);
    }

    @ApiOperation(value = "保证金分页查询", notes = "保证金分页查询", response = ContractBondPlanPageV.class)
    @PostMapping("/page")
    public PageV<ContractBondPlanPageV> pageContractBondPlan(@RequestBody PageF<SearchF<?>> form) {
        return contractBondPlanAppService.pageContractBondPlan(form);
    }

    @ApiOperation(value = "保证金分页总金额", notes = "保证金分页总金额", response = ContractBondPlanSumV.class)
    @PostMapping("/page/sum")
    public ContractBondPlanSumV pageContractBondPlanSum(@RequestBody PageF<SearchF<?>> form) {
        return contractBondPlanAppService.pageContractBondPlanSum(form);
    }

    @ApiOperation(value = "保证金批量收款",notes = "保证金批量收款")
    @PostMapping("/batchCollection")
    public void batchCollection(@RequestBody ContractBondPlanCollectionF form) {
        contractBondPlanAppService.batchCollection(form);
    }

    @ApiOperation(value = "保证金批量付/退款",notes = "保证金批量付/退款")
    @PostMapping("/batchPayment")
    public void batchPayment(@RequestBody ContractBondPlanPaymentF form) {
        contractBondPlanAppService.batchPayment(form);
    }

    @ApiOperation(value = "保证金计划结转",notes = "保证金计划结转")
    @PostMapping("/carryover")
    public void carryover(@RequestBody @Validated ContractBondPlanCarryoverF from) {
        contractBondPlanAppService.carryover(from);
    }

    @ApiOperation(value = "保证金开收据",notes = "保证金开收据")
    @PostMapping("/receipt")
    public ContractReceiptResultV receipt(@RequestBody @Validated BondPlanReceiptF from) {
        return contractBondPlanAppService.receipt(from);
    }

    @ApiOperation(value = "保证金批量扣款",notes = "保证金批量扣款")
    @PostMapping("/batchDeduction")
    public void batchDeduction(@RequestBody ContractBondPlanDeductionF form) {
        contractBondPlanAppService.batchDeduction(form);
    }


}
