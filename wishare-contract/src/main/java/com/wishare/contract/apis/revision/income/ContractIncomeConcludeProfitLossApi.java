package com.wishare.contract.apis.revision.income;

import com.wishare.contract.apps.fo.revision.ContractPlanDateF;
import com.wishare.contract.apps.fo.revision.income.*;
import com.wishare.contract.apps.service.revision.income.ContractIncomeConcludeProfitLossAppService;
import com.wishare.contract.domains.entity.revision.income.ContractIncomeConcludeProfitLossE;
import com.wishare.contract.domains.vo.ContractPlanDateV;
import com.wishare.contract.domains.vo.revision.income.ContractIncomeConcludeProfitLossV;
import com.wishare.contract.domains.vo.revision.income.ContractIncomePlanConcludeV;
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
 * <p>
 * 合同收入损益表
 * </p>
 *
 * @author chenglong
 * @since 2023-10-24
 */
@RequiredArgsConstructor
@RestController
@Validated
@Api(tags = {"合同收入损益"})
@RequestMapping("/contractIncomeConcludeProfitLoss")
public class ContractIncomeConcludeProfitLossApi {

    private final ContractIncomeConcludeProfitLossAppService contractIncomeConcludeProfitLossAppService;
    @ApiOperation(value = "获取详细信息", notes = "获取详细信息", response = ContractIncomeConcludeProfitLossV.class)
    @GetMapping
    public ContractIncomeConcludeProfitLossV get(@Validated ContractIncomeConcludeProfitLossF contractIncomeConcludeProfitLossF){
        return contractIncomeConcludeProfitLossAppService.get(contractIncomeConcludeProfitLossF);
    }

    @ApiOperation(value = "编辑列表", notes = "编辑列表，默认数量20", response = ContractIncomePlanConcludeV.class)
    @PostMapping("/list")
    public List<ContractIncomePlanConcludeV> list(@Validated @RequestBody ContractIncomeConcludeProfitLossListF contractIncomeConcludeProfitLossListF){
        return contractIncomeConcludeProfitLossAppService.list(contractIncomeConcludeProfitLossListF);
    }
//
//    @ApiOperation(value = "新增保存", notes = "新增保存")
//    @PostMapping
//    public String save(@Validated @RequestBody ContractIncomeConcludeProfitLossSaveF contractIncomeConcludeProfitLossF){
//        return contractIncomeConcludeProfitLossAppService.save(contractIncomeConcludeProfitLossF);
//    }
//
    @ApiOperation(value = "更新收款计划", notes = "更新收款计划")
    @PostMapping("/update")
    public String update(@Validated @RequestBody List<ContractIncomePlanConcludeUpdateF> contractPayPlanConcludeUpdateF){
        contractIncomeConcludeProfitLossAppService.update(contractPayPlanConcludeUpdateF);
        return PromptInfo.OK.info;
    }

    @ApiOperation(value = "删除", notes = "根据主键删除")
    @DeleteMapping
    public boolean removeById(@RequestParam("id") String id){
        return contractIncomeConcludeProfitLossAppService.removeById(id);
    }

    @ApiOperation(value = "分页列表", response = ContractIncomePlanConcludeV.class)
    @PostMapping("/page")
    public PageV<ContractIncomePlanConcludeV> page(@RequestBody PageF<SearchF<ContractIncomePlanConcludePageF>> request) {
        return contractIncomeConcludeProfitLossAppService.page(request);
    }

    @ApiOperation(value = "分页列表仅供前台调用", response = ContractIncomeConcludeProfitLossV.class)
    @PostMapping("/pageFront")
    public PageV<ContractIncomeConcludeProfitLossV> frontPage(@RequestBody PageF<SearchF<ContractIncomeConcludeProfitLossE>> request) {
        return contractIncomeConcludeProfitLossAppService.frontPage(request);
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
        return contractIncomeConcludeProfitLossAppService.calculate(planDateF);
    }


    @ApiOperation(value = "新增收款计划", notes = "新增收款计划")
    @PostMapping("/save")
    public Boolean save(@RequestBody List<ContractIncomePlanAddF> addF) {
        return contractIncomeConcludeProfitLossAppService.save(addF);
    }

    @ApiOperation(value = "收款计划提交", notes = "收款计划提交")
    @PostMapping("/sumbitId")
    public String sumbitId(@RequestParam("id") String id) {
        contractIncomeConcludeProfitLossAppService.sumbitId(id);
        return PromptInfo.OK.info;
    }


}
