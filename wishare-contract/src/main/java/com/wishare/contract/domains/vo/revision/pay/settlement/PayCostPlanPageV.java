package com.wishare.contract.domains.vo.revision.pay.settlement;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wishare.starter.beans.Tree;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @description:
 * @author: zhangfuyu
 * @Date: 2023/7/7/10:12
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "成本计划-列表对象", description = "成本计划-列表对象")
public class PayCostPlanPageV{

    @ApiModelProperty("成本计划id")
    private String id;

    @ApiModelProperty("区域名称")
    private String contractRegion;

    @ApiModelProperty("项目id")
    private String communityId;

    @ApiModelProperty("项目名称")
    private String communityName;

    @ApiModelProperty("合同CT码")
    private String conMainCode;

    @ApiModelProperty("合同id")
    private String contractId;

    @ApiModelProperty("合同-合同编码")
    private String contractNo;

    @ApiModelProperty("合同-合同名称")
    private String contractName;


    @ApiModelProperty("结算计划id")
    private String planId;

    @ApiModelProperty("结算计划编号")
    private String planNo;

    @ApiModelProperty("结算计划期数")
    private Integer termDate;


    @ApiModelProperty("成本计划编码")
    private String costPlanCode;

    @ApiModelProperty("供应商id")
    private String merchant;

    @ApiModelProperty("供应商名称")
    private String merchantName;

    @ApiModelProperty("付费对象类型")
    private String payObjectType;

    @ApiModelProperty("费项id")
    private String chargeItemId;

    @ApiModelProperty("费项名称")
    private String chargeItem;

    @ApiModelProperty("费用开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date costStartTime;

    @ApiModelProperty("费用结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date costEndTime;

    @ApiModelProperty("应结算日期")
    @JsonFormat(pattern = "yyyy-MM",timezone = "GMT+8")
    private Date plannedCollectionTime;

    @ApiModelProperty("预估结算金额")
    private BigDecimal plannedSettlementAmount;

    @ApiModelProperty("付款金额")
    private BigDecimal paymentAmount;

    @ApiModelProperty("未付款金额")
    private BigDecimal notPaymentAmount;

    @ApiModelProperty("税率id")
    private String taxRateId;

    @ApiModelProperty("税率")
    private String taxRate;

    @ApiModelProperty("结算状态 0未结算 1部分结算 2已结算")
    private Integer settlementStatus;

    @ApiModelProperty("入账状态 0未入账 1已入账")
    private Integer billGenerationStatus;

    @ApiModelProperty("成本中心id")
    private String costCenterId;

    @ApiModelProperty("成本中心名称")
    private String costCenterName;

    @ApiModelProperty("法定单位id")
    private String ourPartyId;

    @ApiModelProperty("法定单位名称")
    private String ourParty;

    @ApiModelProperty("所属部门id")
    private String departId;

    @ApiModelProperty("所属部门名称")
    private String departName;

    @ApiModelProperty("创建人")
    private String creatorName;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date gmtCreate;
}
