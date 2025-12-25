package com.wishare.finance.apps.model.configure.chargeitem.vo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 税目简单信息
 *
 * @author yancao
 */
@Getter
@Setter
@ApiModel("税目简单信息")
public class TaxItemSimpleV {

    @ApiModelProperty("税目id")
    private Long id;

    @ApiModelProperty("编码")
    private String code;

    @ApiModelProperty("名称")
    private String name;

}
