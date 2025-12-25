package com.wishare.finance.apps.model.invoice.invoice.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author xujian
 * @date 2022/10/14
 * @Description:
 */
@Getter
@Setter
@ApiModel("根据查询条件获取账单信息")
public class InvoiceListF {

    @ApiModelProperty("账单ids")
    private List<Long> billIds;

    @ApiModelProperty("发票收据主表ids")
    private List<Long> invoiceReceiptIds;

}
