package com.wishare.finance.apps.process.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BusinessProcessType {

    PAYMENT_APP_FORM(7, "业务支付申请单"),

    DX_JS_FORM(8, "对下结算单"),
    Reduction_FORM(9, "减免"),
    SRQR_SQ_FORM(11, "中交服务-收入确认实签单流程"),
    VISITOR_FORM(12, "访客邀约");

    @JsonValue
    private Integer code;

    private String desc;

}
