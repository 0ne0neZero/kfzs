package com.wishare.contract.apis.revision;

import com.wishare.contract.apps.fo.revision.ContractNoGenerateF;
import com.wishare.contract.apps.service.revision.base.ContractBaseService;
import com.wishare.contract.domains.vo.revision.ContractNoInfoV;
import com.wishare.contract.domains.vo.revision.ContractPaymentVO;
import com.wishare.contract.domains.vo.revision.income.ContractIncomeConcludeDetailV;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RequiredArgsConstructor
@RestController
@Validated
@Api(tags = {"合同基础信息管理"})
@RequestMapping("/manage/contractBase")
public class ContractBaseApi {

    @Autowired
    private ContractBaseService contractBaseService;

    @ApiOperation(value = "自动生成合同编号", notes = "自动生成合同编号")
    @PostMapping("/generateContractNo")
    public ContractNoInfoV generateContractNo(@Validated @RequestBody ContractNoGenerateF contractNoGenerateF){
        return contractBaseService.generateContractNo(contractNoGenerateF);
    }

    @ApiOperation(value = "根据合同id批量获取收款信息", notes = "根据合同id批量获取收款信息")
    @PostMapping("/getContractPaymentList")
    public List<ContractPaymentVO> getContractPaymentList(@RequestBody List<String> ids){
        return contractBaseService.getContractPaymentList(ids);
    }

    /*@ApiOperation(value = "收入合同-处理合同编号", notes = "收入合同-处理合同编号")
    @GetMapping("/generateContractNo/income")
    public ContractNoInfoV generateIncomeContractNo(@RequestParam(value = "id") String id){
        return contractBaseService.generateIncomeContractNo(id);
    }

    @ApiOperation(value = "支出合同-处理合同编号", notes = "支出合同-处理合同编号")
    @GetMapping("/generateContractNo/pay")
    public ContractNoInfoV generatePayContractNo(@RequestParam(value = "id") String id){
        return contractBaseService.generatePayContractNo(id);
    }*/

}
