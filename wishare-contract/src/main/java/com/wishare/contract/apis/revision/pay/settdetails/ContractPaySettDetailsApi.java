package com.wishare.contract.apis.revision.pay.settdetails;


import com.wishare.contract.apps.fo.revision.pay.settdetails.ContractPaySettDetailsSaveF;
import com.wishare.contract.apps.fo.revision.pay.settdetails.ContractPaySettDetailsUpdateF;
import com.wishare.contract.apps.service.revision.pay.settdetails.ContractPaySettDetailsAppService;
import com.wishare.contract.domains.vo.contractset.ContractConcludeV;
import com.wishare.contract.domains.vo.revision.pay.ContractPayPlanDetailsV;
import com.wishare.contract.domains.vo.revision.pay.settdetails.ContractPaySettDetailsV;
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
@Api(tags = {"支出合同-结算单明细表"})
@RequestMapping("/contractPaySettDetails")
public class ContractPaySettDetailsApi {

    private final ContractPaySettDetailsAppService contractPayFundAppService;

    @ApiOperation(value = "根据id查询详情", notes = "根据id查询详情", response = ContractPaySettDetailsV.class)
    @GetMapping("/getDetailsById")
    public ContractPaySettDetailsV getDetailsById(@RequestParam("id") String id) {
        return contractPayFundAppService.getDetailsById(id);
    }

    @ApiOperation(value = "新增保存", notes = "新增保存")
    @PostMapping("/add")
    public String save(@Validated @RequestBody ContractPaySettDetailsSaveF contractPaySettDetailsSaveF){
        return contractPayFundAppService.save(contractPaySettDetailsSaveF);
    }

    @ApiOperation(value = "更新", notes = "根据主键更新")
    @PostMapping("/edit")
    public String update(@Validated @RequestBody ContractPaySettDetailsUpdateF contractPayFundF){
        contractPayFundAppService.update(contractPayFundF);
        return PromptInfo.OK.info;
    }

    @ApiOperation(value = "删除", notes = "根据主键删除")
    @DeleteMapping
    public boolean removeById(@RequestParam("id") String id){
        return contractPayFundAppService.removeById(id);
    }

    @ApiOperation(value = "根据结算单id查询详情", notes = "根据结算单id查询详情", response = ContractPaySettDetailsV.class)
    @GetMapping("/getDetailsBySettlementId")
    public List<ContractPaySettDetailsV> getDetailsBySettlementId(@RequestParam("settlementId") String settlementId) {
        return contractPayFundAppService.getDetailsBySettlementId(settlementId);
    }
}
