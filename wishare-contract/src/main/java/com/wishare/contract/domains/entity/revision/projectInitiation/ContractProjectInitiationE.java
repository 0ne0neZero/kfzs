package com.wishare.contract.domains.entity.revision.projectInitiation;

import com.baomidou.mybatisplus.annotation.*;
import com.wishare.contract.domains.entity.revision.BaseE;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@TableName("contract_project_initiation")
@EqualsAndHashCode(callSuper = true)
public class ContractProjectInitiationE extends BaseE {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 立项编号
     */
    private String projectCode;

    /**
     * 标题
     */
    private String projectName;

    /**
     * 组织ID
     */
    private String departId;

    /**
     * 组织ID
     */
    private String departName;

    /**
     * 项目ID
     */
    private String communityId;

    /**
     * 项目名称
     */
    private String communityName;

    /**
     * 项目类型ID
     */
    private String communityTypeId;

    /**
     * 项目类型编码
     */
    private Integer communityTypeCode;

    /**
     * 项目类型名称
     */
    private String communityTypeName;

    /**
     * 区域编码
     */
    private Integer regionCode;

    /**
     * 区域
     */
    private String region;

    /**
     * 立项类型Code
     */
    private Integer initiationTypeCode;

    /**
     * 立项类型
     */
    private String initiationType;

    /**
     * 具体分类编码
     */
    private Integer specificCategoryCode;

    /**
     * 具体分类
     */
    private String specificCategory;

    /**
     * 是否代甲方采购（0:否，1:是）
     */
    private Integer isProcurementAgent;

    /**
     * 计划实施时间
     */
    private LocalDate planStartTime;

    /**
     * 计划结束时间
     */
    private LocalDate planEndTime;

    /**
     * 立项事由
     */
    private String initiationReason;

    /**
     * 是否签订合同（0:否，1:是）
     */
    private Integer isContractSigned;

    /**
     * 立项不含税金额
     */
    private BigDecimal amountWithoutTax;

    /**
     * 立项含税金额
     */
    private BigDecimal amountWithTax;

    /**
     * 合约规划名称
     */
    private String contractPlanName;

    /**
     * 占用目标成本一级费项
     */
    private String chargeItemIdLevelOne;

    /**
     * 占用目标成本一级费项
     */
    private String chargeItemNameLevelOne;

    /**
     * 占用目标成本二级费项
     */
    private String chargeItemIdLevelTwo;

    /**
     * 占用目标成本二级费项
     */
    private String chargeItemNameLevelTwo;

    /**
     * 本月累计使用金额(不含税)
     */
    private BigDecimal monthlyUsedAmount;

    /**
     * 本月累计使用占比
     */
    private BigDecimal monthlyUsedPercentage;

    /**
     * 本年累计使用金额(不含税)
     */
    private BigDecimal yearlyUsedAmount;

    /**
     * 本年累计使用占比
     */
    private BigDecimal yearlyUsedPercentage;

    /**
     * 成本确认金额
     */
    private BigDecimal costConfirmationAmount;

    /**
     * 成本确认状态 0 未确认、1 已确认
     */
    private Integer costConfirmationStatus;

    /**
     * 成本确认变更审核流程id
     */
    private String bpmProcInstId;

    /**
     * 成本确认变更审核状态 0 待发起、1 审批中、2 已通过、3 已驳回
     */
    private Integer bpmReviewStatus;

    /**
     * BPM审批通过时间
     */
    private LocalDateTime bpmApprovalDate;

    /**
     * 审批状态 0 待发起、1 审批中、2 已通过、4 已驳回
     */
    private Integer reviewStatus;

    /**
     * 审批通过时间
     */
    private LocalDateTime approveCompletedTime;

    /**
     * 组价依据附件信息
     */
    private String pricingBasisAttachment;

    /**
     * 实施方案附件信息
     */
    private String implementationPlanAttachment;

    /**
     * 地区公司GUID
     */
    private String buGuid;

    /**
     * 项目GUID
     */
    private String projectGuid;

    /**
     * 成本业务线GUID
     */
    private String businessGuid;

    /**
     * 成本业务单元编码
     */
    private String businessUnitCode;

    /**
     * 订单总金额(不含税)
     */
    private BigDecimal orderTotalAmount;

    /**
     * 立项剩余金额(不含税) = 立项不含税金额 - 订单总金额(不含税)
     */
    private BigDecimal remainingAmountWithoutTax;

    /**
     * true 不显示合约规划 false 显示合约规划
     */
    private Boolean showMonthYearlyUsedFields;

    /**
     * 是否不是总部立项
     */
    private Boolean isHeadquarters;

    /**
     * 是否不是总部立项并且不是物资采购、员工福利
     */
    private Boolean isNotHeadquartersAndOther;

    /**
     * 是否不是总部立项并且不是占用目标成本费项是基础物管或非业务增值服务成本场服务时
     */
    private Boolean isShowMonthYearlyUsedFieldsNew;

    /**
     * 是否不是总部立项并且是占用目标成本费项是基础物管或非业务增值服务成本案场服务时并且不是虚拟项目
     */
    private Boolean isShowContractPlan;

    /**
     * 租户id
     */
    private String tenantId;

}