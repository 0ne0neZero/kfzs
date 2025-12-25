package com.wishare.finance.infrastructure.remote.fo.external.baiwangjinshuiyun.enums;

import com.wishare.finance.infrastructure.remote.enums.FangyuanReturnCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author dongpeng
 * @date 2023/11/1 16:50
 */
@Getter
public enum ReturnCodeEnum {
    开票成功("0", "开票成功"),
    开票中("1", "开票成功但未签章"),
    开票失败("3", "开票失败");

    private String code;

    private String des;


    public static ReturnCodeEnum valueOfByCode(String code) {
        ReturnCodeEnum e = null;
        for (ReturnCodeEnum ee : ReturnCodeEnum.values()) {
            if (ee.getCode().equals(code)) {
                e = ee;
                break;
            }
        }
        return e;
    }

    ReturnCodeEnum(String code, String des) {
        this.code = code;
        this.des = des;
    }

    public String getCode() {
        return code;
    }

    public String getDes() {
        return des;
    }
}
