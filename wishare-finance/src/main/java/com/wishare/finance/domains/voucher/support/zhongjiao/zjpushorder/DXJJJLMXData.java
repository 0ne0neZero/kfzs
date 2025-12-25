package com.wishare.finance.domains.voucher.support.zhongjiao.zjpushorder;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 对下结算的 DXJJJLMX（计量明细）说明
 */
@Setter
@Getter
public class DXJJJLMXData {
    /**
     * 计量明细内码
     */
    @JsonProperty("JLMXNM")
    private String JLMXNM;

    /**
     * 结算内码
     */
    @JsonProperty("JSNM")
    private String JSNM;

    /**
     * 清单项目
     */
    @JsonProperty("QDXM")
    private String QDXM;

    /**
     * 数量
     */
    @JsonProperty("ACOUNT")
    private Integer ACOUNT;

    /**
     * 单价（原币）
     */
    @JsonProperty("DJBB")
    private Double DJBB;

    /**
     * 原币
     */
    @JsonProperty("BZID")
    private String BZID;

    /**
     * 汇率
     */
    @JsonProperty("JLHL")
    private Double JLHL;

    /**
     * 含税金额（原币）
     */
    @JsonProperty("HSJEYB")
    private Double HSJEYB;

    /**
     * 含税金额（本币）
     */
    @JsonProperty("HSJEBB")
    private Double HSJEBB;

    /**
     * 税率
     */
    @JsonProperty("SL")
    private Double SL;

    /**
     * 税额（原币）
     */
    @JsonProperty("SEYB")
    private Double SEYB;

    /**
     * 税额（本位币）
     */
    @JsonProperty("SEBB")
    private Double SEBB;

    /**
     * 不含税金额（原币）
     */
    @JsonProperty("BHSYB")
    private Double BHSYB;

    /**
     * 不含税金额（本位币）
     */
    @JsonProperty("BHSBB")
    private Double BHSBB;

    /**
     * WBS名称
     */
    @JsonProperty("WBSMC")
    private String WBSMC;

}
