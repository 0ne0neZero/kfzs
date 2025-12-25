package com.wishare.contract.domains.vo.settle;

import com.wishare.contract.infrastructure.utils.PropertyMsg;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * @description:
 * @author: luguilin
 * @date: 2024-11-20 17:10
 **/
@Data
public class ExportSettlePlanListV {

    @ApiModelProperty("区域")
    private String region;

    @ApiModelProperty("项目名称")
    private String communityName;

    @ApiModelProperty("合同编码")
    private String contractNo;

    @ApiModelProperty("合同CT码")
    private String conmaincode;

    @ApiModelProperty("合同管理类别")
    private String conmanagetypename;

    @ApiModelProperty("合同-是否四保1服 1-是 else-否")
    private Integer contractServeType;

    @ApiModelProperty("合同-是否四保1服 1-是 else-否")
    private String contractServeDesc;

    @ApiModelProperty("合同名称")
    private String contractName;

    @ApiModelProperty("供应商名称")
    private String oppositeOne;

    @ApiModelProperty("合同金额")
    private BigDecimal contractAmount;

    @ApiModelProperty(value = "合同起止日期")
    private String gmtExpireStartEnd;

    @ApiModelProperty("合同-合同履约状态")
    private String contractStatusName;

    @ApiModelProperty(value = "结算周期")
    private Integer splitMode;

    @ApiModelProperty(value = "一月份含税金额")
    private BigDecimal oneTaxAmount;

    @ApiModelProperty(value = "二月份含税金额")
    private BigDecimal twoTaxAmount;

    @ApiModelProperty(value = "三月份含税金额")
    private BigDecimal threeTaxAmount;

    @ApiModelProperty(value = "四月份含税金额")
    private BigDecimal fourTaxAmount;

    @ApiModelProperty(value = "五月份含税金额")
    private BigDecimal fiveTaxAmount;

    @ApiModelProperty(value = "六月份含税金额")
    private BigDecimal sixTaxAmount;

    @ApiModelProperty(value = "七月份含税金额")
    private BigDecimal sevenTaxAmount;

    @ApiModelProperty(value = "八月份含税金额")
    private BigDecimal eightTaxAmount;

    @ApiModelProperty(value = "九月份含税金额")
    private BigDecimal nineTaxAmount;

    @ApiModelProperty(value = "十月份含税金额")
    private BigDecimal tenTaxAmount;

    @ApiModelProperty(value = "十一月份含税金额")
    private BigDecimal elevenTaxAmount;

    @ApiModelProperty(value = "十二月份含税金额")
    private BigDecimal twelveTaxAmount;

    @ApiModelProperty(value = "本年合计")
    private BigDecimal totalForThisYear;

    @ApiModelProperty(value = "本年前期合计")
    private BigDecimal totalForThePreviousPeriod;

    @ApiModelProperty(value = "往年合计")
    private BigDecimal totalOfPreviousYears;

    @ApiModelProperty(value = "往期合计")
    private BigDecimal previousTotal;

    @ApiModelProperty(value = "本期合计")
    private BigDecimal totalForThisPeriod;

    @ApiModelProperty(value = "创建时间")
    private LocalDate createTime;
}
