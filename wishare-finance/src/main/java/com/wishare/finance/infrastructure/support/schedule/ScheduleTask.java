package com.wishare.finance.infrastructure.support.schedule;

/**
 * 调度任务
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/4/5
 */
public class ScheduleTask {

    /**
     * 调度任务唯一标识
     */
    private String id;

    /**
     * 调度任务名称
     */
    private String name;

    /**
     * 调度Cron表达式
     */
    private String cron;

    /**
     * 描述
     */
    private String description;

    /**
     * 任务参数
     */
    private String param;

    /**
     * 负责人
     */
    private String author;

    /**
     * 执行器
     */
    private ScheduleTaskHandler handler;

    public String getId() {
        return id;
    }

    public ScheduleTask setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public ScheduleTask setName(String name) {
        this.name = name;
        return this;
    }

    public String getCron() {
        return cron;
    }

    public ScheduleTask setCron(String cron) {
        this.cron = cron;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ScheduleTask setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getParam() {
        return param;
    }

    public ScheduleTask setParam(String param) {
        this.param = param;
        return this;
    }

    public String getAuthor() {
        return author;
    }

    public ScheduleTask setAuthor(String author) {
        this.author = author;
        return this;
    }

    public ScheduleTaskHandler getHandler() {
        return handler;
    }

    public ScheduleTask setHandler(ScheduleTaskHandler handler) {
        this.handler = handler;
        return this;
    }
}
