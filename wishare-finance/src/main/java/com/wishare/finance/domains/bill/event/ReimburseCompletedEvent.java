package com.wishare.finance.domains.bill.event;

import com.wishare.finance.domains.bill.entity.TransactionOrder;
import com.wishare.finance.domains.invoicereceipt.aggregate.InvoiceA;
import com.wishare.finance.domains.voucher.entity.Voucher;
import com.wishare.finance.infrastructure.event.DefaultEvent;
import com.wishare.finance.infrastructure.event.DispatcherType;
import com.wishare.finance.infrastructure.event.Event;
import com.wishare.finance.infrastructure.event.StreamEventDispatcher;
import com.wishare.starter.beans.IdentityInfo;
import com.wishare.starter.utils.ThreadLocalUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.io.Serializable;
import java.util.List;

/**
 * 报销完成事件
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/3/6
 */
@Getter
@Setter
@Event
public class ReimburseCompletedEvent extends DefaultEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    private TransactionOrder transactionOrder;

    private List<TransactionOrder> transactionOrders;

    private Voucher voucher;

    private List<Voucher> vouchers;

    private String businessId;

    private String billTypeCode;

    private IdentityInfo identityInfo;

    public ReimburseCompletedEvent() {
    }

    public ReimburseCompletedEvent(TransactionOrder transactionOrder, Voucher voucher) {
        this.transactionOrder = transactionOrder;
        this.voucher = voucher;
        identityInfo = ThreadLocalUtil.curIdentityInfo();
    }

    public ReimburseCompletedEvent(TransactionOrder transactionOrder, List<Voucher> vouchers) {
        this.transactionOrder = transactionOrder;
        this.vouchers = vouchers;
        identityInfo = ThreadLocalUtil.curIdentityInfo();
    }

    public ReimburseCompletedEvent(List<TransactionOrder> transactionOrders, List<Voucher> vouchers, String businessId,
                                   String billTypeCode) {
        this.transactionOrders = transactionOrders;
        this.vouchers = vouchers;
        this.businessId = businessId;
        this.billTypeCode = billTypeCode;
        identityInfo = ThreadLocalUtil.curIdentityInfo();
    }

}
