package com.wishare.finance.apps.service.configure.subject;

import com.wishare.finance.apps.model.configure.subject.fo.CreateSubjectCategoryF;
import com.wishare.finance.apps.model.configure.subject.fo.UpdateSubjectCategoryF;
import com.wishare.finance.apps.model.configure.subject.vo.SubjectCategorySimpleV;
import com.wishare.finance.apps.model.configure.subject.vo.SubjectCategoryTreeV;
import com.wishare.finance.apps.model.configure.subject.vo.SubjectCategoryV;
import com.wishare.finance.domains.configure.subject.command.category.CreateSubjectCategoryCommand;
import com.wishare.finance.domains.configure.subject.command.category.DeleteSubjectCategoryCommand;
import com.wishare.finance.domains.configure.subject.command.category.FindChildSubjectCategoryByIdQuery;
import com.wishare.finance.domains.configure.subject.command.category.FindSubjectCategoryByIdQuery;
import com.wishare.finance.domains.configure.subject.command.category.FindSubjectCategoryByPertainIdQuery;
import com.wishare.finance.domains.configure.subject.command.category.UpdateSubjectCategoryCommand;
import com.wishare.finance.domains.configure.subject.consts.enums.SubjectCategoryDefaultEnum;
import com.wishare.finance.domains.configure.subject.entity.SubjectCategoryE;
import com.wishare.finance.domains.configure.subject.service.SubjectCategoryDomainService;
import com.wishare.starter.Global;
import com.wishare.starter.interfaces.ApiBase;
import com.wishare.starter.utils.TreeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 科目类型应用服务
 *
 * @author yancao
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SubjectCategoryAppService implements ApiBase {

    private final SubjectCategoryDomainService subjectCategoryDomainService;

    /**
     * 根据体系id查询科目类型
     *
     * @param id 体系id
     * @return List
     */
    public List<SubjectCategoryTreeV> getTreeByPertainId(Long id) {
        List<Long> pertainIdList = Collections.singletonList(id);
        //根据体系id获取所有科目类型
        FindSubjectCategoryByPertainIdQuery query = new FindSubjectCategoryByPertainIdQuery(pertainIdList, curIdentityInfo().getTenantId());
        List<SubjectCategoryE> subjectCategoryList = subjectCategoryDomainService.getByPertainId(query);
        ArrayList<SubjectCategoryTreeV> subjectCategoryTreeVoList = new ArrayList<>();
        for (SubjectCategoryE subjectCategoryE : subjectCategoryList) {
            SubjectCategoryTreeV subjectCategoryTreeV = Global.mapperFacade.map(subjectCategoryE, SubjectCategoryTreeV.class);
            if (subjectCategoryE.getParentId() != null) {
                subjectCategoryTreeV.setPid(subjectCategoryE.getParentId());
            }
            subjectCategoryTreeVoList.add(subjectCategoryTreeV);
        }
        return TreeUtil.treeing(subjectCategoryTreeVoList);
    }

    /**
     * 创建科目类型
     *
     * @param createSubjectCategoryF createSubjectCategoryF
     * @return Long
     */
    @Transactional
    public Long create(CreateSubjectCategoryF createSubjectCategoryF) {
        CreateSubjectCategoryCommand command = createSubjectCategoryF.getCreateSubjectCategoryCommand(curIdentityInfo());
        return subjectCategoryDomainService.create(command);
    }

    /**
     * 更新科目类型
     *
     * @param updateSubjectCategoryF updateSubjectCategoryF
     * @return Boolean
     */
    @Transactional
    public Boolean update(UpdateSubjectCategoryF updateSubjectCategoryF) {
        UpdateSubjectCategoryCommand command = updateSubjectCategoryF.getUpdateSubjectCategoryCommand(curIdentityInfo());
        return subjectCategoryDomainService.update(command);
    }

    /**
     * 根据id查询科目类型详情
     *
     * @param id id
     * @return SubjectCategoryV
     */
    public SubjectCategoryV queryById(Long id) {
        FindSubjectCategoryByIdQuery query = new FindSubjectCategoryByIdQuery(id);
        SubjectCategoryE subjectCategory = subjectCategoryDomainService.queryById(query);
        return Global.mapperFacade.map(subjectCategory, SubjectCategoryV.class);
    }

    /**
     * 根据id查询子科目类型列表
     *
     * @param id id
     * @return List
     */
    public List<SubjectCategoryV> queryChildById(Long id) {
        FindChildSubjectCategoryByIdQuery query = new FindChildSubjectCategoryByIdQuery(id,curIdentityInfo().getTenantId());
        List<SubjectCategoryE> subjectCategoryList = subjectCategoryDomainService.queryChildById(query);
        return Global.mapperFacade.mapAsList(subjectCategoryList, SubjectCategoryV.class);
    }

    /**
     * 删除科目类型
     *
     * @param id id
     * @return Boolean
     */
    public Boolean delete(Long id) {
        DeleteSubjectCategoryCommand command = new DeleteSubjectCategoryCommand();
        command.setId(id);
        return subjectCategoryDomainService.delete(command);
    }

    /**
     * 根据体系id集合查询所有科目类型
     *
     * @param idList idList
     * @return List
     */
    public List<SubjectCategoryV> getAllByPertainId(List<Long> idList) {
        FindSubjectCategoryByPertainIdQuery query = new FindSubjectCategoryByPertainIdQuery(idList, curIdentityInfo().getTenantId());
        List<SubjectCategoryE> subjectCategoryList = subjectCategoryDomainService.getByPertainId(query);
        return Global.mapperFacade.mapAsList(subjectCategoryList, SubjectCategoryV.class);
    }

    /**
     * 获取默认科目类型
     *
     * @return List
     */
    public List<SubjectCategorySimpleV> queryDefaultCategory() {
        ArrayList<SubjectCategorySimpleV> result = new ArrayList<>();
        SubjectCategoryDefaultEnum[] subjectCategoryDefaultEnums = SubjectCategoryDefaultEnum.values();
        for (SubjectCategoryDefaultEnum subjectCategoryDefaultEnum : subjectCategoryDefaultEnums) {
            SubjectCategorySimpleV subjectCategoryTreeV = new SubjectCategorySimpleV();
            subjectCategoryTreeV.setId((long) subjectCategoryDefaultEnum.getCode());
            subjectCategoryTreeV.setCategoryName(subjectCategoryDefaultEnum.getValue());
            result.add(subjectCategoryTreeV);
        }
        return result;
    }
}
