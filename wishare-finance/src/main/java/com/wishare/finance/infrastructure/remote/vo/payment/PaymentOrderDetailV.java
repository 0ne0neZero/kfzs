package com.wishare.finance.infrastructure.remote.vo.payment;

import java.time.LocalDateTime;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 支付单详情
 *
 * @author dxclay
 * @since 2022-11-21
 */
@Getter
@Setter
@ApiModel("支付单详情")
public class PaymentOrderDetailV {

    @ApiModelProperty(value = "支付单id")
    private Long id;

    @ApiModelProperty(value = "支付单号")
    private String payNo;

    @ApiModelProperty(value = "商户号")
    private String mchNo;

    @ApiModelProperty(value = "支付金额")
    private Long amount;

    @ApiModelProperty(value = "币种编码")
    private String currency;

    @ApiModelProperty(value = "商户订单号")
    private String mchOrderNo;

    @ApiModelProperty(value = "渠道商户号")
    private String channelMchNo;

    @ApiModelProperty(value = "支付渠道订单号")
    private String channelOrderNo;

    @ApiModelProperty(value = "支付状态: 0待支付, 1支付中, 2支付成功, 3支付失败, 4已撤销 5退款中, 6部分退款, 7已退款, 8已关闭")
    private Integer state;

    @ApiModelProperty(value = "支付接口")
    private String method;

    @ApiModelProperty(value = "订单标题")
    private String orderTitle;

    @ApiModelProperty(value = "商品详情" +
            "[{\"goodsId\": \"商品id\"," +
            "\"channelGoodsId\": \"渠道统一商品编号\"," +
            "\"price\": \"商品单价，单位：分\"," +
            "\"goodsName\": \"商品名称\"," +
            "\"goodsCategoryId\": \"商品类目标识\"," +
            "\"goodsCategory\": \"商品类目名称\"," +
            "\"quantity\": \"数量\"," +
            "\"imgUrls\": [\"商品展示地址\"]" +
            "}]")
    private List<Goods> goods;

    @ApiModelProperty(value = "付款人信息 " +
            "{\"payerId\": \"付款人id\"," +
            "\"payerName\": \"付款人名称\"," +
            "\"phone\": \"付款人手机号\"}")
    private Payer payer;

    @ApiModelProperty(value = "支付场景信息" + "{\"clientIp\": \"支付客户端ip\"," + "\"deviceId\": \"设备id\"}")
    private Scene scene;

    @ApiModelProperty(value = "消息通知地址")
    private String notifyUrl;

    @ApiModelProperty(value = "页面跳转地址")
    private String returnUrl;

    @ApiModelProperty(value = "支付超时时间")
    private LocalDateTime expireTime;

    @ApiModelProperty(value = "支付成功时间")
    private LocalDateTime successTime;

    @ApiModelProperty(value = "支付单关闭时间")
    private LocalDateTime closeTime;

    @ApiModelProperty(value = "退款成功次数")
    private Integer refundTimes;

    @ApiModelProperty(value = "退款成功额度")
    private Long refundAmount;

    @ApiModelProperty(value = "扩展信息")
    private String attachParam;

    @ApiModelProperty(value = "支付参数（用于特殊支付场景自定义的参数）")
    private String payParams;

    @ApiModelProperty(value = "支付应用id")
    private Long applicationId;

    @ApiModelProperty(value = "支付渠道id")
    private Long paymentChannelId;

    @ApiModelProperty(value = "是否删除:0未删除，1已删除")
    private Integer deleted;

    @ApiModelProperty(value = "创建人ID")
    private String creator;

    @ApiModelProperty(value = "创建人姓名")
    private String creatorName;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime gmtCreate;

    @ApiModelProperty(value = "操作人ID")
    private String operator;

    @ApiModelProperty(value = "修改人姓名")
    private String operatorName;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime gmtModify;

}
