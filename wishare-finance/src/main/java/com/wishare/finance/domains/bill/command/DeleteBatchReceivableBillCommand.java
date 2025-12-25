package com.wishare.finance.domains.bill.command;

import com.wishare.starter.beans.PageF;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 批量删除应收账单命令
 */
@Getter
@Setter
public class DeleteBatchReceivableBillCommand extends DeleteBatchBillCommand {

    public DeleteBatchReceivableBillCommand() {
    }

    public DeleteBatchReceivableBillCommand(PageF<SearchF<?>> query, List<Long> billIds) {
        super(query, billIds);
    }
}
