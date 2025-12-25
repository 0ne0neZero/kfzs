package com.wishare.contract.domains.vo.revision.projectInitiation.cost;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 合约规划新增返回
 */
@Data
public class DynamicCostIncurredRespF {

    @ApiModelProperty("动态成本GUID")
    private String dynamicCostGuid;

    @ApiModelProperty("请求参数中具体占用动态成本的费项GUID")
    private String occupyItemDynamicCostGuid;

    @ApiModelProperty("项目名称")
    private String projectName;

    @ApiModelProperty("费项GUID")
    private String accountItemGuid;

    @ApiModelProperty("费项全码")
    private String accountItemFullCode;

    @ApiModelProperty("费项全称")
    private String accountItemFullName;

    @ApiModelProperty("费项编码")
    private String accountItemCode;

    @ApiModelProperty("费项名称")
    private String accountItemName;

    @ApiModelProperty("费项父级GUID")
    private String parentAccountItemGuid;

    @ApiModelProperty("费项层级")
    private Integer accountItemLevel;

    @ApiModelProperty("控制类型")
    private String controlType;

    @ApiModelProperty("成本管控方式名称")
    private String costControlTypeName;

    @ApiModelProperty("成本管控方式枚举")
    private Integer costControlTypeEnum;

    @ApiModelProperty("成本预警方式名称")
    private String costWarnTypeName;

    @ApiModelProperty("成本预警方式枚举")
    private Integer costWarnTypeEnum;

    @ApiModelProperty("预算编制年份")
    private Integer budgetYear;

    @ApiModelProperty("年度目标")
    private BigDecimal yearTargetAmount;

    @ApiModelProperty("年度余额")
    private BigDecimal yearSurplus;

    @ApiModelProperty("校验版本，1：执行版，2:在途版")
    private Integer checkVersion;

    @ApiModelProperty("一季度余额")
    private BigDecimal oneSeasonSurplus;

    @ApiModelProperty("二季度余额")
    private BigDecimal twoSeasonSurplus;

    @ApiModelProperty("三季度余额")
    private BigDecimal threeSeasonSurplus;

    @ApiModelProperty("四季度余额")
    private BigDecimal fourSeasonSurplus;

    @ApiModelProperty("一月余额")
    private BigDecimal janSurplus;

    @ApiModelProperty("二月余额")
    private BigDecimal febSurplus;

    @ApiModelProperty("三月余额")
    private BigDecimal marSurplus;

    @ApiModelProperty("四月余额")
    private BigDecimal aprSurplus;

    @ApiModelProperty("五月余额")
    private BigDecimal maySurplus;

    @ApiModelProperty("六月余额")
    private BigDecimal junSurplus;

    @ApiModelProperty("七月余额")
    private BigDecimal julSurplus;

    @ApiModelProperty("八月余额")
    private BigDecimal augSurplus;

    @ApiModelProperty("九月余额")
    private BigDecimal sepSurplus;

    @ApiModelProperty("十月余额")
    private BigDecimal octSurplus;

    @ApiModelProperty("十一月余额")
    private BigDecimal novSurplus;

    @ApiModelProperty("十二月余额")
    private BigDecimal decSurplus;

    @ApiModelProperty("校验失败原因")
    private String errorMessage;

    @ApiModelProperty("管控比例")
    private BigDecimal controlRatio;

    @ApiModelProperty("可用金额")
    private BigDecimal availableAmount;

    @ApiModelProperty("已发生金额")
    private BigDecimal yearIncurredAmount;

    @ApiModelProperty("当前发生金额")
    private BigDecimal currentIncurredAmount;

}
