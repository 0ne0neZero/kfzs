package com.wishare.finance.apps.model.configure.chargeitem.vo;

import com.wishare.starter.beans.Tree;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 费项树返回数据
 *
 * @author yancao
 */
@Getter
@Setter
@ApiModel("费项树返回数据")
public class ChargeItemTreeV extends Tree<ChargeItemTreeV, Long> {

    @ApiModelProperty("费项名称")
    private String name;

    @ApiModelProperty("费项编码")
    private String code;

    @ApiModelProperty("费项属性")
    private Integer attribute;

    @ApiModelProperty("费项类型")
    private Integer type;

}
