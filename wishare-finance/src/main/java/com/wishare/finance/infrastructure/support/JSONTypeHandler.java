package com.wishare.finance.infrastructure.support;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.util.List;


/**
 * 实现 JSON 字段类型处理器
 *
 * @Author dxclay
 * @Date 2022/9/14
 * @Version 1.0
 */
@MappedTypes({Object.class})
@MappedJdbcTypes({JdbcType.VARCHAR})
public class JSONTypeHandler extends AbstractJsonTypeHandler<Object> {

    private final Class<?> type;

    public JSONTypeHandler(Class<?> type) {
        Assert.notNull(type, "Type argument cannot be null");
        this.type = type;
    }

    @Override
    protected Object parse(String json) {
        return JSONObject.parseObject(json, type);
    }

    @Override
    protected String toJson(Object obj) {
        return JSONObject.toJSONString(obj);
    }

}
