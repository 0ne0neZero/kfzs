package com.wishare.finance.domains.bill.command;

import lombok.Getter;
import lombok.Setter;

/**
 * 作废申请参数
 * @author dxclay
 * @since 2022-08-22
 */
@Getter
@Setter
public class ApplyInvalidCommand {

    /**
     *  账单id
     */
    private Long billId;

    /**
     * 审核原因
     */
    private String reason;

}
