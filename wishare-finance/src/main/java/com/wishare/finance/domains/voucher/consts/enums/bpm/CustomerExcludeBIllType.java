package com.wishare.finance.domains.voucher.consts.enums.bpm;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * 不需要校验客商映射表的场景
 */
public class CustomerExcludeBIllType {

    public static Set<String> excludeType;

    static {
        Set<String> excludeType = new HashSet<>();
        excludeType.add(BusinessBillTypeEnum.资金上缴.getCode());
        excludeType.add(BusinessBillTypeEnum.资金下拨.getCode());
        excludeType.add(BusinessBillTypeEnum.资金下拨付款.getCode());
        excludeType.add(BusinessBillTypeEnum.借款申请.getCode());
        excludeType.add(BusinessBillTypeEnum.工资支付.getCode());
        CustomerExcludeBIllType.excludeType = Collections.unmodifiableSet(excludeType);
    }



}
