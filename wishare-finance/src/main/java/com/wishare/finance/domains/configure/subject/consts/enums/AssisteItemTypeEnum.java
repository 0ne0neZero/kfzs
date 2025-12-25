package com.wishare.finance.domains.configure.subject.consts.enums;


import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.starter.exception.BizException;

/**
 * 辅助核算类型
 * @Author dxclay
 * @Date 2022/8/23
 * @Version 1.0
 */
public enum AssisteItemTypeEnum {

    UNKNOWN(0, "", "未知类型"),
    部门(1, "0001", "部门"),
    业务单元(2, "0025", "业务单元"),
    收支项目(3, "0008", "收支项目"),
    业务类型(4, "0063", "业务类型"),
    客商(5, "0004", "客商"),
    增值税税率(6, "0066", "增值税税率"),
    项目(7, "0010", "项目"),
    人员档案(8, "0002", "人员档案"),
    银行账户(9, "0011", "银行账户"),
    存款账户性质(10, "0065", "存款账户性质"),
    坏账准备(11, "0054", "坏账准备增减方式"),
    收支明细项(12, "0074", "收支明细项"),
    ;

    private int code;
    private String ascCode;
    private String value;

    AssisteItemTypeEnum(int code, String ascCode, String value) {
        this.code = code;
        this.ascCode = ascCode;
        this.value = value;
    }

    public static AssisteItemTypeEnum valueOfByCode(int code){
        for (AssisteItemTypeEnum value : values()) {
            if (value.equalsByCode(code)){
                return value;
            }
        }
        throw BizException.throw400(ErrorMessage.VOUCHER_ASSISTE_ITEM_NOT_SUPPORT.msg());
    }

    public static AssisteItemTypeEnum valueOfByAscCode(String ascCode){
        for (AssisteItemTypeEnum value : values()) {
            if (value.equalsByAscCode(ascCode)){
                return value;
            }
        }
        return UNKNOWN;
    }


    public int getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    public String getAscCode() {
        return ascCode;
    }

    public boolean equalsByAscCode(String ascCode){
        return this.ascCode.equals(ascCode);
    }

    public boolean equalsByCode(int code){
        return code == this.code;
    }

}
