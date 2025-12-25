package com.wishare.finance.apps.model.configure.subject.fo;

import com.wishare.finance.domains.configure.subject.command.system.CreateSubjectSystemCommand;
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
 * 创建科目体系入参
 *
 * @author yancao
 */
@Getter
@Setter
@ApiModel("创建科目体系数据")
public class CreateSubjectSystemF {

    @ApiModelProperty(value = "科目体系编码", required = true)
    @NotBlank(message = "科目体系编码不能为空")
    private String code;

    @ApiModelProperty(value = "科目体系名称", required = true)
    @NotBlank(message = "科目体系名称不能为空")
    private String name;

    @ApiModelProperty("是否禁用：0启用，1禁用")
    private Integer disabled;

    public CreateSubjectSystemCommand getCreateSubjectSystemCommand(IdentityInfo identityInfo){
        CreateSubjectSystemCommand command = Global.mapperFacade.map(this, CreateSubjectSystemCommand.class);
        LocalDateTime now = LocalDateTime.now();
        command.setCreator(identityInfo.getUserId());
        command.setCreatorName(identityInfo.getUserName());
        command.setGmtCreate(now);
        command.setOperator(identityInfo.getUserId());
        command.setOperatorName(identityInfo.getUserName());
        command.setGmtModify(now);
        command.setTenantId(identityInfo.getTenantId());
        command.setId(IdentifierFactory.getInstance().generateLongIdentifier("subjectSystemId"));
        return command;
    }

}
