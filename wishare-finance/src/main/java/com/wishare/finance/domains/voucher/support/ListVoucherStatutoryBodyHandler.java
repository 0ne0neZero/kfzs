package com.wishare.finance.domains.voucher.support;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler;
import com.wishare.finance.domains.voucher.entity.VoucherStatutoryBody;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.util.List;


/**
 * 凭证法定单位信息类型转换
 *
 * @Author dxclay
 * @Date 2022/9/14
 * @Version 1.0
 */
@MappedTypes({List.class})
@MappedJdbcTypes({JdbcType.VARCHAR})
public class ListVoucherStatutoryBodyHandler extends AbstractJsonTypeHandler<List<VoucherStatutoryBody>> {

    @Override
    protected List<VoucherStatutoryBody> parse(String json) {
        return JSONObject.parseArray(json, VoucherStatutoryBody.class);
    }

    @Override
    protected String toJson(List<VoucherStatutoryBody> obj) {
        return JSONObject.toJSONString(obj);
    }
}
