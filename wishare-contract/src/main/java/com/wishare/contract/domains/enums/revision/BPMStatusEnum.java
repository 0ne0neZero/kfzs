package com.wishare.contract.domains.enums.revision;

//BPM状态
public enum BPMStatusEnum {
    未发起(0,"未发起"),
    已发起(1,"已发起"),
    已通过(2,"已通过"),
    已驳回(3,"已驳回")
    ;

    private Integer code;
    private String name;

    BPMStatusEnum(Integer code, String name) {
        this.name = name;
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static String parseName(Integer code) {
        for (BPMStatusEnum value : BPMStatusEnum.values()) {
            if (value.getCode().equals(code)) {
                return value.getName();
            }
        }
        return null;
    }

    public static Integer parseCode(String name) {
        for (BPMStatusEnum value : BPMStatusEnum.values()) {
            if (value.getName().equals(name)) {
                return value.getCode();
            }
        }
        return null;
    }


}
