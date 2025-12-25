package com.wishare.finance.infrastructure.remote.vo.bpm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author 周楠
 * @version 1.0.0
 * @ClassName ApprovalBtnVo.java
 * @Description
 * @createTime 2023年08月30日 11:31:00
 */
@Data
@ApiModel("展示审批按钮")
public class ApprovalBtnV {

    @ApiModelProperty("操作按钮。工作台使用")
    private List<String> actionVo;
    @ApiModelProperty("是否展示转派按钮")
    private Boolean showBtnTransfer = false;
    @ApiModelProperty("是否展示回退按钮")
    private Boolean showBtnBack= false;
    @ApiModelProperty("是否展示添加日志按钮")
    private Boolean showBtnAddlog = false;
    @ApiModelProperty("是否展示接收按钮")
    private Boolean showBtnAccept = false;
    @ApiModelProperty("是否展示拒绝接收按钮")
    private Boolean showBtnNotAccept= false;
    @ApiModelProperty("提交")
    private Boolean showBtnCommit = false;
    @ApiModelProperty("重新提交")
    private Boolean showBtnCommitAgain= false;
    @ApiModelProperty("撤销按钮")
    private Boolean showBtnRevoke = false;
    @ApiModelProperty("流程实例id")
    private String procInstId = "";
}
