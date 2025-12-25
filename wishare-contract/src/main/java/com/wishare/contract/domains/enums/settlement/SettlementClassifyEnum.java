package com.wishare.contract.domains.enums.settlement;

/**
 * @author longhuadmin
 */
public enum SettlementClassifyEnum {
    /**
     * 结算分类
     * 0工程类 1秩序类(保安) 2环境类(保洁、绿化、垃圾清运、不含化粪池清掏) 3消防维保 4电梯维保 5设备设施维修 6案场类 7智能化、信息化 8房屋租赁类 9劳务派遣类 10增值类 11猎头委托、培训类 12其他
     **/
    BUILDING(0, "工程类"),
    ORDER(1, "秩序类(保安)"),
    ENVIRONMENT(2,"环境类(保洁、绿化、垃圾清运、不含化粪池清掏)"),
    FIRE(3,"消防维保"),
    ELEVATOR(4,"电梯维保"),
    EQUIPMENT(5,"设备设施维修"),
    CASE(6,"案场类"),
    INTELLIGENCE(7,"智能化、信息化"),
    RENTAL(8,"房屋租赁类"),
    LABOR(9,"劳务派遣类"),
    ADDITION(10,"增值类"),
    TRAINING(11,"猎头委托、培训类"),
    OTHER(12,"其他");

    private Integer code;
    private String name;

    SettlementClassifyEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
