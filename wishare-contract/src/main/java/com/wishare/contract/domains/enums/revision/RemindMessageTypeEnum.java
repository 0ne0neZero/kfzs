package com.wishare.contract.domains.enums.revision;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RemindMessageTypeEnum {

    EXPIRE(0,"合同到期预警"),

    REMIND(1,"收款提醒"),

    OVERDUE(2,"收款逾期预警");

    @EnumValue
    @JsonValue
    private final Integer code;

    private final String desc;

}
