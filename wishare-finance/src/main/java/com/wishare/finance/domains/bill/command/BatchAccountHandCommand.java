package com.wishare.finance.domains.bill.command;

import com.wishare.starter.beans.PageF;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 批量交账命令
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/1/13
 */
@Getter
@Setter
public class BatchAccountHandCommand {

    private PageF<SearchF<?>> query;

    private List<Long> billIds;

    private String supCpUnitId;

    public BatchAccountHandCommand() {
    }

    public BatchAccountHandCommand(PageF<SearchF<?>> query, List<Long> billIds) {
        this.query = query;
        this.billIds = billIds;
    }

    public BatchAccountHandCommand(PageF<SearchF<?>> query, List<Long> billIds,String supCpUnitId) {
        this.query = query;
        this.billIds = billIds;
        this.supCpUnitId = supCpUnitId;
    }
}
