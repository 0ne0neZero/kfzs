package com.wishare.finance.domains.voucher.support.zhongjiao.zjpushorder;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * 对下结算 的 DXJJCBMX（成本明细）说明
 */
@Setter
@Getter
public class DXJJCBMXData {

    /**
     * 成本明细内码
     */
    @JsonProperty("CBMXNM")
    private String CBMXNM;

    /**
     * 结算内码
     */
    @JsonProperty("JSNM")
    private String JSNM;

    /**
     * 业务科目ID
     */

    @JsonProperty("YWKM")
    private String YWKM;

    /**
     * 专项项目
     */
    @JsonProperty("ZXXM")
    private String ZXXM;

    /**
     * 增减变动
     */
    @JsonProperty("ZJBD")
    private String ZJBD;

    /**
     * 原币币种
     */
    @JsonProperty("YBID")
    private String YBID;

    /**
     * 汇率
     */
    @JsonProperty("HL")
    private Double HL;

    /**
     * 价税合计（原币）
     */
    @JsonProperty("JSHJYB")
    private BigDecimal JSHJYB;

    /**
     * 价税合计（本位币）
     */
    @JsonProperty("JSHJBB")
    private BigDecimal JSHJBB;

    /**
     * 可抵扣税额(原币)
     */
    @JsonProperty("KDKSEYB")
    private BigDecimal KDKSEYB;

    /**
     *可抵扣税额(本位币)
     */
    @JsonProperty("KDKSEBB")
    private BigDecimal KDKSEBB;

    /**
     * 不含税金额（原币）
     */
    @JsonProperty("BHSJEYB")
    private Double BHSJEYB;

    /**
     * 不含税金额（本币）
     */
    @JsonProperty("BHSJEBB")
    private Double BHSJEBB;

    /**
     * 预算科目ID
     */
    @JsonProperty("YSKM")
    private String YSKM;
}
