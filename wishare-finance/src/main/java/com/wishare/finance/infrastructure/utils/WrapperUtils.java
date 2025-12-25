package com.wishare.finance.infrastructure.utils;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wishare.finance.domains.voucher.strategy.core.AbstractVoucherStrategy;
import com.wishare.finance.domains.voucher.strategy.core.ManualVoucherStrategyCommand;
import com.wishare.finance.domains.voucher.strategy.core.VoucherStrategyCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Objects;

public class WrapperUtils {
    private static final Logger log = LoggerFactory.getLogger(AbstractVoucherStrategy.class);

    public static void logWrapper(VoucherStrategyCommand command, QueryWrapper<?> wrapper) {
        String jsonString = wrapper.getSqlSegment();
        Map<String, Object> paramNameValuePairs = wrapper.getParamNameValuePairs();
        for (String key : paramNameValuePairs.keySet()) {
            String s1 = "#{ew.paramNameValuePairs." + key + "}";
            Object value = paramNameValuePairs.get(key);
            if (Objects.isNull(value)) {
                log.info("{}键找不到对应value", key);
                continue;
            }
            jsonString= jsonString.replace(s1, value.toString());
        }
        if (ObjectUtil.isNotNull(command)){
            log.info("凭证规则id-{},sql查询条件参数：{}",command.getRuleId(), jsonString);
        }else {
            log.info("sql查询条件参数：{}", jsonString);
        }

    }

    public static void logWrapperPro(VoucherStrategyCommand command, QueryWrapper<?> wrapper,String methodName) {
        String jsonString = wrapper.getSqlSegment();
        Map<String, Object> paramNameValuePairs = wrapper.getParamNameValuePairs();
        for (String key : paramNameValuePairs.keySet()) {
            String s1 = "#{ew.paramNameValuePairs." + key + "}";
            Object value = paramNameValuePairs.get(key);
            if (Objects.isNull(value)) {
                log.info("{}键找不到对应value", key);
                continue;
            }
            jsonString= jsonString.replace(s1, value.toString());
        }
        if (ObjectUtil.isNotNull(command)){
            log.info("凭证规则id-{},方法名-{},sql查询条件参数：{}",command.getRuleId(),methodName, jsonString);
        }else {
            log.info("sql查询条件参数：{}", jsonString);
        }
    }

    public static void logWrapper(QueryWrapper<?> wrapper) {

        logWrapper(null,wrapper);
    }
}
