package com.wishare.contract.infrastructure.utils.build;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 使用建造者模式进行对象创建
 *
 * @Author 龙江锋
 * @Date 2023年7月19日09:14:38
 * @Version 1.0
 */
public class Builder<T> implements Build<T>{
    private static final long serialVersionUID = 971230L;

    private final Supplier<T> constructor;

    private final List<Consumer<T>> modifiers1 = new ArrayList<>();

    private final List<Consumer<T>> modifiers2 = new ArrayList<>();

    public Builder(Supplier<T> supplier) {
        this.constructor = supplier;
    }

    public static <T> Builder<T> of(Supplier<T> supplier) {
        return new Builder<>(supplier);
    }

    public <U> Builder<T> with(BiConsumer<T, U> consumer, U value) {
        Consumer<T> tmpConsumer = instance -> consumer.accept(instance, value);
        modifiers1.add(tmpConsumer);
        return this;
    }

    public <U> Builder<T> with(Function<T, List<U>> function, U value) {
        Consumer<T> tmpConsumer = instance -> function.apply(instance).add(value);
        modifiers1.add(tmpConsumer);
        return this;
    }

    public <K, V> Builder<T> with(KeyValueConsumer<T, K, V> consumer, K key, V value) {
        Consumer<T> tmpConsumer = instance -> consumer.accept(instance, key, value);
        modifiers2.add(tmpConsumer);
        return this;
    }

    @Override
    public T build() {
        T value = constructor.get();
        modifiers1.forEach(instance -> instance.accept(value));
        modifiers2.forEach(instance -> instance.accept(value));
        modifiers1.clear();
        modifiers2.clear();
        return value;
    }
}
