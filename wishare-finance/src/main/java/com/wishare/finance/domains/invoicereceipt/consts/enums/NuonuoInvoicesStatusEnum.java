package com.wishare.finance.domains.invoicereceipt.consts.enums;

/**
 * @author xujian
 * @date 2022/8/9
 * @Description: 发票状态： 2 :开票完成（ 最终状 态），
 * 其他状态分别为: 20:开票中; 21:开票成功签章中;22:开票失败;24: 开票成功签章失败;3:发票已作废 31:发票作废中
 * 备注：22、24状态时，无需再查询，请确认开票失败原因以及签章失败原因；3、31只针对纸票 注：请以该状态码区分发票状态"
 */
public enum NuonuoInvoicesStatusEnum {

    开票完成(2, "开票完成（ 最终状 态）"),
    开票中(20, "开票中"),
    开票成功签章中(21, "开票成功签章中"),
    开票失败(22, "开票失败"),
    开票成功签章失败(24, "开票成功签章失败"),
    发票已作废(3, "发票已作废"),
    发票作废中(31, "发票作废中"),
    ;

    private Integer code;
    private String des;

    NuonuoInvoicesStatusEnum(Integer code, String des) {
        this.code = code;
        this.des = des;
    }

    public static NuonuoInvoicesStatusEnum getEnum(int code) {
        NuonuoInvoicesStatusEnum e = null;
        for (NuonuoInvoicesStatusEnum ee : NuonuoInvoicesStatusEnum.values()) {
            if (ee.getCode() == code) {
                e = ee;
                break;
            }
        }
        return e;
    }

    public Integer getCode() {
        return code;
    }

    public String getDes() {
        return des;
    }
}

