package com.wishare.finance.infrastructure.utils;

import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.starter.exception.BizException;
import com.wishare.starter.helpers.RedisHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.wishare.finance.infrastructure.utils.IdempotentUtil.IdempotentEnum.REVERSE;

/**
 * 幂等工具类
 */
public class IdempotentUtil {

    private static final Logger log = LoggerFactory.getLogger(IdempotentUtil.class);

    public enum IdempotentEnum {

        REVERSE("reverse", "冲销"),
        UNIFIED_ACCOUNTING("unifiedAccounting", "统一入账"),
        BUSINESS_BILL_RULE("businessBillRule", "运行报账规则"),
        VOID_RECEIPT("voidReceipt", "作废收据"),
        RECONCILIATION_MERCHANT("reconciliationMerchant", "商户清分对账"),
        RE_RECORD_INVOICE("reRecordInvoice", "蓝票补录认领");



        private final String code;
        private final String name;

        IdempotentEnum(String code, String name) {
            this.code = code;
            this.name = name;
        }

    }

    public static void setIdempotent(String idempotentKey, IdempotentEnum method, int seconds, ErrorMessage errorMessage) {
        String messageKey = method.code + idempotentKey;
        try {
            if (!RedisHelper.setNotExists(messageKey, "1")) {
                log.info(method.name + errorMessage.msg() + "：{}", idempotentKey);
                throw BizException.throw403(errorMessage.msg());
            }
        } finally {
            RedisHelper.expire(messageKey, seconds);
        }


    }

}
