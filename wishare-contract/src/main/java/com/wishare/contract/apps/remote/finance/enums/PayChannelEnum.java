package com.wishare.contract.apps.remote.finance.enums;

/**
 * @author xujian
 * @date 2023/2/9
 * @Description: 付款方式  0 现金  1 银行转帐  2 汇款  3 支票
 */
public enum PayChannelEnum {

    现金(0, "现金","CASH", "现金"),
    银行转帐(1, "银行转帐","UNIONPAY", "银联"),
    汇款(2, "汇款","BANK", "银行汇款"),
    支票(3, "支票","OTHER", "其他"),
    ;

    //合同的收付款方式
    private Integer contractCode;

    private String contractDes;
    private String financeCode;
    private String value;

    PayChannelEnum(Integer contractCode, String contractDes, String financeCode, String value) {
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

    public static PayChannelEnum getEnum(Integer contractCode) {
        PayChannelEnum e = null;
        for (PayChannelEnum ee : PayChannelEnum.values()) {
            if (ee.getContractCode() == contractCode) {
                e = ee;
                break;
            }
        }
        return e;
    }
}
