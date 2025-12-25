package com.wishare.finance.apps.model.configure.subject.fo;

import com.wishare.finance.domains.configure.subject.command.category.UpdateSubjectCategoryCommand;
import com.wishare.starter.Global;
import com.wishare.starter.beans.IdentityInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 更新科目类型入参
 *
 * @author yancao
 */
@Getter
@Setter
@ApiModel("更新科目类型数据")
public class UpdateSubjectCategoryF {

    @ApiModelProperty(value = "科目类型id",required = true)
    @NotNull(message = "科目类型id不能为空")
    private Long id;

    @ApiModelProperty(value = "科目类型名称",required = true)
    @NotBlank(message = "科目名称不能为空")
    private String categoryName;

    public UpdateSubjectCategoryCommand getUpdateSubjectCategoryCommand(IdentityInfo identityInfo){
        UpdateSubjectCategoryCommand command = Global.mapperFacade.map(this, UpdateSubjectCategoryCommand.class);
        command.setOperator(identityInfo.getUserId());
        command.setOperatorName(identityInfo.getUserName());
        command.setGmtModify(LocalDateTime.now());
        command.setTenantId(identityInfo.getTenantId());
        return command;
    }
}
