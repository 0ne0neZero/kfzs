package com.wishare.contract.domains.enums.revision;

/**
 * @version 1.0.0
 * @Description：
 * @Author： chenglong
 * @since： 2023/6/25  14:50
 */
public enum ContractAreaEnum {

    总部(0,"总部",""),
    区域公司(1,"区域公司",""),
    华北区域(2,"华北区域","HBQY"),
    华南区域(3,"华南区域","HNQY"),
    华东区域(4,"华东区域","HDQY"),
    西部区域(5,"西部区域","XBQY"),
    华中区域(6,"华中区域","HZQY"),
    商写事业部(7,"商写事业部",""),
            ;

    private Integer code;
    private String name;
    private String abbrCode;

    ContractAreaEnum(Integer code, String name,String abbrCode) {
        this.name = name;
        this.code = code;
        this.abbrCode = abbrCode;
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

    public String getAbbrCode() {
        return abbrCode;
    }

    public static String parseName(Integer code) {
        for (ContractAreaEnum value : ContractAreaEnum.values()) {
            if (value.getCode().equals(code)) {
                return value.getName();
            }
        }
        return null;
    }

    public static String parseAbbrCode(Integer code){
        if (code == null){
            return null;
        }
        for (ContractAreaEnum value : ContractAreaEnum.values()) {
            if (value.getCode().equals(code)) {
                return value.getAbbrCode();
            }
        }
        return null;
    }

    public static Integer parseCode(String name) {
        for (ContractAreaEnum value : ContractAreaEnum.values()) {
            if (value.getName().equals(name)) {
                return value.getCode();
            }
        }
        return null;
    }

}
