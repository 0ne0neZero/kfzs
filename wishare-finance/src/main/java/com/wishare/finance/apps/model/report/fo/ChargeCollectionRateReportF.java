package com.wishare.finance.apps.model.report.fo;

import com.wishare.starter.beans.PageF;
import com.wishare.tools.starter.fo.search.SearchF;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * 收费减免入参
 *
 * @author yancao
 */
@Getter
@Setter
@ApiModel("收缴率入参")
public class ChargeCollectionRateReportF {

    @ApiModelProperty(value = "开始时间")
    private LocalDate startDate;

    @ApiModelProperty(value = "结束时间")
    private LocalDate endDate;

    @ApiModelProperty(value = "查询时间")
    private LocalDate queryDate;

    @ApiModelProperty("分页参数")
    PageF<SearchF<?>> query;
}
