package com.wishare.contract.infrastructure.utils.query;

/**
 * 功能解释
 *
 * @author 龙江锋
 * @date 2023/8/15 11:08
 */
public final class WrapperX {
    private WrapperX() {
        // ignore
    }

    /**
     * 获取 QueryWrapperX&lt;T&gt;
     *
     * @param <T> 实体类泛型
     * @return QueryWrapperX&lt;T&gt;
     */
    public static <T> QueryWrapperX<T> queryX() {
        return new QueryWrapperX<>();
    }

    /**
     * 获取 LambdaQueryWrapperX&lt;T&gt;
     *
     * @param <T> 实体类泛型
     * @return LambdaQueryWrapperX&lt;T&gt;
     */
    public static <T> LambdaQueryWrapperX<T> lambdaQueryX() {
        return new LambdaQueryWrapperX<>();
    }
}
