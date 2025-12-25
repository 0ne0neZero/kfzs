package com.wishare.contract.domains.entity.revision.projectInitiation;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wishare.contract.apps.fo.revision.projectInitiation.ContractProjectPlanCostConfirmF;
import com.wishare.contract.apps.fo.revision.projectInitiation.ContractProjectPlanMonthlyAllocationF;
import com.wishare.contract.domains.entity.revision.BaseE;
import com.wishare.contract.domains.vo.revision.projectInitiation.ContractProjectPlanMonthlyAllocationV;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 立项关联合约规划实体类
 */
@Data
@TableName("contract_project_plan")
@EqualsAndHashCode(callSuper = true)
public class ContractProjectPlanE extends BaseE {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 关联的立项ID
     */
    private String projectInitiationId;

    /**
     * 类型 0 合约规划 1 合同
     */
    private Integer type;

    /**
     * 合约规划ID|合同id
     */
    private String contractPlanId;

    /**
     * 合约规划名称|合同名称
     */
    private String contractPlanName;

    /**
     * 合约规划金额|合同金额（不含税）
     */
    private BigDecimal contractPlanAmount;

    /**
     * 合同周期
     */
    private String contractTimeCycle;

    /**
     * 分摊金额(不含税)
     */
    private BigDecimal allocationAmount;

    /**
     * 确认金额(不含税)
     */
    private BigDecimal confirmAmount;

    /**
     * 合约规划已发生-本月已发生金额(不含税)
     */
    private BigDecimal contractMonthlyOccurredAmount;

    /**
     * 合约规划已发生-本月已发生金额占比
     */
    private BigDecimal contractMonthlyOccurredPercentage;

    /**
     * 合约规划已发生-本年已发生金额(不含税)
     */
    private BigDecimal contractYearlyOccurredAmount;

    /**
     * 合约规划已发生-本年已发生金额占比
     */
    private BigDecimal contractYearlyOccurredPercentage;

    /**
     * 合约规划费项Code
     */
    private String costChargeItemCode;

    /**
     * 占用目标成本二级费项
     */
    private String chargeItemIdLevelTwo;

    /**
     * 占用目标成本二级费项
     */
    private String chargeItemNameLevelTwo;

    /**
     * 占用目标成本二级费项-本月已发生金额(不含税)
     */
    private BigDecimal costItemMonthlyAmount;

    /**
     * 占用目标成本二级费项-本月已发生金额占比
     */
    private BigDecimal costItemMonthlyPercentage;

    /**
     * 占用目标成本二级费项-本年已发送金额(不含税)
     */
    private BigDecimal costItemYearlyAmount;

    /**
     * 占用目标成本二级费项-本年已发送金额占比
     */
    private BigDecimal costItemYearlyPercentage;

    /**
     * 租户id
     */
    private String tenantId;

    /**
     * 月度分摊明细
     */
    @TableField(exist = false)
    private List<ContractProjectPlanMonthlyAllocationV> monthlyAllocationList;

    /**
     * 成本确认月度分摊明细
     */
    @TableField(exist = false)
    private List<ContractProjectPlanMonthlyAllocationF> costConfirmMonthlyAllocationList;

    /**
     * 成本确认明细
     */
    @TableField(exist = false)
    private List<ContractProjectPlanCostConfirmF> costConfirmList;

}