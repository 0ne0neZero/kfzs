package com.wishare.finance.domains.voucher.support.zhongjiao.zjpushorder;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * DXJS（对下结算）说明
 */
@Setter
@Getter
public class DXJSData {

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
     * 债权人
     */
    @JsonProperty("ZQR")
    private String ZQR;

    /**
     * 结算单号
     */
    @JsonProperty("JSDH")
    private String JSDH;

    /**
     * 是否签认
     */
    @JsonProperty("SFQR")
    private String SFQR;


    /**
     * 结算开始日期
     */
    @JsonProperty("JSKSRQ")
    private String JSKSRQ;

    /**
     * 结算截止日期
     */
    @JsonProperty("JSJZRQ")
    private String JSJZRQ;

    /**
     * 计量确认日期
     */
    @JsonProperty("JLQRRQ")
    private String JLQRRQ;


    /**
     * 本期结算含税金额（本位币
     */
    @JsonProperty("HSJEBB")
    private Double HSJEBB;

    /**
     * 本期结算不含税金额（本位币）
     */
    @JsonProperty("BHSJEBB")
    private Double BHSJEBB;

    /**
     * 本期结算税额（本位币）
     */
    @JsonProperty("SEBB")
    private Double SEBB;

    /**
     * 本期累计结算含税金额（本位币）
     */
    @JsonProperty("LJHSJEBB")
    private Double LJHSJEBB;

    /**
     * 计税方式
     */
    @JsonProperty("JSFS")
    private String JSFS;

    /**
     * 本位币编号
     */
    @JsonProperty("BWBBH")
    private String BWBBH;

    /**
     * 原币ID
     */
    @JsonProperty("BZID")
    private String BZID;

    /**
     * 附件张数
     */
    @JsonProperty("DJFJZS")
    private Integer DJFJZS;

    /**
     * 报账事由
     */
    @JsonProperty("BZSY")
    private String BZSY;

    /**
     * 单据摘要
     */
    @JsonProperty("DJZY")
    private String DJZY;

    /**
     * 发票页签是否为空
     */
    @JsonProperty("FPYQ")
    private String FPYQ;

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

    @JsonProperty("DJZT")
    private String DJZT;
}
