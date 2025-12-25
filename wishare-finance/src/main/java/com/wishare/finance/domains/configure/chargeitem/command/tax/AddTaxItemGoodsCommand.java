package com.wishare.finance.domains.configure.chargeitem.command.tax;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 添加税目命令
 *
 * @author yancao
 */
@Getter
@Setter
public class AddTaxItemGoodsCommand {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 税收分类编码Id
     */
    private Long taxItemId;
    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品类型（0：收费项目-默认）
     */
    private Integer type;

    /**
     * 是否商品定义（0默认商品，1自定义商品）
     */
    private Integer goodsFlag;

    /**
     * 费项id
     */
    private List<Long> chargeItemIdList;


}
