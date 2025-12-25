package com.wishare.contract.domains.entity.contractset;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.wishare.contract.domains.consts.contractset.ContractBondCarryoverDetailFieldConst;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
/**
 * <p>
 * 保证金结转明细表
 * </p>
 *
 * @author ljx
 * @since 2022-11-21
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@TableName("contract_bond_carryover_detail")
public class ContractBondCarryoverDetailE{

    /**
     * id
     */
    @TableId(value = ContractBondCarryoverDetailFieldConst.ID)
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
     * 关联投标保证金账单编号
     */
    private String bidBondBillNo;

    /**
     * 投标保证金金额
     */
    private BigDecimal bidBondAmount;

    /**
     * 申请结转时间
     */
    private LocalDateTime carryoverTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 审批状态 0通过  1审核中  2未通过
     */
    private Integer auditStatus;

    /**
     * 关联审批编号
     */
    private String auditCode;

    /**
     * 是否删除:0未删除，1已删除
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
    private LocalDateTime gmtCreate;


}
