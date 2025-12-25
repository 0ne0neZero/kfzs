package com.wishare.finance.domains.voucher.model;

import lombok.Data;

/**
 * @author szh
 * @date 2024/6/3 13:57
 */
@Data
public class CarryoverDetailDto {

    /**
     *  结转账单类型 1-应收账单， 2-预收账单， 3-临时收费账单
     */
    private Integer billType;

    /**
     * 结转账单id
     */
    private Long carriedBillId;
}
