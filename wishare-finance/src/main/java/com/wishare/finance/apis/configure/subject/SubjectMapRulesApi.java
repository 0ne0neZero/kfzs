package com.wishare.finance.apis.configure.subject;

import com.wishare.finance.apps.model.configure.subject.fo.*;
import com.wishare.finance.apps.model.configure.subject.vo.SubjectMapRulesV;
import com.wishare.finance.apps.model.configure.subject.vo.SubjectMapUnitDetailPageV;
import com.wishare.finance.apps.model.configure.subject.vo.SubjectMapUnitDetailV;
import com.wishare.finance.apps.service.configure.subject.SubjectMapRulesAppService;
import com.wishare.finance.infrastructure.conts.DataDisabledEnum;
import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.exception.BizException;
import com.wishare.tools.starter.fo.search.SearchF;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author xujian
 * @date 2022/12/19
 * @Description:
 */
@Api(tags = {"科目映射规则接口"})
@RestController
@RequestMapping("/subjectMapRules")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SubjectMapRulesApi {

    private final SubjectMapRulesAppService subjectMapRulesAppService;

    @ApiOperation(value = "新增科目映射规则", notes = "新增科目映射规则")
    @PostMapping("/add")
    public Long addSubjectMapRules(@RequestBody @Validated AddSubjectMapRulesF form) {
        form.check();
        return subjectMapRulesAppService.addSubjectMapRules(form);
    }

    @ApiOperation(value = "修改科目映射规则", notes = "修改科目映射规则")
    @PostMapping("/update")
    public Long updateSubjectMapRules(@RequestBody @Validated UpdateSubjectMapRulesF form) {
        form.check();
        return subjectMapRulesAppService.updateSubjectMapRules(form);
    }

    @ApiOperation(value = "删除科目映射规则", notes = "删除科目映射规则")
    @DeleteMapping("/delete/{id}")
    public Boolean deleteSubjectMapRules(@PathVariable("id") Long id) {
        return subjectMapRulesAppService.deleteSubjectMapRules(id);
    }

    @ApiOperation(value = "根据id获取科目映射规则详情", notes = "根据id获取科目映射规则详情")
    @GetMapping("/detail/{id}")
    public SubjectMapRulesV detailSubjectMapRules(@PathVariable("id") Long id) {
        return subjectMapRulesAppService.detailSubjectMapRules(id);
    }

    @ApiOperation(value = "根据费项id获取科目映射规则详情", notes = "根据费项id获取科目映射规则详情")
    @GetMapping("/detail/item/{id}")
    public SubjectLevelJson detailByChargeItem(@PathVariable("id") Long id) {
        return subjectMapRulesAppService.getByUnitId(id);
    }


    @ApiOperation(value = "获取科目映射规则列表", notes = "获取科目映射规则列表")
    @PostMapping("/list")
    public List<SubjectMapRulesV> listSubjectMapRules(@RequestBody @Validated ListSubjectMapRulesF form) {
        return subjectMapRulesAppService.listSubjectMapRules(form);
    }

    @ApiOperation(value = "映射单元批量设置", notes = "映射单元批量设置")
    @PostMapping("/batchSetting")
    public Boolean batchSetting(@RequestBody @Validated List<BatchSettingF> form) {
        return subjectMapRulesAppService.batchSetting(form);
    }

    @ApiOperation(value = "科目映射单元批量设置", notes = "科目映射单元批量设置")
    @PostMapping("/setting")
    public Boolean setting(@RequestBody @Validated SubMapRulesSettingF form) {
        return subjectMapRulesAppService.setting(form);
    }

    @ApiOperation(value = "映射单元批量设置分页查询", notes = "映射单元批量设置分页查询")
    @PostMapping("/batchSetting/page")
    public PageV<SubjectMapUnitDetailPageV> batchSettingPage(@RequestBody @Validated PageF<SearchF<BatchSettingPageF>> form) {
        return subjectMapRulesAppService.batchSettingPage(form);
    }

    @ApiOperation(value = "根据id启用或禁用科目映射", notes = "根据id启用或禁用科目映射")
    @PostMapping("/enable/{id}/{disableState}")
    public Boolean enable(@PathVariable("id") Long id, @PathVariable("disableState") Integer disableState) {
        if (null == disableState || null == DataDisabledEnum.valueOfByCode(disableState)) {
            throw BizException.throw400(ErrorMessage.DISABLE_STATE_EXCEPTION.getErrMsg());
        }
        return subjectMapRulesAppService.enable(id, disableState);
    }


    @ApiOperation(value = "映射单元详情查询", notes = "映射单元批量设置分页查询")
    @GetMapping("/subjectMapUnitDetailInfo")
    public List<SubjectMapUnitDetailV> queryDetail(@RequestParam(value = "subjectMapRuleId",required = false)@ApiParam("科目映射规则id") Long subjectMapRuleId,
                                                   @RequestParam(value = "subMapUnitId", required = false)@ApiParam("映射单元id") Long subMapUnitId) {
        return subjectMapRulesAppService.queryDetail(subjectMapRuleId,subMapUnitId);
    }
}
