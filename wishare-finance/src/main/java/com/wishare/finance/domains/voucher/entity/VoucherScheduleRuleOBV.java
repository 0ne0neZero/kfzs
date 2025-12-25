package com.wishare.finance.domains.voucher.entity;

import com.wishare.finance.infrastructure.support.schedule.ScheduleRule;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 定时规则
 *
 * @Author dxclay
 * @Date 2022/10/13
 * @Version 1.0
 */
@Getter
@Setter
@ApiModel(value = "凭证定时规则")
public class VoucherScheduleRuleOBV {

    @ApiModelProperty(value = "调度器id")
    private String schedulerId;

    @ApiModelProperty(value = "调度器名称")
    private String schedulerName;

    @ApiModelProperty(value = "调度任务id")
    private String scheduleTaskId;

    @ApiModelProperty(value = "调度任务名称")
    private String scheduleTaskName;

    /**
     * 是否自动推送。0-false,1-是。注意历史数据可能此字段为空
     * */
    @ApiModelProperty(value = "调度任务名称")
    private String autoPush;

    @ApiModelProperty(value = "定时规则信息")
    private ScheduleRule rule;


}
