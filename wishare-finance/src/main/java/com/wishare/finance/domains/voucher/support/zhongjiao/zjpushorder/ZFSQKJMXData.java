package com.wishare.finance.domains.voucher.support.zhongjiao.zjpushorder;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 支付申请款项明细说明
 */
@Setter
@Getter
public class ZFSQKJMXData {
    /**
     * 扣减明细内码
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
     * 到期日期
     */
    @JsonProperty("DQRQ")
    private String DQRQ;


    /**
     * 结算方式
     */
    @JsonProperty("QWJSFS")
    private String QWJSFS;

}
