package com.wishare.contract.domains.bo;

import java.time.LocalDate;

/**
 * @author hhb
 * @describe
 * @date 2025/6/10 11:24
 */
public class YearHalf {
    private final int year;
    private final int half; // 1=上半年, 2=下半年

    public YearHalf(int year, int half) {
        this.year = year;
        this.half = half;
    }

    public static YearHalf from(LocalDate date) {
        int half = (date.getMonthValue() <= 6) ? 1 : 2;
        return new YearHalf(date.getYear(), half);
    }

    public YearHalf nextHalf() {
        if (half == 1) {
            return new YearHalf(year, 2);
        } else {
            return new YearHalf(year + 1, 1);
        }
    }

    public boolean isAfter(YearHalf other) {
        if (year > other.year) return true;
        if (year == other.year) return half > other.half;
        return false;
    }

    @Override
    public String toString() {
        return year + (half == 1 ? "H1" : "H2");
    }
}
