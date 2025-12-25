package com.wishare.finance.infrastructure.remote.enums;

import java.util.List;

/**
 * @author xujian
 * @date 2022/9/21
 * @Description: 推送方式：-1,不推送（默认）,0,邮箱;1,手机;2,邮箱、手机
 */
public enum NuonuoPushModeEnum {

    不推送(-1, "不推送"),
    邮箱(0, "邮箱"),
    手机(1, "手机"),
    邮箱手机(2, "邮箱、手机"),


    ;

    private Integer code;

    private String des;


    public static NuonuoPushModeEnum valueOfByCode(Integer code) {
        NuonuoPushModeEnum e = null;
        for (NuonuoPushModeEnum ee : NuonuoPushModeEnum.values()) {
            if (ee.getCode() == code) {
                e = ee;
                break;
            }
        }
        return e;
    }

    NuonuoPushModeEnum(Integer code, String des) {
        this.code = code;
        this.des = des;
    }

    public Integer getCode() {
        return code;
    }

    public String getDes() {
        return des;
    }
}
