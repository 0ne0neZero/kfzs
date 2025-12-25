package com.wishare.finance.domains.configure.subject.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wishare.finance.domains.configure.subject.entity.SubjectCashFlow;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 科目-现金流量关系表 Mapper 接口
 * </p>
 *
 * @author dxclay
 * @since 2023-03-11
 */
@Mapper
public interface SubjectCashFlowMapper extends BaseMapper<SubjectCashFlow> {


    /**
     * 根据科目映射信息获取现金流量关系
     * @param subjectId 科目id
     * @param mapUnitId 映射单元id
     * @param mapType 映射类别： 1科目，2现金流量
     * @return
     */
    List<SubjectCashFlow> selectByMapDetail(@Param("subjectId") Long subjectId, @Param("mapUnitId") Long mapUnitId, @Param("mapType") Integer mapType);
}
