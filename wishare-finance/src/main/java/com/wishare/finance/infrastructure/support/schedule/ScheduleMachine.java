package com.wishare.finance.infrastructure.support.schedule;

/**
 * 调度机器
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/4/5
 */
public class ScheduleMachine {

    /**
     * 机器名称
     */
    private String name;

    /**
     * 机器地址
     */
    private String host;

    /**
     * 机器端口号
     */
    private String port;


    public String getUrl(){
        return "http://" + host + ":" + port + "/";
    }

    public String getName() {
        return name;
    }

    public ScheduleMachine setName(String name) {
        this.name = name;
        return this;
    }

    public String getHost() {
        return host;
    }

    public ScheduleMachine setHost(String host) {
        this.host = host;
        return this;
    }

    public String getPort() {
        return port;
    }

    public ScheduleMachine setPort(String port) {
        this.port = port;
        return this;
    }
}
