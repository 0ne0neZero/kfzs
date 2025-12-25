package com.wishare.contract.apis.revision.pay;

import com.wishare.contract.apps.fo.revision.ContractPlanDateF;
import com.wishare.contract.apps.fo.revision.pay.*;
import com.wishare.contract.apps.service.revision.pay.ContractPayConcludeProfitLossAppService;
import com.wishare.contract.domains.entity.revision.pay.ContractPayConcludeProfitLossE;
import com.wishare.contract.domains.vo.ContractPlanDateV;
import com.wishare.contract.domains.vo.revision.income.ContractIncomePlanConcludeV;
import com.wishare.contract.domains.vo.revision.pay.ContractPayConcludeProfitLossV;
import com.wishare.contract.domains.vo.revision.pay.ContractPayPlanConcludeV;
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
 * 合同成本损益表
 * </p>
 *
 * @author chenglong
 * @since 2023-10-26
 */
@RequiredArgsConstructor
@RestController
@Validated
@Api(tags = {"合同成本损益表"})
@RequestMapping("/contractPayConcludeProfitLoss")
public class ContractPayConcludeProfitLossApi {

    private final ContractPayConcludeProfitLossAppService contractPayConcludeProfitLossAppService;
    @ApiOperation(value = "获取详细信息", notes = "获取详细信息", response = ContractPayConcludeProfitLossV.class)
    @GetMapping
    public ContractPayConcludeProfitLossV get(@Validated ContractPayConcludeProfitLossF contractPayConcludeProfitLossF){
        return contractPayConcludeProfitLossAppService.get(contractPayConcludeProfitLossF);
    }

    @ApiOperation(value = "下拉列表", notes = "下拉列表，默认数量20", response = ContractPayConcludeProfitLossV.class)
    @PostMapping("/list")
    public List<ContractPayPlanConcludeV> list(@Validated @RequestBody ContractPayConcludeProfitLossListF contractPayConcludeProfitLossListF){
        return contractPayConcludeProfitLossAppService.list(contractPayConcludeProfitLossListF);
    }
//
//    @ApiOperation(value = "新增保存", notes = "新增保存")
//    @PostMapping
//    public String save(@Validated @RequestBody ContractPayConcludeProfitLossSaveF contractPayConcludeProfitLossF){
//        return contractPayConcludeProfitLossAppService.save(contractPayConcludeProfitLossF);
//    }

    @ApiOperation(value = "更新", notes = "根据主键更新")
    @PostMapping("/update")
    public String update(@Validated @RequestBody List<ContractPayPlanConcludeUpdateF> contractPayPlanConcludeUpdateF){
        contractPayConcludeProfitLossAppService.update(contractPayPlanConcludeUpdateF);
        return PromptInfo.OK.info;
    }

    @ApiOperation(value = "删除", notes = "根据主键删除")
    @DeleteMapping
    public boolean removeById(@RequestParam("id") String id){
        return contractPayConcludeProfitLossAppService.removeById(id);
    }

    @ApiOperation(value = "分页列表", response = ContractIncomePlanConcludeV.class)
    @PostMapping("/page")
    public PageV<ContractPayPlanConcludeV> page(@RequestBody PageF<SearchF<ContractPayPlanConcludePageF>> request) {
        return contractPayConcludeProfitLossAppService.page(request);
    }

    @ApiOperation(value = "分页列表仅供前台调用", response = ContractPayConcludeProfitLossV.class)
    @PostMapping("/pageFront")
    public PageV<ContractPayConcludeProfitLossV> frontPage(@RequestBody PageF<SearchF<ContractPayConcludeProfitLossE>> request) {
        return contractPayConcludeProfitLossAppService.frontPage(request);
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
        return contractPayConcludeProfitLossAppService.calculate(planDateF);
    }


    @ApiOperation(value = "新增收款计划", notes = "新增收款计划")
    @PostMapping("/save")
    public Boolean save(@RequestBody List<ContractPayPlanAddF> addF) {
        return contractPayConcludeProfitLossAppService.save(addF);
    }


    @ApiOperation(value = "收款计划提交", notes = "收款计划提交")
    @PostMapping("/sumbitId")
    public String sumbitId(@RequestParam("id") String id) {
        contractPayConcludeProfitLossAppService.sumbitId(id);
        return PromptInfo.OK.info;
    }



}
