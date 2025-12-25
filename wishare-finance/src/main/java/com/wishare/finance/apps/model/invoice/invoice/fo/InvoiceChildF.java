package com.wishare.finance.apps.model.invoice.invoice.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * @author xujian
 * @date 2022/12/3
 * @Description:
 */
@Getter
@Setter
@ApiModel("获取发票子表入参")
public class InvoiceChildF {

    @ApiModelProperty(value = "发票主表id", required = true)
    @NotNull(message = "发票主表id不能为空")
    private Long invoiceReceiptId;
}
