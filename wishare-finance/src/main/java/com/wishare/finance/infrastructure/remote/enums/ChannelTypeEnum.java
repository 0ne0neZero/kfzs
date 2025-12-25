package com.wishare.finance.infrastructure.remote.enums;

/**
 * @author xujian
 * @date 2022/11/23
 * @Description:  渠道商类型：1 微信，2 支付宝，3 银联，4 工商银行，5 光大银行，6 农业银行
 */
public enum ChannelTypeEnum {

    微信(1, "微信"),
    支付宝(2, "支付宝"),
    银联(3, "银联"),
    工商银行(4, "工商银行"),
    光大银行(5, "光大银行"),
    农业银行(6, "农业银行"),
    招商银企直连(7, "招商银企直连"),
    郑州银行(8, "郑州银行"),
    通联支付(9,"通联支付"),
    广发银行(10,"广发银行"),
    招商银行(13, "招商银行"),


    ;

    private Integer code;

    private String des;

    public static ChannelTypeEnum valueOfByCode(Integer code) {
        ChannelTypeEnum e = null;
        for (ChannelTypeEnum ee : ChannelTypeEnum.values()) {
            if (ee.getCode() == code) {
                e = ee;
                break;
            }
        }
        return e;
    }

    ChannelTypeEnum(Integer code, String des) {
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
