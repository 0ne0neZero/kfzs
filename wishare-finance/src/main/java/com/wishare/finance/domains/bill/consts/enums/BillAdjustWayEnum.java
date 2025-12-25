package com.wishare.finance.domains.bill.consts.enums;


import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.starter.exception.BizException;

import java.util.Set;
import java.util.function.Predicate;

/**
 * 调整方式：1应收调整-单价，2应收调整-应收金额，3应收调整-实测面积，4实收调整-实测面积，5实收调整-空置房打折，6实收调整-优惠券，7实收调整-开发减免，8实收调整-其他
 * @Author dxclay
 * @Date 2022/8/23
 * @Version 1.0
 */
public enum BillAdjustWayEnum {

    RECEIVABLE_PRICE(1, "调整-单价"),
    RECEIVABLE_AMOUNT(2, "调整-账单金额"),
    RECEIVABLE_AREA(3, "调整-实测面积"),
    ADJUST_NUM(11, "调整-数量"),
    ADJUST_DEGREE(12, "调整-度数"),
    //ACTUAL_PRICE(4, "实收调整-实测面积"),
    ACTUAL_VACANT_HOUSE_DISCOUNT(5, "减免-空置房减免"),
    ACTUAL_COUPON(6, "减免-优惠券减免"),
    ACTUAL_REDUCE(7, "减免-开发减免"),
    ACTUAL_OTHER(8, "减免-其他"),
    LIQUIDATED_DAMAGES(13, "减免-违约金减免"),
    PAYMENT_ON_BEHALF (15,  "调整-开发代付"),
    ADJUST_ODD (16,  "减免-抹零减免"),
    ADJUST_PREFERENTIAL_RULE (17,  "应收减免-优惠"),
    ADJUST_PRESENT_RULE (18,  "应收减免-赠送"),
    ADJUST_CARRYOVER (19,  "减免-结转"),
    TAXAMOUNT_ADJUST(9,"税额调整"),
    CHARGE_OBJECT(10,"收费对象调整"),
    ADJUST_TAX_RATE (20,  "调整-税率"),
    ADJUST_OVERDUE_RATE (21,  "调整-违约金比率"),
    ADJUST_OVERDUE_BEGIN_DATE (22, "调整-违约金起算日期"),
    ADJUST_RECEIVABLE_DATE (23,  "调整-应收日"),
    ADJUST_CHARGE_ITEM (24,  "调整-费项")
    ;

    private int code;
    private String value;

    BillAdjustWayEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public static BillAdjustWayEnum valueOfByCode(int code){
        for (BillAdjustWayEnum value : values()) {
            if (value.equalsByCode(code)){
                return value;
            }
        }
        throw BizException.throw400(ErrorMessage.BILL_ADJUST_WAY_NOT_SUPPORT.msg());
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

    public final static Set<BillAdjustWayEnum> ADJUST_NO_AMOUNT =
            Set.of(
                    ADJUST_TAX_RATE,
                    CHARGE_OBJECT,
                    ADJUST_OVERDUE_BEGIN_DATE,
                    ADJUST_OVERDUE_RATE,
                    ADJUST_RECEIVABLE_DATE,
                    ADJUST_CHARGE_ITEM
            );

    public final static Set<BillAdjustWayEnum> ADJUST_OVERDUE =
            Set.of(ADJUST_OVERDUE_BEGIN_DATE, ADJUST_OVERDUE_RATE, ADJUST_RECEIVABLE_DATE);

    public final static Predicate<BillAdjustWayEnum> IS_ADJUST_OVERDUE = ADJUST_OVERDUE::contains;
    public final static Predicate<BillAdjustWayEnum> IS_NO_AMOUNT_ADJUST = ADJUST_NO_AMOUNT::contains;
}
