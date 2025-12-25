package com.wishare.contract.domains.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DeletedEnum {

    ZERO(0,"未删除"),

    ONE(1,"已删除");

    @EnumValue
    private final Integer code;

    private final String desc;

}
