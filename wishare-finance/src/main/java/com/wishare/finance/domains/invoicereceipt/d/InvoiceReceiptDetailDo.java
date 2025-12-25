package com.wishare.finance.domains.invoicereceipt.d;

import com.wishare.finance.domains.invoicereceipt.entity.base.FinanceBaseEntity;
import lombok.*;

import java.time.LocalDateTime;

/**
 * @see com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceReceiptDetailE;
 *
 *
 *
 *
 * @see com.wishare.finance.domains.invoicereceipt.d.InvoiceReceiptDetailDo;
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper=false)
@Data
public class InvoiceReceiptDetailDo extends FinanceBaseEntity {
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
     * 房号ID
     */
    private String roomId;
    /**
     * 房号名称
     */
    private String roomName;
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
     * 备注
     */
    private String remark;


}

