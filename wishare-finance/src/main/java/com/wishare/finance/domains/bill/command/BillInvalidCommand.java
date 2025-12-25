package com.wishare.finance.domains.bill.command;

import lombok.Getter;
import lombok.Setter;

/**
 * 账单作废命令
 *
 * @author yancao
 */
@Getter
@Setter
public class BillInvalidCommand {

    /**
     * 账单id
     */
    private Long billId;

    /**
     * 作废原因
     */
    private String reason;

}
