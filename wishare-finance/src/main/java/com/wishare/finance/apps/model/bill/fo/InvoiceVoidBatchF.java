package com.wishare.finance.apps.model.bill.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * @author xujian
 * @date 2022/10/24
 * @Description:
 */
@Getter
@Setter
@ApiModel("批量作废开票金额")
public class InvoiceVoidBatchF {

    @ApiModelProperty(value = "账单id",required = true)
    @NotNull(message = "账单id不能为空")
    private Long billId;

    @ApiModelProperty(value = "上级收费单元id")
    @NotBlank(message = "上级收费单元id不能为空")
    private String supCpUnitId;

    @ApiModelProperty(value = "票据id",required = true)
    private Long invoiceReceiptId;

    @ApiModelProperty(value = "作废，红冲发票金额（单位：分）", required = true)
    @NotNull(message = "作废，红冲发票金额不能为空")
    private Long invoiceAmount;

    @ApiModelProperty(value = "收款单id")
    private Long gatherBillId;

    @ApiModelProperty(value = "收款单明细id")
    private Long gatherDetailId;

    @ApiModelProperty(value = "账单类型 1-应收账单， 2-预收账单， 3-临时收费账单， 4-应付账单")
    private Integer billType;
}
