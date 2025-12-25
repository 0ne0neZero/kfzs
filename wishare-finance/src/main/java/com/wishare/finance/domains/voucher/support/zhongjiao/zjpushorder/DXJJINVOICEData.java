package com.wishare.finance.domains.voucher.support.zhongjiao.zjpushorder;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DXJJINVOICEData {
    /**
     * 发票明细内码
     */
    @JsonProperty("FPMXNM")
    private String FPMXNM;

    /**
     * 结算内码
     */
    @JsonProperty("JSNM")
    private String JSNM;

    /**
     * 发票号码
     */
    @JsonProperty("FPHM")
    private String FPHM;

    /**
     * 发票代码
     */
    @JsonProperty("FPDM")
    private String FPDM;

    /**
     * 校验码
     */
    @JsonProperty("JYM")
    private String JYM;

    /**
     * 开票日期
     */
    @JsonProperty("KPRQ")
    private String KPRQ;

    /**
     * 不含税金额
     */
    @JsonProperty("BHSJE")
    private Double BHSJE;

    /**
     * 税率
     */
    @JsonProperty("SL")
    private String SL;

    /**
     * 税额
     */
    @JsonProperty("SE")
    private Double SE;

    /**
     * 价税合计
     */
    @JsonProperty("JSHJ")
    private Double JSHJ;

    /**
     * 备注
     */
    @JsonProperty("NOTE")
    private String NOTE;

    /**
     * 是否分包
     */
    @JsonProperty("SFFB")
    private String SFFB;

    /**
     * 发票类型
     */
    @JsonProperty("FPLX")
    private String FPLX;

    /**
     * 是否出口退税
     */
    @JsonProperty("SFCKTS")
    private String SFCKTS;

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
     * 可抵扣税额
     */
    @JsonProperty("KDKSE")
    private Double KDKSE;


    /**
     * 可抵扣税额（本币）
     */
    @JsonProperty("KDKJEBB")
    private Double KDKJEBB;


    /**
     * 销售方名称
     */
    @JsonProperty("XSFMC")
    private String XSFMC;

    /**
     * 销售方税号
     */
    @JsonProperty("SH")
    private String SH;


    /**
     * 税码
     */
    @JsonProperty("SM")
    private String SM;

    /**
     * 验证状态
     */
    @JsonProperty("YZZT")
    private String YZZT;

    /**
     * 购买方名称
     */
    @JsonProperty("GMFMC")
    private String GMFMC;

    /**
     * 购买方税号
     */
    @JsonProperty("GMFSH")
    private String GMFSH;
}
