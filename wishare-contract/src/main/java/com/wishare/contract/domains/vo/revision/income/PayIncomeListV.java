package com.wishare.contract.domains.vo.revision.income;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.wishare.contract.infrastructure.utils.PropertyMsg;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;


@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "收入计划列表", description = "收入计划列表")
public class PayIncomeListV{

    @ApiModelProperty("合同ID")
    private String contractId;

    @ApiModelProperty("合同名称")
    private String name;

    @ApiModelProperty("合同编号")
    private String contractNo;

    @PropertyMsg("区域")
    private String region;

    @ApiModelProperty("项目名称")
    private String communityName;

    @ApiModelProperty("项目id")
    private String communityId;

    @ApiModelProperty("合同CT码")
    private String conmaincode;

    @ApiModelProperty("收款计划编码")
    private String costEstimationCode;

    @ApiModelProperty("收入计划id")
    private String id;

    @ApiModelProperty("账单编号")
    private String billCode;

    @ApiModelProperty("付费类型ID")
    private String payTypeId;

    @ApiModelProperty("付费类型")
    private String payType;

    @ApiModelProperty("数量")
    private BigDecimal amountNum;

    @ApiModelProperty("账单金额")
    private BigDecimal plannedCollectionAmount;

    @ApiModelProperty("税率")
    private String taxRate;

    @ApiModelProperty("税率ID")
    private String taxRateId;

    @ApiModelProperty("不含税金额")
    private BigDecimal noTaxAmount;

    @ApiModelProperty("税额")
    private BigDecimal taxAmount;

    @ApiModelProperty("应收日期")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(format = "yyyy-MM-dd")
    private Date plannedCollectionTime;

    @ApiModelProperty("归属月")
    private String belongingMonth;

    @ApiModelProperty("账单创建日期")
    private LocalDateTime billCreationTime;

    @ApiModelProperty("费用开始日期")
    private LocalDate costStartTime;

    @ApiModelProperty("费用结束日期")
    private LocalDate costEndTime;

    @ApiModelProperty("法定单位-ID")
    private String ourPartyId;

    @ApiModelProperty("法定单位-名称")
    private String ourParty;

    @ApiModelProperty("收费对象-ID")
    private String draweeId;

    @ApiModelProperty("收费对象-名称")
    private String drawee;

    @ApiModelProperty("业务类型")
    private String serviceType;

    @ApiModelProperty("审核状态0待提交1审批中2已通过3已拒绝")
    private Integer reviewStatus;

    @ApiModelProperty("审核状态")
    private String reviewStatusName;

    @ApiModelProperty("确收状态")
    private Integer settlementStatus;

    @ApiModelProperty("确收状态")
    private String settlementStatusName;

    @ApiModelProperty("账单来源")
    private String billSource;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("创建人")
    private String creatorName;

    @ApiModelProperty("创建时间")
    private LocalDateTime gmtCreate;

    @ApiModelProperty(" 推送状态")
    private Integer pushStatus;

    @ApiModelProperty(" 推送状态")
    private String pushStatusName;

    @ApiModelProperty("收款计划id")
    private String planId;
    @ApiModelProperty("入账状态")
    private Integer iriStatus;
    @ApiModelProperty("入账状态")
    private String iriStatusName;
    @ApiModelProperty("调整金额")
    private BigDecimal adjustmentAmount;

    @ApiModelProperty("收入计划编码")
    private String incomePlanCode;

    @ApiModelProperty("收款计划期数")
    private Integer termDate;

    @ApiModelProperty("费项")
    private String chargeItem;

    @ApiModelProperty("收款金额")
    private BigDecimal paymentAmount;

    @ApiModelProperty("未收款金额")
    private BigDecimal noReceiptAmount;

    @ApiModelProperty("成本中心")
    private String costCenterName;

    @ApiModelProperty("客户名称")
    private String oppositeOne;

    @ApiModelProperty("所属部门")
    private String departName;


}
