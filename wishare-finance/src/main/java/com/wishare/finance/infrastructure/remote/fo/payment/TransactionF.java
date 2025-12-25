package com.wishare.finance.infrastructure.remote.fo.payment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 支付下单信息
 * @Author dxclay
 * @Date 2022/12/1
 * @Version 1.0
 */
@Getter
@Setter
@ApiModel("支付下单信息")
public class TransactionF {

    @ApiModelProperty(value = "支付金额 单位:分")
    @NotNull(message = "商品详情不能为空")
    @Max(value = 1000000000, message = "商品单价格式不正确, 区间为[1, 1000000000]")
    @Min(value = 1, message = "商品单价格式不正确, 区间为[1, 1000000000]")
    private Long amount;

    @ApiModelProperty(value = "币种编码 默认：CNY（人民币）")
    @Length(max = 10, message = "币种编码格式不正确")
    private String currency;

    @ApiModelProperty(value = "商户订单号")
    @Length(max = 32, message = "商户订单号格式不正确")
    private String mchOrderNo;

    @ApiModelProperty(value = "订单标题")
    @Length(max = 128, message = "订单标题格式不正确")
    private String orderTitle;

    @ApiModelProperty(value = "商品详情列表")
    @NotNull(message = "商品详情不能为空")
    @Valid
    private List<GoodsF> goods;

    @ApiModelProperty(value = "付款人信息")
    @NotNull(message = "付款人信息不能为空")
    @Valid
    private PayerF payer;

    @ApiModelProperty(value = "收款人信息，银行转账或银企直连时不能为空")
    @Valid
    private PayeeF payee;

    @ApiModelProperty(value = "支付场景信息")
    @Valid
    private SceneF scene;

    @ApiModelProperty(value = "消息通知地址")
    @Length(max = 256, message = "消息通知地址格式不正确")
    private String notifyUrl;

    @ApiModelProperty(value = "页面跳转地址")
    @Length(max = 256, message = "页面跳转地址格式不正确")
    private String returnUrl;

    @ApiModelProperty(value = "支付超时时间 格式：yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expireTime;

    @ApiModelProperty(value = "扩展信息")
    @Length(max = 256, message = "扩展信息格式不正确")
    private String attachParam;

    @ApiModelProperty(value = "支付参数（用于特殊支付场景自定义的参数）")
    @Length(max = 256, message = "支付参数格式不正确")
    private PayParamF payParam;

    @ApiModelProperty(value = "系统来源 1收费系统，2合同系统")
    @NotNull(message = "系统来源不能为空")
    private Integer sysSource;

    @ApiModelProperty(value = "服务商应用id")
    @Length(max = 40, message = "服务商号格式不正确")
    private String spAppId;

    @ApiModelProperty(value = "应用id")
    @Length(max = 40, message = "应用id格式不正确")
    private String appId;


    @ApiModelProperty(value = "商户号")
    @Length(max = 40, message = "商户号格式不正确")
    private String mchNo;

    @ApiModelProperty(value = "支付接口名称")
    private String method;

}
