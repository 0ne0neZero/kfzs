package com.wishare.finance.infrastructure.remote.fo.msg;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author fxl
 * @describe
 * @date 2024/1/11
 */
@Data
@ApiModel("任务消息-自定义内容对象")
public class TaskMsgDO {

    @ApiModelProperty("任务id")
    private String taskId;

    @ApiModelProperty("任务流水号")
    private String serialNumber;

    @ApiModelProperty("任务应用名称Code")
    private String appName;

    @ApiModelProperty("任务应用名称，如任务中台，财务中台，流程中心等")
    private String appDesc;

    @ApiModelProperty("任务类型Code")
    private String type;

    @ApiModelProperty("任务类型，如安全生产、巡查、巡检等")
    private String typeDesc;

    @ApiModelProperty("子任务类型Code")
    private String subType;

    @ApiModelProperty("子任务类型名称，如请假审批")
    private String subTypeDesc;

    @ApiModelProperty("任务状态名称")
    private String statusName;

    @ApiModelProperty("任务状态值")
    private String statusValue;

    @ApiModelProperty("任务短id(暂时不用)")
    private String code;

    private String title;

    public void setTitle(String title) {
        this.title = title;
        this.serialNumber = title;
    }
}
