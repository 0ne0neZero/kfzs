package com.wishare.contract.apps.service.event.handler;

import com.wishare.contract.apps.service.event.model.ApproveProcessCompleteMsg;

/**
 * @author hhb
 * @describe
 * @date 2025/10/29 11:33
 */
public interface PushBillExeHandler <T> {
    void handle(ApproveProcessCompleteMsg param);
}
