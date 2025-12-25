package com.wishare.finance.apps.model.configure.chargeitem.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 费项类型信息
 *
 * @author yancao
 */
@Getter
@Setter
@ApiModel("费项类型信息")
public class ChargeTypeV {

    @ApiModelProperty("费项类型id")
    private Long id;

    @ApiModelProperty("费项类型名称")
    private String name;

}
