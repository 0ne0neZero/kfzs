package com.wishare.finance.apps.model.bill.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author xujian
 * @date 2022/12/30
 * @Description:
 */
@Getter
@Setter
@ApiModel("收款付款记录统计入参")
public class GatherAndPayStatisticsF {

    @ApiModelProperty("上级收费单元ids")
    private List<Long> supCpUnitIds;

    @ApiModelProperty("统计开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty("统计结束时间")
    private LocalDateTime endTime;
}
