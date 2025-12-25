package com.wishare.finance.domains.bill.command;

import lombok.Getter;
import lombok.Setter;

/**
 * @description: 账单推凭查询
 * @author: pgq
 * @since: 2022/10/24 21:30
 * @version: 1.0.0
 */
@Getter
@Setter
public class BillInferenceQuery {

    /**
     * 账单ids
     */
    Long billId;

    /**
     * 项目id
     */
    private String supCpUnitId;

    /**
     * 账单推凭动作
     */
    private int actionEventCode;
}
