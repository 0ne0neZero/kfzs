package com.wishare.finance.domains.bill.command;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * @author xujian
 * @date 2022/10/11
 * @Description:
 */
@Getter
@Setter
@ApiModel("完成开票command")
public class FinishInvoiceCommand {

    @ApiModelProperty(value = "账单id",required = true)
    @NotNull(message = "账单id不能为空")
    private Long billId;

    @ApiModelProperty(value = "票据id",required = true)
    private Long invoiceReceiptId;

    @ApiModelProperty(value = "开票金额（单位：分）", required = true)
    @NotNull(message = "开票金额不能为空")
    private Long invoiceAmount;

    @ApiModelProperty("开票状态:true 成功 false 失败")
    private Boolean success;

    @ApiModelProperty(value = "上级收费单元id")
    private String supCpUnitId;

    @ApiModelProperty(value = "收款单id")
    private Long gatherBillId;

    @ApiModelProperty(value = "收款单明细id")
    private Long gatherDetailId;

    @ApiModelProperty(value = "账单类型 1-应收账单， 2-预收账单， 3-临时收费账单， 4-应付账单")
    private Integer billType;
}
