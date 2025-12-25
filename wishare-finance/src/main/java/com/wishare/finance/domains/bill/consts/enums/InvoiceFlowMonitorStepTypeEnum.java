package com.wishare.finance.domains.bill.consts.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 调整明细中的状态
 * @Author dxclay
 * @Date 2022/8/23
 * @Version 1.0
 */
@AllArgsConstructor
@Getter
public enum InvoiceFlowMonitorStepTypeEnum {

    STEP_INIT(1, "初步(开票、开具)参数留存"),
    STEP_SIGN(2, "发起e签宝开具"),
    STEP_VOID(3, "(定时任务内置)进行发起e签宝作废"),
    STEP_WAKE_UP_INVOICING(4, "(定时任务内置)唤起开票"),
    STEP_END(5, "开票完成，开票id回填"),
    STEP_WAKE_UP_RECEIPT(6, "(定时任务内置)唤起开据"),
    STEP_SIGN_RECEIPT_ERROR(7, "电子收据签署失败"),
    ;

    private int code;
    private String value;


}
