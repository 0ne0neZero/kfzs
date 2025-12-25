package com.wishare.finance.domains.configure.subject.repository;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.apps.model.configure.subject.fo.ListSubjectMapRulesF;
import com.wishare.finance.domains.configure.subject.entity.SubjectMapRulesE;
import com.wishare.finance.domains.configure.subject.repository.mapper.SubjectMapRulesMapper;
import com.wishare.finance.infrastructure.conts.DataDeletedEnum;
import com.wishare.finance.infrastructure.conts.DataDisabledEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author xujian
 * @date 2022/12/19
 * @Description:
 */
@Repository
public class SubjectMapRulesRepository extends ServiceImpl<SubjectMapRulesMapper, SubjectMapRulesE> {

    @Autowired
    private SubjectMapRulesMapper subjectMapRulesMapper;

    /**
     * 获取科目映射规则列表
     *
     * @param form
     * @return
     */
    public List<SubjectMapRulesE> listSubjectMapRules(ListSubjectMapRulesF form) {
        LambdaUpdateWrapper<SubjectMapRulesE> wrapper = new LambdaUpdateWrapper<>();
        wrapper.like(StringUtils.isNotBlank(form.getSubMapName()), SubjectMapRulesE::getSubMapName, form.getSubMapName());
        return subjectMapRulesMapper.selectList(wrapper);
    }

    /**
     * 根据科目映射规则名称精确查询
     *
     * @param subMapName
     * @return
     */
    public SubjectMapRulesE getBySubMapName(String subMapName) {
        LambdaUpdateWrapper<SubjectMapRulesE> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(SubjectMapRulesE::getSubMapName, subMapName);
        return subjectMapRulesMapper.selectOne(wrapper);
    }

    /**
     * 根据科目体系、映射单元、映射单元属性（费项属性/辅助核算项目）查询规则
     *
     * @param subSysId
     * @param subMapType
     * @param chargeItemAttribute
     * @return
     */
    public SubjectMapRulesE getBySysAndUnit(Long subSysId, Integer subMapType, Integer chargeItemAttribute) {
        LambdaUpdateWrapper<SubjectMapRulesE> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(SubjectMapRulesE::getSubSysId, subSysId);
        wrapper.eq(SubjectMapRulesE::getSubMapType, subMapType);
        wrapper.eq(SubjectMapRulesE::getChargeItemAttribute, chargeItemAttribute);
        return subjectMapRulesMapper.selectOne(wrapper);
    }
    //根据费项属性获取对应科目映射费项数据
    public SubjectMapRulesE getSubMapRulesByAttribute(Integer chargeItemAttribute) {
        LambdaUpdateWrapper<SubjectMapRulesE> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(SubjectMapRulesE::getDeleted, DataDeletedEnum.NORMAL.getCode());
        wrapper.eq(SubjectMapRulesE::getDisabled, DataDisabledEnum.启用.getCode());
        wrapper.eq(SubjectMapRulesE::getChargeItemAttribute, chargeItemAttribute);
        return subjectMapRulesMapper.selectOne(wrapper);
    }
}
