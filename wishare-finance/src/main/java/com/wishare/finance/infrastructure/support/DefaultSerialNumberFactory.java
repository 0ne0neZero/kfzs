package com.wishare.finance.infrastructure.support;

import com.alibaba.fastjson.JSON;
import com.wishare.finance.infrastructure.remote.vo.org.OrgTenantRv;
import com.wishare.starter.helpers.RedisHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Calendar;

/**
 * 默认序列号生成器
 *
 * @Author dxclay
 * @Date 2022/9/9
 * @Version 1.0
 */
@Component
@AutoConfigureAfter(RedisHelper.class)
public class DefaultSerialNumberFactory implements SerialNumberFactory{

    private final String SERIAL_KEY_PREFIX = "Serial:";

    private static final DateTimeFormatter DATE_FORMATTER;
    private static final DateTimeFormatter DATE_TIME_FORMATTER;
    private static final DateTimeFormatter DATE_TIME_MS_FORMATTER;

    private static final Integer MIN_LENGTH = 5;

    static {
        DATE_FORMATTER = (new DateTimeFormatterBuilder()).parseCaseInsensitive().appendValue(ChronoField.YEAR, 4).appendValue(ChronoField.MONTH_OF_YEAR, 2).appendValue(ChronoField.DAY_OF_MONTH, 2).toFormatter();
        DATE_TIME_FORMATTER = (new DateTimeFormatterBuilder()).parseCaseInsensitive().appendValue(ChronoField.YEAR, 4).appendValue(ChronoField.MONTH_OF_YEAR, 2).appendValue(ChronoField.DAY_OF_MONTH, 2).appendValue(ChronoField.HOUR_OF_DAY, 2).appendValue(ChronoField.MINUTE_OF_HOUR, 2).appendValue(ChronoField.SECOND_OF_MINUTE, 2).toFormatter();
        DATE_TIME_MS_FORMATTER = (new DateTimeFormatterBuilder()).parseCaseInsensitive().appendValue(ChronoField.YEAR, 4).appendValue(ChronoField.MONTH_OF_YEAR, 2).appendValue(ChronoField.DAY_OF_MONTH, 2).appendValue(ChronoField.HOUR_OF_DAY, 2).appendValue(ChronoField.MINUTE_OF_HOUR, 2).appendValue(ChronoField.SECOND_OF_MINUTE, 2).appendValue(ChronoField.MILLI_OF_SECOND, 3).toFormatter();
    }

    @Override
    public String serialNumber() {
        StringBuilder number = new StringBuilder();
        String tenantId = ApiData.API.getTenantId().isPresent() ? ApiData.API.getTenantId().get() : "serialNumber";
        String key = SERIAL_KEY_PREFIX + tenantId;
        if (!RedisHelper.exists(key)) {
            RedisHelper.setAtExpire(key,getSecondsNextEarlyMorning(),"0");
        }
        long increment = RedisHelper.increment(key);
        int currentLength = String.valueOf(increment).length();
        if(currentLength < MIN_LENGTH){
            number.append("0".repeat(MIN_LENGTH - currentLength));
        }
        number.append(increment).insert(0, LocalDateTime.now().format(DATE_TIME_FORMATTER));

        //租户简称
        if(ApiData.API.getTenantId().isPresent()){
            //String tenantInfo = RedisHelper.getG(CacheConst.TENANT+apiData.getTenantId().get());
            String tenantInfo = "";
            OrgTenantRv orgTenantRv = JSON.parseObject(tenantInfo, OrgTenantRv.class);
            number.insert(0, StringUtils.isNotEmpty(orgTenantRv.getEnglishName()) ? orgTenantRv.getEnglishName() : "");
        }
        return number.toString();
    }

    /**
     * 获取当前时间距离第二天0点时的秒数
     *
     * @return Long
     */
    public Long getSecondsNextEarlyMorning() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return (cal.getTimeInMillis() - System.currentTimeMillis()) / 1000;
    }

}
