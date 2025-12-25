package com.wishare.finance.apps.model.configure.chargeitem.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 费项名称信息
 *
 * @author yancao
 */
@Getter
@Setter
@ApiModel("费项名称信息")
public class ChargeNameV {

    @ApiModelProperty("费项类型id")
    private Long id;

    @ApiModelProperty("费项名称")
    private String name;

    @ApiModelProperty("费项编码")
    private String code;

    @ApiModelProperty("费项状态：0不存在,1末级节点,2非末级节点")
    private Integer status;

    @ApiModelProperty("费项类型：1常规收费类型,2临时收费类型,3押金收费类型,4常规付费类型,5押金付费类型")
    private Integer type;

    @ApiModelProperty("费项属性： 1收入,2支出,3代收代付及其他")
    private Integer attribute;

    @ApiModelProperty("分成费项父id")
    private Long shareParentId;

}
