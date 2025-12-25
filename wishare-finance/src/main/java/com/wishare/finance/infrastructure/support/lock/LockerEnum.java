package com.wishare.finance.infrastructure.support.lock;

/**
 * @NAME: LockerEnum
 * @Author: xujian
 * @Date: 2022/1/4
 */
public enum LockerEnum implements ILockerParam {

    BILL_LOCK("Bill", "账单全局锁", 180),
    INVOICE_BATCH_LOCK("invoiceBatch", "开具发票(蓝票,红票)", 180),
    TRANSACTION("Transaction", "交易", 180),
    INVOICE_CALLBACK_LOCK("invoiceCallback", "开具发票回调", 300),
    GATHER_LOCK("GatherBill","收款单全局锁",180),
    INVOICE_GATHER_DETAIL_LOCK("InvoiceGatherBillDetail","收款明细开票全局锁",300),

    E_SIGN_RECEIPT_LOCK("eSignReceipt","eSign签署重复提交",15),
    KING_DEE_PUSH("kingDeePush","金蝶单据推送",10)
    ;

    private String prefix;
    private String desc;

    private int leaseTime;

    LockerEnum(String prefix, String desc, int leaseTime) {
        this.prefix = prefix;
        this.desc = desc;
        this.leaseTime = leaseTime;
    }

    @Override
    public String prefix() {
        return prefix;
    }

    @Override
    public String desc() {
        return desc;
    }

    @Override
    public int defLeaseTime() {
        return leaseTime;
    }
}


