package com.wishare.finance.apis.configure.subject;

import com.wishare.finance.apps.model.configure.subject.fo.CreateSubjectCategoryF;
import com.wishare.finance.apps.model.configure.subject.fo.UpdateSubjectCategoryF;
import com.wishare.finance.apps.model.configure.subject.vo.SubjectCategorySimpleV;
import com.wishare.finance.apps.model.configure.subject.vo.SubjectCategoryTreeV;
import com.wishare.finance.apps.model.configure.subject.vo.SubjectCategoryV;
import com.wishare.finance.apps.service.configure.subject.SubjectCategoryAppService;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 科目类型控制器
 *
 * @author yancao
 */
@Api(tags = {"科目类型接口"})
@RestController
@RequestMapping("/subject/category")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SubjectCategoryApi {

    private final SubjectCategoryAppService subjectCategoryAppService;

    @ApiOperation(value = "根据体系id查询科目类型树", notes = "根据体系id查询科目类型树")
    @GetMapping("/query/pertainId/{id}")
    public List<SubjectCategoryTreeV> getTreeByPertainId(@PathVariable("id") @ApiParam("科目体系id") Long id) {
        return subjectCategoryAppService.getTreeByPertainId(id);
    }

    @ApiOperation(value = "根据体系id集合查询所有科目类型", notes = "根据体系id集合查询所有科目类型")
    @PostMapping("/query/pertainId")
    public List<SubjectCategoryV> getByPertainId(@RequestBody List<Long> idList) {
        return subjectCategoryAppService.getAllByPertainId(idList);
    }

    @ApiOperation("创建科目类型")
    @PostMapping("/create")
    Long create(@RequestBody @Validated CreateSubjectCategoryF createSubjectCategoryF) {
        return subjectCategoryAppService.create(createSubjectCategoryF);
    }

    @ApiOperation("更新科目类型")
    @PostMapping("/update")
    Boolean update(@RequestBody @Validated UpdateSubjectCategoryF updateSubjectCategoryF) {
        return subjectCategoryAppService.update(updateSubjectCategoryF);
    }

    @ApiOperation("根据类型id删除科目类型")
    @DeleteMapping("/delete/id/{id}")
    Boolean deleteById(@PathVariable("id") Long id) {
        return subjectCategoryAppService.delete(id);
    }

    @ApiOperation("根据id查询科目类型详情")
    @GetMapping("/query/id/{id}")
    SubjectCategoryV queryById(@PathVariable("id") Long id) {
        return subjectCategoryAppService.queryById(id);
    }

    @ApiOperation("根据科目类型id和科目体系id查询子科目类型列表")
    @GetMapping("/query/child/id/{id}")
    List<SubjectCategoryV> queryChildById(@PathVariable("id") Long id) {
        return subjectCategoryAppService.queryChildById(id);
    }

    @ApiOperation(value = "获取默认科目类型", notes = "根据体系id查询科目类型树")
    @GetMapping("/query/defaultCategory")
    public List<SubjectCategorySimpleV> queryDefaultCategory() {
        return subjectCategoryAppService.queryDefaultCategory();
    }

}
