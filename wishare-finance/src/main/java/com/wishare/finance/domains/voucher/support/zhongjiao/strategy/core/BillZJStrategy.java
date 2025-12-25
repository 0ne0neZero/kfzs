package com.wishare.finance.domains.voucher.support.zhongjiao.strategy.core;


import com.wishare.finance.domains.voucher.support.zhongjiao.enums.ZJTriggerEventBillTypeEnum;

import java.util.List;

/**
 * 凭证策略
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/3/22
 */
public interface BillZJStrategy<C extends BillZJStrategyCommand> {

    /**
     * 事件类型
     *
     * @return 事件类型
     */
    ZJTriggerEventBillTypeEnum eventType();

    /**
     *  推单模式
     *
     * @return 推单模式
     */
    ZJPushMethod pushMethod();

    /**
     * 执行策略
     */
    void execute(C command);

    default List<PushZJBusinessBillForSettlement> autoExecute(AutoBillZJStrategyCommand command){
        return null;
    };

}
