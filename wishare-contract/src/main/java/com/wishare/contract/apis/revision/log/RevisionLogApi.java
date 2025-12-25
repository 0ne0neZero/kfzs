package com.wishare.contract.apis.revision.log;

import org.springframework.web.bind.annotation.RequestMapping;

import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.contract.apps.fo.revision.log.RevisionLogPageF;
import com.wishare.contract.domains.entity.revision.log.RevisionLogE;
import org.springframework.web.bind.annotation.RestController;
import com.wishare.starter.enums.PromptInfo;
import com.wishare.tools.starter.fo.search.SearchF;
import org.springframework.validation.annotation.Validated;
import com.wishare.contract.apps.service.revision.log.RevisionLogAppService;
import com.wishare.contract.domains.vo.revision.log.RevisionLogV;
import com.wishare.contract.apps.fo.revision.log.RevisionLogF;
import com.wishare.contract.apps.fo.revision.log.RevisionLogSaveF;
import com.wishare.contract.apps.fo.revision.log.RevisionLogUpdateF;
import com.wishare.contract.domains.vo.revision.log.RevisionLogListV;
import com.wishare.contract.apps.fo.revision.log.RevisionLogListF;
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

import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 合同改版动态记录表
 * </p>
 *
 * @author chenglong
 * @since 2023-07-12
 */
@RequiredArgsConstructor
@RestController
@Validated
@Api(tags = {"合同改版动态记录表"})
@RequestMapping("/revisionLog")
public class RevisionLogApi {

    private final RevisionLogAppService revisionLogAppService;
    @ApiOperation(value = "获取详细信息", notes = "获取详细信息", response = RevisionLogV.class)
    @GetMapping
    public RevisionLogV get(@Validated RevisionLogF revisionLogF){
        return revisionLogAppService.get(revisionLogF);
    }

    @ApiOperation(value = "下拉列表", notes = "下拉列表，默认数量20", response = RevisionLogV.class)
    @PostMapping("/list")
    public RevisionLogListV list(@Validated @RequestBody RevisionLogListF revisionLogListF){
        return revisionLogAppService.list(revisionLogListF);
    }

    @ApiOperation(value = "新增保存", notes = "新增保存")
    @PostMapping
    public String save(@Validated @RequestBody RevisionLogSaveF revisionLogF){
        return revisionLogAppService.save(revisionLogF);
    }

    @ApiOperation(value = "更新", notes = "根据主键更新")
    @PutMapping
    public String update(@Validated @RequestBody RevisionLogUpdateF revisionLogF){
        revisionLogAppService.update(revisionLogF);
        return PromptInfo.OK.info;
    }

    @ApiOperation(value = "删除", notes = "根据主键删除")
    @DeleteMapping
    public boolean removeById(@RequestParam("id") String id){
        return revisionLogAppService.removeById(id);
    }

    @ApiOperation(value = "获取日志动态数据", notes = "获取日志动态数据", response = RevisionLogV.class)
    @GetMapping("/log")
    public List<RevisionLogV> getLogById(@RequestParam("id") @NotBlank(message = "id不可为空") String id) {
        return revisionLogAppService.getListLogById(id);
    }

    @ApiOperation(value = "分页列表", response = RevisionLogV.class)
    @PostMapping("/page")
    public PageV<RevisionLogV> page(@RequestBody PageF<RevisionLogPageF> request) {
        return revisionLogAppService.page(request);
    }

    @ApiOperation(value = "分页列表仅供前台调用", response = RevisionLogV.class)
    @PostMapping("/pageFront")
    public PageV<RevisionLogV> frontPage(@RequestBody PageF<SearchF<RevisionLogE>> request) {
        return revisionLogAppService.frontPage(request);
    }

}
