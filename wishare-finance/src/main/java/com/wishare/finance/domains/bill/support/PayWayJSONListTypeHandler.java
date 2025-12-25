package com.wishare.finance.domains.bill.support;

import com.wishare.finance.domains.bill.entity.PayWay;
import com.wishare.finance.infrastructure.support.JSONListTypeHandler;

/**
 * 减免说明JSON List处理器
 *
 * @Author dxclay
 * @Date 2022/12/28
 * @Version 1.0
 */
public class PayWayJSONListTypeHandler extends JSONListTypeHandler<PayWay> {

    public PayWayJSONListTypeHandler(Class<?> type) {
        super(type, PayWay.class);
    }
}
