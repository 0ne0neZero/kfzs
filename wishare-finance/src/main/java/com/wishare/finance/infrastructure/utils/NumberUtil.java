package com.wishare.finance.infrastructure.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * 数字工具类
 */
public class NumberUtil {

    /**
     * 判断是否数字
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        if (StringUtils.isNotBlank(str)) {
            return str.matches("-?\\d+(\\.\\d+)?");
        } else {
            return false;
        }
    }

    /**
     * 校验税率是否正确
     * 0%到100%，数字后可带小数
     * @param taxRateStr
     * @return
     */
    public static boolean isValidTaxRate(String taxRateStr) {
        // 正则表达式：匹配 0-100 的数字，包括小数
        // 分为三部分：0%, 1-99% (可带小数点), 100%
        String regex = "^((100(\\.0+)?%)|([0-9]|[1-9][0-9])(\\.\\d+)?%)$";

        // 返回字符串是否匹配正则表达式
        return taxRateStr != null && taxRateStr.matches(regex);
    }

}
