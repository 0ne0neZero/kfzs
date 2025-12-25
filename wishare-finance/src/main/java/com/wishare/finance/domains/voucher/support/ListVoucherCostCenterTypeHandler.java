package com.wishare.finance.domains.voucher.support;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler;
import com.wishare.finance.domains.voucher.entity.VoucherCostCenterOBV;
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
public class ListVoucherCostCenterTypeHandler extends AbstractJsonTypeHandler<List<VoucherCostCenterOBV>> {

    @Override
    protected List<VoucherCostCenterOBV> parse(String json) {
        return JSONObject.parseArray(json, VoucherCostCenterOBV.class);
    }

    @Override
    protected String toJson(List<VoucherCostCenterOBV> obj) {
        return JSONObject.toJSONString(obj);
    }
}
