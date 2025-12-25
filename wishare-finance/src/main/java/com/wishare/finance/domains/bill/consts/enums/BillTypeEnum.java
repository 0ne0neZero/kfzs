package com.wishare.finance.domains.bill.consts.enums;

import cn.hutool.core.util.ObjectUtil;
import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.starter.exception.BizException;

/**
 * 账单类型
 * @author yancao
 */
public enum BillTypeEnum {
    //1应收账单,2预收账单, 3临时收费账单, 4应付账单, 5退款账单, 6付款单, 7收款单
    默认(0,"无对应类型账单"),
    应收账单(1, "应收账单"),
    预收账单(2, "预收账单"),
    临时收费账单(3, "临时账单"),
    应付账单(4, "应付账单"),
    退款账单(5, "退款账单"),
    付款单(6,"付款单"),
    收款单(7,"收款单"),
    ;

    private int code;
    private String value;

    BillTypeEnum(int code, String value) {
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

    public static BillTypeEnum valueOfByCode(int code){
        for (BillTypeEnum value : values()) {
            if (value.equalsByCode(code)){
                return value;
            }
        }
        throw BizException.throw400(ErrorMessage.BILL_TYPE_NOT_SUPPORT.msg());
    }

    public static String valueOfByCodeNoExe(Integer code){
        if (ObjectUtil.isNull(code)){
            return "";
        }
        for (BillTypeEnum value : values()) {
            if (value.equalsByCode(code)){
                return value.getValue();
            }
        }
        return "";
    }

}
