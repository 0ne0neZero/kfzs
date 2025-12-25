package com.wishare.finance.infrastructure.support;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author xujian
 * @date 2022/9/8
 * @Description:
 */
public abstract class AbstractQuery<T> {
    protected Map<Long, T> map = Maps.newHashMap();

    public T get(Long id) {
        if (null == id) {
            return null;
        }
        return map.get(id);
    }


    public List<T> getAll() {
        return Lists.newLinkedList(map.values());
    }

    public AbstractQuery() {
    }

    public void putList(List<T> list, Function<T, Long> function) {
        if (!CollectionUtils.isEmpty(list)) {
            list.forEach(d -> {
                map.put(function.apply(d), d);
            });
        }
    }

    /**
     * @param qClass     具体实现的query类
     * @param mapperFn   从数据库查询的方法能返回一个List
     * @param buildKeyFn 抽取Map中Key的方法
     * @param <Q>
     * @return
     */
    public static <Q extends AbstractQuery, T> Q generate(Class<Q> qClass, Supplier<List<T>> mapperFn, Function<T, String> buildKeyFn) {
        try {
            Q q = qClass.newInstance();
            List<T> list = mapperFn.get();
            q.putList(list, buildKeyFn);
            return q;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> AbstractQuery<T> generate(Supplier<List<T>> mapperFn, Function<T, Long> buildKeyFn) {
        try {
            AbstractQuery<T> query = new AbstractQuery<T>() {
            };
            List<T> list = mapperFn.get();
            query.putList(list, buildKeyFn);
            return query;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
