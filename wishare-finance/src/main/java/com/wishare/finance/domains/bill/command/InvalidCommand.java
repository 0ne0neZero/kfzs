package com.wishare.finance.domains.bill.command;

import lombok.Getter;
import lombok.Setter;

/**
 * 作废命令
 *
 * @author yancao
 */
@Getter
@Setter
public class InvalidCommand {

    /**
     * 账单id
     */
    private Long billId;

    /**
     * 作废原因
     */
    private String reason;

    /**
     * 项目id
     */
    private String supCpUnitId;


}
