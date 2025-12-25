package com.wishare.finance.apps.model.configure.chargeitem.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 修改税收商品信息
 *
 * @author yancao
 */
@Getter
@Setter
@ApiModel("修改税收商品信息")
public class UpdateTaxItemGoodsF {

    @ApiModelProperty("id")
    @NotNull(message = "id不能为空")
    private String id;

    @ApiModelProperty("税收分类编码Id")
    private Long taxItemId;

    @ApiModelProperty("商品名称")
    private String goodsName;

    @ApiModelProperty("是否商品定义（0默认商品，1自定义商品）")
    private Integer goodsFlag;

    @ApiModelProperty("商品类型（0：收费项目-默认）")
    private Integer type;

    @ApiModelProperty("关联的费项id")
    private List<Long> chargeItemIdList;

}
