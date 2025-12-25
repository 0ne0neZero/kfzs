package com.wishare.finance.infrastructure.remote.enums;

/**
 * 方圆传参发票主体--清单标志
 * @author dongpeng
 * @version 1.0
 * @since 2023/7/5
 */
public enum FangyuanQdqdbzEnum {

    无清单(0, "无清单"),
    带清单(1, "带清单");

    private Integer code;

    private String des;


    public static FangyuanQdqdbzEnum valueOfByCode(Integer code) {
        FangyuanQdqdbzEnum e = null;
        for (FangyuanQdqdbzEnum ee : FangyuanQdqdbzEnum.values()) {
            if (ee.getCode().equals(code)) {
                e = ee;
                break;
            }
        }
        return e;
    }

    FangyuanQdqdbzEnum(Integer code, String des) {
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
