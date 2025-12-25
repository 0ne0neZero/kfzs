package com.wishare.finance.infrastructure.event;

import com.wishare.finance.infrastructure.identifier.IdentifierFactory;

import java.util.Objects;
import java.util.UUID;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 事件持有者
 *
 * @Author dxclay
 * @Date 2022/12/26
 * @Version 1.0
 */
public class EventMessageStack {

    /**
     * 默认组id
     */
    private String defaultGroupId;
    /**
     * 消息组
     */
    private ConcurrentHashMap<String, Vector<EventMessage<?>>> group = new ConcurrentHashMap<>();

    /**
     * 设置分组
     *
     * @param groupId
     */
    public void setGroup(String groupId) {
        if (group.containsKey(groupId)) {
            throw new IllegalArgumentException("消息分组[" + groupId + "] 已存在");
        }
        setGroup(groupId, new Vector<>());
    }

    /**
     * 创建默认组
     */
    public void createDefaultGroup() {
        setGroup(generateDefaultGroupId(), new Vector<>());
    }

    /**
     * 设置分组
     * @param groupId   分组信息
     * @param messages  消息列表
     */
    public void setGroup(String groupId, Vector<EventMessage<?>> messages) {
        if (group.containsKey(groupId)) {
            throw new IllegalArgumentException("消息分组[" + groupId + "] 已存在");
        }
        group.put(groupId, messages);
    }

    /**
     * 获取所有消息组
     * @return
     */
    public ConcurrentHashMap<String, Vector<EventMessage<?>>> getAllGroup() {
        return group;
    }

    /**
     * 根据分组id获取消息组信息
     * @param groupId
     * @return
     */
    public Vector<EventMessage<?>> getMessages(String groupId) {
        return group.get(groupId);
    }

    /**
     * 上传消息
     *
     * @param groupId
     * @param message
     */
    public void push(String groupId, EventMessage<?> message) {
        if (group.containsKey(groupId)) {
            group.get(groupId).add(message);
        }
    }

    /**
     * 上传消息
     *
     * @param message
     */
    public void push(EventMessage<?> message) {
        if (defaultGroupId == null) {
            defaultGroupId = IdentifierFactory.generateNSUUID();
            setGroup(defaultGroupId);
        }
        group.get(defaultGroupId).add(message);
    }

    /**
     * 出栈
     */
    public EventMessage<?> pop() {
        if (Objects.nonNull(defaultGroupId)) {
            Vector<EventMessage<?>> eventMessages = group.get(defaultGroupId);
            EventMessage<?> eventMessage = eventMessages.get(0);
            eventMessages.remove(eventMessage);
            return eventMessage;
        }
        return null;
    }

    /**
     * 获取栈顶
     * @return
     */
    public EventMessage<?> peek(){
        if (Objects.nonNull(defaultGroupId)) {
            Vector<EventMessage<?>> eventMessages = group.get(defaultGroupId);
            if (Objects.nonNull(eventMessages) && !eventMessages.isEmpty()){
                return eventMessages.get(0);
            }
        }
        return null;
    }

    /**
     * 根据消息id查询消息数据
     * @param messageId
     * @return
     */
    public EventMessage<?> search(String messageId){
        if (Objects.nonNull(defaultGroupId)) {
            Vector<EventMessage<?>> eventMessages = group.get(defaultGroupId);
            if (Objects.nonNull(eventMessages)){
                for (EventMessage<?> msg : eventMessages) {
                    if (messageId.equals(msg.messageKey)){
                        return msg;
                    }
                }
            }
        }
        return null;
    }

    /**
     * 清空所有的消息
     */
    public void clear(){
        defaultGroupId = null;
        group.clear();
    }

    /**
     * 生成默认id
     * @return
     */
    private String generateDefaultGroupId(){
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

}
