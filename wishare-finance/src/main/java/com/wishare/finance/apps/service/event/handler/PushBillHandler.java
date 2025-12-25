package com.wishare.finance.apps.service.event.handler;

import com.wishare.finance.infrastructure.remote.model.ApproveProcessCompleteMsg;
import lombok.SneakyThrows;

import java.io.InputStream;

public interface PushBillHandler<T> extends PushBillExeHandler<T> {
    boolean support(ApproveProcessCompleteMsg param);

    @SneakyThrows
    default void action(ApproveProcessCompleteMsg param){
        this.handle(param);
    }

    void approveAgree(Long voucherBillId, String procInstId);

    void approveRefuse(Long voucherBillId, String procInstId);

    void approveAgree(String voucherBillId, String procInstId);
    void approveRefuse(String voucherBillId, String procInstId);
}
