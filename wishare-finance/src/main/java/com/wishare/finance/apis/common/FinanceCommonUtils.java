package com.wishare.finance.apis.common;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author szh
 * @date 2024/5/14 14:12
 */
public class FinanceCommonUtils {


    public static String F2Y(BigDecimal s){
        if (ObjectUtil.isNull(s)){
            return "";
        }
        return NumberUtil.div(s, (new BigDecimal("100")), 2, RoundingMode.UP).toString();
    }

    public static String F2Y(Long s){
        if (ObjectUtil.isNull(s)){
            return "";
        }
        return NumberUtil.div(new BigDecimal(s), (new BigDecimal("100")), 2, RoundingMode.UP).toString();
    }
}
