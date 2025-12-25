package com.wishare.finance.infrastructure.remote.fo.external.baiwangjinshuiyun.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author dongpeng
 * @date 2023/10/28 11:33
 * 优惠政策标识
 */
@Getter
@AllArgsConstructor
public enum PreferentialPolicyEnum {

    不使用("0", "不使用(默认)"),
    使用优惠政策("1", "使用优惠政策"),
    ;

    private String code;

    private String name;
}
