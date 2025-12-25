package com.wishare.contract.domains.enums.revision;

import java.util.Arrays;
import java.util.List;

//NK状态
public enum NkStatusEnum {
    未开启(0,"未开启"),
    已开启(1,"已开启"),
    已关闭(2,"已关闭"),
    关闭中(3,"关闭中")
    ;

    private Integer code;
    private String name;

    NkStatusEnum(Integer code, String name) {
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
        for (NkStatusEnum value : NkStatusEnum.values()) {
            if (value.getCode().equals(code)) {
                return value.getName();
            }
        }
        return null;
    }

    public static Integer parseCode(String name) {
        for (NkStatusEnum value : NkStatusEnum.values()) {
            if (value.getName().equals(name)) {
                return value.getCode();
            }
        }
        return null;
    }

    public static List<Integer> getNkStatusList() {
        return Arrays.asList(NkStatusEnum.已开启.getCode(), NkStatusEnum.已关闭.getCode(), NkStatusEnum.关闭中.getCode());
    }


}
