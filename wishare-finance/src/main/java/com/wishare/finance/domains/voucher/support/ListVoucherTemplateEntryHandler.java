package com.wishare.finance.domains.voucher.support;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler;
import com.wishare.finance.domains.voucher.entity.VoucherTemplateEntryOBV;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.util.List;


/**
 * 凭证规则借贷分录类型转换
 *
 * @Author dxclay
 * @Date 2022/9/14
 * @Version 1.0
 */
@MappedTypes({List.class})
@MappedJdbcTypes({JdbcType.VARCHAR})
public class ListVoucherTemplateEntryHandler extends AbstractJsonTypeHandler<List<VoucherTemplateEntryOBV>> {

    @Override
    protected List<VoucherTemplateEntryOBV> parse(String json) {
        return JSONObject.parseArray(json, VoucherTemplateEntryOBV.class);
    }

    @Override
    protected String toJson(List<VoucherTemplateEntryOBV> obj) {
        return JSONObject.toJSONString(obj);
    }
}
