package com.wishare.finance.domains.voucher.support.zhongjiao.zjpushorder;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 资金收款单 （认领明细）
 */
@Setter
@Getter
public class SKMX3Data {
    /**
     * 认领明细内码
     */
    @JsonProperty("RLMXNM")
    private String RLMXNM;


    /**
     * 单据内码
     */
    @JsonProperty("DJNM")
    private String DJNM;

    /**
     * 认领通知单ID
     */
    @JsonProperty("RLTZID")
    private String RLTZID;

    /**
     * 认领金额
     */
    @JsonProperty("RLJE")
    private Double RLJE;

    @JsonProperty("RLBBJE")
    private String RLBBJE;
}
