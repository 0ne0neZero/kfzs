package com.wishare.finance.apps.model.configure.organization.fo;

import com.wishare.finance.domains.configure.organization.command.AddStatutoryBodyAccountCommand;
import com.wishare.finance.infrastructure.conts.DataDeletedEnum;
import com.wishare.finance.infrastructure.conts.DataDisabledEnum;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import com.wishare.starter.Global;
import com.wishare.starter.beans.IdentityInfo;
import com.wishare.starter.helpers.UidHelper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

/**
 * @author xujian
 * @date 2022/8/11
 * @Description:
 */
@Getter
@Setter
@ApiModel("新增银行账户入参")
public class AddStatutoryBodyAccountF {

    @ApiModelProperty(value = "账户名称", required = true)
    @NotBlank(message = "账户名称不能为空")
    private String name;

    @ApiModelProperty(value = "账户类型：1.基本账户，2一般账户，3专用账户", required = true)
    @NotNull(message = "账户类型不能为空")
    private Integer type;

    @ApiModelProperty(value = "开户行名称", required = true)
    @NotBlank(message = "开户行名称不能为空")
    private String bankName;

    @ApiModelProperty(value = "开户行账号", required = true)
    @NotBlank(message = "开户行账号不能为空")
    @Size(min = 1,max = 50,message = "开户行账号长度范围为1~50位")
    private String bankAccount;

    @ApiModelProperty(value = "收款付款类型：1.收款付款，2.收款，3.付款", required = true)
    @NotNull(message = "收款付款类型不能为空")
    private Integer recPayType;

    @ApiModelProperty(value = "是否禁用：0启用，1禁用", required = true)
    @NotNull(message = "是否禁用不能为空")
    private Integer disabled;

    @ApiModelProperty(value = "法定单位Id", required = true)
    @NotNull(message = "法定单位Id不能为空")
    private Long statutoryBodyId;

    @ApiModelProperty(value = "法定单位名称")
    private String statutoryBodyName;

    /**
     * 构建新增银行账户command
     *
     * @param curIdentityInfo
     * @return
     */
    public AddStatutoryBodyAccountCommand getAddStatutoryBodyAccountCommand(IdentityInfo curIdentityInfo) {
        AddStatutoryBodyAccountCommand command = Global.mapperFacade.map(this, AddStatutoryBodyAccountCommand.class);
        command.setId(IdentifierFactory.getInstance().generateLongIdentifier("statutory_body_account_id"));
        command.setTenantId(curIdentityInfo.getTenantId());
        command.setDeleted(DataDeletedEnum.NORMAL.getCode());
        command.setDisabled(DataDisabledEnum.启用.getCode());
        return command;
    }
}
