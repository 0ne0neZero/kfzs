package com.wishare.contract.domains.vo.settle;

import com.wishare.contract.infrastructure.utils.PropertyMsg;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;


@Data
public class SettlePlanListV {

    /**--------------------------------------合同基本信息-------------------------------------------*/
    @PropertyMsg("计划id")
    private String planId;

    @PropertyMsg("合同id")
    private String contractId;


    @PropertyMsg("区域")
    private String region;

    @ApiModelProperty("项目名称")
    private String communityName;


    @ApiModelProperty("项目id")
    private String communityId;

    @ApiModelProperty("合同CT码")
    private String conmaincode;


    @ApiModelProperty("合同管理类别")
    private String conmanagetypename;

    @ApiModelProperty("合同管理类别")
    private String conmanagetype;

    @ApiModelProperty("合同编码")
    private String contractNo;

    @ApiModelProperty("合同-是否四保1服 1-是 else-否")
    private Integer contractServeType;

    @ApiModelProperty("合同名称")
    private String contractName;

    @ApiModelProperty("供应商名称")
    private String oppositeOne;

    @ApiModelProperty("合同金额")
    private BigDecimal contractAmount;

    @ApiModelProperty(value = "合同起始日期")
    private String gmtExpireStart;

    @ApiModelProperty(value = "合同结束日期")
    private String gmtExpireEnd;

    @ApiModelProperty("合同-合同履约状态")
    private String contractStatusName;

    @ApiModelProperty("合同-合同履约状态")
    private Integer status;

    @ApiModelProperty(value = "结算周期")
    private Integer splitMode;

    @ApiModelProperty(value = "结算周期名称")
    private String splitModeName;
    @ApiModelProperty("合同业务线（1.物管合同；2.代建合同;3.商管合同）")
    private Integer contractBusinessLine;

    @ApiModelProperty("合同业务线描述")
    private String contractBusinessLineName;
    /**--------------------------------------结算信息-------------------------------------------*/

    @ApiModelProperty("年")
    private String year;

    @ApiModelProperty("1月份")
    private String oneMonth ="1";

    @ApiModelProperty("含税金额")
    private BigDecimal oneTaxAmount;

    @ApiModelProperty("2月份")
    private String twoMonth = "2";

    @ApiModelProperty("含税金额")
    private BigDecimal twoTaxAmount;

    @ApiModelProperty("3月份")
    private String threeMonth= "3";

    @ApiModelProperty("含税金额")
    private BigDecimal threeTaxAmount;

    @ApiModelProperty("4月份")
    private String fourMonth= "4";

    @ApiModelProperty("含税金额")
    private BigDecimal fourTaxAmount;

    @ApiModelProperty("5月份")
    private String fiveMonth= "5";

    @ApiModelProperty("含税金额")
    private BigDecimal fiveTaxAmount;

    @ApiModelProperty("6月份")
    private String sixMonth= "6";

    @ApiModelProperty("含税金额")
    private BigDecimal sixTaxAmount;

    @ApiModelProperty("7月份")
    private String sevenMonth= "7";

    @ApiModelProperty("含税金额")
    private BigDecimal sevenTaxAmount;


    @ApiModelProperty("8月份")
    private String eightMonth= "8";

    @ApiModelProperty("含税金额")
    private BigDecimal eightTaxAmount;


    @ApiModelProperty("9月份")
    private String nineMonth= "9";

    @ApiModelProperty("含税金额")
    private BigDecimal nineTaxAmount;


    @ApiModelProperty("10月份")
    private String tenMonth= "10";

    @ApiModelProperty("含税金额")
    private BigDecimal tenTaxAmount;


    @ApiModelProperty("11月份")
    private String elevenMonth= "11";

    @ApiModelProperty("含税金额")
    private BigDecimal elevenTaxAmount;

    @ApiModelProperty("12月份")
    private String twelveMonth= "12";

    @ApiModelProperty("含税金额")
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

    @ApiModelProperty(value = "是否可以编辑收款计划")
    private Boolean canEditPlan = true;

    @ApiModelProperty(value = "是否可以删除收款计划")
    private Boolean canDeletePlan = true;


}
