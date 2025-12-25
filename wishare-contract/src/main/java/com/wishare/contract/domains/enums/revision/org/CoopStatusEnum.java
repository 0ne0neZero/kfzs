package com.wishare.contract.domains.enums.revision.org;

/**
 * @version 1.0.0
 * @Description： 供应商客户合作状态枚举
 * @Author： chenglong
 * @since： 2023/6/5  20:06
 */
public enum CoopStatusEnum {

    合作中(1, "合作中"),
    未合作(2, "未合作"),
    已结束(3, "已结束"),
    ;

    private Integer code;
    private String msg;

    CoopStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static CoopStatusEnum parse(Integer code) {
        for (CoopStatusEnum value : CoopStatusEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

}
