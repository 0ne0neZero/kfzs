package com.wishare.finance.apps.model.configure.chargeitem.fo;

import com.wishare.finance.domains.configure.chargeitem.command.tax.AddTaxRateCommand;
import com.wishare.finance.infrastructure.conts.DataDisabledEnum;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import com.wishare.starter.Global;
import com.wishare.starter.beans.IdentityInfo;
import com.wishare.starter.helpers.UidHelper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@ApiModel("新增税率信息")
public class AddTaxRateF {

    @ApiModelProperty("税种id")
    @NotNull(message = "税种id不能为空")
    private Long taxCategoryId;

    @ApiModelProperty("税率")
    @NotNull(message = "税率不能为空")
    private BigDecimal rate;

    @ApiModelProperty("税率编码")
    private String code;

    @ApiModelProperty("计税方式 1:一般计税 2:简易计税")
    private String taxType;

    /**
     * 构造新增税率实体
     *
     * @return
     */
    public AddTaxRateCommand getAddTaxRateCommand(IdentityInfo identityInfo) {
        AddTaxRateCommand addTaxRateCommand = Global.mapperFacade.map(this, AddTaxRateCommand.class);
        addTaxRateCommand.setId(IdentifierFactory.getInstance().generateLongIdentifier("taxRateId"));
        addTaxRateCommand.setDeleted(DataDisabledEnum.启用.getCode());
        addTaxRateCommand.setTenantId(identityInfo.getTenantId());
        addTaxRateCommand.setCreator(identityInfo.getUserId());
        addTaxRateCommand.setCreatorName(identityInfo.getUserName());
        addTaxRateCommand.setGmtCreate(LocalDateTime.now());
        addTaxRateCommand.setOperator(identityInfo.getUserId());
        addTaxRateCommand.setOperatorName(identityInfo.getUserName());
        addTaxRateCommand.setGmtModify(LocalDateTime.now());
        return addTaxRateCommand;
    }

    ;
}
