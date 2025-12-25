package com.wishare.contract.apps.fo.revision.projectInitiation;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("合约规划月度分摊明细")
public class ContractProjectPlanMonthlyAllocationF {

    @ApiModelProperty("主键ID")
    private String id;

    @ApiModelProperty("月度分摊类型 0 合约规划, 1 成本确认")
    private Integer monthlyAllocationType;

    @ApiModelProperty("分摊类型 0 可用金额, 1 分摊金额, 2 分摊后剩余, 3 上次分摊金额")
    private Integer type;

    @ApiModelProperty(name = "动态成本GUID", notes = "动态成本GUID")
    private String dynamicCostGuid;

    @ApiModelProperty(name = "成本管控方式名称", notes = "成本管控方式名称")
    private String costControlTypeName;

    @ApiModelProperty(name = "成本管控方式枚举", notes = "成本管控方式枚举")
    private Integer costControlTypeEnum;

    @ApiModelProperty("年份")
    private String year;

    @ApiModelProperty(name = "当年合计", notes = "年度")
    private BigDecimal yearSurplus;

    @ApiModelProperty(name = "一月", notes = "一月")
    private BigDecimal janSurplus;

    @ApiModelProperty(name = "二月", notes = "二月")
    private BigDecimal febSurplus;

    @ApiModelProperty(name = "三月", notes = "三月")
    private BigDecimal marSurplus;

    @ApiModelProperty(name = "四月", notes = "四月")
    private BigDecimal aprSurplus;

    @ApiModelProperty(name = "五月", notes = "五月")
    private BigDecimal maySurplus;

    @ApiModelProperty(name = "六月", notes = "六月")
    private BigDecimal junSurplus;

    @ApiModelProperty(name = "七月", notes = "七月")
    private BigDecimal julSurplus;

    @ApiModelProperty(name = "八月", notes = "八月")
    private BigDecimal augSurplus;

    @ApiModelProperty(name = "九月", notes = "九月")
    private BigDecimal sepSurplus;

    @ApiModelProperty(name = "十月", notes = "十月")
    private BigDecimal octSurplus;

    @ApiModelProperty(name = "十一月", notes = "十一月")
    private BigDecimal novSurplus;

    @ApiModelProperty(name = "十二月", notes = "十二月")
    private BigDecimal decSurplus;

    @ApiModelProperty(value = "租户id")
    private String tenantId;

}