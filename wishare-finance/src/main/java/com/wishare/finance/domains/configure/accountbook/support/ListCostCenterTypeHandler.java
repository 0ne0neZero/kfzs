package com.wishare.finance.domains.configure.accountbook.support;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler;
import com.wishare.finance.apps.model.configure.accountbook.fo.CostCenter;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.util.List;


/**
 * 成本中心列表类型转换
 *
 * @Author dxclay
 * @Date 2022/9/14
 * @Version 1.0
 */
@MappedTypes({List.class})
@MappedJdbcTypes({JdbcType.VARCHAR})
public class ListCostCenterTypeHandler extends AbstractJsonTypeHandler<List<CostCenter>> {

    @Override
    protected List<CostCenter> parse(String json) {
        return JSONObject.parseArray(json, CostCenter.class);
    }

    @Override
    protected String toJson(List<CostCenter> obj) {
        return JSONObject.toJSONString(obj);
    }
}
