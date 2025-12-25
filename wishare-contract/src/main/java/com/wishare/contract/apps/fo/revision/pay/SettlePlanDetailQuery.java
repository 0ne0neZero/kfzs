package com.wishare.contract.apps.fo.revision.pay;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author dengjie03
 * @Description
 * @Date 2024-11-13
 */
@Data
public class SettlePlanDetailQuery {

    @ApiModelProperty(value = "合同id")
    @NotNull(message = "合同id不可为空")
    private String contractId;

    @ApiModelProperty("来源：1:编辑 2:详情")
    private Integer source = 1;

}
