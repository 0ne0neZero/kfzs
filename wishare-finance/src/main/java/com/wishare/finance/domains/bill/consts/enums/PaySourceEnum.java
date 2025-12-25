package com.wishare.finance.domains.bill.consts.enums;


import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.starter.exception.BizException;
import io.swagger.annotations.ApiModelProperty;

/**
 * 结算渠道
 * @Author dxclay
 * @Date 2022/9/13
 * @Version 1.0
 */
public enum PaySourceEnum {

    @ApiModelProperty("支付来源:0-PC管理后台 1-业主端app 2-业主端小程序 3-物管端app 4-物管端小程序 " +
            "10-亿家生活app，11-亿家生活公众号，12-亿家生活小程序，13-亿管家APP，14-亿管家智能POS机，15-亿家生活公众号物管端")

    PC管理后台(0, "管理后台"),
    业主端app(1, "业主端app"),
    业主端小程序(2, "业主端小程序"),
    物管端app(3, "物管端app"),
    物管端小程序(4, "物管端小程序"),
    通联POS机(5, "通联POS机"),
    催缴二维码(6, "催缴二维码"),
    亿家生活app(10, "亿家生活app"),
    亿家生活公众号(11,"亿家生活公众号"),
    亿家生活小程序(12,"亿家生活小程序"),
    亿管家APP(13,"亿管家APP"),
    亿管家智能POS机(14, "亿管家智能POS机"),
    亿家生活公众号物管端(15, "亿家生活公众号物管端"),
    方圆车场(16, "方圆车场临时账单"),
    畅洋电表(17, "畅洋电表推送"),
    远洋车场(18, "远洋通通车场临时账单"),
    中交车场(19, "中交车场临时账单"),
    //经营组付锋文这边停车包月会传20
    停车包月(20, "停车包月临时账单"),

    ;

    private Integer code;
    private String value;

    PaySourceEnum(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    public Integer getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    public boolean equalsByCode(Integer code){
        return this.code.equals(code);
    }

    public static PaySourceEnum valueOfByCode(Integer code){
        for (PaySourceEnum value : values()) {
            if (value.equalsByCode(code)){
                return value;
            }
        }
        throw BizException.throw400(ErrorMessage.PAY_SOURCE_NOT_SUPPORT.msg());
    }

}
