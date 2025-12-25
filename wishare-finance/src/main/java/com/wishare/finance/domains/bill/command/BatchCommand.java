package com.wishare.finance.domains.bill.command;

import com.wishare.starter.beans.PageF;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 批量命令
 *
 * @Author dxclay
 * @Date 2022/9/21
 * @Version 1.0
 */
@Getter
@Setter
public class BatchCommand {

    /**
     * 检索条件（二选一条件，账单id列表为空时使用， 两个条件都不为空时默认使用账单id列表
     */
    private PageF<SearchF<?>> query;

    /**
     * 账单id列表（二选一条件，账单id列表不为空时使用）
     */
    private List<Long> billIds;

    /**
     * 项目id
     */
    private String supCpUnitId;

}
