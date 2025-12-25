package com.wishare.finance.apps.model.configure.chargeitem.fo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 同步税率
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/3/13
 */
@Setter
@Getter
public class SyncTaxRateF {

    @ApiModelProperty("税种编码")
    private String code;

    @ApiModelProperty("税种名称")
    private String name;

    @ApiModelProperty("是否禁用：0启用，1禁用")
    private Integer disabled;

    @ApiModelProperty("父税种id,默认根id为0")
    private Long parentId;

    @ApiModelProperty("税种id路径")
    private String path;

    @ApiModelProperty(value = "税率信息列表")
    private List<AddTaxRateF> taxRates;

}
