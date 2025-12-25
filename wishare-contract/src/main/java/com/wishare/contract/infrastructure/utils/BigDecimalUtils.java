package com.wishare.contract.infrastructure.utils;

import com.wishare.owl.exception.OwlBizException;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author hhb
 * @describe
 * @date 2025/6/11 17:41
 */
@Slf4j
public class BigDecimalUtils {
    public static Map<String, BigDecimal> calculateProportion(Map<String, BigDecimal> funTotalAmount,
                                                              BigDecimal totalAmount) {
        Map<String, BigDecimal> result = new LinkedHashMap<>();
        BigDecimal remaining = BigDecimal.ONE; // 初始剩余比例为1(100%)
        int size = funTotalAmount.size();
        int count = 0;

        for (Map.Entry<String, BigDecimal> entry : funTotalAmount.entrySet()) {
            count++;
            String fundId = entry.getKey();
            BigDecimal amount = entry.getValue();

            // 如果是最后一项，使用倒减逻辑
            if (count == size) {
                result.put(fundId, remaining);
            } else {
                // 计算当前比例
                BigDecimal proportion = amount.divide(totalAmount, 6, RoundingMode.HALF_UP);
                result.put(fundId, proportion);
                remaining = remaining.subtract(proportion);
            }
        }

        return result;
    }


    /**
     * 计算各基金金额在总金额中的比例，并按比例分配目标金额
     * @param funTotalAmount 各基金原始金额Map（保持顺序）
     * @param totalAmount 原始总金额
     * @param targetTotalAmount 要分配的目标总金额
     * @return 按比例分配后的金额Map（最后一项使用倒减逻辑）
     */
    public static Map<String, BigDecimal> calculateDistributedAmounts(
            Map<String, BigDecimal> funTotalAmount,
            BigDecimal totalAmount,
            BigDecimal targetTotalAmount) {
        targetTotalAmount = Objects.isNull(targetTotalAmount) ? BigDecimal.ZERO : targetTotalAmount;
        // 参数校验
        if (totalAmount.compareTo(BigDecimal.ZERO) <= 0) {
            //throw new OwlBizException("扣款金额不能大于实际结算金额");
            log.error("BigDecimalUtils.calculateDistributedAmounts调用异常，扣款金额不能大于实际结算金额");
            funTotalAmount = funTotalAmount.keySet().stream()
                    .collect(Collectors.toMap(
                            key -> key,
                            key -> BigDecimal.ZERO
                    ));
            return funTotalAmount;
        }
        if (targetTotalAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new OwlBizException("结算金额不能为负数");
        }

        Map<String, BigDecimal> result = new LinkedHashMap<>();
        BigDecimal remainingAmount = targetTotalAmount; // 剩余待分配金额
        int size = funTotalAmount.size();
        int count = 0;

        for (Map.Entry<String, BigDecimal> entry : funTotalAmount.entrySet()) {
            count++;
            String fundId = entry.getKey();
            BigDecimal originalAmount = entry.getValue();

            // 计算当前基金原始比例
            BigDecimal ratio = originalAmount.divide(totalAmount, 10, RoundingMode.HALF_UP);

            // 如果是最后一项，使用倒减逻辑
            if (count == size) {
                result.put(fundId, remainingAmount);
            } else {
                // 按比例计算分配金额
                BigDecimal distributedAmount = targetTotalAmount.multiply(ratio)
                        .setScale(2, RoundingMode.HALF_UP);
                result.put(fundId, distributedAmount);
                remainingAmount = remainingAmount.subtract(distributedAmount);
            }
        }

        return result;
    }
}
