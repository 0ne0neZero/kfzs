package com.wishare.finance.apps.model.bill.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * 所有账单查询入参
 *
 * @author yancao
 */
@Getter
@Setter
@ApiModel("所有账单查询入参")
public class AllBillQueryF {

    @ApiModelProperty("账单id")
    @NotNull(message = "账单id不能为空")
    private Long id;

    @ApiModelProperty("账单类型")
    @NotNull(message = "账单类型不能为空")
    private Integer billType;

    @ApiModelProperty(value = "上级收费单元id")
    @NotBlank(message = "上级收费单元id不能为空")
    private String supCpUnitId;

}
