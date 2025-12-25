package com.wishare.finance.infrastructure.support.schedule;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.finance.infrastructure.remote.clients.xxljob.OldXxlJobClient;
import com.wishare.finance.infrastructure.remote.clients.xxljob.XxlJobManageDto;
import com.wishare.starter.exception.BizException;
import com.wishare.starter.utils.ErrorAssertUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.support.CronExpression;
import org.springframework.stereotype.Component;

/**
 * 调度器客户端
 *   用于注册服务
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/4/5
 */
@Component
public class ScheduleClient {

    private static final String GET_COOKIE_URL = "/login";

    private static final String JOB_GROUP_PAGE_URL = "/jobgroup/pageList";
    private static final String JOB_GROUP_SAVE_URL = "/jobgroup/save";

    private static final String JOB_ADD_URL = "/jobinfo/add";

    private static final String JOB_UPDATE_URL = "/jobinfo/update";

    private static final String JOB_REMOVE_URL = "/jobinfo/remove";

    private static final String JOB_START_URL = "/jobinfo/start";

    private static final String JOB_STOP_URL = "/jobinfo/stop";

    private static final String JOB_INFO_URL = "/jobinfo/pageList";


    //public void register
    //
    //public String registerJob(){
    //
    //}
    //
    //public String addJob(XxlJobManageDto xxlJobManageDto) {
    //    if (xxlJobManageDto == null
    //            || StringUtils.isEmpty(xxlJobManageDto.getAuthor())
    //            || StringUtils.isEmpty(xxlJobManageDto.getJobDesc())
    //            || StringUtils.isEmpty(xxlJobManageDto.getJobHandler())
    //            || StringUtils.isEmpty(xxlJobManageDto.getCron())) {
    //        throw BizException.throw400(ErrorMessage.XXL_JOB_PARAMS_ERROR.msg());
    //    }
    //    ErrorAssertUtil.isTrueThrow300(CronExpression.isValidExpression(xxlJobManageDto.getCron()), ErrorMessage.XXL_JOB_CRON_RULE_ERROR);
    //
    //    //获取jobGroup
    //    String jobGroup = getJobGroup();
    //
    //    //添加调度任务
    //    OldXxlJobClient.JobRequestParam request = new OldXxlJobClient.JobRequestParam();
    //    request.setJobGroup(Integer.parseInt(jobGroup));
    //    request.setJobDesc(xxlJobManageDto.getJobDesc() != null ? xxlJobManageDto.getJobDesc() : "");
    //    request.setScheduleConf(xxlJobManageDto.getCron());
    //    request.setExecutorHandler(xxlJobManageDto.getJobHandler());
    //    request.setExecutorParam(xxlJobManageDto.getParam());
    //    request.setAuthor(xxlJobManageDto.getAuthor());
    //    return execute(JSONObject.parseObject(JSON.toJSONString(request)), JOB_ADD_URL);
    //
    //}


}
