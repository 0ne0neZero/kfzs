package com.wishare.finance.apps.service.event.handler;

import com.wishare.finance.infrastructure.remote.model.ApproveProcessCompleteMsg;
import com.wishare.starter.exception.BizException;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * description
 * @author Yuting.Wang
 * @since 2025/1/8 14:10
 */
@Component
public class CallBackHandlerProxy {

    @Resource
    private List<PushBillHandler> pushBillHandlers;

    @SneakyThrows
    public void handle(ApproveProcessCompleteMsg param) {

        PushBillHandler pushBillHandler = pushBillHandlers.stream()
                .filter(handler -> handler.support(param))
                .findFirst()
                .orElseThrow(() -> new BizException(400, "未找到对应excel处理器"));

        pushBillHandler.action(param);
    }
}
