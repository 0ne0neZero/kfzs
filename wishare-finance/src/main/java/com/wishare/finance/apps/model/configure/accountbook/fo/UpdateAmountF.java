package com.wishare.finance.apps.model.configure.accountbook.fo;

import com.wishare.finance.domains.configure.accountbook.command.UpdateAmountCommand;
import com.wishare.starter.Global;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author xujian
 * @date 2022/8/22
 * @Description:
 */
@Getter
@Setter
@ApiModel("账簿金额变动入参")
public class UpdateAmountF {

    @ApiModelProperty(value = "账簿id",required = true)
    @NotNull(message = "账簿id不能为空")
    private Long accountBookId;

    @ApiModelProperty(value = "费项id",required = true)
    @NotNull(message = "费项id不能为空")
    private Long chargeItemId;

    @ApiModelProperty("资产金额")
    private BigDecimal assetsAmount;

    @ApiModelProperty("费用金额")
    private BigDecimal costAmount;

    @ApiModelProperty("负债金额")
    private BigDecimal liabilitiesAmount;

    @ApiModelProperty("所有者权益金额")
    private BigDecimal equitiesAmount;

    @ApiModelProperty("收入金额")
    private BigDecimal incomeAmount;

    public UpdateAmountCommand getUpdateAmountCommand() {
        UpdateAmountCommand command = Global.mapperFacade.map(this, UpdateAmountCommand.class);
        return command;
    }
}
