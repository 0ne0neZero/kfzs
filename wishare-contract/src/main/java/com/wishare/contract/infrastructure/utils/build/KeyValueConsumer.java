package com.wishare.contract.infrastructure.utils.build;

import java.util.Objects;

/**
 * @Author 龙江锋
 * @Date 2023年7月19日09:14:45
 * @Version 1.0
 */
@FunctionalInterface
public interface KeyValueConsumer<T, K, V> {

    /**
     * 接收数据
     *
     * @param t     数据对象
     * @param key   key
     * @param value value
     */
    void accept(T t, K key, V value);


    /**
     * And then key value consumer.
     *
     * @param after the after
     * @return the key value consumer
     */
    default KeyValueConsumer<T, K, V> andThen(KeyValueConsumer<? super T, ? super K, ? super V> after) {
        Objects.requireNonNull(after);

        return (t, k, v) -> {
            accept(t, k, v);
            after.accept(t, k, v);
        };
    }
}
