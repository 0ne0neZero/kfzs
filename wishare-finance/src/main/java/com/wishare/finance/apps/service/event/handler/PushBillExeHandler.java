package com.wishare.finance.apps.service.event.handler;

import com.wishare.finance.infrastructure.beans.ExcelImportResult;
import com.wishare.finance.infrastructure.remote.model.ApproveProcessCompleteMsg;

public interface PushBillExeHandler <T> {
    void handle(ApproveProcessCompleteMsg param);
}
