package com.wishare.finance.apps.model.invoice.invoice.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author xujian
 * @date 2022/10/13
 * @Description:
 */
@Getter
@Setter
@ApiModel("开具收据下发")
public class ReceiptSendF {


    @ApiModelProperty(value = "收据主表id")
    @NotNull(message = "invoiceReceiptId")
    private Long invoiceReceiptId;

    @ApiModelProperty(value = "推送方式：-1,不推送,0,邮箱;1,手机")
    @NotNull(message = "推送方式不能为空")
    private List<Integer> pushMode;

    @ApiModelProperty(value = "购方手机（pushMode为1时，此项为必填）")
    private String buyerPhone;

    @ApiModelProperty(value = "推送邮箱（pushMode为0时，此项为必填）")
    private String email;

}
