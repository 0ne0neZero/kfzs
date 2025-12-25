package com.wishare.finance.apps.model.bill.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * 差额退款表单
 *
 * @Author dxclay
 * @Date 2023/1/11
 * @Version 1.0
 */
@Setter
@Getter
@ApiModel("差额退款表单")
public class DifferenceRefundF {

    @ApiModelProperty(value = "账单id", required = true)
    @NotNull(message = "不能为空")
    private Long billId;

    @ApiModelProperty(value = "账单类型 1-应收账单， 2-预收账单， 3-临时收费账单， 4-应付账单", required = true)
    @NotNull(message = "账单类型不能为空")
    private Integer billType;

    @ApiModelProperty(value = "上级收费单元id")
    private String supCpUnitId;

    @ApiModelProperty(value = "退款金额", required = true)
    @NotNull(message = "退款金额不能为空")
    private Long refundAmount;

    @ApiModelProperty(value = "退款原因")
    private String refundReason;

}
