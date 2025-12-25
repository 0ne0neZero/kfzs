package com.wishare.finance.domains.bill.consts.enums;

import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.starter.exception.BizException;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 账单审核后的动作
 * @author yancao
 */
public enum BillApprovedActionEnum {

    退款(1, "退款"),
    转预收(2, "转预收"),
    ;

    private int code;
    private String value;

    BillApprovedActionEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public int getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    public static BillApprovedActionEnum valueOfByCode(int code){
        for (BillApprovedActionEnum value : values()) {
            if (value.equalsByCode(code)){
                return value;
            }
        }
        throw BizException.throw400(ErrorMessage.BILL_APPROVE_STATE_NOT_SUPPORT.msg());
    }


    /**
     * 根据编码列表获取所有的审核状态
     * @param codes
     * @return
     */
    public static List<BillApprovedActionEnum> valueOfByCodes(List<Integer> codes){
        List<BillApprovedActionEnum> states = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(codes)){
            for (Integer code : codes) {
                states.add(valueOfByCode(code)); //因为懒，所以不想写新方法，这是垃圾代码，不要在意这些细节
            }
        }
        return states;
    }

    public boolean equalsByCode(int code){
        return code == this.code;
    }

}
