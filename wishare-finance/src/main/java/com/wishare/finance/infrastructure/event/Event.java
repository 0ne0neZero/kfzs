package com.wishare.finance.infrastructure.event;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 事件定义
 *
 * @Author dxclay
 * @Date 2022/11/3
 * @Version 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Event {

    /**
     * 事件调度器
     *   默认为spring 事件调度器
     * @return
     */
    Class<? extends EventDispatcher> dispatcher() default DefaultEventDispatcher.class;

    /**
     * 调度器类型
     * @return
     */
    DispatcherType dispatcherType() default DispatcherType.SPRING_EVENT;

    /**
     * 绑定名称
     * @return
     */
    String binderName() default "";

    /**
     * 事件名称
     * @return
     */
    String name()  default "";

}
