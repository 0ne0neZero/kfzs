package com.wishare.finance.infrastructure.remote.enums;

import java.util.Arrays;
import java.util.List;

/**
 * @author xujian
 * @date 2022/10/16
 * @Description: 审核操作类型：0 生成审核，1 调整，2 作废，3 结转，4 退款
 */
public enum OperateTypeEnum {

    生成审核(0, "生成审核"),
    调整(1, "调整"),
    作废(2, "作废"),
    结转(3, "结转"),
    退款(4, "退款"),
    冲销(5,"冲销"),
    减免(6,"减免"),
    收款单退款(7, "收款单退款"),
    跳收(8,"跳收"),
    收款单冲销(9,"收款单冲销"),
    资金收款(10,"资金收款"),
//   资金收款 (11, "资金收款"),
    收入确认(12, "收入确认"),
    对下结算(13, "对下结算"),
    通用收入确认单(14, "通用收入确认单"),


    对下结算计提单(20, "对下结算-计提单"),
    收入确认计提单(21, "收入确认-计提单"),
    业务支付申请单(22, "支付申请单"),
    收入确认实签单(23, "收入确认-实签单"),

    支出合同结束NK(24, "支出合同结束NK"),
    收入合同修正(25, "收入合同修正"),

    立项管理成本确认(31, "立项管理成本确认"),
    立项管理慧采下单(32, "立项管理慧采下单"),
    ;

    private Integer code;

    private String des;

    public static final Integer[] APPROVEING_TYPE = new Integer[]{调整.getCode(),作废.getCode(),结转.getCode(),退款.getCode(),
            冲销.getCode(),减免.getCode(),收款单退款.getCode(),跳收.getCode(),收款单冲销.getCode(),资金收款.getCode(),收入确认.getCode(),对下结算.getCode()};


    public static final List<String> APPROVEING_DX_BPM_DES = Arrays.asList(对下结算计提单.getDes(), 收入确认计提单.getDes(), 业务支付申请单.getDes(), 收入确认实签单.getDes());

    public static OperateTypeEnum valueOfByCode(Integer code) {
        OperateTypeEnum e = null;
        for (OperateTypeEnum ee : OperateTypeEnum.values()) {
            if (ee.getCode() == code) {
                e = ee;
                break;
            }
        }
        return e;
    }

    OperateTypeEnum(Integer code, String des) {
        this.code = code;
        this.des = des;
    }

    public Integer getCode() {
        return code;
    }

    public String getDes() {
        return des;
    }
}
