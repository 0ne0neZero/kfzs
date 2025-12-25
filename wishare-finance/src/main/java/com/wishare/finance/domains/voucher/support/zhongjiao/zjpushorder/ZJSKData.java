package com.wishare.finance.domains.voucher.support.zhongjiao.zjpushorder;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 2.1.5资金收款单
 */
@Setter
@Getter
public class ZJSKData {
    /**
     * 单据内码
     */
    @JsonProperty("DJNM")
    private String DJNM;
    /**
     * 单据编号
     */
    @JsonProperty("DJBH")
    private String DJBH;
    /**
     * 核算组织编号
     */
    @JsonProperty("HSDW")
    private String HSDW;
    /**
     * 核算部门编号
     */
    @JsonProperty("HSBM")
    private String HSBM;
    /**
     * 行政组织OID
     */
    @JsonProperty("XZZZ")
    private String XZZZ;
    /**
     * 行政部门OID
     */
    @JsonProperty("XZBM")
    private String XZBM;
    /**
     * 制单人
     */
    @JsonProperty("ZDR")
    private String ZDR;

    /**
     * 还款日期
     */
    @JsonProperty("DJRQ")
    private String DJRQ;
    /**
     * 来源系统
     */
    @JsonProperty("LYXT")
    private String LYXT;
    /**
     * 业务类型
     */
    @JsonProperty("YWLX")
    private String YWLX;

    /**
     * 收款方式
     */
    @JsonProperty("SKFS")
    private String SKFS;

    /**
     * 实际付款人
     */
    @JsonProperty("SJFKR")
    private String SJFKR;


    /**
     * 付款账号（银行户）
     */
    @JsonProperty("FKZH")
    private String FKZH;

    /**
     * 收款银行账号
     */
    @JsonProperty("SKYHZH")
    private String SKYHZH;

    /**
     * 本位币币种
     */
    @JsonProperty("BWBBZ")
    private String BWBBZ;

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
     * 收款金额（原币）
     */
    @JsonProperty("SKJEYB")
    private Double SKJEYB;

    /**
     * 收款金额（本币）
     */
    @JsonProperty("SKJEBB")
    private Double SKJEBB;

    /**
     * 附件张数
     */
    @JsonProperty("FJZS")
    private Integer FJZS;

    /**
     * 业务是由
     */
    @JsonProperty("YWSY")
    private String YWSY;

    /**
     * 备注
     */
    @JsonProperty("BZ")
    private String BZ;

    /**
     * 收款日期
     */
    @JsonProperty("SKRQ")
    private String SKRQ;


    /**
     * 付款账号（内部户）
     */
    @JsonProperty("FKZHNB")
    private String FKZHNB;

    /**
     * 是否生成准则差异账
     */
    @JsonProperty("NWZBZ")
    private String NWZBZ;
    /**
     * 凭证日期
     */
    @JsonProperty("PZRQ")
    private String PZRQ;

    /**
     * 认领人
     */
    @JsonProperty("RLR")
    private String RLR;




}
