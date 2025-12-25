package com.wishare.contract.domains.enums.revision;

/**
 * @version 1.0.0
 * @Description：
 * @Author： chenglong
 * @since： 2023/6/27  11:21
 */
public enum ContractRevStatusEnum {

    //-- 0 待提交， 1 审批中， 2 已拒绝， 3 未开始， 4 执行中， 5 已到期， 6 已终止
    尚未履行(0,"尚未履行"),
    正在履行(1,"正在履行"),
    合同停用(2,"合同停用"),
    合同终止(3,"合同终止"),
    未生效(99,"未生效");

    private Integer code;
    private String name;

    ContractRevStatusEnum(Integer code, String name) {
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
        for (ContractRevStatusEnum value : ContractRevStatusEnum.values()) {
            if (value.getCode().equals(code)) {
                return value.getName();
            }
        }
        return null;
    }

}
