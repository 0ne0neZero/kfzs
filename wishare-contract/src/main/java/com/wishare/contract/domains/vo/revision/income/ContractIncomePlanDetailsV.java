package com.wishare.contract.domains.vo.revision.income;

import com.wishare.contract.domains.vo.revision.pay.bill.ContractPayBillV;
import com.wishare.contract.domains.vo.revision.pay.bill.ContractSettFundV;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author zhangfuyu
 * @mender 龙江锋
 * @Date 2023/7/13/14:30
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "收入合同付款计划详情表视图对象", description = "收入合同付款计划详情表视图对象")
public class ContractIncomePlanDetailsV {
    @ApiModelProperty("主键")
    private String id;
    @ApiModelProperty("收款计划编号")
    private String payNotecode;
    @ApiModelProperty("客户名称")
    private String customerName;
    @ApiModelProperty("合同id")
    private String contractId;
    @ApiModelProperty("合同名称")
    private String contractName;
    @ApiModelProperty("合同编号")
    private String contractNo;
    @ApiModelProperty("期数")
    private Integer termDate;
    @ApiModelProperty("计划付款日期")
    private LocalDate plannedCollectionTime;
    @ApiModelProperty("计划付款金额")
    private BigDecimal plannedCollectionAmount;
    @ApiModelProperty("结算金额")
    private BigDecimal settlementAmount;
    @ApiModelProperty("收款金额")
    private BigDecimal receiptAmount;
    @ApiModelProperty("结算状态")
    private Integer paymentStatus;
    @ApiModelProperty("结算状态")
    private Integer settleStatus;
    @ApiModelProperty("审批状态")
    private Integer reviewStatus;
    @ApiModelProperty("付款方式  0现金  1银行转帐  2支票")
    private Integer paymentMethod;
    @ApiModelProperty("收票金额")
    private BigDecimal invoiceApplyAmount;
    @ApiModelProperty("收票状态")
    private Integer invoiceStatus;
    @ApiModelProperty("付款金额")
    private String paymentAmount;
    @ApiModelProperty("未收款金额")
    private BigDecimal noReceiptAmount;
    @ApiModelProperty("计划状态")
    private Integer planStatus;
    @ApiModelProperty("创建人")
    private String creator;
    @ApiModelProperty("创建时间")
    private LocalDateTime gmtCreate;
    @ApiModelProperty("更新时间")
    private LocalDateTime gmtModify;
    @ApiModelProperty("拆分方式")
    private Integer splitMode;
    @ApiModelProperty("金额比例")
    private String ratio;
    @ApiModelProperty("服务类型")
    private String serviceType;
    @ApiModelProperty("服务类型")
    private String serviceTypeName;
    @ApiModelProperty("费项")
    private String chargeItem;
    @ApiModelProperty("税率")
    private String taxRate;
    @ApiModelProperty("不含税金额")
    private BigDecimal noTaxAmount;
    @ApiModelProperty("税额")
    private BigDecimal taxAmount;
    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("费用开始日期")
    private LocalDate costStartTime;
    @ApiModelProperty("费用结束日期")
    private LocalDate costEndTime;
    @ApiModelProperty("成本预估编码")
    private String costEstimationCode;
    @ApiModelProperty("暂估确收金额")
    private BigDecimal confirmedAmountReceivedTemp;


    @ApiModelProperty("成本中心名称")
    private String costCenterName;

    @ApiModelProperty(value = "所属部门")
    private String departName;

    @ApiModelProperty(value = "法定单位")
    private String ourPartyId;

    @ApiModelProperty(value = "法定单位")
    private String ourParty;

    @ApiModelProperty(value = "合同金额")
    private BigDecimal contractAmountOriginalRate;


    /**************************************************************************************/
    @ApiModelProperty("结算单详情")
    private List<ContractIncomeSettlementConcludeDetailsV> contractPaySettDetailsSaveList;
    @ApiModelProperty("收票单详情")
    private List<ContractPayBillV> contractPayBillList;
    @ApiModelProperty("结算单收款单详情")
    private List<ContractSettFundV> contractSettFundVList;
}
