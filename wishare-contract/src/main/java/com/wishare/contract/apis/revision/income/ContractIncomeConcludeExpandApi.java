package com.wishare.contract.apis.revision.income;

import org.springframework.web.bind.annotation.RequestMapping;

import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.contract.apps.fo.revision.income.ContractIncomeConcludeExpandPageF;
import com.wishare.contract.domains.entity.revision.income.ContractIncomeConcludeExpandE;
import org.springframework.web.bind.annotation.RestController;
import com.wishare.starter.enums.PromptInfo;
import com.wishare.tools.starter.fo.search.SearchF;
import org.springframework.validation.annotation.Validated;
import com.wishare.contract.apps.service.revision.income.ContractIncomeConcludeExpandAppService;
import com.wishare.contract.domains.vo.revision.income.ContractIncomeConcludeExpandV;
import com.wishare.contract.apps.fo.revision.income.ContractIncomeConcludeExpandF;
import com.wishare.contract.apps.fo.revision.income.ContractIncomeConcludeExpandSaveF;
import com.wishare.contract.apps.fo.revision.income.ContractIncomeConcludeExpandUpdateF;
import com.wishare.contract.domains.vo.revision.income.ContractIncomeConcludeExpandListV;
import com.wishare.contract.apps.fo.revision.income.ContractIncomeConcludeExpandListF;
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
 * 收入合同订立信息拓展表
 * </p>
 *
 * @author chenglong
 * @since 2023-09-22
 */
@RequiredArgsConstructor
@RestController
@Validated
@Api(tags = {"收入合同订立信息拓展表"})
@RequestMapping("/contractIncomeConcludeExpand")
public class ContractIncomeConcludeExpandApi {

    private final ContractIncomeConcludeExpandAppService contractIncomeConcludeExpandAppService;
    @ApiOperation(value = "获取详细信息", notes = "获取详细信息", response = ContractIncomeConcludeExpandV.class)
    @GetMapping
    public ContractIncomeConcludeExpandV get(@Validated ContractIncomeConcludeExpandF contractIncomeConcludeExpandF){
        return contractIncomeConcludeExpandAppService.get(contractIncomeConcludeExpandF);
    }

    @ApiOperation(value = "下拉列表", notes = "下拉列表，默认数量20", response = ContractIncomeConcludeExpandV.class)
    @PostMapping("/list")
    public ContractIncomeConcludeExpandListV list(@Validated @RequestBody ContractIncomeConcludeExpandListF contractIncomeConcludeExpandListF){
        return contractIncomeConcludeExpandAppService.list(contractIncomeConcludeExpandListF);
    }

    @ApiOperation(value = "新增保存", notes = "新增保存")
    @PostMapping
    public Long save(@Validated @RequestBody ContractIncomeConcludeExpandSaveF contractIncomeConcludeExpandF){
        return contractIncomeConcludeExpandAppService.save(contractIncomeConcludeExpandF);
    }

    @ApiOperation(value = "更新", notes = "根据主键更新")
    @PutMapping
    public String update(@Validated @RequestBody ContractIncomeConcludeExpandUpdateF contractIncomeConcludeExpandF){
        contractIncomeConcludeExpandAppService.update(contractIncomeConcludeExpandF);
        return PromptInfo.OK.info;
    }

    @ApiOperation(value = "删除", notes = "根据主键删除")
    @DeleteMapping
    public boolean removeById(@RequestParam("id") Long id){
        return contractIncomeConcludeExpandAppService.removeById(id);
    }

    @ApiOperation(value = "分页列表", response = ContractIncomeConcludeExpandV.class)
    @PostMapping("/page")
    public PageV<ContractIncomeConcludeExpandV> page(@RequestBody PageF<ContractIncomeConcludeExpandPageF> request) {
        return contractIncomeConcludeExpandAppService.page(request);
    }

    @ApiOperation(value = "分页列表仅供前台调用", response = ContractIncomeConcludeExpandV.class)
    @PostMapping("/pageFront")
    public PageV<ContractIncomeConcludeExpandV> frontPage(@RequestBody PageF<SearchF<ContractIncomeConcludeExpandE>> request) {
        return contractIncomeConcludeExpandAppService.frontPage(request);
    }

}
