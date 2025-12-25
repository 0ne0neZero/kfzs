package com.wishare.finance.infrastructure.remote.vo.bpm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * @author hhb
 * @describe
 * @date 2025/5/27 16:36
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("发送信息")
public class SendMessageReqVO {

    @ApiModelProperty("任务应用名称Code")
    private String appName;
    @ApiModelProperty("任务应用名称，如任务中台，财务中台，流程中心等")
    private String appDesc;
    @ApiModelProperty("任务类型Code")
    private String type;
    @ApiModelProperty("任务类型，如安全生产、巡查、巡检等")
    private String typeDesc;
    @ApiModelProperty("任务标题")
    private String title;
    @ApiModelProperty("项目id")
    private String communityId;
    @ApiModelProperty("租户id")
    private String tenantId;
    @ApiModelProperty("任务状态名称")
    private String statusName;
    @ApiModelProperty("任务状态值")
    private String statusValue;
    @ApiModelProperty("待处理人数组")
    private String[] handleUserIdArr;
    @ApiModelProperty("用户ID")
    private String userId;
    @ApiModelProperty("用户名称")
    private String userName;
}
