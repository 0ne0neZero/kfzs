package com.wishare.finance.domains.voucher.support.zhongjiao.zjpushorder;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KJMXData {
    /**
     * 款项明细内码
     */
    @JsonProperty("KJMXNM")
    private String KJMXNM;
    /**
     * 主表内码
     */
    @JsonProperty("ZBNM")
    private String ZBNM;
    /**
     * 变动
     */
    @JsonProperty("BD")
    private String BD;
    /**
     * 款项ID
     */
    @JsonProperty("KXID")
    private String KXID;
    /**
     * 项目ID
     */
    @JsonProperty("XMID")
    private String XMID;
    /**
     * 计税方式
     */
    @JsonProperty("JSFS")
    private String JSFS;
    /**
     * 合同ID
     */
    @JsonProperty("HTID")
    private String HTID;
    /**
     * 往来单位
     */
    @JsonProperty("WLDWID")
    private String WLDWID;
    /**
     * 应收应付ID
     */
    @JsonProperty("YSYFID")
    private String YSYFID;
    /**
     * 原币ID
     */
    @JsonProperty("YBID")
    private String YBID;
    /**
     * 汇率
     */
    @JsonProperty("HL")
    private Double HL;
    /**
     * 金额（原币）
     */
    @JsonProperty("KJJEYB")
    private Double KJJEYB;
    /**
     * 金额（本位币）
     */
    @JsonProperty("KJJEBB")
    private Double KJJEBB;
    /**
     * 到期日期
     */
    @JsonProperty("DQRQ")
    private String DQRQ;
    /**
     * 预计收付款日期
     */
    @JsonProperty("YJSKRQ")
    private String YJSKRQ;
    /**
     * 未核销金额（原币）
     */
    @JsonProperty("WHXJEYB")
    private Double WHXJEYB;
    /**
     * 本次核销税额（原币）
     */
    @JsonProperty("BCHXSEYB")
    private Double BCHXSEYB;
    /**
     * 本次核销税额（本位币）
     */
    @JsonProperty("BCHXSEBB")
    private Double BCHXSEBB;
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
     * 预留字段5
     */
    @JsonProperty("YJSFKRQ")
    private String YJSFKRQ;

    /**
     * 往来单位编号
     */
    @JsonProperty("WLDW")
    private String WLDW;

    /**
     * 计量明细内码
     */
    @JsonProperty("SRQRMXNM")
    private String SRQRMXNM;

    @JsonProperty("XMMC")
    private String XMMC;
    /**
     * 原币ID
     */
    @JsonProperty("BZID")
    private String BZID;



/*-----------------------------------------new   **/


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
