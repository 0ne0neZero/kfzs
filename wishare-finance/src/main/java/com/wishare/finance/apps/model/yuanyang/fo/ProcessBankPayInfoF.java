package com.wishare.finance.apps.model.yuanyang.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 支付方银行信息
 * @author luzhonghe
 * @version 1.0
 * @since 2023/8/24
 */
@Getter
@Setter
@ApiModel("支付方银行信息")
public class ProcessBankPayInfoF {

    @ApiModelProperty(value = "支付信息唯一标识id",required = true)
    @NotBlank(message = "支付信息唯一标识id不能为空")
    private String payId;

    @ApiModelProperty(value = "支付渠道（ALIPAY：支付宝，WECHATPAY:微信支付，CASH:现金，UNIONPAY:银联，BANK:银行汇款，CBS: 招商银企直连，OTHER: 其他）",required = true)
    @NotBlank(message = "支付渠道不能为空")
    private String payChannel;

    @ApiModelProperty(value = "支付方式(0线上，1线下)， 默认：线上",required = true)
    private Integer payWay = 0;

    @ApiModelProperty(value = "付款方式")
    private String payMethod;

    @ApiModelProperty(value = "是否银企直连户: 0:否,1:是", required = true)
    @NotNull(message = "是否银企直连户不能为空")
    private Integer bankEnterpriseDirect;

    @ApiModelProperty(value = "付款账号", required = true)
    private String payerAccount;

    @ApiModelProperty(value = "付款人名称", required = true)
    private String payerName;

    @ApiModelProperty(value = "付款金额", required = true)
    @NotNull(message = "付款金额不能为空")
    private Long payAmount;

}
