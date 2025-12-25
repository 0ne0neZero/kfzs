package com.wishare.finance.domains.voucher.support.zhongjiao.zjpushorder;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SRQRMXData {
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
     * 项目ID
     */
    @JsonProperty("XMMC")
    private String XMMC;
    /**
     * 合同ID
     */
    @JsonProperty("HTBH")
    private String HTBH;
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
     * 汇率
     */
    @JsonProperty("HL")
    private Double HL;
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
    private String SL;
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
    /**
     * 销售代理佣金
     */
    @JsonProperty("XSDLYJ")
    private Double XSDLYJ;
    /**
     * 预算科目ID
     */
    @JsonProperty("YSKMID")
    private String YSKMID;
    /**
     * 计税方式
     */
    @JsonProperty("JSFS")
    private String JSFS;
    /**
     * 高新收入
     */
    @JsonProperty("GXSR")
    private Double GXSR;
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
}
