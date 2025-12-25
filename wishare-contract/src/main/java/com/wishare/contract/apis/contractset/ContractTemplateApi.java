package com.wishare.contract.apis.contractset;

import com.wishare.contract.apps.fo.contractset.*;
import com.wishare.contract.apps.service.contractset.ContractTemplateAppService;
import com.wishare.contract.apps.vo.contractset.ContractTemplateTreeV;
import com.wishare.contract.apps.vo.contractset.ContractTemplateV;
import com.wishare.contract.domains.entity.contractset.ContractTemplateE;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(tags = {"合同范本接口"})
@RestController
@RequestMapping("/contract/template")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ContractTemplateApi {

    private final ContractTemplateAppService contractTemplateAppService;

    @ApiOperation(value = "批量新增合同范本", notes = "批量新增合同范本")
    @PostMapping("/create")
    public Map<String, List<String>> create(@RequestBody @Validated List<CreateContractTemplateF> createContractTemplateFList) {
        return contractTemplateAppService.createContractTemplate(createContractTemplateFList);
    }

    @ApiOperation(value = "合同范本分页查询", notes = "合同范本分页查询")
    @PostMapping("/query/page")
    public PageV<ContractTemplateTreeV> queryByPage(@RequestBody PageF<SearchF<PageContractTemplateF>> form) {
        return contractTemplateAppService.queryByPage(form);
    }

    @ApiOperation(value = "id查询合同范本", notes = "id查询合同范本")
    @GetMapping("/query/id/{id}")
    public ContractTemplateV queryById(@PathVariable Long id) {
        return contractTemplateAppService.queryById(id);
    }

    @ApiOperation(value = "变更范本",notes = "变更范本")
    @PostMapping("/change")
    public Long changeContractTemplate(@RequestBody @Validated ChangeContractTemplateF changeContractTemplateF) {
        return contractTemplateAppService.changeContractTemplate(changeContractTemplateF);
    }

    @ApiOperation(value = "删除范本",notes = "删除范本")
    @DeleteMapping("/delete/id/{id}")
    public Boolean deleteContractTemplate(@PathVariable Long id) {
        return contractTemplateAppService.deleteById(id);
    }

    @ApiOperation(value = "编辑范本",notes = "编辑范本")
    @PutMapping("/editor")
    public Boolean editorContractTemplate(@RequestBody @Validated EditorContractTemplateF contractTemplateF) {
        return contractTemplateAppService.editorContractTemplate(contractTemplateF);
    }

    @ApiOperation(value = "启用/禁用范本",notes = "启用/禁用范本")
    @PutMapping("/update/status")
    public Boolean updateContractTemplateStatus(@RequestBody @Validated UpdateContractTemplateF updateContractTemplateF) {
        return contractTemplateAppService.updateContractTemplateStatus(updateContractTemplateF);
    }

    @ApiOperation(value = "查询范本集", notes = "查询范本集")
    @GetMapping("/query")
    public List<ContractTemplateV> query(@ModelAttribute ContractTemplateF contractTemplateF) {
        return contractTemplateAppService.query(contractTemplateF);
    }
}
