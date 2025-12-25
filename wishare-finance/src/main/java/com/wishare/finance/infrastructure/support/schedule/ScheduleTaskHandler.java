package com.wishare.finance.infrastructure.support.schedule;

/**
 * 调度执行器
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/4/5
 */
public class ScheduleTaskHandler {

    /**
     * 执行器名称
     */
    private String name;

    /**
     * 执行器唯一编码
     */
    private String code;

    /**
     * 执行器附带参数
     */
    private String params;

    public String getCode() {
        return code;
    }

    public ScheduleTaskHandler setCode(String code) {
        this.code = code;
        return this;
    }

    public String getName() {
        return name;
    }

    public ScheduleTaskHandler setName(String name) {
        this.name = name;
        return this;
    }

    public String getParams() {
        return params;
    }

    public ScheduleTaskHandler setParams(String params) {
        this.params = params;
        return this;
    }
}
