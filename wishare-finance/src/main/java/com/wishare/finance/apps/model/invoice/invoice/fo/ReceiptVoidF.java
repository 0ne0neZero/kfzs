package com.wishare.finance.apps.model.invoice.invoice.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * 目前场景：中交e签宝作废
 * @see com.wishare.finance.domains.invoicereceipt.entity.invoicing.ReceiptE;
 *
 * @see com.wishare.finance.apps.model.invoice.invoice.fo.ReceiptVoidF;
 */
@Getter
@Setter
@ApiModel("外部签约的收据作废")
public class ReceiptVoidF {


    @ApiModelProperty(value = "收据主表id")
    @NotNull(message = "invoiceReceiptId")
    private Long invoiceReceiptId;

//    @ApiModelProperty(value = "上级收费单元id不能为空")
//    @NotNull(message = "supCpUnitId")
//    String supCpUnitId;


}
