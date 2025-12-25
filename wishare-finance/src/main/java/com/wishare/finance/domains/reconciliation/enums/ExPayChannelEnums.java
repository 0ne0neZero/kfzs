package com.wishare.finance.domains.reconciliation.enums;

/**
 * @author xujian
 * @date 2023/2/21
 * @Description: 外部支付渠道
 */
public enum ExPayChannelEnums {

    银联银行卡消费("银联","C", "银行卡消费"),
    银联支付宝("银联", "Z","支付宝"),
    银联微信("银联", "W","微信"),
    银联pos通("银联", "P","pos通"),
    银联沃支付("银联", "WZF","沃支付"),
    银联二维码("银联", "U","银联二维码"),
    银联数字人民币("银联", "ECNY","数字人民币"),
    ;

    /**
     * 第三方平台名称
     */
    private String external;
    /**
     * 对应的编码
     */
    private String code;

    /**
     * 描述
     */
    private String des;

    public static ExPayChannelEnums valueOfByCode(String code){
        for (ExPayChannelEnums value : values()) {
            if (value.equalsByValue(code)){
                return value;
            }
        }
        return null;
    }

    ExPayChannelEnums(String external, String code, String des) {
        this.external = external;
        this.code = code;
        this.des = des;
    }

    public String getExternal() {
        return external;
    }

    public String getCode() {
        return code;
    }

    public String getDes() {
        return des;
    }

    public boolean equalsByValue(String code){
        return code.equals(this.code);
    }
}
