package com.wishare.contract.domains.vo.contractset;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContractBondPlanPageV {

    @ApiModelProperty("合同id -合同")
    private Long contractId;

    @ApiModelProperty("保证金计划id")
    private Long bondPlanId;

    @ApiModelProperty("合同名称-合同")
    private String contractName;

    @ApiModelProperty(value = "所属部门ID", required = true)
    private String belongOrgId;

    @ApiModelProperty("所属部门名称")
    private String belongOrgName;

    @ApiModelProperty("保证金额（本币）-合同")
    private BigDecimal bondHomeCurrency;

    @ApiModelProperty("计划收款时间-保证金计划")
    private LocalDateTime plannedCollectionTime;

    @ApiModelProperty("计划收款金额（原币）-保证金计划")
    private BigDecimal plannedCollectionAmount;

    @ApiModelProperty("本币金额（元）-保证金计划")
    private BigDecimal localCurrencyAmount;

    @ApiModelProperty("收款/付款状态 0未收/付  1部分收/付  2已收/付 -保证金计划")
    private Integer paymentStatus;

    @ApiModelProperty("已收/付款金额 -保证金计划")
    private BigDecimal paymentAmount;

    @ApiModelProperty("退款状态/收款状态 0未收/退  1部分收/退  2已收/退 -保证金计划")
    private Integer refundStatus;

    @ApiModelProperty("已收/退款金额 -保证金计划")
    private BigDecimal refundAmount;

    @ApiModelProperty("摘要 -保证金计划")
    private String summary;

    @ApiModelProperty("费项id -保证金计划")
    private Long chargeItemId;

    @ApiModelProperty("费项名称 -保证金计划")
    private String chargeItemName;

    @ApiModelProperty("成本中心id -保证金计划")
    private Long costId;

    @ApiModelProperty("成本中心名称 -保证金计划")
    private String costName;

    @ApiModelProperty("是否招投标保证金 -保证金计划")
    private Boolean bidBond;

    @ApiModelProperty("中台临时账单编号 -保证金计划")
    private String billNo;

    @ApiModelProperty("结转金额 -保证金计划")
    private BigDecimal settleTransferAmount;

    @ApiModelProperty("结转状态 0 未结转  1已结转 -保证金计划")
    private Integer settleTransferStatus;

    @ApiModelProperty("临时账单id")
    private Long billId;

    @ApiModelProperty("保证金类型 0 收取类 1缴纳类")
    private Integer bondType;

    @ApiModelProperty("关联招投标保证金账单编号 - 合同")
    private String bidBondBillNo;

    @ApiModelProperty("投保证金金额 - 合同")
    private BigDecimal bidBondAmount;

    @ApiModelProperty("合同状态 0 未履行 1 履行中 2 已到期 3 已终止  4 终止中 5已补充 - 合同")
    private Integer contractState;

    @ApiModelProperty("已开收据金额")
    private BigDecimal receiptAmount;

    @ApiModelProperty("扣款金额")
    private BigDecimal deductionAmount;
}
