package com.wishare.contract.domains.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * 动态成本-明细账单状态
 */
@Getter
@AllArgsConstructor
public enum BillTypeEnum {
    /**
     * 发起合同
     */
    CONTRACT_INIT(1, "发起合同", "合同类"),
    /**
     * 签补充协议
     */
    CONTRACT_SIGN(2, "签补充协议", "合同类"),
    /**
     * 驳回
     */
    CONTRACT_REJECT(3, "驳回", "合同类"),
    /**
     * 实际结算
     */
    CONTRACT_SETTLE(4, "实际结算", "合同类"),
    /**
     * 立项发起
     */
    PROJECT_INIT(5, "立项发起", "非合同类"),
    /**
     * 立项驳回
     */
    PROJECT_REJECT(6, "立项驳回", "非合同类"),
    /**
     * 立项结算
     */
    PROJECT_SETTLE(7, "立项结算", "非合同类");

    private final Integer code;
    private final String name;
    private final String type;


    /**
     * @param code 枚举code
     * @return 枚举中文
     */
    public static String getNameByCode(Integer code) {
        for (BillTypeEnum e : BillTypeEnum.values()) {
            if (e.code.equals(code)) {
                return e.name;
            }
        }
        return null;
    }

    public static BillTypeEnum getEnumByCode(Integer code) {
        for (BillTypeEnum enums : BillTypeEnum.values()) {
            if (enums.getCode().equals(code)) {
                return enums;
            }
        }
        return null;
    }

    public static BillTypeEnum getEnumByType(String type) {
        for (BillTypeEnum enums : BillTypeEnum.values()) {
            if (enums.getType().equals(type)) {
                return enums;
            }
        }
        return null;
    }

    /**
     * @param code 枚举code
     * @return 枚举中文
     */
    public static String getTypeByCode(Integer code) {
        for (BillTypeEnum e : BillTypeEnum.values()) {
            if (e.code.equals(code)) {
                return e.type;
            }
        }
        return null;
    }

    public static Map<String, String> getNameTypeMap() {
        Map<String, String> map = new HashMap<>();
        for (BillTypeEnum value : BillTypeEnum.values()) {
            map.put(value.getName(), value.getType());
        }
        return map;
    }

}
