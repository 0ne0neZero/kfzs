package com.wishare.finance.apps.model.bill.fo;

import com.wishare.starter.beans.PageF;
import com.wishare.tools.starter.fo.search.SearchF;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * @description:
 * @author: pgq
 * @since: 2022/10/12 18:17
 * @version: 1.0.0
 */
@Setter
@Getter
@ApiModel("批量交账表单")
public class AccountHandBatchF {

    @ApiModelProperty(value = "检索条件（二选一条件，账单id列表为空时使用， 两个条件都不为空时默认使用账单id列表）")
    private PageF<SearchF<?>> query;

    /**
     * 账单id集合
     */
    @ApiModelProperty(value = "账单id集合", required = true)
    private List<Long> billIds;

    /**
     * 账单类型
     */
    @ApiModelProperty(value = "账单类型 1-应收账单， 2-预收账单， 3-临时收费账单， 4-应付账单", required = true)
    private Integer billType;
}
