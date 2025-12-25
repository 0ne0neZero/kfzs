package com.wishare.finance.domains.configure.subject.strategy;

import com.wishare.finance.domains.configure.subject.consts.enums.AssisteItemTypeEnum;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 辅助核算上下文
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/4/11
 */
@Component
public class AssisteItemContext {

    private static final Map<AssisteItemTypeEnum, AssisteItemStrategy> strategy = new HashMap<>();

    public AssisteItemContext(List<AssisteItemStrategy> strategies) {
        if (CollectionUtils.isNotEmpty(strategies)){
            for (AssisteItemStrategy assisteItemStrategy : strategies) {
                strategy.put(assisteItemStrategy.type(), assisteItemStrategy);
            }
        }
    }

    /**
     * 获取策略
     * @param type
     * @return
     */
    public static AssisteItemStrategy getStrategy(int type){
       return strategy.get(AssisteItemTypeEnum.valueOfByCode(type));
    }

    /**
     * 获取策略
     * @param assisteItemType
     * @return
     */
    public static AssisteItemStrategy getStrategy(AssisteItemTypeEnum assisteItemType){
        return strategy.get(assisteItemType);
    }


}
