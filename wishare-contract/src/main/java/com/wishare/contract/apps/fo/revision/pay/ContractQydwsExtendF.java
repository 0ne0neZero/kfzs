package com.wishare.contract.apps.fo.revision.pay;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @description:
 * @author: zhangfuyu
 * @Date: 2024/3/1/11:05
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class ContractQydwsExtendF {
    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("是否内部往来单位")
    private Integer isInner;



}
