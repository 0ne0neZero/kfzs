package com.wishare.finance.domains.bill.command;

import com.wishare.finance.domains.bill.consts.enums.BillApproveStateEnum;
import com.wishare.starter.beans.PageF;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 批量审批账单命令
 */
@Setter
@Getter
public class BatchApproveReceivableBillCommand extends BatchApproveBillCommand{

    public BatchApproveReceivableBillCommand() {
    }

    public BatchApproveReceivableBillCommand(PageF<SearchF<?>> query, List<Long> billIds, Integer approveState) {
        super(query, billIds, BillApproveStateEnum.valueOfByCode(approveState));
    }

}
