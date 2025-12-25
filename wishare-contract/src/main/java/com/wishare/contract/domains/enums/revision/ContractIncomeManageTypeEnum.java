package com.wishare.contract.domains.enums.revision;

/**
 * @version 1.0.0
 * @Description：
 * @Author： chenglong
 * @since： 2023/6/25  14:50
 */
public enum ContractIncomeManageTypeEnum {

    基础物管类餐饮(3,"基础物管类/餐饮/案场","JF"),
    增值类合同(4,"增值类合同","ZF"),
    增值类合同含餐饮(5,"增值类合同（限餐饮、资产）","ZF");

    private Integer code;
    private String name;
    private String abbrCode;

    ContractIncomeManageTypeEnum(Integer code, String name,String abbrCode) {
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
        for (ContractIncomeManageTypeEnum value : ContractIncomeManageTypeEnum.values()) {
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
        for (ContractIncomeManageTypeEnum value : ContractIncomeManageTypeEnum.values()) {
            if (value.getCode().equals(code)) {
                return value.getAbbrCode();
            }
        }
        return null;
    }

    public static Integer parseCode(String name) {
        for (ContractIncomeManageTypeEnum value : ContractIncomeManageTypeEnum.values()) {
            if (value.getName().equals(name)) {
                return value.getCode();
            }
        }
        return null;
    }



}
