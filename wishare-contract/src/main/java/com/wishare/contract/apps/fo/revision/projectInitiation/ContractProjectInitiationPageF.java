package com.wishare.contract.apps.fo.revision.projectInitiation;

import com.wishare.starter.beans.PageF;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("立项分页查询参数")
public class ContractProjectInitiationPageF extends PageF {

    /**
     * 所属部门ID
     */
    public static final String DEPART_ID = "departId";

    @ApiModelProperty(value = "项目区域编码")
    private List<String> regionCode;

    @ApiModelProperty(value = "项目区域")
    private List<String> region;

    @ApiModelProperty(value = "部门ID")
    private List<String> departId;

    @ApiModelProperty(value = "部门名称")
    private String departName;

    @ApiModelProperty(value = "项目ID")
    private List<String> communityId;

    private String communityName;

    @ApiModelProperty("立项编号")
    private String projectCode;

    @ApiModelProperty("立项名称")
    private String projectName;

    @ApiModelProperty("立项类型")
    private List<Integer> initiationTypeCode;

    @ApiModelProperty("占用目标成本一级费项ID")
    private String chargeItemIdLevelOne;

    @ApiModelProperty("占用目标成本一级费项名称")
    private String chargeItemNameLevelOne;

    @ApiModelProperty("占用目标成本二级费项ID")
    private String chargeItemIdLevelTwo;

    @ApiModelProperty("占用目标成本二级费项名称")
    private String chargeItemNameLevelTwo;

    @ApiModelProperty("合约规划名称 p.contractPlanName")
    private String contractPlanName;

    @ApiModelProperty(value = "是否代甲方采购（0:否，1:是）")
    private Integer isProcurementAgent;

    @ApiModelProperty(value = "成本确认状态 0 未确认、1 已确认")
    private Integer costConfirmationStatus;

    @ApiModelProperty(value = "成本确认变更审核流程id")
    private String bpmProcInstId;

    @ApiModelProperty(value = "成本确认变更审核状态 0 待发起、1 审批中、2 已通过、3 已驳回")
    private Integer bpmReviewStatus;

    @ApiModelProperty(value = "BPM审批通过时间")
    private LocalDateTime bpmApprovalDate;

    @ApiModelProperty(value = "审批状态 0 待发起、1 审批中、2 已通过、4 已驳回")
    private String reviewStatus;

    @ApiModelProperty("经办人")
    private String creatorName;

    @ApiModelProperty(value = "租户id")
    private String tenantId;

}