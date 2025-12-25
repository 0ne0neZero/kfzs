package com.wishare.finance.domains.bill.command;

import com.wishare.finance.domains.bill.consts.enums.BillAccountHandedStateEnum;
import com.wishare.starter.beans.PageF;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class BatchHandBillCommand extends BatchCommand{

    /**
     * 账单id集合
     */
    private List<Long> billIds;

    /**
     * 申请驳回理由
     */
    private String invoiceReceipts;

    public BatchHandBillCommand() {
    }

    public BatchHandBillCommand(PageF<SearchF<?>> query, List<Long> billIds, BillAccountHandedStateEnum accountHanded) {
        setQuery(query);
        setBillIds(billIds);
    }

}
