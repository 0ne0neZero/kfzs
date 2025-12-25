package com.wishare.finance.domains.bill.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * 减免说明列表
 *
 * @Author dxclay
 * @Date 2022/12/19
 * @Version 1.0
 */
@Getter
@Setter
@ApiModel("减免说明列表")
public class DiscountOBV {

    @ApiModelProperty(value = "外部减免唯一标识")
    private String outDiscountId;

    @ApiModelProperty(value = "减免类型")
    @NotNull(message = "减免类型不能为空")
    private String discountType;

    @ApiModelProperty(value = "减免金额（分）")
    @NotNull(message = "减免金额不能为空")
    private Long amount;

    @ApiModelProperty(value = "减免说明")
    private String description;

}
