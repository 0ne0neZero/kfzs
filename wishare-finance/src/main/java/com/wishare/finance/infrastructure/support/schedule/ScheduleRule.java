package com.wishare.finance.infrastructure.support.schedule;

import com.wishare.starter.exception.BizException;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.Map;

/**
 * 定时规则
 *
 * @Author dxclay
 * @Date 2022/10/13
 * @Version 1.0
 */
@ApiModel("定时规则信息")
public class ScheduleRule {

    private static final String EVERY_DAY_CRON_TEMP = "{0} {1} {2} ? * *";
    private static final String EVERY_WEEK_CRON_TEMP = "{0} {1} {2} ? * {3}";
    private static final String EVERY_MONTH_CRON_TEMP = "{0} {1} {2} {3} * ?";
    private static final String EVERY_YEAR_CRON_TEMP = "{0} {1} {2} {3} {4} ? *";
    private static final String EVERY_QUARTER_CRON_TEMP = "{0} {1} {2} ? * *";

    @ApiModelProperty(value = "类型：每日（EVERY_DAY）每周（EVERY_WEEK）每月（EVERY_MONTH） 每季（EVERY_QUARTER）每年（EVERY_YEAR）", required = true)
    @NotNull(message = "定时规则类型不能为空")
    private String type;

    @ApiModelProperty(value = "执行时间 格式：HH:mm:ss ", required = true)
    @NotNull(message = "执行时间不能为空")
    private LocalTime moment;

    @ApiModelProperty(value = "指定月份")
    @Max(value = 12, message = "指定月份不能大于12")
    @Min(value = 1, message = "指定月份不能小于1")
    private int month;

    @ApiModelProperty(value = "季初：0, 季末：1")
    private int quarter;

    @ApiModelProperty(value = "指定日")
    @Max(value = 31, message = "指定日不能大于31")
    @Min(value = 1, message = "指定日不能小于1")
    private int day;

    @ApiModelProperty(value = "提前日")
    @Max(value = 28, message = "提前日不能大于28")
    @Min(value = 1, message = "提前日不能小于1")
    private int advanceDay;

    @ApiModelProperty(value = "指定星期")
    @Max(value = 7, message = "指定日不能大于31")
    @Min(value = 1, message = "指定日不能小于1")
    private int week;

    @ApiModelProperty(value = "样例时间 格式： yyyy-MM-dd HH:mm:ss")
    private String exampleTime;

    /**
     * 获取cron表达式
     *
     * @return
     */
    public String cron() {
        // 每天上午10:15触发
        switch (CronType.valueOf(type)) {
            case EVERY_DAY:
                return MessageFormat.format(EVERY_DAY_CRON_TEMP, moment.getSecond(),moment.getMinute(), moment.getHour());
            case EVERY_WEEK:
                return MessageFormat.format(EVERY_WEEK_CRON_TEMP,moment.getSecond(), moment.getMinute(), moment.getHour(), week);
            case EVERY_MONTH:
                if (week == 0){ //0则表示退后一天
                    return MessageFormat.format(EVERY_MONTH_CRON_TEMP, moment.getSecond(),moment.getMinute(), moment.getHour(), "L");
                }else{ //由于月底前几天无法使用cron表达式，故当做每天跑
                    return MessageFormat.format(EVERY_MONTH_CRON_TEMP, moment.getSecond(),moment.getMinute(), moment.getHour(), "?");
                }
            case EVERY_QUARTER:
                return MessageFormat.format(EVERY_QUARTER_CRON_TEMP, moment.getSecond(),moment.getMinute(), moment.getHour());
            case EVERY_YEAR:
                return MessageFormat.format(EVERY_YEAR_CRON_TEMP, month, day, moment.getMinute(), moment.getHour());
            default:
                throw BizException.throw400("执行时间格式不正确");
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

    public int getQuarter() {
        return quarter;
    }

    public void setQuarter(int quarter) {
        this.quarter = quarter;
    }

    public String getType() {
        return type;
    }

    public ScheduleRule setType(String type) {
        this.type = type;
        return this;
    }

    public int getAdvanceDay() {
        return advanceDay;
    }

    public ScheduleRule setAdvanceDay(int advanceDay) {
        this.advanceDay = advanceDay;
        return this;
    }

    public int getMonth() {
        return month;
    }

    public ScheduleRule setMonth(int month) {
        this.month = month;
        return this;
    }

    public int getDay() {
        return day;
    }

    public ScheduleRule setDay(int day) {
        this.day = day;
        return this;
    }

    public LocalTime getMoment() {
        return moment;
    }

    public ScheduleRule setMoment(LocalTime moment) {
        this.moment = moment;
        return this;
    }

    public int getWeek() {
        return week;
    }

    public ScheduleRule setWeek(int week) {
        this.week = week;
        return this;
    }

    public String getExampleTime() {
        return exampleTime;
    }

    public ScheduleRule setExampleTime(String exampleTime) {
        this.exampleTime = exampleTime;
        return this;
    }
}
