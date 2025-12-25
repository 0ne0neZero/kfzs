package com.wishare.contract.domains.entity.revision.pay;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@TableName("pay_cost_plan")
public class PayCostPlanE {

    /**
     * 主键ID
     */
    @TableId(value = "id")
    private String id;

    /**
     * 成本计划编码
     */
    private String costPlanCode;

    /**
     * 租户id
     */
    private String tenantId;

    /**
     * 关联合同ID
     */
    private String contractId;

    /**
     * 临时账单ID
     */
    private String billId;

    /**
     * 账单编号
     */
    private String billCode;

    /**
     * 结算计划id
     */
    private String planId;

    /**
     * 结算计划编号
     */
    private String planNo;

    /**
     * 核销状态 0未结算 1部分结算 2已结算
     */
    private Integer settlementStatus;

    /**
     * 入账状态 0未入账 1已入账
     */
    private Integer billGenerationStatus;

    /**
     * 费用开始日期
     */
    private LocalDate costStartTime;

    /**
     * 费用结束日期
     */
    private LocalDate costEndTime;

    /**
     * 应结算日期
     */
    private LocalDate plannedCollectionTime;

    /**
     * 计划付款日期-暂不处理
     */
    private LocalDate plannedPayDate;

    /**
     * 计划付款金额
     */
    private BigDecimal plannedSettlementAmount;

    /**
     * 税额
     */
    private BigDecimal taxAmount;

    /**
     * 不含税金额
     */
    private BigDecimal noTaxAmount;

    /**
     * 付款金额-核销处理-对应账单应收金额
     */
    private BigDecimal paymentAmount;

    /**
     * 调整后暂估金额
     */
    private BigDecimal estimatedAmount;

    /**
     * 调整金额
     */
    private BigDecimal adjustAmount;

    /**
     * 结算计划-编码-冗余
     */
    private String planNumber;

    /**
     * 结算计划-期数-冗余
     */
    private Integer termDate;

    /**
     * 供应商-冗余
     */
    private String merchant;

    /**
     * 供应商名称-冗余
     */
    private String merchantName;

    /**
     * 付费对象类型
     */
    private String payObjectType;

    /**
     * 费项ID
     */
    private Long chargeItemId;

    /**
     * 费项
     */
    private String chargeItem;

    /**
     * 税率ID
     */
    private String taxRateId;

    /**
     * 税率
     */
    private String taxRate;

    /**
     * 款项类型ID
     */
    private String typeId;

    /**
     * 款项类型
     */
    private String type;

    /**
     * 付款方式ID
     */
    private String payTypeId;

    /**
     * 付款方式
     */
    private String payType;

    /**
     * 成本中心ID-合同冗余
     */
    private String costCenterId;

    /**
     * 成本中心名称-合同冗余
     */
    private String costCenterName;

    /**
     * 我方单位-ID-合同冗余
     */
    private String ourPartyId;

    /**
     * 我方单位-名称-合同冗余
     */
    private String ourParty;

    /**
     * 所属部门ID-合同冗余
     */
    private String departId;

    /**
     * 所属部门名称-合同冗余
     */
    private String departName;

    /**
     * 收入方编码
     */
    private String payeeId;

    /**
     * 收入方名称
     */
    private String payee;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 创建人名称
     */
    private String creatorName;

    /**
     * 创建时间
     */
    private LocalDateTime gmtCreate;

    /**
     * 操作人
     */
    private String operator;

    /**
     * 操作人名称
     */
    private String operatorName;

    /**
     * 操作时间
     */
    private LocalDateTime gmtModify;

    /**
     * 是否删除 0 正常 1 删除
     */
    private Integer deleted;
    /**
     * 入账时间
     */
    private LocalDateTime iriTime;
    /**
     * 数量
     */
    private BigDecimal amountNum;

    /**
     * 后续推送财务云类型(1:计提 2:实签)
     */
    private Integer billType;

    /**
     * 初始不含税金额
     **/
    private BigDecimal originNoTaxAmount;

    /**
     * 初始税额
     **/
    private BigDecimal originTaxAmount;

    /**
     * 减免金额
     **/
    private BigDecimal reductionAmount;
    /**
     * 合同清单id
     */
    private String payFundId;
}
