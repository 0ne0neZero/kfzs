package com.wishare.finance.domains.invoicereceipt.consts.enums;

/**
 * @author xujian
 * @date 2022/9/23
 * @Description: 系统来源编码 1收费系统，2合同系统，3民宿管理，101 亿家优选系统，102 BPM系统
 */
public enum SysSourceEnum {
    未知系统(0, "未知系统"),
    收费系统(1, "收费系统"),
    合同系统(2, "合同系统"),
    民宿管理(3,"民宿管理"),
    工单系统(4,"工单系统"),
    电商管理(5,"电商管理"),
    停车包月管理(6,"停车包月管理"),
    停车场入账管理(7,"停车场入账管理"),
    // 财务中台(4,"财务中台"),

    亿家优选系统(101,"亿家优选系统"),
    BPM系统(102,"BPM系统"),


    用友ncc(22,"用友ncc"),
    ;

    private Integer code;

    private String des;

    public static SysSourceEnum valueOfByCode(Integer code) {
        SysSourceEnum e = null;
        for (SysSourceEnum ee : SysSourceEnum.values()) {
            if (ee.getCode() == code) {
                e = ee;
                break;
            }
        }
        return e;
    }

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
