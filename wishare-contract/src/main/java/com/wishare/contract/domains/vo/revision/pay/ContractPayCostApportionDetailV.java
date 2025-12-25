package com.wishare.contract.domains.vo.revision.pay;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author hhb
 * @describe
 * @date 2025/11/7 17:25
 */
@Data
public class ContractPayCostApportionDetailV {
    @ApiModelProperty("分摊类型")
    private Integer apportionType;
    @ApiModelProperty("分摊类型描述")
    private String apportionTypeDesc;
    @ApiModelProperty("分摊类型小i提示")
    private String apportionTypePrompt;
    @ApiModelProperty("年份")
    private String year;
    @ApiModelProperty("动态成本GUID")
    private String dynamicCostGuid;
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

    //成本-管控方式
    private Integer costControlTypeEnum;
    //成本-管控方式名称
    private String costControlTypeName;
}
