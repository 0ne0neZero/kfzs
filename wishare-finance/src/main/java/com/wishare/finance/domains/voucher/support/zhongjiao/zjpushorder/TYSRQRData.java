package com.wishare.finance.domains.voucher.support.zhongjiao.zjpushorder;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
/**
 * 财务云通用收入确认入参
 */
public class TYSRQRData {
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
     * 经办人ID
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
     * 业务类型ID
     */
    @JsonProperty("YWLX")
    private String YWLX;
    /**
     * 合同编号
     */
    @JsonProperty("HTBH")
    private String HTBH;
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
     * 计税方式
     */
    @JsonProperty("JSFS")
    private String JSFS;
    /**
     * 本位币id
     */
    @JsonProperty("BWBID")
    private String BWBID;
    /**
     * 01,申请:默认 01
     */
    @JsonProperty("DJZT")
    private String DJZT;
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
     * 摘要
     */
    @JsonProperty("DJZY")
    private String DJZY;
    /**
     * 收入确认金额（本位币）
     */
    @JsonProperty("SRQRJEBB")
    private Double SRQRJEBB;
    /**
     * 纳税人类型
     */
    @JsonProperty("NSRLX")
    private String NSRLX;
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
     * 预留字段1
     */
    @JsonProperty("YLZD1")
    private String YLZD1;
    /**
     * 预留字段2
     */
    @JsonProperty("YLZD2")
    private String YLZD2;
    /**
     * 预留字段3
     */
    @JsonProperty("YLZD3")
    private String YLZD3;
    /**
     * 预留字段4
     */
    @JsonProperty("YLZD4")
    private String YLZD4;
    /**
     * 预留字段5
     */
    @JsonProperty("YLZD5")
    private String YLZD5;
    /**
     * 是否主营  默认1
     */
    @JsonProperty("SFZY")
    private Integer SFZY;
    /**
     * 收入类型名称
     */
    @JsonProperty("SRLX")
    private String SRLX;
    /**
     * 计量明细内码
     */
    @JsonProperty("SRQRMXNM")
    private String SRQRMXNM;

    /**
     * 主表内码
     */
    @JsonProperty("ZBNM")
    private String ZBNM;
    /**
     * 项目id
     */
    @JsonProperty("XMMC")
    private String XMMC;
    /**
     * 往来单位编号
     */
    @JsonProperty("WLDW")
    private String WLDW;
    /**
     * 原币ID
     */
    @JsonProperty("BZID")
    private String BZID;
    /**
     * 税率
     */
    @JsonProperty("HL")
    private Double HL;
    /**
     * 含税金额（原币
     */
    @JsonProperty("HSJEYB")
    private Double HSJEYB;
    /**
     * 含税金额（本币
     */
    @JsonProperty("HSJEBB")
    private Double HSJEBB;

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
     * 不含税（原币）
     */
    @JsonProperty("BHSYB")
    private Double BHSYB;
    /**
     * 不含税（本位币）
     */
    @JsonProperty("BHSBB")
    private Double BHSBB;




}
