package com.wishare.contract.apis.revision.pay;

import com.wishare.contract.apps.fo.revision.ContractPlanDateF;
import com.wishare.contract.apps.fo.revision.pay.*;
import com.wishare.contract.apps.fo.revision.pay.settlement.GenerateCostPlanF;
import com.wishare.contract.domains.dto.settlementPlan.SettlementPlanResult;
import com.wishare.contract.domains.service.revision.pay.ContractPayPlanConcludeService;
import com.wishare.contract.domains.vo.ContractPlanDateV;
import com.wishare.contract.domains.vo.contractset.ContractConcludeV;
import com.wishare.contract.domains.vo.revision.pay.*;
import com.wishare.contract.domains.vo.settle.ExportSettlePlanListV;
import com.wishare.contract.domains.vo.settle.PreSettlePlanDataV2;
import com.wishare.contract.domains.vo.settle.PreSettlePlanV;
import com.wishare.contract.domains.vo.settle.SettlePlanListV;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.enums.PromptInfo;
import com.wishare.tools.starter.fo.search.SearchF;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author zhangfuyu
 * @mender 龙江锋
 * @Date 2023/7/6/11:21
 */
@RequiredArgsConstructor
@RestController
@Validated
@Api(tags = {"支出合同付款计划信息表"})
@RequestMapping("/contractPayPlanConclude")
public class ContractPayPlanConcludeApi {

    private final ContractPayPlanConcludeService contractPayPlanConcludeService;

    @ApiOperation(value = "根据id查询详情", notes = "根据id查询详情", response = ContractConcludeV.class)
    @GetMapping("/getDetailsById")
    public ContractPayPlanDetailsV getDetailsById(@RequestParam("contractPlanId") String contractPlanId) {
        return contractPayPlanConcludeService.getDetailsById(contractPlanId);
    }

    @ApiOperation(value = "根据合同id查询内部信息-后端内部使用", notes = "根据合同id查询内部信息-后端内部使用", response = ContractPayPlanInnerInfoV.class)
    @PostMapping("/getInnerInfoByContractIds")
    public List<ContractPayPlanInnerInfoV> getInnerInfoByContractId(@RequestBody List<String> contractIds){
        return contractPayPlanConcludeService.getInnerInfoByContractId(contractIds);
    }

    @ApiOperation(value = "付款计划分页列表", notes = "收款/付款计划分页查询", response = ContractPayPlanConcludeV.class)
    @PostMapping("/page")
    public PageV<ContractPayPlanConcludeV> page(@RequestBody PageF<SearchF<ContractPayPlanConcludePageF>> request) {
        return contractPayPlanConcludeService.page(request);
    }

    @ApiOperation(value = "付款计划查询分页列表", notes = "收款/付款计划查询分页列表", response = ContractPayPlanConcludeV.class)
    @PostMapping("/pageInfo")
    public PageV<ContractPayPlanConcludeInfoV> pageInfo(@RequestBody PageF<SearchF<ContractPayPlanConcludePageF>> request) {
        return contractPayPlanConcludeService.pageInfo(request);
    }

    @ApiOperation(value = "编辑列表", notes = "下拉列表，默认数量20", response = ContractPayPlanConcludeV.class)
    @PostMapping("/list")
    public List<ContractPayPlanConcludeV> list(@Validated @RequestBody ContractPayPlanConcludeListF contractPayPlanConcludeListF){
        return contractPayPlanConcludeService.list(contractPayPlanConcludeListF);
    }

    @ApiOperation(value = "编辑列表", notes = "下拉列表，默认数量20", response = ContractPayPlanConcludeV.class)
    @PostMapping("/listInfo")
    public List<ContractPayPlanConcludeInfoV> listInfo(@Validated @RequestBody ContractPayPlanConcludeListF contractPayPlanConcludeListF){
        return contractPayPlanConcludeService.listInfo(contractPayPlanConcludeListF);
    }

    @ApiOperation(value = "查询可选择付款列表", notes = "下拉列表，默认数量20", response = ContractPayPlanConcludeV.class)
    @PostMapping("/listPid")
    public ContractPayPlanPidConcludeV listPid(@Validated @RequestBody ContractPayPlanConcludeListF contractPayPlanConcludeListF){
        return contractPayPlanConcludeService.listPid(contractPayPlanConcludeListF);
    }

    @ApiOperation(value = "查询可选择付款列表-新")
    @PostMapping("/listPidNew")
    public ContractPayPlanPidConcludeV listPidNew(@Validated @RequestBody ContractPayPlanConcludeListF contractPayPlanConcludeListF) {
        return contractPayPlanConcludeService.listPidNew(contractPayPlanConcludeListF);
    }

    @ApiOperation(value = "合同台账金额统计", response = ContractPayPlanConcludeSumV.class)
    @PostMapping("/accountAmountSum")
    public ContractPayPlanConcludeSumV accountAmountSum(@RequestBody PageF<SearchF<ContractPayPlanConcludePageF>> request) {
        return contractPayPlanConcludeService.accountAmountSum(request);
    }


    @ApiOperation(value = "新增付款计划", notes = "新增付款计划")
    @PostMapping("/save")
    public Boolean save(@RequestBody List<ContractPayPlanAddF> addF) {
        return contractPayPlanConcludeService.save(addF);
    }

    /**
     * 按照拆分方式拆分收付款计划,给用户出几个标准的懒人拆分方案
     *
     * @param planDateF 拆分依据
     * @return 拆分方案
     */
    @ApiOperation(value = "计算付款计划", notes = "计算付款计划")
    @PostMapping("/calculate")
    public List<ContractPlanDateV> calculateSplitDate(ContractPlanDateF planDateF) {
        return contractPayPlanConcludeService.calculate(planDateF);
    }

    @ApiOperation(value = "更新付款计划", notes = "更新付款计划")
    @PutMapping
    public String update(@RequestBody List<ContractPayPlanConcludeUpdateF> contractPayPlanConcludeUpdateF) {
        contractPayPlanConcludeService.update(contractPayPlanConcludeUpdateF);
        return PromptInfo.OK.info;
    }

    @ApiOperation(value = "付款计划删除", notes = "付款计划删除")
    @DeleteMapping
    public Boolean removeById(@RequestParam("id") String id) {
        return contractPayPlanConcludeService.removeById(id);
    }

    @ApiOperation(value = "付款计划提交", notes = "付款计划提交")
    @PostMapping("/sumbitId")
    public String sumbitId(@RequestParam("id") String id) {
        contractPayPlanConcludeService.sumbitId(id);
        return PromptInfo.OK.info;
    }

    @ApiOperation(value = "支出合同计划反审核", notes = "支出合同计划反审核")
    @PostMapping("/returnId")
    public String returnId(@RequestParam("id") String id){
        contractPayPlanConcludeService.returnId(id);
        return PromptInfo.OK.info;
    }

    @ApiOperation(value = "新增结算计划", notes = "新增结算计划")
    @PostMapping("/saveSettlementPlan")
    public SettlementPlanResult saveSettlementPlan(@RequestBody SettlementPlanAddF req) {
        return contractPayPlanConcludeService.saveSettlementPlan(req);
    }

    @ApiOperation(value = "新增结算计划V2", notes = "新增结算计划V2")
    @PostMapping("/saveSettlementPlan/v2")
    public String saveSettlementPlanV2(@RequestBody SettlementPlanEditV req) {
        return contractPayPlanConcludeService.saveSettlementPlanV2(req);
    }

    @ApiOperation(value = "编辑结算计划", notes = "编辑结算计划")
    @PostMapping("/editSettlementPlan")
    public SettlementPlanResult editSettlementPlan(@RequestBody SettlementPlanAddF req) {
        return contractPayPlanConcludeService.editSettlementPlan(req);
    }

    @ApiOperation(value = "编辑结算计划V2", notes = "编辑结算计划V2")
    @PostMapping("/editSettlementPlan/v2")
    public String editSettlementPlanV2(@RequestBody SettlementPlanEditV req) {
        return contractPayPlanConcludeService.editSettlementPlanV2(req);
    }

    @ApiOperation(value = "预生成结算计划", notes = "预生成结算计划")
    @PostMapping("/preSettlementPlan")
    public PreSettlePlanV preSettlementPlan(@RequestBody PreSettlePlanQuery req) {
        return contractPayPlanConcludeService.preSettlementPlan(req);
    }

    @ApiOperation(value = "预生成结算计划V2", notes = "预生成结算计划V2")
    @GetMapping("/preSettlementPlan/v2")
    public List<PreSettlePlanDataV2> preSettlementPlanV2(@RequestParam(value = "contractId") String contractId){
        return contractPayPlanConcludeService.preSettlementPlanV2(contractId);
    }

    @ApiOperation(value = "结算计划查询计划详情", notes = "结算计划查询计划详情")
    @PostMapping("/queryPlanDetails")
    public PreSettlePlanV queryPlanDetails(@RequestBody SettlePlanDetailQuery req) {
        return contractPayPlanConcludeService.details(req);
    }

    @ApiOperation(value = "生成成本计划", notes = "生成成本计划")
    @PostMapping("/generateCostPlan")
    public String generateCostPlan(@Validated @RequestBody GenerateCostPlanF req) {
        return contractPayPlanConcludeService.generateCostPlan(req);
    }

    @ApiOperation(value = "结算计划分页列表", notes = "结算计划分页列表")
    @PostMapping("/listPlan")
    public PageV<SettlePlanListV> listPlan(@RequestBody PageF<SearchF<SettlementPlanListQuery>> query) {
        return contractPayPlanConcludeService.listPlan(query);
    }

    @ApiOperation(value = "结算计划导出分页列表", notes = "结算计划导出分页列表")
    @PostMapping("/exportListPlan")
    public PageV<ExportSettlePlanListV> exportListPlan(@RequestBody PageF<SearchF<SettlementPlanListQuery>> query) {
        return contractPayPlanConcludeService.exportListPlan(query);
    }

    @ApiOperation(value = "刷数据", notes = "刷数据")
    @GetMapping("/refresh")
    public void refresh() {
        contractPayPlanConcludeService.refresh();
    }

    @ApiOperation(value = "刷数据2", notes = "刷数据2")
    @GetMapping("/refresh2")
    public void refresh2(@RequestParam("contractIds") List<String> contractIds) {
        contractPayPlanConcludeService.refresh2(contractIds);
    }

    @ApiOperation(value = "根据合同id查询内部信息-后端内部使用", notes = "根据合同id查询内部信息-后端内部使用", response = ContractPayPlanInnerInfoV.class)
    @PostMapping("/getInnerInfoByContractIdsForPayApp")
    public List<ContractPayPlanInnerInfoV> getInnerInfoByContractIdsForPayApp(@RequestBody List<String> contractIds){
        return contractPayPlanConcludeService.getInnerInfoByContractIdsForPayApp(contractIds);
    }

    @ApiOperation(value = "根据id删除成本计划")
    @GetMapping("/deletedPayCostPlan")
    public Boolean deletedPayCostPlan(@RequestParam("id") String id) {
        return contractPayPlanConcludeService.deletedPayCostPlan( id);
    }

    @ApiOperation(value = "根据id删除结算计划")
    @GetMapping("/deletedPayPlan")
    public Boolean deletedPayPlan(@RequestParam("id") String id) {
        return contractPayPlanConcludeService.deletedPayPlan(id);
    }
}
