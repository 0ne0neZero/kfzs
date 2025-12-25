package com.wishare.finance.infrastructure.remote.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 交易通知信息
 *
 * @Author dxclay
 * @Date 2022/12/26
 * @Version 1.0
 */
@Getter
@Setter
public class TransactionNotifyBody {

    /**
     * 支付单号
     */
    private String payNo;

    /**
     * 商户号
     */
    private String mchNo;

    /**
     * 支付金额
     */
    private Long amount;

    /**
     * 币种编码
     */
    private String currency;

    /**
     * 商户订单号
     */
    private String mchOrderNo;

    /**
     * 支付渠道订单号
     */
    private String channelOrderNo;

    /**
     * 支付状态: 0待支付, 1支付中, 2支付成功, 3支付失败, 4已撤销 5退款中, 6部分退款, 7已退款, 8已关闭
     */
    private Integer state;

    /**
     * 支付接口
     */
    private String method;

    /**
     * 订单标题
     */
    private String orderTitle;

    /**
     * 商品详情" +
     * "[{\"goodsId\": \"商品id\"," +
     * "\"channelGoodsId\": \"渠道统一商品编号\"," +
     * "\"price\": \"商品单价，单位：分\"," +
     * "\"goodsName\": \"商品名称\"," +
     * "\"goodsCategoryId\": \"商品类目标识\"," +
     * "\"goodsCategory\": \"商品类目名称\"," +
     * "\"quantity\": \"数量\"," +
     * "\"imgUrls\": [\"商品展示地址\"]" +
     * "}]
     */
    private List<Goods> goods;

    /**
     * 付款人信息 " +
     * "{\"payerId\": \"付款人id\"," +
     * "\"payerName\": \"付款人名称\"," +
     * "\"phone\": \"付款人手机号\"}
     */
    private Payer payer;

    /**
     * 支付场景信息" +
     * "{\"clientIp\": \"支付客户端ip\"," +
     * "\"deviceId\": \"设备id\"}
     */
    private Scene scene;

    /**
     * 支付成功时间
     */
    private LocalDateTime successTime;

    /**
     * 支付单关闭时间
     */
    private LocalDateTime closeTime;

    /**
     * 扩展信息
     */
    private String attachParam;

    /**
     * 支付参数（用于特殊支付场景自定义的参数）
     */
    private String payParams;


}
