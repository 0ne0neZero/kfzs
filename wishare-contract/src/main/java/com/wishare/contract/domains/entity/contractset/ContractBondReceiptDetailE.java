package com.wishare.contract.domains.entity.contractset;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.wishare.contract.domains.consts.contractset.ContractBondReceiptDetailFieldConst;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
/**
 * <p>
 * 保证金收据明细表
 * </p>
 *
 * @author ljx
 * @since 2022-12-12
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@TableName("contract_bond_receipt_detail")
public class ContractBondReceiptDetailE {

    /**
     * id
     */
    @TableId(value = ContractBondReceiptDetailFieldConst.ID)
    private Long id;

    /**
     * 合同id
     */
    private Long contractId;

    /**
     * 保证金计划id
     */
    private Long bondPlanId;

    /**
     * 收票金额
     */
    private BigDecimal invoiceAmount;

    /**
     * 发票类型  6电子收据
     */
    private Integer invoiceType;

    /**
     * 开票状态  0开票中  1成功 2失败
     */
    private Integer invocieStatus;

    /**
     * 票据号码  收据编号
     */
    private String invoiceNumber;

    /**
     * 申请开票时间
     */
    private LocalDateTime invoiceTime;

    /**
     * 备注
     */
    private String remark;

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

    /**
     * 收据id
     */
    private Long receiptId;

}
