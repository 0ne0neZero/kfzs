package com.wishare.finance.infrastructure.support.schedule;

import java.util.ArrayList;
import java.util.List;

/**
 * 调度器
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/4/5
 */
public class Scheduler {

    /**
     * 调度器id
     */
    private String id;

    /**
     * 服务名称
     */
    private String service;

    /**
     * 调度器名称
     */
    private String name;

    /**
     * 调度器类型 0:：自动注册， 1：手动注册
     */
    private int type;

    /**
     * 调度任务
     */
    private List<ScheduleTask> tasks = new ArrayList<>();

    /**
     * 机器地址
     */
    private List<ScheduleMachine> machines = new ArrayList<>();

    public Scheduler addTask(ScheduleTask task) {
        tasks.add(task);
        return this;
    }

    public String getId() {
        return id;
    }

    public Scheduler setId(String id) {
        this.id = id;
        return this;
    }

    public String getService() {
        return service;
    }

    public Scheduler setService(String service) {
        this.service = service;
        return this;
    }

    public String getName() {
        return name;
    }

    public Scheduler setName(String name) {
        this.name = name;
        return this;
    }

    public int getType() {
        return type;
    }

    public Scheduler setType(int type) {
        this.type = type;
        return this;
    }

    public List<ScheduleTask> getTasks() {
        return tasks;
    }

    public Scheduler setTasks(List<ScheduleTask> tasks) {
        this.tasks = tasks;
        return this;
    }

    public List<ScheduleMachine> getMachines() {
        return machines;
    }

    public Scheduler setMachines(List<ScheduleMachine> machines) {
        this.machines = machines;
        return this;
    }
}
