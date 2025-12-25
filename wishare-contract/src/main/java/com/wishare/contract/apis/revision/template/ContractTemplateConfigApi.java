package com.wishare.contract.apis.revision.template;

import org.springframework.web.bind.annotation.RequestMapping;

import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.contract.apps.fo.revision.template.ContractTemplateConfigPageF;
import com.wishare.contract.domains.entity.revision.template.ContractTemplateConfigE;
import org.springframework.web.bind.annotation.RestController;
import com.wishare.starter.enums.PromptInfo;
import com.wishare.tools.starter.fo.search.SearchF;
import org.springframework.validation.annotation.Validated;
import com.wishare.contract.apps.service.revision.template.ContractTemplateConfigAppService;
import com.wishare.contract.domains.vo.revision.template.ContractTemplateConfigV;
import com.wishare.contract.apps.fo.revision.template.ContractTemplateConfigF;
import com.wishare.contract.apps.fo.revision.template.ContractTemplateConfigSaveF;
import com.wishare.contract.apps.fo.revision.template.ContractTemplateConfigUpdateF;
import com.wishare.contract.domains.vo.revision.template.ContractTemplateConfigListV;
import com.wishare.contract.apps.fo.revision.template.ContractTemplateConfigListF;
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
 * 合同范本字段配置表
 * </p>
 *
 * @author zhangfuyu
 * @since 2023-07-26
 */
@RequiredArgsConstructor
@RestController
@Validated
@Api(tags = {"合同范本字段配置表"})
@RequestMapping("/contractTemplateConfig")
public class ContractTemplateConfigApi {

    private final ContractTemplateConfigAppService contractTemplateConfigAppService;
    @ApiOperation(value = "获取详细信息", notes = "获取详细信息", response = ContractTemplateConfigV.class)
    @GetMapping
    public ContractTemplateConfigV get(@Validated ContractTemplateConfigF contractTemplateConfigF){
        return contractTemplateConfigAppService.get(contractTemplateConfigF);
    }

    @ApiOperation(value = "下拉列表", notes = "下拉列表，默认数量20", response = ContractTemplateConfigV.class)
    @PostMapping("/list")
    public ContractTemplateConfigListV list(@Validated @RequestBody ContractTemplateConfigListF contractTemplateConfigListF){
        return contractTemplateConfigAppService.list(contractTemplateConfigListF);
    }

    @ApiOperation(value = "新增保存", notes = "新增保存")
    @PostMapping
    public String save(@Validated @RequestBody ContractTemplateConfigSaveF contractTemplateConfigF){
        return contractTemplateConfigAppService.save(contractTemplateConfigF);
    }

    @ApiOperation(value = "更新", notes = "根据主键更新")
    @PutMapping
    public String update(@Validated @RequestBody ContractTemplateConfigUpdateF contractTemplateConfigF){
        contractTemplateConfigAppService.update(contractTemplateConfigF);
        return PromptInfo.OK.info;
    }

    @ApiOperation(value = "删除", notes = "根据主键删除")
    @DeleteMapping
    public boolean removeById(@RequestParam("id") String id){
        return contractTemplateConfigAppService.removeById(id);
    }

    @ApiOperation(value = "分页列表", response = ContractTemplateConfigV.class)
    @PostMapping("/page")
    public PageV<ContractTemplateConfigV> page(@RequestBody PageF<ContractTemplateConfigPageF> request) {
        return contractTemplateConfigAppService.page(request);
    }

    @ApiOperation(value = "分页列表仅供前台调用", response = ContractTemplateConfigV.class)
    @PostMapping("/pageFront")
    public PageV<ContractTemplateConfigV> frontPage(@RequestBody PageF<SearchF<ContractTemplateConfigE>> request) {
        return contractTemplateConfigAppService.frontPage(request);
    }

}
