package com.wishare.finance.domains.voucher.support.zhongjiao.strategy.core;


import com.wishare.finance.domains.voucher.support.zhongjiao.enums.ZJTriggerEventBillTypeEnum;
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
public class PushBillZJStrategyContext {

    private static final HashMap<String, BillZJStrategy> strategies = new HashMap<>();

    public PushBillZJStrategyContext(List<BillZJStrategy> billStrategies) {

        if (CollectionUtils.isNotEmpty(billStrategies)){
            for (BillZJStrategy billStrategy : billStrategies) {
                strategies.put(getStrategyKey(billStrategy.eventType(), billStrategy.pushMethod()), billStrategy);
            }
        }
    }

    /**
     * 获取策略
     * @param code 触发事件类型
     * @return
     */
    public static BillZJStrategy getStrategy(int code, ZJPushMethod mode){
        return getStrategy(ZJTriggerEventBillTypeEnum.valueOfByCode(code), mode);
    }

    /**
     * 获取策略
     * @param eventType
     * @return
     */
    public static BillZJStrategy getStrategy(ZJTriggerEventBillTypeEnum eventType, ZJPushMethod mode){
        return strategies.get(getStrategyKey(eventType, mode));
    }

    /**
     * @param eventType 事件类型
     * @param mode 模式
     * @return
     */
    private static String getStrategyKey(ZJTriggerEventBillTypeEnum eventType, ZJPushMethod mode){
        return eventType.getCode() + "_" + mode;
    }

}
