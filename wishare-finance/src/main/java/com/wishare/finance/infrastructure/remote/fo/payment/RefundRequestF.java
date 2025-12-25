package com.wishare.finance.infrastructure.remote.fo.payment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 退款接口 统一入参
 */
@Getter
@Setter
@ApiModel("退款接口信息")
@Accessors(chain = true)
public class RefundRequestF {

    @ApiModelProperty(value = "支付单号")
    private String payNo;

    @ApiModelProperty(value = "支付来源")
    private Integer paySource;

    @ApiModelProperty(value = "渠道订单号")
    private String channelOrderNo;

    @ApiModelProperty(value = "商户退款单号")
    @NotNull(message = "商户退款单号不能为空")
    private String mchRefundNo;

    @ApiModelProperty(value = "退款金额 单位:分")
    @NotNull(message = "退款金额不能为空")
    @Max(value = 1000000000, message = "退款金额格式不正确, 区间为[1, 1000000000]")
    @Min(value = 1, message = "退款金额格式不正确, 区间为[1, 1000000000]")
    private Long refundAmount;

    @ApiModelProperty(value = "退款原因")
    private String refundReason;

    @ApiModelProperty(value = "场景信息")
    private SceneF scene;

    @ApiModelProperty(value = "商户号")
    @Length(max = 40, message = "商户号格式不正确")
    private String mchNo;

    @ApiModelProperty(value = "系统来源 1收费系统，2合同系统")
    @NotNull(message = "系统来源不能为空")
    private Integer sysSource;

    @ApiModelProperty(value = "支付参数（用于特殊支付场景自定义的参数）")
    @Length(max = 256, message = "支付参数格式不正确")
    private PayParamF payParam;

    @ApiModelProperty(value = "服务商应用id")
    @Length(max = 40, message = "服务商号格式不正确")
    private String spAppId;

    @ApiModelProperty(value = "应用id")
    @Length(max = 40, message = "应用id格式不正确")
    private String appId;

    @ApiModelProperty(value = "支付接口名称")
    private String method;

    @ApiModelProperty(value = "关联业务id")
    private String businessId;
}
