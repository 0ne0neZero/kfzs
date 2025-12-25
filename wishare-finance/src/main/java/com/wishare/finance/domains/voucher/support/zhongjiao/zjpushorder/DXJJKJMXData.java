package com.wishare.finance.domains.voucher.support.zhongjiao.zjpushorder;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 对下结算的 DXJJKJMX(扣减明细/款项明细)说明
 */
@Setter
@Getter
public class DXJJKJMXData {
    /**
     * 扣减明细内码
     */
    @JsonProperty("KJMXNM")
    private String KJMXNM;

    /**
     * 变动
     */
    @JsonProperty("BD")
    private String BD;


    /**
     * 结算内码
     */
    @JsonProperty("JSNM")
    private String JSNM;

    /**
     * 款项ID
     */
    @JsonProperty("KJXM")
    private String KJXM;

    /**
     * 应收应付ID
     */
    @JsonProperty("YSYFBH")
    private String YSYFBH;


    /**
     * 原币ID
     */
    @JsonProperty("YBID")
    private String YBID;


    /**
     * 汇率
     */
    @JsonProperty("HL")
    private Double HL;

    /**
     * 金额（原币）
     */
    @JsonProperty("KJJEYB")
    private String KJJEYB;


    /**
     * 金额（本位币）
     */
    @JsonProperty("KJJEBB")
    private String KJJEBB;

    /**
     * 到期日期
     */
    @JsonProperty("DQRQ")
    private String DQRQ;

    /**
     * 预计收付款日期
     */
    @JsonProperty("SFKRQ")
    private String SFKRQ;


    /**
     * 专项项目ID
     */
    @JsonProperty("ZXXMID")
    private String ZXXMID;
}
