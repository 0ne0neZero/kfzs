package com.wishare.contract.apps.remote.finance.enums;

/**
 * @author xujian
 * @date 2022/9/23
 * @Description: 系统来源编码 1收费系统，2合同系统，3民宿管理，101 亿家优选系统，102 BPM系统
 */
public enum SysSourceEnum {

    合同系统(2, "合同系统"),
    ;

    private Integer code;

    private String des;

    SysSourceEnum(Integer code, String des) {
        this.code = code;
        this.des = des;
    }

    public boolean equalsByCode(int code){
        return code == this.code;
    }

    public Integer getCode() {
        return code;
    }

    public String getDes() {
        return des;
    }
}
