package com.wishare.contract.domains.enums;

import com.wishare.bizlog.action.Action;

/**
 * @Author :wangrui
 * @Date: 2023/1/10
 * 合同日志业务类型枚举
 */
public enum BusinessTypeEnum {

    合同("合同","1"),
    收款("收款","2"),
    合并收款("合并收款","3"),
    开票("开票","4"),
    合并开票("合并开票","5"),
    预付款申请("预付款申请","6"),
    合并预付款("合并预付款","7"),
    结算收票申请("结算收票申请","8"),
    合并结算收票("合并结算收票","9"),
    减免申请("减免申请","10"),
    扣款申请("扣款申请","11"),
    补充申请("补充申请","12"),
    续签申请("续签申请","13"),
    协议终止申请("协议终止申请","14"),
    强制终止申请("强制终止申请","15"),
    子合同申请("子合同申请","16"),
    保证金收据("保证金收据","17"),
    保证金收款("保证金收款","18"),
    保证金结转("保证金结转","19"),
    保证金退款("保证金退款","20"),
    保证金付款("保证金付款","21"),
    工程计提申请("工程计提申请","22"),

    保证金扣款("保证金退款","23"),
    ;

    private String name;
    private String code;

    BusinessTypeEnum(String name,String code) {
        this.name = name;

    }

    public String getName() {
        return name;
    }

    public void setTypes(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
