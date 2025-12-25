package com.wishare.finance.domains.bill.command;

import com.wishare.finance.domains.bill.consts.enums.BillApproveStateEnum;
import com.wishare.starter.beans.PageF;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 批量审批预收账单命令
 * @author yancao
 */
@Setter
@Getter
public class BatchApproveAdvanceBillCommand extends BatchApproveBillCommand{

    public BatchApproveAdvanceBillCommand() {
    }

    public BatchApproveAdvanceBillCommand(PageF<SearchF<?>> query, List<Long> billIds, Integer approveState) {
        super(query, billIds, BillApproveStateEnum.valueOfByCode(approveState));
    }

}
