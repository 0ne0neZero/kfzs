package com.wishare.contract.domains.vo.contractset;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.alibaba.fastjson.annotation.JSONField;
/**
* <p>
* 
* </p>
*
* @author wangrui
* @since 2022-09-13
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class ContractProfitLossPlanV {

    @ApiModelProperty("主键")
    private Long id;
    @ApiModelProperty("费项Id")
    private Long chargeItemId;
    @ApiModelProperty("成本中心Id")
    private Long costId;
    @ApiModelProperty("账单id")
    private Long billId;
    @ApiModelProperty("收/付款计划id")
    private Long collectionPlanId;
    @ApiModelProperty("合同Id")
    private Long contractId;
    @ApiModelProperty("责任部门（组织id）")
    private Long orgId;
    @ApiModelProperty("预算科目")
    private String budgetAccount;
    @ApiModelProperty("摘要")
    private String summary;
    @ApiModelProperty("票据类型")
    private String billType;
    @ApiModelProperty("税率id")
    private Long taxRateId;
    @ApiModelProperty("税率")
    private String taxRate;
    @ApiModelProperty("损益确认时间")
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDate confirmTime;
    @ApiModelProperty("计划损益金额（原币/含税）")
    private BigDecimal amountTaxIncluded;
    @ApiModelProperty("本币金额（含税）")
    private BigDecimal localCurrencyAmount;
    @ApiModelProperty("本币金额（不含税）")
    private BigDecimal taxExcludedAmount;
    @ApiModelProperty("服务开始日期")
    private LocalDate serviceStartDate;
    @ApiModelProperty("服务结束日期")
    private LocalDate serviceEndDate;
    @ApiModelProperty("损益核算方式")
    private Integer profitLossAccounting;
    @ApiModelProperty("部门名称")
    private String orgName;
    @ApiModelProperty("成本中心名称")
    private String costName;
    @ApiModelProperty("费项名称")
    private String chargeItemName;
    @ApiModelProperty("已收款/付款金额")
    private BigDecimal paymentAmount;
    @ApiModelProperty("已开票/收票金额")
    private BigDecimal invoiceAmount;
    @ApiModelProperty("收款/付款状态 0未收/付  1部分收/付  2已收/付")
    private Integer paymentStatus;
    @ApiModelProperty("开票/收票状态 0未开/收  1部分开/收  2已开/收")
    private Integer invoiceStatus;
    @ApiModelProperty("工程计提编号")
    private String engineeringCode;
    @ApiModelProperty("税额")
    private BigDecimal taxAmount;
}
