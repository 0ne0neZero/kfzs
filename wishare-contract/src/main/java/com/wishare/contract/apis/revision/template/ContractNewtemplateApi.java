package com.wishare.contract.apis.revision.template;

import com.wishare.tools.starter.vo.FileVo;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.contract.apps.fo.revision.template.ContractNewtemplatePageF;
import com.wishare.contract.domains.entity.revision.template.ContractNewtemplateE;
import org.springframework.web.bind.annotation.RestController;
import com.wishare.starter.enums.PromptInfo;
import com.wishare.tools.starter.fo.search.SearchF;
import org.springframework.validation.annotation.Validated;
import com.wishare.contract.apps.service.revision.template.ContractNewtemplateAppService;
import com.wishare.contract.domains.vo.revision.template.ContractNewtemplateV;
import com.wishare.contract.apps.fo.revision.template.ContractNewtemplateF;
import com.wishare.contract.apps.fo.revision.template.ContractNewtemplateSaveF;
import com.wishare.contract.apps.fo.revision.template.ContractNewtemplateUpdateF;
import com.wishare.contract.domains.vo.revision.template.ContractNewtemplateListV;
import com.wishare.contract.apps.fo.revision.template.ContractNewtemplateListF;
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

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 新合同范本表
 * </p>
 *
 * @author zhangfy
 * @since 2023-07-21
 */
@RequiredArgsConstructor
@RestController
@Validated
@Api(tags = {"新合同范本表"})
@RequestMapping("/contractNewtemplate")
public class ContractNewtemplateApi {

    private final ContractNewtemplateAppService contractNewtemplateAppService;
    @ApiOperation(value = "获取详细信息", notes = "获取详细信息", response = ContractNewtemplateV.class)
    @GetMapping
    public ContractNewtemplateV get(@Validated ContractNewtemplateF contractNewtemplateF){
        return contractNewtemplateAppService.get(contractNewtemplateF);
    }

    @ApiOperation(value = "下拉列表", notes = "下拉列表，默认数量20", response = ContractNewtemplateV.class)
    @PostMapping("/list")
    public List<ContractNewtemplateV> list(@Validated @RequestBody ContractNewtemplateListF contractNewtemplateListF){
        return contractNewtemplateAppService.list(contractNewtemplateListF);
    }

    @ApiOperation(value = "新增保存", notes = "新增保存")
    @PostMapping
    public String save(@Validated @RequestBody ContractNewtemplateSaveF contractNewtemplateF){
        return contractNewtemplateAppService.save(contractNewtemplateF);
    }

    @ApiOperation(value = "更新", notes = "根据主键更新")
    @PutMapping
    public String update(@Validated @RequestBody ContractNewtemplateUpdateF contractNewtemplateF){
        contractNewtemplateAppService.update(contractNewtemplateF);
        return PromptInfo.OK.info;
    }

    @ApiOperation(value = "禁用", notes = "根据主键更新")
    @PostMapping("/disable")
    public String disable(@RequestParam("id") String id){
        contractNewtemplateAppService.disable(id);
        return PromptInfo.OK.info;
    }

    @ApiOperation(value = "启用", notes = "根据主键更新")
    @PostMapping("/able")
    public String able(@RequestParam("id") String id){
        contractNewtemplateAppService.able(id);
        return PromptInfo.OK.info;
    }

    @ApiOperation("预览")
    @PostMapping("/preview")
    public String preview(@RequestParam("id") String id) {
        return contractNewtemplateAppService.preview(id);
    }

    @ApiOperation(value = "删除", notes = "根据主键删除")
    @DeleteMapping
    public boolean removeById(@RequestParam("id") String id){
        return contractNewtemplateAppService.removeById(id);
    }

    @ApiOperation(value = "分页列表", response = ContractNewtemplateV.class)
    @PostMapping("/page")
    public PageV<ContractNewtemplateV> page(@RequestBody PageF<ContractNewtemplatePageF> request) {
        return contractNewtemplateAppService.page(request);
    }

    @ApiOperation(value = "分页列表仅供前台调用", response = ContractNewtemplateV.class)
    @PostMapping("/pageFront")
    public PageV<ContractNewtemplateV> frontPage(@RequestBody PageF<SearchF<ContractNewtemplateE>> request) {
        return contractNewtemplateAppService.frontPage(request);
    }

}
