package com.wishare.finance.infrastructure.event;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 延时消息
 *
 * @Author dxclay
 * @Date 2022/12/26
 * @Version 1.0
 */
public class DelayEventMessage<T> extends EventMessage<T> {

    public static final String DELAY_CODE = "x-delay";

    /**
     * 延时时间（秒）
     */
    int delay;

    public DelayEventMessage() {
    }

    private DelayEventMessage(String messageKey, String routeKey, String name, @Nullable String binderName,
                              T payload, String contentType, Map<String, Object> headers, int delay) {
        this.messageKey = messageKey;
        this.routeKey = routeKey;
        this.name = name;
        this.binderName = binderName;
        this.payload = payload;
        this.contentType = contentType;
        this.headers = Objects.isNull(headers) ? new HashMap<>() : headers;
        this.delay = delay;
        //添加延时消息
        headers.put(DELAY_CODE, delay*1000);
    }

    public static <T> DelayEventMessageBuilder<T> builder(int delay){
        return new DelayEventMessageBuilder<T>(delay);
    }

    public int getDelay() {
        return delay;
    }

    public static class DelayEventMessageBuilder<T> extends EventMessage.EventMessageBuilder<T>{

        int delay;

        public DelayEventMessageBuilder(int delay) {
            this.delay = delay;
        }

        public DelayEventMessageBuilder() {
        }

        public DelayEventMessageBuilder delay(int delay) {
            this.delay = delay;
            return this;
        }

        @Override
        public DelayEventMessage<T> build() {
            return new DelayEventMessage<T>(messageKey, routeKey, name, binderName, payload, contentType, headers, delay);
        }
    }

}
