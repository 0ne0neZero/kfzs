package com.wishare.finance.domains.voucher.strategy.bpm.logic;

import com.wishare.finance.domains.voucher.consts.enums.VoucherTemplateLogicCodeEnum;
import com.wishare.finance.domains.voucher.consts.enums.bpm.VoucherTemplateBpmLogicCodeEnum;
import com.wishare.finance.domains.voucher.strategy.logic.LogicStrategy;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class BpmLogicStrategyContext {

    private static final Map<VoucherTemplateBpmLogicCodeEnum, BpmLogicStrategy> strategy = new HashMap<>();

    public BpmLogicStrategyContext(List<BpmLogicStrategy> strategies) {
        if (CollectionUtils.isNotEmpty(strategies)){
            for (BpmLogicStrategy logicStrategy : strategies) {
                strategy.put(logicStrategy.logicCode(), logicStrategy);
            }
        }
    }

    /**
     * 获取策略
     * @param code
     * @return
     */
    public static BpmLogicStrategy getStrategy(String code){
        return strategy.get(VoucherTemplateBpmLogicCodeEnum.valueOfByCode(code));
    }

    /**
     * 获取策略
     * @param logicCode
     * @return
     */
    public static BpmLogicStrategy getStrategy(VoucherTemplateBpmLogicCodeEnum logicCode){
        return strategy.get(logicCode);
    }


}
