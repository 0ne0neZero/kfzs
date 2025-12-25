package com.wishare.finance.apps.model.report.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 收费日报分页返回参数
 * @author yancao
 */
@Setter
@Getter
@ApiModel("收费日报分页返回参数")
public class ChargeDailyReportPageV {

    @ApiModelProperty("项目名称")
    private String communityName;

    @ApiModelProperty("房号名称")
    private String roomName;

}
