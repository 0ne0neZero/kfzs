package com.wishare.contract.domains.vo.revision.projectInitiation;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Accessors(chain = true)
@ApiModel(value = "立项关联合约规划")
public class ContractProjectPlanV {

    @ApiModelProperty(value = "ID")
    private String id;

    @ApiModelProperty(value = "立项ID")
    private String projectInitiationId;

    @ApiModelProperty(value = "类型 0 合约规划 1 合同")
    private Integer type;

    @ApiModelProperty(value = "合约规划ID|合同id")
    private String contractPlanId;

    @ApiModelProperty(value = "合约规划名称|合同名称")
    private String contractPlanName;

    @ApiModelProperty(value = "合约规划金额|合同金额（不含税）")
    private BigDecimal contractPlanAmount;

    @ApiModelProperty(value = "合同周期")
    private String contractTimeCycle;

    @ApiModelProperty(value = "分摊金额(不含税)")
    private BigDecimal allocationAmount;

    @ApiModelProperty("确认金额(不含税)")
    private BigDecimal confirmAmount;

    @ApiModelProperty(value = "合约规划已发生-本月已发生金额(不含税)")
    private BigDecimal contractMonthlyOccurredAmount;

    @ApiModelProperty(value = "合约规划已发生-本月已发生金额占比")
    private BigDecimal contractMonthlyOccurredPercentage;

    @ApiModelProperty(value = "合约规划已发生-本年已发生金额(不含税)")
    private BigDecimal contractYearlyOccurredAmount;

    @ApiModelProperty(value = "合约规划已发生-本年已发生金额占比")
    private BigDecimal contractYearlyOccurredPercentage;

    @ApiModelProperty("合约规划费项Code")
    private String costChargeItemCode;

    @ApiModelProperty("占用目标成本二级费项-费项ID")
    private String chargeItemIdLevelTwo;

    @ApiModelProperty("占用目标成本二级费项-费项名称")
    private String chargeItemNameLevelTwo;

    @ApiModelProperty(value = "占用目标成本二级费项-本月已发生金额(不含税)")
    private BigDecimal costItemMonthlyAmount;

    @ApiModelProperty(value = "占用目标成本二级费项-本月已发生金额占比")
    private BigDecimal costItemMonthlyPercentage;

    @ApiModelProperty(value = "占用目标成本二级费项-本年已发送金额(不含税)")
    private BigDecimal costItemYearlyAmount;

    @ApiModelProperty(value = "占用目标成本二级费项-本年已发送金额占比")
    private BigDecimal costItemYearlyPercentage;

    @ApiModelProperty(value = "月度分摊明细")
    private List<ContractProjectPlanMonthlyAllocationV> monthlyAllocationList;

    @ApiModelProperty(value = "成本确认月度分摊明细")
    private List<ContractProjectPlanMonthlyAllocationV> costConfirmMonthlyAllocationList;

    @ApiModelProperty(value = "成本确认明细")
    private List<ContractProjectPlanCostConfirmV> costConfirmList;

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