package com.wishare.finance.domains.voucher.strategy.summary;

import com.wishare.finance.domains.voucher.consts.enums.VoucherTemplateLogicCodeEnum;
import com.wishare.finance.domains.voucher.consts.enums.VoucherTemplateSummaryTypeEnum;
import com.wishare.finance.domains.voucher.strategy.logic.LogicStrategy;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/4/12
 */
@Component
public class SummaryStrategyContext {

    private static final Map<VoucherTemplateSummaryTypeEnum, SummaryStrategy> strategy = new HashMap<>();

    public SummaryStrategyContext(List<SummaryStrategy> strategies) {
        if (CollectionUtils.isNotEmpty(strategies)){
            for (SummaryStrategy summaryStrategy : strategies) {
                strategy.put(summaryStrategy.summaryType(), summaryStrategy);
            }
        }
    }

    /**
     * 获取策略
     * @param type
     * @return
     */
    public static SummaryStrategy getStrategy(String type){
        return strategy.get(VoucherTemplateSummaryTypeEnum.valueOfByCode(type));
    }

    /**
     * 获取策略
     * @param summaryType
     * @return
     */
    public static SummaryStrategy getStrategy(VoucherTemplateSummaryTypeEnum summaryType){
        return strategy.get(summaryType);
    }

}
