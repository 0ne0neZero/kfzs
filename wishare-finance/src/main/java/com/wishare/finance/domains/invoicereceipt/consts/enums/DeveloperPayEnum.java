package com.wishare.finance.domains.invoicereceipt.consts.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 是否为开发代付-开票
 * @author szh
 * @date 2024/5/11 9:33
 */

@AllArgsConstructor
@Getter
public enum DeveloperPayEnum {
    /**
     * 是为开发代付-开票
     */
    YES("TJ")
    ;

    /**
     * 收据前缀
     */
    private final String receiptNoPrefix;
}
