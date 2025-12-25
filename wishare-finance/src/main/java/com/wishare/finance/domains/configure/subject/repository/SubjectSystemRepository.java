package com.wishare.finance.domains.configure.subject.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.domains.configure.subject.entity.SubjectSystemE;
import com.wishare.finance.domains.configure.subject.repository.mapper.SubjectSystemMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 财务科目体系表 Mapper 接口
 *
 * @author yancao
 * @since 2022-06-01
 */
@Repository
public class SubjectSystemRepository extends ServiceImpl<SubjectSystemMapper, SubjectSystemE> {

    /**
     * 根据体系编码获取科目体系
     *
     * @param currentId currentId
     * @param code      code
     * @param tenantId  tenantId
     * @return SubjectSystemE
     */
    public SubjectSystemE getSubjectSystemByCode(Long currentId, String code, String tenantId) {
        LambdaQueryWrapper<SubjectSystemE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SubjectSystemE::getCode, code);
        queryWrapper.ne(currentId != null, SubjectSystemE::getId, currentId);
        queryWrapper.eq(StringUtils.isNotEmpty(tenantId), SubjectSystemE::getTenantId, tenantId);
        queryWrapper.last("limit 1");
        return this.getOne(queryWrapper);
    }

    /**
     * 根据法定单位id分页获取关联的科目体系
     *
     * @param page page
     * @param statutoryBodyId 法定单位id
     * @return Page
     */
    public Page<SubjectSystemE> queryPageByStatutoryBodyId(Page<Object> page, Long statutoryBodyId) {
        return baseMapper.queryPageByStatutoryBodyId(page, statutoryBodyId);
    }

    /**
     * 根据名称模糊查询科目体系
     *
     * @param name 科目体系名称
     * @return List
     */
    public List<SubjectSystemE> querySubjectSystemByName(String name) {
        LambdaQueryWrapper<SubjectSystemE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(name), SubjectSystemE::getName, name);
        return list(queryWrapper);
    }

    /**
     * 根据编码获取科目体系
     * @param code 编码
     * @return 科目体系
     */
    public SubjectSystemE getByCode(String code) {
        return getOne(new LambdaUpdateWrapper<SubjectSystemE>().eq(SubjectSystemE::getCode, code));
    }
}
