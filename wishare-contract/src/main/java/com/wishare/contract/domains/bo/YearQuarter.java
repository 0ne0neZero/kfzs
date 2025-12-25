package com.wishare.contract.domains.bo;

import java.time.LocalDate;

/**
 * @author hhb
 * @describe
 * @date 2025/6/10 9:26
 */
// 年季度封装类
public class YearQuarter implements Comparable<YearQuarter> {
    private final int year;
    private final int quarter;

    private YearQuarter(int year, int quarter) {
        this.year = year;
        this.quarter = quarter;
    }

    public static YearQuarter from(LocalDate date) {
        int year = date.getYear();
        int month = date.getMonthValue();
        int quarter = (month - 1) / 3 + 1;
        return new YearQuarter(year, quarter);
    }

    public YearQuarter nextQuarter() {
        if (quarter == 4) {
            return new YearQuarter(year + 1, 1);
        } else {
            return new YearQuarter(year, quarter + 1);
        }
    }

    public boolean isAfter(YearQuarter other) {
        if (year > other.year) return true;
        if (year == other.year) return quarter > other.quarter;
        return false;
    }

    @Override
    public String toString() {
        return year + "Q" + quarter;
    }

    @Override
    public int compareTo(YearQuarter other) {
        if (year != other.year) {
            return Integer.compare(year, other.year);
        }
        return Integer.compare(quarter, other.quarter);
    }
}