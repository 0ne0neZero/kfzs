package com.wishare.finance.apps.model.bill.fo;

import com.wishare.finance.domains.bill.entity.DiscountOBV;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 账单统一交易参数
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/2/20
 */
@Getter
@Setter
@ApiModel("账单统一收款参数")
public class BillGatherF {

    @ApiModelProperty(value = "商户订单号")
    @Length(max = 32, message = "商户订单号格式不正确")
    private String mchOrderNo;

    @ApiModelProperty(value = "商户订单标题")
    
    private String mchOrderTitle;

    @ApiModelProperty(value = "收款金额（单位：分）", required = true)
    @NotNull(message = "收款金额不能为空")
    private Long totalAmount;

    @ApiModelProperty(value = "减免说明列表")
    private List<DiscountOBV> discounts;

    @ApiModelProperty(value = "收款人信息")
    private PayeeF payee;

    @ApiModelProperty(value = "付款人信息")
    private PayerF payer;

    @ApiModelProperty(value = "请求超时时间 格式：yyyy-MM-dd HH:mm:ss，默认为15分钟")
    private LocalDateTime expireTime;

    @ApiModelProperty(value = "支付渠道（ALIPAY：支付宝，WECHATPAY:微信支付，CASH:现金，UNIONPAY:银联，BANK:银行汇款，CBS: 招商银企直连，OTHER: 其他）",required = true)
    @NotBlank(message = "支付渠道不能为空")
    private String payChannel;

    @ApiModelProperty(value = "支付方式(0线上，1线下)， 默认：线上",required = true)
    private Integer payWay = 0;

    @ApiModelProperty(value = "支付扩展信息，支付通知原样返回")
    @Length(max = 256, message = "支付扩展信息格式不正确")
    private String payAttachParam;

    @ApiModelProperty(value = "消息通知地址")
    @Length(max = 256, message = "消息通知地址格式不正确")
    private String notifyUrl;

    @ApiModelProperty(value = "页面跳转地址")
    @Length(max = 256, message = "页面跳转地址格式不正确")
    private String returnUrl;

    @ApiModelProperty(value = "系统来源 1收费系统，2合同系统，101亿家优选系统，102BPM系统", required = true)
    @NotBlank(message = "系统来源不能为空")
    private Integer sysSource;

    @ApiModelProperty(value = "是否推凭：0不推凭，1推凭 默认：0")
    private Integer pushVoucher = 0;

    @ApiModelProperty(value = "支付账单列表, 仅应收、临时账单，用于账单缴费场景")
    @Size(max = 200, min = 1, message = "账单列表大小不正确，大小区间为[1,500]")
    private List<BillGatherInfoF> payBills;

    @ApiModelProperty(value = "入账账单列表, 支付完成时可添加额外账单")
    private List<BillEnterInfoF> enterBills;

}
