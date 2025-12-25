package com.wishare.finance.domains.configure.accountbook.support;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler;
import com.wishare.finance.apps.model.configure.accountbook.fo.StatutoryBody;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.util.List;

/**
 * 法定单位列表类型转换
 * @author dongpeng
 * @date 2023/9/6 9:08
 */
@MappedTypes({List.class})
@MappedJdbcTypes({JdbcType.VARCHAR})
public class ListStatutoryBodyTypeHandler extends AbstractJsonTypeHandler<List<StatutoryBody>> {
    @Override
    protected List<StatutoryBody> parse(String json) {
        return JSONObject.parseArray(json, StatutoryBody.class);
    }

    @Override
    protected String toJson(List<StatutoryBody> obj) {
        return JSONObject.toJSONString(obj);
    }
}
