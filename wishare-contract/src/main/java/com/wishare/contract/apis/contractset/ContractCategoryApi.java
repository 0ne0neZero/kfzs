package com.wishare.contract.apis.contractset;

import com.wishare.contract.apps.fo.contractset.CreateContractCategoryF;
import com.wishare.contract.apps.fo.contractset.UpdateContractCategoryF;
import com.wishare.contract.apps.service.contractset.ContractCategoryAppService;
import com.wishare.contract.apps.vo.contractset.ContractCategoryDetailV;
import com.wishare.contract.apps.vo.contractset.ContractCategoryTreeV;
import com.wishare.contract.apps.vo.contractset.ContractCategoryV;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *  合同分类控制器
 *
 * @author yancao
 */
@Api(tags = {"合同分类接口"})
@RestController
@RequestMapping("/contract/category")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ContractCategoryApi {

    private final ContractCategoryAppService contractCategoryAppService;

    @ApiOperation(value = "新增合同分类", notes = "新增合同分类")
    @PostMapping("/create")
    public Long create(@RequestBody @Validated CreateContractCategoryF createContractCategoryF) {
        return contractCategoryAppService.create(createContractCategoryF);
    }

    @ApiOperation(value = "更新合同分类", notes = "更新合同分类")
    @PostMapping("/update")
    public Boolean update(@RequestBody @Validated UpdateContractCategoryF updateContractCategoryF) {
        return contractCategoryAppService.update(updateContractCategoryF);
    }

    @ApiOperation(value = "删除合同分类", notes = "删除合同分类")
    @GetMapping("/delete/id/{id}")
    public Boolean delete( @PathVariable(value = "id") Long id) {
        return contractCategoryAppService.delete(id);
    }

    @ApiOperation(value = "获取合同分类树", notes = "获取合同分类树")
    @GetMapping("/query/tree")
    public List<ContractCategoryTreeV> queryTree(@RequestParam(value = "keyword",required = false) String keyword) {
        return contractCategoryAppService.queryTree(keyword);
    }

    @ApiOperation(value = "根据parentId获取下级分类", notes = "根据parentId获取下级分类")
    @GetMapping("/query/parentId")
    public List<ContractCategoryV> queryByParentId(@RequestParam(value = "parentId",required = false) Long parentId) {
        return contractCategoryAppService.queryByParentId(parentId);
    }

    @ApiOperation(value = "根据id获取合同分类详细信息", notes = "根据id获取合同分类详细信息")
    @GetMapping("/query/id/{id}")
    public ContractCategoryDetailV queryById(@PathVariable(value = "id") Long id) {
        return contractCategoryAppService.queryById(id);
    }
}
