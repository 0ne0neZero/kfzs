package com.wishare.finance.domains.voucher.support.zhongjiao.zjpushorder;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * 业务支付申请单 下的 KXMX(款项明细)说明
 */
@Setter
@Getter
public class KXMXData {

    /**
     * 款项明细内码
     */
    @JsonProperty("KXMXNM")
    private String KXMXNM;

    /**
     * 主表内码
     */
    @JsonProperty("ZBNM")
    private String ZBNM;

    /**
     * 业务科目
     */
    @JsonProperty("YWLX")
    private String YWLX;

    /**
     * 资金计划编号
     */
    @JsonProperty("ZJJHBH")
    private String ZJJHBH;

    /**
     * 核销应付编号
     */
    @JsonProperty("HEYFBH")
    private String HEYFBH;

    /**
     * 币种（原币）
     */
    @JsonProperty("BZID")
    private String BZID;

    /**
     * 汇率
     */
    @JsonProperty("HL")
    private Double HL;

    /**
     * 金额（原币）
     */
    @JsonProperty("JEYB")
    private Double JEYB;

    /**
     * 金额（本币）
     */
    @JsonProperty("JEBB")
    private Double JEBB;


    /**
     * 专项工程项目
     */
    @JsonProperty("ZXGCXMID")
    private String ZXGCXMID;

    /**
     * 未核销金额(原币)
     */
    @JsonProperty("WHXJE")
    private Double WHXJE;

    /**
     * 到期日期
     */
    @JsonProperty("DQRQ")
    private String DQRQ;

    /**
     * 结算方式
     */
    @JsonProperty("QWJSFS")
    private String QWJSFS;

    /**
     * 租赁负债增减变动
     */
    @JsonProperty("ZLFZ")
    private String ZLFZ;

}



