package com.wishare.finance.domains.invoicereceipt.entity.invoicebook;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.google.common.collect.Lists;
import com.wishare.finance.domains.invoicereceipt.consts.enums.InvoicePoolStateEnum;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import com.wishare.finance.infrastructure.utils.InvoiceUtil;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 票本表(InvoiceBook)实体类
 *
 * @author makejava
 * @since 2022-08-31 10:34:45
 */
@Getter
@Setter
@TableName("invoice_book")
public class InvoiceBookE {
    /**
     * 主键id
     */
    private Long id;
    /**
     * 票本编号
     */
    private String invoiceBookNumber;
    /**
     * 票据类型:1增值税普通发票，2增值税专用发票，3纸质收据
     */
    private Integer type;
    /**
     * 购入组织id
     */
    private Long buyOrgId;
    /**
     * 购入组织名称
     */
    private String buyOrgName;
    /**
     * 购入数量
     */
    private Long buyQuantity;
    /**
     * 购入日期
     */
    private LocalDate buyDate;
    /**
     * 票面金额
     */
    private Long denomination;
    /**
     * 发票代码
     */
    private String invoiceCode;
    /**
     * 票本起始号码
     */
    private String invoiceStartNumber;
    /**
     * 票本结束号码
     */
    private String invoiceEndNumber;
    /**
     * 票本状态：1.未领用 2.部分领用 3.全部领用
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


    /**
     * 新增票池
     *
     * @return
     */
    public List<InvoicePoolE> generalInvoicePool() {
        List<InvoicePoolE> invoicePoolEList = Lists.newArrayList();
        String startNumber = this.getInvoiceStartNumber();
        for (int i = 0; i < buyQuantity; i++) {
            InvoicePoolE invoicePoolE = new InvoicePoolE();
            invoicePoolE.setId(IdentifierFactory.getInstance().generateLongIdentifier("invoice_pool_id"));
            invoicePoolE.setInvoiceBookId(this.id);
            invoicePoolE.setInvoiceType(this.getType());
            startNumber = InvoiceUtil.addOrSubtract(startNumber, 1);
            invoicePoolE.setInvoiceNum(startNumber);
            invoicePoolE.setState(InvoicePoolStateEnum.未领用.getCode());
            invoicePoolEList.add(invoicePoolE);
        }
        return invoicePoolEList;
    }
}

