package com.wishare.contract.infrastructure.utils.enums;

/**
 * Copyright @ 2022 慧享科技 Co. Ltd.
 * All right reserved.
 *
 * @author: PengAn
 * @create: 2022-04-14
 * @description: 枚举类工具，使用该工具的枚举类必须实现NameValueEnum
 **/
public final class EnumUtils {

    public static <T> boolean isExist(ValueEnum<T>[] enums, T value) {
        if (value == null) {
            return false;
        }
        for (ValueEnum<T> e : enums) {
            if (value.equals(e.getValue())) {
                return true;
            }
        }
        return false;
    }

    public static <E extends Enum<? extends ValueEnum<V>>, V> boolean isExist(Class<E> enumClass, V value) {
        for (Enum<? extends ValueEnum<V>> e : enumClass.getEnumConstants()) {
            if (((ValueEnum<V>) e).getValue().equals(value)) {
                return true;
            }
        }
        return false;
    }

    public static <T> String getNameByValue(NameValueEnum<T>[] enums, T value) {
        if (value == null) {
            return null;
        }
        for (NameValueEnum<T> e : enums) {
            if (e.getValue().equals(value)) {
                return e.getName();
            }
        }
        return null;
    }

    public static <E extends Enum<? extends ValueEnum<V>>, V> E getEnumByValue(E[] enums, V value) {
        for (E e : enums) {
            if (((ValueEnum<V>) e).getValue().equals(value)) {
                return e;
            }
        }
        return null;
    }

    public static <E extends Enum<? extends ValueEnum<V>>, V> E getEnumByValue(Class<E> enumClass, V value) {
        return getEnumByValue(enumClass.getEnumConstants(), value);
    }
}
