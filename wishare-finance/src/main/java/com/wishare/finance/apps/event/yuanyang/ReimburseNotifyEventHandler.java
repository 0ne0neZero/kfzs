package com.wishare.finance.apps.event.yuanyang;

import com.alibaba.fastjson.JSONObject;
import com.wishare.finance.domains.bill.event.ReimburseNotifyEvent;
import com.wishare.finance.infrastructure.event.DelayEventMessage;
import com.wishare.finance.infrastructure.event.EventLifecycle;
import com.wishare.finance.infrastructure.utils.WebUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

/**
 * 报销处理通知事件处理器
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/3/6
 */
@Slf4j
@Component
public class ReimburseNotifyEventHandler implements Consumer<ReimburseNotifyEvent> {

    public static List<Integer> minutes = new ArrayList<>(Arrays.asList(60, 300, 600, 3600));

    @Override
    public void accept(ReimburseNotifyEvent event) {
        try {
            log.error("报销处理通知事件-三方接口" + event.getNotifyUrl() + "参数：" + event.getNotifyData());
            String response = WebUtil.doPostWithClient(HttpClients.createDefault(), event.getNotifyUrl(), event.getNotifyData());
            log.error("报销处理通知事件-三方接口结果：" + response);
            if(!"success".equals(response) && !"SUCCESS".equals(response)){
                int count = event.getNotifyCount() + 1;
                event.setNotifyCount(count);
                if (count <= 10){
                    EventLifecycle.apply(new DelayEventMessage.DelayEventMessageBuilder<ReimburseNotifyEvent>()
                            .delay(minutes.get(count - 1))
                            .payload(event)
                            .headers("notifyType", "TRANSACTION")
                            .build());
                }
            }
        } catch (IOException e) {
            int count = event.getNotifyCount() + 1;
            event.setNotifyCount(count);
            if (count <= 10){
                EventLifecycle.apply(new DelayEventMessage.DelayEventMessageBuilder<ReimburseNotifyEvent>()
                        .delay(minutes.get(count - 1))
                        .payload(event)
                        .headers("notifyType", "TRANSACTION")
                        .build());
            }
            log.error("报销处理通知事件-三方接口异常： 通知参数 --> " + JSONObject.toJSONString(event), e);
        }

    }
}
