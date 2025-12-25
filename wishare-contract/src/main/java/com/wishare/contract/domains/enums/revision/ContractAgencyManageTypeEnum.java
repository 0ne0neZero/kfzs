package com.wishare.contract.domains.enums.revision;

/**
 * @author hhb
 * @describe
 * @date 2025/11/5 11:11
 */
public enum ContractAgencyManageTypeEnum {

    代建类(1,"代建类","JG"),
    工程类(2,"工程类","JG"),
    租赁类(3,"租赁类","JG"),
    设计类(4,"设计类","JG"),
    咨询服务类(5,"咨询服务类","JG"),
    营销类(6,"营销类","JG"),
    材料设备类(7,"材料设备类","JG"),
    人力资源类(8,"人力资源类","JG"),
    其他类(9,"其他类","JG")
    ;

    private Integer code;
    private String name;
    private String abbrCode;

    ContractAgencyManageTypeEnum(Integer code, String name,String abbrCode) {
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
        for (ContractAgencyManageTypeEnum value : ContractAgencyManageTypeEnum.values()) {
            if (value.getCode().equals(code)) {
                return value.getName();
            }
        }
        return null;
    }

    public static String parseAbbrCode(Integer code) {
        if (code == null){
            return null;
        }
        for (ContractAgencyManageTypeEnum value : ContractAgencyManageTypeEnum.values()) {
            if (value.getCode().equals(code)) {
                return value.getAbbrCode();
            }
        }
        return null;
    }

    public static Integer parseCode(String name) {
        for (ContractAgencyManageTypeEnum value : ContractAgencyManageTypeEnum.values()) {
            if (value.getName().equals(name)) {
                return value.getCode();
            }
        }
        return null;
    }

}
