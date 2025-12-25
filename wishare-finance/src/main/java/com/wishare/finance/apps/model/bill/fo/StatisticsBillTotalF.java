package com.wishare.finance.apps.model.bill.fo;

import com.wishare.starter.beans.PageF;
import com.wishare.tools.starter.fo.search.SearchF;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;
import java.util.List;

/**
 * 统计账单合计数据
 * @Author dxclay
 * @Date 2022/8/26
 * @Version 1.0
 */
@Setter
@Getter
@ApiModel("统计账单合计数据")
public class StatisticsBillTotalF {

    @ApiModelProperty(value = "审核状态（0未审核，1审核中，2已审核，3未通过）全部：null/[]", required = true)
    @Size(max = 4, message = "审核状态列表参数不正确，大小区间为[1,4]")
    private List<Integer> approveState;

    @ApiModelProperty(value = "检索条件（二选一条件，账单id列表为空时使用， 两个条件都不为空时默认使用账单id列表）")
    private PageF<SearchF<?>> query;

    @ApiModelProperty("上级收费单元id")
    private String supCpUnitId;

}
