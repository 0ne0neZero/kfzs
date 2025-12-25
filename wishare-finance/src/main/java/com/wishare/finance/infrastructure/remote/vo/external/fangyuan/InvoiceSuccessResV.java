package com.wishare.finance.infrastructure.remote.vo.external.fangyuan;

import lombok.Data;


/**
 * 方圆开票反参参数
 * @author dongpeng
 * @date 2023/7/4 20:17
 */
@Data
public class InvoiceSuccessResV {
    private String code;
    private String mess;
    private InvoiceResDatalV data;
    private String data2;
    private String data3;
}
