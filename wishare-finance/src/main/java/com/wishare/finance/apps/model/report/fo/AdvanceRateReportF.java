package com.wishare.finance.apps.model.report.fo;

import com.wishare.starter.beans.PageF;
import com.wishare.tools.starter.fo.search.SearchF;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 分页查询预收率统计表报表入参
 *
 * @author yancao
 */
@Getter
@Setter
@ApiModel("分页查询预收率统计表报表入参")
public class AdvanceRateReportF {

    @ApiModelProperty(value = "开始时间")
    private String startDate;

    @ApiModelProperty(value = "结束时间")
    private String endDate;

    @ApiModelProperty("分页参数")
    PageF<SearchF<?>> query;
}
