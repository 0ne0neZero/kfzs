package com.wishare.finance.domains.bill.dto;

import com.wishare.finance.domains.bill.entity.ReceivableBill;
import lombok.Getter;
import lombok.Setter;

/**
 * @author yancao
 */
@Getter
@Setter
public class ReceivableBillsDto extends ReceivableBill {

    /**
     * 账单id集合
     */
    private String billIds;
}
