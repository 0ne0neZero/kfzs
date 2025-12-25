package com.wishare.contract.infrastructure.utils;

import com.google.common.collect.Lists;
import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 基于java8的日期工具类
 *
 * @Author dxclay
 * @Date 2022/11/28
 * @Version 1.0
 */
public class DateTimeUtil {

    public static final String MINUTE_PATTERN = "HH:mm";
    public static final String TIME_PATTERN = "HH:mm:ss";
    public static final String TIME_NOC_PATTERN = "HHmmss";
    public static final String MONTH_PATTERN = "yyyy-MM";
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final String CN_DATE_PATTERN = "yyyy年MM月dd日";
    public static final String CN_MONTH_PATTERN = "yyyy年MM月";

    public static final String DATE_SPA_PATTERN = "yyyy/MM/dd";
    public static final String MONTH_NOC_PATTERN = "yyyyMM";
    public static final String DATE_NOC_PATTERN = "yyyyMMdd";
    public static final String CN_MONTH = "yyyy年M月";

    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_TIME_SPA_PATTERN = "yyyy/MM/dd HH:mm:ss";
    public static final String DATE_TIME_NOC_PATTERN = "yyyyMMddHHmmss";
    public static final String DATE_TIME_MILLI_PATTERN = "yyyy-MM-dd HH:mm:ss SSS";
    public static final String DATE_TIME_MILLI_SPA_PATTERN = "yyyy/MM/dd HH:mm:ss SSS";
    public static final String DATE_TIME_MILLI_NOC_PATTERN = "yyyyMMddHHmmssSSS";

    public static final DateTimeFormatter MINUTE_FORMAT = DateTimeFormatter.ofPattern(MINUTE_PATTERN);
    public static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern(TIME_PATTERN);
    public static final DateTimeFormatter TIME_NOC_FORMAT = DateTimeFormatter.ofPattern(TIME_NOC_PATTERN);

    public static final DateTimeFormatter MONTH_FORMAT = DateTimeFormatter.ofPattern(DATE_PATTERN);
    public static final DateTimeFormatter CN_DATE_FORMAT = DateTimeFormatter.ofPattern(CN_DATE_PATTERN);
    public static final DateTimeFormatter CN_YEARMONTH_FORMAT = DateTimeFormatter.ofPattern(CN_MONTH_PATTERN);
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern(DATE_PATTERN);
    public static final DateTimeFormatter DATE_SPA_FORMAT = DateTimeFormatter.ofPattern(DATE_SPA_PATTERN);
    public static final DateTimeFormatter MONTH_NOC_FORMAT = DateTimeFormatter.ofPattern(DATE_NOC_PATTERN);
    public static final DateTimeFormatter DATE_NOC_FORMAT = DateTimeFormatter.ofPattern(DATE_NOC_PATTERN);
    public static final DateTimeFormatter CN_MONTH_FORMAT = DateTimeFormatter.ofPattern(CN_MONTH);

    public static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);
    public static final DateTimeFormatter DATE_TIME_SPA_FORMAT = DateTimeFormatter.ofPattern(DATE_TIME_SPA_PATTERN);
    public static final DateTimeFormatter DATE_TIME_NOC_FORMAT = DateTimeFormatter.ofPattern(DATE_TIME_NOC_PATTERN);

    public static final DateTimeFormatter DATE_TIME_MILLI_FORMAT = DateTimeFormatter.ofPattern(DATE_TIME_MILLI_PATTERN);
    public static final DateTimeFormatter DATE_TIME_MILLI_SPA_FORMAT = DateTimeFormatter.ofPattern(DATE_TIME_MILLI_SPA_PATTERN);
    public static final DateTimeFormatter DATE_TIME_MILLI_NOC_FORMAT = DateTimeFormatter.ofPattern(DATE_TIME_MILLI_NOC_PATTERN);
    public static final DateTimeFormatter DATE_TIME_REC3339_FORMAT = new DateTimeFormatterBuilder().parseCaseInsensitive()
            .append(DateTimeFormatter.ISO_LOCAL_DATE).appendLiteral('T')
            .append(DateTimeFormatter.ISO_LOCAL_TIME).appendLiteral("+08:00").toFormatter();

    /**
     * 获取默认格式日期 {@value TIME_PATTERN}
     *
     * @return
     */
    public static String nowTime() {
        return LocalDateTime.now().format(TIME_FORMAT);
    }

    /**
     * 获取默认格式分钟 {@value MINUTE_PATTERN}
     *
     * @return
     */
    public static String nowMinute() {
        return LocalDateTime.now().format(MINUTE_FORMAT);
    }

    /**
     * 获取默认格式分钟 {@value MINUTE_PATTERN}
     *
     * @return
     */
    public static LocalTime parseMinute(String minute) {
        return LocalTime.parse(minute, MINUTE_FORMAT);
    }

    /**
     * 获取默认格式分钟 {@value MINUTE_PATTERN}
     *
     * @return
     */
    public static String formatMinute(LocalTime minute) {
        return minute.format(MINUTE_FORMAT);
    }


    /**
     * 获取默认格式日期 {@value TIME_NOC_PATTERN}
     *
     * @return
     */
    public static String nowTimeNoc() {
        return LocalDateTime.now().format(TIME_NOC_FORMAT);
    }

    /**
     * 获取默认格式日期 {@value DATE_PATTERN}
     *
     * @return
     */
    public static String nowDate() {
        return LocalDateTime.now().format(DATE_FORMAT);
    }

    /**
     * 解析日期为字符串 默认格式日期 {@value DATE_PATTERN}
     *
     * @return
     */
    public static String formatDate(LocalDate date) {
        return Objects.isNull(date) ? null : date.format(DATE_FORMAT);
    }

    /**
     * 解析日期为字符串 默认格式日期 {@value MONTH_NOC_PATTERN}
     *
     * @return 月份字符串
     */
    public static String formatMonthNoc(LocalDate date) {
        return Objects.isNull(date) ? null : date.format(MONTH_NOC_FORMAT);
    }



    /**
     * 解析日期为月份字符串 默认格式日期 {@value MONTH_PATTERN}
     *
     * @return 月份
     */
    public static String formatMonth(LocalDate date) {
        return Objects.isNull(date) ? null : date.format(MONTH_FORMAT);
    }

    public static String formatCNDate(LocalDate date) {
        return Objects.isNull(date) ? null : date.format(CN_DATE_FORMAT);
    }

    public static String formatCNMouth(LocalDate date) {
        return Objects.isNull(date) ? null : date.format(CN_YEARMONTH_FORMAT);
    }

    public static String nowCNDate() {
        return LocalDate.now().format(CN_DATE_FORMAT);
    }

    /**
     * 解析月份字符串为日期 默认格式日期 {@value MONTH_PATTERN}
     * @param date 月份字符串
     * @return
     */
    public static LocalDate parseMonth(String date) {
        return Objects.isNull(date) ? null : LocalDate.parse(date, MONTH_FORMAT);
    }


    /**
     * 解析字符串日期为LocalDate 默认格式日期 {@value DATE_PATTERN}
     *
     * @return
     */
    public static LocalDate formatDate(String date) {
        return Objects.isNull(date) ? null : LocalDate.parse(date, DATE_FORMAT);
    }

    /**
     * 获取默认格式日期 {@value DATE_SPA_PATTERN}
     *
     * @return
     */
    public static String nowDateSpa() {
        return LocalDateTime.now().format(DATE_SPA_FORMAT);
    }

    /**
     * 获取默认格式日期 {@value DATE_NOC_PATTERN}
     *
     * @return
     */
    public static String nowDateNoc() {
        return LocalDateTime.now().format(DATE_NOC_FORMAT);
    }

    /**
     * 获取默认格式月份 {@value MONTH_NOC_PATTERN}
     *
     * @return 当前月份
     */
    public static String nowMonthNoc() {
        return LocalDateTime.now().format(DATE_NOC_FORMAT);
    }


    /**
     * 获取默认格式日期 {@value DATE_TIME_PATTERN}
     *
     * @return
     */
    public static String nowDateTime() {
        return LocalDateTime.now().format(DATE_TIME_FORMAT);
    }

    /**
     * 获取默认格式日期 {@value DATE_TIME_SPA_PATTERN}
     *
     * @return
     */
    public static String nowDateTimeSpa() {
        return LocalDateTime.now().format(DATE_TIME_SPA_FORMAT);
    }

    /**
     * 获取默认格式日期 {@value DATE_TIME_NOC_PATTERN}
     *
     * @return
     */
    public static String nowDateTimeNoc() {
        return LocalDateTime.now().format(DATE_TIME_NOC_FORMAT);
    }

    /**
     * 获取默认格式日期 {@value DATE_TIME_MILLI_PATTERN}
     *
     * @return
     */
    public static String nowMilli() {
        return LocalDateTime.now().format(DATE_TIME_MILLI_FORMAT);
    }

    /**
     * 获取默认格式日期 {@value DATE_TIME_MILLI_SPA_PATTERN}
     *
     * @return
     */
    public static String nowMilliSpa() {
        return LocalDateTime.now().format(DATE_TIME_MILLI_SPA_FORMAT);
    }

    /**
     * 获取默认格式日期 {@value DATE_TIME_MILLI_NOC_PATTERN}
     *
     * @return
     */
    public static String nowMilliNoc() {
        return LocalDateTime.now().format(DATE_TIME_MILLI_NOC_FORMAT);
    }

    /**
     * 日期转化为字符串 格式{@value DATE_TIME_PATTERN}
     *
     * @param dateTime
     * @return
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        return dateTime.format(DATE_TIME_FORMAT);
    }

    /**
     * 日期转化为字符串 格式{@value DATE_TIME_PATTERN}
     *
     * @param dateTime
     * @return
     */
    public static String formatSpaDateTime(LocalDateTime dateTime) {
        return dateTime.format(DATE_TIME_SPA_FORMAT);
    }

    /**
     * 日期转化为字符串 格式{@value DATE_TIME_PATTERN}
     *
     * @param dateTime
     * @return
     */
    public static String formatNocDateTime(LocalDateTime dateTime) {
        return dateTime.format(DATE_TIME_NOC_FORMAT);
    }

    /**
     * 日期转化为字符串 格式{@value DATE_TIME_MILLI_PATTERN}
     *
     * @param dateTime
     * @return
     */
    public static String formatMilliDateTime(LocalDateTime dateTime) {
        return dateTime.format(DATE_TIME_MILLI_FORMAT);
    }

    /**
     * 日期转化为字符串 格式{@value DATE_TIME_MILLI_SPA_PATTERN}
     *
     * @param dateTime
     * @return
     */
    public static String formatSpaMilliDateTime(LocalDateTime dateTime) {
        return dateTime.format(DATE_TIME_MILLI_SPA_FORMAT);
    }

    /**
     * 日期转化为字符串 格式{@value DATE_TIME_MILLI_NOC_PATTERN}
     *
     * @param dateTime
     * @return
     */
    public static String formatNocMilliDateTime(LocalDateTime dateTime) {
        return dateTime.format(DATE_TIME_MILLI_NOC_FORMAT);
    }

    /**
     * 日期转化为字符串 格式: yyyy-MM-ddTHH:mm:ss +8:00
     *
     * @param dateTime
     * @return
     */
    public static String formatRFC3339(LocalDateTime dateTime) {
        return Objects.isNull(dateTime) ? null : dateTime.format(DATE_TIME_REC3339_FORMAT);
    }

    /**
     * 日期转化为字符串 格式: yyyy-MM-ddTHH:mm:ss +8:00
     *
     * @param dateTime
     * @return
     */
    public static LocalDateTime parseRFC3339(String dateTime) {
        return Objects.isNull(dateTime) ? null : LocalDateTime.parse(dateTime, DATE_TIME_REC3339_FORMAT);
    }


    /**
     * 强制字符串转化为日期 格式{@value DATE_TIME_PATTERN}
     *
     * @param dateTime
     * @return
     */
    public static LocalDateTime forceParseDateTime(String dateTime) {
        return LocalDateTime.parse(dateTime);
    }

    /**
     * 字符串转化为日期 格式{@value DATE_TIME_PATTERN}
     *
     * @param dateTime
     * @return
     */
    public static LocalDateTime parseDateTime(String dateTime) {
        return LocalDateTime.parse(dateTime, DATE_TIME_FORMAT);
    }

    /**
     * 字符串转化为日期 格式{@value DATE_TIME_PATTERN}
     *
     * @param dateTime
     * @return
     */
    public static LocalDateTime parseSpaDateTime(String dateTime) {
        return LocalDateTime.parse(dateTime, DATE_TIME_SPA_FORMAT);
    }

    /**
     * 字符串转化为日期 格式{@value DATE_TIME_PATTERN}
     *
     * @param dateTime
     * @return
     */
    public static LocalDateTime parseNocDateTime(String dateTime) {
        return LocalDateTime.parse(dateTime, DATE_TIME_NOC_FORMAT);
    }

    /**
     * 字符串转化为日期 格式{@value DATE_TIME_MILLI_PATTERN}
     *
     * @param dateTime
     * @return
     */
    public static LocalDateTime parseMilliDateTime(String dateTime) {
        return LocalDateTime.parse(dateTime, DATE_TIME_MILLI_FORMAT);
    }

    /**
     * 字符串转化为日期 格式{@value DATE_TIME_MILLI_SPA_PATTERN}
     *
     * @param dateTime
     * @return
     */
    public static LocalDateTime parseSpaMilliDateTime(String dateTime) {
        return LocalDateTime.parse(dateTime, DATE_TIME_MILLI_SPA_FORMAT);
    }

    /**
     * 字符串转化为日期 格式{@value DATE_TIME_MILLI_NOC_PATTERN}
     *
     * @param dateTime
     * @return
     */
    public static LocalDateTime parseNocMilliDateTime(String dateTime) {
        return LocalDateTime.parse(dateTime, DATE_TIME_MILLI_NOC_FORMAT);
    }

    /**
     * 获取默认格式日期 {@value TIME_PATTERN}
     *
     * @return
     */
    public static String timestamp() {
        return String.valueOf(System.currentTimeMillis());
    }

    /**
     * 获取没有连接符的日期
     *
     * @return
     */
    public static String dateNocPattern(LocalDate localDate){
        return localDate.format(DATE_NOC_FORMAT);
    }


    /**
     * 比较时间再某个时间段是否超时
     *
     * @param beginTime 目标开始时间
     * @param timeout   超时时长(秒)
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return
     */
    public static int compareTimeout(LocalDateTime beginTime, long timeout, LocalTime startTime, LocalTime endTime) {
        //1.目标开始时间
        LocalDate targetDate = beginTime.toLocalDate();
        LocalTime targetTime = beginTime.toLocalTime();
        LocalDateTime targetStartTime;
        //当前时间
        LocalDateTime now = LocalDateTime.now();
        //目标时间为2022-12-22 08:30   计算时间： 09:00~18:00
        if (targetTime.compareTo(startTime) <= 0) {
            //目标开始时间在开始时间之前=开始时间  2022-12-22 09:00
            targetStartTime = LocalDateTime.of(targetDate, startTime);
        } else if (targetTime.compareTo(endTime) >= 0) {
            //目标开始时间在计算时间之后=下一天的开始时间 2022-12-23 09:00
            targetStartTime = LocalDateTime.of(targetDate.plusDays(1), endTime);
        } else {
            //目标开始时间在计算时间之间=目标开始时间 2022-12-22 09:10
            targetStartTime = beginTime;
        }
        //如果目标开始时间已经大于当前时间，返回-1  now=08:30  targetStartTime=09:00
        if (targetStartTime.compareTo(now) >= 0) {
            return -1;
        }

        //2.将时间分割成多个分段
        long durationSeconds = Duration.between(startTime, endTime).toSeconds();
        long spans = timeout / durationSeconds;
        //去区间段内最小值
        long minTimeout = spans > 0 ? durationSeconds : timeout;
        long recSeconds = 0;
        LocalDateTime comDateTime = targetStartTime;
        for (int i = 0; i <= spans; i++) {
            //目标开始时间加上最小值得到过期时间
            comDateTime = comDateTime.plusSeconds(minTimeout + recSeconds);
            LocalTime localTime = comDateTime.toLocalTime();
            //如果过期时间 > 结束时间，则过期时间等于结束时间  18:00
            if (localTime.isAfter(endTime)) {
                LocalDateTime nTime = LocalDateTime.of(targetStartTime.toLocalDate(), endTime);
                if (nTime.compareTo(now) <= 0) {
                    return -1;
                }
                //得到剩余的过期时间
                recSeconds = Duration.between(nTime, comDateTime).toSeconds();
                continue;//进行下一次循环
            }
            //如果目标时间大于等于过期时间返回-1
            int compare = comDateTime.compareTo(now);
            if (compare > 0) {
                return -1;
            } else if (compare < 0) {
                return 0;
            } else {
                return 0;
            }
        }
        return 0;
    }

    /**
     * 字符串转中文月份
     * @param date 日期
     * @return 中文月份
     */
    public static String formatCNMonth(String date){
       return parseMonth(date).format(CN_MONTH_FORMAT);
    }

    /**
     * 字符串转中文月份
     * @param date 日期
     * @return 中文月份
     */
    public static String formatCNMonth(LocalDate date){
        return date.format(CN_MONTH_FORMAT);
    }


    /**
     * 比较时间并获取最大时间
     *
     * @param dateTime1 时间1
     * @param dateTime2 时间2
     * @return 最大时间
     */
    public static LocalDateTime maxDateTime(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        if (dateTime1 == null) {
            return dateTime2;
        }
        if (dateTime2 == null) {
            return dateTime1;
        }
        return dateTime1.isAfter(dateTime2) ? dateTime1 : dateTime2;
    }

    /**
     * 比较时间并获取最小时间
     *
     * @param dateTime1 时间1
     * @param dateTime2 时间2
     * @return 最小时间
     */
    public static LocalDateTime minDateTime(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        if (dateTime1 == null) {
            return dateTime2;
        }
        if (dateTime2 == null) {
            return dateTime1;
        }
        return dateTime1.isAfter(dateTime2) ? dateTime2 : dateTime1;
    }

    /**
     * 获取两个日期之间的所有月份 (年月)
     *
     * @return
     */
    public static List<String> getMonthBetweenDate(LocalDate startDate, LocalDate endDate) {
        List<String> result = Lists.newArrayList();
        YearMonth from = YearMonth.from(startDate);
        YearMonth to = YearMonth.from(endDate);
        for (long i = 0; i <= ChronoUnit.MONTHS.between(from, to); i++) {
            result.add(from.plusMonths(i).toString());
        }
        return result;
    }

    /**
     * 获取两个月份之间的所有月份（年月）
     * @param startMonth
     * @param endMonth
     * @return
     */
    public static List<String> getMonthBetweenMonth(YearMonth startMonth, YearMonth endMonth) {
        if (startMonth.isAfter(endMonth)) {
            return new ArrayList<>();
        }
        List<String> months = new ArrayList<>();
        for (long i = 0; i <= ChronoUnit.MONTHS.between(startMonth, endMonth); i++) {
            months.add(startMonth.plusMonths(i).toString());
        }
        return months;
    }

    /**
     * 处理时间日期，字符串转localdate补齐一号
     *
     * @param dates "2023-01"
     * @return
     */
    public static List<LocalDate> handleMonthBetweenDateYM(List<String> dates) {
        List<LocalDate> localDateList = Lists.newArrayList();
        dates.forEach(date -> {
            LocalDate parse = LocalDate.parse(date + "-01");
            localDateList.add(parse);
        });
        return localDateList;
    }

    /**
     * 处理时间日期
     *
     * @param dates "2023-01-01 10:10:10"
     * @return
     */
    public static List<LocalDate> handleMonthBetweenDateYMD(List<String> dates) {
        List<LocalDate> localDateList = Lists.newArrayList();
        dates.forEach(date -> {
            LocalDateTime parse = LocalDateTime.parse(date, DateTimeFormatter.ofPattern(DATE_TIME_PATTERN));
            localDateList.add(parse.toLocalDate());
        });
        return localDateList;
    }

    /**
     * 返回连续时间的对象和不是连续时间的对象和统计的求和值
     *
     * @param dataParam        比较的数据
     * @param headTimeName     数据对象的开始时间name
     * @param headNextTimeName 数据对象的结束时间name
     * @param clazz            比较对象的class
     * @return
     */
    private static Map<String, ArrayDeque> groupByContinuouslyBillTimes(List<?> dataParam, String headTimeName, String headNextTimeName, Class<?> clazz) {
        List<Object> data = (List<Object>) dataParam;
        // 构建初始化对象   制造一个哑节点比较使用但最后不会入栈，不会影响最终结果。
        Object newInstance = null;
        try {
            newInstance = clazz.getDeclaredConstructor().newInstance();
            Field headTimeNameField = clazz.getDeclaredField(headTimeName);
            Field headNextTimeNameField = clazz.getDeclaredField(headNextTimeName);
            headTimeNameField.setAccessible(true);
            headNextTimeNameField.setAccessible(true);
            headTimeNameField.set(newInstance, LocalDateTime.parse("2099-01-01 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            data.add(newInstance);
            Collections.sort(data, new Comparator<Object>() {
                @SneakyThrows
                @Override
                public int compare(Object o1, Object o2) {
                    LocalDateTime billStartTime1 = (LocalDateTime) headTimeNameField.get(o1);
                    LocalDateTime billStartTime2 = (LocalDateTime) headTimeNameField.get(o2);
                    return billStartTime1.compareTo(billStartTime2);
                }
            });
            AtomicInteger sequence = new AtomicInteger();
            Map<String, ArrayDeque> continuousBillTimeGroup = new HashMap<>();
            ArrayDeque<Object> stack = new ArrayDeque<>();
            for (Object e : data) {
                if (stack.isEmpty()) {
                    stack.addLast(e);
                    continue;
                }
                Object peek = stack.peekLast();
                LocalDateTime billEndTime = (LocalDateTime) headNextTimeNameField.get(peek);
                LocalDateTime billStartTime = (LocalDateTime) headTimeNameField.get(e);
                if (daysBetween(billEndTime, billStartTime) == 1L) {
                    stack.addLast(e);
                } else {
                    continuousBillTimeGroup.put(String.valueOf(sequence.incrementAndGet()), stack);
                    stack = new ArrayDeque<>();
                    stack.push(e);
                }
            }
            return continuousBillTimeGroup;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 日期差几天
     */
    public static long daysBetween(LocalDateTime startTime, LocalDateTime endTime) {
        return ChronoUnit.DAYS.between(startTime.toLocalDate(), endTime.toLocalDate());
    }

    /**
     * 计算相差月份
     */
    public static long getBetweenMonth(LocalDate start, LocalDate end) {
        int months = (end.getYear() - start.getYear()) * 12 + (end.getMonthValue() - start.getMonthValue()) + 1;
        return Long.valueOf(months);
    }

    /**
     * 处理时间范围,起止时间添加时分秒
     * @param input
     * @return
     */
    public static Map<String, Object> processTimeRange(Map<String, Object> input) {
        Map<String, Object> result = new HashMap<>();

        // 定义日期格式
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter datetimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // 处理开始时间
        if (input.containsKey("start")) {
            LocalDate startDate = LocalDate.parse((CharSequence) input.get("start"), dateFormatter);
            // 设置为当天的开始时间（00:00:00）
            LocalDateTime startDateTime = startDate.atStartOfDay();
            result.put("start", startDateTime.format(datetimeFormatter));
        }

        // 处理结束时间
        if (input.containsKey("end")) {
            LocalDate endDate = LocalDate.parse((CharSequence) input.get("end"), dateFormatter);
            // 设置为当天的结束时间（23:59:59）
            LocalDateTime endDateTime = endDate.atTime(23, 59, 59);
            result.put("end", endDateTime.format(datetimeFormatter));
        }

        return result;
    }


    /**
     * 计算指定日期所在季度的总天数
     * @param date 指定日期
     * @return 季度的总天数
     */
    public static int getQuarterTotalDays(LocalDate date) {
        Month firstMonth = date.getMonth().firstMonthOfQuarter();
        int totalDays = 0;

        // 累加季度中三个月的天数
        for (int i = 0; i < 3; i++) {
            YearMonth yearMonth = YearMonth.of(date.getYear(), firstMonth.plus(i));
            totalDays += yearMonth.lengthOfMonth();
        }

        return totalDays;
    }

    /**
     * 计算到指定日期为止，季度已过的天数
     * @param date 指定日期
     * @return 已过天数
     */
    public static int getDaysElapsedInQuarter(LocalDate date) {
        Month firstMonth = date.getMonth().firstMonthOfQuarter();
        int daysElapsed = 0;

        // 累加前几个完整月的天数
        Month currentMonth = date.getMonth();
        for (Month month = firstMonth; month.getValue() < currentMonth.getValue(); month = month.plus(1)) {
            YearMonth yearMonth = YearMonth.of(date.getYear(), month);
            daysElapsed += yearMonth.lengthOfMonth();
        }

        // 加上当前月已过的天数
        daysElapsed += date.getDayOfMonth();
        return daysElapsed;
    }

    /**
     * 计算从指定日期开始，季度剩余的天数
     * @param date 指定日期
     * @return 剩余天数
     */
    public static int getDaysRemainingInQuarter(LocalDate date) {
        return getQuarterTotalDays(date) - getDaysElapsedInQuarter(date);
    }

    // 获取下个半年的第一天
    public static LocalDate getNextHalfYearStart(LocalDate date) {
        int month = date.getMonthValue();
        int year = date.getYear();

        if (month <= 6) {
            return LocalDate.of(year, 7, 1);
        } else {
            return LocalDate.of(year + 1, 1, 1);
        }
    }

    // 获取上个半年的最后一天
    public static LocalDate getPreviousHalfYearEnd(LocalDate date) {
        int month = date.getMonthValue();
        int year = date.getYear();

        if (month <= 6) {
            return LocalDate.of(year - 1, 12, 31);
        } else {
            return LocalDate.of(year, 6, 30);
        }
    }

    /**
     * 获取日期所在的半年 (1-2)
     * @param date 日期
     * @return 半年 (1表示1-6月，2表示7-12月)
     */
    public static int getHalfYear(LocalDate date) {
        int month = date.getMonthValue();
        return month <= 6 ? 1 : 2;
    }

    /**
     * 获取半年起始月份
     * @param halfYear 半年 (1-2)
     * @return 半年起始月份
     */
    public static int getHalfYearStartMonth(int halfYear) {
        return halfYear == 1 ? 1 : 7;
    }

    /**
     * 获取半年结束月份
     * @param halfYear 半年 (1-2)
     * @return 半年结束月份
     */
    public static int getHalfYearEndMonth(int halfYear) {
        return halfYear == 1 ? 6 : 12;
    }

    /**
     * 获取半年第一天
     * @param date 日期
     * @return 半年第一天
     */
    public static LocalDate getHalfYearStartDate(LocalDate date) {
        int year = date.getYear();
        int startMonth = getHalfYearStartMonth(getHalfYear(date));
        return LocalDate.of(year, startMonth, 1);
    }

    /**
     * 获取半年最后一天
     * @param date 日期
     * @return 半年最后一天
     */
    public static LocalDate getHalfYearEndDate(LocalDate date) {
        int year = date.getYear();
        int endMonth = getHalfYearEndMonth(getHalfYear(date));
        return YearMonth.of(year, endMonth).atEndOfMonth();
    }

    /**
     * 判断两个日期是否属于同一个完整的自然半年
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return true如果是同一个完整的自然半年，false如果不是
     */
    public static boolean isSameFullHalfYear(LocalDate startDate, LocalDate endDate) {
        // 获取两个日期所在的半年信息
        LocalDate startHalfYearStart = getHalfYearStartDate(startDate);
        LocalDate startHalfYearEnd = getHalfYearEndDate(startDate);

        LocalDate endHalfYearStart = getHalfYearStartDate(endDate);
        LocalDate endHalfYearEnd = getHalfYearEndDate(endDate);

        // 检查是否为同一个半年
        boolean sameHalfYear = startHalfYearStart.equals(endHalfYearStart) &&
                startHalfYearEnd.equals(endHalfYearEnd);

        return sameHalfYear;
    }


    /**
     * 计算日期所在自然半年内的总天数
     *
     * @param date 日期
     * @return 半年内的总天数
     */
    public static Integer getDaysInHalfYear(LocalDate date) {
        LocalDate halfYearEndDate = getHalfYearEndDate(date);
        return Integer.valueOf((int) (halfYearEndDate.toEpochDay() - getHalfYearStartDate(date).toEpochDay() + 1));
    }


    /**
     * 计算开始时间到该自然半年底的剩余天数
     * @param startDate 开始日期
     * @return 剩余天数
     */
    public static Integer getDaysToHalfYearEnd(LocalDate startDate) {
        LocalDate halfYearEnd = getHalfYearEndDate(startDate);
        return Integer.valueOf((int) (ChronoUnit.DAYS.between(startDate, halfYearEnd) + 1)); // +1 包含当天
    }

    /**
     * 计算开始时间距离该自然半年开始的天数
     * @param startDate 开始日期
     * @return 天数差（正数表示在半年开始之后，负数表示在半年开始之前，但逻辑上不会出现负数）
     */
    public static Integer getDaysSinceHalfYearStart(LocalDate startDate) {
        LocalDate halfYearStart = getHalfYearStartDate(startDate);
        return Integer.valueOf((int) (ChronoUnit.DAYS.between(halfYearStart, startDate)));
    }

    /**
     * 计算开始时间所属年的天数
     * @param startDate 开始日期
     * @return 该年的总天数
     */
    public static int getDaysInYear(LocalDate startDate) {
        return startDate.isLeapYear() ? 366 : 365;
    }

    /**
     * 计算当前时间距离年末剩余的天数
     * @param currentDate 当前日期
     * @return 剩余的天数
     */
    public static Integer getDaysRemainingInYear(LocalDate currentDate) {
        LocalDate endOfYear = LocalDate.of(currentDate.getYear(), 12, 31);
        return Integer.valueOf((int) (ChronoUnit.DAYS.between(currentDate, endOfYear)));
    }

    /**
     * 计算当前日期距离本年第一天有多少天
     * @param currentDate 当前日期
     * @return 距离本年第一天的天数
     */
    public static Integer getDaysFromYearStart(LocalDate currentDate) {
        LocalDate firstDayOfYear = LocalDate.of(currentDate.getYear(), 1, 1);
        return Integer.valueOf((int) (ChronoUnit.DAYS.between(firstDayOfYear, currentDate)));
    }

    public static LocalDate getFirstDayFromYearMonthString(String yearMonthStr) {
        // 直接解析为LocalDate，自动设置为该月第一天
        return LocalDate.parse(yearMonthStr + "-01", DateTimeFormatter.ISO_LOCAL_DATE);
    }
    public static LocalDate getLastDayFromYearMonthString(String yearMonthStr) {
        // 定义格式器
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");

        // 解析字符串为YearMonth对象
        YearMonth yearMonth = YearMonth.parse(yearMonthStr, formatter);

        // 获取该月的最后一天
        return yearMonth.atEndOfMonth();
    }

    public static List<String> getMonthsCrossYear(String startMonth, int monthsToAdd) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");

        try {
            YearMonth start = YearMonth.parse(startMonth, formatter);
            YearMonth end = start.plusMonths(monthsToAdd);

            // 计算两个YearMonth之间的所有月份
            long totalMonths = start.until(end, java.time.temporal.ChronoUnit.MONTHS) + 1;

            return IntStream.range(0, (int) totalMonths)
                    .mapToObj(i -> start.plusMonths(i).format(formatter))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("无效的日期格式: " + startMonth);
            return List.of();
        }
    }
}
