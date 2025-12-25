package com.wishare.finance.domains.report.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 收费日报统计收费项目返回参数
 * @author yancao
 */
@Setter
@Getter
@ApiModel("收费日报统计收费项目返回参数")
public class ChargeDailyReportTotalDto {

    @ApiModelProperty("金额单位")
    private String amountUnit = "分";

    @ApiModelProperty("收费项目")
    private String name;

    @ApiModelProperty("收费金额")
    private String amount;
}
