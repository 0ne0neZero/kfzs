package com.wishare.finance.domains.configure.subject.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wishare.finance.domains.configure.subject.entity.SubjectCategoryE;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 科目类型 Mapper 接口
 *
 * @author yancao
 * @since 2022-06-01
 */
public interface SubjectCategoryMapper extends BaseMapper<SubjectCategoryE> {

    /**
     * 查询子节点
     *
     * @param idList idList
     * @return List
     */
    List<SubjectCategoryE> queryChildSubjectCategoryById(@Param("idList") List<Long> idList);

    /**
     * 获取所有科目类型数量（忽略逻辑删除）
     * @return Long
     */
    Long countAll(@Param("parentId") Long parentId);
}
