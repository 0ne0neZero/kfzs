package com.wishare.finance.domains.invoicereceipt.command.invocing;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author xujian
 * @date 2022/9/21
 * @Description: 发票明细command
 */
@Getter
@Setter
public class AddInvoiceReceiptDetailCommand {

    /**
     * 商品编码
     */
    private String goodsCode;

    /**
     * 主键id
     */
    private Long id;
    /**
     * 开票主表id
     */
    private Long invoicingRecordId;
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
     * 单价含税标志
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
     * 费项id
     */
    private Long chargeItemId;

    /**
     * 费项名称
     */
    private String chargeItemName;

    /**
     * 账单的结算金额
     */
    private Long settleAmount;

    /**
     * 含税金额
     */
    private Long taxIncludedAmount;

}
