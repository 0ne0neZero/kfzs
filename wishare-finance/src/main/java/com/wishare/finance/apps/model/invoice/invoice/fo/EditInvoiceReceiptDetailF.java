package com.wishare.finance.apps.model.invoice.invoice.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * @author xujian
 * @date 2022/12/16
 * @Description:
 */
@Getter
@Setter
@ApiModel("编辑票据明细表")
public class EditInvoiceReceiptDetailF {

    @ApiModelProperty(value = "票据明细表id",required = true)
    @NotNull(message = "票据明细表id不能为空")
    private Long invoiceReceiptDetailId;

    @ApiModelProperty("票据明细表备注")
    private String remark;

    @ApiModelProperty("方圆套打收据专用备注")
    private String remarkNew;
}
