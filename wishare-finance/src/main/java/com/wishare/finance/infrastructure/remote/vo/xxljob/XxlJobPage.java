package com.wishare.finance.infrastructure.remote.vo.xxljob;

import java.util.List;

/**
 * xxl-job响应数据
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/4/5
 */
public class XxlJobPage<T> {


    /**
     * 总记录数
     */
    private int recordsTotal;

    /**
     * 过滤后的总记录数
     */
    private int recordsFiltered;

    /**
     * 分页列表
     */
    private List<T> data;


    public int getRecordsTotal() {
        return recordsTotal;
    }

    public XxlJobPage<T> setRecordsTotal(int recordsTotal) {
        this.recordsTotal = recordsTotal;
        return this;
    }

    public int getRecordsFiltered() {
        return recordsFiltered;
    }

    public XxlJobPage<T> setRecordsFiltered(int recordsFiltered) {
        this.recordsFiltered = recordsFiltered;
        return this;
    }

    public List<T> getData() {
        return data;
    }

    public XxlJobPage<T> setData(List<T> data) {
        this.data = data;
        return this;
    }
}
