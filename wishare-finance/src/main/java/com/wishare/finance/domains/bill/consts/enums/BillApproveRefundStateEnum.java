package com.wishare.finance.domains.bill.consts.enums;

/**
 * @author xujian
 * @date 2022/9/8
 * @Description: 退款记录的退款状态：退款状态（0未退款，1退款中，2部分退款，已退款）
 */
public enum BillApproveRefundStateEnum {
    待退款(0,"待退款"),
    退款中(1,"退款中"),
    已退款(2,"已退款"),
    未生效(3,"未生效"),
    ;

    private Integer code;

    private String des;

    BillApproveRefundStateEnum(Integer code, String des) {
        this.code = code;
        this.des = des;
    }

    public Integer getCode() {
        return code;
    }

    public String getDes() {
        return des;
    }

    public boolean equalsByCode(int code){
        return code == this.code;
    }

}
