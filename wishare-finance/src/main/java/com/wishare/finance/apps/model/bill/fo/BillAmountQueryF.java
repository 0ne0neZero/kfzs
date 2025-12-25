package com.wishare.finance.apps.model.bill.fo;

import com.wishare.tools.starter.fo.search.SearchF;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author xujian
 * @date 2022/12/21
 * @Description:
 */
@Getter
@Setter
@ApiModel("账单金额查询")
public class BillAmountQueryF {

    @ApiModelProperty("查询条件")
    private SearchF<?> query;

}
