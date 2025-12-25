package com.wishare.finance.apps.event.payment;

import com.alibaba.fastjson.JSON;
import com.wishare.finance.apps.model.yuanyang.fo.ReimbursementCallbackF;
import com.wishare.finance.apps.service.yuanyang.ReimbursementAppService;
import com.wishare.finance.domains.bill.command.TransactionCallbackCommand;
import com.wishare.finance.domains.bill.service.BillPaymentDomainService;
import com.wishare.finance.infrastructure.remote.model.Notification;
import com.wishare.finance.infrastructure.remote.model.TransactionNotifyBody;
import com.wishare.starter.helpers.RedisHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

/**
 * 支付通知事件处理器
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/3/6
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class TransactionEventHandler implements Consumer<Message<Notification>> {

    private final ReimbursementAppService reimbursementAppService;

    @Override
    public void accept(Message<Notification> notificationMessage) {
        Notification notification = notificationMessage.getPayload();
        log.info("支付处理通知参数：" + JSON.toJSONString(notification));
        TransactionNotifyBody transactionNotifyBody = JSON.parseObject(notification.getResource().getCipherData(), TransactionNotifyBody.class);
        //获取缓存的报销缓存的信息
        log.info("支付获取key:{}", ReimbursementAppService.REIMBURSE_CACHE_PREFIX + transactionNotifyBody.getMchOrderNo());
        String reimburseData = RedisHelper.get(ReimbursementAppService.REIMBURSE_CACHE_PREFIX + transactionNotifyBody.getMchOrderNo());
        log.info("reimburseData:{}", reimburseData);
        if (StringUtils.isNotBlank(reimburseData)){
            reimbursementAppService.transactCallback(JSON.parseObject(reimburseData, ReimbursementCallbackF.class),
                    new TransactionCallbackCommand()
                    .setMchOrderNo(transactionNotifyBody.getMchOrderNo())
                    .setPayNo(transactionNotifyBody.getPayNo())
                    .setPayState(transactionNotifyBody.getState())
                    .setChanelTradeNo(transactionNotifyBody.getChannelOrderNo())
                    .setSuccessTime(transactionNotifyBody.getSuccessTime()));
        }
    }

}
