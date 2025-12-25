package com.wishare.finance.apps.scheduler.voucher;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wishare.finance.apps.model.voucher.dto.VoucherRuleSchedulerDto;
import com.wishare.finance.apps.model.voucher.fo.RunVoucherRuleF;
import com.wishare.finance.apps.service.voucher.VoucherRuleAppService;
import com.wishare.finance.domains.voucher.support.fangyuan.entity.BillRule;
import com.wishare.finance.domains.voucher.support.fangyuan.repository.BillRuleRepository;
import com.wishare.finance.domains.voucher.support.fangyuan.service.BillRuleDomainService;
import com.wishare.finance.infrastructure.support.schedule.ScheduleRule;
import com.wishare.starter.beans.IdentityInfo;
import com.wishare.starter.exception.BizException;
import com.wishare.starter.utils.ThreadLocalUtil;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Map;

/**
 * 凭证定时任务调度器
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/4/8
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class VoucherScheduler {

    private final BillRuleRepository billRuleRepository;
    private final BillRuleDomainService billRuleDomainService;
    private final VoucherRuleAppService voucherRuleAppService;

    private final String string ="凭证规则定时调度任务启动VoucherScheduler";

    @XxlJob("voucherRuleScheduler")
    private void voucherRuleScheduler(){
        XxlJobHelper.log(string + "...");
        log.info(string);
        // 获取参数
        String p = XxlJobHelper.getJobParam();
        XxlJobHelper.log(string + "...[{}]", p);
        if (StrUtil.isBlank(p)){
            throw new BizException(400,"voucherRuleScheduler凭证规则定时调度任务任务参数为空，请检查");
        }
        VoucherRuleSchedulerDto param = JSONObject.parseObject(p, VoucherRuleSchedulerDto.class);
        //补充身份标识
        ThreadLocalUtil.set("IdentityInfo", getJobIdentityInfo(param.getTenantId()));


        RunVoucherRuleF f = new RunVoucherRuleF();
        f.setVoucherRuleId(param.getTaskId());

        XxlJobHelper.log(string + "ruleId:{}",param.getTaskId());
        log.info(string + "ruleId:{}",param.getTaskId());

        boolean executed = voucherRuleAppService.executeVoucherRule(f);

        XxlJobHelper.log(string + "结束ruleId:{}，执行结果:{}",param.getTaskId(), executed);
        log.info(string + "ruleId:{}，执行结果:{}", param.getTaskId(),executed);
    }

    /**补充身份标识
     * @param tenantId tenantId
     * @return
     */
    private IdentityInfo getJobIdentityInfo(String tenantId){
        IdentityInfo identityInfo = new IdentityInfo();
        identityInfo.setTenantId(tenantId);
        ThreadLocalUtil.set("IdentityInfo", identityInfo);
        return identityInfo;
    }

    @XxlJob("receiptRuleScheduler")
    private void receiptRuleScheduler() {
        BillRule billRule = param();
        //得到定时规则信息    定时规则类型：每日（EVERY_DAY）每周（EVERY_WEEK）每月（EVERY_MONTH）每季度（EVERY_QUARTER) 每年（EVERY_YEAR）")
        ScheduleRule rule = billRule.getScheduleRule().getRule();
        log.info("定时规则信息: {}" + rule.toString());
        //当前时间
        Calendar cale = Calendar.getInstance();
        int month = cale.get(Calendar.MONTH) + 1;
        int days = cale.get(Calendar.DATE);
        //对定时规则为每季度的处理
        if (rule.getType().equals("EVERY_QUARTER")) {
            //指定月份
            int moment = rule.getMonth();
            //判断是季度初还是季度末 0:季度初， 1:季度末
            if (rule.getQuarter() == 0) {
                if (((month + 2) - moment) % 3 == 0 && rule.getAdvanceDay() + 1 == days) {
                    log.info("推单规则定时调度任务启动...");
                    billRuleDomainService.taskExecute(billRule.getId());
                }
            } else {
                if ((month - moment) % 3 == 0) {
                    Integer integer = checkDate();
                    if (integer - rule.getAdvanceDay() == days) {
                        log.info("推单规则定时调度任务启动...");
                        billRuleDomainService.taskExecute(billRule.getId());
                    }
                }
            }
        } else {
            // 每天执行
            log.info("推单规则定时调度任务启动...");
            billRuleDomainService.taskExecute(billRule.getId());
        }
    }

    @XxlJob("billRuleRemindSend")
    private Boolean billRuleRemindSend() {
        log.info("开始发送报账汇总规则-提醒消息");
        IdentityInfo identityInfo = new IdentityInfo();
        identityInfo.setTenantId("13554968497211");
        ThreadLocalUtil.set("IdentityInfo", identityInfo);
        billRuleDomainService.send();
        log.info("发送报账汇总规则-提醒消息发送完毕");
        return Boolean.TRUE;
    }

    public BillRule param(){
        //获取XxlJob的参数
        String jobParam = XxlJobHelper.getJobParam();
        log.info("推单规则参数:  {}" + jobParam);
        Map map = JSON.parseObject(jobParam, Map.class);
        long taskId = (long) map.get("taskId");
        //查出当前规则的信息
        BillRule billRule = billRuleRepository.getBaseMapper().selectById(taskId);
        return billRule;
    }

    /**
     * 获取当前月最后一天
     * @return
     */
    public Integer checkDate() {
        Calendar cale = Calendar.getInstance();
        cale.add(Calendar.MONTH, 1);
        cale.set(Calendar.DAY_OF_MONTH, 0);
        return cale.get(Calendar.DATE);
    }

}
