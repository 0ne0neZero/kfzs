package com.wishare.finance.apps.model.configure.chargeitem.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.math.BigDecimal;


@Getter
@Setter
@ApiModel("税率信息")
public class TaxRateV {

    @ApiModelProperty("税率主键id")
    private Long taxRateId;
    @ApiModelProperty("税率目录主键id")
    private Long taxCategoryId;

    @ApiModelProperty("税率")
    private BigDecimal rate;

    @ApiModelProperty("税率主键id,用于构建树图")
    private Long id;

    @ApiModelProperty("税率,用于构建树图")
    private String name;
    @ApiModelProperty("税率code")
    private String code;

    public Long getId() {
        return taxRateId;
    }

    public void setId(Long id) {
        this.taxRateId = taxRateId;
    }

    public String getName() {
        return rate.toString();
    }

    public void setName(String name) {
        this.rate = rate;
    }
}
