package com.wishare.finance.apps.service.bill.prepay;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author ℳ๓采韵
 * @project wishare-finance
 * @title PrepayOprCheck
 * @date 2023.11.10  12:35
 * @description 账单预支付状态校验
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PrepayOprCheck {
}
