package com.wishare.finance.domains.voucher.support.zhongjiao.zjpushorder;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 业务支付申请单下的 ZFMX(支付明细)
 */
@Setter
@Getter
public class PayZFMXData {
    /**
     * 支付明细内码
     */
    @JsonProperty("ZFMXNM")
    private String ZFMXNM;

    /**
     * 主表内码
     */
    @JsonProperty("ZBNM")
    private String ZBNM;

    /**
     * 收款单位
     */
    @JsonProperty("SKDW")
    private String SKDW;

    /**
     * 现金流量
     */
    @JsonProperty("XJLL")
    private String XJLL;

    /**
     * 支付方式
     */
    @JsonProperty("ZFFS")
    private String ZFFS;


    /**
     * 付款账户内码
     */
    @JsonProperty("FKZH")
    private String FKZH;


    /**
     * 支付币种
     */
    @JsonProperty("ZFBZ")
    private String ZFBZ;

    /**
     * 汇率
     */
    @JsonProperty("HL")
    private Double HL;

    /**
     * 支付金额本币
     */
    @JsonProperty("ZFBB")
    private Double ZFBB;

    /**
     * 转账附言
     */
    @JsonProperty("ZZFY")
    private String ZZFY;

    /**
     * 票据支付方式
     */
    @JsonProperty("PJZFFS")
    private Integer PJZFFS;

    /**
     * 票据数量
     */
    @JsonProperty("PJSL")
    private Integer PJSL;

    /**
     * 批准金额
     */
    @JsonProperty("PZJE")
    private Double PZJE;

    @JsonProperty("SKZH")
    private String SKZH;

}
