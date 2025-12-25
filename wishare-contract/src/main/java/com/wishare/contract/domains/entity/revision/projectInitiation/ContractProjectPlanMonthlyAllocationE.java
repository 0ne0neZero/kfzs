package com.wishare.contract.domains.entity.revision.projectInitiation;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wishare.contract.domains.entity.revision.BaseE;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 立项关联合约规划月度分摊明细
 */
@Data
@Accessors(chain = true)
@TableName("contract_project_plan_monthly_allocation")
@EqualsAndHashCode(callSuper = true)
public class ContractProjectPlanMonthlyAllocationE extends BaseE {

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
     * 月度分摊类型 0 合约规划, 1 成本确认
     */
    private Integer monthlyAllocationType;

    /**
     * 分摊类型 0 可用金额, 1 分摊金额, 2 分摊后剩余, 3 上次分摊金额
     */
    private Integer type;

    /**
     * 动态成本GUID
     */
    private String dynamicCostGuid;

    /**
     * 成本管控方式名称
     */
    private String costControlTypeName;

    /**
     * 成本管控方式枚举
     */
    private String costControlTypeEnum;

    /**
     * 年份
     */
    private String year;

    /**
     * 当年合计
     */
    private BigDecimal yearSurplus;

    /**
     * 一月
     */
    private BigDecimal janSurplus;

    /**
     * 二月
     */
    private BigDecimal febSurplus;

    /**
     * 三月
     */
    private BigDecimal marSurplus;

    /**
     * 四月
     */
    private BigDecimal aprSurplus;

    /**
     * 五月
     */
    private BigDecimal maySurplus;

    /**
     * 六月
     */
    private BigDecimal junSurplus;

    /**
     * 七月
     */
    private BigDecimal julSurplus;

    /**
     * 八月
     */
    private BigDecimal augSurplus;

    /**
     * 九月
     */
    private BigDecimal sepSurplus;

    /**
     * 十月
     */
    private BigDecimal octSurplus;

    /**
     * 十一月
     */
    private BigDecimal novSurplus;

    /**
     * 十二月
     */
    private BigDecimal decSurplus;

    /**
     * 租户id
     */
    private String tenantId;

}