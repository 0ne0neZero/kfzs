package com.wishare.finance.domains.voucher.support.zhongjiao.enums;


import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.starter.exception.BizException;

import java.util.Arrays;
import java.util.List;

public enum ZJTriggerEventBillTypeEnum {

    收入确认(1, "收入确认"),
    资金收款(2, "资金收款"),
    对下结算计提(3, "对下结算-计提"),
    对下结算实签(4,"对下结算-实签"),

//    支付申请(4, "支付申请");

    收入确认计提(5,"收入确认计提"),
    收入确认实签(6,"收入确认实签"),
    对下结算计提冲销(7,"对下结算-计提冲销"),
    收入确认计提冲销(8, "收入确认-计提冲销"),
    支付申请(9, "支付申请");
    private int code;
    private String value;

    ZJTriggerEventBillTypeEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    //可上传影像类型
    public static final List<Integer> UPLOAD_TYPE = Arrays.asList(对下结算计提.getCode(), 收入确认计提.getCode(), 收入确认实签.getCode(), 对下结算实签.getCode(), 对下结算计提冲销.getCode(), 收入确认计提冲销.getCode(), 支付申请.getCode());

    //外部系统附件
    public static final List<Integer> EXT_UPLOAD_TYPE = Arrays.asList(收入确认实签.getCode(), 对下结算实签.getCode(), 对下结算计提冲销.getCode(), 收入确认计提冲销.getCode());
    //获取合同与补充协议扫描件类型
    public static final List<Integer> CONTRACT_TYPE_LIST = Arrays.asList(对下结算计提.getCode(), 收入确认实签.getCode(), 对下结算实签.getCode(), 对下结算计提冲销.getCode(), 收入确认计提冲销.getCode());

    /**
     * 来源收入合同的单据
     **/
    public static final List<Integer> INCOME_CONTRACT_FROM_EVENT_TYPE =
            Arrays.asList(收入确认计提.getCode(), 收入确认实签.getCode(), 收入确认计提冲销.getCode());
    /**
     * 来源支出合同的单据
     **/
    public static final List<Integer> PAY_CONTRACT_FROM_EVENT_TYPE =
            Arrays.asList(对下结算计提.getCode(), 对下结算实签.getCode(), 对下结算计提冲销.getCode(), 支付申请.getCode());

    public static ZJTriggerEventBillTypeEnum valueOfByCode(int code){
        for (ZJTriggerEventBillTypeEnum value : values()) {
            if (value.equalsByCode(code)){
                return value;
            }
        }
        throw BizException.throw400(ErrorMessage.VOUCHER_BUSINESS_BILL_TYPE_NOT_SUPPORT.msg());
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
