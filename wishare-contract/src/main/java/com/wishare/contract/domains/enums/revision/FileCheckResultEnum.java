package com.wishare.contract.domains.enums.revision;

/**
 * @description:
 * @author: zhangfuyu
 * @Date: 2024/5/9/10:15
 */
public enum FileCheckResultEnum {
    验证通过(0,"验证通过"),
    文件被篡改(1,"文件被篡改"),
    没有签名或印章(2,"没有签名或印章"),
    OFD印章不合法(3,"OFD印章不合法"),
    xbrl解析失败(4,"xbrl解析失败"),
    ;

    private Integer code;
    private String name;

    FileCheckResultEnum(Integer code, String name) {
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
        for (FileCheckResultEnum value : FileCheckResultEnum.values()) {
            if (value.getCode().equals(code)) {
                return value.getName();
            }
        }
        return null;
    }

    public static Integer parseCode(String name) {
        for (FileCheckResultEnum value : FileCheckResultEnum.values()) {
            if (value.getName().equals(name)) {
                return value.getCode();
            }
        }
        return null;
    }
}
