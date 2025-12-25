package com.wishare.contract.apps.fo.revision.projectInitiation;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@ApiModel(value = "立项关联合约规划成本确认")
public class ContractProjectPlanCostConfirmF {

    @ApiModelProperty(value = "ID")
    private String id;

    @ApiModelProperty("关联的立项ID")
    private String projectInitiationId;

    @ApiModelProperty("合约规划id")
    private String contractProjectPlanId;

    @ApiModelProperty("变更类型 0 确认金额, 1 关联合同")
    private Integer type;

    @ApiModelProperty("变更前内容")
    private String originalContent;

    @ApiModelProperty("变更后内容")
    private String revisedContent;

    @ApiModelProperty(name = "成本确认变更审核状态 0 待发起、1 审批中、2 已通过、3 已驳回")
    private Integer bpmReviewStatus;

    @ApiModelProperty(value = "租户ID")
    private String tenantId;

    @ApiModelProperty(value = "创建人")
    private String creator;

    @ApiModelProperty(value = "创建人")
    private String creatorName;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime gmtCreate;

    @ApiModelProperty(value = "更新人")
    private String operator;

    @ApiModelProperty(value = "更新人")
    private String operatorName;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime gmtModify;

}