package com.wishare.finance.domains.bill.command;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 对账命令
 *
 * @Author dxclay
 * @Date 2022/10/28
 * @Version 1.0
 */
@Getter
@Setter
public class ReconcileBatchCommand {

    /**
     * 账单id列表
     */
    private List<Long> billIds;

    /**
     * 项目id列表
     */
    private String supCpUnitId;

    /**
     * 对账结果(false:未对平，true:已对平)
     */
    private boolean result;

}
