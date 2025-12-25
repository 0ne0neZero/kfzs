package com.wishare.finance.domains.invoicereceipt.entity.invoicebook;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 票池表(InvoicePool)实体类
 *
 * @author makejava
 * @since 2022-09-20 14:59:16
 */
@Getter
@Setter
@TableName("invoice_pool")
public class InvoicePoolE {
    /**
     * 主键id
     */
    private Long id;
    /**
     * 票本id
     */
    private Long invoiceBookId;

    /**
     * 增值税普通发票
     * 1: 增值税普通发票
     * 2: 增值税专用发票
     * 3: 增值税电子发票
     * 4: 增值税电子专票
     * 5: 收据
     * 6：电子收据
     * 7:纸质收据
     * 8：全电普票
     */
    private Integer invoiceType;
    /**
     * 发票编号
     */
    private String invoiceNum;
    /**
     * 领用状态：1 未领用；2 已领用
     */
    private Integer state;
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

