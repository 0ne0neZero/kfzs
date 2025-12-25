package com.wishare.contract.apis.revision.bond;

import com.wishare.contract.domains.vo.revision.bond.BondCollectDetailV;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.contract.apps.fo.revision.bond.RevisionBondCollectPageF;
import com.wishare.contract.domains.entity.revision.bond.RevisionBondCollectE;
import org.springframework.web.bind.annotation.RestController;
import com.wishare.starter.enums.PromptInfo;
import com.wishare.tools.starter.fo.search.SearchF;
import org.springframework.validation.annotation.Validated;
import com.wishare.contract.apps.service.revision.bond.RevisionBondCollectAppService;
import com.wishare.contract.domains.vo.revision.bond.RevisionBondCollectV;
import com.wishare.contract.apps.fo.revision.bond.RevisionBondCollectF;
import com.wishare.contract.apps.fo.revision.bond.RevisionBondCollectSaveF;
import com.wishare.contract.apps.fo.revision.bond.RevisionBondCollectUpdateF;
import com.wishare.contract.domains.vo.revision.bond.RevisionBondCollectListV;
import com.wishare.contract.apps.fo.revision.bond.RevisionBondCollectListF;
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
 * 保证金改版-收取类保证金
 * </p>
 *
 * @author chenglong
 * @since 2023-07-26
 */
@RequiredArgsConstructor
@RestController
@Validated
@Api(tags = {"保证金改版-收取类保证金"})
@RequestMapping("/revisionBondCollect")
public class RevisionBondCollectApi {

    private final RevisionBondCollectAppService revisionBondCollectAppService;
    @ApiOperation(value = "获取详细信息", notes = "获取详细信息", response = RevisionBondCollectV.class)
    @GetMapping
    public RevisionBondCollectV get(@Validated RevisionBondCollectF revisionBondCollectF){
        return revisionBondCollectAppService.get(revisionBondCollectF);
    }

    @ApiOperation(value = "下拉列表", notes = "下拉列表，默认数量20", response = RevisionBondCollectV.class)
    @PostMapping("/list")
    public RevisionBondCollectListV list(@Validated @RequestBody RevisionBondCollectListF revisionBondCollectListF){
        return revisionBondCollectAppService.list(revisionBondCollectListF);
    }

    @ApiOperation(value = "新增保存", notes = "新增保存")
    @PostMapping
    public String save(@Validated @RequestBody RevisionBondCollectSaveF revisionBondCollectF){
        return revisionBondCollectAppService.save(revisionBondCollectF);
    }

    @ApiOperation(value = "更新", notes = "根据主键更新")
    @PutMapping
    public String update(@Validated @RequestBody RevisionBondCollectUpdateF revisionBondCollectF){
        revisionBondCollectAppService.update(revisionBondCollectF);
        return PromptInfo.OK.info;
    }

    @ApiOperation(value = "删除", notes = "根据主键删除")
    @DeleteMapping
    public boolean removeById(@RequestParam("id") String id){
        return revisionBondCollectAppService.removeById(id);
    }

    @ApiOperation(value = "分页列表", response = RevisionBondCollectV.class)
    @PostMapping("/page")
    public PageV<RevisionBondCollectV> page(@RequestBody PageF<RevisionBondCollectPageF> request) {
        return revisionBondCollectAppService.page(request);
    }

    @ApiOperation(value = "分页列表仅供前台调用", response = RevisionBondCollectV.class)
    @PostMapping("/pageFront")
    public PageV<BondCollectDetailV> frontPage(@RequestBody PageF<SearchF<RevisionBondCollectE>> request) {
        return revisionBondCollectAppService.frontPage(request);
    }

}
