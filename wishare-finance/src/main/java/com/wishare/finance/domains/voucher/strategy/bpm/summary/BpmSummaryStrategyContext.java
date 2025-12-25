package com.wishare.finance.domains.voucher.strategy.bpm.summary;

import com.wishare.finance.domains.voucher.consts.enums.VoucherTemplateSummaryTypeEnum;
import com.wishare.finance.domains.voucher.consts.enums.bpm.VoucherTemplateBpmSummaryTypeEnum;
import com.wishare.finance.domains.voucher.strategy.summary.SummaryStrategy;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class BpmSummaryStrategyContext {

    private static final Map<VoucherTemplateBpmSummaryTypeEnum, BpmSummaryStrategy> strategy = new HashMap<>();

    public BpmSummaryStrategyContext(List<BpmSummaryStrategy> strategies) {
        if (CollectionUtils.isNotEmpty(strategies)){
            for (BpmSummaryStrategy summaryStrategy : strategies) {
                strategy.put(summaryStrategy.summaryType(), summaryStrategy);
            }
        }
    }

    /**
     * 获取策略
     * @param type
     * @return
     */
    public static BpmSummaryStrategy getStrategy(String type){
        return strategy.get(VoucherTemplateBpmSummaryTypeEnum.valueOfByCode(type));
    }

    /**
     * 获取策略
     * @param summaryType
     * @return
     */
    public static BpmSummaryStrategy getStrategy(VoucherTemplateBpmSummaryTypeEnum summaryType){
        return strategy.get(summaryType);
    }

}
