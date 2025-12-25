package com.wishare.contract.domains.enums.revision;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RemindMessageConfigEnum {

    EXPIRE_MSG(0, "合同到期预警", "2"),

    REMIND(1, "收款提醒", "1"),

    OVERDUE(2, "收款逾期预警", "2");

    @JsonValue
    @EnumValue
    private final Integer code;

    private final String title;

    private final String typeId;

}
