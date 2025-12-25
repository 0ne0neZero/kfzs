package com.wishare.contract.apis.contractset;

import com.wishare.contract.apps.fo.contractset.*;
import com.wishare.contract.apps.service.contractset.ContractCollectionPlanAppService;
import com.wishare.contract.domains.entity.contractset.ContractPaymentDetailE;
import com.wishare.contract.domains.vo.contractset.*;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.enums.PromptInfo;
import com.wishare.tools.starter.fo.search.SearchF;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;

/**
 * <p>
 * 合同收款计划信息
 * </p>
 *
 * @author wangrui
 * @since 2022-09-09
 */
@RequiredArgsConstructor
@RestController
@Api(tags = {"合同收款计划信息"})
@RequestMapping("/contractCollectionPlan")
public class ContractCollectionPlanApi {

    private final ContractCollectionPlanAppService contractCollectionPlanAppService;

    @ApiOperation(value = "下拉列表",response = ContractCollectionPlanV.class)
    @GetMapping("/list")
    public List<ContractCollectionPlanV> listContractCollectionPlan(@ModelAttribute ContractCollectionPlanF contractCollectionPlanF) {
        return contractCollectionPlanAppService.listContractCollectionPlan(contractCollectionPlanF);
    }

    @ApiOperation(value = "新增/更新收/付款计划", notes = "新增/更新收款计划")
    @PostMapping("/save")
    public List<ContractProfitLossPlanV> saveContractCollectionPlan(@Validated @RequestBody ContractPlanSaveF contractCollectionPlanF){
        return contractCollectionPlanAppService.save(contractCollectionPlanF);
    }

    @ApiOperation(value = "更新收付款计划和损益计划", notes = "更新收付款计划和损益计划")
    @PostMapping("/update")
    public void update(@Validated @RequestBody CollectionAndLossPlanUpdateF planUpdateF){
        contractCollectionPlanAppService.update(planUpdateF);
    }

    @ApiOperation(value = "调整收款计划", notes = "调整收款计划")
    @PostMapping("/adjustPlan")
    public void adjustPlan(@Validated @RequestBody List<ContractCollectionPlanSaveF> contractCollectionPlanF) throws ParseException {
        contractCollectionPlanAppService.adjustPlan(contractCollectionPlanF);
    }

    @ApiOperation(value = "删除收款计划", notes = "删除收款计划")
    @DeleteMapping("/remove")
    public void removeContractCollectionPlan(@RequestParam("id") Long id) {
        contractCollectionPlanAppService.removeContractCollectionPlan(id);
    }

    @ApiOperation(value = "收款/付款计划分页查询", notes = "收款/付款计划分页查询", response = ContractCollectionPlanDetailV.class)
    @PostMapping("/collectionPlan/page")
    public PageV<ContractCollectionPlanDetailV> collectionPlanPage(@RequestBody PageF<SearchF<ContractCollectionPlanPageF>> form) {
        return contractCollectionPlanAppService.collectionPlanDetailPage(form);
    }

    @ApiOperation(value = "初始化数据，对所有costName字段为空的数据进行赋值", notes = "初始化数据，对所有costName字段为空的数据进行赋值")
    @PostMapping("/collectionPlan/initAllDataForCostName")
    public Integer initAllDataForCostName() {
        return contractCollectionPlanAppService.initAllDataForCostName();
    }

    @ApiOperation(value = "收款/付款计划金额总和", notes = "收款/付款计划金额总和", response = CollectionPlanSumV.class)
    @PostMapping("/collectionPlan/amountSum")
    public CollectionPlanSumV collectionPlanAmountSum(@RequestBody PageF<SearchF<ContractCollectionPlanPageF>> form) {
        return contractCollectionPlanAppService.collectionPlanAmountSum(form);
    }

    @ApiOperation(value = "批量收款", notes = "批量收款")
    @PostMapping("/batchCollection")
    public void batchCollection(@Validated @RequestBody ContractCollectionF collectionF) {
        contractCollectionPlanAppService.batchCollection(collectionF);
    }

    @ApiOperation(value = "批量开票", notes = "批量开票")
    @PostMapping("/batchInvoice")
    public void batchInvoice(@Validated @RequestBody ContractInvoiceF contractInvoiceF) {
        contractCollectionPlanAppService.batchInvoice(contractInvoiceF);
    }

    @ApiOperation(value = "批量付款(不再使用，改为付款收票接口)", notes = "批量付款")
    @PostMapping("/batchPayment")
    public void batchInvoice(@RequestBody ContractPaymentF contractPaymentF) throws ParseException {
        contractCollectionPlanAppService.batchPayment(contractPaymentF);
    }

    @ApiOperation(value = "减免申请(本币)", notes = "减免申请(本币)")
    @PostMapping("/derate")
    public void derate(@Validated @RequestBody CollectionPlanDerateF collectionPlanDerateF) throws ParseException {
        contractCollectionPlanAppService.derate(collectionPlanDerateF);
    }

    @ApiOperation(value = "付款收票", notes = "付款收票")
    @PostMapping("/paymentInvoice")
    public void paymentInvoice(@Validated @RequestBody List<CollectionPlanPaymentInvoiceF> from) {
        contractCollectionPlanAppService.paymentInvoice(from);
    }

    @ApiOperation(value = "新增/修改逾期说明", notes = "新增/修改逾期说明")
    @PostMapping("/saveOverdueStatement")
    public Boolean saveOverdueStatement(@Validated @RequestBody CollectionPlanOverdueStatementF from) {
        return contractCollectionPlanAppService.saveOverdueStatement(from);
    }

    @ApiOperation(value = "合同统计-应收应付", notes = "合同统计-应收应付")
    @GetMapping("/collectionPlanStatistics")
    public List<ContractCollectionPlanStatisticsV> selectCollectionPlanStatistics(@ApiParam("合同性质 1 收入 2 支出") @RequestParam("contractNature") Integer contractNature,
                                                                                  @ApiParam("年份") @RequestParam("year") String year) {
        return contractCollectionPlanAppService.selectCollectionPlanStatistics(contractNature, year);
    }
}
