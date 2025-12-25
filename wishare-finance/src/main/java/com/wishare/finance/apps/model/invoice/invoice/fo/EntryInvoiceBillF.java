package com.wishare.finance.apps.model.invoice.invoice.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * @author xujian
 * @date 2022/12/1
 * @Description:
 */
@Getter
@Setter
@ApiModel("进项发票账单信息")
public class EntryInvoiceBillF {

    @ApiModelProperty(value = "账单id",required = true)
    @NotNull(message = "账单id不能为空")
    private Long billId;

    @ApiModelProperty(value = "账单类型：1.应收账单 2.预收账单 3.临时缴费账单 4.应付账单",required = true)
    @NotNull(message = "账单类型不能为空")
    private Integer billType;

    @ApiModelProperty(value = "账单开票金额")
    private Long invoiceAmount;

    @ApiModelProperty("税额")
    private Long taxAmount;
}
