package com.wishare.finance.domains.configure.subject;

import com.wishare.finance.apps.model.configure.subject.fo.CreateSubjectCategoryF;
import com.wishare.finance.domains.configure.subject.command.category.CreateSubjectCategoryCommand;
import com.wishare.finance.domains.configure.subject.command.category.DeleteSubjectCategoryByPertainIdCommand;
import com.wishare.finance.domains.configure.subject.command.system.CreateSubjectSystemCommand;
import com.wishare.finance.domains.configure.subject.command.system.DeleteSubjectSystemCommand;
import com.wishare.finance.domains.configure.subject.consts.enums.SubjectCategoryDefaultEnum;
import com.wishare.finance.domains.configure.subject.service.SubjectCategoryDomainService;
import com.wishare.finance.domains.configure.subject.service.SubjectDomainService;
import com.wishare.finance.domains.configure.subject.service.SubjectSystemDomainService;
import com.wishare.starter.interfaces.ApiBase;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 科目聚合
 *
 * @author yancao
 */
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SubjectAggregate implements ApiBase {

    private final SubjectSystemDomainService subjectSystemDomainService;

    private final SubjectCategoryDomainService subjectCategoryDomainService;

    private final SubjectDomainService subjectDomainService;

    /**
     * 创建科目体系和默认的科目类型
     *
     * @param command command
     * @return Long
     */
    public Long createSubjectSystem(CreateSubjectSystemCommand command){
        //创建科目体系
        Long subjectSystemId = subjectSystemDomainService.create(command);
        //不存在默认的科目类型创建，否则不创建
        SubjectCategoryDefaultEnum[] subjectCategoryDefaultEnums = SubjectCategoryDefaultEnum.values();
        for (SubjectCategoryDefaultEnum subjectCategoryDefaultEnum : subjectCategoryDefaultEnums) {
            CreateSubjectCategoryF subjectCategoryF = new CreateSubjectCategoryF();
            subjectCategoryF.setCategoryName(subjectCategoryDefaultEnum.getValue());
            subjectCategoryF.setPertainId(subjectSystemId);
            CreateSubjectCategoryCommand categoryCommand = subjectCategoryF.getCreateSubjectCategoryCommand(curIdentityInfo());
            categoryCommand.setId((long) subjectCategoryDefaultEnum.getCode());
            subjectCategoryDomainService.create(categoryCommand);
        }
        return subjectSystemId;
    }

    /**
     * 删除科目体系和体系关联的科目类型以及科目
     *
     * @return Boolean
     */
    public Boolean deleteSubjectSystem(DeleteSubjectSystemCommand command){
        //移除科目体系
        subjectSystemDomainService.delete(command);
        //移除关联的科目类型
        DeleteSubjectCategoryByPertainIdCommand deleteCategoryCommand = new DeleteSubjectCategoryByPertainIdCommand(command.getId());
        subjectCategoryDomainService.deleteByPertainId(deleteCategoryCommand);
        return true;
    }
}
