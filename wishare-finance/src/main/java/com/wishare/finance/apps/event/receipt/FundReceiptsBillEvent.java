package com.wishare.finance.apps.event.receipt;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.List;
import java.util.concurrent.CountDownLatch;

@Getter
public class FundReceiptsBillEvent extends ApplicationEvent {

    private final List<Long> ids;

    private final List<String> supCpUnitIds;

    private final CountDownLatch countDownLatch;

    public FundReceiptsBillEvent(Object source, List<Long> ids, List<String> supCpUnitIds, CountDownLatch countDownLatch) {
        super(source);
        this.ids = ids;
        this.supCpUnitIds = supCpUnitIds;
        this.countDownLatch = countDownLatch;
    }
}