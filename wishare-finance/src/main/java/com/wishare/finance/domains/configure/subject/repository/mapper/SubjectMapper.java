package com.wishare.finance.domains.configure.subject.repository.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.domains.configure.subject.dto.SubjectD;
import com.wishare.finance.domains.configure.subject.entity.SubjectE;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 财务科目表 Mapper 接口
 *
 * @author yancao
 * @since 2022-06-01
 */
@Repository
public interface SubjectMapper extends BaseMapper<SubjectE> {

    /**
     * 根据科目id获取下级所有节点
     *
     * @param idList idList
     * @return List
     */
    List<SubjectE> querySubjectByIdList(@Param("idList") List<Long> idList);

    /**
     * 分页查询科目
     *
     * @param page         page
     * @param queryWrapper queryWrapper
     * @return Page
     */
    Page<SubjectD> querySubjectByPage(Page<SubjectE> page, @Param(Constants.WRAPPER) Wrapper<SubjectE> queryWrapper);

    /**
     * 获取子科目（同时获取科目类型）
     *
     * @param idList idList
     * @return List
     */
    List<SubjectD> querySubjectWithCategoryNameByIdList(@Param("idList") List<Long> idList, @Param(Constants.WRAPPER) Wrapper<SubjectE> queryWrapper);

    /**
     * 获取过滤的科目以及子科目
     *
     * @param filterId filterId
     * @param tenantId tenantId
     * @return List
     */
    List<SubjectE> getFilterSubjectList(@Param("filterId") Long filterId, @Param("tenantId") String tenantId);

    /**
     *
     * 根据费项id查科目数据
     * @param headSubjectId 一级科目id
     * @param chargeItemId 费项id
     * @return
     */
    SubjectE getSubjectByChargeItemIdAndHeadSubjectId(@Param("headSubjectId") Long headSubjectId, @Param("chargeItemId") Long chargeItemId);

    /**
     * 根据编码获取上级科目
     * @param code 科目编码
     * @return 上级科目
     */
    SubjectE selectSupSubjectByCode(@Param("code") String code);

    /**
     * 更新子科目的现金类型
     * @param path 父级路径
     * @param cashType 现金类型
     * @return 更新的数量
     */
    int updateChildrenCashType(@Param("paths") List<Long> path, @Param("cashType") Integer cashType);

    /**
     * 根据科目编码查询科目全部信息（包含全部路径名称）
     * @param subjectCodes 科目编码
     * @return 科目信息
     */
    List<SubjectE> selectListFullByCodes(@Param("subjectCodes") List<String> subjectCodes);

    /**
     * 查询科目
     * @param subjectId
     * @return
     */
    SubjectE getSubject(@Param("subjectId") Long subjectId);

    List<String> getAllAuxiliary(@Param("subjectId") Long subjectId);
}
