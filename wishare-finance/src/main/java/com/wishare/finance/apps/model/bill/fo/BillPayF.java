package com.wishare.finance.apps.model.bill.fo;

import com.wishare.finance.domains.bill.command.UnitaryEnterBillCommand;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

/**
 * 账单统一付款参数
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/2/20
 */
@Getter
@Setter
@ApiModel("账单统一收付参数")
public class BillPayF {

    @ApiModelProperty(value = "业务系统交易单号")
    @Length(max = 64, message = "业务系统交易单号格式不正确")
    private String bizTransactionNo;

    @ApiModelProperty(value = "商户订单标题")
    
    private String transactionTitle;

    @ApiModelProperty(value = "付款金额（单位：分）", required = true)
    @NotNull(message = "付款金额不能为空")
    private Long amount;

    //@ApiModelProperty(value = "减免说明列表")
    //private List<DiscountOBV> discounts;

    @ApiModelProperty(value = "收款人信息")
    @Valid
    private PayeeF payee;

    @ApiModelProperty(value = "付款人信息")
    @Valid
    private PayerF payer;

    @ApiModelProperty(value = "支付场景信息")
    @Valid
    private SceneF scene;

    @ApiModelProperty(value = "请求超时时间 格式：yyyy-MM-dd HH:mm:ss，默认为15分钟")
    private LocalDateTime expireTime;

    @ApiModelProperty(value = "支付渠道（ALIPAY：支付宝，WECHATPAY:微信支付，CASH:现金，UNIONPAY:银联，BANK:银行汇款，CBS: 招商银企直连，OTHER: 其他）",required = true)
    @NotBlank(message = "支付渠道不能为空")
    private String payChannel;

    @ApiModelProperty(value = "支付方式(0线上，1线下)， 默认：线上",required = true)
    private Integer payWay = 0;

    @ApiModelProperty(value = "支付扩展信息，支付通知原样返回")
    @Length(max = 256, message = "支付扩展信息格式不正确")
    private String attachParam;

    @ApiModelProperty(value = "支付信息")
    private String payParam;

    @ApiModelProperty(value = "消息通知地址")
    //@Pattern(regexp = "/^http(s)?:\\\\/\\\\/.+/", message = "消息通知地址格式不正确")
    @Length(max = 256, message = "消息通知地址格式不正确")
    private String notifyUrl;

    @ApiModelProperty(value = "页面跳转地址")
    @Length(max = 256, message = "页面跳转地址格式不正确")
    private String returnUrl;

    @ApiModelProperty(value = "系统来源 1收费系统，2合同系统，101亿家优选系统，102BPM系统", required = true)
    @NotNull(message = "系统来源不能为空")
    private Integer sysSource;

    @ApiModelProperty(value = "是否推凭：0不推凭，1推凭 默认：不推凭")
    private Integer voucherFlag = 0;

    @ApiModelProperty(value = "入账信息", required = true)
    @NotNull(message = "入账信息不能为空")
    @Valid
    private UnitaryEnterBillCommand billInfo;

}
