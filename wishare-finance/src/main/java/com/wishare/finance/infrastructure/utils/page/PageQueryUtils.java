package com.wishare.finance.infrastructure.utils.page;

import com.wishare.starter.beans.PageF;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.tools.starter.fo.search.SearchF;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import org.apache.commons.lang3.StringUtils;

/**
 * 分页查询工具类
 */
public class PageQueryUtils {

    /**
     * 校验
     * @param query
     * @param columnName
     */
    public static void validQueryContainsFieldAndValue(PageF<SearchF<?>> query, String columnName) {
        //判断是否有flag字段，有的话就把spaceTreeId作为项目ID
        boolean exist = query.getConditions().getFields().stream()
            .anyMatch(v -> (columnName.equals(v.getName()) && StringUtils.isNotBlank(v.getValue().toString())));
        if(!exist) {
            throw new IllegalArgumentException("必须要传入：" + columnName+ " 字段!");
        }
    }

    public static void validQueryContainsFieldAndValue(SearchF<?> query, String columnName) {
        boolean exist = query.getFields().stream()
            .anyMatch(v -> (v.getName().equals(columnName) && StringUtils.isNotBlank(v.getValue().toString())));
        if(!exist) {
            throw new IllegalArgumentException("必须要传入：" + columnName+ " 字段!");
        }
    }
}
