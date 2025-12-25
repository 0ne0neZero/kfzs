package com.wishare.finance.domains.voucher.support.fangyuan.strategy.core;


import com.wishare.finance.domains.voucher.support.fangyuan.enums.TriggerEventBillTypeEnum;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

/**
 * 推凭策略上下文
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/4/13
 */
@Component
public class PushBillStrategyContext {

    private static final HashMap<String, BillStrategy<BillStrategyCommand>> strategies = new HashMap<>();

    public PushBillStrategyContext(List<BillStrategy> billStrategies) {
        if (CollectionUtils.isNotEmpty(billStrategies)){
            for (BillStrategy<BillStrategyCommand> billStrategy : billStrategies) {
                strategies.put(getStrategyKey(billStrategy.eventType(), billStrategy.pushMethod()), billStrategy);
            }
        }
    }

    /**
     * 获取策略
     * @param code 触发事件类型
     * @return
     */
    public static BillStrategy<BillStrategyCommand> getStrategy(int code, PushMethod mode){
        return getStrategy(TriggerEventBillTypeEnum.valueOfByCode(code), mode);
    }

    /**
     * 获取策略
     * @param eventType
     * @return
     */
    public static BillStrategy<BillStrategyCommand> getStrategy(TriggerEventBillTypeEnum eventType, PushMethod mode){
        return strategies.get(getStrategyKey(eventType, mode));
    }

    /**
     * @param eventType 事件类型
     * @param mode 模式
     * @return
     */
    private static String getStrategyKey(TriggerEventBillTypeEnum eventType, PushMethod mode){
        return eventType.getCode() + "_" + mode;
    }

}
