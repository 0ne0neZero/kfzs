package com.wishare.finance.domains.bill.command;

import com.wishare.starter.beans.PageF;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 批量冻结账单
 */
@Getter
@Setter
public class FreezeBatchBillCommand extends BatchCommand{

    public FreezeBatchBillCommand() {
    }

    public FreezeBatchBillCommand(PageF<SearchF<?>> query, List<Long> billIds) {
       setBillIds(billIds);
       setQuery(query);
    }

    public FreezeBatchBillCommand(PageF<SearchF<?>> query, List<Long> billIds,String supCpUnitId) {
        setBillIds(billIds);
        setQuery(query);
        setSupCpUnitId(supCpUnitId);
    }
}
