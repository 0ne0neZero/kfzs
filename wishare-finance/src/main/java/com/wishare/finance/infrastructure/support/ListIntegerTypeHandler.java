package com.wishare.finance.infrastructure.support;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler;
import com.wishare.finance.apps.model.configure.accountbook.fo.ChargeItem;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.util.List;


/**
 * 整型列表类型转换
 *
 * @Author dxclay
 * @Date 2022/9/14
 * @Version 1.0
 */
@MappedTypes({List.class})
@MappedJdbcTypes({JdbcType.VARCHAR})
public class ListIntegerTypeHandler extends AbstractJsonTypeHandler<List<Integer>> {

    @Override
    protected List<Integer> parse(String json) {
        return JSONObject.parseArray(json, Integer.class);
    }

    @Override
    protected String toJson(List<Integer> obj) {
        return JSONObject.toJSONString(obj);
    }
}
