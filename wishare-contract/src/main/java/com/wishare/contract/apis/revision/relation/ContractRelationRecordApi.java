package com.wishare.contract.apis.revision.relation;

import org.springframework.web.bind.annotation.RequestMapping;

import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.contract.apps.fo.revision.relation.ContractRelationRecordPageF;
import com.wishare.contract.domains.entity.revision.relation.ContractRelationRecordE;
import org.springframework.web.bind.annotation.RestController;
import com.wishare.starter.enums.PromptInfo;
import com.wishare.tools.starter.fo.search.SearchF;
import org.springframework.validation.annotation.Validated;
import com.wishare.contract.apps.service.revision.relation.ContractRelationRecordAppService;
import com.wishare.contract.domains.vo.revision.relation.ContractRelationRecordV;
import com.wishare.contract.apps.fo.revision.relation.ContractRelationRecordF;
import com.wishare.contract.apps.fo.revision.relation.ContractRelationRecordSaveF;
import com.wishare.contract.apps.fo.revision.relation.ContractRelationRecordUpdateF;
import com.wishare.contract.domains.vo.revision.relation.ContractRelationRecordListV;
import com.wishare.contract.apps.fo.revision.relation.ContractRelationRecordListF;
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
 * 
 * </p>
 *
 * @author chenglong
 * @since 2023-06-28
 */
@RequiredArgsConstructor
@RestController
@Validated
@Api(tags = {""})
@RequestMapping("/contractRelationRecord")
public class ContractRelationRecordApi {

    private final ContractRelationRecordAppService contractRelationRecordAppService;
    @ApiOperation(value = "获取详细信息", notes = "获取详细信息", response = ContractRelationRecordV.class)
    @GetMapping
    public ContractRelationRecordV get(@Validated ContractRelationRecordF contractRelationRecordF){
        return contractRelationRecordAppService.get(contractRelationRecordF);
    }

    @ApiOperation(value = "下拉列表", notes = "下拉列表，默认数量20", response = ContractRelationRecordV.class)
    @PostMapping("/list")
    public ContractRelationRecordListV list(@Validated @RequestBody ContractRelationRecordListF contractRelationRecordListF){
        return contractRelationRecordAppService.list(contractRelationRecordListF);
    }

    @ApiOperation(value = "新增保存", notes = "新增保存")
    @PostMapping
    public String save(@Validated @RequestBody ContractRelationRecordSaveF contractRelationRecordF){
        return contractRelationRecordAppService.save(contractRelationRecordF);
    }

    @ApiOperation(value = "更新", notes = "根据主键更新")
    @PutMapping
    public String update(@Validated @RequestBody ContractRelationRecordUpdateF contractRelationRecordF){
        contractRelationRecordAppService.update(contractRelationRecordF);
        return PromptInfo.OK.info;
    }

    @ApiOperation(value = "删除", notes = "根据主键删除")
    @DeleteMapping
    public boolean removeById(@RequestParam("id") String id){
        return contractRelationRecordAppService.removeById(id);
    }

    @ApiOperation(value = "分页列表", response = ContractRelationRecordV.class)
    @PostMapping("/page")
    public PageV<ContractRelationRecordV> page(@RequestBody PageF<ContractRelationRecordPageF> request) {
        return contractRelationRecordAppService.page(request);
    }

    @ApiOperation(value = "分页列表仅供前台调用", response = ContractRelationRecordV.class)
    @PostMapping("/pageFront")
    public PageV<ContractRelationRecordV> frontPage(@RequestBody PageF<SearchF<ContractRelationRecordE>> request) {
        return contractRelationRecordAppService.frontPage(request);
    }

}
