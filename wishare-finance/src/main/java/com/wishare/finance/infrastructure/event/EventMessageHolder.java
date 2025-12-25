package com.wishare.finance.infrastructure.event;

import java.util.Objects;

/**
 * 消息持有者
 *
 * @Author dxclay
 * @Date 2022/12/26
 * @Version 1.0
 */
public class EventMessageHolder {

    private static final InheritableThreadLocal<EventMessageStack> messageStack = new InheritableThreadLocal<>();

    /**
     * 设置消息栈
     * @param eventMessageStack
     */
    public static void set(EventMessageStack eventMessageStack){
        messageStack.set(eventMessageStack);
    }

    /**
     * 获取消息栈数据
     * @return
     */
    public static EventMessageStack get(){
       return messageStack.get();
    }

    /**
     * 获取消息栈数据，没有则创建一个消息栈
     * @return
     */
    public static EventMessageStack getAndSet(){
        EventMessageStack eventMessageStack = messageStack.get();
        if (Objects.isNull(eventMessageStack)){
            eventMessageStack = new EventMessageStack();
            set(eventMessageStack);
        }
        return eventMessageStack;
    }




}
