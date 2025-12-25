package com.wishare.finance.domains.voucher.support.zhongjiao.zjpushorder;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 资金收款单 （现金流量）
 */
@Setter
@Getter
public class SKXJLLData {
    /**
     * 明细内码
     */
    @JsonProperty("XJLLMXNM")
    private String XJLLMXNM;

    /**
     * 单据内码
     */
    @JsonProperty("DJNM")
    private String DJNM;

    /**
     * 现金流量ID
     */
    @JsonProperty("XJLL")
    private String XJLL;

    /**
     * 金额（原币）
     */
    @JsonProperty("JEYB")
    private Double JEYB;

    /**
     * 原币币种
     */
    @JsonProperty("YBBZ")
    private String YBBZ;

    /**
     * 汇率
     */
    @JsonProperty("HL")
    private Double HL;

    /**
     * 金额（本币）
     */
    @JsonProperty("JEBB")
    private Double JEBB;
}
