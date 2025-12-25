package com.wishare.finance.apps.model.invoice.invoice.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * @author xujian
 * @date 2022/10/14
 * @Description:
 */
@Getter
@Setter
@ApiModel("红票")
public class InvoiceBatchRedF {

    @ApiModelProperty(value = "发票主表id",required = true)
    @NotNull(message = "发票主表id不能为空")
    private Long invoiceReceiptId;

    @ApiModelProperty(value = "红字发票号码")
    private String invoiceReceiptNo;

    @ApiModelProperty(value = "红冲金额")
    private Long amount;

}
