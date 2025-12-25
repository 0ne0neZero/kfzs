package com.wishare.finance.apps.pushbill.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @Author dengjie03
 * @Description
 * @Date 2025-01-21
 */
@Data
@ApiModel("支付申请-财务初审核-通过或者驳回")
public class ApproveOrRejectF {

    @ApiModelProperty("支付申请单id")
    @NotEmpty(message = "支付申请单id不能为空")
    private String payApplicationFormId;

    @ApiModelProperty("审批类型 1:通过 2:驳回")
    @NotNull(message = "审批类型不能为空")
    private Integer type;

    @ApiModelProperty("审批备注")
    @NotEmpty(message = "审批备注不能为空")
    private String remark;


}
