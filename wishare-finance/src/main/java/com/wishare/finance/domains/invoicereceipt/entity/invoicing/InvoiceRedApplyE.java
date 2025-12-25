package com.wishare.finance.domains.invoicereceipt.entity.invoicing;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

import static com.wishare.finance.infrastructure.conts.TableNames.INVOICE_RED_APPLY;

/**
 *
 * @author luzhonghe
 * @version 1.0
 * @since 2023/4/15
 */
@Getter
@Setter
@TableName(INVOICE_RED_APPLY)
public class InvoiceRedApplyE {

    /**
     * 主键id(红字确认单申请号)
     */
    private Long id;
    /**
     * 发票收据主表id
     */
    private Long invoiceReceiptId;
    /**
     * 红票发票流水号
     */
    private String invoiceSerialNum;
    /**
     * 原蓝票发票收据主表id
     */
    private Long blueInvoiceReceiptId;
    /**
     * 冲红原因： 1销货退回 2开票有误 3服务中
     * 止 4销售折让
     */
    private String redReason;
    /**
     * 红字确认单uuid
     */
    private String billUuid;
    /**
     * 红字确认单状态： 01 无需确认 02 销方录入待
     * 购方确认 03 购方录入待销方确认 04 购销双方
     * 已确认 05 作废（销方录入购方否认） 06 作废
     * （购方录入销方否认） 07 作废（超72小时未确
     * 认） 08 作废（发起方已撤销） 09 作废（确认
     * 后撤销） 15 申请中 16 申请失败
     */
    private String status;
    /**
     * 操作状态：（根据操作方返回对应状态，可能为
     * 空） 01 撤销中 02撤销失败 03 确认中 04 确认
     * 失败
     */
    private String requestStatus;
    /**
     * 红字确认单明细信息列表（全电部分冲红
     * 时才需要传）
     */
    private String detail;
    /**
     * 租户id
     */
    @TableField(fill = FieldFill.INSERT)
    private String tenantId;
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

    public InvoiceRedApplyE() {
        setId(IdentifierFactory.getInstance().generateLongIdentifier(INVOICE_RED_APPLY));
    }
}
