package com.wishare.finance.domains.voucher.support.zhongjiao.zjpushorder;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 财务云-支付申请-业务支付申请
 */
@Data
public class ZFSQData {

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
     * 项目ID
     */
    @JsonProperty("XMID")
    private String XMID;
    /**
     * 制单人
     */
    @JsonProperty("ZDR")
    private String ZDR;

    /**
     * 制单日期
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
     * 合同编号
     */
    @JsonProperty("HTBH")
    private String HTBH;

    /**
     * 收款账户ID
     */
    @JsonProperty("SKZH")
    private String SKZH;

    /**
     * 实际收款人
     */
    @JsonProperty("SJSKR")
    private String SJSKR;

    /**
     * 报账事由
     */
    @JsonProperty("BZSY")
    private String BZSY;


    /**
     * 期望付款日期
     */
    @JsonProperty("QWFKRQ")
    private String QWFKRQ;

    /**
     * 加急标志
     */
    @JsonProperty("JJBZ")
    private Boolean JJBZ;

    /**
     * 应付金额(本位币)
     */
    @JsonProperty("YFJEBB")
    private Double YFJEBB;


    /**
     * 附件张数
     */
    @JsonProperty("FJZS")
    private Integer FJZS;

    /**
     * 合同收款人ID
     */
    @JsonProperty("HTSKRID")
    private String HTSKRID;

    /**
     * 凭证日期
     * YYYY-MM-DD格式
     */
    @JsonProperty("PZRQ")
    private String PZRQ;

    @JsonProperty("BWBID")
    private String BWBID;


    @JsonProperty("DJZT")
    private String DJZT;



}
