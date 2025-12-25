package com.wishare.contract.apps.fo.revision.projectInitiation;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.wishare.contract.apps.fo.revision.income.ContractFjxxF;
import com.wishare.contract.domains.vo.revision.projectInitiation.ContractProjectPlanCostConfirmV;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Accessors(chain = true)
@ApiModel(value = "立项更新参数")
public class ContractProjectInitiationUpdateF {

    @ApiModelProperty(value = "立项ID")
    @NotBlank(message = "立项ID不能为空")
    private String id;

    @ApiModelProperty(value = "标题")
    @NotBlank(message = "标题不能为空")
    private String projectName;

    @ApiModelProperty(value = "部门ID")
    @NotBlank(message = "部门ID不能为空")
    private String departId;

    @ApiModelProperty(value = "部门名称")
    @NotBlank(message = "部门名称不能为空")
    private String departName;

    @ApiModelProperty("项目ID")
    @NotBlank(message = "项目ID不能为空")
    private String communityId;

    @ApiModelProperty("项目名称")
    @NotBlank(message = "项目名称不能为空")
    private String communityName;

    @ApiModelProperty(value = "项目类型ID")
    @NotBlank(message = "项目类型ID不能为空")
    private String communityTypeId;

    @ApiModelProperty(value = "项目类型编码")
    private Integer communityTypeCode;

    @ApiModelProperty(value = "项目类型名称")
    private String communityTypeName;

    @ApiModelProperty("区域编码")
    @NotNull(message = "区域编码不能为空")
    private Integer regionCode;

    @ApiModelProperty(value = "区域")
    @NotBlank(message = "区域不能为空")
    private String region;

    @ApiModelProperty(value = "立项类型编码")
    @NotNull(message = "立项类型编码不能为空")
    private Integer initiationTypeCode;

    @ApiModelProperty(value = "立项类型")
    @NotBlank(message = "立项类型不能为空")
    private String initiationType;

    @ApiModelProperty(value = "具体类别编码")
    private Integer specificCategoryCode;

    @ApiModelProperty(value = "具体类别")
    private String specificCategory;

    @ApiModelProperty(value = "是否采购代理")
    private Integer isProcurementAgent;

    @ApiModelProperty(value = "计划开始时间")
    @NotNull(message = "计划开始时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(format = "yyyy-MM-dd")
    private LocalDate planStartTime;

    @ApiModelProperty(value = "计划结束时间")
    @NotNull(message = "计划结束时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(format = "yyyy-MM-dd")
    private LocalDate planEndTime;

    @ApiModelProperty(value = "立项事由")
    private String initiationReason;

    @ApiModelProperty(value = "是否已签订合同")
    private Integer isContractSigned;

    @ApiModelProperty(value = "不含税金额")
    private BigDecimal amountWithoutTax;

    @ApiModelProperty(value = "含税金额")
    private BigDecimal amountWithTax;

    @ApiModelProperty("合约规划名称")
    private String contractPlanName;

    @ApiModelProperty("占用目标成本一级费项ID")
    private String chargeItemIdLevelOne;

    @ApiModelProperty("占用目标成本一级费项名称")
    private String chargeItemNameLevelOne;

    @ApiModelProperty("占用目标成本二级费项ID")
    private String chargeItemIdLevelTwo;

    @ApiModelProperty("占用目标成本二级费项名称")
    private String chargeItemNameLevelTwo;

    @ApiModelProperty(value = "当月已使用金额")
    private BigDecimal monthlyUsedAmount;

    @ApiModelProperty(value = "当月已使用百分比")
    private BigDecimal monthlyUsedPercentage;

    @ApiModelProperty(value = "当年已使用金额")
    private BigDecimal yearlyUsedAmount;

    @ApiModelProperty(value = "当年已使用百分比")
    private BigDecimal yearlyUsedPercentage;

    @ApiModelProperty(value = "成本确认金额")
    private BigDecimal costConfirmationAmount;

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

    @ApiModelProperty(name = "成本地区公司ID")
    private String buGuid;

    @ApiModelProperty(name = "成本项目GUID")
    private String projectGuid;

    @ApiModelProperty(name = "成本业务线GUID")
    private String businessGuid;

    @ApiModelProperty(name = "成本业务单元编码")
    private String businessUnitCode;

    @ApiModelProperty(name = "true 不显示合约规划 false 显示合约规划")
    private Boolean showMonthYearlyUsedFields;

    @ApiModelProperty(name = "是否不是总部立项")
    private Boolean isHeadquarters;

    @ApiModelProperty(name = "是否不是总部立项并且不是物资采购、员工福利")
    private Boolean isNotHeadquartersAndOther;

    @ApiModelProperty(name = "是否不是总部立项并且不是占用目标成本费项是基础物管或非业务增值服务成本场服务时")
    private Boolean isShowMonthYearlyUsedFieldsNew;

    @ApiModelProperty(name = "是否不是总部立项并且是占用目标成本费项是基础物管或非业务增值服务成本案场服务时并且不是虚拟项目")
    private Boolean isShowContractPlan;

    @ApiModelProperty(value = "租户id")
    private String tenantId;

    @ApiModelProperty(value = "是否发起审批")
    private Boolean startApprovalFlag = false;

    @ApiModelProperty("(组价依据附件信息)")
    @NotEmpty(message = "组价依据附件信息不能为空")
    private List<ContractFjxxF> pricingBasisAttachmentList;

    @ApiModelProperty("(实施方案附件信息)")
    private List<ContractFjxxF> implementationPlanAttachmentList;

    @ApiModelProperty(value = "合约规划列表")
    private List<ContractProjectPlanF> contractPlanList;

    @ApiModelProperty(value = "合同列表")
    private List<ContractProjectPlanF> contractList;

    @ApiModelProperty(value = "成本确认明细")
    private List<ContractProjectPlanCostConfirmV> costConfirmList;
}