package com.wishare.finance.domains.voucher.strategy.assisteitem;

import com.wishare.finance.domains.configure.subject.consts.enums.AssisteItemTypeEnum;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 凭证辅助核算上下文
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/4/11
 */
@Component
public class VoucherAssisteItemContext {

    private static final Map<AssisteItemTypeEnum, VoucherAssisteItemStrategy> strategy = new HashMap<>();

    public VoucherAssisteItemContext(List<VoucherAssisteItemStrategy> strategies) {
        if (CollectionUtils.isNotEmpty(strategies)){
            for (VoucherAssisteItemStrategy voucherAssisteItemStrategy : strategies) {
                strategy.put(voucherAssisteItemStrategy.type(), voucherAssisteItemStrategy);
            }
        }
    }

    /**
     * 获取策略
     * @param type
     * @return
     */
    public static VoucherAssisteItemStrategy getStrategy(int type){
       return strategy.get(AssisteItemTypeEnum.valueOfByCode(type));
    }

    /**
     * 获取策略
     * @param assisteItemType
     * @return
     */
    public static VoucherAssisteItemStrategy getStrategy(AssisteItemTypeEnum assisteItemType){
        return strategy.get(assisteItemType);
    }


}
