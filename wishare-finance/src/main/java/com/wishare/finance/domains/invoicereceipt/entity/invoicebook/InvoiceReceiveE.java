package com.wishare.finance.domains.invoicereceipt.entity.invoicebook;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 票本领用记录表(InvoiceBookReceiveRecord)实体类
 *
 * @author makejava
 * @since 2022-08-31 13:57:12
 */
@Getter
@Setter
@TableName("invoice_receive")
public class InvoiceReceiveE {
    /**
     * 主键id
     */
    private Long id;
    /**
     * 领用票本id
     */
    private Long receiveInvoiceBookId;
    /**
     * 领用组织id
     */
    private Long receiveOrgId;
    /**
     * 领用组织名称
     */
    private String receiveOrgName;
    /**
     * 领用时间
     */
    private LocalDateTime receiveTime;
    /**
     * 发票起始号码
     */
    private Long invoiceStartNumber;
    /**
     * 发票起始号码
     */
    private Long invoiceEndNumber;
    /**
     * 领用数量
     */
    private Long receiveNumber;
    /**
     * 项目信息
     */
    private String community;
    /**
     * 租户id
     */
    @TableField(fill = FieldFill.INSERT)
    private String tenantId;
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
     * 创建时间 yyyy-MM-dd HH:mm:ss
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
     * 修改时间 yyyy-MM-dd HH:mm:ss
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModify;

}

