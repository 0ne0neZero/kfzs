package com.wishare.contract.apps.remote.fo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

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
public class ContractBillListRf {

    @ApiModelProperty("收款计划id")
    private String billId;
    @ApiModelProperty("摘要")
    private String remark;
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
    private String firstAmount;
    @ApiModelProperty("本币金额（含税）")
    private String amount;
    @ApiModelProperty("本币金额（不含税）")
    private String excludingTaxAmount;
    @ApiModelProperty("已收金额")
    private String realAmount;
    @ApiModelProperty("未收金额")
    private String receivedAmount;
    @ApiModelProperty("收款状态")
    private Integer billStatus;
    @ApiModelProperty("账单状态 0、正常，1、已终止")
    private Integer billingStatus;
    @ApiModelProperty("删除状态 0否 1是 默认0")
    private Integer deleteFlag;
}
