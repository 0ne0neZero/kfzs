package com.wishare.finance.infrastructure.remote.enums;

import com.wishare.finance.domains.invoicereceipt.consts.enums.InvoiceReceiptStateEnum;

/**
 * @author xujian
 * @date 2022/9/22
 * @Description: 发票状态： 2 :开票完成（ 最终状 态），其他状态
 * 分别为: 20:开票中; 21:开票成功签章中;22:开票失
 * 败;24: 开票成功签章失败;3:发票已作废 31: 发票作
 * 废中 备注：22、24状态时，无需再查询，请确认
 * 开票失败原因以及签章失败原因； 注：请以该状
 * 态码区分发票状态
 */
public enum NuonuoInvoiceStatusEnum {

    开票完成(2, "开票完成", InvoiceReceiptStateEnum.开票成功.getCode()),
    开票中(20, "开票中",InvoiceReceiptStateEnum.开票中.getCode()),
    开票成功签章中(21, "开票成功签章中",InvoiceReceiptStateEnum.开票中.getCode()),
    开票失败(22, "开票失败",InvoiceReceiptStateEnum.开票失败.getCode()),
    开票成功签章失败(24, "开票成功签章失败",InvoiceReceiptStateEnum.开票成功签章失败.getCode()),
    发票已作废(3, "发票已作废",InvoiceReceiptStateEnum.已作废.getCode()),
    发票作废中(31, "发票作废中",InvoiceReceiptStateEnum.开票中.getCode()),
    ;

    private Integer code;

    private String des;

    private Integer invoicingState;

    public static NuonuoInvoiceStatusEnum valueOfByCode(Integer code) {
        NuonuoInvoiceStatusEnum e = null;
        for (NuonuoInvoiceStatusEnum ee : NuonuoInvoiceStatusEnum.values()) {
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

    public Integer getInvoicingState() {
        return invoicingState;
    }

    NuonuoInvoiceStatusEnum(Integer code, String des, Integer invoicingState) {
        this.code = code;
        this.des = des;
        this.invoicingState = invoicingState;
    }
}
