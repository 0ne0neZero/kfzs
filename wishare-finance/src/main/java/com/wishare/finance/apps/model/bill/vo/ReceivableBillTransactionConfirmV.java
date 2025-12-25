package com.wishare.finance.apps.model.bill.vo;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 支付结果确认响应参数
 *
 * @Author dxclay
 * @Date 2022/12/19
 * @Version 1.0
 */
@Getter
@Setter
@ApiModel("支付结果确认响应参数")
public class ReceivableBillTransactionConfirmV {

    private String billId;

    private List<String> billIds;

    private boolean success;

    public ReceivableBillTransactionConfirmV() {
    }

    public ReceivableBillTransactionConfirmV(String billId, List<String> billIds, boolean success) {
        this.billId = billId;
        this.billIds = billIds;
        this.success = success;
    }
}
