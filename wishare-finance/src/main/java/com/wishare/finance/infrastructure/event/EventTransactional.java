package com.wishare.finance.infrastructure.event;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 时间
 *
 * @Author dxclay
 * @Date 2022/12/26
 * @Version 1.0
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface EventTransactional {

    /**
     * 事务回滚异常列表
     * @return
     */
    Class<? extends Throwable>[] rollbackFor() default {};

    /**
     * 发送失败是否重试
     * @return
     */
    boolean retry() default false;

}
