package com.wishare.finance.domains.configure.chargeitem.dto.taxitem;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 税目dto
 *
 * @author yancao
 */
@Getter
@Setter
public class TaxChargeItemRelationD {

    @ApiModelProperty("税目id")
    private Long taxItemId;

    @ApiModelProperty("税目编码")
    private String taxItemCode;

    @ApiModelProperty("税目名称")
    private String taxItemName;

    @ApiModelProperty("税收商品id")
    private Long taxItemGoodsId;

    @ApiModelProperty("费项id")
    private Long chargeItemId;

    @ApiModelProperty("费项名称")
    private String name;

    @ApiModelProperty("费项编码")
    private String code;

    @ApiModelProperty("费项类型：1常规收费类型,2临时收费类型,3押金收费类型,4常规付费类型,5押金付费类型")
    private Integer type;

}
