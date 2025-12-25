package com.wishare.contract.domains.enums.revision;

/**
 * @author hhb
 * @describe
 * @date 2025/11/5 11:11
 */
public enum ContractBusinessManageTypeEnum {

    材料设备类(1,"材料设备类","SG"),
    工程服务类(2,"工程服务类","SG"),
    工程类(3,"工程类","SG"),
    设计服务类(4,"设计服务类","SG"),
    综合措施类(5,"综合措施类","SG"),
    第三方造价咨询类(6,"第三方造价咨询类","SG"),
    第三方优化设计类(7,"第三方优化设计类","SG"),
    土地拓展类(8,"土地拓展类","SG"),
    报批报建类(9,"报批报建类","SG"),
    商业服务类(10,"商业服务类","SG"),
    其他类(11,"其他类","SG");

    private Integer code;
    private String name;
    private String abbrCode;

    ContractBusinessManageTypeEnum(Integer code, String name,String abbrCode) {
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
        for (ContractBusinessManageTypeEnum value : ContractBusinessManageTypeEnum.values()) {
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
        for (ContractBusinessManageTypeEnum value : ContractBusinessManageTypeEnum.values()) {
            if (value.getCode().equals(code)) {
                return value.getAbbrCode();
            }
        }
        return null;
    }

    public static Integer parseCode(String name) {
        for (ContractBusinessManageTypeEnum value : ContractBusinessManageTypeEnum.values()) {
            if (value.getName().equals(name)) {
                return value.getCode();
            }
        }
        return null;
    }
}
