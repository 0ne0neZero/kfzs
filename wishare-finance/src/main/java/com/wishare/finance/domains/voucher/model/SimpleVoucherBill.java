package com.wishare.finance.domains.voucher.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @description: 用于凭证展示存储的账单类
 * @author: pgq
 * @since: 2023/2/21 11:31
 * @version: 1.0.0
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SimpleVoucherBill {

    private Long billId;

    private String billNo;

    private Integer billType;
}
