package com.wishare.contract.domains.enums.revision;

/**
 * @version 1.0.0
 * @Description：
 * @Author： chenglong
 * @since： 2023/6/25  14:50
 */
public enum ContractPayManageTypeEnum {

    工程类(0,"工程类","GC"),
    服务类维修(1,"服务类/维保类/其他类","FW"),
    劳务派遣类(2,"劳务派遣类","LP"),
    增值类(3,"增值类","ZF"),
    智能化信息化(4,"智能化、信息化","XX"),
    猎头委托培训类(5,"猎头委托、培训类","PX"),
    其他(6,"其他","QT");

    private Integer code;
    private String name;
    private String abbrCode;

    ContractPayManageTypeEnum(Integer code, String name, String abbrCode) {
        this.name = name;
        this.code = code;
        this.abbrCode = abbrCode;
    }

    public String getAbbrCode() {
        return abbrCode;
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
        for (ContractPayManageTypeEnum value : ContractPayManageTypeEnum.values()) {
            if (value.getCode().equals(code)) {
                return value.getName();
            }
        }
        return null;
    }

    public static String parseAbbrCode(Integer code) {
        for (ContractPayManageTypeEnum value : ContractPayManageTypeEnum.values()) {
            if (value.getCode().equals(code)) {
                return value.getAbbrCode();
            }
        }
        return null;
    }

    public static Integer parseCode(String name) {
        for (ContractPayManageTypeEnum value : ContractPayManageTypeEnum.values()) {
            if (value.getName().equals(name)) {
                return value.getCode();
            }
        }
        return null;
    }

}
