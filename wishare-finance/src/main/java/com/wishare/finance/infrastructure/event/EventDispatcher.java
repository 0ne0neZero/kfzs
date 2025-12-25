package com.wishare.finance.infrastructure.event;

/**
 * 时间调度器
 *
 * @Author dxclay
 * @Date 2022/11/3
 * @Version 1.0
 */
public interface EventDispatcher {

    /**
     * 调度事件
     * @param message
     */
    <T> void apply(EventMessage<T> message);

}
