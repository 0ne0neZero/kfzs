package com.wishare.finance.apps.model.configure.organization.fo;

import com.wishare.finance.domains.configure.organization.command.UpdateStatutoryBodyAccountCommand;
import com.wishare.starter.Global;
import com.wishare.starter.beans.IdentityInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * @author xujian
 * @date 2022/8/11
 * @Description:
 */
@Getter
@Setter
@ApiModel("修改银行账户入参")
public class UpdateStatutoryBodyAccountF {

    @ApiModelProperty(value = "银行账户id", required = true)
    @NotNull(message = "银行账户id不能为空")
    private Long id;

    @ApiModelProperty(value = "账户名称")
    private String name;

    @ApiModelProperty(value = "账户类型：1.基本账户，2一般账户，3专用账户")
    private Integer type;

    @ApiModelProperty(value = "开户行名称")
    private String bankName;

    @ApiModelProperty(value = "开户行账号")
    private String bankAccount;

    @ApiModelProperty(value = "收款付款类型：1.收款付款，2.收款，3.付款")
    private Integer recPayType;

    @ApiModelProperty(value = "是否禁用：0启用，1禁用")
    private Integer disabled;

    @ApiModelProperty(value = "法定单位Id")
    private Long statutoryBodyId;

    /**
     * 构建修改银行账户command
     *
     * @param curIdentityInfo
     * @return
     */
    public UpdateStatutoryBodyAccountCommand getUpdateStatutoryBodyAccountCommand(IdentityInfo curIdentityInfo) {
        UpdateStatutoryBodyAccountCommand command = Global.mapperFacade.map(this, UpdateStatutoryBodyAccountCommand.class);
        return command;
    }
}
