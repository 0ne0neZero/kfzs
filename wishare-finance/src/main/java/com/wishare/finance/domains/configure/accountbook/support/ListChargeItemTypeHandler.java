package com.wishare.finance.domains.configure.accountbook.support;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler;
import com.wishare.finance.apps.model.configure.accountbook.fo.ChargeItem;
import com.wishare.finance.domains.bill.entity.CarryoverDetail;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.util.List;


/**
 * 费项列表类型转换
 *
 * @Author dxclay
 * @Date 2022/9/14
 * @Version 1.0
 */
@MappedTypes({List.class})
@MappedJdbcTypes({JdbcType.VARCHAR})
public class ListChargeItemTypeHandler extends AbstractJsonTypeHandler<List<ChargeItem>> {

    @Override
    protected List<ChargeItem> parse(String json) {
        return JSONObject.parseArray(json, ChargeItem.class);
    }

    @Override
    protected String toJson(List<ChargeItem> obj) {
        return JSONObject.toJSONString(obj);
    }
}
