package com.wishare.finance.infrastructure.utils;

import com.google.common.collect.Lists;
import com.wishare.finance.domains.configure.accountbook.entity.AccountBookE;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 集合工具类
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/4/26
 */
public class ListUtils {


    /**
     * 将列表按照指定条件分组成单个对象
     * @param list
     * @param function
     * @return
     * @param <K>
     * @param <V>
     */
    public static <K, V> Map<K, V> toGroupSingleMap(List<V> list, Function<V, K> function){
        Map<K, V> map = new HashMap<>();
        for (V v : list) {
            map.put(function.apply(v), v);
        }
        return map;
    }

    /**
     * 将列表按照指定条件分组成单个对象列表
     * @param list
     * @param function
     * @return
     * @param <K>
     * @param <V>
     */
    public static <K, V> Map<K, List<V>> toGroupMap(List<V> list, Function<V, K> function){
        return list.stream().collect(Collectors.groupingBy(function));
    }


    /**
     * 对List按groupSize的数量进行分组
     * @param list
     * @param groupSize
     * @return
     * @param <T>
     */
    public static<T> List<List<T>> grouping(List<T> list, int groupSize) {
        List<List<T>> groups = new ArrayList<>();
        List<List<T>> partitions = Lists.partition(list, groupSize);
        for (List<T> partition : partitions) {
            List<T> group = new ArrayList<>(partition);
            groups.add(group);
        }
        return groups;
    }

}
