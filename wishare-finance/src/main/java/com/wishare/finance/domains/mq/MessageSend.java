package com.wishare.finance.domains.mq;

import com.wishare.finance.infrastructure.remote.fo.msg.NoticeConsumerD;
import com.wishare.finance.infrastructure.remote.fo.msg.NoticeCustomerD;
import com.wishare.starter.msg.CommandMsgD;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;


@MessagingGateway
public interface MessageSend {

    String GATHER_BILL_DETAIL_URL = "/h5Open/#/propertyPay/invoiceDetail?id=%S&amount=%S&gatherPayType=gather";

    String INVOICE_DETAIL_URL = "/h5Open/#/propertyPay/invoiceDetail?id=";

    String MSG_CATEGORY_CODE = "FINZNCE_MESSAGE";

    String MSG_CATEGORY_CODE_NAME = "财务中台站内信";

    String ICON = "eb1e1ad8-85af-42d0-ba3c-21229be19009/application/CommodityInfoAddF/14217096688902.0fabeb18-b76a-4333-ad86-d6c700d75b43/1692590055165103.png";

    @Gateway(requestChannel = "MSG_COMMAND_SEND_OUTPUT")
    void sendMessageCommand(@Payload CommandMsgD<?> commandMsgD);

    @Gateway(requestChannel = "MSG_NOTICE_COMMAND_SEND_OUTPUT")
    void sendNoticeMessageCommand(@Payload NoticeConsumerD consumerD);

    /**
     * 发送C端站内信
     * @param noticeCustomerD
     */
    @Gateway(requestChannel = "NOTICE_CUSTOMER_WISHARE_MSG_OUTPUT")
    void sendMessageToCustomer(NoticeCustomerD noticeCustomerD);

}
