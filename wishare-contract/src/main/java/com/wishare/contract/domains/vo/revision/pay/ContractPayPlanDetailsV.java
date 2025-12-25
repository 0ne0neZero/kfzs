package com.wishare.contract.domains.vo.revision.pay;

import com.wishare.contract.domains.vo.revision.pay.bill.ContractPayBillV;
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
@ApiModel(value = "支出合同付款计划详情表视图对象", description = "支出合同付款计划详情表视图对象")
public class ContractPayPlanDetailsV {
    @ApiModelProperty("主键")
    private String id;
    @ApiModelProperty("供应商名称")
    private String merchantName;
    @ApiModelProperty("合同名称")
    private String contractName;
    @ApiModelProperty("合同id")
    private String contractId;
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
    @ApiModelProperty("扣款金额")
    private BigDecimal deductionAmount;
    @ApiModelProperty("结算状态")
    private Integer paymentStatus;
    @ApiModelProperty("收票金额")
    private BigDecimal invoiceApplyAmount;
    @ApiModelProperty("收票状态")
    private Integer invoiceStatus;
    @ApiModelProperty("付款金额")
    private String paymentAmount;
    @ApiModelProperty("未付金额")
    private BigDecimal noPaymentAmount;
    @ApiModelProperty("计划状态")
    private Integer planStatus;
    @ApiModelProperty("创建人")
    private String creator;
    @ApiModelProperty("创建时间")
    private LocalDateTime gmtCreate;
    @ApiModelProperty("拆分方式")
    private Integer splitMode;
    @ApiModelProperty("金额比例")
    private String ratioAmount;
    @ApiModelProperty("服务类型")
    private String serviceType;
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
    /*******************************************************************************/
    @ApiModelProperty("结算单详情")
    private List<ContractPaySettlementConcludeDetailsV> contractPaySettlementConcludeList;
    @ApiModelProperty("收票单详情")
    private List<ContractPayBillV> contractPayBillList;
}
