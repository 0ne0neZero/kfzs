package com.wishare.contract.apps.fo.revision.projectInitiation;

import com.wishare.contract.domains.vo.revision.projectInitiation.ContractProjectPlanMonthlyAllocationV;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

@Data
@Accessors(chain = true)
@ApiModel("合约规划基础参数")
public class ContractProjectPlanF {

    @ApiModelProperty("主键ID")
    private String id;

    @ApiModelProperty("关联的立项ID")
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

    @ApiModelProperty("合约规划已发生-本月已发生金额(不含税)")
    private BigDecimal contractMonthlyOccurredAmount;

    @ApiModelProperty("合约规划已发生-本月已发生金额占比")
    private BigDecimal contractMonthlyOccurredPercentage;

    @ApiModelProperty("合约规划已发生-本年已发生金额(不含税)")
    private BigDecimal contractYearlyOccurredAmount;

    @ApiModelProperty("合约规划已发生-本年已发生金额占比")
    private BigDecimal contractYearlyOccurredPercentage;

    @ApiModelProperty("合约规划费项Code")
    private String costChargeItemCode;

    @ApiModelProperty("占用目标成本二级费项-费项ID")
    private String chargeItemIdLevelTwo;

    @ApiModelProperty("占用目标成本二级费项-费项名称")
    private String chargeItemNameLevelTwo;

    @ApiModelProperty("占用目标成本二级费项-本月累计使用金额(不含税)")
    private BigDecimal costItemMonthlyAmount;

    @ApiModelProperty("占用目标成本二级费项-本月累计使用占比")
    private BigDecimal costItemMonthlyPercentage;

    @ApiModelProperty("占用目标成本二级费项-本年累计使用金额(不含税)")
    private BigDecimal costItemYearlyAmount;

    @ApiModelProperty("占用目标成本二级费项-本年累计使用占比")
    private BigDecimal costItemYearlyPercentage;

    @ApiModelProperty(value = "租户id")
    private String tenantId;

    @ApiModelProperty(value = "月度分摊明细")
    private List<ContractProjectPlanMonthlyAllocationF> monthlyAllocationList;

    @ApiModelProperty(value = "成本确认月度分摊明细")
    private List<ContractProjectPlanMonthlyAllocationV> costConfirmMonthlyAllocationList;

    @ApiModelProperty(value = "成本确认明细")
    private List<ContractProjectPlanCostConfirmF> costConfirmList;
}