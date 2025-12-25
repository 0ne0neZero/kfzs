package com.wishare.finance.domains.bill.command;

import com.wishare.starter.beans.PageF;
import com.wishare.tools.starter.fo.search.SearchF;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 统计账单合计数据
 * @author yancao
 */
@Setter
@Getter
@ApiModel("统计账单合计数据")
@AllArgsConstructor
public class StatisticsBillTotalCommand {

    /**
     * 账单类型（1:应收账单，2:预收账单，3:临时收费账单，4应付账单）
     */
    private Integer billType;

    /**
     * 检索条件（二选一条件，账单id列表为空时使用， 两个条件都不为空时默认使用账单id列表）
     */
    private PageF<SearchF<?>> query;

    /**
     * 账单id列表（二选一条件，账单id列表不为空时使用）
     */
    private List<Long> billIds;

    /**
     * 账单审核状态（0初始审核，1变更审核）
     */
    private Integer billState;

    /**
     * 是否统计无效账单(0否，1是)
     */
    private Integer billInvalid;
}
