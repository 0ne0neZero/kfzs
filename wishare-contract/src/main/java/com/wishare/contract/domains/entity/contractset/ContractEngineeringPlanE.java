package com.wishare.contract.domains.entity.contractset;

import com.baomidou.mybatisplus.annotation.*;
import com.wishare.contract.domains.consts.contractset.ContractEngineeringPlanFieldConst;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 工程类合同计提信息表
 * </p>
 *
 * @author wangrui
 * @since 2022-11-29
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@TableName("contract_engineering_plan")
public class ContractEngineeringPlanE {

    /**
     * 主键
     */
    private Long id;

    /**
     * 合同id
     */
    private Long contractId;

    /**
     * 损益计划Id
     */
    private Long profitLossId;

    /**
     * 租户Id
     */
    private Long tenantId;

    /**
     * 计提编号
     */
    private String accrualCode;

    /**
     * 上次工程完成百分比
     */
    private BigDecimal lastPercent;

    /**
     * 上次工程总金额
     */
    private BigDecimal lastAmount;

    /**
     * 本次工程百分比
     */
    private BigDecimal thisTimePercent;

    /**
     * 本次工程总金额
     */
    private BigDecimal thisTimeAmount;

    /**
     * 本次计提金额
     */
    private BigDecimal accrualAmount;

    /**
     * 计提资料
     */
    private String accrualData;

    /**
     * 计提资料名称
     */
    private String accrualDataName;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private String creator;

    /**
     * 创建人名称
     */
    @TableField(fill = FieldFill.INSERT)
    private String creatorName;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    /**
     * 操作人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String operator;

    /**
     * 操作人名称
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String operatorName;

    /**
     * 操作时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModify;

    /**
     * 是否删除  0 正常 1 删除
     */
    @TableLogic
    private Integer deleted;


}
