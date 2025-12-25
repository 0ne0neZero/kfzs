package com.wishare.contract.domains.vo.revision.projectInitiation;

import cn.hutool.core.util.NumberUtil;
import com.wishare.contract.apps.fo.revision.income.ContractFjxxF;
import com.wishare.contract.domains.enums.revision.BPMStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Accessors(chain = true)
@ApiModel(value = "立项详情")
public class ContractProjectInitiationV {

    @ApiModelProperty(value = "立项ID")
    private String id;

    @ApiModelProperty(value = "立项编号")
    private String projectCode;

    @ApiModelProperty(value = "标题")
    private String projectName;

    @ApiModelProperty(value = "部门ID")
    private String departId;

    @ApiModelProperty(value = "部门名称")
    private String departName;

    @ApiModelProperty(value = "项目ID")
    private String communityId;

    @ApiModelProperty(value = "项目名称")
    private String communityName;

    @ApiModelProperty(value = "项目类型ID")
    private String communityTypeId;

    @ApiModelProperty(value = "项目类型编码")
    private Integer communityTypeCode;

    @ApiModelProperty(value = "项目类型")
    private String communityTypeName;

    @ApiModelProperty(value = "项目区域编码")
    private Integer regionCode;

    @ApiModelProperty(value = "项目区域")
    private String region;

    @ApiModelProperty(value = "立项类型编码")
    private Integer initiationTypeCode;

    @ApiModelProperty(value = "立项类型")
    private String initiationType;

    @ApiModelProperty(value = "具体类别编码")
    private Integer specificCategoryCode;

    @ApiModelProperty(value = "具体类别")
    private String specificCategory;

    @ApiModelProperty(value = "是否代甲方采购（0:否，1:是）")
    private Integer isProcurementAgent;

    @ApiModelProperty(value = "计划开始时间")
    private LocalDate planStartTime;

    @ApiModelProperty(value = "计划结束时间")
    private LocalDate planEndTime;

    @ApiModelProperty(value = "立项事由")
    private String initiationReason;

    @ApiModelProperty(value = "是否签订合同（0:否，1:是）")
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

    @ApiModelProperty(value = "当月已使用百分比")
    private String monthlyUsedPercentageStr;

    @ApiModelProperty(value = "当年已使用金额")
    private BigDecimal yearlyUsedAmount;

    @ApiModelProperty(value = "当年已使用百分比")
    private BigDecimal yearlyUsedPercentage;

    @ApiModelProperty(value = "当年已使用百分比")
    private String yearlyUsedPercentageStr;

    @ApiModelProperty(value = "组价依据附件")
    private String pricingBasisAttachment;

    @ApiModelProperty("(组价依据附件信息)")
    private List<ContractFjxxF> pricingBasisAttachmentList;

    @ApiModelProperty(value = "实施方案附件")
    private String implementationPlanAttachment;

    @ApiModelProperty("(实施方案附件信息)")
    private List<ContractFjxxF> implementationPlanAttachmentList;

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
    private Integer reviewStatus;

    @ApiModelProperty(value = "审批通过时间")
    private LocalDateTime approveCompletedTime;

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

    @ApiModelProperty(value = "合约规划列表")
    private List<ContractProjectPlanV> contractPlanList;

    @ApiModelProperty(value = "合同列表")
    private List<ContractProjectPlanV> contractList;

    @ApiModelProperty(value = "订单列表")
    private List<ContractProjectOrderV> contractProjectOrderList;

    @ApiModelProperty(value = "成本确认明细")
    private List<ContractProjectPlanCostConfirmV> costConfirmList;

    @ApiModelProperty(value = "订单总金额(不含税)")
    private BigDecimal orderTotalAmount;

    @ApiModelProperty(value = "立项剩余金额(不含税) = 立项不含税金额 - 订单总金额(不含税)")
    private BigDecimal remainingAmountWithoutTax;

    @ApiModelProperty(value = "分摊金额(不含税)")
    private BigDecimal allocationAmount;

    @ApiModelProperty("确认金额(不含税)")
    private BigDecimal confirmAmount;

    @ApiModelProperty(value = "合约规划已发生-本月已发生金额(不含税)")
    private BigDecimal contractMonthlyOccurredAmount;

    @ApiModelProperty(value = "合约规划已发生-本月已发生金额占比")
    private BigDecimal contractMonthlyOccurredPercentage;

    @ApiModelProperty(value = "合约规划已发生-本月已发生金额占比")
    private String contractMonthlyOccurredPercentageStr;

    @ApiModelProperty(value = "合约规划已发生-本年已发生金额(不含税)")
    private BigDecimal contractYearlyOccurredAmount;

    @ApiModelProperty(value = "合约规划已发生-本年已发生金额占比")
    private BigDecimal contractYearlyOccurredPercentage;

    @ApiModelProperty(value = "合约规划已发生-本年已发生金额占比")
    private String contractYearlyOccurredPercentageStr;

    @ApiModelProperty(value = "成本确认状态 0 未确认、1 已确认")
    private String costConfirmationStatusStr;

    public String getContractMonthlyOccurredPercentageStr() {
        return this.digitalPercentageConversion(contractMonthlyOccurredPercentage);
    }

    public String getContractYearlyOccurredPercentageStr() {
        return this.digitalPercentageConversion(contractYearlyOccurredPercentage);
    }

    public String getMonthlyUsedPercentageStr() {
        return this.digitalPercentageConversion(monthlyUsedPercentage);
    }

    public String getYearlyUsedPercentageStr() {
        return this.digitalPercentageConversion(yearlyUsedPercentage);
    }

    public String getCostConfirmationStatusStr() {
        return costConfirmationStatus.equals(0) ? "未确认" : "已确认";
    }

    /**
     * 数字百分比转换
     *
     * @param number
     * @return
     */
    public String digitalPercentageConversion(BigDecimal number) {
        return number == null ? null : NumberUtil.decimalFormat("0.00", number) + "%";
    }
}