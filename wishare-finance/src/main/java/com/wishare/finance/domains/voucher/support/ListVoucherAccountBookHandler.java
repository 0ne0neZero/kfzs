package com.wishare.finance.domains.voucher.support;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler;
import com.wishare.finance.domains.voucher.entity.VoucherAccountBook;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.util.List;

/**
 * @author luzhonghe
 * @version 1.0
 * @since 2023/4/26
 */
@MappedTypes({List.class})
@MappedJdbcTypes({JdbcType.VARCHAR})
public class ListVoucherAccountBookHandler extends AbstractJsonTypeHandler<List<VoucherAccountBook>> {


    @Override
    protected List<VoucherAccountBook> parse(String json) {
        return JSONObject.parseArray(json, VoucherAccountBook.class);
    }

    @Override
    protected String toJson(List<VoucherAccountBook> obj) {
        return JSONObject.toJSONString(obj);
    }
}
