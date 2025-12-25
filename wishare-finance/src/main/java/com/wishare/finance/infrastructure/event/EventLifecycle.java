package com.wishare.finance.infrastructure.event;

import com.wishare.starter.Global;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 事件生命周期管理器
 *
 * @Author dxclay
 * @Date 2022/11/3
 * @Version 1.0
 */
@Slf4j
public class EventLifecycle {

    /**
     * 发布一个事件，并且由事件的事务进行管理
     *   配合{@link EventTransactional} 使用
     * @param messageId
     * @param event      事件， 注：事件必须由{@link Event} 注解定义
     */
    public static void push(String messageId, Object event) {
        EventMessageHolder.getAndSet().push(convertMessage(messageId, event));
    }

    /**
     * 发布一个事件，并且由事件的事务进行管理
     *   配合{@link EventTransactional} 使用
     * @param eventMessage   事件消息
     */
    public static void push(EventMessage<?> eventMessage) {
        EventMessageHolder.getAndSet().push(eventMessage);
    }

    /**
     * 事件派发
     *    如果message id 之前已经保存了一个事件，则会先发布之前的事件，再派发一个新的事件
     * @param messageId
     */
    public static void apply(String messageId) {
        EventMessageStack eventMessageStack = EventMessageHolder.get();
        if (Objects.nonNull(eventMessageStack)){
            EventMessage<?> eventMessage = eventMessageStack.search(messageId);
            if (eventMessage != null) {
                apply(eventMessage);
            }
        }

    }

    /**
     * 派发当前线程下所有的事件
     * @param retry  是否重试
     */
    public static void applyLocalMessages(boolean retry) {
        EventMessageStack eventMessageStack = EventMessageHolder.get();
        if (Objects.isNull(eventMessageStack)){
            return;
        }
        ConcurrentHashMap<String, Vector<EventMessage<?>>> allGroup = eventMessageStack.getAllGroup();
        if (Objects.nonNull(allGroup) && !allGroup.isEmpty()){
            //这里暂时单个线程下的事件并不多，暂时不用开新的线程处理
            allGroup.values().forEach(messages ->{
                for (EventMessage<?> message : messages) {
                    try {
                        apply(message);
                    }catch (Exception e){
                        log.error("事件派发异常{}", e);
                        if (retry){
                            retryApply(message);
                        }
                    }
                }
            });
        }
    }

    /**
     * 清空当前线程下所有的事件
     */
    public static void clearLocalMessages() {
        EventMessageStack eventMessageStack = EventMessageHolder.get();
        if (Objects.nonNull(eventMessageStack)){
            eventMessageStack.clear();
        }
    }

    /**
     * 事件派发
     *
     * @param eventMessage
     */
    public static void apply(EventMessage<?> eventMessage) {
        if (eventMessage == null || eventMessage.getPayload() == null) {
            log.warn("event message or message payload is null.");
            return;
        }
        Class<?> eventClass = eventMessage.getPayload().getClass();
        //判断是否是一个事件
        if (!eventClass.isAnnotationPresent(Event.class)) {
            throw new IllegalArgumentException(eventClass.getName() + "is not a event. please add @Event in class type.");
        }
        Event eventAnnotation = eventClass.getAnnotation(Event.class);
        EventDispatcher dispatcher = Global.ac.getBean(eventAnnotation.dispatcher());
        if (dispatcher == null) {
            throw new IllegalArgumentException(eventAnnotation.dispatcher().getName() + " not injected into spring container.");
        }
        String name = eventAnnotation.name();
        if (name != null && !"".equals(name)) {
            eventMessage.setName(name);
        }
        String binderName = eventAnnotation.binderName();
        if (binderName != null && !"".equals(binderName)) {
            eventMessage.setBinderName(binderName);
        }
        try {
            dispatcher.apply(eventMessage);
        } catch (Exception e) {
            log.error("event dispatch error.", e);
        }
    }

    /**
     * 消息转换
     * @param messageId
     * @param event
     * @return
     */
    private static EventMessage<?> convertMessage(String messageId, Object event){
        return event instanceof EventMessage ? (EventMessage<?>) event :  EventMessage.builder(messageId).payload(event).build();
    }

    /**
     * 重试
     * @param message
     */
    private static void retryApply(EventMessage<?> message){
        //
    }

}
