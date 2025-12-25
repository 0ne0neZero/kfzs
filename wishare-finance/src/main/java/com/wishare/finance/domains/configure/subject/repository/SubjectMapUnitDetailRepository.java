package com.wishare.finance.domains.configure.subject.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.domains.configure.subject.consts.enums.SubMapTypeEnum;
import com.wishare.finance.domains.configure.subject.consts.enums.SubjectRuleMapTypeEnum;
import com.wishare.finance.domains.configure.subject.entity.SubjectE;
import com.wishare.finance.domains.configure.subject.entity.SubjectMapUnitDetailE;
import com.wishare.finance.domains.configure.subject.repository.mapper.SubjectMapUnitDetailMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * @author xujian
 * @date 2022/12/19
 * @Description:
 */
@Repository
public class SubjectMapUnitDetailRepository extends ServiceImpl<SubjectMapUnitDetailMapper, SubjectMapUnitDetailE> {

    @Autowired
    private SubjectMapUnitDetailMapper subjectMapUnitDetailMapper;

    /**
     * 根据科目映射规则获取映射单元详情
     *
     * @param subjectMapRuleId
     * @param chargeItemIds
     * @return
     */
    public List<SubjectMapUnitDetailE> getBySubjectMapRuleId(Long subjectMapRuleId, List<Long> chargeItemIds) {
        LambdaUpdateWrapper<SubjectMapUnitDetailE> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(SubjectMapUnitDetailE::getSubMapRuleId, subjectMapRuleId);
        wrapper.in(CollectionUtils.isNotEmpty(chargeItemIds),SubjectMapUnitDetailE::getSubMapUnitId, chargeItemIds);
        return subjectMapUnitDetailMapper.selectList(wrapper);
    }

    /**
     * 根据费项id获取映射单元详情
     *
     * @param chargeItemId
     * @return
     */
    public List<SubjectMapUnitDetailE> getChargeItemIdBySubjectMapRuleId(Long chargeItemId) {
        return subjectMapUnitDetailMapper.selectList(new LambdaQueryWrapper<SubjectMapUnitDetailE>()
                .eq(SubjectMapUnitDetailE::getSubMapUnitId,chargeItemId)
                .eq(SubjectMapUnitDetailE::getDeleted,0));
    }

    /**
     * 根据规则id和一级科目id查找
     *
     * @param subjectMapRuleId
     * @param subjectLevelOneId
     * @return
     */
    public SubjectMapUnitDetailE getSubjectMapUnit(Long subjectMapRuleId,Long subMapUnitId, Long subjectLevelOneId) {
        LambdaUpdateWrapper<SubjectMapUnitDetailE> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(SubjectMapUnitDetailE::getSubMapRuleId, subjectMapRuleId);
        wrapper.eq(SubjectMapUnitDetailE::getSubjectLevelOneId, subjectLevelOneId);
        wrapper.eq(SubjectMapUnitDetailE::getSubMapUnitId, subMapUnitId);
        return subjectMapUnitDetailMapper.selectOne(wrapper);
    }

    public List<SubjectMapUnitDetailE> getByUnitIds(List<Long> unitIds, int subMapType) {
        return list(new LambdaUpdateWrapper<SubjectMapUnitDetailE>()
                .in(SubjectMapUnitDetailE::getSubMapUnitId, unitIds)
                .eq(SubjectMapUnitDetailE::getSubMapType, subMapType));
    }

    /**
     * 获取映射详情列表
     * @param unitIds 映射单元id雷彪
     * @param firstSubjectId 以及科目id
     * @param subMapType 映射类型 1 费项 2 辅助核算
     * @return 映射详情列表
     */
    public SubjectMapUnitDetailE getMapDetail(List<Long> unitIds, String firstSubjectId, int subMapType) {
        return getOne(new LambdaUpdateWrapper<SubjectMapUnitDetailE>().in(SubjectMapUnitDetailE::getSubMapUnitId, unitIds)
                .eq(SubjectMapUnitDetailE::getSubMapType, subMapType)
                .eq(SubjectMapUnitDetailE::getSubjectLevelOneId, firstSubjectId), false);
    }

    /**
     * 根据科目和映射科目信息获取映射科目信息
     * @param subjectId 科目id
     * @param subMapUnitId 映射单元id
     * @param subMapType 映射单元类型（1 费项 2 辅助核算）
     * @return 映射科目
     */
    public SubjectE getSubject(Long subjectId, Long subMapUnitId, int subMapType){
        return baseMapper.selectSubject(subjectId, subMapUnitId, subMapType);
    }

    /**
     * 获取映射详情
     * @param unitId 映射单元信息（费项）
     * @param mapType 映射类别： 1科目，2现金流量
     * @return 映射详情
     */
    public SubjectMapUnitDetailE getByUnitId(Long unitId, int mapType){
        return getOne(new LambdaUpdateWrapper<SubjectMapUnitDetailE>().eq(SubjectMapUnitDetailE::getSubMapUnitId, unitId)
                .eq(SubjectMapUnitDetailE::getMapType,mapType), false);
    }
    public SubjectMapUnitDetailE getByUnitId(Long unitId){
        LambdaUpdateWrapper<SubjectMapUnitDetailE> subjectMapUnitDetailELambdaUpdateWrapper = new LambdaUpdateWrapper<SubjectMapUnitDetailE>()
                .eq(SubjectMapUnitDetailE::getSubMapUnitId, unitId)
                .eq(SubjectMapUnitDetailE::getSubMapType, SubMapTypeEnum.费项.getCode())
                .eq(SubjectMapUnitDetailE::getMapType, SubjectRuleMapTypeEnum.现金流量.getCode());
        return getOne(subjectMapUnitDetailELambdaUpdateWrapper, false);
    }

    //根据体系ID及属性获取所有单元ID
    public List<String> getSubMapUnitDetIdList(String tenantId, Long subMapRuleId, List<String> subMapUnitIdList, String subjectLevelOneName) {
        return baseMapper.getSubMapUnitDetIdList(tenantId, subMapRuleId, subMapUnitIdList, subjectLevelOneName);
    }

    /**
     * 查询对应费项的 代收代付属性费项映射 其他应付款是否配置
     *
     * @param subjectMapRuleId
     * @param chargeItemIds
     * @return
     */
    public Long countSubjectLevelLastId(Long subjectMapRuleId, Long subjectLevelOneId, Collection<Long> chargeItemIds) {
        LambdaUpdateWrapper<SubjectMapUnitDetailE> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(SubjectMapUnitDetailE::getSubMapRuleId, subjectMapRuleId);
        wrapper.eq(SubjectMapUnitDetailE::getSubjectLevelOneId, subjectLevelOneId);
        wrapper.in(CollectionUtils.isNotEmpty(chargeItemIds), SubjectMapUnitDetailE::getSubMapUnitId, chargeItemIds);
        wrapper.eq(SubjectMapUnitDetailE::getDeleted, 0);
        wrapper.isNotNull(SubjectMapUnitDetailE::getSubjectLevelLastId);
        return subjectMapUnitDetailMapper.selectCount(wrapper);
    }
}
