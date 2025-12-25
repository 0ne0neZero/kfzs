package com.wishare.finance.apps.model.bill.vo;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

/**
 * 预支付结果信息
 *
 * @Author dxclay
 * @Date 2022/12/19
 * @Version 1.0
 */
@Getter
@Setter
@ApiModel("预支付响应参数")
public class ReceivableBillPreTransactionV {

    private String preTradeId;

    public ReceivableBillPreTransactionV() {
    }

    public ReceivableBillPreTransactionV(String preTradeId) {
        this.preTradeId = preTradeId;
    }
}
