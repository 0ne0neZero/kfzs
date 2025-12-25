package com.wishare.finance.apps.model.configure.subject.fo;

import com.wishare.finance.domains.configure.subject.command.category.CreateSubjectCategoryCommand;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import com.wishare.starter.Global;
import com.wishare.starter.beans.IdentityInfo;
import com.wishare.starter.helpers.UidHelper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * 创建科目入参
 *
 * @author yancao
 */
@Getter
@Setter
@ApiModel("创建科目类型数据")
public class CreateSubjectCategoryF {


    @ApiModelProperty(value = "科目类型名称", required = true)
    @NotBlank(message = "科目类型名称不能为空")
    private String categoryName;

    @ApiModelProperty("父科目类型id")
    private Long parentId;

    @ApiModelProperty("科目体系id")
    private Long pertainId;

    public CreateSubjectCategoryCommand getCreateSubjectCategoryCommand(IdentityInfo identityInfo) {
        CreateSubjectCategoryCommand command = Global.mapperFacade.map(this, CreateSubjectCategoryCommand.class);
        LocalDateTime now = LocalDateTime.now();
        command.setCreator(identityInfo.getUserId());
        command.setCreatorName(identityInfo.getUserName());
        command.setGmtCreate(LocalDateTime.now());
        command.setOperator(identityInfo.getUserId());
        command.setOperatorName(identityInfo.getUserName());
        command.setGmtModify(now);
        command.setTenantId(identityInfo.getTenantId());
        command.setId(IdentifierFactory.getInstance().generateLongIdentifier("SubjectCategoryId"));
        return command;
    }

}
