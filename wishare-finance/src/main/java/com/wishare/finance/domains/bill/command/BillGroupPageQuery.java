package com.wishare.finance.domains.bill.command;

import com.wishare.finance.domains.bill.entity.Bill;
import com.wishare.starter.beans.PageF;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 账单分组分页查询
 *
 * @Author dxclay
 * @Date 2022/9/21
 * @Version 1.0
 */
@Getter
@Setter
public abstract class BillGroupPageQuery<B extends Bill> {

    /**
     * 查询条件
     */
    private PageF<SearchF<?>> query;

    /**
     * 分组的参数信息
     */
    private List<String> groupParams;
    /**
     * 分组排序参数
     */
    private List<String> orderByParams;

    /**
     * 分组类型： 0：正常分组  1：审核分组
     */
    private Integer type;

    /**
     * 获取分组的key
     * @param bill
     * @return
     */
    public abstract String groupKey(B bill);

}
