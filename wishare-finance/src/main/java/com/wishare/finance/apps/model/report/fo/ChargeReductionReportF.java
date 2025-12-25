package com.wishare.finance.apps.model.report.fo;

import com.wishare.starter.beans.PageF;
import com.wishare.tools.starter.fo.search.SearchF;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 收费减免入参
 *
 * @author yancao
 */
@Getter
@Setter
@ApiModel("收费减免入参")
public class ChargeReductionReportF {

    @ApiModelProperty(value = "减免开始时间")
    private String reductionStartDate;

    @ApiModelProperty(value = "减免结束时间")
    private String reductionEndDate;

    @ApiModelProperty("分页参数")
    PageF<SearchF<?>> query;
}
