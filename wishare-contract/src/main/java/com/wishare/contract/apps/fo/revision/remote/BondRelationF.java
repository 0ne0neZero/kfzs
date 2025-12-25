package com.wishare.contract.apps.fo.revision.remote;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @version 1.0.0
 * @Description：
 * @Author： chentian
 * @since： 2023/7/11  15:25
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "BondRelationF", description = "BondRelationF")
public class BondRelationF {

    /**
     * 客户ID  或  供应商ID
     */
    @ApiModelProperty(value = "业务ID (客户ID 或 供应商ID)", required = true)
    private String id;

    /**
     * 合同名称 非必传
     */
    @ApiModelProperty("合同名称")
    private String name;

}
