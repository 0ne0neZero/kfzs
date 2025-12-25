package com.wishare.finance.apps.model.configure.businessunit.fo;

import com.wishare.finance.domains.configure.businessunit.command.businessunit.CreateBusinessUnitCommand;
import com.wishare.finance.domains.configure.businessunit.command.businessunit.CreateBusinessUnitDetailCommand;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import com.wishare.starter.Global;
import com.wishare.starter.beans.IdentityInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 创建业务单元关联入参
 *
 * @author
 */
@Getter
@Setter
@ApiModel("创建业务单元关联数据")
public class CreateBusinessUnitDetailF {

    @ApiModelProperty(value = "业务单元id", required = true)
    private Long businessUnitId;

    @ApiModelProperty(value = "关联id", required = true)
    private Long relevanceId;

    @ApiModelProperty(value = "关联类型", required = true)
    private Integer type;

    public CreateBusinessUnitDetailCommand getCreateBusinessUnitDetailCommand(IdentityInfo identityInfo){
        CreateBusinessUnitDetailCommand command = Global.mapperFacade.map(this, CreateBusinessUnitDetailCommand.class);
        //设置创建人数据
        LocalDateTime now = LocalDateTime.now();
        command.setCreator(identityInfo.getUserId());
        command.setCreatorName(identityInfo.getUserName());
        command.setGmtCreate(now);
        command.setOperator(identityInfo.getUserId());
        command.setOperatorName(identityInfo.getUserName());
        command.setGmtModify(now);
        command.setTenantId(identityInfo.getTenantId());
        command.setId(IdentifierFactory.getInstance().generateLongIdentifier("businessUnitDetail"));
        return command;
    }
}
