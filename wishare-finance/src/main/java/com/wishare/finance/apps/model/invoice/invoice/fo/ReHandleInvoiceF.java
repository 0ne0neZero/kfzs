package com.wishare.finance.apps.model.invoice.invoice.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author luzhonghe
 * @version 1.0
 * @since 2023/8/2
 */
@Setter
@Getter
@ApiModel("重新处理更新发票状态入参")
public class ReHandleInvoiceF {

    @NotBlank
    @ApiModelProperty(value = "流水号")
    private String invoiceSerialNum;

}
