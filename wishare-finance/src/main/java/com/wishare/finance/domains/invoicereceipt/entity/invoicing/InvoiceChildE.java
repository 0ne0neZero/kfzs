package com.wishare.finance.domains.invoicereceipt.entity.invoicing;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
import java.io.Serializable;

/**
 * 发票子表（用于记录因为税盘限额而拆分的多张票据信息）(InvoiceChild)实体类
 *
 * @author makejava
 * @since 2022-12-03 10:45:12
 */
@Getter
@Setter
@TableName("invoice_child")
public class InvoiceChildE {
    /**
     * 主键id
     */
    private Long id;
    /**
     * 发票收据主表id
     */
    private Long invoiceReceiptId;
    /**
     * 发票代码
     */
    private String invoiceCode;
    /**
     * 发票号码
     */
    private String invoiceNo;
    /**
     * 开票金额
     */
    private Long invoiceAmount;
    /**
     * 红冲金额
     */
    private Long redInvoiceAmount = 0L;
    /**
     * 发票url地址
     */
    private String invoiceUrl;
    /**
     * 开票状态： 2 开票成功 3 开票失败 5 已红冲  8.部分红冲
     */
    private Integer state;
    /**
     * 诺诺发票pdf地址
     */
    private String nuonuoUrl;
    /**
     * 失败原因
     */
    private String failReason;
    /**
     * 第三方反参
     */
    private String thridReturnParameter;

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

