package com.wishare.finance.apps.model.bill.fo;

import com.wishare.starter.beans.PageF;
import com.wishare.tools.starter.fo.search.SearchF;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@ApiModel("批量操作账单参数")
public class BatchBillF {

    @ApiModelProperty(value = "检索条件（二选一条件，账单id列表为空时使用， 两个条件都不为空时默认使用账单id列表）")
    private PageF<SearchF<?>> query;

    @ApiModelProperty(value = "账单id列表（二选一条件，账单id列表不为空时使用）")
    private List<Long> billIds;

    @ApiModelProperty(value = "上级收费单元id")
    @NotBlank(message = "上级收费单元id不能为空")
    private String supCpUnitId;

}
