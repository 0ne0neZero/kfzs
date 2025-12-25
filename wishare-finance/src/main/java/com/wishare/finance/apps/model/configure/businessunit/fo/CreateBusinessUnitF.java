package com.wishare.finance.apps.model.configure.businessunit.fo;

import com.wishare.finance.domains.configure.businessunit.command.businessunit.CreateBusinessUnitCommand;
import com.wishare.finance.domains.configure.chargeitem.command.chargeitem.CreateChargeItemCommand;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import com.wishare.starter.Global;
import com.wishare.starter.beans.IdentityInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 创建业务单元入参
 *
 * @author
 */
@Getter
@Setter
@ApiModel("创建业务单元数据")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateBusinessUnitF {

    @ApiModelProperty(value = "业务单元名称", required = true)
    @NotBlank(message = "业务单元名称不能为空")
    private String name;

    @ApiModelProperty(value = "业务单元编码", required = true)
    @NotBlank(message = "业务单元编码不能为空")
    private String code;

    @ApiModelProperty(value = "是否末级：0否,1是")
    private Integer lastLevel;

    @ApiModelProperty("父业务单元id")
    private Long parentId;

    @ApiModelProperty("是否禁用：0启用，1禁用")
    private Integer disabled;

    @ApiModelProperty("法定单位id")
    private List<Long> legalIds;

    @ApiModelProperty("成本中心id")
    private List<Long> costIds;

    @ApiModelProperty("行政组织id")
    private List<Long> orgIds;

    @ApiModelProperty("银行账号id")
    private List<Long> statutoryBodyAccountId;

    public CreateBusinessUnitCommand getCreateBusinessUnitCommand(IdentityInfo identityInfo){
        CreateBusinessUnitCommand command = Global.mapperFacade.map(this, CreateBusinessUnitCommand.class);
        //设置创建人数据
        LocalDateTime now = LocalDateTime.now();
        command.setCreator(identityInfo.getUserId());
        command.setCreatorName(identityInfo.getUserName());
        command.setGmtCreate(now);
        command.setOperator(identityInfo.getUserId());
        command.setOperatorName(identityInfo.getUserName());
        command.setGmtModify(now);
        command.setTenantId(identityInfo.getTenantId());
        command.setId(IdentifierFactory.getInstance().generateLongIdentifier("businessUnit"));
        return command;
    }
}
