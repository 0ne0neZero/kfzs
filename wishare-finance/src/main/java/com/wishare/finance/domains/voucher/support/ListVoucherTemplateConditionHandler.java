package com.wishare.finance.domains.voucher.support;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler;
import com.wishare.finance.domains.voucher.entity.VoucherRuleConditionOBV;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.util.List;


/**
 * 凭证规则过滤条件类型转换
 *
 * @Author dxclay
 * @Date 2022/9/14
 * @Version 1.0
 */
@MappedTypes({List.class})
@MappedJdbcTypes({JdbcType.VARCHAR})
public class ListVoucherTemplateConditionHandler extends AbstractJsonTypeHandler<List<VoucherRuleConditionOBV>> {

    @Override
    protected List<VoucherRuleConditionOBV> parse(String json) {
        return JSONObject.parseArray(json, VoucherRuleConditionOBV.class);
    }

    @Override
    protected String toJson(List<VoucherRuleConditionOBV> obj) {
        return JSONObject.toJSONString(obj);
    }
}
