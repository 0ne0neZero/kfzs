package com.wishare.finance.apps.model.invoice.nuonuo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author xujian
 * @date 2022/8/9
 * @Description:
 */
@Getter
@Setter
@ApiModel("发票作废接口反参")
public class InvoiceCancellationV {

    @ApiModelProperty("发票流水号(提交成功则返回发票请求流水号)")
    private String invoiceId;
}
