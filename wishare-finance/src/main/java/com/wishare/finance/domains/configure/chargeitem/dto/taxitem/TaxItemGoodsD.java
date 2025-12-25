package com.wishare.finance.domains.configure.chargeitem.dto.taxitem;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wishare.finance.infrastructure.configs.LongToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 税收商品dto
 *
 * @author yancao
 */
@Getter
@Setter
public class TaxItemGoodsD {

    @ApiModelProperty("税收商品id")
    @JsonSerialize(using = LongToStringSerializer.class)
    private Long id;

    @ApiModelProperty("税目编码")
    private String code;

    @ApiModelProperty("税目id")
    @JsonSerialize(using = LongToStringSerializer.class)
    private Long taxItemId;

    @ApiModelProperty("税目名称")
    private String name;

    @ApiModelProperty("商品类型（0：收费项目-默认）")
    private Integer type;

    @ApiModelProperty("是否商品定义（0：默认商品，1：自定义商品）")
    private Integer goodsFlag;

    @ApiModelProperty("商品名称")
    private String goodsName;

    @ApiModelProperty("更新人")
    private String operatorName;

    @ApiModelProperty("更新时间")
    private LocalDateTime gmtModify;

    @ApiModelProperty("费项id")
    private Long chargeItemId;

    @ApiModelProperty("关联的费项")
    private List<TaxChargeItemRelationD> chargeItemList;

}
