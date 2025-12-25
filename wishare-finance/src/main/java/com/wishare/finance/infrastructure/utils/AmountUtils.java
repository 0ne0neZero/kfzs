package com.wishare.finance.infrastructure.utils;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * 支付金额计算工具类
 *
 * @author: Dxclay
 * @date: 2022/4/7
 */
public class AmountUtils {

    private static final BigDecimal DEFAULT_DIV = BigDecimal.valueOf(100);
    private static final int DEFAULT_SCALE = 2;

    private AmountUtils() {}

    /**
     * 将整数型转为 BigDecimal型（单位：元）
     * @param amount 金额（单位：分）
     * @return BigDecimal
     */
    public static BigDecimal toDecimal(int amount){
        return new BigDecimal(amount).divide(DEFAULT_DIV, DEFAULT_SCALE, RoundingMode.DOWN);
    }

    /**
     * 将整数型转为 BigDecimal型（单位：元）
     * @param amount 金额（单位：分）
     * @return BigDecimal
     */
    public static BigDecimal toDecimal(long amount){
        return new BigDecimal(amount).divide(DEFAULT_DIV, DEFAULT_SCALE, RoundingMode.DOWN);
    }

    /**
     * 将整数型转为 BigDecimal型（单位：元）
     * @param amount 金额（单位：分）
     * @return BigDecimal
     */
    public static BigDecimal toDecimal(Long amount){
        return Objects.isNull(amount) ? BigDecimal.ZERO : new BigDecimal(amount).divide(DEFAULT_DIV, DEFAULT_SCALE, RoundingMode.DOWN);
    }

    /**
     * 将整数型转为 BigDecimal型（单位：分）
     * @param amount 金额（单位：分）
     * @return BigDecimal
     */
    public static BigDecimal longToDecimal(Long amount){
        return Objects.isNull(amount) ? BigDecimal.ZERO : new BigDecimal(amount);
    }


    /**
     * 将double型转为 BigDecimal型（单位：元）
     * @param amount 金额（单位：分）
     * @return BigDecimal
     */
    public static BigDecimal toDecimal(double amount){
        return new BigDecimal(amount).divide(DEFAULT_DIV, DEFAULT_SCALE, RoundingMode.DOWN);
    }

    public static BigDecimal toScale(BigDecimal amount){
        return amount.divide(DEFAULT_DIV, DEFAULT_SCALE, RoundingMode.DOWN);
    }

    /**
     * 将BigDecimal型转换为整数型（单位：分）
     * @param amount 金额（单位：元）
     * @return int
     */
    public static int toInt(BigDecimal amount){
        return amount.multiply(DEFAULT_DIV).intValue();
    }

    /**
     * 将BigDecimal型转换为整数型（单位：分）
     * @param amount 金额（单位：元）
     * @return long
     */
    public static long toLong(BigDecimal amount){
        return amount.multiply(DEFAULT_DIV).longValue();
    }

    /**
     * 将BigDecimal型转换为整数型（单位：分）
     * @param amount 金额（单位：元）
     * @return long
     */
    public static long toLong(String amount){
        return Objects.isNull(amount) || amount.isBlank() ? 0 : new BigDecimal(amount).multiply(DEFAULT_DIV).longValue();
    }

    /**
     * 将BigDecimal型转换为整数型（单位：分）
     * @param amount 金额（单位：元）根据金额的小数位数乘以10的指数
     * @return long
     */
    public static long toLongByScaleLength(String amount){
        return Objects.isNull(amount) || amount.isBlank() ? 0 : new BigDecimal(amount).multiply(
                BigDecimal.valueOf((int) Math.pow(10, new BigDecimal(amount).scale()))
        ).longValue();
    }

    /**
     * 将整数型转为 字符串型（单位：元）
     * @param amount 金额（单位：分）
     * @return 字符串型金额
     */
    public static String toStringAmount(int amount){
        return toDecimal(amount).toString();
    }

    /**
     * 将整数型转为 字符串型（单位：元）
     * @param amount 金额（单位：分）
     * @return 字符串型金额
     */
    public static String toStringAmount(long amount){
        return toDecimal(amount).toString();
    }

    /**
     * 将double型转为 字符串型（单位：元）
     * @param amount 金额（单位：分）
     * @return 字符串型金额
     */
    public static String toStringAmount(double amount){
        return toDecimal(amount).toString();
    }
}
