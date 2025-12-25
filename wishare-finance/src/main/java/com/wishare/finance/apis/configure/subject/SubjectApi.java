package com.wishare.finance.apis.configure.subject;

import com.wishare.finance.apps.model.configure.subject.fo.CreateSubjectF;
import com.wishare.finance.apps.model.configure.subject.fo.UpdateSubjectF;
import com.wishare.finance.apps.model.configure.subject.vo.SubjectDetailV;
import com.wishare.finance.apps.model.configure.subject.vo.SubjectSimpleV;
import com.wishare.finance.apps.model.configure.subject.vo.SubjectTreeV;
import com.wishare.finance.apps.model.configure.subject.vo.SubjectV;
import com.wishare.finance.apps.service.configure.subject.SubjectAppService;
import com.wishare.finance.domains.configure.subject.command.subject.SyncSubjectCommand;
import com.wishare.finance.domains.configure.subject.command.subject.SyncSystemSubjectCommand;
import com.wishare.finance.domains.configure.subject.entity.SubjectE;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 科目控制器
 *
 * @author yancao
 */
@Api(tags = {"科目接口"})
@RestController
@RequestMapping("/subject")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SubjectApi {

    private final SubjectAppService subjectAppService;

    @ApiOperation(value = "新增科目", notes = "新增科目")
    @PostMapping("/create")
    public Long create(@RequestBody @Validated CreateSubjectF createSubjectF) {
        return subjectAppService.create(createSubjectF);
    }

    @ApiOperation(value = "更新科目", notes = "更新科目")
    @PostMapping("/update")
    Boolean update(@RequestBody @Validated UpdateSubjectF updateSubjectF) {
        return subjectAppService.update(updateSubjectF);
    }

    @ApiOperation(value = "删除科目", notes = "删除科目")
    @DeleteMapping("/delete/id/{id}")
    public Boolean delete(@PathVariable("id") @ApiParam("科目id") Long id) {
        return subjectAppService.delete(id);
    }

    @ApiOperation(value = "根据ID查科目数据", notes = "根据ID查科目数据")
    @GetMapping("/query/id/{id}")
    public SubjectDetailV getById(@PathVariable("id") @ApiParam("科目id") Long id) {
        return subjectAppService.getById(id);
    }

    @ApiOperation("分页查询科目")
    @PostMapping("/query/page")
    PageV<SubjectV> queryByPage(@RequestBody PageF<SearchF<SubjectE>> querySubjectPageF) {
        return subjectAppService.queryByPage(querySubjectPageF);
    }

    @ApiOperation("获取科目树")
    @GetMapping("/query/tree")
    List<SubjectTreeV> queryTree(@RequestParam(value = "filterId",required = false) Long filterId,
                                 @RequestParam(value = "categoryId",required = false) Long categoryId,
                                 @RequestParam(value = "subjectSystemId",required = false) Long subjectSystemId) {
        return subjectAppService.queryTree(filterId,categoryId, subjectSystemId);
    }

    @ApiOperation("获取科目列表")
    @GetMapping("/query/list")
    List<SubjectV> queryList(@RequestParam(value = "filterId",required = false) Long filterId,
                                 @RequestParam(value = "categoryId",required = false) Long categoryId,
                                 @RequestParam(value = "subjectSystemId",required = false) Long subjectSystemId) {
        return subjectAppService.queryList(filterId,categoryId, subjectSystemId);
    }


    @ApiOperation("获取科目列表(带全路径)")
    @GetMapping("/query/list/allpath")
    List<SubjectV> queryListWithAllPath(@RequestParam(value = "filterId",required = false) Long filterId,
                                 @RequestParam(value = "categoryId",required = false) Long categoryId,
                                 @RequestParam(value = "subjectSystemId",required = false) Long subjectSystemId) {
        return subjectAppService.queryListWithAllPath(filterId,categoryId, subjectSystemId);
    }

    @ApiOperation(value = "获取末级科目(最后一级科目)", notes = "获取末级科目(最后一级科目)")
    @GetMapping("/query/lastStage")
    public List<SubjectSimpleV> getLastStage(@RequestParam(value = "systemId",required = false) Long systemId,
                                             @RequestParam(value = "categoryId", required = false) Long categoryId,
                                             @RequestParam(value = "name",required = false) String name,
                                             @RequestParam(value = "subjectId",required = false) Long subjectId){
        return subjectAppService.getLastStage(systemId,categoryId,name,subjectId);
    }

    @ApiOperation(value = "获取末级科目(最后一级科目) 全路径", notes = "获取末级科目(最后一级科目)")
    @GetMapping("/query/lastStage/allPatch")
    public List<SubjectV> getLastStageAllPatch(@RequestParam(value = "systemId",required = false) Long systemId,
                                             @RequestParam(value = "categoryId", required = false) Long categoryId,
                                             @RequestParam(value = "name",required = false) String name,
                                             @RequestParam(value = "subjectId",required = false) Long subjectId){
        return subjectAppService.getLastStageAllPatch(systemId,categoryId,name,subjectId);
    }

    @ApiOperation("根据id查询子科目列表")
    @GetMapping("/query/child")
    List<SubjectSimpleV> queryChildById(@RequestParam(value = "id", required = false) Long id,
                                        @RequestParam(value = "systemId", required = false) Long systemId) {
        return subjectAppService.queryChildById(id,systemId);
    }

    @ApiOperation(value = "导入科目", notes = "导入科目")
    @PostMapping("/import")
    @ApiImplicitParam(name = "file", value = "文件", dataType = "__File", allowMultiple = true, paramType = "query", dataTypeClass = MultipartFile.class)
    public Boolean importCharge(@RequestParam("file") MultipartFile file, @RequestParam(value = "subjectSystemId") Long subjectSystemId){
        return subjectAppService.importSubject(file,subjectSystemId);
    }

    @ApiOperation(value = "根据ID查科目名称", notes = "根据ID查科目名称")
    @GetMapping("/query/name/{id}")
    public String getNameById(@PathVariable("id") @ApiParam("科目id") Long id) {
        return subjectAppService.getNameById(id);
    }

    @ApiOperation(value = "根据IDs查科目数据", notes = "根据IDs查科目数据")
    @GetMapping("/list/ids")
    public List<SubjectV> listByIds(@RequestParam List<Long> subjects) {
        return subjectAppService.listByIds(subjects);
    }

    @ApiOperation(value = "根据费项id查科目数据", notes = "根据费项id查科目数据")
    @GetMapping("/detail/map")
    public SubjectV getSubjectByChargeItemIdAndHeadSubjectId(@RequestParam Long headSubjectId, @RequestParam Long chargeItemId) {
        return subjectAppService.getSubjectByChargeItemIdAndHeadSubjectId(headSubjectId, chargeItemId);
    }

    @ApiOperation(value = "同步科目信息", notes = "同步科目信息")
    @PostMapping("/sync")
    public Boolean syncSubject(@RequestBody @Validated SyncSystemSubjectCommand syncSystemSubjectCommand){
        return subjectAppService.syncSubject(syncSystemSubjectCommand);
    }

    @ApiOperation(value = "查询科目以及下级科目的所有辅助核算", notes = "查询科目以及下级科目的所有辅助核算")
    @GetMapping("/getAllAuxiliary")
    public List<String> getAllAuxiliary(@RequestParam Long subjectId) {
        return subjectAppService.getAllAuxiliary(subjectId);
    }

}
