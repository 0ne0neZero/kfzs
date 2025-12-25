package com.wishare.finance.domains.invoicereceipt.entity.invoicing;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import com.wishare.starter.helpers.UidHelper;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 开票明细表(InvoicingRecordDetail)实体类
 * 开票明细表(invoice_receipt_detail)
 * @author makejava
 * @since 2022-09-21 20:30:27
 */
@Getter
@Setter
@TableName("invoice_receipt_detail")
public class InvoiceReceiptDetailE {
    /**
     * 主键id
     */
    private Long id;
    /**
     * 发票收据主表id
     */
    private Long invoiceReceiptId;
    /**
     * 行号
     */
    private Integer lineNo;
    /**
     * 账单id
     */
    private Long billId;
    /**
     * 账单编号
     */
    private String billNo;
    /**
     * 收款单id
     */
    private Long gatherBillId;
    /**
     * 收款单号
     */
    private String gatherBillNo;
    /**
     * 收款单明细id
     */
    private Long gatherDetailId;
    /**
     * 房号ID
     */
    private String roomId;
    /**
     * 房号名称
     */
    private String roomName;
    /**
     * 空间路径
     */
    private String path;
    /**
     * 商品名称
     */
    private String goodsName;
    /**
     * 数量
     */
    private String num;
    /**
     * 税率
     */
    private String taxRate;
    /**
     * 单位
     */
    private String unit;
    /**
     * 单价含税标志：0:不含税,1:含税
     */
    private Integer withTaxFlag;
    /**
     * 单价
     */
    private String price;
    /**
     * 规格型号
     */
    private String spectype;

    /**
     * 账单类型 1-应收账单， 2-预收账单， 3-临时收费账单， 4-应付账单
     */
    private Integer billType;

    /**
     * 账单开始时间
     */
    private LocalDateTime billStartTime;

    /**
     * 账单结束时间
     */
    private LocalDateTime billEndTime;

    /**
     * 账单缴费时间
     */
    private LocalDateTime billPayTime;

    /**
     * 收费对象类型（0:业主，1开发商，2租客）
     */
    private Integer payerType;

    /**
     * 费项id
     */
    private Long chargeItemId;

    /**
     * 费项名称
     */
    private String chargeItemName;

    /**
     * 账单结算金额
     */
    private Long settleAmount;

    /**
     * 账单的开票金额
     */
    private Long invoiceAmount;

    /**
     * 账单所处开票的价税合计
     */
    private Long priceTaxAmount;

    /**
     * 账单的折扣额
     */
    private Long discountAmount;

    /**
     * 备注
     */
    private String remark;

    /**
     * 方圆套打收据专用备注
     */
    private String remarkNew;

    /**
     * 票面税额
     */
    private Long faceTaxAmount;
    /**
     * 票面不含税金额
     */
    private Long faceTaxExcludedAmount;

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

//    @TableField(exist = false)
    private Integer overdue;


    public InvoiceReceiptDetailE() {
        generateIdentifier();
    }

    /**
     * 构造id
     */
    public void generateIdentifier() {
        if (Objects.isNull(getId())){
            setId(IdentifierFactory.getInstance().generateLongIdentifier("invoice_receipt_id"));
        }

    }

    public void generateNewIdentifier() {
        setId(IdentifierFactory.getInstance().generateLongIdentifier("invoice_receipt_id"));
    }
}

