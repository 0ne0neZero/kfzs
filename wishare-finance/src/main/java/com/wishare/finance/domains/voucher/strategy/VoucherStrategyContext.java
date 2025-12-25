package com.wishare.finance.domains.voucher.strategy;

import com.wishare.finance.domains.voucher.consts.enums.VoucherEventTypeEnum;
import com.wishare.finance.domains.voucher.strategy.core.PushMode;
import com.wishare.finance.domains.voucher.strategy.core.VoucherStrategy;
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
public class VoucherStrategyContext {

    private static final HashMap<String, VoucherStrategy> strategies = new HashMap<>();

    public VoucherStrategyContext(List<VoucherStrategy> voucherStrategies) {

        if (CollectionUtils.isNotEmpty(voucherStrategies)){
            for (VoucherStrategy voucherStrategy : voucherStrategies) {
                strategies.put(getStrategyKey(voucherStrategy.eventType(), voucherStrategy.mode()), voucherStrategy);
            }
        }
    }

    /**
     * 获取策略
     * @param code 触发事件类型
     * @return
     */
    public static VoucherStrategy getStrategy(int code, PushMode mode){
        return getStrategy(VoucherEventTypeEnum.valueOfByCode(code), mode);
    }

    /**
     * 获取策略
     * @param eventType
     * @return
     */
    public static VoucherStrategy getStrategy(VoucherEventTypeEnum eventType, PushMode mode){
        return strategies.get(getStrategyKey(eventType, mode));
    }

    /**
     * 获取辅助核算key
     * @param eventType 事件类型
     * @param mode 模式
     * @return
     */
    private static String getStrategyKey(VoucherEventTypeEnum eventType, PushMode mode){
        return eventType.getCode() + "_" + mode;
    }

}
