package com.wishare.finance.apps.model.invoice.nuonuo.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author xujian
 * @date 2022/8/9
 * @Description:
 */
@Getter
@Setter
@ApiModel("发票作废接口")
public class InvoiceCancellationF {

    @ApiModelProperty(value = "企业税号",required = true)
    @NotBlank(message = "企业税号不能为空")
    private String taxnum;

    @ApiModelProperty(value = "租户id",required = true)
    @NotBlank(message = "租户id不能为空")
    private String tenantId;

    @ApiModelProperty(value = "发票流水号",required = true)
    @NotBlank(message = "发票流水号不能为空")
    private String invoiceId;

    @ApiModelProperty(value = "发票代码",required = true)
    @NotBlank(message = "发票代码不能为空")
    private String invoiceCode;

    @ApiModelProperty(value = "发票号码",required = true)
    @NotBlank(message = "发票号码不能为空")
    private String invoiceNo;

}
