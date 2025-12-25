package com.wishare.finance.domains.configure.subject.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.domains.configure.subject.entity.SubjectCashFlow;
import com.wishare.finance.domains.configure.subject.repository.mapper.SubjectCashFlowMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 现金流量资源库
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/3/11
 */
@Service
public class SubjectCashFlowRepository extends ServiceImpl<SubjectCashFlowMapper, SubjectCashFlow> {

    /**
     * 根据科目id列表获取现金流量信息列表
     * @param subjectIds 科目id列表
     * @return 现金流量信息列表
     */
    public List<SubjectCashFlow> listBySubjectIds(List<Long> subjectIds) {
        return list(new LambdaUpdateWrapper<SubjectCashFlow>().in(SubjectCashFlow::getSubjectId, subjectIds));
    }

    /**
     * 根据映射信息获取科目现金流量信息
     * @param subjectId 科目id
     * @param mapUnitId 映射单元id
     * @param mapType 映射类别： 1科目，2现金流量
     * @return 科目现金流量信息
     */
    public List<SubjectCashFlow> listByMapDetail(Long subjectId, Long mapUnitId, Integer mapType){
        return baseMapper.selectByMapDetail(subjectId, mapUnitId, mapType);
    }

    /**
     * 根据科目id获取科目现金流量信息
     * @param subjectId 科目id
     * @return 科目现金流量信息
     */
    public List<SubjectCashFlow> listBySubjectId(Long subjectId){
        return list(new LambdaQueryWrapper<SubjectCashFlow>().eq(SubjectCashFlow::getSubjectId, subjectId));
    }

    /**
     * 根据科目编号删除现金流量
     * @param subjectId 科目id
     * @return
     */
    public boolean deleteBySubjectId(Long subjectId){
        return remove(new LambdaQueryWrapper<SubjectCashFlow>().eq(SubjectCashFlow::getSubjectId, subjectId));
    }

}
