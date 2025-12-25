package com.wishare.finance.apps.model.reconciliation.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 根据差额认领审核状态更新认领记录、流水状态
 *
 */
@ApiModel(value = "根据差额认领审核状态更新认领记录、流水状态入参")
@Setter
@Getter
public class UpdateFlowF {

    @ApiModelProperty("流水认领记录id")
    private Long id;

    @ApiModelProperty("审核标识 0 通过 1 拒绝")
    private Integer flag;

    @ApiModelProperty("审核意见")
    private String reviewComments;
}
