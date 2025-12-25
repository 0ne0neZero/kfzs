package com.wishare.contract.apis.revision.income.settdetails;


import com.wishare.contract.apps.fo.revision.income.settdetails.ContractIncomeSettDetailsSaveF;
import com.wishare.contract.apps.fo.revision.income.settdetails.ContractIncomeSettDetailsUpdateF;
import com.wishare.contract.apps.service.revision.income.settdetails.ContractIncomeSettDetailsAppService;
import com.wishare.contract.domains.vo.contractset.ContractConcludeV;
import com.wishare.contract.domains.vo.revision.income.settdetails.ContractIncomeSettDetailsV;
import com.wishare.starter.enums.PromptInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @description:
 * @author: zhangfuyu
 * @Date: 2023/7/7/11:09
 */
@RequiredArgsConstructor
@RestController
@Validated
@Api(tags = {"收入合同-结算单明细表"})
@RequestMapping("/contractIncomeSettDetails")
public class ContractIncomeSettDetailsApi {

    private final ContractIncomeSettDetailsAppService contractPayFundAppService;

    @ApiOperation(value = "根据id查询详情", notes = "根据id查询详情", response = ContractConcludeV.class)
    @GetMapping("/getDetailsById")
    public ContractIncomeSettDetailsV getDetailsById(@RequestParam("id") String id) {
        return contractPayFundAppService.getDetailsById(id);
    }

    @ApiOperation(value = "新增保存", notes = "新增保存")
    @PostMapping("/add")
    public String save(@Validated @RequestBody ContractIncomeSettDetailsSaveF contractPaySettDetailsSaveF){
        return contractPayFundAppService.save(contractPaySettDetailsSaveF);
    }

    @ApiOperation(value = "更新", notes = "根据主键更新")
    @PostMapping("/edit")
    public String update(@Validated @RequestBody ContractIncomeSettDetailsUpdateF contractPayFundF){
        contractPayFundAppService.update(contractPayFundF);
        return PromptInfo.OK.info;
    }

    @ApiOperation(value = "删除", notes = "根据主键删除")
    @DeleteMapping
    public boolean removeById(@RequestParam("id") String id){
        return contractPayFundAppService.removeById(id);
    }

    @ApiOperation(value = "根据结算单id查询详情", notes = "根据结算单id查询详情", response = ContractIncomeSettDetailsV.class)
    @GetMapping("/getDetailsBySettlementId")
    public List<ContractIncomeSettDetailsV> getDetailsBySettlementId(@RequestParam("settlementId") String settlementId) {
        return contractPayFundAppService.getDetailsBySettlementId(settlementId);
    }

}
