package com.wishare.contract.domains.entity.revision.income;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <p>
 * 合同成本损益表
 * </p>
 *
 * @author chenglong
 * @since 2023-10-26
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@TableName("contract_income_conclude_settlement_plan_relation")
public class ContractIncomeConcludeSettlementPlanRelationE {

    /**
     * 主键ID
     */
    @TableId(value = ID)
    private Long id;

    /**
     * 结算单id
     **/
    @TableField("settlement_id")
    private String settlementId;

    /**
     * 成本预估计划id
     **/
    @TableField("plan_id")
    private String planId;

    public static final String ID = "id";
}
