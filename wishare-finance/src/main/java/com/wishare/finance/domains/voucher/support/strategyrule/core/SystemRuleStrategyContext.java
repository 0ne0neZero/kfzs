package com.wishare.finance.domains.voucher.support.strategyrule.core;


import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

/**
 * 推凭策略上下文
 *
 * @author zmh
 * @version 1.0
 * @since 2023/12/02
 */
@Component
public class SystemRuleStrategyContext {

    private static final HashMap<Integer, SystemRuleStrategy> strategies = new HashMap<>();

    public SystemRuleStrategyContext(List<SystemRuleStrategy> systemRuleStrategies) {

        if (CollectionUtils.isNotEmpty(systemRuleStrategies)){
            for (SystemRuleStrategy ruleStrategy : systemRuleStrategies) {
                strategies.put(getStrategyKey(ruleStrategy.eventType()), ruleStrategy);
            }
        }
    }

    /**
     * 获取策略
     * @param code 触发事件类型
     * @return
     */
    public static SystemRuleStrategy getStrategy(int code){
        return getStrategy(SystemMethodEnum.valueOfByCode(code));
    }

    /**
     * 获取策略
     * @param eventType
     * @return
     */
    public static SystemRuleStrategy getStrategy(SystemMethodEnum eventType){
        return strategies.get(getStrategyKey(eventType));
    }

    /**
     * @param eventType 事件类型
     * @return
     */
    private static Integer getStrategyKey(SystemMethodEnum eventType){
        return eventType.getCode();
    }

}
