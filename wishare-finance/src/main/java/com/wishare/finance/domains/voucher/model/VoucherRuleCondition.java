package com.wishare.finance.domains.voucher.model;

import com.alibaba.fastjson.JSONArray;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * @description: 规则过滤条件类
 * @author: pgq
 * @since: 2023/1/3 19:31
 * @version: 1.0.0
 */
@Getter
@Setter
public class VoucherRuleCondition<T> {

    /**
     * 比较值
     */
    private String compare;

    /**
     * 值名称
     */
    private String valueName;

    /**
     * 比较中文
     */
    private String compareName;

    /**
     * 条件值
     */
    private String conditions;

    /**
     * 条件名称
     */
    private String conditionsName;

    /**
     * 值
     */
    private T value;
}
