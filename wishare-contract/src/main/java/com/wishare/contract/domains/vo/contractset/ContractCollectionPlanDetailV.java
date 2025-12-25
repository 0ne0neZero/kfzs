package com.wishare.contract.domains.vo.contractset;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 收款计划/付款计划列表
 * @author ljx
 * @since 2022-09-26
 */

@Getter
@Setter
@ToString
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContractCollectionPlanDetailV {

    @ApiModelProperty("主键")
    private Long id;
    @ApiModelProperty("费项Id")
    private Long chargeItemId;
    @ApiModelProperty("费项名称")
    private String chargeItemName;

    @ApiModelProperty(value = "所属部门ID", required = true)
    private String belongOrgId;

    @ApiModelProperty("所属部门名称")
    private String belongOrgName;
    @ApiModelProperty("成本中心Id")
    private Long costId;
    @ApiModelProperty("成本中心名")
    private String costName;
    @ApiModelProperty("合同Id")
    private Long contractId;
    @ApiModelProperty("责任部门（组织id）")
    private Long orgId;
    @ApiModelProperty("责任部门名")
    private String orgName;
    @ApiModelProperty("预算科目")
    private String budgetAccount;
    @ApiModelProperty("税率")
    private String taxRate;
    @ApiModelProperty("摘要")
    private String summary;
    @ApiModelProperty("票据类型")
    private String billType;
    @ApiModelProperty("计划收款时间")
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDate plannedCollectionTime;
    @ApiModelProperty("计划收款金额（原币/含税）")
    private BigDecimal plannedCollectionAmount;
    @ApiModelProperty("本币金额（含税）")
    private BigDecimal localCurrencyAmount;
    @ApiModelProperty("本币金额（不含税）")
    private BigDecimal taxExcludedAmount;
    @ApiModelProperty("服务开始日期")
//    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
//    @DateTimeFormat(pattern = "yyyy-MM-dd")
//    @JSONField(format = "yyyy-MM-dd")
    private LocalDate serviceStartDate;
    @ApiModelProperty("服务结束日期")
//    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
//    @DateTimeFormat(pattern = "yyyy-MM-dd")
//    @JSONField(format = "yyyy-MM-dd")
    private LocalDate serviceEndDate;
    @ApiModelProperty("创建人名称")
    private String creatorName;
    @ApiModelProperty("创建时间")
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtCreate;
    @ApiModelProperty("已收款/付款金额")
    private BigDecimal paymentAmount;
    @ApiModelProperty("已开票/收票金额")
    private BigDecimal invoiceAmount;
    @ApiModelProperty("未收款/付款金额")
    private BigDecimal notPaymentAmount;
    @ApiModelProperty("未开票/收票金额")
    private BigDecimal notInvoiceAmount;
    @ApiModelProperty("可开票金额")
    private BigDecimal canInvoiceAmount;
    @ApiModelProperty("收款/付款状态 0未收/付  1部分收/付  2已收/付")
    private Integer paymentStatus;
    @ApiModelProperty("开票/收票状态 0未开/收  1部分开/收  2已开/收")
    private Integer invoiceStatus;
    /**
     * 合同信息
     */
    @ApiModelProperty("合同名称")
    private String name;
    @ApiModelProperty("合同编号")
    private String contractNo;
    @ApiModelProperty("合同性质 1 收入 2 支出")
    private Integer contractNature;
//    @ApiModelProperty("公司id")
//    private String tenantId;
//    @ApiModelProperty("公司名称")
//    private String tenantName;
//    @ApiModelProperty("项目id")
//    private String communityId;
//    @ApiModelProperty("项目名称")
//    private String communityName;
    @ApiModelProperty("甲方id")
    private Long partyAId;
    @ApiModelProperty("乙方id")
    private Long partyBId;
    @ApiModelProperty("丙方id")
    private Long partyCId;
    @ApiModelProperty("甲方名称")
    private String partyAName;
    @ApiModelProperty("乙方名称")
    private String partyBName;
    @ApiModelProperty("丙方名称")
    private String partyCName;
    @ApiModelProperty("合同金额（含税）")
    private BigDecimal amountTaxIncluded;
    @ApiModelProperty("合同生效日期")
//    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
//    @DateTimeFormat(pattern = "yyyy-MM-dd")
//    @JSONField(format = "yyyy-MM-dd")
    private LocalDate gmtExpireStart;
    @ApiModelProperty("合同结束日期")
//    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
//    @DateTimeFormat(pattern = "yyyy-MM-dd")
//    @JSONField(format = "yyyy-MM-dd")
    private LocalDate gmtExpireEnd;
    @ApiModelProperty("减免金额")
    private BigDecimal creditAmount;
    @ApiModelProperty("原币金额（含税）")
    private BigDecimal originalCurrency;
    @ApiModelProperty("币种")
    private String currency;
    @ApiModelProperty("汇率")
    private String exchangeRate;
    @ApiModelProperty("合同预警状态 0正常 1 临期 2 已到期")
    private Integer warnState;
    @ApiModelProperty("税额")
    private BigDecimal taxAmount;
    @ApiModelProperty("合同状态 0 未履行 1 履行中 2 已到期 3 已终止  4 终止中 5已补充")
    private Integer contractState;
}
