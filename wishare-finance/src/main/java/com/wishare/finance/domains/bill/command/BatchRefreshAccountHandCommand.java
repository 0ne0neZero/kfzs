package com.wishare.finance.domains.bill.command;

import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 更新交账记录命令
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/1/12
 */
@Getter
@Setter
public class BatchRefreshAccountHandCommand {

    private List<Long> billIds;

    BillTypeEnum billType;

    @NotBlank(message = "上级收费单元ID不能为空!")
    private String supCpUnitId;

    public BatchRefreshAccountHandCommand() {
    }

    public BatchRefreshAccountHandCommand(List<Long> billIds, BillTypeEnum billType) {
        this.billIds = billIds;
        this.billType = billType;
    }

    public BatchRefreshAccountHandCommand(List<Long> billIds, BillTypeEnum billType, String supCpUnitId) {
        this.billIds = billIds;
        this.billType = billType;
        this.supCpUnitId = supCpUnitId;
    }
}
