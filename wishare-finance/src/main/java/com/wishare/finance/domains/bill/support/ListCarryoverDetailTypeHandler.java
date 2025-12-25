package com.wishare.finance.domains.bill.support;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler;
import com.wishare.finance.domains.bill.entity.CarryoverDetail;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.util.List;


/**
 * 结转明细列表类型转换
 *
 * @Author dxclay
 * @Date 2022/9/14
 * @Version 1.0
 */
@MappedTypes({List.class})
@MappedJdbcTypes({JdbcType.VARCHAR})
public class ListCarryoverDetailTypeHandler extends AbstractJsonTypeHandler<List<CarryoverDetail>> {

    @Override
    protected List<CarryoverDetail> parse(String json) {
        return JSONObject.parseArray(json, CarryoverDetail.class);
    }

    @Override
    protected String toJson(List<CarryoverDetail> obj) {
        return JSONObject.toJSONString(obj);
    }
}
