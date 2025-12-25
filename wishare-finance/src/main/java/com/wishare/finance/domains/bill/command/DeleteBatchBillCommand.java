package com.wishare.finance.domains.bill.command;

import com.wishare.starter.beans.PageF;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 批量删除账单
 */
@Getter
@Setter
public class DeleteBatchBillCommand extends BatchCommand{

    public DeleteBatchBillCommand() {
    }

    public DeleteBatchBillCommand(PageF<SearchF<?>> query, List<Long> billIds) {
       setBillIds(billIds);
       setQuery(query);
    }

    public DeleteBatchBillCommand(PageF<SearchF<?>> query, List<Long> billIds,String supCpUnitId) {
        setBillIds(billIds);
        setQuery(query);
        setSupCpUnitId(supCpUnitId);
    }
}
