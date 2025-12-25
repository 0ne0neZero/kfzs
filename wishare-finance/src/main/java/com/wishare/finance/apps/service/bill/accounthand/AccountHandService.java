package com.wishare.finance.apps.service.bill.accounthand;

import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;

/**
 * @description:
 * @author: pgq
 * @since: 2022/9/26 19:16
 * @version: 1.0.0
 */
public interface AccountHandService {

    /**
     * 账单类型
     *
     * @return
     */
    BillTypeEnum getBillType();

    /**
     * 票据红冲
     *
     * @param billId
     * @return
     */
    Boolean invoiceReversal(Long billId);
}
