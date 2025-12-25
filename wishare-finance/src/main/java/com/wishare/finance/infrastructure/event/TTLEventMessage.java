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
public class TTLEventMessage<T> extends EventMessage<T> {

    public static final String TTL_CODE = "x-message-ttl";

    /**
     * 过期时间（秒）
     */
    int expire;

    public TTLEventMessage() {
    }

    private TTLEventMessage(String messageKey, String routeKey, String name, @Nullable String binderName,
                            T payload, String contentType, Map<String, Object> headers, int expire) {
        this.messageKey = messageKey;
        this.routeKey = routeKey;
        this.name = name;
        this.binderName = binderName;
        this.payload = payload;
        this.contentType = contentType;
        this.headers = Objects.isNull(headers) ? new HashMap<>() : headers;
        this.expire = expire;
        //添加延时消息
        this.headers.put(TTL_CODE, expire*1000);
    }

    public int getExpire() {
        return expire;
    }

    public static class TTLEventMessageBuilder<T> extends EventMessageBuilder<T>{

        int expire;

        public TTLEventMessageBuilder(int expire) {
            this.expire = expire;
        }

        public TTLEventMessageBuilder() {
        }

        public TTLEventMessageBuilder expire(int expire) {
            this.expire = expire;
            return this;
        }

        @Override
        public TTLEventMessage<T> build() {
            return new TTLEventMessage<T>(messageKey, routeKey, name, binderName, payload, contentType, headers, expire);
        }
    }

}
