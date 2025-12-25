package com.wishare.contract.domains.entity.revision.projectInitiation;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wishare.contract.domains.entity.revision.BaseE;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 立项关联合约规划成本确认明细
 */
@Data
@TableName("contract_project_plan_cost_confirm")
@EqualsAndHashCode(callSuper = true)
public class ContractProjectPlanCostConfirmE extends BaseE {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 关联的立项ID
     */
    private String projectInitiationId;

    /**
     * 合约规划
     */
    private String contractProjectPlanId;

    /**
     * 变更类型 0 确认金额, 1 关联合同
     */
    private Integer type;

    /**
     * 变更前内容
     */
    private String originalContent;

    /**
     * 变更后内容
     */
    private String revisedContent;

    /**
     * 成本确认变更审核状态 0 待发起、1 审批中、2 已通过、3 已驳回
     */
    private Integer bpmReviewStatus;

    /**
     * 租户id
     */
    private String tenantId;

}