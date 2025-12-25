package com.wishare.contract.domains.entity.contractset;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.wishare.contract.domains.consts.contractset.ContractBondPlanFieldConst;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <p>
 * 合同保证金计划信息
 * </p>
 *
 * @author wangrui
 * @since 2022-09-09
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@TableName("contract_bond_plan")
public class ContractBondPlanE {

    /**
     * 主键
     */
    private Long id;

    /**
     * 费项Id
     */
    private Long chargeItemId;

    /**
     * 成本中心Id
     */
    private Long costId;

    /**
     * 合同Id
     */
    private Long contractId;

    /**
     * 责任部门（组织id）
     */
    private Long orgId;

    /**
     * 预算科目
     */
    private String budgetAccount;

    /**
     * 摘要
     */
    private String summary;

    /**
     * 合同性质 1 收入 2 支出
     */
    private Integer contractNature;

    /**
     * 票据类型
     */
    private String billType;

    /**
     * 招投标保证金
     */
    private Boolean bidBond;

    /**
     * 计划收款时间
     */
    private LocalDate plannedCollectionTime;

    /**
     * 计划收款金额（原币）
     */
    private BigDecimal plannedCollectionAmount;

    /**
     * 本币金额（元）
     */
    private BigDecimal localCurrencyAmount;

    /**
     * 扣款金额
     */
    private BigDecimal deductionAmount;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private String creator;

    /**
     * 创建人名称
     */
    @TableField(fill = FieldFill.INSERT)
    private String creatorName;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    /**
     * 操作人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String operator;

    /**
     * 操作人名称
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String operatorName;

    /**
     * 操作时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModify;

    private Integer deleted;

    /**
     * 结转状态 0 未结转  1已结转
     */
    private Integer settleTransferStatus;

    /**
     * 结转金额
     */
    private BigDecimal settleTransferAmount;

    /**
     * 中台临时账单id
     */
    private Long billId;

    /**
     * 中台临时账单编号（招投标保证金才有）
     */
    private String billNo;
    /**
     * 已收/付款金额
     */
    private BigDecimal paymentAmount;
    /**
     * 收款/付款状态 0未收/付  1部分收/付  2已收/付
     */
    private Integer paymentStatus;
    /**
     * 已收/退款金额
     */
    private BigDecimal refundAmount;
    /**
     * 退款状态/收款状态 0未收/退  1部分收/退  2已收/退
     */
    private Integer refundStatus;

    /**
     * 已开收据金额
     */
    private BigDecimal receiptAmount;
}
