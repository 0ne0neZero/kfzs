package com.wishare.contract.apps.remote.fo;

import io.swagger.annotations.ApiModel;
import lombok.Data;


/**
 * @description:
 * @author: zhangfuyu
 * @Date: 2024/5/8/10:24
 */
@Data
@ApiModel("发票信息参数")
public class InvoiceBaseInfoF {

    /**
     * 发票号码
     */
    private String invoicenumber;

    /**
     * 发票代码
     */
    private String invoicecode;
}
