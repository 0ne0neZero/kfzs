package com.wishare.contract.apps.service.event.handler;

import com.wishare.contract.apps.service.event.model.ApproveProcessCompleteMsg;
import com.wishare.starter.exception.BizException;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author hhb
 * @describe
 * @date 2025/10/29 17:32
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
