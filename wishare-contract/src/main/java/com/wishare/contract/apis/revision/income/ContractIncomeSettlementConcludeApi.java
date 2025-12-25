package com.wishare.contract.apis.revision.income;

import com.wishare.contract.apps.fo.revision.income.ContractIncomeSettlementAddF;
import com.wishare.contract.apps.fo.revision.income.ContractIncomeSettlementConcludePageF;
import com.wishare.contract.apps.fo.revision.income.ContractIncomeSettlementConcludeUpdateF;
import com.wishare.contract.apps.fo.revision.income.settlement.ContractIncomePlanListF;
import com.wishare.contract.apps.fo.revision.income.settlement.ContractIncomeSettlementConcludeListF;
import com.wishare.contract.apps.fo.revision.pay.bill.ContractSettlementsBillF;
import com.wishare.contract.apps.fo.revision.pay.bill.ContractSettlementsFundF;
import com.wishare.contract.apps.fo.revision.pay.settlement.ContractPaySettlementStepF;
import com.wishare.contract.apps.service.revision.income.ContractIncomeSettlementConcludeAppService;
import com.wishare.contract.domains.vo.contractset.ContractConcludeV;
import com.wishare.contract.domains.vo.revision.income.ContractIncomeSettlementConcludeSumV;
import com.wishare.contract.domains.vo.revision.income.ContractIncomeSettlementConcludeV;
import com.wishare.contract.domains.vo.revision.income.ContractIncomeSettlementDetailsV;
import com.wishare.contract.domains.vo.revision.income.settdetails.ContractIncomeSettdeductionDetailV;
import com.wishare.contract.domains.vo.revision.income.settlement.ContractIncomePlanForSettlementV;
import com.wishare.contract.domains.vo.revision.income.settlement.ContractIncomePlanPeriodV;
import com.wishare.contract.domains.vo.revision.income.settlement.ContractIncomeSettlementConcludeEditV;
import com.wishare.contract.domains.vo.revision.income.settlement.ContractIncomeSettlementPageV2;
import com.wishare.contract.domains.vo.revision.pay.settlement.ContractPayPlanPeriodV;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.enums.PromptInfo;
import com.wishare.tools.starter.fo.search.SearchF;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @description:
 * @author: zhangfuyu
 * @Date: 2023/7/6/11:21
 */
@RequiredArgsConstructor
@RestController
@Validated
@Api(tags = {"收入合同结算单表"})
@RequestMapping("/contractIncomeSettlementConclude")
public class ContractIncomeSettlementConcludeApi {

    private final ContractIncomeSettlementConcludeAppService contractIncomeSettlementConcludeAppService;

    @ApiOperation(value = "根据确收单id对应的主合同和补充协议合同id", notes = "根据确收单id对应的主合同和补充协议合同id")
    @GetMapping("/getAllContractId")
    public List<String> getAllContractId(@RequestParam("id") String id){
        return contractIncomeSettlementConcludeAppService.getAllContractId(id);
    }

    @ApiOperation(value = "根据id查询详情", notes = "根据id查询详情", response = ContractConcludeV.class)
    @GetMapping("/getDetailsById")
    public ContractIncomeSettlementDetailsV getDetailsById(@RequestParam("id") String id) {
        return contractIncomeSettlementConcludeAppService.getDetailsById(id);
    }

    @ApiOperation(value = "根据合同id获取计量周期(原结算周期)周期下拉列表", notes = "根据合同id获取计量周期(原结算周期)下拉列表", response = ContractPayPlanPeriodV.class)
    @GetMapping("/getPlanPeriod")
    public ContractIncomePlanPeriodV getPlanPeriod(@Validated @NotBlank(message = "合同id不能为空")
                                                @RequestParam("contractId")  String contractId) {
        return contractIncomeSettlementConcludeAppService.getPlanPeriod(contractId);
    }

    @ApiOperation(value = "更新结算单的步骤", notes = "更新结算单的步骤", response = String.class)
    @PostMapping("/step/update")
    public String updateSettlementStep(@Validated @RequestBody ContractPaySettlementStepF stepF){
        return contractIncomeSettlementConcludeAppService.updateSettlementStep(stepF.getSettlementId(),stepF.getStep());
    }

    @ApiOperation(value = "获取结算单可用的成本预估计划列表", notes = "获取结算单可用的成本预估计划列表")
    @PostMapping("/getPlanList")
    public List<ContractIncomePlanForSettlementV> getPlanList(@Validated @RequestBody ContractIncomePlanListF contractIncomePlanListF){
        return contractIncomeSettlementConcludeAppService.getPlanList(contractIncomePlanListF);
    }


    @ApiOperation(value = "收入合同结算单分页列表")
    @PostMapping("/page")
    public PageV<ContractIncomeSettlementConcludeV> page(@RequestBody PageF<SearchF<ContractIncomeSettlementConcludePageF>> request) {
        return contractIncomeSettlementConcludeAppService.page(request);
    }

    @ApiOperation(value = "收入合同结算单分页列表V2")
    @PostMapping("/v2/page")
    public PageV<ContractIncomeSettlementPageV2> pageV2(@RequestBody PageF<SearchF<?>> request) {
        return contractIncomeSettlementConcludeAppService.pageV2(request);
    }

    @ApiOperation(value = "合同台账金额统计", response = ContractIncomeSettlementConcludeSumV.class)
    @PostMapping("/accountAmountSum")
    public ContractIncomeSettlementConcludeSumV accountAmountSum(@RequestBody PageF<SearchF<ContractIncomeSettlementConcludePageF>> request) {
        return contractIncomeSettlementConcludeAppService.accountAmountSum(request);
    }

    @ApiOperation(value = "新增收入合同结算单", notes = "新增收入合同结算单")
    @PostMapping("/save")
    public String save(@Validated @RequestBody ContractIncomeSettlementAddF addF){
        return contractIncomeSettlementConcludeAppService.save(addF);
    }


    @ApiOperation(value = "编辑-获取信息", notes = "编辑-获取信息")
    @PostMapping("/editInfo")
    public ContractIncomeSettlementConcludeEditV list(@Validated @RequestBody ContractIncomeSettlementConcludeListF contractIncomePlanConcludeListF){
        return contractIncomeSettlementConcludeAppService.editInfo(contractIncomePlanConcludeListF);
    }

    @ApiOperation(value = "更新收入合同结算单", notes = "更新收入合同结算单")
    @PostMapping("/update")
    public String update(@Validated @RequestBody ContractIncomeSettlementConcludeUpdateF contractPaySettlementConcludeUpdateF){
        contractIncomeSettlementConcludeAppService.update(contractPaySettlementConcludeUpdateF);
        return PromptInfo.OK.info;
    }

    @ApiOperation(value = "收入合同结算单删除", notes = "收入合同结算单删除")
    @DeleteMapping
    public boolean removeById(@RequestParam("id") String id){
        return contractIncomeSettlementConcludeAppService.removeById(id);
    }

    @ApiOperation(value = "收入合同结算单提交", notes = "收入合同结算单提交")
    @PostMapping("/submitId")
    public String submitId(@RequestParam("id") String id){
        return contractIncomeSettlementConcludeAppService.submitId(id);
    }

    @ApiOperation(value = "收入合同结算单反审核", notes = "收入合同结算单反审核")
    @PostMapping("/returnId")
    public String returnId(@RequestParam("id") String id){
        contractIncomeSettlementConcludeAppService.returnId(id);
        return PromptInfo.OK.info;
    }

    @ApiOperation(value = "收入合同结算单开票", notes = "收入合同结算单开票")
    @PostMapping("/invoice")
    public String invoice(@RequestBody ContractSettlementsBillF contractSettlementsBillF){
        return contractIncomeSettlementConcludeAppService.invoice(contractSettlementsBillF);
    }

    @ApiOperation(value = "收入合同结算单收付款", notes = "收入合同结算单收付款")
    @PostMapping("/setFund")
    public String setFund(@RequestBody ContractSettlementsFundF contractSettlementsFundF){
        return contractIncomeSettlementConcludeAppService.setFund(contractSettlementsFundF);
    }

    @ApiOperation(value = "减免明细")
    @PostMapping("/contractSettdeductionDetailPage")
    public PageV<ContractIncomeSettdeductionDetailV> contractSettdeductionDetailPage(@RequestBody PageF<SearchF<?>> request) {
        return contractIncomeSettlementConcludeAppService.contractSettdeductionDetailPage(request);
    }

    @ApiOperation(value = "根据id删除确收审批")
    @GetMapping("/deletedIncomeSettlement")
    public Boolean deletedIncomeSettlement(@RequestParam("id") String id) {
        return contractIncomeSettlementConcludeAppService.deletedIncomeSettlement(id);
    }
}
