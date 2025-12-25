package com.wishare.contract.domains.entity.contractset;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.wishare.contract.domains.consts.contractset.ContractBondPaymentDetailFieldConst;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
/**
 * <p>
 * 保证金计划付/退款明细
 * </p>
 *
 * @author ljx
 * @since 2022-10-25
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@TableName("contract_bond_payment_detail")
public class ContractBondPaymentDetailE{

    /**
     * id
     */
    @TableId(value = ContractBondPaymentDetailFieldConst.ID)
    private Long id;

    /**
     * 合同Id
     */
    private Long contractId;

    /**
     * 保证金计划id
     */
    private Long bondPlanId;

    /**
     * 申请付/退款金额（元）
     */
    private BigDecimal paymentAmount;

    /**
     * 付/退款方式  0现金  1银行转帐  2汇款  3支票
     */
    private Integer paymentMethod;

    /**
     * 关联审批编号
     */
    private String auditCode;

    /**
     * 审批状态 0通过  1审核中  2未通过
     */
    private Integer auditStatus;

    /**
     * 财务状态  0已确认  1未确认
     */
    private Integer confirmStatus;

    /**
     * 申请付/退款时间
     */
    private LocalDateTime applyPaymentTime;

    /**
     * 实际付/退款时间
     */
    private LocalDateTime actualPaymentTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否删除
     */
    @TableLogic(value = "0", delval = "1")
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
     * 付/退类型  1 付 2退
     */
    private Integer type;

    /**
     * 付/退申请编号
     */
    private String paymentNumber;
}
