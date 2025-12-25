package com.wishare.finance.infrastructure.support.schedule;

import cn.hutool.core.util.StrUtil;
import com.wishare.finance.infrastructure.remote.clients.xxljob.XxlJobClient;
import com.wishare.finance.infrastructure.remote.fo.xxljob.XxlJobGroup;
import com.wishare.finance.infrastructure.remote.fo.xxljob.XxlJobInfo;
import com.wishare.starter.exception.BizException;
import com.wishare.starter.exception.SysException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 定时任务管理这
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/4/5
 */
@Component
public class ScheduleManager {

    private static XxlJobClient xxlJobClient;

    public ScheduleManager(XxlJobClient xxlJobClient) {
        ScheduleManager.xxlJobClient = xxlJobClient;
    }





    /**
     * 注册调度器并启动任务
     *
     * @param scheduler 调度器
     * @param isUpdate  存在是否更新， true：存在则更新 ， false: 存在则抛异常
     */
    public static Scheduler registerAndStartTask(Scheduler scheduler, boolean isUpdate) {
        register(scheduler, isUpdate);
        for (ScheduleTask task : scheduler.getTasks()) {
            startTask(task.getId());
        }
        return scheduler;
    }



    /**
     * 注册调度器
     *
     * @param scheduler 调度器
     * @param isUpdate  存在是否更新， true：存在则更新 ， false: 存在则抛异常
     */
    public static Scheduler register(Scheduler scheduler, boolean isUpdate) {
        String appName = StrUtil.trim(scheduler.getService());
        String title = StrUtil.trim(scheduler.getName());

        //1.todo 注册调度器存在问题，导致执行器存在多个
        XxlJobGroup xxlJobGroup = xxlJobClient.getJobGroupByAppName(appName);
        if (Objects.isNull(xxlJobGroup)) {
            //如果调度器不存在则构建一个调度器
            xxlJobGroup = new XxlJobGroup();
            xxlJobGroup.setAppname((appName));
            xxlJobGroup.setTitle(title);
            xxlJobGroup.setAddressType(scheduler.getType());
            List<ScheduleMachine> machines = scheduler.getMachines();
            if (Objects.nonNull(machines) && !machines.isEmpty()) {
                xxlJobGroup.setAddressList(machines.stream().map(ScheduleMachine::getUrl).collect(Collectors.joining(",")));
            }
            Integer jobGroupId = xxlJobClient.saveJobGroup(xxlJobGroup);
            xxlJobGroup.setId(jobGroupId);
        }
        scheduler.setId(String.valueOf(xxlJobGroup.getId()));

        //2.注册任务
        List<ScheduleTask> tasks = scheduler.getTasks();
        if (tasks != null && !tasks.isEmpty()) {
            for (ScheduleTask task : tasks) {
                registerJob(String.valueOf(xxlJobGroup.getId()), task, isUpdate);
            }
        }
        return scheduler;
    }

    /**
     * 注册调度任务
     *
     * @param schedulerId  调度器id
     * @param scheduleTask 调度任务信息
     */
    public static ScheduleTask registerJob(String schedulerId, ScheduleTask scheduleTask) {
        return registerJob(schedulerId, scheduleTask, false);
    }

    /**
     * 更新调度任务
     *
     * @param schedulerId  调度器id
     * @param scheduleTask 调度任务信息
     */
    public static void updateJob(String schedulerId, ScheduleTask scheduleTask) {
        registerJob(schedulerId, scheduleTask, true);
    }

    /**
     * 注册调度任务并启动任务
     *
     * @param schedulerId  调度器id
     * @param scheduleTask 调度任务信息
     */
    public static void registerTaskAndStart(String schedulerId, ScheduleTask scheduleTask) {
        //1.注册任务
        registerJob(schedulerId, scheduleTask, false);
        //2.启动任务
        xxlJobClient.startJob(Integer.valueOf(scheduleTask.getId()));
    }

    /**
     * 启动任务
     *
     * @param taskId 调度任务id
     */
    public static void startTask(String taskId) {
        //启动任务
        xxlJobClient.startJob(Integer.valueOf(taskId));
    }

    /**
     * 停止调度任务
     *
     * @param taskId 调度任务id
     */
    public static void stopTask(String taskId) {
        //启动任务
        xxlJobClient.stopJob(Integer.valueOf(taskId));
    }

    /**
     * 删除任务
     * @param taskId 调度任务id
     */
    public static void removeTask(String taskId) {
        //启动任务
        xxlJobClient.removeJob(Integer.valueOf(taskId));
    }


    /**
     * 注册调度任务
     *
     * @param schedulerId  调度器id
     * @param scheduleTask 调度任务信息
     * @param isUpdate     存在是否更新， true：存在则更新 ， false: 存在则抛异常
     */
    public static ScheduleTask registerJob(String schedulerId, ScheduleTask scheduleTask, boolean isUpdate) {
        ScheduleTaskHandler handler = scheduleTask.getHandler();
        if (handler == null) {
            throw SysException.throw403("调度任务执行器不能为空");
        }
        //1.判断是否调度是否已存在
        XxlJobInfo xxlJobInfo = xxlJobClient.getJobByHandler(Integer.valueOf(schedulerId), handler.getCode(), scheduleTask.getName());
        if (Objects.nonNull(xxlJobInfo)) {
            if (!isUpdate) {
                throw BizException.throw403("执行器调度任务【" + handler.getCode() + "】已存在");
            }
            //更新调度任务
            xxlJobClient.updateJob(mapToJobInfo(scheduleTask, xxlJobInfo));
        } else {
            //新增调度任务
            xxlJobInfo = new XxlJobInfo();
            xxlJobInfo.setJobGroup(Integer.parseInt(schedulerId));
            String jobId = xxlJobClient.saveJob(mapToJobInfo(scheduleTask, xxlJobInfo));
            xxlJobInfo.setId(Integer.parseInt(jobId));
        }
        scheduleTask.setId(String.valueOf(xxlJobInfo.getId()));
        return scheduleTask;
    }

    /**
     * 任务转换
     *
     * @param scheduleTask 调度任务
     * @param xxlJobInfo   xxl-job调度任务
     * @return xxl-job调度任务
     */
    private static XxlJobInfo mapToJobInfo(ScheduleTask scheduleTask, XxlJobInfo xxlJobInfo) {
        xxlJobInfo.setScheduleConf(scheduleTask.getCron());
        xxlJobInfo.setExecutorHandler(scheduleTask.getHandler().getCode());
        xxlJobInfo.setAuthor(scheduleTask.getAuthor());
        xxlJobInfo.setJobDesc(scheduleTask.getName());
        xxlJobInfo.setExecutorParam(scheduleTask.getHandler().getParams());
        xxlJobInfo.setScheduleType("CRON");
        xxlJobInfo.setGlueType("BEAN");
        xxlJobInfo.setExecutorRouteStrategy("FIRST");
        xxlJobInfo.setMisfireStrategy("DO_NOTHING");
        xxlJobInfo.setExecutorBlockStrategy("SERIAL_EXECUTION");
        xxlJobInfo.setExecutorTimeout(0);
        xxlJobInfo.setExecutorFailRetryCount(0);
        xxlJobInfo.setGlueRemark("GLUE代码初始化");
        return xxlJobInfo;
    }

}
