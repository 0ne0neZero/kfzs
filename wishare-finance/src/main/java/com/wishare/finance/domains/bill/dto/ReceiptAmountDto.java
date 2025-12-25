package com.wishare.finance.domains.bill.dto;

import lombok.Data;

/**
 * @author szh
 * @date 2024/4/7 15:05
 */
@Data
public class ReceiptAmountDto {

    /**
     * gatherBillId
     */
    private Long gatherBillId;
    /**
     * 过滤收款方式为结转和押金结转的收款金额
     */
    private Long noCheckAmount;
}
