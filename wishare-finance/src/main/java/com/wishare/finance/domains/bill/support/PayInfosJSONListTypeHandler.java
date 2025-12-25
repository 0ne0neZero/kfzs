package com.wishare.finance.domains.bill.support;

import com.wishare.finance.domains.bill.entity.PayInfo;
import com.wishare.finance.infrastructure.support.JSONListTypeHandler;

/**
* @description:receivable_bill表pay_infos字段json解析
* @author: zhenghui
* @date: 2023/3/29 10:17
*/
public class PayInfosJSONListTypeHandler extends JSONListTypeHandler<PayInfo> {

    public PayInfosJSONListTypeHandler(Class<?> type) {
        super(type, PayInfo.class);
    }
}
