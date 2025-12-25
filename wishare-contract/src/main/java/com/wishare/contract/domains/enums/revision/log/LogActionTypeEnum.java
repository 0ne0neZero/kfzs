package com.wishare.contract.domains.enums.revision.log;

/**
 * @version 1.0.0
 * @Description：
 * @Author： chentian
 * @since： 2023/6/28  14:19
 */
public enum LogActionTypeEnum {

    新增("add","新增"),

    EXCEL导入新增("excelAdd","EXCEL导入新增"),

    编辑("edit","编辑"),

    删除("delete","删除"),

    终止("end","终止"),

    提交("post","提交"),

    变更("change","变更"),

    续签("continue","续签"),

    推送("push","推送"),
    ;

    private String code;
    private String name;

    LogActionTypeEnum(String code, String name) {
        this.name = name;
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static String parseName(String code) {
        for (LogActionTypeEnum value : LogActionTypeEnum.values()) {
            if (value.getCode().equals(code)) {
                return value.getName();
            }
        }
        return null;
    }

}
