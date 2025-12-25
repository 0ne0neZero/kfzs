package com.wishare.finance.apps.service.yuanyang;

import cn.hutool.core.collection.CollUtil;
import com.github.rholder.retry.Retryer;
import com.github.rholder.retry.RetryerBuilder;
import com.github.rholder.retry.StopStrategies;
import com.github.rholder.retry.WaitStrategies;
import com.wishare.finance.domains.bill.entity.TransactionOrder;
import com.wishare.finance.domains.configure.accountbook.entity.AccountBookE;
import com.wishare.finance.domains.voucher.entity.BusinessBill;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author szh
 * @date 2024/4/17 11:55
 */
public class RetryUtils {




    public static final Retryer<TransactionOrder> transactionOrderRetryer = RetryerBuilder.<TransactionOrder>newBuilder()
            .retryIfResult(Objects::isNull)
            .retryIfException()

            .withWaitStrategy(WaitStrategies.fixedWait(30, TimeUnit.SECONDS))
            .withStopStrategy(StopStrategies.stopAfterAttempt(4))
            .withRetryListener(new MasterRetryListener<>())
            .build();

    // BusinessBill
    public static final Retryer<BusinessBill> businessBillRetryer = RetryerBuilder.<BusinessBill>newBuilder()
            .retryIfResult(Objects::isNull)
            .retryIfException()

            .withWaitStrategy(WaitStrategies.fixedWait(10, TimeUnit.SECONDS))
            .withStopStrategy(StopStrategies.stopAfterAttempt(3))
            .withRetryListener(new MasterRetryListener<>())
            .build();


}
