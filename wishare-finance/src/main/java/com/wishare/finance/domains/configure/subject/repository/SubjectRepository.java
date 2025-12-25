package com.wishare.finance.domains.configure.subject.repository;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.domains.configure.subject.consts.enums.SubjectLeafStatusEnum;
import com.wishare.finance.domains.configure.subject.dto.SubjectD;
import com.wishare.finance.domains.configure.subject.entity.SubjectE;
import com.wishare.finance.domains.configure.subject.repository.mapper.SubjectMapper;
import com.wishare.finance.infrastructure.conts.DataDisabledEnum;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

/**
 * 财务科目表 Mapper 接口
 *
 * @author yancao
 * @since 2022-06-01
 */
@Repository
public class SubjectRepository extends ServiceImpl<SubjectMapper, SubjectE> {

    /**
     * 根据科目id获取下级所有节点
     *
     * @param idList idList
     * @return List
     */
    public List<SubjectE> querySubjectByIdList(List<Long> idList) {
        return baseMapper.querySubjectByIdList(idList);
    }

    /**
     * 分页查询科目
     *
     * @param page         page
     * @param queryWrapper queryWrapper
     * @return Page
     */
    public Page<SubjectD> querySubjectByPage(Page<SubjectE> page, Wrapper<SubjectE> queryWrapper) {
        return baseMapper.querySubjectByPage(page, queryWrapper);
    }

    /**
     * 获取子科目（同时获取科目类型）
     *
     * @param idList idList
     * @return List
     */
    public List<SubjectD> querySubjectWithCategoryNameByIdList(List<Long> idList,Wrapper<SubjectE> queryWrapper) {
        return baseMapper.querySubjectWithCategoryNameByIdList(idList,queryWrapper);
    }

    /**
     * 获取当前层级下的科目
     *
     * @param parentId         父科目id
     * @param subjectSystemId  体系id
     * @param tenantId         租户id
     * @param currentSubjectId 当前科目id
     * @return List
     */
    public List<SubjectE> getCurrentLevelSubject(Long parentId, Long subjectSystemId, String tenantId, Long currentSubjectId) {
        LambdaQueryWrapper<SubjectE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(parentId!=null,SubjectE::getParentId, parentId);
        queryWrapper.eq(subjectSystemId != null, SubjectE::getSubjectSystemId, subjectSystemId);
        queryWrapper.eq(StringUtils.isNotEmpty(tenantId), SubjectE::getTenantId, tenantId);
        queryWrapper.ne(currentSubjectId != null, SubjectE::getId, currentSubjectId);
        queryWrapper.orderByDesc(SubjectE::getId);
        return this.list(queryWrapper);
    }

    /**
     * 根据科目编码获取科目
     *
     * @param subjectCode      科目编码
     * @param tenantId         租户id
     * @param currentSubjectId 当前科目id
     * @return SubjectE
     */
    public SubjectE isExistSubjectCode(String subjectCode, String tenantId, Long currentSubjectId,Long subjectSystemId) {
        LambdaQueryWrapper<SubjectE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SubjectE::getSubjectCode, subjectCode);
        queryWrapper.eq(StringUtils.isNotEmpty(tenantId), SubjectE::getTenantId, tenantId);
        queryWrapper.eq(subjectSystemId != null, SubjectE::getSubjectSystemId, subjectSystemId);
        queryWrapper.ne(currentSubjectId != null, SubjectE::getId, currentSubjectId);
        queryWrapper.last("limit 1");
        return this.getOne(queryWrapper);
    }

    /**
     * 根据父科目id获取子科目数量
     *
     * @param parentId 父科目id
     * @param tenantId 租户id
     * @return long
     */
    public long getSubCountWithCurrentSubject(Long parentId, String tenantId) {
        LambdaQueryWrapper<SubjectE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SubjectE::getParentId, parentId);
        queryWrapper.eq(StringUtils.isNotEmpty(tenantId), SubjectE::getTenantId, tenantId);
        return this.count(queryWrapper);
    }

    /**
     * 根据科目体系删除科目
     *
     * @param subjectSystemId 体系id
     */
    public void removeBySubjectSystemId(Long subjectSystemId) {
        LambdaQueryWrapper<SubjectE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SubjectE::getSubjectSystemId, subjectSystemId);
        this.remove(queryWrapper);
    }

    /**
     * 获取当前租户下的所有正常的科目（过滤传入的id）
     *
     * @param filterIdList   过滤id
     * @param categoryId 类型id
     * @param systemId   体系id
     * @param tenantId   租户id
     * @return List
     */
    public List<SubjectE> getNormalSubject(List<Long> filterIdList, Long categoryId, Long systemId, String tenantId) {
        LambdaQueryWrapper<SubjectE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SubjectE::getDisabled, DataDisabledEnum.启用.getCode());
        queryWrapper.eq(categoryId != null, SubjectE::getCategoryId, categoryId);
        queryWrapper.eq(systemId != null, SubjectE::getSubjectSystemId, systemId);
        queryWrapper.eq(StringUtils.isNotEmpty(tenantId), SubjectE::getTenantId, tenantId);
        queryWrapper.notIn(!CollectionUtils.isEmpty(filterIdList), SubjectE::getId, filterIdList);
        queryWrapper.orderByDesc(SubjectE::getId);
        return this.list(queryWrapper);
    }

    /**
     * 根据科目体系id获取第一条科目
     *
     * @param subjectSystemId 体系id
     * @return SubjectE
     */
    public SubjectE getSubjectBySubjectSystemId(Long subjectSystemId) {
        LambdaQueryWrapper<SubjectE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SubjectE::getSubjectSystemId, subjectSystemId);
        queryWrapper.last("limit 1");
        return this.getOne(queryWrapper);
    }

    /**
     * 获取过滤的科目以及子科目
     *
     * @param filterId filterId
     * @param tenantId tenantId
     * @return List
     */
    public List<SubjectE> getFilterSubjectList(Long filterId, String tenantId) {
        return baseMapper.getFilterSubjectList(filterId, tenantId);
    }


    /**
     * 根据编码集合获取科目
     *
     * @param codeList codeList
     * @return List
     */
    public List<SubjectE> queryByCodeList(List<String> codeList, String tenantId) {
        LambdaQueryWrapper<SubjectE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotEmpty(tenantId), SubjectE::getTenantId, tenantId);
        queryWrapper.in(!CollectionUtils.isEmpty(codeList), SubjectE::getSubjectCode, codeList);
        return list(queryWrapper);
    }

    /**
     * 根据编码集合获取科目
     *
     * @param codeList codeList
     * @return List
     */
    public List<SubjectE> listByCodes(List<String> codeList) {
        if (CollectionUtils.isEmpty(codeList)) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<SubjectE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(!CollectionUtils.isEmpty(codeList), SubjectE::getSubjectCode, codeList);
        return list(queryWrapper);
    }

    /**
     * 根据科目编码查询科目全部信息（包含全部路径名称）
     * @param subjectCodes 科目编码
     * @return 科目信息
     */
    public List<SubjectE> listFullByCodes(List<String> subjectCodes){
        return baseMapper.selectListFullByCodes(subjectCodes);
    }

    /**
     * 根据科目体系id和类型id获取末级科目
     *
     * @param systemId 科目体系id
     * @param categoryId 类型id
     * @param limit
     * @return List
     */
    public List<SubjectE> getLastStage(Long systemId, Long categoryId, String name, Long subjectId, int limit) {
        LambdaQueryWrapper<SubjectE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(systemId != null, SubjectE::getSubjectSystemId, systemId);
        queryWrapper.eq(categoryId != null, SubjectE::getCategoryId, categoryId);
        queryWrapper.like(StringUtils.isNotEmpty(name) , SubjectE::getSubjectName, name);
        queryWrapper.eq(SubjectE::getLeaf, SubjectLeafStatusEnum.叶子节点.getCode());
        queryWrapper.like(Objects.nonNull(subjectId), SubjectE::getPath,subjectId );
        if (limit > 0) {
            queryWrapper.last(" limit " + limit);
        }
        return list(queryWrapper);
    }

    /**
     *
     * 根据费项id查科目数据
     * @param headSubjectId 一级科目id
     * @param chargeItemId 费项id
     * @return
     */
    public SubjectE getSubjectByChargeItemIdAndHeadSubjectId(Long headSubjectId, Long chargeItemId) {
        return baseMapper.getSubjectByChargeItemIdAndHeadSubjectId(headSubjectId, chargeItemId);
    }

    /**
     * 根据科目编码获取上级科目信息
     * @param code 科目编码
     * @return
     */
    public SubjectE getSupSubjectByCode(String code){
        return baseMapper.selectSupSubjectByCode(code);
    }

    /**
     * 根据科目编码获取科目列表
     * @param subjectCodes 根据科目编码获取科目列表
     * @return 科目列表
     */
    public List<SubjectE> listBySubjectCodes(List<String> subjectCodes) {
        return list(new LambdaQueryWrapper<SubjectE>().in(SubjectE::getSubjectCode, subjectCodes));
    }

    /**
     * 更新子科目的现金类型
     * @param path 父级路径
     * @param cashType 现金类型
     * @return 结果
     */
    public boolean updateChildrenCashType(List<Long> path, Integer cashType) {
        if (CollectionUtils.isEmpty(path)){
            return false;
        }
        return baseMapper.updateChildrenCashType(path, cashType) > 0;
    }

    /**
     * 根据编码获取科目信息
     * @param subjectCode 科目编码
     * @return 科目信息
     */
    public SubjectE getByCode(String subjectCode) {
        return getOne(new LambdaQueryWrapper<SubjectE>().eq(SubjectE::getSubjectCode, subjectCode), false);
    }

    public SubjectE getSubjectByCode(String code){
        return getOne(new LambdaUpdateWrapper<SubjectE>().eq(SubjectE::getSubjectCode,code), false);
    }
    /**
     * 查询科目
     * @param subjectId
     * @return
     */
    public SubjectE getSubject(Long subjectId) {
        return baseMapper.getSubject(subjectId);
    }

    public List<String> getAllAuxiliary(Long subjectId) {
        return baseMapper.getAllAuxiliary(subjectId);
    }
}
