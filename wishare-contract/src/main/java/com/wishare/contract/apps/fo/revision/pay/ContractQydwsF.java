package com.wishare.contract.apps.fo.revision.pay;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @description:
 * @author: zhangfuyu
 * @Date: 2024/3/1/11:05
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class ContractQydwsF {
    @ApiModelProperty("签约单位名称")
    private String oppositeOne;

    @ApiModelProperty("签约单位名称id")
    private String oppositeOneId;

    @ApiModelProperty("是否内部往来单位")
    private Integer isInner;

    @ApiModelProperty("签约单位名称id")
    private List<ContractQydwsExtendF> oppositeTwoIdName;

}
