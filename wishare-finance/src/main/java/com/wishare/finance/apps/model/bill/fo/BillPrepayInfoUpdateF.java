package com.wishare.finance.apps.model.bill.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * @author ℳ๓采韵
 * @project wishare-finance
 * @title BillPrepayInfoUpdateF
 * @date 2023.11.08  17:01
 * @description 账单预支付状态变更
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@ApiModel("账单预支付状态变更")
public class BillPrepayInfoUpdateF extends BillPrepayInfoF{

    @ApiModelProperty("支付状态不能为空")
    @NotNull(message = "支付状态不能为空")
    private Integer payState;

    @ApiModelProperty("支付失败原因")
    private String reason;

    @ApiModelProperty("关联二维码url")
    private String qrCodeUrl;
}
