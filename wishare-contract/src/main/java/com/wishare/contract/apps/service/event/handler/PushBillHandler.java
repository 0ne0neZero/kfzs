package com.wishare.contract.apps.service.event.handler;

import com.wishare.contract.apps.service.event.model.ApproveProcessCompleteMsg;
import lombok.SneakyThrows;

/**
 * @author hhb
 * @describe
 * @date 2025/10/29 11:39
 */
public interface PushBillHandler<T> extends PushBillExeHandler<T> {
    boolean support(ApproveProcessCompleteMsg param);

    @SneakyThrows
    default void action(ApproveProcessCompleteMsg param){
        this.handle(param);
    }

    void approveAgree(String id, String procInstId);

    void approveRefuse(String id, String procInstId);
}
