package com.wishare.finance.apis.configure.subject;

import com.wishare.finance.apps.model.configure.subject.fo.CreateSubjectSystemF;
import com.wishare.finance.apps.model.configure.subject.fo.RelatedLegalBatchF;
import com.wishare.finance.apps.model.configure.subject.fo.RelatedLegalF;
import com.wishare.finance.apps.model.configure.subject.fo.UpdateSubjectSystemF;
import com.wishare.finance.apps.model.configure.subject.vo.SubjectSystemRelatedV;
import com.wishare.finance.apps.model.configure.subject.vo.SubjectSystemSimpleV;
import com.wishare.finance.apps.model.configure.subject.vo.SubjectSystemTreeV;
import com.wishare.finance.apps.model.configure.subject.vo.SubjectSystemV;
import com.wishare.finance.apps.service.configure.subject.SubjectSystemAppService;
import com.wishare.finance.domains.configure.subject.entity.SubjectSystemE;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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

import java.util.List;

/**
 * 科目体系控制器
 *
 * @author yancao
 */
@Api(tags = {"科目体系接口"})
@RestController
@RequestMapping("/subject/system")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SubjectSystemApi {

    private final SubjectSystemAppService subjectSystemAppService;

    @ApiOperation(value = "新增科目体系", notes = "新增科目体系")
    @PostMapping("/create")
    public Long create(@RequestBody @Validated CreateSubjectSystemF createSubjectSystemF) {
        return subjectSystemAppService.create(createSubjectSystemF);
    }

    @ApiOperation(value = "更新科目体系", notes = "更新科目体系")
    @PostMapping("/update")
    Boolean update(@RequestBody @Validated UpdateSubjectSystemF updateSubjectSystemF) {
        return subjectSystemAppService.update(updateSubjectSystemF);
    }

    @ApiOperation(value = "删除科目体系", notes = "删除科目体系")
    @DeleteMapping("/delete/id/{id}")
    public Boolean delete(@PathVariable("id") @ApiParam("科目体系id") Long id) {
        return subjectSystemAppService.delete(id);
    }

    @ApiOperation(value = "根据id查科目体系数据", notes = "根据id查科目体系数据")
    @GetMapping("/query/id/{id}")
    public SubjectSystemV queryById(@PathVariable("id") @ApiParam("科目体系id") Long id) {
        return subjectSystemAppService.queryById(id);
    }

    @ApiOperation(value = "根据法定单位id查询科目体系", notes = "根据法定单位id查询科目体系")
    @GetMapping("/query/pertainId")
    List<SubjectSystemV> queryByPertainId(@RequestParam(value = "id",required = false) @ApiParam("法定单位id") Long id) {
        return subjectSystemAppService.queryByPertainId(id);
    }

    @ApiOperation(value = "根据名称查询所有科目体系", notes = "根据名称查询所有科目体系")
    @GetMapping("/query/all")
    List<SubjectSystemV> queryAllByTenantId(@RequestParam(value = "name",required = false) String name){
        return subjectSystemAppService.queryAllByTenantId(name);
    }

    @ApiOperation(value = "批量关联法定单位", notes = "批量关联法定单位")
    @PostMapping("/related/batch")
    Boolean batchRelatedLegalUnits(@RequestBody @Validated RelatedLegalBatchF relatedLegalBatchF){
        return subjectSystemAppService.relatedLegalUnitsBatch(relatedLegalBatchF);
    }

    @ApiOperation(value = "关联法定单位", notes = "关联法定单位")
    @PostMapping("/related")
    Boolean relatedLegalUnits(@RequestBody @Validated RelatedLegalF relatedLegalF){
        return subjectSystemAppService.relatedLegalUnits(relatedLegalF);
    }

    @ApiOperation(value = "根据关联id撤销关联法定单位", notes = "根据关联id撤销关联法定单位")
    @GetMapping("/disassociate/{relatedId}")
    Boolean disassociateLegalUnits(@PathVariable("relatedId") Long relatedId){
        return subjectSystemAppService.disassociateLegalUnits(relatedId);
    }

    @ApiOperation(value = "分页获取关联法定单位", notes = "分页获取关联法定单位")
    @PostMapping("/query/legalUnit/page")
    PageV<SubjectSystemRelatedV> queryLegalUnitsPage(@RequestBody @Validated PageF<SearchF<?>> queryPageF){
        return subjectSystemAppService.queryLegalUnitsPage(queryPageF);
    }

    @ApiOperation(value = "分页获取科目体系", notes = "分页获取科目体系")
    @PostMapping("/query/page")
    PageV<SubjectSystemSimpleV> queryPage(@RequestBody @Validated PageF<SearchF<SubjectSystemE>> queryPageF){
        return subjectSystemAppService.queryPage(queryPageF);
    }


}
