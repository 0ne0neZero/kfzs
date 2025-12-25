package com.wishare.finance.apps.model.configure.chargeitem.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 费项树返回数据
 *
 * @author yancao
 */
@Getter
@Setter
@ApiModel("费项树查询参数")
public class ChargeItemTreeF {

    @ApiModelProperty("费项名称")
    private String name;

    @ApiModelProperty("费项编码")
    private String code;

    @ApiModelProperty("费项属性")
    private List<Integer> attribute;

    @ApiModelProperty("费项类型")
    private List<Integer> type;

}
