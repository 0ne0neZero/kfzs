package com.wishare.finance.apps.service.configure.subject;

import com.google.common.collect.Lists;
import com.wishare.finance.apps.model.configure.subject.fo.*;
import com.wishare.finance.apps.model.configure.subject.vo.SubjectMapRulesV;
import com.wishare.finance.apps.model.configure.subject.vo.SubjectMapUnitDetailPageV;
import com.wishare.finance.apps.model.configure.subject.vo.SubjectMapUnitDetailV;
import com.wishare.finance.domains.configure.subject.command.subject.AddSubjectMapRulesCommand;
import com.wishare.finance.domains.configure.subject.command.subject.BatchSettingCommand;
import com.wishare.finance.domains.configure.subject.command.subject.UpdateSubjectMapRulesCommand;
import com.wishare.finance.domains.configure.subject.entity.SubjectMapRulesE;
import com.wishare.finance.domains.configure.subject.service.SubjectMapRulesDomainService;
import com.wishare.starter.Global;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author xujian
 * @date 2022/12/19
 * @Description:
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SubjectMapRulesAppService {

    private final SubjectMapRulesDomainService subjectMapRulesDomainService;

    /**
     * 新增科目映射规则
     *
     * @param form
     * @return
     */
    public Long addSubjectMapRules(AddSubjectMapRulesF form) {
        return subjectMapRulesDomainService.addSubjectMapRules(Global.mapperFacade.map(form, AddSubjectMapRulesCommand.class));
    }

    /**
     * 修改科目映射规则
     *
     * @param form
     * @return
     */
    public Long updateSubjectMapRules(UpdateSubjectMapRulesF form) {
        return subjectMapRulesDomainService.updateSubjectMapRules(Global.mapperFacade.map(form, UpdateSubjectMapRulesCommand.class));
    }

    /**
     * 删除科目映射规则
     *
     * @param id
     * @return
     */
    public Boolean deleteSubjectMapRules(Long id) {
        return subjectMapRulesDomainService.deleteSubjectMapRules(id);
    }

    /**
     * 获取科目映射规则详情
     *
     * @param id
     * @return
     */
    public SubjectMapRulesV detailSubjectMapRules(Long id) {
        SubjectMapRulesE subjectMapRulesE = subjectMapRulesDomainService.detailSubjectMapRules(id);
        return new SubjectMapRulesV().general(subjectMapRulesE);
    }

    /**
     * 获取科目映射规则列表
     *
     * @param form
     * @return
     */
    public List<SubjectMapRulesV> listSubjectMapRules(ListSubjectMapRulesF form) {
        List<SubjectMapRulesE> subjectMapRulesEList = subjectMapRulesDomainService.listSubjectMapRules(form);
        List<SubjectMapRulesV> subjectMapRulesVList = Lists.newArrayList();
        subjectMapRulesEList.forEach(subjectMapRulesE -> {
            SubjectMapRulesV subjectMapRulesV = new SubjectMapRulesV().general(subjectMapRulesE);
            subjectMapRulesVList.add(subjectMapRulesV);
        });
        return subjectMapRulesVList;
    }

    /**
     * 映射单元批量设置
     *
     * @param form
     * @return
     */
    public Boolean batchSetting(List<BatchSettingF> form) {
        return subjectMapRulesDomainService.batchSetting(Global.mapperFacade.mapAsList(form, BatchSettingCommand.class));
    }

    /**
     * 科目映射单元批量设置
     *
     * @param form
     * @return
     */
    @Transactional
    public Boolean setting(SubMapRulesSettingF form) {
        List<Long> subMapUnitIds = form.getSubMapUnitIds();
        for (Long subMapUnitId : subMapUnitIds) {
            List<BatchSettingF> batchSettingFList = form.getBatchSettingFList();
            List<BatchSettingCommand> commands = Global.mapperFacade.mapAsList(batchSettingFList, BatchSettingCommand.class);
            for (BatchSettingCommand command : commands) {
                command.setSubMapUnitId(subMapUnitId);
            }
            subjectMapRulesDomainService.batchSetting(commands);
        }
        return true;
    }
    /**
     * 映射单元分页查询
     *
     * @param form
     * @return
     */
    public PageV<SubjectMapUnitDetailPageV> batchSettingPage(PageF<SearchF<BatchSettingPageF>> form) {
        return subjectMapRulesDomainService.batchSettingPage(form);
    }


    /**
     * 科目映射启用禁用
     *
     * @param id
     * @param disableState
     * @return
     */
    public Boolean enable(Long id, Integer disableState) {
        return subjectMapRulesDomainService.enable(id,disableState);
    }

    /**
     * 查询科目映射详情信息
     *
     * @param subjectMapRuleId
     * @return
     */
    public List<SubjectMapUnitDetailV> queryDetail(Long subjectMapRuleId, Long subMapUnitId) {
        return subjectMapRulesDomainService.queryDetail(subjectMapRuleId,subMapUnitId);
    }
    public SubjectLevelJson getByUnitId(Long unitId) {
        SubjectLevelJson byUnitId = subjectMapRulesDomainService.getByUnitId(unitId);
        return byUnitId;
    }

}
