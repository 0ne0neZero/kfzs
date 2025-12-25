package com.wishare.finance.domains.voucher.support;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler;
import com.wishare.finance.domains.voucher.entity.VoucherRuleConditionOBV;
import com.wishare.finance.domains.voucher.support.fangyuan.entity.VoucherBillRuleConditionOBV;
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
public class ListPushBillTemplateConditionHandler extends AbstractJsonTypeHandler<List<VoucherBillRuleConditionOBV>> {

    @Override
    protected List<VoucherBillRuleConditionOBV> parse(String json) {
        return JSONObject.parseArray(json, VoucherBillRuleConditionOBV.class);
    }

    @Override
    protected String toJson(List<VoucherBillRuleConditionOBV> obj) {
        return JSONObject.toJSONString(obj);
    }
}
