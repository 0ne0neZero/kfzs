package com.wishare.finance.domains.bill.command;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @description: 账单推凭查询
 * @author: pgq
 * @since: 2022/10/24 21:30
 * @version: 1.0.0
 */
@Getter
@Setter
public class BatchBillInferenceQuery {

    /**
     * 账单ids
     */
    List<Long> billIds;

    /**
     * 项目id
     */
    private String supCpUnitId;

    /**
     * 账单推凭动作
     */
    private int actionEventCode;
}
