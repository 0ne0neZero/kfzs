package com.wishare.finance.domains.configure.subject.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.domains.configure.subject.entity.SubjectStatutoryRelationE;
import com.wishare.finance.domains.configure.subject.repository.mapper.SubjectStatutoryRelationMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 科目体系法定单位关联表 Mapper 接口
 *
 * @author yancao
 * @since 2022-06-01
 */
@Repository
public class SubjectStatutoryRelationRepository extends ServiceImpl<SubjectStatutoryRelationMapper, SubjectStatutoryRelationE> {

    /**
     * 根据科目体系和法定单位id获取关联信息
     *
     * @param subjectSystemId 科目体系id
     * @param statutoryBodyIdList 法定单位id
     * @return List
     */
    public List<SubjectStatutoryRelationE> queryByStatutoryBodyIdAndSubjectSystemId(Long subjectSystemId, List<Long> statutoryBodyIdList) {
        LambdaQueryWrapper<SubjectStatutoryRelationE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SubjectStatutoryRelationE::getSubjectSystemId, subjectSystemId);
        queryWrapper.in(SubjectStatutoryRelationE::getStatutoryBodyId, statutoryBodyIdList);
        return list(queryWrapper);
    }

    /**
     * 根据体系id获取已关联的法定单位
     *
     * @param subjectSystemId 科目体系id
     * @return List
     */
    public List<SubjectStatutoryRelationE> queryBySubjectSystemId(Long subjectSystemId) {
        LambdaQueryWrapper<SubjectStatutoryRelationE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SubjectStatutoryRelationE::getSubjectSystemId, subjectSystemId);
        return list(queryWrapper);
    }

    /**
     * 根据法定单位id获取关联信息
     *
     * @param statutoryBodyId 法定单位id
     * @return List
     */
    public List<SubjectStatutoryRelationE> queryByStatutoryBodyId(Long statutoryBodyId) {
        LambdaQueryWrapper<SubjectStatutoryRelationE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SubjectStatutoryRelationE::getStatutoryBodyId, statutoryBodyId);
        return list(queryWrapper);
    }
}
