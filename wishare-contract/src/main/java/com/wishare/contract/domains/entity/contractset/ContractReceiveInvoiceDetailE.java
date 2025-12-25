package com.wishare.contract.domains.entity.contractset;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.wishare.contract.domains.consts.contractset.ContractReceiveInvoiceDetailFieldConst;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
/**
 * <p>
 * 付款计划收票明细
 * </p>
 *
 * @author ljx
 * @since 2022-11-29
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@TableName("contract_receive_invoice_detail")
public class ContractReceiveInvoiceDetailE{

    /**
     * id
     */
    @TableId(value = ContractReceiveInvoiceDetailFieldConst.ID)
    private Long id;

    /**
     * 合同Id
     */
    private Long contractId;

    /**
     * 付款计划id
     */
    private Long collectionPlanId;

    /**
     * 收票金额
     */
    private BigDecimal invoiceAmount;

    /**
     * 发票类型  3增值税电子发票
     */
    private Integer invoiceType;

    /**
     * 票据url
     */
    private String invocieUrl;

    /**
     * 票据号码
     */
    private String invoiceNumber;

    /**
     * 收票时间
     */
    private LocalDateTime invoiceTime;

    /**
     * 关联审批
     */
    private String auditCode;

    /**
     * 审核状态  0通过  1审核中  2未通过
     */
    private Integer auditStatus;

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
     * 审批流id
     */
    private Long procId;


}
