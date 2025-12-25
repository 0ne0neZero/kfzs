package com.wishare.finance.domains.bill.consts.enums.third;


import com.wishare.finance.domains.bill.consts.enums.BillApproveStateEnum;
import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.starter.exception.BizException;

/**
* @description:远洋移动端获取应收或者催缴房间账单数据
* @author: zhenghui
* @date: 2023/2/21 14:41
*/
public enum BillReceivableRoomTypeEnum {

    催缴房间(0, "催缴"),
    应收房间(1, "应收"),
    预收查询房间(2, "预收查询房间"),
    ;

    private int code;
    private String value;

    BillReceivableRoomTypeEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public int getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    public boolean equalsByCode(int code){
        return code == this.code;
    }



}
