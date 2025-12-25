package com.wishare.finance.domains.voucher.strategy.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 凭证数据类
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/4/12
 */
@Component
@Slf4j
public class VoucherData {

    public static String makerId;
    public static String makerName;

    public static Long defaultMerchantId;

    public static Long defaultTempCustomerId;

    @Value("${wishare.finance.voucher.ncc.makerId:AutoAC}")
    public void setMakerId(String makerId) {
        VoucherData.makerId = makerId;
    }

    @Value("${wishare.finance.voucher.ncc.makerName:系统推凭}")
    public void setMakerName(String makerName) {
        VoucherData.makerName = makerName;
    }

    @Value("${wishare.finance.voucher.ncc.merchantId:13098236448600001}")
    public void setDefaultMerchantId(Long merchantId) {
        VoucherData.defaultMerchantId = merchantId;
    }

    //customerId目前取的是研发库（临时客商）
    @Value("${wishare.finance.voucher.ncc.tempCustomerId:13511808115820002}")
    public void setDefaultTempCustomerId(Long tempCustomerId) {
        VoucherData.defaultTempCustomerId = tempCustomerId;
    }
}
