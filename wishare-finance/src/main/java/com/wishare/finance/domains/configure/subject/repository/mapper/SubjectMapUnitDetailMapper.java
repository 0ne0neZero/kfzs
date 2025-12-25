package com.wishare.finance.domains.configure.subject.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wishare.finance.domains.configure.subject.entity.SubjectE;
import com.wishare.finance.domains.configure.subject.entity.SubjectMapUnitDetailE;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author xujian
 * @date 2022/12/20
 * @Description:
 */
public interface SubjectMapUnitDetailMapper extends BaseMapper<SubjectMapUnitDetailE> {

    /**
     * 根据科目和映射科目信息获取映射科目信息
     * @param subjectId 科目id
     * @param subMapUnitId 映射单元id
     * @param subMapType 映射类型
     * @return 映射科目
     */
    SubjectE selectSubject(@Param("subjectId") Long subjectId, @Param("subMapUnitId") Long subMapUnitId, @Param("subMapType") int subMapType);

    //根据条件获取对应科目ID
    List<String> getSubMapUnitDetIdList(@Param(value = "tenantId") String tenantId,
                               @Param(value = "subMapRuleId")Long subMapRuleId,
                               @Param(value = "subMapUnitIdList") List<String> subMapUnitIdList,
                               @Param(value = "subjectLevelOneName") String subjectLevelOneName);
}
