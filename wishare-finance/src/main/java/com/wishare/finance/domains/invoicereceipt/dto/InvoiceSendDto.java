package com.wishare.finance.domains.invoicereceipt.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 票据推送结果
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/3/26
 */
@Getter
@Setter
@AllArgsConstructor
@ApiModel("票据推送结果")
public class InvoiceSendDto {

    @ApiModelProperty("推送方式：-1,不推送,0,邮箱;1,手机;2,站内信")
    private Integer pushMode;

    @ApiModelProperty("推送结果")
    private Boolean result;

}
