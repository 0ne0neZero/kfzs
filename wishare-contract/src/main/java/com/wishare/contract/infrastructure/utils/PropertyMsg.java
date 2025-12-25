package com.wishare.contract.infrastructure.utils;

import java.lang.annotation.*;
/**
 * @description:
 * @author: zhangfuyu
 * @Date: 2023/7/21/11:25
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface PropertyMsg {
    String value();
}