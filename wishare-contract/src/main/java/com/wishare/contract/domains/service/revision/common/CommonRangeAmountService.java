package com.wishare.contract.domains.service.revision.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.nacos.shaded.com.google.common.collect.Lists;
import com.wishare.contract.domains.bo.*;
import com.wishare.contract.domains.enums.SplitModeEnum;
import com.wishare.contract.infrastructure.utils.DateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

import static java.lang.System.*;

/**
 * @author hhb
 * @describe
 * @date 2025/6/9 17:12
 */
@Service
@Slf4j
public class CommonRangeAmountService {

    //根据时间及结算周期类型计算金额数据
    public CommonRangeAmountBO getRangeAmount(Integer splitModeEnum,LocalDate startDate, LocalDate endDate, BigDecimal amount) {
        switch (splitModeEnum) {
            case 1:
                return getMonthRangeAmount(startDate, endDate, amount);
            case 2:
                //return getQuarterRangeAmount(startDate, endDate, amount);
                return getQuarterRangeContinuousAmount(startDate, endDate, amount, SplitModeEnum.THREE_MONTH.getInterval());
            case 3:
                //return getHalfYearRangeAmount(startDate, endDate, amount);
                return getQuarterRangeContinuousAmount(startDate, endDate, amount, SplitModeEnum.HALF_YEAR.getInterval());
            case 4:
                //return getYearRangeAmount(startDate, endDate, amount);
                return getQuarterRangeContinuousAmount(startDate, endDate, amount, SplitModeEnum.YEAR.getInterval());
            default:
                return new CommonRangeAmountBO();
        }
    }


    //按月拆分数据，获取其对应金额
    private static CommonRangeAmountBO getMonthRangeAmount(LocalDate startDate, LocalDate endDate, BigDecimal amount) {

        //判断开始日期是否为当月第一天
        Boolean isFirstDayOfMonth = startDate.getDayOfMonth() == 1;
        //判断结束日期是否为当月最后一天
        Boolean isEndDayOfMonth = endDate.equals(endDate.with(TemporalAdjusters.lastDayOfMonth()));
        //判断起止时间是否为同一个月
        Boolean isSameMonth = startDate.getYear() == endDate.getYear() && startDate.getMonth() == endDate.getMonth();

        List<YearMonth> fullMonths = new ArrayList<>();
        LocalDate adjustedStart = startDate;
        LocalDate adjustedEnd = endDate;
        // 如果开始日期不是当月第一天，则从下个月开始计算
        if (!isFirstDayOfMonth) {
            adjustedStart = startDate.plusMonths(1).withDayOfMonth(1);
        }

        // 如果结束日期不是当月最后一天，则计算到上个月
        if (!isEndDayOfMonth) {
            adjustedEnd = endDate.withDayOfMonth(1).minusDays(1);
        }

        // 计算满自然月
        if (!adjustedStart.isAfter(adjustedEnd)) {
            YearMonth current = YearMonth.from(adjustedStart);
            YearMonth endMonth = YearMonth.from(adjustedEnd);
            while (!current.isAfter(endMonth)) {
                fullMonths.add(current);
                current = current.plusMonths(1);
            }
        }
        //满自然月数
        int fullMonthsCount = fullMonths.size();
        //计算开始时间本月天数
        int startMonthDays = startDate.lengthOfMonth();
        //计算开始时间到月底的剩余天数
        LocalDate lastDayOfMonth = startDate.with(TemporalAdjusters.lastDayOfMonth());
        int daysStartRemaining = lastDayOfMonth.getDayOfMonth() - startDate.getDayOfMonth()+1;

        //计算结束时间本月天数
        int endMonthDays = endDate.lengthOfMonth();
        //计算结束时间到月初天数
        LocalDate firstDayOfMonth = endDate.with(TemporalAdjusters.firstDayOfMonth());
        int daysEndRemaining = endDate.getDayOfMonth() - firstDayOfMonth.getDayOfMonth()+1;

        return getCommonRangeAmountBO(amount, fullMonthsCount, isSameMonth, daysStartRemaining, startMonthDays, daysEndRemaining, endMonthDays, isFirstDayOfMonth, isEndDayOfMonth);
    }

    //计算各类数据时间
    private static CommonRangeAmountBO getCommonRangeAmountBO(BigDecimal amount,
                                                              int fullMonthsCount,
                                                              Boolean isSameMonth,
                                                              int daysStartRemaining,
                                                              int startMonthDays,
                                                              int daysEndRemaining,
                                                              int endMonthDays,
                                                              Boolean isFirstDayOfMonth,
                                                              Boolean isEndDayOfMonth) {
        CommonRangeAmountBO commonRangeAmountBO = new CommonRangeAmountBO();

        //时间范围不足自然月,且为同一个月
        if(fullMonthsCount == 0 && isSameMonth){
            commonRangeAmountBO.setStartMonthAmount(amount);
            commonRangeAmountBO.setAvgMonthAmount(amount);
            commonRangeAmountBO.setEndMonthAmount(amount);
            return commonRangeAmountBO;
        }
        //时间范围不足自然月,且为不同一个月
        else if(fullMonthsCount == 0){
            // 计算月金额 = 合同金额/(起止月剩余天数/起止月当月天数+结束月开始天数/结束月当月天数)
            BigDecimal monthAmount = amount.divide(
                            (BigDecimal.valueOf(daysStartRemaining).divide(BigDecimal.valueOf(startMonthDays), 10, RoundingMode.HALF_UP))
                            .add(BigDecimal.valueOf(daysEndRemaining).divide(BigDecimal.valueOf(endMonthDays), 10, RoundingMode.HALF_UP)
            ), 0, RoundingMode.DOWN);
            BigDecimal csNum = BigDecimal.valueOf(daysStartRemaining).divide(BigDecimal.valueOf(startMonthDays), 10, RoundingMode.HALF_UP);
            BigDecimal firstAmount = monthAmount.multiply(csNum).setScale( 0, RoundingMode.DOWN);
            commonRangeAmountBO.setStartMonthAmount(firstAmount);
            commonRangeAmountBO.setEndMonthAmount(amount.subtract(firstAmount));
            return commonRangeAmountBO;
        }
        //时间范围为满自然月
        else if(isFirstDayOfMonth && isEndDayOfMonth){
            if(fullMonthsCount == 1){
                commonRangeAmountBO.setStartMonthAmount(amount);
                commonRangeAmountBO.setAvgMonthAmount(amount);
                commonRangeAmountBO.setEndMonthAmount(amount);
                return commonRangeAmountBO;
            }
            BigDecimal avgAmount = amount.divide(BigDecimal.valueOf(fullMonthsCount), 0, RoundingMode.DOWN);
            commonRangeAmountBO.setStartMonthAmount(avgAmount);
            commonRangeAmountBO.setAvgMonthAmount(avgAmount);
            commonRangeAmountBO.setEndMonthAmount(amount.subtract(avgAmount.multiply(BigDecimal.valueOf(fullMonthsCount -1))));
            return commonRangeAmountBO;
        }
        //时间范围含满自然月，且开始时间不为本月第一天，且结束时间不为本月最后一天
        else if(!isFirstDayOfMonth && !isEndDayOfMonth){
            // 计算月金额 = 合同金额/(起止月剩余天数/起止月当月天数+结束月开始天数/结束月当月天数)
            BigDecimal monthAmount = amount.divide(
                            BigDecimal.valueOf(fullMonthsCount)
                            .add((BigDecimal.valueOf(daysStartRemaining).divide(BigDecimal.valueOf(startMonthDays), 10, RoundingMode.HALF_UP))
                            .add(BigDecimal.valueOf(daysEndRemaining).divide(BigDecimal.valueOf(endMonthDays), 10, RoundingMode.HALF_UP))
            ), 0, RoundingMode.DOWN);
            BigDecimal csNum = BigDecimal.valueOf(daysStartRemaining).divide(BigDecimal.valueOf(startMonthDays), 10, RoundingMode.HALF_UP);
            commonRangeAmountBO.setStartMonthAmount(monthAmount.multiply(csNum).setScale(0, RoundingMode.DOWN));
            commonRangeAmountBO.setAvgMonthAmount(monthAmount);
            commonRangeAmountBO.setEndMonthAmount(amount.subtract(commonRangeAmountBO.getStartMonthAmount()).subtract(monthAmount.multiply(BigDecimal.valueOf(fullMonthsCount))));
            return commonRangeAmountBO;
        }
        //时间范围含满自然月，且开始时间不为本月第一天
        else if(!isFirstDayOfMonth){
            // 计算月金额 = 合同金额/(起止月剩余天数/起止月当月天数+结束月开始天数/结束月当月天数)
            BigDecimal monthAmount = amount.divide(
                    BigDecimal.valueOf(fullMonthsCount)
                            .add(BigDecimal.valueOf(daysStartRemaining).divide(BigDecimal.valueOf(startMonthDays), 10, RoundingMode.HALF_UP))
                            , 0, RoundingMode.DOWN);
            commonRangeAmountBO.setStartMonthAmount(amount.subtract(monthAmount.multiply(BigDecimal.valueOf(fullMonthsCount))));
            commonRangeAmountBO.setAvgMonthAmount(monthAmount);
            commonRangeAmountBO.setEndMonthAmount(monthAmount);
            return commonRangeAmountBO;
        }
        //时间范围含满自然月，且结束时间不为本月最后一天
        else{
            BigDecimal monthAmount = amount.divide(
                    BigDecimal.valueOf(fullMonthsCount)
                                    .add(BigDecimal.valueOf(daysEndRemaining).divide(BigDecimal.valueOf(endMonthDays), 10, RoundingMode.HALF_UP))
                            , 0, RoundingMode.DOWN);
            commonRangeAmountBO.setStartMonthAmount(monthAmount);
            commonRangeAmountBO.setAvgMonthAmount(monthAmount);
            commonRangeAmountBO.setEndMonthAmount(amount.subtract(monthAmount.multiply(BigDecimal.valueOf(fullMonthsCount))));
            return commonRangeAmountBO;
        }
    }

    //按季度拆分数据，获取其对应金额
    private static CommonRangeAmountBO getQuarterRangeAmount(LocalDate startDate, LocalDate endDate, BigDecimal amount) {
        //判断开始日期是否为当季第一天
        Boolean isFirstDayOfQuarter = startDate.with(startDate.getMonth().firstMonthOfQuarter()).withDayOfMonth(1).equals(startDate);
        //判断结束日期是否为当季最后一天
        Boolean isEndDayOfQuarter =  endDate.with(endDate.getMonth().firstMonthOfQuarter()).plusMonths(2).with(TemporalAdjusters.lastDayOfMonth()).equals(endDate);
        int quarter1 = (startDate.getMonthValue() - 1) / 3 + 1;
        int quarter2 = (endDate.getMonthValue() - 1) / 3 + 1;
        //判断起止时间是否为同一个季度
        Boolean isSameQuarter = startDate.getYear() == endDate.getYear() && quarter1 == quarter2;

        // 计算满自然季度
        List<YearQuarter> fullQuarters = new ArrayList<>();
        LocalDate adjustedStart = startDate;
        LocalDate adjustedEnd = endDate;

        // 如果开始日期不是季度第一天，则从下个季度开始计算
        if (!isFirstDayOfQuarter) {
            adjustedStart =  startDate.with(startDate.getMonth().firstMonthOfQuarter())
                    .withDayOfMonth(1).plusMonths(3);
        }

        // 如果结束日期不是季度最后一天，则计算到上个季度
        if (!isEndDayOfQuarter) {
            adjustedEnd = endDate.with(endDate.getMonth().firstMonthOfQuarter())
                    .withDayOfMonth(1).minusDays(1);
        }

        // 计算满自然季度
        if (!adjustedStart.isAfter(adjustedEnd)) {
            YearQuarter current = YearQuarter.from(adjustedStart);
            YearQuarter endQuarter = YearQuarter.from(adjustedEnd);

            while (!current.isAfter(endQuarter)) {
                fullQuarters.add(current);
                current = current.nextQuarter();
            }
        }

        //满自然月数
        int fullQuarterCount = fullQuarters.size();
        //计算开始时间本季度天数
        int startQuarterDays = DateTimeUtil.getQuarterTotalDays(startDate);
        //计算开始时间到季度底的剩余天数
        int daysStartRemaining = DateTimeUtil.getDaysRemainingInQuarter(startDate);

        //计算结束时间本季度天数
        int endQuarterDays = DateTimeUtil.getQuarterTotalDays(endDate);
        //计算结束时间到季度初天数
        int daysEndRemaining = DateTimeUtil.getDaysElapsedInQuarter(endDate);

        return getCommonRangeAmountBO(amount, fullQuarterCount, isSameQuarter, daysStartRemaining, startQuarterDays, daysEndRemaining, endQuarterDays, isFirstDayOfQuarter, isEndDayOfQuarter);
    }

    //按半年拆分数据，获取其对应金额
    private static CommonRangeAmountBO getHalfYearRangeAmount(LocalDate startDate, LocalDate endDate, BigDecimal amount) {
        //判断开始日期是否为当半年第一天
        int month = startDate.getMonthValue();
        Boolean isFirstDayOfHalfYear = (month == 1 && startDate.getDayOfMonth() == 1) || (month == 7 && startDate.getDayOfMonth() == 1);
        //判断结束日期是否为当半年最后一天
        int monthEnd = endDate.getMonthValue();
        int dayEnd = endDate.getDayOfMonth();
        int lastDayEnd = endDate.lengthOfMonth();
        Boolean isEndDayOfHalfYear =  (monthEnd == 6 && dayEnd == lastDayEnd) ||  (monthEnd == 12 && dayEnd == lastDayEnd);

        //判断起止时间是否为同一个季度
        Boolean isSameHalfYear = DateTimeUtil.isSameFullHalfYear(startDate, endDate);

        // 计算满自然季度
        // 计算满自然半年
        List<YearHalf> fullHalfYears = new ArrayList<>();
        LocalDate adjustedStart = startDate;
        LocalDate adjustedEnd = endDate;

        // 如果开始日期不是半年第一天，则从下个半年开始计算
        if (!isFirstDayOfHalfYear) {
            adjustedStart = DateTimeUtil.getNextHalfYearStart(startDate);
        }

        // 如果结束日期不是半年最后一天，则计算到上个半年结束
        if (!isEndDayOfHalfYear) {
            adjustedEnd = DateTimeUtil.getPreviousHalfYearEnd(endDate);
        }

        // 计算满自然半年
        if (!adjustedStart.isAfter(adjustedEnd)) {
            YearHalf current = YearHalf.from(adjustedStart);
            YearHalf endHalf = YearHalf.from(adjustedEnd);

            while (!current.isAfter(endHalf)) {
                fullHalfYears.add(current);
                current = current.nextHalf();
            }
        }

        //满自然月数
        int fullHalfYearsCount = fullHalfYears.size();
        //计算开始时间本季度天数
        int startHalfYearsDays = DateTimeUtil.getDaysInHalfYear(startDate);
        //计算开始时间到季度底的剩余天数
        int daysStartRemaining = DateTimeUtil.getDaysToHalfYearEnd(startDate);

        //计算结束时间本季度天数
        int endHalfYearsDays = DateTimeUtil.getDaysInHalfYear(endDate);
        //计算结束时间到季度初天数
        int daysEndRemaining = DateTimeUtil.getDaysSinceHalfYearStart(endDate);

        return getCommonRangeAmountBO(amount, fullHalfYearsCount, isSameHalfYear, daysStartRemaining, startHalfYearsDays, daysEndRemaining, endHalfYearsDays, isFirstDayOfHalfYear, isEndDayOfHalfYear);
    }

    //按年拆分数据，获取其对应金额
    private static CommonRangeAmountBO getYearRangeAmount(LocalDate startDate, LocalDate endDate, BigDecimal amount) {
        //判断开始日期是否为当半年第一天
        Boolean isFirstDayOfYear = startDate.getDayOfYear() == 1;
        //判断结束日期是否为当半年最后一天
        Boolean isEndDayOfYear =  endDate.equals(endDate.with(TemporalAdjusters.lastDayOfYear()));

        //判断起止时间是否为同一年
        Boolean isSameHalfYear = startDate.getYear() == endDate.getYear();


        // 获取年份范围
        int startYear = startDate.getYear();
        int endYear = endDate.getYear();
        // 计算完整自然年
        List<Integer> completeYears = new ArrayList<>();
        // 如果起始年和结束年是同一年
        if (startYear == endYear) {
            if (isFirstDayOfYear && isEndDayOfYear) {
                completeYears.add(startYear);
            }
        } else {
            // 计算中间的完整年份
            int firstFullYear = isFirstDayOfYear ? startYear : startYear + 1;
            int lastFullYear = isEndDayOfYear ? endYear : endYear - 1;

            for (int year = firstFullYear; year <= lastFullYear; year++) {
                completeYears.add(year);
            }
        }
        //满自然月数
        int fullYearsCount = completeYears.size();

        //计算开始时间本季度天数
        int startYearsDays = DateTimeUtil.getDaysInYear(startDate);
        //计算开始时间到季度底的剩余天数
        int daysStartRemaining = DateTimeUtil.getDaysRemainingInYear(startDate);

        //计算结束时间本季度天数
        int endYearsDays = DateTimeUtil.getDaysInYear(endDate);
        //计算结束时间到季度初天数
        int daysEndRemaining = DateTimeUtil.getDaysFromYearStart(endDate);

        return getCommonRangeAmountBO(amount, fullYearsCount, isSameHalfYear, daysStartRemaining, startYearsDays, daysEndRemaining, endYearsDays, isFirstDayOfYear, isEndDayOfYear);
    }


    public void getRangeAmount(List<CommonRangeDayAmountBO> dayAmountList, BigDecimal totalAmount) {
        if (dayAmountList == null || dayAmountList.isEmpty() || totalAmount == null) {
            return;
        }

        // 1. 获取最小startDate和最大endDate
        LocalDate minStartDate = dayAmountList.stream()
                .map(CommonRangeDayAmountBO::getStartDate)
                .min(LocalDate::compareTo)
                .orElseThrow(() -> new RuntimeException("无法获取最小开始日期"));

        LocalDate maxEndDate = dayAmountList.stream()
                .map(CommonRangeDayAmountBO::getEndDate)
                .max(LocalDate::compareTo)
                .orElseThrow(() -> new RuntimeException("无法获取最大结束日期"));

        // 2. 计算总天数
        long totalDays = ChronoUnit.DAYS.between(minStartDate, maxEndDate) + 1; // +1包含首尾两天

        // 3. 计算每条记录的天数占比并分配金额
        BigDecimal totalDistributed = BigDecimal.ZERO;

        // 处理前n-1条记录
        for (int i = 0; i < dayAmountList.size() - 1; i++) {
            CommonRangeDayAmountBO bo = dayAmountList.get(i);

            // 计算当前记录的天数
            long days = ChronoUnit.DAYS.between(bo.getStartDate(), bo.getEndDate()) + 1;

            // 计算占比
            BigDecimal ratio = BigDecimal.valueOf(days)
                    .divide(BigDecimal.valueOf(totalDays), 10, RoundingMode.HALF_UP);

            // 计算分配的金额
            BigDecimal distributedAmount = totalAmount.multiply(ratio)
                    .setScale(2, RoundingMode.HALF_UP);
            totalDistributed = totalDistributed.add(distributedAmount);

            // 设置amount
            bo.setReductionAmount(addBigDecimal(bo.getReductionAmount(),distributedAmount));
        }

        // 4. 处理最后一条记录（倒减逻辑）
        if (dayAmountList.size() > 1) {
            CommonRangeDayAmountBO lastBo = dayAmountList.get(dayAmountList.size() - 1);
            BigDecimal remainingAmount = totalAmount.subtract(totalDistributed)
                    .setScale(2, RoundingMode.HALF_UP);
            lastBo.setReductionAmount(addBigDecimal(lastBo.getReductionAmount(),remainingAmount));
        } else {
            // 如果只有一条记录，直接分配全部金额
            dayAmountList.get(0).setReductionAmount(addBigDecimal(dayAmountList.get(0).getReductionAmount(),totalAmount));
        }
    }
    public static BigDecimal addBigDecimal(BigDecimal amount, BigDecimal amount1) {
        // 如果 amount 为 null，则将其视为 0
        BigDecimal safeAmount = (amount != null) ? amount : BigDecimal.ZERO;
        // 如果 amount1 为 null，则将其视为 0
        BigDecimal safeAmount1 = (amount1 != null) ? amount1 : BigDecimal.ZERO;
        // 返回相加结果
        return safeAmount.add(safeAmount1);
    }

    /*private static CommonRangeAmountBO getQuarterRangeContinuousAmount(LocalDate startDate, LocalDate endDate, BigDecimal amount){
        CommonRangeAmountBO result = new CommonRangeAmountBO();
        int fullQuarters = 0;
        BigDecimal startPartialDays = BigDecimal.ZERO;
        BigDecimal endPartialDays = BigDecimal.ZERO;

        // 计算开始部分季度
        LocalDate quarterStart = startDate;
        LocalDate quarterEnd = quarterStart.plusMonths(3).minusDays(1);
        quarterEnd = quarterEnd.withDayOfMonth(quarterEnd.lengthOfMonth());
        if (quarterEnd.isAfter(endDate)) {
            result.setStartMonthAmount(amount);
            result.setAvgMonthAmount(amount);
            result.setEndMonthAmount(amount);
            // 整个时间段不足一个季度
            *//*long totalDays = ChronoUnit.DAYS.between(startDate, endDate) + 1;
            startPartialDays = totalDays / (double) quarterStart.lengthOfYear() * 4; // 转换为季度比例
            return new QuarterCalculationResult(0, startPartialDays, 0, allQuarters);*//*
        }

        //计算开始时间到季度结束时间的天数
        long startQuarterDays = ChronoUnit.DAYS.between(startDate, quarterEnd);
        //当前季度总天数
        long quarterTotallDays = ChronoUnit.DAYS.between(startDate.withDayOfMonth(1), quarterEnd);
        startPartialDays = BigDecimal.valueOf(startQuarterDays).divide(BigDecimal.valueOf(quarterTotallDays), 2, RoundingMode.HALF_UP);

        // 移动到下一个季度开始
        quarterStart = quarterEnd.plusDays(1).withDayOfMonth(1);

        // 计算完整季度
        while (!quarterStart.plusMonths(3).minusDays(1).isAfter(endDate)) {
            quarterEnd = quarterStart.plusMonths(3).minusDays(1);
            //quarterEnd = quarterEnd.withDayOfMonth(quarterEnd.lengthOfMonth());
            fullQuarters++;
            quarterStart = quarterEnd.plusDays(1).withDayOfMonth(1);
        }
        // 计算结束部分季度
        if (quarterStart.isBefore(endDate) || quarterStart.isEqual(endDate)) {
            long endQuarterDays = ChronoUnit.DAYS.between(quarterStart, endDate);
            //当前季度总天数
            long endQuarterTotallDays = ChronoUnit.DAYS.between(quarterStart.withDayOfMonth(1), quarterEnd);
            endPartialDays = BigDecimal.valueOf(endQuarterDays).divide(BigDecimal.valueOf(endQuarterTotallDays), 2, RoundingMode.HALF_UP);
        }

        BigDecimal quarterAmount = amount.divide((
                BigDecimal.valueOf(fullQuarters)
                        .add(startPartialDays)
                                .add(endPartialDays)
                        ), 0, RoundingMode.DOWN);
        result.setAvgMonthAmount(quarterAmount);
        if(startPartialDays.compareTo(BigDecimal.ZERO) > 0 && endPartialDays.compareTo(BigDecimal.ZERO) > 0){
            result.setAvgMonthAmount(quarterAmount);
            result.setStartMonthAmount(quarterAmount.multiply(startPartialDays).setScale(0, RoundingMode.DOWN));
            result.setEndMonthAmount(amount.subtract(result.getStartMonthAmount()).subtract(quarterAmount.multiply(BigDecimal.valueOf(fullQuarters))));
        }else if (startPartialDays.compareTo(BigDecimal.ZERO) > 0 && endPartialDays.compareTo(BigDecimal.ZERO) <= 0){
            result.setAvgMonthAmount(quarterAmount);
            result.setEndMonthAmount(quarterAmount);
            result.setStartMonthAmount(amount.subtract(quarterAmount.multiply(BigDecimal.valueOf(fullQuarters))));
        }else{
            result.setAvgMonthAmount(quarterAmount);
            result.setStartMonthAmount(quarterAmount);
            result.setEndMonthAmount(amount.subtract(quarterAmount.multiply(BigDecimal.valueOf(fullQuarters))));
        }

        return result;
    }*/

    private static CommonRangeAmountBO getQuarterRangeContinuousAmount(LocalDate startDate, LocalDate endDate, BigDecimal amount,int quarterCount){
        CommonRangeAmountBO result = new CommonRangeAmountBO();
        //计算时间月份清单
        List<String> allMonthList = DateTimeUtil.getMonthBetweenDate(startDate,endDate);
        //按时间对其进行分割
        List<List<String>> batcheMonthList = Lists.partition(allMonthList, quarterCount);
        if(batcheMonthList.size() == 1){
            result.setStartMonthAmount(amount);
            result.setAvgMonthAmount(amount);
            result.setEndMonthAmount(amount);
            return result;
        }
        //完美期数
        int totalDataNum = batcheMonthList.size();

        List<String> firstMonthList = batcheMonthList.get(0);
        //首期剩余天数占比
        BigDecimal firstRatioNum = BigDecimal.ZERO;
        if(!DateTimeUtil.getFirstDayFromYearMonthString(firstMonthList.get(0)).isEqual(startDate)){
            totalDataNum--;
            //计算开始时间到月初天数
            LocalDate firstDayOfMonth = startDate.with(TemporalAdjusters.firstDayOfMonth());
            int daysRemaining = startDate.getDayOfMonth() - firstDayOfMonth.getDayOfMonth();
            //计算该区间总天数
            int startMonthDays = 0;
            for(String month : firstMonthList){
                startMonthDays = startMonthDays + DateTimeUtil.getFirstDayFromYearMonthString(month).lengthOfMonth();
            }
            firstRatioNum = (BigDecimal.valueOf(startMonthDays).subtract(BigDecimal.valueOf(daysRemaining)).divide(BigDecimal.valueOf(startMonthDays), 2, RoundingMode.HALF_UP));
        }

        List<String> endMonthList = batcheMonthList.get(batcheMonthList.size()-1);
        //末期剩余天数占比
        BigDecimal endRatioNum = BigDecimal.ZERO;
        if(!DateTimeUtil.getLastDayFromYearMonthString(endMonthList.get(endMonthList.size()-1)).isEqual(endDate) || endMonthList.size() != quarterCount){
            totalDataNum--;
            //计算结束时间到月初天数
            LocalDate endDayOfMonth = endDate.with(TemporalAdjusters.firstDayOfMonth());
            int daysRemaining = endDate.getDayOfMonth() - endDayOfMonth.getDayOfMonth()+1;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
            for (String month : endMonthList){
                if(endDate.format(formatter).equals( month)){
                    break;
                }
                daysRemaining = daysRemaining + DateTimeUtil.getFirstDayFromYearMonthString(month).lengthOfMonth();
            }
            //计算该区间总天数
            List<String> remainingMonthList =  DateTimeUtil.getMonthsCrossYear(endMonthList.get(0),  quarterCount-1);
            int endMonthDays = 0;
            for(String month : remainingMonthList){
                endMonthDays = endMonthDays + DateTimeUtil.getFirstDayFromYearMonthString(month).lengthOfMonth();
            }
            endRatioNum = BigDecimal.valueOf(daysRemaining).divide(BigDecimal.valueOf(endMonthDays), 2, RoundingMode.HALF_UP);
        }

        //计算平均金额
        BigDecimal quarterAmount = amount.divide((
                BigDecimal.valueOf(totalDataNum).add(firstRatioNum).add(endRatioNum)
                        ), 0, RoundingMode.DOWN);

        //每月平均金额
        if(totalDataNum == batcheMonthList.size()){
            result.setStartMonthAmount(quarterAmount);
            result.setAvgMonthAmount(quarterAmount);
            result.setEndMonthAmount(amount.subtract(quarterAmount.multiply(BigDecimal.valueOf(totalDataNum-1))));
            return result;
        }
        //首次数据不足
        else if(totalDataNum == batcheMonthList.size()-1 && firstRatioNum.compareTo(BigDecimal.ZERO) > 0){
            result.setStartMonthAmount(amount.subtract(quarterAmount.multiply(BigDecimal.valueOf(totalDataNum))));
            result.setAvgMonthAmount(quarterAmount);
            result.setEndMonthAmount(quarterAmount);
            return result;
        }
        //末次数据不足
        else if(totalDataNum == batcheMonthList.size()-1 && endRatioNum.compareTo(BigDecimal.ZERO) > 0){
            result.setStartMonthAmount(quarterAmount);
            result.setAvgMonthAmount(quarterAmount);
            result.setEndMonthAmount(amount.subtract(quarterAmount.multiply(BigDecimal.valueOf(totalDataNum))));
            return result;
        }
        //首位数据都不足
        else {
            result.setStartMonthAmount(quarterAmount.multiply(firstRatioNum).setScale(0, RoundingMode.DOWN));
            result.setAvgMonthAmount(quarterAmount);
            result.setEndMonthAmount(amount.subtract(quarterAmount.multiply(BigDecimal.valueOf(totalDataNum))).subtract(result.getStartMonthAmount()));
            return result;
        }
    }
    public static void main(String[] args) {
        /*LocalDate startDate5 = LocalDate.of(2024, 1, 1);
        LocalDate endDate5 = LocalDate.of(2024, 12, 31);
        CommonRangeAmountBO res5 = getQuarterRangeContinuousAmount(startDate5, endDate5,BigDecimal.valueOf(5000),3);
        out.println("全部满季度："+startDate5+"至"+endDate5+"数据"+JSON.toJSON(res5));
        LocalDate startDate6 = LocalDate.of(2024, 6, 01);
        LocalDate endDate6 = LocalDate.of(2025, 5, 31);
        CommonRangeAmountBO res6 = getQuarterRangeContinuousAmount(startDate6, endDate6,BigDecimal.valueOf(5000),3);
        out.println("连续季度："+startDate6+"至"+endDate6+"数据"+JSON.toJSON(res6));
        LocalDate startDate = LocalDate.of(2024, 6, 15);
        LocalDate endDate = LocalDate.of(2025, 5, 31);
        CommonRangeAmountBO res = getQuarterRangeContinuousAmount(startDate, endDate,BigDecimal.valueOf(5000),3);
        out.println("开始不满季度："+startDate+"至"+endDate+"数据"+JSON.toJSON(res));
        LocalDate startDate1 = LocalDate.of(2024, 6, 1);
        LocalDate endDate1 = LocalDate.of(2025, 5, 15);
        CommonRangeAmountBO res1 = getQuarterRangeContinuousAmount(startDate1, endDate1,BigDecimal.valueOf(5000),3);
        out.println("结尾不满季度："+startDate1+"至"+endDate1+"数据"+JSON.toJSON(res1));*/


       /* List<CommonRangeDateBO> dateBOList = new ArrayList<>();
        CommonRangeDateBO dateBO = new CommonRangeDateBO();
        dateBO.setId("1");
        dateBO.setCostStartTime(LocalDate.of(2025, 1, 10));
        dateBO.setCostEndTime(LocalDate.of(2025, 1, 31));
        dateBOList.add(dateBO);
        CommonRangeDateBO dateBO2 = new CommonRangeDateBO();
        dateBO2.setId("2");
        dateBO2.setCostStartTime(LocalDate.of(2025, 10, 01));
        dateBO2.setCostEndTime(LocalDate.of(2025, 10, 31));
        dateBOList.add(dateBO2);
        CommonRangeDateBO dateBO1 = new CommonRangeDateBO();
        dateBO1.setId("3");
        dateBO1.setCostStartTime(LocalDate.of(2025, 11, 01));
        dateBO1.setCostEndTime(LocalDate.of(2025, 11, 30));
        dateBOList.add(dateBO1);
        out.println("--"+JSON.toJSONString(calculateDistributedAmounts(dateBOList,new BigDecimal(2955))));*/
       /* List<CommonRangeDateBO> list = new ArrayList<>();

        // 定义日期范围数组
        LocalDate[][] dateRanges = {
                {LocalDate.of(2025, 1, 10), LocalDate.of(2025, 1, 31)},
                {LocalDate.of(2025, 2, 1), LocalDate.of(2025, 2, 28)},
                {LocalDate.of(2025, 3, 1), LocalDate.of(2025, 3, 9)},
                {LocalDate.of(2025, 3, 10), LocalDate.of(2025, 3, 31)},
                {LocalDate.of(2025, 4, 1), LocalDate.of(2025, 4, 30)},
                {LocalDate.of(2025, 5, 1), LocalDate.of(2025, 5, 31)},
                {LocalDate.of(2025, 6, 1), LocalDate.of(2025, 6, 30)},
                {LocalDate.of(2025, 7, 1), LocalDate.of(2025, 7, 9)}
        };

        // 创建实体并添加到列表，ID递增
        int id = 1;
        for (LocalDate[] range : dateRanges) {
            CommonRangeDateBO bo = new CommonRangeDateBO();
            bo.setId(String.valueOf(id++));
            bo.setCostStartTime(range[0]);
            bo.setCostEndTime(range[1]);
            bo.setProportion(BigDecimal.ONE); // 默认比例为1，你可以根据需要修改
            list.add(bo);
        }
        out.println("--"+JSON.toJSON(calculateDistributedAmounts(list,BigDecimal.valueOf(327534.4))));

*/
        List<CommonRangeDateBO> list2 = new ArrayList<>();

        // 定义日期范围数组（从你提供的数据）
        LocalDate[][] dateRanges1 = {
               /* {LocalDate.of(2024, 11, 9), LocalDate.of(2024, 11, 30)},
                {LocalDate.of(2024, 12, 1), LocalDate.of(2024, 12, 8)},
                {LocalDate.of(2024, 12, 9), LocalDate.of(2024, 12, 31)},
                {LocalDate.of(2025, 1, 1), LocalDate.of(2025, 1, 8)},
                {LocalDate.of(2025, 1, 9), LocalDate.of(2025, 1, 31)},
                {LocalDate.of(2025, 2, 1), LocalDate.of(2025, 2, 8)},
                {LocalDate.of(2025, 2, 9), LocalDate.of(2025, 2, 28)},
                {LocalDate.of(2025, 3, 1), LocalDate.of(2025, 3, 8)},
                {LocalDate.of(2025, 3, 9), LocalDate.of(2025, 3, 31)},
                {LocalDate.of(2025, 4, 1), LocalDate.of(2025, 4, 8)},*/
                {LocalDate.of(2025, 5, 1), LocalDate.of(2025, 5, 31)},
                {LocalDate.of(2025, 6, 1), LocalDate.of(2025, 6, 30)},
                {LocalDate.of(2025, 7, 1), LocalDate.of(2025, 7, 31)},
                {LocalDate.of(2025, 11, 1), LocalDate.of(2025, 11, 30)}
        };

        // 创建实体并添加到列表，ID递增
        int id1 = 1;
        for (LocalDate[] range : dateRanges1) {
            CommonRangeDateBO bo = new CommonRangeDateBO();
            bo.setId(String.valueOf(id1++));
            bo.setCostStartTime(range[0]);
            bo.setCostEndTime(range[1]);
            bo.setProportion(BigDecimal.ONE); // 默认比例为1
            list2.add(bo);
        }
        out.println("--"+JSON.toJSON(calculateDistributedAmounts(list2,BigDecimal.valueOf(216072.58))));
    }

    public static Map<String, BigDecimal> calculateDistributedAmounts(
            List<CommonRangeDateBO> dateBOList,
            BigDecimal totalAmount){
        if(Objects.isNull(totalAmount)){
            totalAmount = BigDecimal.ZERO;
        }
        Map<String, BigDecimal> resultMap = new LinkedHashMap<>();
        BigDecimal totalNum = BigDecimal.ZERO;
        for(CommonRangeDateBO dateBO : dateBOList){
            //判断开始日期是否为当月第一天
            Boolean isFirstDayOfMonth = dateBO.getCostStartTime().getDayOfMonth() == 1;
            //判断结束日期是否为当月最后一天
            Boolean isEndDayOfMonth = dateBO.getCostEndTime().equals(dateBO.getCostEndTime().with(TemporalAdjusters.lastDayOfMonth()));
            //满自然月
            if(isFirstDayOfMonth && isEndDayOfMonth){
                totalNum = totalNum.add(BigDecimal.ONE);
                dateBO.setProportion(BigDecimal.ONE);
            }
            //结束时间不为本月最后一天
            else if(isFirstDayOfMonth){
                //计算结束时间本月天数
                int endMonthDays = dateBO.getCostEndTime().lengthOfMonth();
                //计算结束时间到月初天数
                LocalDate firstDayOfMonth = dateBO.getCostEndTime().with(TemporalAdjusters.firstDayOfMonth());
                int daysEndRemaining = dateBO.getCostEndTime().getDayOfMonth() - firstDayOfMonth.getDayOfMonth()+1;
                BigDecimal radio = BigDecimal.valueOf(daysEndRemaining).divide(BigDecimal.valueOf(endMonthDays), 10, RoundingMode.HALF_UP);
                totalNum = totalNum.add(radio);
                dateBO.setProportion(radio);
            }
            //开始时间不为本月第一天
            else{
                //计算开始时间本月天数
                int startMonthDays = dateBO.getCostStartTime().lengthOfMonth();
                //计算开始时间到月底的剩余天数
                LocalDate lastDayOfMonth = dateBO.getCostStartTime().with(TemporalAdjusters.lastDayOfMonth());
                int daysStartRemaining = lastDayOfMonth.getDayOfMonth() - dateBO.getCostStartTime().getDayOfMonth()+1;
                BigDecimal radio = BigDecimal.valueOf(daysStartRemaining).divide(BigDecimal.valueOf(startMonthDays), 10, RoundingMode.HALF_UP);
                totalNum = totalNum.add(radio);
                dateBO.setProportion(radio);
            }
        }
        //计算平均核销金额
        BigDecimal avgAmount = totalAmount.divide(totalNum, 0, RoundingMode.DOWN);
        BigDecimal useAmount = BigDecimal.ZERO;
        //分配核销金额
        for(int i = 0; i < dateBOList.size(); i++){
            //最后一条倒减
            if (i == dateBOList.size() - 1) {
                resultMap.put(dateBOList.get(i).getId(), totalAmount.subtract(useAmount));
            }else{
                BigDecimal thisAmount = avgAmount.multiply(dateBOList.get(i).getProportion()).setScale( 0, RoundingMode.DOWN);
                resultMap.put(dateBOList.get(i).getId(), thisAmount);
                useAmount = useAmount.add(thisAmount);
            }
        }
        return resultMap;
    }

}
