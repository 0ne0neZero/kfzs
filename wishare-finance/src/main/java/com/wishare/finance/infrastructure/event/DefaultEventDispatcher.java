package com.wishare.finance.infrastructure.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 默认事件调度器
 *
 * @Author dxclay
 * @Date 2022/11/3
 * @Version 1.0
 */
@Slf4j
@Component
public class DefaultEventDispatcher implements EventDispatcher{

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public <T> void apply(EventMessage<T> message) {
        T payload = message.getPayload();
        if (Objects.nonNull(payload)){
            if (payload instanceof DefaultEvent){
                applicationEventPublisher.publishEvent(payload);
            }else{
                applicationEventPublisher.publishEvent(new DefaultEvent(payload));
            }
        }
    }

}
