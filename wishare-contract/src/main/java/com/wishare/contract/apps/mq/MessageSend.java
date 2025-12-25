package com.wishare.contract.apps.mq;

import com.wishare.contract.apps.remote.fo.message.CommandMsgD;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

/**
 * @author syy
 * @since 2022/7/1
 */
@MessagingGateway
public interface MessageSend {
//    /**
//     * 发送消息
//     */
//    @Gateway(requestChannel = "MSG_COMMAND_SEND_OUTPUT")
//    void sendMessageInfo(@Payload CommandMsgD<?> commandMsgD, @Header(AmqpHeaders.PUBLISH_CONFIRM_CORRELATION) MessageCorrelationData info);

    @Gateway(requestChannel = "MSG_COMMAND_SEND_OUTPUT")
    void sendMessage(@Payload CommandMsgD<?> commandMsgD);

    @Gateway(requestChannel = "CONTRACT_ALERT")
    void sendMessageContract(@Payload CommandMsgD<?> commandMsgD);
}

