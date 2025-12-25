package com.wishare.contract.apps.remote.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("发票收据明细")
public class InvoiceReceiptDetailRv {

    @ApiModelProperty("金额单位")
    private String amountUnit = "分";

    @ApiModelProperty("账单id")
    private Long billId;

    @ApiModelProperty("账单编号")
    private String billNo;

    @ApiModelProperty("账单类型")
    private Integer billType;

    @ApiModelProperty("费项名称")
    private String chargeItemName;

    @ApiModelProperty("单价（单位：分）")
    private Long price;

    @ApiModelProperty("数量")
    private String num;

    @ApiModelProperty("数量描述（计量）")
    private String numStr;

    @ApiModelProperty("开票金额（单位：分）")
    private Long invoiceAmount;

    @ApiModelProperty("账单的原始结算金额（单位：分）")
    private Long settleAmount;

    @ApiModelProperty("备注")
    private String remark;
}
