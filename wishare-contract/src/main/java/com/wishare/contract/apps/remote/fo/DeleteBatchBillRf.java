package com.wishare.contract.apps.remote.fo;

import com.wishare.starter.beans.PageF;
import com.wishare.tools.starter.fo.search.SearchF;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@ApiModel("批量删除账单参数")
public class DeleteBatchBillRf {

    @ApiModelProperty(value = "检索条件（二选一条件，账单id列表为空时使用， 两个条件都不为空时默认使用账单id列表）")
    private PageF<SearchF<?>> query;

    @ApiModelProperty(value = "账单id列表（二选一条件，账单id列表不为空时使用）")
    private List<Long> billIds;

    @ApiModelProperty("supCpUnitId")
    private String supCpUnitId;

}
