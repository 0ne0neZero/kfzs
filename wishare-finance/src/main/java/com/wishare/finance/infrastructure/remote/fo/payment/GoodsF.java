package com.wishare.finance.infrastructure.remote.fo.payment;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * 商品信息
 *
 * @Author dxclay
 * @Date 2022/12/1
 * @Version 1.0
 */
@Getter
@Setter
public class GoodsF {

    @ApiModelProperty(value = "商品id")
    @Length(max = 64, message = "商品id格式不正确")
    private String goodsId;

    @ApiModelProperty(value = "渠道统一商品编号")
    @Length(max = 64, message = "商品id格式不正确")
    private String channelGoodsId;

    @ApiModelProperty(value = "商品单价，单位：分")
    @Max(value = 1000000000, message = "商品单价格式不正确, 区间为[1, 1000000000]")
    @Min(value = 1, message = "商品单价格式不正确, 区间为[1, 1000000000]")
    private Long price;

    @ApiModelProperty(value = "商品名称")
    @Length(max = 64, message = "商品名称格式不正确")
    private String goodsName;

    @ApiModelProperty(value = "商品类目标识")
    @Length(max = 64, message = "商品类目标识格式不正确")
    private String goodsCategoryId;

    @ApiModelProperty(value = "商品类目名称")
    @Length(max = 100, message = "商品类目名称格式不正确")
    private String goodsCategory;

    @ApiModelProperty(value = "数量")
    private int quantity;

    @ApiModelProperty(value = "商品展示地址")
    @Length(max = 256, message = "商品展示地址不正确")
    private String imgUrls;



}
