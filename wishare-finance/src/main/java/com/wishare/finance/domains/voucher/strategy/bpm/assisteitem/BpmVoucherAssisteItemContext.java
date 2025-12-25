package com.wishare.finance.domains.voucher.strategy.bpm.assisteitem;

import com.wishare.finance.domains.configure.subject.consts.enums.AssisteItemTypeEnum;
import com.wishare.finance.domains.voucher.consts.enums.bpm.VoucherTemplateBpmAssisteItemEnum;
import com.wishare.finance.domains.voucher.strategy.assisteitem.VoucherAssisteItemStrategy;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class BpmVoucherAssisteItemContext {

    private static final Map<VoucherTemplateBpmAssisteItemEnum, BpmVoucherAssisteItemStrategy> strategy = new HashMap<>();

    public BpmVoucherAssisteItemContext(List<BpmVoucherAssisteItemStrategy> strategies) {
        if (CollectionUtils.isNotEmpty(strategies)){
            for (BpmVoucherAssisteItemStrategy voucherAssisteItemStrategy : strategies) {
                strategy.put(voucherAssisteItemStrategy.type(), voucherAssisteItemStrategy);
            }
        }
    }

    /**
     * 获取策略
     * @param type
     * @return
     */
    public static BpmVoucherAssisteItemStrategy getStrategy(String type){
        return strategy.get(VoucherTemplateBpmAssisteItemEnum.valueOfByCode(type));
    }

    /**
     * 获取策略
     * @param assisteItemType
     * @return
     */
    public static BpmVoucherAssisteItemStrategy getStrategy(VoucherTemplateBpmAssisteItemEnum assisteItemType){
        return strategy.get(assisteItemType);
    }

}
