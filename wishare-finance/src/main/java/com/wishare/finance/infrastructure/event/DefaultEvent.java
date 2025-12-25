package com.wishare.finance.infrastructure.event;

import org.springframework.context.ApplicationEvent;

/**
 * 默认事件
 *
 * @Author dxclay
 * @Date 2022/11/3
 * @Version 1.0
 */
@Event
public class DefaultEvent extends ApplicationEvent {

    public DefaultEvent() {
        super("DefaultEvent");
    }

    public DefaultEvent(Object source) {
        super(source);
    }


}
