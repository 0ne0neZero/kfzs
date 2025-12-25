package com.wishare.finance.infrastructure.remote.fo.external.baiwang.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: Linitly
 * @date: 2023/6/19 16:15
 * @descrption: 整单折扣类型
 */
@Getter
@AllArgsConstructor
public enum DiscountTypeEnum {

    按折扣金额价内折扣("1", "按折扣金额价内折扣"),
    按折扣金额价外折扣("2", "按折扣金额价外折扣"),
    按折扣率价内折扣("3", "按折扣率价内折扣"),
    按折扣率价外折扣("4", "按折扣率价外折扣"),
    ;

    private String code;

    private String name;
}
