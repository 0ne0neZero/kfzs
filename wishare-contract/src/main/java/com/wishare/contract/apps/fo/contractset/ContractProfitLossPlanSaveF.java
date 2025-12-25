package com.wishare.contract.apps.fo.contractset;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * <p>
 * 保存请求参数
 * </p>
 *
 * @author wangrui
 * @since 2022-09-13
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class ContractProfitLossPlanSaveF {

    @ApiModelProperty("id")
    private Long id;
    @ApiModelProperty("费项Id")
    private Long chargeItemId;
    @ApiModelProperty("成本中心Id")
    private Long costId;
    @ApiModelProperty("账单id")
    private Long billId;
    @ApiModelProperty("收款计划Id")
    private Long collectionPlanId;
    @ApiModelProperty("合同性质 1 收入 2 支出")
    private Integer contractNature;
    @ApiModelProperty("合同Id")
    private Long contractId;
    @ApiModelProperty("责任部门（组织id）")
    private Long orgId;
    @ApiModelProperty("租户Id")
    private String tenantId;
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
    private LocalDate confirmTime;
    @ApiModelProperty("计划损益金额（原币/含税）")
    private BigDecimal amountTaxIncluded;
    @ApiModelProperty("本币金额（含税）")
    private BigDecimal localCurrencyAmount;
    @ApiModelProperty("本币金额（不含税）")
    private BigDecimal taxExcludedAmount;
    @ApiModelProperty("服务开始日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate serviceStartDate;
    @ApiModelProperty("服务结束日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate serviceEndDate;
    @ApiModelProperty("损益核算方式")
    private Integer profitLossAccounting;
    @ApiModelProperty("工程计提编号")
    private String engineeringCode;
    @ApiModelProperty("税额")
    private BigDecimal taxAmount;

}
