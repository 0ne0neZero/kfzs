package com.wishare.finance.apps.model.invoice.invoice.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author xujian
 * @date 2022/12/16
 * @Description:
 */
@Getter
@Setter
@ApiModel("编辑收据")
public class EditReceiptF {

    @ApiModelProperty(value = "收据主表id", required = true)
    @NotNull(message = "收据主表id不能为空")
    private Long invoiceReceiptId;

    @Valid
    @ApiModelProperty(value = "票据明细表编辑对象", required = true)
    @NotNull(message = "票据明细表编辑对象不能为空")
    private List<EditInvoiceReceiptDetailF> editInvoiceReceiptDetailFList;
}
