package com.wishare.finance.domains.bill.support;

import com.wishare.finance.domains.bill.entity.DiscountOBV;
import com.wishare.finance.infrastructure.support.JSONListTypeHandler;

/**
 * 减免说明JSON List处理器
 *
 * @Author dxclay
 * @Date 2022/12/28
 * @Version 1.0
 */
public class DiscountOBVJSONListTypeHandler extends JSONListTypeHandler<DiscountOBV> {

    public DiscountOBVJSONListTypeHandler(Class<?> type) {
        super(type, DiscountOBV.class);
    }
}
