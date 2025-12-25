package com.wishare.finance.infrastructure.conts.enumTable.receipt;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReceiptSendStatusEnum {



    //       @ApiModelProperty(value = "是否需要发信息 1：需要，2：已发送 ,3:发送失败 4:不需要")
    need(1, "需要"),
    hadSend(2, "已发送"),
    FailSend(3, "发送失败"),

    no(4, "不需要"),


;
    private int code;
    private String desc;




}
