package com.wishare.finance.infrastructure.remote.fo.bpm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 发起审批流程入参
 */
@Getter
@Setter
@ApiModel("应收账单结转入参")
public class ProcessStartF {

    @ApiModelProperty("任务应用名称，如任务中台，财务中台，流程中心等")
    private String appDesc;

    @ApiModelProperty("业务Key")
    private String businessKey;

    @ApiModelProperty("业务类型")
    private String businessType;

    @ApiModelProperty("任务分类名称")
    private String categoryName;

    @ApiModelProperty("deptId")
    private String deptId;

    @ApiModelProperty("报账单备注")
    private String receiptRemark;

    @ApiModelProperty("")
    private Object formData;

    @ApiModelProperty("流程实例来源")
    private String instSource;

    @ApiModelProperty("主流程实例ID")
    private String mainProcInstId;

    @ApiModelProperty("流程模型ID")
    private String modelId;

    @ApiModelProperty("流程实例名称")
    private String procInstName;

    @ApiModelProperty("流程实例关系类型。1.派生，2.关联")
    private Integer procRelateType;

    @ApiModelProperty("工单编码")
    private String processCode;

    @ApiModelProperty("")
    private OrgUser processUsers;

    @ApiModelProperty("适用对象ID")
    private String suitableTargetId;

    @ApiModelProperty("适用对象ID")
    private String suitableTargetName;

    @ApiModelProperty("适用对象类型")
    private String suitableTargetType;

    @ApiModelProperty("用户ID")
    private String userId;

    @ApiModelProperty("认领金额")
    private String claimAmount;

}
