package com.wishare.contract.domains.enums.settlement;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommunityTypeEnum {

    RESIDENCE(0, "住宅项目"),

    NOT_RESIDENCE(1, "非住宅项目");

    private final Integer code;

    private final String name;

}
