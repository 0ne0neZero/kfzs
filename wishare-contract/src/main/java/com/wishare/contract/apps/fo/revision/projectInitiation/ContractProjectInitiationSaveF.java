package com.wishare.contract.apps.fo.revision.projectInitiation;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.wishare.contract.apps.fo.revision.income.ContractFjxxF;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@ApiModel("立项保存参数")
public class ContractProjectInitiationSaveF {

    @ApiModelProperty("标题")
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

    @ApiModelProperty("区域")
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

    @ApiModelProperty("是否代甲方采购（0:否，1:是）")
    private Integer isProcurementAgent;

    @ApiModelProperty("计划实施时间")
    @NotNull(message = "计划实施时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(format = "yyyy-MM-dd")
    private LocalDate planStartTime;

    @ApiModelProperty("计划结束时间")
    @NotNull(message = "计划结束时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(format = "yyyy-MM-dd")
    private LocalDate planEndTime;

    @ApiModelProperty("立项事由")
    @NotBlank(message = "立项事由不能为空")
    private String initiationReason;

    @ApiModelProperty("是否签订合同（0:否，1:是）")
    private Integer isContractSigned;

    @ApiModelProperty("立项不含税金额")
    @NotNull(message = "立项不含税金额不能为空")
    private BigDecimal amountWithoutTax;

    @ApiModelProperty("立项含税金额")
    @NotNull(message = "立项含税金额不能为空")
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

    @ApiModelProperty("本月累计使用金额(不含税)")
    private BigDecimal monthlyUsedAmount;

    @ApiModelProperty("本月累计使用占比")
    private BigDecimal monthlyUsedPercentage;

    @ApiModelProperty("本年累计使用金额(不含税)")
    private BigDecimal yearlyUsedAmount;

    @ApiModelProperty("本年累计使用占比")
    private BigDecimal yearlyUsedPercentage;

    @ApiModelProperty("(组价依据附件信息)")
    @NotEmpty(message = "组价依据附件信息不能为空")
    private List<ContractFjxxF> pricingBasisAttachmentList;

    @ApiModelProperty("(实施方案附件信息)")
    private List<ContractFjxxF> implementationPlanAttachmentList;

    @ApiModelProperty(value = "合约规划列表")
    private List<ContractProjectPlanF> contractPlanList;

    @ApiModelProperty(value = "合同列表")
    private List<ContractProjectPlanF> contractList;

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
}