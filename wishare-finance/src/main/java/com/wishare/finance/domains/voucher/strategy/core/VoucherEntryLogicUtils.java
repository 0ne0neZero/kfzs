package com.wishare.finance.domains.voucher.strategy.core;

import com.wishare.finance.apps.model.yuanyang.fo.ProcessAccountBookF;
import com.wishare.finance.domains.voucher.consts.enums.VoucherTemplateLogicCodeEnum;
import com.wishare.finance.domains.voucher.consts.enums.bpm.VoucherTemplateBpmLogicCodeEnum;
import com.wishare.finance.domains.voucher.strategy.bpm.logic.BpmLogicStrategyContext;
import com.wishare.finance.domains.voucher.strategy.logic.LogicStrategyContext;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 凭证逻辑信息
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/4/12
 */
public class VoucherEntryLogicUtils {

    /**
     * 业务单据信息转换为指定类型的金额
     * @param businessBills 业务单据信息
     * @return 金额映射
     */
    public static Map<String, BigDecimal> parseAmountMap(List<VoucherBusinessBill> businessBills){
        Map<String, BigDecimal> logicValues = new HashMap<>();
        for (VoucherBusinessBill businessBill : businessBills) {
            for (VoucherTemplateLogicCodeEnum logicCode : VoucherTemplateLogicCodeEnum.values()) {
                BigDecimal amount = LogicStrategyContext.getStrategy(logicCode).logicValue(businessBill);
                logicValues.put(logicCode.getCode(), logicValues.containsKey(logicCode.getCode()) ? amount.add(logicValues.get(logicCode.getCode())): amount);
            }
        }
        return logicValues;
    }

    /**
     * 业务单据信息转换为指定类型的金额
     * @param businessBill 业务单据信息
     * @return 金额映射
     */
    public static Map<String, BigDecimal> parseAmountMap(VoucherBusinessBill businessBill){
        Map<String, BigDecimal> logicValues = new HashMap<>();
        for (VoucherTemplateLogicCodeEnum logicCode : VoucherTemplateLogicCodeEnum.values()) {
            BigDecimal amount = LogicStrategyContext.getStrategy(logicCode).logicValue(businessBill);
            logicValues.put(logicCode.getCode(), logicValues.containsKey(logicCode.getCode()) ? amount.add(logicValues.get(logicCode.getCode())): amount);
        }
        return logicValues;
    }

    /**
     * @param accountBook
     * @param index
     * @param bpmBillTypeCode {@linkplain BusinessBillTypeEnum}
     * @return
     */
    public static Map<String, BigDecimal> parseAmountMap(ProcessAccountBookF accountBook, int index,String bpmBillTypeCode){
        Map<String, BigDecimal> logicValues = new HashMap<>();
        for (VoucherTemplateBpmLogicCodeEnum logicCode : VoucherTemplateBpmLogicCodeEnum.values()) {
            BigDecimal amount = BpmLogicStrategyContext.getStrategy(logicCode).logicValue(accountBook, index,bpmBillTypeCode);
            logicValues.put(logicCode.getCode(), logicValues.containsKey(logicCode.getCode()) ? amount.add(logicValues.get(logicCode.getCode())): amount);
        }
        return logicValues;
    }

}
