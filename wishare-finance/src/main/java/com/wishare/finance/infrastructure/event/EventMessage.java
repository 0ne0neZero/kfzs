package com.wishare.finance.infrastructure.event;

import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * 事件消息
 *
 * @Author dxclay
 * @Date 2022/11/7
 * @Version 1.0
 */
@Getter
@Setter
public class EventMessage<T> {

    /**
     * 消息id， 需要由业务指定
     */
    String messageKey;

    /**
     * 消息路由id
     */
    String routeKey;
    /**
     * 事件名称
     *  如果是stream 为生产者绑定的名称
     */
    String name;
    /**
     * 绑定者名称
     */
    @Nullable
    String binderName;
    /**
     * 消息体
     */
    T payload;
    /**
     * 请求的方法体
     */
    String contentType = "application/json";

    /**
     * 请求头部信息
     */
    Map<String, Object> headers;


    public EventMessage() {
    }

    /**
     * 消息建造者
     * @param messageKey
     * @return
     * @param <T>
     */
    public static <T> EventMessageBuilder<T> builder(String messageKey){
        return new EventMessageBuilder<>(messageKey);
    }

    public static <T> EventMessageBuilder<T> builder(){
        return new EventMessageBuilder<>();
    }

    private EventMessage(String messageKey, String routeKey, String name, @Nullable String binderName,
                         T payload, String contentType, Map<String, Object> headers) {
        this.messageKey = messageKey;
        this.routeKey = routeKey;
        this.name = name;
        this.binderName = binderName;
        this.payload = payload;
        this.contentType = contentType;
        this.headers = headers;
    }

    public static class EventMessageBuilder<T>{
        String messageKey;
        String routeKey;
        String name;
        @Nullable
        String binderName;
        T payload;
        String contentType = "application/json";
        Map<String, Object> headers;

        public EventMessageBuilder() {
        }

        private EventMessageBuilder(String messageKey) {
            this.messageKey = messageKey;
        }

        public EventMessageBuilder routeKey(String routeKey) {
            this.routeKey = routeKey;
            return this;
        }

        public EventMessageBuilder name(String name) {
            this.name = name;
            return this;
        }

        public EventMessageBuilder binderName(@Nullable String binderName) {
            this.binderName = binderName;
            return this;
        }

        public EventMessageBuilder<T> payload(T payload) {
            this.payload = payload;
            return this;
        }

        public EventMessageBuilder contentType(String contentType) {
            this.contentType = contentType;
            return this;
        }

        public EventMessageBuilder headers(Map<String, Object> headers) {
            verifyHeader();
            this.headers.putAll(headers);
            return this;
        }

        public EventMessageBuilder<T> headers(String k1, Object v1) {
            verifyHeader();
            this.headers.put(k1, v1);
            return this;
        }

        public EventMessageBuilder headers(String k1, Object v1, String k2, Object v2) {
            verifyHeader();
            this.headers.put(k1, v1);
            this.headers.put(k2, v2);
            return this;
        }

        public EventMessageBuilder headers(String k1, Object v1, String k2, Object v2, String k3, Object v3) {
            verifyHeader();
            this.headers.put(k1, v1);
            this.headers.put(k2, v2);
            this.headers.put(k3, v3);
            return this;
        }

        public EventMessage<T> build(){
            return new EventMessage<T>(messageKey, routeKey, name, binderName, payload, contentType, headers);
        }

        private void verifyHeader(){
            if (this.headers == null){
                this.headers = new HashMap<>();
            }
        }

    }

}
