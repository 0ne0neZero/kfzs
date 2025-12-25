package com.wishare.contract.domains.enums;

import com.wishare.owl.exception.OwlBizException;
import org.apache.commons.lang3.StringUtils;

/**
 * 据实结算:5
 * 按年:4
 * 按半年:3
 * 按季度:2
 * 按月:1
 */
public enum SplitModeEnum {

    /**
     * code 交互使用编码
     * name 名称
     * payWayId 合同清单使用的编码
     * interval 月份间隔 0无意义
     **/
    MONTH(1,"按月", "1", 1),
    THREE_MONTH(2,"按季度", "2", 3),
    HALF_YEAR(3,"按半年","3", 6),
    YEAR(4,"按年","4", 12),
    // 0代表无意义 其他没有间隔的概念
    ACTUAL_SETTLE(5,"据实结算","5", 0)
    ;

    private Integer code;
    private String name;
    private String payWayId;
    private Integer interval;

    SplitModeEnum(Integer code, String name,String payWayId,Integer interval) {
        this.name = name;
        this.code = code;
        this.payWayId = payWayId;
        this.interval = interval;
    }

    public Integer getInterval() {
        return interval;
    }

    public void setInterval(Integer interval) {
        this.interval = interval;
    }

    public String getPayWayId() {
        return payWayId;
    }

    public void setPayWayId(String payWayId) {
        this.payWayId = payWayId;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static String parseName(Integer code) {
        for (SplitModeEnum value : SplitModeEnum.values()) {
            if (value.getCode().equals(code)) {
                return value.getName();
            }
        }
        return null;
    }
    public static Integer parseCode(String name) {
        for (SplitModeEnum value : SplitModeEnum.values()) {
            if (value.getName().equals(name)) {
                return value.getCode();
            }
        }
        return null;
    }

    public static SplitModeEnum parseByPayWayId(String payWayId) {
        for (SplitModeEnum value : SplitModeEnum.values()) {
            if (StringUtils.equals(value.getPayWayId(), payWayId)) {
                return value;
            }
        }
        throw new OwlBizException("未知的清单付款方式");
    }

}
