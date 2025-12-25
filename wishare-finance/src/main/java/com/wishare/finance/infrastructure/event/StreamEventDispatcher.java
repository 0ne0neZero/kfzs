package com.wishare.finance.infrastructure.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;

/**
 * 默认事件调度器
 *
 * @Author dxclay
 * @Date 2022/11/3
 * @Version 1.0
 */
@Slf4j
@Component
public class StreamEventDispatcher implements EventDispatcher{

    @Autowired
    private StreamBridge streamBridge;

    @Override
    public <T> void apply(EventMessage<T> message) {
        streamBridge.send(message.getName(), message.getBinderName(),
                MessageBuilder.withPayload(message.getPayload()).copyHeaders(message.getHeaders()).build(),
                MimeTypeUtils.parseMimeType(message.getContentType()));
    }

}
