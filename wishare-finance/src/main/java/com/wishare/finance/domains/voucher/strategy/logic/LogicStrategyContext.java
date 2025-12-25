package com.wishare.finance.domains.voucher.strategy.logic;

import com.wishare.finance.domains.voucher.consts.enums.VoucherTemplateLogicCodeEnum;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 逻辑策略上下文
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/4/12
 */
@Component
public class LogicStrategyContext {

    private static final Map<VoucherTemplateLogicCodeEnum, LogicStrategy> strategy = new HashMap<>();

    public LogicStrategyContext(List<LogicStrategy> strategies) {
        if (CollectionUtils.isNotEmpty(strategies)){
            for (LogicStrategy logicStrategy : strategies) {
                strategy.put(logicStrategy.logicCode(), logicStrategy);
            }
        }
    }

    /**
     * 获取策略
     * @param code
     * @return
     */
    public static LogicStrategy getStrategy(String code){
        return strategy.get(VoucherTemplateLogicCodeEnum.valueOfByCode(code));
    }

    /**
     * 获取策略
     * @param logicCode
     * @return
     */
    public static LogicStrategy getStrategy(VoucherTemplateLogicCodeEnum logicCode){
        return strategy.get(logicCode);
    }


}
