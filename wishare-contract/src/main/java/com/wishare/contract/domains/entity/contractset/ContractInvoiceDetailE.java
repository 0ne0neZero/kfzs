package com.wishare.contract.domains.entity.contractset;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.wishare.contract.domains.consts.contractset.ContractInvoiceDetailFieldConst;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <p>
 * 合同开票明细表
 * </p>
 *
 * @author ljx
 * @since 2022-09-26
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@TableName("contract_invoice_detail")
public class ContractInvoiceDetailE {

    /**
     * id
     */
    @TableId(value = ContractInvoiceDetailFieldConst.ID, type = IdType.AUTO)
    private Long id;

    /**
     * 合同Id
     */
    private Long contractId;

    /**
     * 收款计划id
     */
    private Long collectionPlanId;

    /**
     * 开票申请编号
     */
    private String invoiceApplyNumber;

    /**
     * 申请开票金额
     */
    private BigDecimal invoiceApplyAmount;

    /**
     * 申请开票时间
     */
    private LocalDateTime invoiceApplyTime;

    /**
     * 开票说明
     */
    private String remark;

    /**
     * 开票人id
     */
    private String userId;

    /**
     * 开票人姓名
     */
    private String userName;

    /**
     * 开票税额
     */
    private BigDecimal invoiceTax;

    /**
     * 开票状态  0成功  1开票中  2失败
     */
    private Integer invoiceStatus;

    /**
     * 票据号码
     */
    private String invoiceNumber;

    /**
     * 审核状态  0通过  1审核中  2未通过
     */
    private Integer auditStatus;

    /**
     * 审批编号
     */
    private String auditNumber;

    /**
     * 财务状态  0已确认  1未确认
     */
    private Integer confirmStatus;

    /**
     * 租户id
     */
    private String tenantId;

    /**
     * 是否删除:0未删除，1已删除
     */
    @TableLogic
    private Integer deleted;

    /**
     * 创建人ID
     */
    @TableField(fill = FieldFill.INSERT)
    private String creator;

    /**
     * 创建人姓名
     */
    @TableField(fill = FieldFill.INSERT)
    private String creatorName;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    /**
     * 操作人ID
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String operator;

    /**
     * 操作人姓名
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String operatorName;

    /**
     * 操作时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModify;

    /**
     * 货品名称
     */
    private String productName;

    /**
     * 发票类型  1 增值类普通发票  2  增值类专用发票  3其他发票
     */
    private String billType;

    /**
     * 中台发票id
     */
    private Long invoiceId;
}
