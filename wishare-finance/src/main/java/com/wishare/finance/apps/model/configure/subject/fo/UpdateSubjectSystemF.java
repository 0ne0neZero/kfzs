package com.wishare.finance.apps.model.configure.subject.fo;

import com.wishare.finance.domains.configure.subject.command.system.UpdateSubjectSystemCommand;
import com.wishare.starter.Global;
import com.wishare.starter.beans.IdentityInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 创建科目入参
 *
 * @author yancao
 */
@Getter
@Setter
@ApiModel("更新科目体系数据")
public class UpdateSubjectSystemF {

    @ApiModelProperty(value = "科目体系id", required = true)
    @NotNull(message = "科目体系id不能为空")
    private Long id;

    @ApiModelProperty(value = "科目体系编码")
    private String code;

    @ApiModelProperty(value = "科目体系名称")
    private String name;

    @ApiModelProperty(value = "是否禁用：0启用，1禁用")
    private Integer disabled;

    public UpdateSubjectSystemCommand getUpdateSubjectSystemCommand(IdentityInfo identityInfo){
        UpdateSubjectSystemCommand command = Global.mapperFacade.map(this, UpdateSubjectSystemCommand.class);
        command.setOperator(identityInfo.getUserId());
        command.setOperatorName(identityInfo.getUserName());
        command.setGmtModify(LocalDateTime.now());
        command.setTenantId(identityInfo.getTenantId());
        return command;
    }
}
