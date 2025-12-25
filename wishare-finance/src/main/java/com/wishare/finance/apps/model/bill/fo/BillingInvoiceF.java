package com.wishare.finance.apps.model.bill.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * @author xujian
 * @date 2022/9/26
 * @Description:
 */
@Getter
@Setter
@ApiModel("开票状态变更入参")
public class BillingInvoiceF {

    @ApiModelProperty(value = "账单id", required = true)
    @NotNull(message = "账单id不能为空")
    private Long billId;

    @ApiModelProperty(value = "开票金额（单位：分）", required = true)
    @NotNull(message = "开票金额不能为空")
    private Long invoiceAmount;

    @ApiModelProperty("开票状态：1开票中")
    private Integer invoiceState;
}
