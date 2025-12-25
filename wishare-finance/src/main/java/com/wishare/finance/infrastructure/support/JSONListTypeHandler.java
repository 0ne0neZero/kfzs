package com.wishare.finance.infrastructure.support;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.util.List;

/**
 * JSON List类型处理器
 *
 * @Author dxclay
 * @Date 2022/9/14
 * @Version 1.0
 */
@MappedTypes({List.class})
@MappedJdbcTypes({JdbcType.VARCHAR})
public abstract class JSONListTypeHandler<T> extends AbstractJsonTypeHandler<List<T>> {
    private final Class<?> type;

    private final Class<T> targetType;

    public JSONListTypeHandler(Class<?> type, Class<T> targetType) {
        Assert.notNull(type, "Type argument cannot be null");
        this.type = type;
        this.targetType = targetType;
    }

    @Override
    protected List<T> parse(String json) {
        return JSONObject.parseArray(json, targetType);
    }


    @Override
    protected String toJson(List<T> obj) {
        return JSONObject.toJSONString(obj);
    }
}