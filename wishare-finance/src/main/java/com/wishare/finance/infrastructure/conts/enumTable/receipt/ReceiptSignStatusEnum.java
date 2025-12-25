package com.wishare.finance.infrastructure.conts.enumTable.receipt;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReceiptSignStatusEnum {

    //    @ApiModelProperty(value = "0 - 申请签署 1 - 签署中  - 已完成（所有签署方完成签署） 3 - 已撤销（发起方撤销签署任务）5 - 已过期（签署截止日到期后触发） 7 - 已拒签（签署方拒绝签署） 8 - 未知（调用服务异常等）")
    apply(0, "申请签署"),
    signing(1, "签署中"),
    completeSigning(2, "已完成（所有签署方完成签署）"),

    unKnow(8, "未知（调用服务异常等）"),
;
    private int code;
    private String desc;




}
