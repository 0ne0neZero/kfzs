package com.wishare.finance.apps.model.invoice.nuonuo.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author dongpeng
 * @date 2023/8/22 16:24
 */
@ApiModel("开票回调数据")
@Data
public class InvoiceCallbackF {

    @ApiModelProperty(value = "接口标识：callback")
    private String operater;

    @ApiModelProperty(value = "订单编号")
    private String orderno;

    @ApiModelProperty(value = "开票信息")
    private String content;
}
