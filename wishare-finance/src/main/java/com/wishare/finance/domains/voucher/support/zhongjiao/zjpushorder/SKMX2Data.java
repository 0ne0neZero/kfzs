package com.wishare.finance.domains.voucher.support.zhongjiao.zjpushorder;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 资金收款单 （应收款明细）说明
 */
@Setter
@Getter
public class SKMX2Data {

    /**
     * 应收明细内码
     */
    @JsonProperty("YSMXNM")
    private String YSMXNM;

    /**
     * 单据内码
     */
    @JsonProperty("DJNM")
    private String DJNM;

    /**
     * 核销应收编号
     */
    @JsonProperty("HXYSBH")
    private String HXYSBH;

    /**
     * 未核销金额（原币）
     */
    @JsonProperty("WHXJEYB")
    private String WHXJEYB;


    /**
     * 员工
     */
    @JsonProperty("YG")
    private String YG;

    /**
     * 资金计划
     */
    @JsonProperty("ZJJH")
    private String ZJJH;
    /**
     * 价税合计（本币）
     */
    @JsonProperty("JSHJBB")
    private Double JSHJBB;

    /**
     * 价税合计（原币）
     */
    @JsonProperty("JSHJYB")
    private Double JSHJYB;

    /**
     * 税率
     */
    @JsonProperty("SL")
    private String SL;

    /**
     * 税额（本币）
     */
    @JsonProperty("SEBB")
    private Double SEBB;

    /**
     * 税额（原币）
     */
    @JsonProperty("SEYB")
    private Double SEYB;

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
     * 专项项目
     */
    @JsonProperty("ZXXM")
    private String ZXXM;

    /**
     * 业务科目ID
     */
    @JsonProperty("YWKM")
    private String YWKM;

    /**
     *项目ID
     */
    @JsonProperty("XMID")
    private String XMID;

    /**
     * 合同编号
     */
    @JsonProperty("HTBH")
    private String HTBH = "999999999";

    /**
     * 计税方式
     */
    @JsonProperty("JSFS")
    private String JSFS = "1";

    /**
     * 核销金额（原币）
     */
    @JsonProperty("HXJEYB")
    private Double HXJEYB;

    /**
     * 到期日期
     */
    @JsonProperty("DQRQ")
    private String DQRQ;

    /**
     * 合同付款人
     */
    @JsonProperty("HTFKR")
    private String HTFKR;

    /**
     * 币种
     */
    @JsonProperty("BZ")
    private String BZ;

}
