package com.wishare.finance.apps.model.configure.chargeitem.fo;

import com.wishare.finance.domains.configure.chargeitem.command.tax.UpdateTaxRateCommand;
import com.wishare.starter.Global;
import com.wishare.starter.beans.IdentityInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@ApiModel("修改税率信息")
public class UpdateTaxRateF {

    @ApiModelProperty("税率id")
    @NotNull(message = "税率id不能为空")
    private Long id;

    @ApiModelProperty("税率")
    @NotNull(message = "税率不能为空")
    private BigDecimal rate;

    @ApiModelProperty("计税方式 1:一般计税 2:简易计税")
    private String taxType;

    /**
     * 构建修改税率信息
     *
     * @param identityInfo
     * @return
     */
    public UpdateTaxRateCommand getUpdateTaxRateCommand(IdentityInfo identityInfo) {
        UpdateTaxRateCommand updateTaxRateCommand = Global.mapperFacade.map(this, UpdateTaxRateCommand.class);
        updateTaxRateCommand.setId(this.getId());
        updateTaxRateCommand.setRate(this.getRate());
        updateTaxRateCommand.setOperator(identityInfo.getUserId());
        updateTaxRateCommand.setOperatorName(identityInfo.getUserName());
        updateTaxRateCommand.setGmtModify(LocalDateTime.now());
        return updateTaxRateCommand;
    }
}
