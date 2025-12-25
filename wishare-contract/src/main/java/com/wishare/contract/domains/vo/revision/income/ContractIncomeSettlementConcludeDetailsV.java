package com.wishare.contract.domains.vo.revision.income;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @description:
 * @author: zhangfuyu
 * @Date: 2023/7/13/14:50
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "支出合同付款计划结算单详情表视图对象", description = "支出合同付款计划结算单详情表视图对象")
public class ContractIncomeSettlementConcludeDetailsV {

    @ApiModelProperty("主键")
    private String id;
    @ApiModelProperty("结算单编号")
    private String payFundNumber;
    @ApiModelProperty("计划付款日期")
    private LocalDate plannedCollectionTime;
    @ApiModelProperty("结算金额")
    private BigDecimal plannedCollectionAmount;
    @ApiModelProperty("扣款金额")
    private BigDecimal deductionAmount;
    @ApiModelProperty("结算状态")
    private Integer settleStatus;
    @ApiModelProperty("结算状态名称")
    private String settleStatusName;
    @ApiModelProperty("收票金额")
    private BigDecimal invoiceApplyAmount;
    @ApiModelProperty("收票状态")
    private Integer invoiceStatus;
    @ApiModelProperty("收票状态名称")
    private String invoiceStatusName;
    @ApiModelProperty("付款金额")
    private String paymentAmount;
    @ApiModelProperty("付款状态")
    private Integer paymentStatus;
    @ApiModelProperty("付款状态名称")
    private String paymentStatusName;
    @ApiModelProperty("付款类型  0有票付款  1无票付款")
    private Integer paymentType;
    @ApiModelProperty("付款类型名称")
    private String paymentTypeName;
    @ApiModelProperty("付款方式  0现金  1银行转帐  2支票")
    private Integer paymentMethod;
    @ApiModelProperty("付款方式名称")
    private String paymentMethodName;
    @ApiModelProperty("备注")
    private String remark;
    @ApiModelProperty("创建人")
    private String creator;
    @ApiModelProperty("创建时间")
    private LocalDateTime gmtCreate;
}
