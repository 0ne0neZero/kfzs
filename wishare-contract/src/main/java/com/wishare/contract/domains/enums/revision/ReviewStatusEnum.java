package com.wishare.contract.domains.enums.revision;

/**
 * @version 1.0.0
 * @Description：
 * @Author： chenglong
 * @since： 2023/6/25  15:18
 */
public enum ReviewStatusEnum {

    待提交(0,"待提交"),
    审批中(1,"审批中"),
    已通过(2,"已通过"),
    已拒绝(3,"已拒绝"),
    已驳回(4,"已驳回"),

    DRAFT(9,"草稿"),
    审批通过(99,"审批通过")
    ;

    private Integer code;
    private String name;

    ReviewStatusEnum(Integer code, String name) {
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
        for (ReviewStatusEnum value : ReviewStatusEnum.values()) {
            if (value.getCode().equals(code)) {
                return value.getName();
            }
        }
        return null;
    }

}
