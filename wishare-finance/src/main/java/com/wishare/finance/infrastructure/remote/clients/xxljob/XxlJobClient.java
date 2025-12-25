package com.wishare.finance.infrastructure.remote.clients.xxljob;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.wishare.finance.infrastructure.configs.XxlJobConfig;
import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.finance.infrastructure.remote.fo.xxljob.XxlJobGroup;
import com.wishare.finance.infrastructure.remote.fo.xxljob.XxlJobInfo;
import com.wishare.finance.infrastructure.remote.vo.xxljob.XxlJobPage;
import com.wishare.starter.exception.BizException;
import com.xxl.job.core.biz.model.ReturnT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.net.HttpCookie;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * xxl-job 客户端
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/4/5
 */
@Component
public class XxlJobClient {

    private static final Logger log = LoggerFactory.getLogger(XxlJobClient.class);

    private static final String GET_COOKIE_URL = "/login";

    private static final String JOB_GROUP_PAGE_URL = "/jobgroup/pageList";
    private static final String JOB_GROUP_SAVE_URL = "/jobgroup/save";
    private static final String JOB_GROUP_UPDATE_URL = "/jobgroup/update";
    private static final String JOB_GROUP_REMOVE_URL = "/jobgroup/remove";

    private static final String JOB_ADD_URL = "/jobinfo/add";

    private static final String JOB_UPDATE_URL = "/jobinfo/update";

    private static final String JOB_REMOVE_URL = "/jobinfo/remove";

    private static final String JOB_START_URL = "/jobinfo/start";

    private static final String JOB_STOP_URL = "/jobinfo/stop";

    private static final String JOB_INFO_URL = "/jobinfo/pageList";

    private static final int timeout = 3;

    private final XxlJobConfig xxlJobConfig;

    private static String cookie;

    private static long cookieTimeStamp;

    public XxlJobClient(XxlJobConfig xxlJobConfig) {
        this.xxlJobConfig = xxlJobConfig;
    }

    /**
     * 新增执行器
     *
     * @param xxlJobGroup 执行器信息
     * @return 执行器id
     */
    public Integer saveJobGroup(XxlJobGroup xxlJobGroup) {
        Integer jobGroupId = post(JOB_GROUP_SAVE_URL, BeanUtil.beanToMap(xxlJobGroup), Integer.class);
        if (Objects.isNull(jobGroupId)){
            XxlJobGroup jobGroup = getJobGroupByAppName(xxlJobGroup.getAppname());
            if (jobGroup == null){
                throw new IllegalStateException("新增执行器异常，请稍后重试");
            }
            jobGroupId = jobGroup.getId();
        }
        return jobGroupId;
    }

    /**
     * 更新执行器
     *
     * @param xxlJobGroup 执行器信息
     * @return 结果默认 null
     */
    public String updateJobGroup(XxlJobGroup xxlJobGroup) {
        return post(JOB_GROUP_UPDATE_URL, BeanUtil.beanToMap(xxlJobGroup), String.class);
    }

    /**
     * 删除执行器
     *
     * @param xxlJobGroupId 执行器id
     * @return 结果默认 null
     */
    public String removeJobGroup(Integer xxlJobGroupId) {
        return post(JOB_GROUP_REMOVE_URL, Map.of("id", xxlJobGroupId), String.class);
    }


    /**
     * 根据应用id查询执行器
     *
     * @param appName 应用id
     * @return 执行器
     */
    public XxlJobGroup getJobGroupByAppName(String appName) {
        XxlJobPage<XxlJobGroup> jobGroupXxlJobPage = postPage(JOB_GROUP_PAGE_URL, Map.of("appname",
                StrUtil.trim(appName)), XxlJobGroup.class);
        List<XxlJobGroup> xxlJobGroups = jobGroupXxlJobPage.getData();
        if (xxlJobGroups != null && !xxlJobGroups.isEmpty()) {
            //TODO 因为xxl-job 原生接口不支持单个查询，默这里先默认第一个，后续会修改xxl-job源码提供更精确的接口
            return xxlJobGroups.get(0);
        }
        return null;
    }


    /**
     * 新增调度任务
     * @param xxlJobInfo 调度任务信息
     * @return 调度任务id
     */
    public String saveJob(XxlJobInfo xxlJobInfo) {
        return post(JOB_ADD_URL, BeanUtil.beanToMap(xxlJobInfo), String.class);
    }

    /**
     * 更新调度任务
     * @param xxlJobInfo 调度任务信息
     * @return 结果默认 null
     */
    public String updateJob(XxlJobInfo xxlJobInfo) {
        return post(JOB_UPDATE_URL, BeanUtil.beanToMap(xxlJobInfo), String.class);
    }

    /**
     * 启动调度任务
     * @param jobId  任务id
     */
    public void startJob(Integer jobId) {
        post(JOB_START_URL, Map.of("id", jobId), String.class);
    }

    /**
     * 停止调度任务
     *
     * @param jobId 任务id
     */
    public void stopJob(Integer jobId) {
        post(JOB_STOP_URL, Map.of("id", jobId), String.class);
    }

    /**
     * 删除调度任务
     *
     * @param jobId 任务id
     */
    public void removeJob(Integer jobId) {
        post(JOB_REMOVE_URL, Map.of("id", jobId), String.class);
    }


    /**
     * 根据调度任务处理器获取调度任务
     * @param jobGroupId 执行器id
     * @param handler 调度任务executorHandler
     * @param jobDesc 任务描述
     * @return 调度任务
     */
    public XxlJobInfo getJobByHandler(Integer jobGroupId, String handler, String jobDesc) {
        XxlJobPage<XxlJobInfo> jobGroupXxlJobPage = postPage(JOB_INFO_URL,
                Map.of("jobGroup", jobGroupId, "executorHandler", handler, "jobDesc", jobDesc,
                        "triggerStatus", -1, "length", 1, "start", 0), XxlJobInfo.class);
        List<XxlJobInfo> jobInfos = jobGroupXxlJobPage.getData();
        if (jobInfos != null && !jobInfos.isEmpty()) {
            //TODO 因为xxl-job 原生接口不支持单个查询，默这里先默认第一个，后续会修改xxl-job源码提供更精确的接口
            return jobInfos.get(0);
        }
        return null;
    }

    private <T> T post(String url, Map<String, Object> params, Class<T> clazz) {
        ReturnT<T> tReturnT = JSONObject.parseObject(doPost(xxlJobConfig.getAdminAddresses() + url, params), new TypeReference<>(clazz) {
        });
        if (tReturnT.getCode() == 200) {
            return tReturnT.getContent();
        }
        throw BizException.throw400("xxl-job 请求失败，" + tReturnT.getMsg());
    }

    private <T>  XxlJobPage<T> postPage(String url, Map<String, Object> params, Class<T> clazz) {
        XxlJobPage<T> xxlJobPage = JSONObject.parseObject(doPost(xxlJobConfig.getAdminAddresses() + url, params), new TypeReference<XxlJobPage<T>>(clazz) {
        });
        if (xxlJobPage.getData() != null ) {
            return xxlJobPage;
        }
        throw BizException.throw400("xxl-job 请求获取分页数据失败");
    }


    private <T> ReturnT<T> doPost(String url, Map<String, Object> params, Class<T> clazz) {
        return JSONObject.parseObject(doPost(xxlJobConfig.getAdminAddresses() + url, params), new TypeReference<ReturnT<T>>(clazz) {
        });
    }

    private String doPost(String url, Map<String, Object> params) {
        log.info("xxl-job请求地址：" + url + " | 参数：" + params);
        String body = HttpRequest.post(url)
                .form(params)
                .cookie(getCookie())
                .execute().body();
        log.info("xxl-job响应参数：" + body);
        return body;
    }

    private String getCookie() {
        if (cookie == null || Math.abs(cookieTimeStamp - System.currentTimeMillis()) > 60000) {
            cookie = refreshCookie();
            cookieTimeStamp = System.currentTimeMillis();
        }
        return cookie;
    }

    /**
     * 模拟登录，获取cookie
     *
     * @return String
     */
    private String refreshCookie() {
        HashMap<String, Object> requestParams = new HashMap<>();
        if (StrUtil.isBlank(xxlJobConfig.getLoginUserName()) || StrUtil.isBlank(xxlJobConfig.getLoginPwd())){
            throw BizException.throw400(ErrorMessage.XXL_JOB_PWD_ERROR.msg());
        }
        requestParams.put("userName", xxlJobConfig.getLoginUserName());
        requestParams.put("password", xxlJobConfig.getLoginPwd());

        String login = xxlJobConfig.getAdminAddresses() + GET_COOKIE_URL;
        log.info("获取xxl-cookie,url;{},req：{}", login,JSON.toJSONString(requestParams));
        HttpResponse response = HttpRequest.post(login)
                .form(requestParams)
                .execute();
        if (!response.isOk()) {
            log.error("调用xxl获取cookie失败,response：{}", JSON.toJSONString(response.body()));
            throw BizException.throw400(ErrorMessage.XXL_JOB_RESPONSE_ERROR.msg());
        }

        List<HttpCookie> cookies = response.getCookies();
        log.info("获取xxl cookie成功,cookies：{}", JSON.toJSONString(cookies));

        StringBuilder sb = new StringBuilder();
        cookies.forEach(cookie -> sb.append(cookie.toString()));
        return sb.toString();
    }

}
