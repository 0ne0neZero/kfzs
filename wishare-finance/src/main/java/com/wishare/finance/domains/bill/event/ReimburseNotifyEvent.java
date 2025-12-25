package com.wishare.finance.domains.bill.event;

import com.wishare.finance.infrastructure.event.DefaultEvent;
import com.wishare.finance.infrastructure.event.DispatcherType;
import com.wishare.finance.infrastructure.event.Event;
import com.wishare.finance.infrastructure.event.StreamEventDispatcher;
import com.wishare.starter.beans.IdentityInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 报销完成事件
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/3/6
 */
@Getter
@Setter
@Accessors(chain = true)
@Event(dispatcher = StreamEventDispatcher.class, dispatcherType = DispatcherType.STREAM, name = "FINANCE_TRANSACTION_DELAY_NOTIFY_OUTPUT")
public class ReimburseNotifyEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 通知次数
     */
    private int notifyCount;

    /**
     * 通知地址
     */
    private String notifyUrl;

    /**
     * 通知数据
     */
    private String notifyData;

    /**
     * 身份信息
     */
    private IdentityInfo identityInfo;

}
