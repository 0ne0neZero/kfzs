package com.wishare.contract.domains.enums.revision;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PlanFxmPushType {

    INSERT(1,"新增"),

    UPDATE(2,"更新"),

    DELETE(3,"删除");

    @JsonValue
    private Integer code;

    private String name;

}
