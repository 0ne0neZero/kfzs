package com.wishare.contract.apis.revision.income;

import com.alibaba.fastjson.JSONArray;
import com.wishare.contract.apps.fo.revision.ContractPlanDateF;
import com.wishare.contract.apps.fo.revision.income.*;
import com.wishare.contract.apps.fo.revision.pay.bill.ContractSettlementsBillF;
import com.wishare.contract.apps.fo.revision.pay.bill.ContractSettlementsFundF;
import com.wishare.contract.apps.fo.revision.pay.bill.ContractSettlementsReductF;
import com.wishare.contract.apps.remote.vo.config.DictionaryCode;
import com.wishare.contract.apps.service.contractset.ContractPayIncomePlanService;
import com.wishare.contract.domains.dto.settlementPlan.SettlementPlanResult;
import com.wishare.contract.domains.service.revision.income.ContractIncomePlanConcludeService;
import com.wishare.contract.domains.vo.ContractPlanDateV;
import com.wishare.contract.domains.vo.contractset.ContractConcludeV;
import com.wishare.contract.domains.vo.revision.income.*;
import com.wishare.contract.domains.vo.revision.pay.ContractPayPlanConcludeV;
import com.wishare.contract.domains.vo.revision.pay.ContractPayPlanInnerInfoV;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.enums.PromptInfo;
import com.wishare.tools.starter.fo.search.SearchF;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;


/**
 * @author zhangfuyu
 * @mender 龙江锋
 * @Date 2023/7/6/11:21
 */
@RequiredArgsConstructor
@RestController
@Validated
@Api(tags = {"收入合同收款计划信息表"})
@RequestMapping("/contractIncomePlanConclude")
public class ContractIncomePlanConcludeApi {

    private final ContractIncomePlanConcludeService contractIncomePlanConcludeService;

    private final ContractPayIncomePlanService contractPayIncomePlanService;

    @ApiOperation(value = "根据id查询详情", notes = "根据id查询详情", response = ContractConcludeV.class)
    @GetMapping("/getDetailsById")
    public ContractIncomePlanDetailsV getDetailsById(@RequestParam("contractPlanId") String contractPlanId) {
        return contractIncomePlanConcludeService.getDetailsById(contractPlanId);
    }

    @ApiOperation(value = "根据合同id查询内部信息-后端内部使用", notes = "根据合同id查询内部信息-后端内部使用", response = ContractPayPlanInnerInfoV.class)
    @PostMapping("/getInnerInfoByContractIds")
    public List<ContractPayPlanInnerInfoV> getInnerInfoByContractId(@RequestBody List<String> contractIds){
        return contractIncomePlanConcludeService.getInnerInfoByContractId(contractIds);
    }

    @ApiOperation(value = "收款计划分页列表", notes = "收款计划分页查询", response = ContractPayPlanConcludeV.class)
    @PostMapping("/pageInfo")
    public PageV<ContractIncomePlanConcludeInfoV> pageInfo(@RequestBody PageF<SearchF<ContractIncomePlanConcludePageF>> request) {
        return contractIncomePlanConcludeService.pageInfo(request);
    }

    @ApiOperation(value = "编辑列表", notes = "下拉列表，默认数量20", response = ContractIncomePlanConcludeV.class)
    @PostMapping("/list")
    public List<ContractIncomePlanConcludeV> list(@Validated @RequestBody ContractIncomePlanConcludeListF contractIncomeConcludeListF){
        return contractIncomePlanConcludeService.list(contractIncomeConcludeListF);
    }

    @ApiOperation(value = "编辑列表", notes = "下拉列表，默认数量20", response = ContractIncomePlanConcludeV.class)
    @PostMapping("/listInfo")
    public List<ContractIncomePlanConcludeInfoV> listInfo(@Validated @RequestBody ContractIncomePlanConcludeListF contractIncomeConcludeListF){
        return contractIncomePlanConcludeService.listInfo(contractIncomeConcludeListF);
    }

    @ApiOperation(value = "合同台账金额统计", response = ContractIncomePlanConcludeSumV.class)
    @PostMapping("/accountAmountSum")
    public ContractIncomePlanConcludeSumV accountAmountSum(@RequestBody PageF<SearchF<ContractIncomePlanConcludePageF>> request) {
        return contractIncomePlanConcludeService.accountAmountSum(request);
    }


    @ApiOperation(value = "新增收款计划", notes = "新增收款计划")
    @PostMapping("/save")
    public Boolean save(@RequestBody List<ContractIncomePlanAddF> addF) {
        return contractIncomePlanConcludeService.save(addF);
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
        return contractIncomePlanConcludeService.calculate(planDateF);
    }

    @ApiOperation(value = "更新收款计划", notes = "更新收款计划")
    @PostMapping("/update")
    public String update(@RequestBody List<ContractIncomePlanConcludeUpdateF> contractPayPlanConcludeUpdateF) {
        contractIncomePlanConcludeService.update(contractPayPlanConcludeUpdateF);
        return PromptInfo.OK.info;
    }

    @ApiOperation(value = "收款计划删除", notes = "收款计划删除")
    @DeleteMapping
    public Boolean removeById(@RequestParam("id") String id) {
        return contractIncomePlanConcludeService.removeById(id);
    }

    @ApiOperation(value = "收款计划提交", notes = "收款计划提交")
    @PostMapping("/sumbitId")
    public String sumbitId(@RequestParam("id") String id) {
        contractIncomePlanConcludeService.sumbitId(id);
        return PromptInfo.OK.info;
    }

    @ApiOperation(value = "收入合同无结算开票", notes = "收入合同无结算开票")
    @PostMapping("/invoice")
    public String invoice(@RequestBody ContractSettlementsBillF contractSettlementsBillF){
        return contractIncomePlanConcludeService.invoice(contractSettlementsBillF);
    }

    @ApiOperation(value = "收入合同无结算收款", notes = "收入合同无结算收款")
    @PostMapping("/setFund")
    public String setFund(@RequestBody ContractSettlementsFundF contractSettlementsFundF){
        return contractIncomePlanConcludeService.setFund(contractSettlementsFundF);
    }

    @ApiOperation(value = "收入合同减免", notes = "收入合同减免")
    @PostMapping("/setReduct")
    public String setReduct(@RequestBody ContractSettlementsReductF contractSettlementsFundF){
        return contractIncomePlanConcludeService.setReduct(contractSettlementsFundF);
    }

    @ApiOperation(value = "新增收款计划", notes = "新增收款计划")
    @PostMapping("/saveSettlementPlan")
    public SettlementPlanResult saveSettlementPlan(@RequestBody IncomePlanAddF req) {
        return contractIncomePlanConcludeService.saveSettlementPlan(req);
    }

    @ApiOperation(value = "新增收款计划V2", notes = "新增收款计划V2")
    @PostMapping("/saveSettlementPlan/v2")
    public String saveSettlementPlanV2(@RequestBody IncomePlanEditV req) {
        return contractIncomePlanConcludeService.saveSettlementPlanV2(req);
    }

    @ApiOperation(value = "编辑收款计划", notes = "编辑收款计划")
    @PostMapping("/editSettlementPlan")
    public SettlementPlanResult editSettlementPlan(@RequestBody IncomePlanAddF req) {
        return contractIncomePlanConcludeService.editSettlementPlan(req);
    }

    @ApiOperation(value = "编辑收款计划V2", notes = "编辑收款计划V2")
    @PostMapping("/editSettlementPlan/v2")
    public String editSettlementPlanV2(@RequestBody IncomePlanEditV req) {
        return contractIncomePlanConcludeService.editSettlementPlanV2(req);
    }

    @ApiOperation(value = "删除收款计划", notes = "删除收款计划")
    @PostMapping("/deleteSettlementPlan")
    public void deleteSettlementPlan(@RequestParam String planId) {
        contractIncomePlanConcludeService.deleteSettlementPlan(planId);
    }

    @ApiOperation(value = "手动推送收款计划到枫行梦", notes = "手动推送收款计划到枫行梦")
    @PostMapping("/manualPushIncomePlan2Fxm")
    public Boolean manualPushIncomePlan2Fxm(@RequestBody IncomePlanIdsF incomePlanIdsF) {
        return contractIncomePlanConcludeService.manualPushIncomePlan2Fxm(incomePlanIdsF);
    }

    @ApiOperation(value = "手动执行推送失败收款数据到枫行梦定时任务", notes = "手动执行推送失败收款数据到枫行梦定时任务")
    @PostMapping("/rePushFxmFailedData")
    public Boolean rePushFxmFailedData() {
        contractIncomePlanConcludeService.rePushFxmFailedData();
        return Boolean.TRUE;
    }

    @ApiOperation(value = "预生成收款计划", notes = "预生成收款计划")
    @PostMapping("/preSettlementPlan")
    public PreIncomePlanV preSettlementPlan(@RequestBody PreIncomePlanQuery req) {
        return contractIncomePlanConcludeService.preSettlementPlan(req);
    }

    @ApiOperation(value = "预生成收款计划V2", notes = "预生成收款计划V2")
    @GetMapping("/preSettlementPlan/v2")
    public List<PreIncomePlanDataV2> preSettlementPlanV2(@RequestParam(value = "contractId") String contractId){
        return contractIncomePlanConcludeService.preSettlementPlanV2(contractId);
    }

    public static void main(String[] args) {


    }

    @ApiOperation(value = "收款计划查询计划详情", notes = "收款计划查询计划详情")
    @PostMapping("/queryPlanDetails")
    public PreIncomePlanV queryPlanDetails(@RequestBody IncomePlanDetailQuery req) {
        return contractIncomePlanConcludeService.details(req);
    }

    @ApiOperation(value = "根据计划id查询详情", notes = "根据计划id查询详情", response = ContractConcludeV.class)
    @GetMapping("/getDetailsByIdSettle")
    public ContractIncomePlanDetailsV getDetailsByIdSettle(@RequestParam("id") String id) {
        return contractIncomePlanConcludeService.getDetailsByIdSettle(id);
    }

    @ApiOperation(value = "收款计划分页列表", notes = "收款计划分页列表")
    @PostMapping("/listPlan")
    public PageV<IncomePlanListV> listPlan(@RequestBody PageF<SearchF<IncomePlanListQuery>> query) {
        return contractIncomePlanConcludeService.listPlan(query);
    }

    @ApiOperation(value = "收款计划分页列表", notes = "收款计划分页查询", response = ContractIncomePlanConcludeV.class)
    @PostMapping("/page")
    public PageV<ContractIncomePlanConcludeV> page(@RequestBody PageF<SearchF<ContractIncomePlanConcludePageF>> request) {
        return contractIncomePlanConcludeService.page(request);
    }

    @ApiOperation(value = "根据收款计划Id获取收入计划列表", notes = "根据收款计划Id获取收入计划列表")
    @GetMapping(value = "/getIncomePlanByConcludePlanId", name = "根据收款计划Id获取收入计划列表")
    List<String> getIncomePlanByContractId(@RequestParam("concludePlanId") String concludePlanId){
        return contractPayIncomePlanService.getIncomePlanByConcludePlanId(concludePlanId);
    }

    @ApiOperation(value = "作废", notes = "作废")
    @PostMapping("/sakuIncomePlan")
    public Boolean sakuIncomePlan(@RequestBody IncomePlanCancleF planCancleF) {
        return contractIncomePlanConcludeService.sakuIncomePlan(planCancleF);
    }

    @ApiOperation(value = "收入合同管理类别", notes = "收入合同管理类别")
    @PostMapping("/queryConmanagetype")
    public List<DictionaryCode> queryConmanagetype() {
        return contractIncomePlanConcludeService.queryConmanagetype();
    }


    @ApiOperation(value = "生成收入计划", notes = "生成收入计划")
    @PostMapping("/createIncomePlan")
    public void createIncomePlan(@RequestBody PayIncomePlanV payIncomePlanV) {
        contractIncomePlanConcludeService.createIncomePlan(payIncomePlanV);
    }

    @ApiOperation(value = "修改收入计划", notes = "修改收入计划")
    @PostMapping("/updateIncomePlan")
    public void updateIncomePlan(@RequestBody PayIncomePlanV payIncomePlanV) {
        contractIncomePlanConcludeService.updateIncomePlan(payIncomePlanV);
    }

    @ApiOperation(value = "收入计划分页列表", notes = "收入计划分页列表")
    @PostMapping("/listPayIncomePlan")
    public PageV<PayIncomeListV> listPayIncomePlan(@RequestBody PageF<SearchF<PayIncomeListQuery>> query) {
        return contractPayIncomePlanService.listPlan(query);
    }


    @ApiOperation(value = "查询可选择收款列表-新")
    @PostMapping("/listPidNew")
    public ContractIncomePlanPidConcludeV listPidNew(@RequestBody ContractIncomePlanConcludeListF contractIncomePlanConcludeListF) {
        return contractIncomePlanConcludeService.listPidNew(contractIncomePlanConcludeListF);
    }

    @ApiOperation(value = "收入计划入账", notes = "收入计划入账")
    @PostMapping("/iriPlan")
    public void iriPlan(@RequestBody PayIncomePlanV payIncomePlanV) {
        contractIncomePlanConcludeService.iriPlan(payIncomePlanV);
    }

    @ApiOperation(value = "根据id删除收入计划")
    @GetMapping("/deletedPayIncomePlan")
    public Boolean deletedPayIncomePlan(@RequestParam("id") String id) {
        return contractIncomePlanConcludeService.deletedPayIncomePlan( id);
    }

    @ApiOperation(value = "根据id删除收款计划")
    @GetMapping("/deletedIncomePlan")
    public Boolean deletedIncomePlan(@RequestParam("id") String id) {

        return contractIncomePlanConcludeService.deletedIncomePlan( id);
    }
}
