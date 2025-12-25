package com.wishare.contract.domains.entity.contractset;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.wishare.contract.domains.consts.contractset.ContractCollectionPlanFieldConst;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <p>
 * 合同收款计划信息
 * </p>
 *
 * @author wangrui
 * @since 2022-09-09
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@TableName("contract_collection_plan")
public class ContractCollectionPlanE {

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
     * 成本中心名称
     */
    private String costName;

    /**
     * 合同Id
     */
    private Long contractId;

    /**
     * 责任部门（组织id）
     */
    private Long orgId;

    /**
     * 责任部门名称
     */
    private String orgName;

    /**
     * 预算科目
     */
    private String budgetAccount;

    /**
     * 租户id
     */
    private String tenantId;

    /**
     * 税种税率id
     */
    private String taxRateIdPath;

    /**
     * 税率
     */
    private BigDecimal taxRate;

    /**
     * 摘要
     */
    private String summary;

    /**
     * 合同性质 1 收入 2 支出
     */
    private Integer contractNature;

    /**
     * 合同预警状态 0正常 1 临期 2 已到期
     */
    private Integer warnState;

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
     * 计划收款金额（原币/含税）
     */
    private BigDecimal plannedCollectionAmount;

    /**
     * 本币金额（含税）
     */
    private BigDecimal localCurrencyAmount;

    /**
     * 本币金额（不含税）
     */
    private BigDecimal taxExcludedAmount;

    /**
     * 损益核算方式
     */
    private Integer profitLossAccounting;

    /**
     * 服务开始日期
     */
    private LocalDate serviceStartDate;

    /**
     * 服务结束日期
     */
    private LocalDate serviceEndDate;

    /**
     * 合同开始日期
     */
    private LocalDate gmtExpireStart;

    /**
     * 合同开始日期
     */
    private LocalDate gmtExpireEnd;

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
     * 已收款/付款金额
     */
    private BigDecimal paymentAmount;

    /**
     * 已开票/收票金额
     */
    private BigDecimal invoiceAmount;

    /**
     * 收款/付款状态 0未收/付  1部分收/付  2已收/付
     */
    private Integer paymentStatus;

    /**
     * 开票/收票状态 0未开/收  1部分开/收  2已开/收
     */
    private Integer invoiceStatus;

    /**
     * 减免金额
     */
    private BigDecimal creditAmount;

    /**
     * 税额
     */
    private BigDecimal taxAmount;

    /**
     * 款项比例
     */
    private BigDecimal paymentProportion;

    /**
     * 逾期原因
     */
    private String overdueReason;

    /**
     * 逾期说明
     */
    private String overdueStatement;

    /**
     * 逾期原因id集
     */
    private String overdueReasonIds;

    /**
     * 逾期原因填写时间
     */
    private LocalDateTime overdueReasonTime;
}
