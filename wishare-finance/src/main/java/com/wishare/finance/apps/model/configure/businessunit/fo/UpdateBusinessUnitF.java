package com.wishare.finance.apps.model.configure.businessunit.fo;

import com.wishare.finance.domains.configure.businessunit.command.businessunit.UpdateBusinessUnitCommand;
import com.wishare.finance.domains.configure.chargeitem.command.chargeitem.UpdateChargeItemCommand;
import com.wishare.starter.Global;
import com.wishare.starter.beans.IdentityInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 更新业务单元入参
 *
 * @author
 */
@Getter
@Setter
@ApiModel("更新业务单元数据")
public class UpdateBusinessUnitF {

    @ApiModelProperty(value = "业务单元id", required = true)
    @NotNull(message = "业务单元id不能为空")
    private Long id;

    @ApiModelProperty(value = "父业务单元id")
    private Long parentId;

    @ApiModelProperty(value ="业务单元编码", required = true)
    @NotBlank(message = "业务单元编码不能为空")
    private String code;

    @ApiModelProperty(value ="业务单元名称", required = true)
    @NotBlank(message = "业务单元名称不能为空")
    private String name;

    @ApiModelProperty(value = "是否末级：0否,1是")
    private Integer lastLevel;

    @ApiModelProperty(value ="是否禁用：0启用，1禁用", required = true)
    @NotNull(message = "是否禁用不能为空")
    private Integer disabled;

    @ApiModelProperty("法定单位id")
    private List<Long> legalIds;

    @ApiModelProperty("成本中心id")
    private List<Long> costIds;

    @ApiModelProperty("行政组织id")
    private List<Long> orgIds;

    @ApiModelProperty("银行账号id")
    private List<Long> statutoryBodyAccountId;

    public UpdateBusinessUnitCommand getUpdateBusinessUnitCommand(IdentityInfo identityInfo){
        UpdateBusinessUnitCommand command = Global.mapperFacade.map(this, UpdateBusinessUnitCommand.class);
        command.setOperator(identityInfo.getUserId());
        command.setOperatorName(identityInfo.getUserName());
        command.setGmtModify(LocalDateTime.now());
        command.setTenantId(identityInfo.getTenantId());
        return command;
    }
}
