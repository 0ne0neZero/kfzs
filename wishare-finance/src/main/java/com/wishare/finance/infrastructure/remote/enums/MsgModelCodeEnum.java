package com.wishare.finance.infrastructure.remote.enums;

import lombok.Getter;

@Getter
public enum MsgModelCodeEnum {

    FLOW_CLAIM("FLOW_CLAIM", "差额认领审核", MsgCardCodeTypeEnum.FLOW_CLAIM_REFUSE);

    private final String code;

    private final String name;

    private final MsgCardCodeTypeEnum msgCardCodeTypeEnum;

    MsgModelCodeEnum(String code, String name, MsgCardCodeTypeEnum msgCardCodeTypeEnum) {
        this.code = code;
        this.name = name;
        this.msgCardCodeTypeEnum = msgCardCodeTypeEnum;
    }
}
