package com.wishare.contract.domains.vo.contractset;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * <p>
 * 合同空间资源信息
 * </p>
 *
 * @author wangrui
 * @since 2022-12-26
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class ContractBillListV {

    @ApiModelProperty("收款计划id")
    private String billId;
    @ApiModelProperty("摘要")
    private String remark;
    @ApiModelProperty("费项Id")
    private Long chargeItemId;
    @ApiModelProperty("成本中心Id")
    private Long costId;
    @ApiModelProperty("合同Id")
    private Long contractId;
    @ApiModelProperty("责任部门（组织id）")
    private Long orgId;
    @ApiModelProperty("费项")
    private String feeName;
    @ApiModelProperty("成本中心")
    private String costCenterName;
    @ApiModelProperty("责任部门")
    private String deptName;
    @ApiModelProperty("票据类型")
    private String invoiceType;
    @ApiModelProperty("税率")
    private String taxRate;
    @ApiModelProperty("计划收款日期")
    private String firstStartDate;
    @ApiModelProperty("计划收款金额（原币/含税）")
    private BigDecimal firstAmount;
    @ApiModelProperty("本币金额（含税）")
    private BigDecimal amount;
    @ApiModelProperty("本币金额（不含税）")
    private BigDecimal excludingTaxAmount;
    @ApiModelProperty("已收金额")
    private BigDecimal realAmount;
    @ApiModelProperty("未收金额")
    private BigDecimal receivedAmount;
    @ApiModelProperty("收款状态")
    private Integer billStatus;
    @ApiModelProperty("账单状态 0、正常，1、已终止")
    private Integer billingStatus;
    @ApiModelProperty("删除状态 0否 1是 默认0")
    private Integer deleteFlag;
}
