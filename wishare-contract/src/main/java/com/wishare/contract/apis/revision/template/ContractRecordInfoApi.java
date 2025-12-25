package com.wishare.contract.apis.revision.template;

import org.springframework.web.bind.annotation.RequestMapping;

import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.contract.apps.fo.revision.template.ContractRecordInfoPageF;
import com.wishare.contract.domains.entity.revision.template.ContractRecordInfoE;
import org.springframework.web.bind.annotation.RestController;
import com.wishare.starter.enums.PromptInfo;
import com.wishare.tools.starter.fo.search.SearchF;
import org.springframework.validation.annotation.Validated;
import com.wishare.contract.apps.service.revision.template.ContractRecordInfoAppService;
import com.wishare.contract.domains.vo.revision.template.ContractRecordInfoV;
import com.wishare.contract.apps.fo.revision.template.ContractRecordInfoF;
import com.wishare.contract.apps.fo.revision.template.ContractRecordInfoSaveF;
import com.wishare.contract.apps.fo.revision.template.ContractRecordInfoUpdateF;
import com.wishare.contract.domains.vo.revision.template.ContractRecordInfoListV;
import com.wishare.contract.apps.fo.revision.template.ContractRecordInfoListF;
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
 * 合同修改记录表
 * </p>
 *
 * @author zhangfuyu
 * @since 2023-07-28
 */
@RequiredArgsConstructor
@RestController
@Validated
@Api(tags = {"合同修改记录表"})
@RequestMapping("/contractRecordInfo")
public class ContractRecordInfoApi {

    private final ContractRecordInfoAppService contractRecordInfoAppService;
    @ApiOperation(value = "获取详细信息", notes = "获取详细信息", response = ContractRecordInfoV.class)
    @GetMapping
    public ContractRecordInfoV get(@Validated ContractRecordInfoF contractRecordInfoF){
        return contractRecordInfoAppService.get(contractRecordInfoF);
    }

    @ApiOperation(value = "下拉列表", notes = "下拉列表，默认数量20", response = ContractRecordInfoV.class)
    @PostMapping("/list")
    public ContractRecordInfoListV list(@Validated @RequestBody ContractRecordInfoListF contractRecordInfoListF){
        return contractRecordInfoAppService.list(contractRecordInfoListF);
    }

    @ApiOperation(value = "新增保存", notes = "新增保存")
    @PostMapping
    public String save(@Validated @RequestBody ContractRecordInfoSaveF contractRecordInfoF){
        return contractRecordInfoAppService.save(contractRecordInfoF);
    }

    @ApiOperation(value = "更新", notes = "根据主键更新")
    @PutMapping
    public String update(@Validated @RequestBody ContractRecordInfoUpdateF contractRecordInfoF){
        contractRecordInfoAppService.update(contractRecordInfoF);
        return PromptInfo.OK.info;
    }

    @ApiOperation(value = "删除", notes = "根据主键删除")
    @DeleteMapping
    public boolean removeById(@RequestParam("id") String id){
        return contractRecordInfoAppService.removeById(id);
    }

    @ApiOperation(value = "分页列表", response = ContractRecordInfoV.class)
    @PostMapping("/page")
    public PageV<ContractRecordInfoV> page(@RequestBody PageF<ContractRecordInfoPageF> request) {
        return contractRecordInfoAppService.page(request);
    }

    @ApiOperation(value = "分页列表仅供前台调用", response = ContractRecordInfoV.class)
    @PostMapping("/pageFront")
    public PageV<ContractRecordInfoV> frontPage(@RequestBody PageF<SearchF<ContractRecordInfoE>> request) {
        return contractRecordInfoAppService.frontPage(request);
    }

}
