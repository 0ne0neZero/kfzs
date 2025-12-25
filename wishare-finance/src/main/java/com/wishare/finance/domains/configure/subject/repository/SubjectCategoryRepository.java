package com.wishare.finance.domains.configure.subject.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.domains.configure.subject.entity.SubjectCategoryE;
import com.wishare.finance.domains.configure.subject.repository.mapper.SubjectCategoryMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 科目类型 Mapper 接口
 *
 * @author yancao
 * @since 2022-06-01
 */
@Repository
public class SubjectCategoryRepository extends ServiceImpl<SubjectCategoryMapper, SubjectCategoryE> {

    /**
     * 查询子节点
     *
     * @param idList idList
     * @return List
     */
    public List<SubjectCategoryE> queryChildSubjectCategoryById(List<Long> idList) {
        return baseMapper.queryChildSubjectCategoryById(idList);
    }

    /**
     * 获取所有科目类型数量（忽略逻辑删除）
     *
     * @return Long
     */
    public Long countAll(Long parentId) {
        return baseMapper.countAll(parentId);
    }

    /**
     * 根据体系id获取科目类型
     *
     * @param pertainIdList 体系id
     * @param tenantId      租户id
     * @return List
     */
    public List<SubjectCategoryE> getSubjectCategoryByPertainId(List<Long> pertainIdList, String tenantId) {
        LambdaQueryWrapper<SubjectCategoryE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(SubjectCategoryE::getId, SubjectCategoryE::getPertainId, SubjectCategoryE::getParentId, SubjectCategoryE::getCategoryName);
        queryWrapper.in(!CollectionUtils.isEmpty(pertainIdList), SubjectCategoryE::getPertainId, pertainIdList);
        queryWrapper.eq(StringUtils.isNotEmpty(tenantId), SubjectCategoryE::getTenantId, tenantId);
        return this.list(queryWrapper);
    }

    /**
     * 根据parentId获取科目类型
     *
     * @param parentId  父科目类型id
     * @param pertainId 体系id
     * @param tenantId  租户id
     * @return List
     */
    public List<SubjectCategoryE> getSubjectCategoryByParentId(Long parentId, Long pertainId, String tenantId) {
        LambdaQueryWrapper<SubjectCategoryE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SubjectCategoryE::getParentId, parentId);
        queryWrapper.eq(pertainId != null, SubjectCategoryE::getPertainId, pertainId);
        queryWrapper.eq(StringUtils.isNotEmpty(tenantId), SubjectCategoryE::getTenantId, tenantId);
        return this.list(queryWrapper);
    }

    /**
     * 根据父科目类型id获取子科目类型数量
     *
     * @param parentId 父科目id
     * @param tenantId 租户id
     * @return long
     */
    public long getSubCountWithCurrentSubjectCategory(Long parentId, String tenantId) {
        LambdaQueryWrapper<SubjectCategoryE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SubjectCategoryE::getParentId, parentId);
        queryWrapper.eq(StringUtils.isNotEmpty(tenantId), SubjectCategoryE::getTenantId, tenantId);
        return this.count(queryWrapper);
    }

    /**
     * 根据体系id删除科目类型
     *
     * @param pertainId 体系id
     */
    public void removeByPertainId(Long pertainId) {
        LambdaQueryWrapper<SubjectCategoryE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SubjectCategoryE::getPertainId, pertainId);
        this.remove(queryWrapper);
    }
}
