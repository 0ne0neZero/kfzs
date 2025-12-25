package com.wishare.contract.apps.remote.finance.enums;

/**
 * @author xujian
 * @date 2023/2/9
 * @Description: 收款方式   0 现金  1 网上转账  2 支付宝  3 微信
 */
public enum GatherChannelEnum {

    现金(0,"现金","CASH", "现金"),
    网上转账(1,"网上转账","OTHER", "其他"),//不知道对应什么 暂时对应其他
    支付宝(2,"支付宝","ALIPAY", "支付宝"),
    微信(3,"微信","WECHATPAY", "微信"),

    ;

    private Integer contractCode;

    private String contractDes;
    private String financeCode;
    private String value;

    GatherChannelEnum(Integer contractCode, String contractDes, String financeCode, String value) {
        this.contractCode = contractCode;
        this.contractDes = contractDes;
        this.financeCode = financeCode;
        this.value = value;
    }

    public Integer getContractCode() {
        return contractCode;
    }

    public String getFinanceCode() {
        return financeCode;
    }

    public String getValue() {
        return value;
    }

    public String getContractDes() {
        return contractDes;
    }

    public static GatherChannelEnum getEnum(Integer contractCode) {
        GatherChannelEnum e = null;
        for (GatherChannelEnum ee : GatherChannelEnum.values()) {
            if (ee.getContractCode() == contractCode) {
                e = ee;
                break;
            }
        }
        return e;
    }
}
