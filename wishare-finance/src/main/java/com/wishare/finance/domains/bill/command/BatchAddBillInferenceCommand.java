package com.wishare.finance.domains.bill.command;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @description:
 * @author: pgq
 * @since: 2022/10/26 9:40
 * @version: 1.0.0
 */
@Getter
@Setter
public class BatchAddBillInferenceCommand {

    /**
     * 账单ids
     */
    private List<Long> billIds;

    /**
     * 账单类型
     */
    private Integer billType;

    /**
     * 触发事件类型
     */
    private Integer eventType;
}
