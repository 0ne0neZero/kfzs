package com.wishare.contract.apis.revision.pay;

import com.wishare.contract.domains.vo.contractset.ContractConcludeV;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.contract.apps.fo.revision.pay.ContractPayConcludeExpandPageF;
import com.wishare.contract.domains.entity.revision.pay.ContractPayConcludeExpandE;
import org.springframework.web.bind.annotation.RestController;
import com.wishare.starter.enums.PromptInfo;
import com.wishare.tools.starter.fo.search.SearchF;
import org.springframework.validation.annotation.Validated;
import com.wishare.contract.apps.service.revision.pay.ContractPayConcludeExpandAppService;
import com.wishare.contract.domains.vo.revision.pay.ContractPayConcludeExpandV;
import com.wishare.contract.apps.fo.revision.pay.ContractPayConcludeExpandF;
import com.wishare.contract.apps.fo.revision.pay.ContractPayConcludeExpandSaveF;
import com.wishare.contract.apps.fo.revision.pay.ContractPayConcludeExpandUpdateF;
import com.wishare.contract.domains.vo.revision.pay.ContractPayConcludeExpandListV;
import com.wishare.contract.apps.fo.revision.pay.ContractPayConcludeExpandListF;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import lombok.RequiredArgsConstructor;
/**
 * <p>
 * 支出合同订立信息拓展表
 * </p>
 *
 * @author chenglong
 * @since 2023-09-22
 */
@RequiredArgsConstructor
@RestController
@Validated
@Api(tags = {"支出合同订立信息拓展表"})
@RequestMapping("/contractPayConcludeExpand")
public class ContractPayConcludeExpandApi {

    private final ContractPayConcludeExpandAppService contractPayConcludeExpandAppService;
    @ApiOperation(value = "获取详细信息", notes = "获取详细信息", response = ContractPayConcludeExpandV.class)
    @GetMapping
    public ContractPayConcludeExpandV get(@Validated ContractPayConcludeExpandF contractPayConcludeExpandF){
        return contractPayConcludeExpandAppService.get(contractPayConcludeExpandF);
    }

    @ApiOperation(value = "获取详细信息", notes = "获取详细信息", response = ContractPayConcludeExpandV.class)
    @PostMapping("/get")
    public ContractPayConcludeExpandV gets(@Validated @RequestBody ContractPayConcludeExpandF contractPayConcludeExpandF){
        return contractPayConcludeExpandAppService.get(contractPayConcludeExpandF);
    }

    @ApiOperation(value = "下拉列表", notes = "下拉列表，默认数量20", response = ContractPayConcludeExpandV.class)
    @PostMapping("/list")
    public ContractPayConcludeExpandListV list(@Validated @RequestBody ContractPayConcludeExpandListF contractPayConcludeExpandListF){
        return contractPayConcludeExpandAppService.list(contractPayConcludeExpandListF);
    }

    @ApiOperation(value = "新增保存", notes = "新增保存")
    @PostMapping
    public Long save(@Validated @RequestBody ContractPayConcludeExpandSaveF contractPayConcludeExpandF){
        return contractPayConcludeExpandAppService.save(contractPayConcludeExpandF);
    }

    @ApiOperation(value = "更新", notes = "根据主键更新")
    @PutMapping
    public String update(@Validated @RequestBody ContractPayConcludeExpandUpdateF contractPayConcludeExpandF){
        contractPayConcludeExpandAppService.update(contractPayConcludeExpandF);
        return PromptInfo.OK.info;
    }

    @ApiOperation(value = "删除", notes = "根据主键删除")
    @DeleteMapping
    public boolean removeById(@RequestParam("id") Long id){
        return contractPayConcludeExpandAppService.removeById(id);
    }

    @ApiOperation(value = "分页列表", response = ContractPayConcludeExpandV.class)
    @PostMapping("/page")
    public PageV<ContractPayConcludeExpandV> page(@RequestBody PageF<ContractPayConcludeExpandPageF> request) {
        return contractPayConcludeExpandAppService.page(request);
    }

    @ApiOperation(value = "分页列表仅供前台调用", response = ContractPayConcludeExpandV.class)
    @PostMapping("/pageFront")
    public PageV<ContractPayConcludeExpandV> frontPage(@RequestBody PageF<SearchF<ContractPayConcludeExpandE>> request) {
        return contractPayConcludeExpandAppService.frontPage(request);
    }

    @ApiOperation(value = "根据合同id查询收款信息详情", notes = "根据合同id查询收款信息详情", response = ContractConcludeV.class)
    @GetMapping("/getPaymentInformationDetailsById")
    public ContractPayConcludeExpandV getPaymentInformationDetailsById(@RequestParam("contractId") String contractId) {
        return contractPayConcludeExpandAppService.getPaymentInformationDetailsById(contractId);
    }
}
