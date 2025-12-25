package com.wishare.finance.infrastructure.utils;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 发票工具类
 * @author luzhonghe
 * @date 2023/3/24
 * @Description:
 */
@Slf4j
public class InvoiceUtil {

    /**
     * 票本号码计算
     * @param invoiceNumber
     * @param value
     * @return
     */
    public static String addOrSubtract(String invoiceNumber, long value) {
        long num = Long.parseLong(invoiceNumber);
        num += value;
        // 根据原字符串长度生成对应的格式化字符串
        String format = "%0" + invoiceNumber.length() + "d";
        return String.format(format, num);
    }

    /**
     * 税率计算
     * 税额=价税合计金额*税率/(1+税率）
     * @param taxRateText 文本: 9% -> 0.09
     * @param priceTaxPrice
     * @return
     */
    public static BigDecimal getTaxAmount(String taxRateText, Long priceTaxPrice) {
        BigDecimal invoiceAmount = new BigDecimal(priceTaxPrice)
                .divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
        BigDecimal taxRate = new BigDecimal(taxRateText);
        return invoiceAmount.multiply(taxRate)
                .divide(new BigDecimal(1).add(taxRate), 2, RoundingMode.HALF_UP);
    }

    public static Long getLongTaxAmount(String taxRateText, Long priceTaxPrice) {
        BigDecimal taxAmount = getTaxAmount(taxRateText, priceTaxPrice);
        return AmountUtils.toLong(taxAmount);
    }

    public static void main(String[] args) {
        BigDecimal taxAmount = getTaxAmount("0.13", 91560L);
        System.out.println(taxAmount.toPlainString());
    }

}
