package com.wishare.contract.domains.consts;

/**
 * @author xujian
 * @date 2023/2/7
 * @Description: 合同对接bpm各增值业务类型流程，枚举
 */
public enum ContractAddValueEnum {

    资产销售("资产销售"),
    资产增值("资产增值"),
    社区生活("社区生活"),
    商品零售("商品零售"),
    美居服务("美居服务"),
    餐饮服务("餐饮服务"),
    到家服务("到家服务"),
    ;

    private String code;

    public static ContractAddValueEnum getEnum(String code) {
        ContractAddValueEnum e = null;
        for (ContractAddValueEnum ee : ContractAddValueEnum.values()) {
            if (ee.getCode().equals(code)) {
                e = ee;
                break;
            }
        }
        return e;
    }

    ContractAddValueEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
