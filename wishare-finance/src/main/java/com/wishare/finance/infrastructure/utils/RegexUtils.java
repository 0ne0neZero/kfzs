package com.wishare.finance.infrastructure.utils;

import com.wishare.finance.domains.bill.service.TemporaryChargeBillDomainService;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @description:
 * @author: luguilin
 * @date: 2025-02-13 14:57
 **/
public class RegexUtils {

    /**
     * 账单费项拆分提取标识符的辅助方法
     * @param remark
     * @param tag
     * @return
     */
    public static String extractId(String remark, String tag) {
        Pattern pattern = Pattern.compile("\\[" + tag + ":(.*?)\\]");
        Matcher matcher = pattern.matcher(remark);
        return matcher.find() ? matcher.group(1) : null;
    }
}
