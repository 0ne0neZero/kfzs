package com.wishare.finance.apps.model.invoice.invoice.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * 收票详情信息
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/3/2
 */
@Getter
@Setter
@ApiModel("收票详情信息")
public class InvoiceCollectionDetailV {

    @ApiModelProperty(value = "发票号码",required = true)
    @NotBlank(message = "发票号码不能为空")
    private String invoiceNo;

    @ApiModelProperty(value = "收票状态： true成功， false失败")
    private boolean collected;

    @ApiModelProperty(value = "失败原因")
    private String errorReason;

}
