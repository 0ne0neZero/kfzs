package com.wishare.finance.apps.model.configure.chargeitem.fo;

import com.wishare.finance.domains.configure.chargeitem.command.chargeitem.ShowedChargeItemCommand;
import com.wishare.starter.Global;
import com.wishare.starter.beans.IdentityInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 显示/隐藏费项入参
 *
 * @author yancao
 */
@Getter
@Setter
@ApiModel("显示或隐藏费项数据")
public class ShowOrHideChargeItemF {

    @ApiModelProperty(value = "费项id集合", required = true)
    @NotEmpty(message = "费项id集合不能为空")
    private List<Long> idList;

    @ApiModelProperty(value = "显示或隐藏（0隐藏 1显示）", required = true)
    @NotNull(message = "显示或隐藏不能为空")
    private Integer showed;

    public ShowedChargeItemCommand getShowedChargeItemCommand(IdentityInfo identityInfo){
        ShowedChargeItemCommand command = Global.mapperFacade.map(this, ShowedChargeItemCommand.class);
        command.setOperator(identityInfo.getUserId());
        command.setOperatorName(identityInfo.getUserName());
        command.setGmtModify(LocalDateTime.now());
        command.setTenantId(identityInfo.getTenantId());
        return command;
    }
}
