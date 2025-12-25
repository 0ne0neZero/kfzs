package com.wishare.finance.domains.voucher.support.zhongjiao.zjpushorder;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 资金收款单 （审批日志）说明
 */
@Setter
@Getter
public class SPRZData {
    /**
     * 审批日志ID
     */
    @JsonProperty("SPRZID")
    private String SPRZID;

    /**
     * 任务名称
     */
    @JsonProperty("RWMC")
    private String RWMC;

    /**
     * 结点名称
     */
    @JsonProperty("JDMC")
    private String JDMC;

    /**
     * 审批人
     */
    @JsonProperty("SPR")
    private String SPR;

    /**
     * 审批动作
     */
    @JsonProperty("SPDZ")
    private String SPDZ;

    /**
     * 审批意见
     */
    @JsonProperty("SPYJ")
    private String SPYJ;

    /**
     * 开始时间
     */
    @JsonProperty("KSRQ")
    private String KSRQ;

    /**
     * 审批时间
     */
    @JsonProperty("SPRQ")
    private String SPRQ;

}
