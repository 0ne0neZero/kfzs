package com.wishare.finance.apps.model.configure.chargeitem.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author xujian
 * @date 2022/8/4
 * @Description:
 */
@Getter
@Setter
@ApiModel("税率详情信息")
public class TaxRateDetailV {

    @ApiModelProperty("税率主键id")
    private Long taxRateId;

    @ApiModelProperty("税率")
    private BigDecimal rate;

    @ApiModelProperty("税种id")
    private Long categoryId;

    @ApiModelProperty("税种名称")
    private String categoryName;

    @ApiModelProperty("税种编码")
    private String categoryCode;

    @ApiModelProperty("税种启用状态")
    private String categoryDisabled;

    @ApiModelProperty("计税方式 1:一般计税 2:简易计税")
    private String taxType;
}
