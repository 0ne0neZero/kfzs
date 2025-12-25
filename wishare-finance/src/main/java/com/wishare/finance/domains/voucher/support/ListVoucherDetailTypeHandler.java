package com.wishare.finance.domains.voucher.support;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler;
import com.wishare.finance.domains.voucher.entity.VoucherDetailOBV;
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
public class ListVoucherDetailTypeHandler extends AbstractJsonTypeHandler<List<VoucherDetailOBV>> {

    @Override
    protected List<VoucherDetailOBV> parse(String json) {
        return JSONObject.parseArray(json, VoucherDetailOBV.class);
    }

    @Override
    protected String toJson(List<VoucherDetailOBV> obj) {
        return JSONObject.toJSONString(obj);
    }
}
