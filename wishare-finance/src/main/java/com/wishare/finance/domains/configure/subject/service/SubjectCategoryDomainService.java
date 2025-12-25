package com.wishare.finance.domains.configure.subject.service;

import com.alibaba.fastjson.JSON;
import com.wishare.finance.domains.configure.subject.command.category.CreateSubjectCategoryCommand;
import com.wishare.finance.domains.configure.subject.command.category.DeleteSubjectCategoryByPertainIdCommand;
import com.wishare.finance.domains.configure.subject.command.category.DeleteSubjectCategoryCommand;
import com.wishare.finance.domains.configure.subject.command.category.FindChildSubjectCategoryByIdQuery;
import com.wishare.finance.domains.configure.subject.command.category.FindSubjectCategoryByIdQuery;
import com.wishare.finance.domains.configure.subject.command.category.FindSubjectCategoryByPertainIdQuery;
import com.wishare.finance.domains.configure.subject.command.category.UpdateSubjectCategoryCommand;
import com.wishare.finance.domains.configure.subject.consts.enums.SubjectLeafStatusEnum;
import com.wishare.finance.domains.configure.subject.consts.enums.SubjectStatusEnum;
import com.wishare.finance.domains.configure.subject.entity.SubjectCategoryE;
import com.wishare.finance.domains.configure.subject.repository.SubjectCategoryRepository;
import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.starter.Global;
import com.wishare.starter.consts.Const;
import com.wishare.starter.exception.BizException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 科目类型业务层
 *
 * @author yancao
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SubjectCategoryDomainService {

    private final SubjectCategoryRepository subjectCategoryRepository;

    /**
     * 根据体系id查询科目类型
     *
     * @param query query
     * @return List
     */
    public List<SubjectCategoryE> getByPertainId(FindSubjectCategoryByPertainIdQuery query) {
        return subjectCategoryRepository.getSubjectCategoryByPertainId(query.getPertainIdList(), query.getTenantId());
    }

    /**
     * 创建科目类型
     *
     * @param command command
     * @return Long
     */
    public Long create(CreateSubjectCategoryCommand command) {
        SubjectCategoryE subjectCategory = Global.mapperFacade.map(command, SubjectCategoryE.class);
        Long parentId = subjectCategory.getParentId() == null ? Const.State._0 : subjectCategory.getParentId();
        Long pertainId = subjectCategory.getPertainId();

        //校验当前法定单位下，当前体系下，当前层级下是否存在相同科目类型名称
        checkCategoryName(null, parentId, pertainId, command.getCategoryName(), command.getTenantId());

        //获取当前父科目下的子科目数量
        Long count = subjectCategoryRepository.countAll(parentId);
        if (parentId != 0) {
            //parentId不为0判断父科目类型层级是否超过最大层数
            SubjectCategoryE parentSubjectCategory = subjectCategoryRepository.getById(parentId);
            if (parentSubjectCategory == null) {
                throw BizException.throw400(ErrorMessage.SUBJECT_CATEGORY_PARENT_NOT_EXIST.getErrMsg());
            }
            if (parentSubjectCategory.getLevel() >= SubjectStatusEnum.SUBJECT_CATEGORY_MAX_LEVEL.getCode()) {
                throw BizException.throw400(ErrorMessage.SUBJECT_CATEGORY_EXCEED_LEVEL.getErrMsg());
            }

            //设置层级和编码
            Integer level = parentSubjectCategory.getLevel();
            subjectCategory.setLevel(level + 1);
            String categoryCode = generateSubjectCategoryCode(parentSubjectCategory.getCategoryCode(), count);
            subjectCategory.setCategoryCode(categoryCode);
            List<Long> path = JSON.parseArray(parentSubjectCategory.getPath(), Long.class);
            path.add(subjectCategory.getId());
            subjectCategory.setPath(JSON.toJSONString(path));

            //更新父类为非叶子节点
            if (parentSubjectCategory.getLeaf() == SubjectLeafStatusEnum.叶子节点.getCode()) {
                parentSubjectCategory.setLeaf(SubjectLeafStatusEnum.非叶子节点.getCode());
                subjectCategoryRepository.updateById(parentSubjectCategory);
            }
        } else {
            //parentId不为0说明是根节点（从1开始）
            subjectCategory.setLevel(SubjectStatusEnum.SUBJECT_CATEGORY_LEVEL_START_NUM.getCode());
            subjectCategory.setParentId(parentId);
            String categoryCode = generateSubjectCategoryCode("", count);
            subjectCategory.setCategoryCode(categoryCode);
            ArrayList<Long> path = new ArrayList<>();
            path.add(subjectCategory.getId());
            subjectCategory.setPath(JSON.toJSONString(path));
        }

        subjectCategory.setLeaf(SubjectLeafStatusEnum.叶子节点.getCode());
        subjectCategoryRepository.save(subjectCategory);
        return subjectCategory.getId();
    }

    /**
     * 更新科目类型
     *
     * @param command command
     * @return Boolean
     */
    public Boolean update(UpdateSubjectCategoryCommand command) {
        SubjectCategoryE subjectCategory = Global.mapperFacade.map(command, SubjectCategoryE.class);
        Long subjectCategoryId = subjectCategory.getId();
        SubjectCategoryE currentSubjectCategory = subjectCategoryRepository.getById(subjectCategoryId);
        if (currentSubjectCategory == null) {
            throw BizException.throw400(ErrorMessage.SUBJECT_CATEGORY_NOT_EXIST.getErrMsg());
        }
        //校验当前法定单位下，当前体系下，当前层级下是否存在相同科目类型名称
        checkCategoryName(subjectCategoryId, currentSubjectCategory.getParentId(), currentSubjectCategory.getPertainId(),
                subjectCategory.getCategoryName(), subjectCategory.getTenantId());
        return subjectCategoryRepository.updateById(subjectCategory);
    }

    /**
     * 根据id查询科目类型详情
     *
     * @param query query
     * @return SubjectCategoryE
     */
    public SubjectCategoryE queryById(FindSubjectCategoryByIdQuery query) {
        return subjectCategoryRepository.getById(query.getId());
    }

    /**
     * 校验科目类型名称是否已存在
     *
     * @param currentCategoryId currentCategoryId
     * @param parentId          parentId
     * @param categoryName      categoryName
     * @param pertainId         体系id
     */
    private void checkCategoryName(Long currentCategoryId, Long parentId, Long pertainId, String categoryName, String tenantId) {
        List<SubjectCategoryE> subjectCategoryList = subjectCategoryRepository.getSubjectCategoryByParentId(parentId, pertainId, tenantId);
        List<SubjectCategoryE> collect = subjectCategoryList.stream()
                .filter(subjectCategory -> subjectCategory.getCategoryName().equals(categoryName) && !subjectCategory.getId().equals(currentCategoryId))
                .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(collect)) {
            throw BizException.throw400(ErrorMessage.SUBJECT_CATEGORY_EXIST.getErrMsg());
        }
    }

    /**
     * 生成科目类型编码
     *
     * @param parentCode 父类型编码
     * @param count      当前类型下的类型总数
     * @return String
     */
    private static String generateSubjectCategoryCode(String parentCode, Long count) {
        String currentCode = String.valueOf(101 + count);
        return parentCode + currentCode;
    }

    /**
     * 根据科目类型id查询子科目类型列表
     *
     * @param query query
     * @return List
     */
    public List<SubjectCategoryE> queryChildById(FindChildSubjectCategoryByIdQuery query) {
        Long parentId = query.getId();
        return subjectCategoryRepository.getSubjectCategoryByParentId(parentId, null, query.getTenantId());
    }

    /**
     * 删除科目类型
     *
     * @param command command
     * @return Boolean
     */
    public Boolean delete(DeleteSubjectCategoryCommand command) {
        //获取当前科目类型
        SubjectCategoryE subjectCategory = subjectCategoryRepository.getById(command.getId());
        if (subjectCategory == null) {
            throw BizException.throw400(ErrorMessage.SUBJECT_CATEGORY_NOT_EXIST.getErrMsg());
        }

        //获取该科目类型下的所有子类型
        List<Long> idList = Collections.singletonList(subjectCategory.getId());
        List<SubjectCategoryE> subjectCategoryList = subjectCategoryRepository.queryChildSubjectCategoryById(idList);

        //当前科目类型不为根节点时，如果当前科目的父科目只有一个子科目，更新父科目为叶子节点
        if (!subjectCategory.getParentId().equals(0L)) {
            long count = subjectCategoryRepository.getSubCountWithCurrentSubjectCategory(command.getId(), subjectCategory.getTenantId());
            if (count == 1) {
                SubjectCategoryE parentSubjectCategoryE = new SubjectCategoryE();
                parentSubjectCategoryE.setId(subjectCategory.getParentId());
                parentSubjectCategoryE.setLeaf(SubjectLeafStatusEnum.叶子节点.getCode());
                subjectCategoryRepository.updateById(parentSubjectCategoryE);
            }
        }

        //更新是否删除字段
        List<Long> deleteIdList = subjectCategoryList.stream().map(SubjectCategoryE::getId).collect(Collectors.toList());
        return subjectCategoryRepository.removeByIds(deleteIdList);
    }

    /**
     * 根据体系id删除科目类型
     *
     * @param command command
     */
    public void deleteByPertainId(DeleteSubjectCategoryByPertainIdCommand command) {
        subjectCategoryRepository.removeByPertainId(command.getPertainId());
    }
}
