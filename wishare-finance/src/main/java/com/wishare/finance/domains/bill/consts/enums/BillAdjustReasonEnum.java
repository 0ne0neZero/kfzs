package com.wishare.finance.domains.bill.consts.enums;


import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.starter.exception.BizException;

/**
 * 调整原因
 * @Author dxclay
 * @Date 2022/8/24
 * @Version 1.0
 */
public enum BillAdjustReasonEnum {

    物业服务(1, "物业服务终止、失联"),
    房屋变化(2, "破产或房屋拍卖、易主房屋"),
    房屋质量(3, "销售承诺、房屋质量问题"),
    服务质量(4, "服务质量瑕疵"),
    法规变化(5, "法院判决类、属地法规规定的空置减免类"),
    收费对象变更(7, "收费对象变更"),
    其他(99, "其他"),
    OVERDUE_REASON_1(19, "违约金配置错误"),
    OVERDUE_REASON_2(20, "应收日配置错误")
    ;

    private int code;
    private String value;

    BillAdjustReasonEnum(int code, String value) {
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

    public static BillAdjustReasonEnum valueOfByCode(int code){
        for (BillAdjustReasonEnum value : values()) {
            if (value.equalsByCode(code)){
                return value;
            }
        }
        return null;
    }

}
