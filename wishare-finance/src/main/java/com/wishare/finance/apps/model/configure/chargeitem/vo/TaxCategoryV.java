package com.wishare.finance.apps.model.configure.chargeitem.vo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TaxCategoryV {

    @ApiModelProperty("税种id")
    private Long id;

    @ApiModelProperty("税种编码")
    private String code;

    @ApiModelProperty("税种名称")
    private String name;

    @ApiModelProperty("税种是否禁用：0启用，1禁用")
    private Integer disabled;

    @ApiModelProperty("税种id")
    private List<TaxRateV> taxRateS;
}
