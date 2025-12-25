package com.wishare.contract.apps.remote.vo;

import lombok.Data;

@Data
public class InvoiceInfoRv {

    /**
     * 失败原因
     */
    private String failReason;

    /**
     * 发票id
     */
    private Long id;

    /**
     * 发票代码
     */
    private String invoiceCode;

    /**
     * 发票号码
     */
    private String invoiceNo;

    /**
     * 发票url
     */
    private String invoiceUrl;

    /**
     * 诺诺pdf地址
     */
    private String nuonuoUrl;

    /**
     * 开票状态 1 开票中 2 开票成功 3 开票失败 4 红冲中 5 已红冲 6 已作废
     */
    private Integer state;
    private Integer invoiceStatus;

    /**
     * 系统来源：1 收费系统 2合同系统
     */
    private Integer sysSource;
}
