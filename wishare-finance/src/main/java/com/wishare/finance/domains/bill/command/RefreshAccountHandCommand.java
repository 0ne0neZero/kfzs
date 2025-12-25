package com.wishare.finance.domains.bill.command;

import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import com.wishare.finance.domains.bill.event.BillAction;
import lombok.Getter;
import lombok.Setter;

/**
 * 更新交账记录命令
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/1/12
 */
@Getter
@Setter
public class RefreshAccountHandCommand {

    private Long billId;

    private BillTypeEnum billType;

    private BillAction action;

    private String supCpUnitId;
    public RefreshAccountHandCommand() {
    }

    public RefreshAccountHandCommand(Long billId, BillTypeEnum billType) {
        this.billId = billId;
        this.billType = billType;
    }

    public RefreshAccountHandCommand(Long billId, BillTypeEnum billType, String supCpUnitId) {
        this.billId = billId;
        this.billType = billType;
        this.supCpUnitId = supCpUnitId;
    }

    public RefreshAccountHandCommand(Long billId, BillTypeEnum billType,BillAction action) {
        this.billId = billId;
        this.billType = billType;
        this.action = action;
    }

    public RefreshAccountHandCommand(Long billId, BillTypeEnum billType, BillAction action,
        String supCpUnitId) {
        this.billId = billId;
        this.billType = billType;
        this.action = action;
        this.supCpUnitId = supCpUnitId;
    }
}
