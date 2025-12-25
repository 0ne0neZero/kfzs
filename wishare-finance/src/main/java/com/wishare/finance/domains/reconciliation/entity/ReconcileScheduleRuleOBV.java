package com.wishare.finance.domains.reconciliation.entity;

import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.starter.exception.BizException;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * 定时规则
 *
 * @Author dxclay
 * @Date 2022/10/13
 * @Version 1.0
 */
@Getter
@Setter
@ApiModel("定时规则信息")
public class ReconcileScheduleRuleOBV {

    private static final String EVERY_DAY_CRON_TEMP = "0 {0} {1} ? * *";
    private static final String EVERY_WEEK_CRON_TEMP = "0 {0} {1} ? * {2}";
    private static final String EVERY_MONTH_CRON_TEMP = "0 {0} {1} {2} * ?";

    @ApiModelProperty(value = "类型：每日（EVERY_DAY）每周（EVERY_WEEK）每月（EVERY_MONTH）", required = true)
    private String type;

    @ApiModelProperty(value = "执行时间 格式：HH:mm:ss ")
    private LocalTime moment;

    @ApiModelProperty(value = "特定时间（周：星期几、月：日）")
    private int specifiedTime;


    /**
     * 获取cron表达式
     *
     * @return
     */
    public String cron() {
        // 每天上午10:15触发
        switch (CronType.valueOf(type)) {
            case EVERY_DAY:
                return MessageFormat.format(EVERY_DAY_CRON_TEMP, moment.getMinute(), moment.getHour());
            case EVERY_WEEK:
                return MessageFormat.format(EVERY_WEEK_CRON_TEMP, moment.getMinute(), moment.getHour(), specifiedTime);
            case EVERY_MONTH:
                if (specifiedTime == 0){ //0则表示退后一天
                    return MessageFormat.format(EVERY_MONTH_CRON_TEMP, moment.getMinute(), moment.getHour(), "L");
                }else{ //由于月底前几天无法使用cron表达式，故当做每天跑
                    return MessageFormat.format(EVERY_MONTH_CRON_TEMP, moment.getMinute(), moment.getHour(), "?");
                }
            default:
                throw BizException.throw400(ErrorMessage.RECONCILE_RULE_TIME_NOT_EXIST.getErrMsg());
        }
    }

    /**
     * 判断是否可以允许
     * @return
     */
    public boolean checkRun(){
        if (CronType.valueOf(type) == CronType.EVERY_MONTH){
            //如果是月份前几天，则判断当前时间是否等于这一天
            LocalDate now = LocalDate.now();
            LocalDate nextMonthDate = now.plusMonths(1);
            LocalDate localDate = LocalDate.of(nextMonthDate.getYear(), nextMonthDate.getMonth(), 1).plusDays(-1);
            return localDate.compareTo(now) == 0;
        }
        return true;
    }

    public enum CronType {
        EVERY_DAY, EVERY_WEEK, EVERY_MONTH
    }
}
