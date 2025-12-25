package com.wishare.finance.infrastructure.remote.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 商品信息
 *
 * @Author dxclay
 * @Date 2022/12/1
 * @Version 1.0
 */
@Getter
@Setter
public class Goods {

    /**
     * 商品id
     */
    private String goodsId;
    /**
     * 渠道统一商品编号
     */
    private String channelGoodsId;
    /**
     * 商品单价，单位：分
     */
    private Long price;
    /**
     * 商品名称
     */
    private String goodsName;
    /**
     * 商品类目标识
     */
    private String goodsCategoryId;
    /**
     * 商品类目名称
     */
    private String goodsCategory;
    /**
     * 数量
     */
    private int quantity;
    /**
     * 商品展示地址
     */
    private String imgUrls;

    /**
     * 商品说明
     */
    private String description;

}
