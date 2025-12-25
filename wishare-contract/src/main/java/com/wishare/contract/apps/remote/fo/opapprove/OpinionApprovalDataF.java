package com.wishare.contract.apps.remote.fo.opapprove;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description:
 * @author: zhangfuyu
 * @Date: 2023/6/12/15:33
 */
@Data
@ApiModel(value = "审批意见获取请求数据")
public class OpinionApprovalDataF {

    @ApiModelProperty(value = "流程请求id")
    private String requestId;


    @ApiModelProperty(value = "表单唯一id")
    private String formdataid;

    @ApiModelProperty(value = "查询类型")
    private String type;
}
