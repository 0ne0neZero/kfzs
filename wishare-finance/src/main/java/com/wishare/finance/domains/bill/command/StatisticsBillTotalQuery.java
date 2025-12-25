package com.wishare.finance.domains.bill.command;

import com.wishare.starter.beans.PageF;
import com.wishare.tools.starter.fo.search.SearchF;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 账单合计查询
 */
@Getter
@Setter
public class StatisticsBillTotalQuery {

    /**
     * 检索条件（二选一条件，账单id列表为空时使用， 两个条件都不为空时默认使用账单id列表
     */
    private PageF<SearchF<?>> query;

    /**
     * 账单id列表（二选一条件，账单id列表不为空时使用）
     */
    private List<Long> billIds;


    /**
     * 上级账单id
     */
    private String supCpUnitId;

    /**
     * 是否统计无效账单
     */
    private Integer billInvalid;

    /**
     * 是否统计退款账单
     */
    private Integer billRefund;


    public StatisticsBillTotalQuery() {
    }

    public StatisticsBillTotalQuery(PageF<SearchF<?>> query, List<Long> billIds, Integer billInvalid,Integer billRefund) {
        this.query = query;
        this.billIds = billIds;
        this.billInvalid = billInvalid;
        this.billRefund = billRefund;
    }

    public StatisticsBillTotalQuery(PageF<SearchF<?>> query, List<Long> billIds, Integer billInvalid,Integer billRefund,String supCpUnitId) {
        this.query = query;
        this.billIds = billIds;
        this.billInvalid = billInvalid;
        this.billRefund = billRefund;
        this.supCpUnitId = supCpUnitId;
    }

}
