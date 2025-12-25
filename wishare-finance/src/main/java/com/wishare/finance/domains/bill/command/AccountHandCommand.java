package com.wishare.finance.domains.bill.command;

import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * 交账命令
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/1/12
 */
@Getter
@Setter
public class AccountHandCommand {

    private Long billId;

    private BillTypeEnum billType;

    private String supCpUnitId;

    public AccountHandCommand() {
    }

    public AccountHandCommand(Long billId, BillTypeEnum billType) {
        this.billId = billId;
        this.billType = billType;
    }

    public AccountHandCommand(Long billId, BillTypeEnum billType,String supCpUnitId) {
        this.billId = billId;
        this.billType = billType;
        this.supCpUnitId = supCpUnitId;
    }
}
