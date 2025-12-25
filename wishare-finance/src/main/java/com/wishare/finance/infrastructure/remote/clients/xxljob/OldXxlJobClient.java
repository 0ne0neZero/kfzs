package com.wishare.finance.infrastructure.remote.clients.xxljob;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.starter.exception.BizException;
import com.wishare.starter.utils.ErrorAssertUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.support.CronExpression;
import org.springframework.stereotype.Component;

import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * xxl-job工具类
 *
 * @author yancao
 */
@Slf4j
@Component
public class OldXxlJobClient {

    private static final String GET_COOKIE_URL = "/login";

    private static final String GET_JOB_GROUP_URL = "/jobgroup/pageList";

    private static final String JOB_ADD_URL = "/jobinfo/add";

    private static final String JOB_UPDATE_URL = "/jobinfo/update";

    private static final String JOB_REMOVE_URL = "/jobinfo/remove";

    private static final String JOB_START_URL = "/jobinfo/start";

    private static final String JOB_STOP_URL = "/jobinfo/stop";

    private static final String JOB_INFO_URL = "/jobinfo/pageList";

    @Value("${xxl.job.admin.addresses:}")
    private String adminAddresses;

    @Value("${xxl.job.user.username:admin}")
    private String loginUserName;

    @Value("${xxl.job.user.password:admin123}")
    private String loginPwd;

    /**
     * 执行器名称
     */
    @Value("${spring.application.name:}")
    private String appName;

    /**
     * 有些接口的返回不一样，没有code
     * 例：比如/jobgroup/pageList，直接用的Map返回
     */
    private static final List<String> UNCHECK_RESULT_API = new ArrayList<>();

    static {
        UNCHECK_RESULT_API.add("/jobgroup/pageList");
        UNCHECK_RESULT_API.add("/jobinfo/pageList");
    }

    /**
     * 添加定时任务
     *
     * @param xxlJobManageDto 入参
     * @return String
     */
    public String addJob(XxlJobManageDto xxlJobManageDto) {
        if (xxlJobManageDto == null
                || StringUtils.isEmpty(xxlJobManageDto.getAuthor())
                || StringUtils.isEmpty(xxlJobManageDto.getJobDesc())
                || StringUtils.isEmpty(xxlJobManageDto.getJobHandler())
                || StringUtils.isEmpty(xxlJobManageDto.getCron())) {
            throw BizException.throw400(ErrorMessage.XXL_JOB_PARAMS_ERROR.msg());
        }
        ErrorAssertUtil.isTrueThrow300(CronExpression.isValidExpression(xxlJobManageDto.getCron()), ErrorMessage.XXL_JOB_CRON_RULE_ERROR);

        //获取jobGroup
        String jobGroup = getJobGroup();

        //添加调度任务
        JobRequestParam request = new JobRequestParam();
        request.setJobGroup(Integer.parseInt(jobGroup));
        request.setJobDesc(xxlJobManageDto.getJobDesc() != null ? xxlJobManageDto.getJobDesc() : "");
        request.setScheduleConf(xxlJobManageDto.getCron());
        request.setExecutorHandler(xxlJobManageDto.getJobHandler());
        request.setExecutorParam(xxlJobManageDto.getParam());
        request.setAuthor(xxlJobManageDto.getAuthor());
        return execute(JSONObject.parseObject(JSON.toJSONString(request)), JOB_ADD_URL);

    }

    /**
     * 更新定时任务
     *
     * @param xxlJobManageDto 入参
     */
    public void updateJob(XxlJobManageDto xxlJobManageDto) {
        if (xxlJobManageDto == null
                || StringUtils.isEmpty(String.valueOf(xxlJobManageDto.getTaskId()))
                || StringUtils.isEmpty(xxlJobManageDto.getJobDesc())
                || StringUtils.isEmpty(xxlJobManageDto.getAuthor())
                || StringUtils.isEmpty(xxlJobManageDto.getJobHandler())
                || StringUtils.isEmpty(xxlJobManageDto.getCron())) {
            throw new BizException(HttpStatus.BAD_REQUEST.value(), "xxl-job参数错误");
        }

        ErrorAssertUtil.isTrueThrow300(CronExpression.isValidExpression(xxlJobManageDto.getCron()), ErrorMessage.XXL_JOB_PARAMS_ERROR);
        //获取jobGroup
        String jobGroup = getJobGroup();

        JobRequestParam request = new JobRequestParam();
        request.setId(xxlJobManageDto.getTaskId());
        request.setJobGroup(Integer.parseInt(jobGroup));
        request.setScheduleConf(xxlJobManageDto.getCron());
        request.setExecutorHandler(xxlJobManageDto.getJobHandler());
        request.setAuthor(xxlJobManageDto.getAuthor());
        request.setExecutorParam(xxlJobManageDto.getParam());
        request.setJobDesc(xxlJobManageDto.getJobDesc());
        execute(JSONObject.parseObject(JSON.toJSONString(request)), JOB_UPDATE_URL);
    }


    /**
     * 删除定时任务
     *
     * @param taskId 任务id
     */
    public void removeJob(String taskId) {
        if (StringUtils.isEmpty(taskId)) {
            throw BizException.throw400(ErrorMessage.XXL_JOB_TASK_ID_ERROR.msg());
        }
        HashMap<String, Object> requestParams = new HashMap<>(2);
        requestParams.put("id", Integer.valueOf(taskId));
        execute(requestParams, JOB_REMOVE_URL);
    }

    /**
     * 启动定时任务
     *
     * @param taskId 任务id
     */
    public void startJob(String taskId) {
        if (StringUtils.isEmpty(taskId)) {
            throw BizException.throw400(ErrorMessage.XXL_JOB_TASK_ID_ERROR.msg());
        }
        HashMap<String, Object> requestParams = new HashMap<>(2);
        requestParams.put("id", Integer.valueOf(taskId));
        execute(requestParams, JOB_START_URL);
    }

    /**
     * 停止定时任务
     *
     * @param taskId 任务id
     */
    public void stopJob(String taskId) {
        if (StringUtils.isEmpty(taskId)) {
            throw BizException.throw400(ErrorMessage.XXL_JOB_TASK_ID_ERROR.msg());
        }
        HashMap<String, Object> requestParams = new HashMap<>(2);
        requestParams.put("id", Integer.valueOf(taskId));
        execute(requestParams, JOB_STOP_URL);
    }

    /**
     * 执行xxl-job请求，获取结果
     *
     * @param requestParams 请求参数
     * @param url           请求地址
     * @return String
     * @throws BizException BizException
     */
    public String execute(Map<String, Object> requestParams, String url) throws BizException {
        log.info("请求xxl-job api,url：{},req：{}", url, JSON.toJSON(requestParams));
        HttpResponse response = HttpRequest.post(adminAddresses + url)
                .form(requestParams)
                .cookie(getCookie())
                .execute();
        String resultBody = response.body();
        if (!response.isOk()) {
            log.error("xxl-job api[{}]执行失败,result：{}", url, resultBody);
            throw BizException.throw400("xxl-job响应错误");
        }
        log.info("xxl-job api响应,url：{},result：{}", url, resultBody);
        if (UNCHECK_RESULT_API.contains(url)) {
            return resultBody;
        }
        Map<String, Object> resultMap = JSON.parseObject(resultBody);
        Object content = resultMap.get("content");
        return content == null ? null : content.toString();
    }

    /**
     * 模拟登录，获取cookie
     *
     * @return String
     */
    private String getCookie() {
        HashMap<String, Object> requestParams = new HashMap<>();
        requestParams.put("userName", loginUserName);
        requestParams.put("password", loginPwd);

        log.debug("获取xxl cookie,req：{}", JSON.toJSONString(requestParams));
        HttpResponse response = HttpRequest.post(adminAddresses + GET_COOKIE_URL)
                .form(requestParams)
                .execute();
        if (!response.isOk()) {
            log.error("调用xxl获取cookie失败,response：{}", JSON.toJSONString(response.body()));
            throw BizException.throw400(ErrorMessage.XXL_JOB_RESPONSE_ERROR.msg());
        }

        List<HttpCookie> cookies = response.getCookies();
        log.debug("获取xxl cookie成功,cookies：{}", JSON.toJSONString(cookies));

        StringBuilder sb = new StringBuilder();
        cookies.forEach(cookie -> sb.append(cookie.toString()));
        return sb.toString();
    }

    /**
     * 获取jobGroup
     *
     * @return String
     */
    private String getJobGroup() {
        HashMap<String, Object> requestParams = new HashMap<>();
        requestParams.put("appname", "wishare-finance");
        String jobGroupResult = execute(requestParams, GET_JOB_GROUP_URL);
        String jobGroup;
        //获取jobGroup
        Map<String, Object> jobGroupResultMap = JSON.parseObject(jobGroupResult);
        if (Integer.parseInt(jobGroupResultMap.get("recordsTotal").toString()) == 0) {
            log.error("xxl-job api[{}]未找到执行器,result：{}", GET_JOB_GROUP_URL, jobGroupResult);
            throw BizException.throw400(ErrorMessage.XXL_JOB_EXECUTOR_ERROR.msg());
        } else {
            jobGroup = JSON.parseObject(((List<Map<String, Object>>) jobGroupResultMap.get("data")).get(0).get("id").toString(), String.class);
        }
        return jobGroup;
    }

    public String getJobPageList(Map<String, Object> requestParams) {
        requestParams.put("jobGroup", getJobGroup());
        requestParams.put("triggerStatus", -1);
        requestParams.put("start", 0);
        requestParams.put("length", 20);
        String jobGroupResult = execute(requestParams, JOB_INFO_URL);
        Map<String, Object> jobGroupResultMap = JSON.parseObject(jobGroupResult);
        System.out.println(JSON.toJSONString(jobGroupResultMap));
        if (Integer.parseInt(jobGroupResultMap.get("recordsTotal").toString()) == 0) {
            log.error("xxl-job api[{}]未找到执行器,result：{}", JOB_INFO_URL, jobGroupResult);
            throw BizException.throw400(ErrorMessage.XXL_JOB_EXECUTOR_ERROR.msg());
        }
        return jobGroupResultMap.get("data").toString();
    }

    @Data
    static class JobRequestParam {
        /**
         * 主键ID
         */
        private int id;

        /**
         * 执行器主键ID
         */
        private int jobGroup;

        /**
         * 任务执行CRON表达式
         */
        private String scheduleConf;
        /**
         * 任务描述
         */
        private String jobDesc;

        private Date addTime;

        private Date updateTime;

        /**
         * 负责人
         */
        private String author;

        /**
         * 报警邮件
         */
        private String alarmEmail;

        /**
         * 执行器，任务Handler名称
         */
        private String executorHandler;

        /**
         * 执行器，任务参数
         */
        private String executorParam;

        /**
         * 调度类型
         */
        private String scheduleType = "CRON";

        /**
         * 调度过期策略
         */
        private String misfireStrategy = "DO_NOTHING";

        /**
         * 执行器路由策略
         * FIRST：第一个
         * LAST：最后一个
         * ROUND：轮询
         * RANDOM：随机
         * CONSISTENT_ASH：一致性HASH
         * LEAST_FREQUENTLY_USED：最不经常使用
         * LEAST_RECENTLY_USED：最近最久未使用
         * FAILOVER：故障转移
         * BUSYOVER：忙碌转移
         * SHARDING_BROADCAST：分片广播
         */
        private String executorRouteStrategy = "FIRST";

        /**
         * 阻塞处理策略：
         * 单机串行：SERIAL_EXECUTION
         * 丢弃后续调度：DISCARD_LATER
         * 覆盖之前调度：COVER_EARLY
         */
        private String executorBlockStrategy = "SERIAL_EXECUTION";

        /**
         * 任务执行超时时间，单位秒
         */
        private int executorTimeout = 30;

        /**
         * 失败重试次数
         */
        private int executorFailRetryCount = 0;

        /**
         * GLUE类型
         */
        private String glueType = "BEAN";

        /**
         * GLUE源代码
         */
        private String glueSource;

        /**
         * GLUE备注
         */
        private String glueRemark;

        /**
         * GLUE更新时间
         */
        private Date glueUpdateTime;

        /**
         * 子任务ID，多个逗号分隔
         */
        private String childJobId;

        /**
         * 调度状态：0-停止，1-运行
         */
        private int triggerStatus = 1;

        /**
         * 上次调度时间
         */
        private long triggerLastTime;

        /**
         * 下次调度时间
         */
        private long triggerNextTime;
    }
}
