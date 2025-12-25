package com.wishare.finance.infrastructure.remote.clients.xxljob;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @Author: zhangxiaopeng
 * @Date: 2022/6/7
 * @Description:
 */
@Getter
@Setter
@Accessors(chain = true)
public class XxlJobManageDto {

    /**
     * 任务id
     */
    private Integer taskId;

    /**
     * Cron表达式
     */
    private String cron;

    /**
     * 任务描述
     */
    private String jobDesc;

    /**
     * 任务参数
     */
    private String param;

    /**
     * 负责人
     */
    private String author;

    /**
     * JobHandler
     */
    private String jobHandler;

}
