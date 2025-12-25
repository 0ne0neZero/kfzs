package com.wishare.finance.domains.voucher.support.zhongjiao.zjpushorder;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ZJParameter {
    /**
     * 实例编号
     */
    private String appInstanceCode;
    /**
     * 核算组织编号
     */
    private String unitCode;
    /**
     * 来源系统
     */
    private String sourceSystem;
    /**
     * 业务单据类型编号
     */
    private String businessCode;
    /**
     * 业务单据数据
     */
    private Object psData;
}
