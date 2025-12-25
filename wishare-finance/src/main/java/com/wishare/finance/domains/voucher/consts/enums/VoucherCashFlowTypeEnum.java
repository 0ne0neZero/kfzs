package com.wishare.finance.domains.voucher.consts.enums;

/**
 * 凭证现金流方向
 * @author dxclay
 * @since  2023/3/17
 * @version 1.0
 */
public enum VoucherCashFlowTypeEnum {

    流入(1, "流入"),
    流出(2, "流出"),

    ;

    private int code;
    private String des;

    VoucherCashFlowTypeEnum(int code, String des) {
        this.code = code;
        this.des = des;
    }

    public boolean equalsByCode(int code){
        return this.code == code;
    }

    public int getCode() {
        return code;
    }

    public String getDes() {
        return des;
    }

}
