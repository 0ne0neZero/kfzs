package com.wishare.finance.apps.model.report.fo;

import com.wishare.starter.beans.PageF;
import com.wishare.tools.starter.fo.search.SearchF;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 收费日报入参
 *
 * @author yancao
 */
@Getter
@Setter
@ApiModel("收费日报入参")
public class ChargeDailyReportF {

    @ApiModelProperty(value = "收费日期(格式：yyyy-MM-dd)")
    private String chargeDate;

    @ApiModelProperty("分页参数")
    PageF<SearchF<?>> query;
}
