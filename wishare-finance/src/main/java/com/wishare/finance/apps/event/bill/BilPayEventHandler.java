package com.wishare.finance.apps.event.bill;

import com.wishare.finance.infrastructure.remote.model.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

/**
 * 账单交账批量变动事件处理器
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/1/12
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BilPayEventHandler implements Consumer<Notification> {


    @Override
    public void accept(Notification notification) {

    }

}
