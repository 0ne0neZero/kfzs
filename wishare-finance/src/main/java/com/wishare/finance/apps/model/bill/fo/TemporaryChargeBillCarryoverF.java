package com.wishare.finance.apps.model.bill.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * 临时账单结转入参
 *
 * @author yancao
 */
@Getter
@Setter
@ApiModel("临时账单结转入参")
public class TemporaryChargeBillCarryoverF extends BillCarryoverF{

    @ApiModelProperty(value = "上级收费单元ID")
    @NotBlank(message = "上级收费单元ID不能为空!")
    private String supCpUnitId;

}
