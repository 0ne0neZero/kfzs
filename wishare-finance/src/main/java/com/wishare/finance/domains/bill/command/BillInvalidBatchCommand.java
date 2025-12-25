package com.wishare.finance.domains.bill.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 批量账单作废命令
 *
 * @author yancao
 */
@Getter
@Setter
@AllArgsConstructor
public class BillInvalidBatchCommand {

    /**
     * 账单id
     */
    private List<Long> billIdList;

}
