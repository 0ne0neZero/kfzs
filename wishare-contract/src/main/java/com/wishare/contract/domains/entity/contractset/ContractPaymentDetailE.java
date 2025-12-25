package com.wishare.contract.domains.entity.contractset;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wishare.contract.domains.consts.contractset.ContractPaymentDetailFieldConst;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 合同付款明细表
 * </p>
 *
 * @author ljx
 * @since 2022-09-29
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@TableName("contract_payment_detail")
public class ContractPaymentDetailE{

    /**
     * id
     */
    @TableId(value = ContractPaymentDetailFieldConst.ID)
    private Long id;

    /**
     * 合同id
     */
    private Long contractId;

    /**
     * 收款计划id
     */
    private Long collectionPlanId;

    /**
     * 申请付款金额
     */
    private BigDecimal paymentAmount;

    /**
     * 申请付款时间
     */
    private LocalDateTime applyPaymentTime;

    /**
     * 实际付款时间
     */
    private LocalDateTime actualPaymentTime;

    /**
     * 付款申请编码
     */
    private String paymentApplyNumber;

    /**
     * 付款申请人id
     */
    private String userId;

    /**
     * 付款申请人姓名
     */
    private String userName;

    /**
     * 请款说明
     */
    private String remark;

    /**
     * 付款类型  0有票付款  1无票付款
     */
    private Integer paymentType;

    /**
     * 付款方式  0现金  1银行转帐  2汇款  3支票"
     */
    private Integer paymentMethod;


    /**
     * 发票文件集
     */
    private String invoiceUrl;

    /**
     * 票据号码集
     */
    private String invoiceCode;

    /**
     * 审核状态  0通过  1审核中  2未通过
     */
    private Integer auditStatus;

    /**
     * 审批编号
     */
    private String auditCode;

    /**
     * 付款状态  0已付款  1未付款
     */
    private Integer paymentStatus;

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
    @TableLogic(value = "0", delval = "1")
    private Integer deleted;

    /**
     * 创建人id
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
     * 操作人id
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
     * 审批流id
     */
    private Long procId;

}
