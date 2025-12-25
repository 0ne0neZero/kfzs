package com.wishare.finance.domains.invoicereceipt.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 发票发邮件短信所需信息
 * @author luzhonghe
 * @version 1.0
 * @since 2023/4/3
 */
@Getter
@Setter
public class InvoiceMessageDto {

    /**
     * 发票收据主表id
     */
    private Long invoiceReceiptId;
    /**
     * 诺诺pdf url
     */
    private String nuonuoUrl;
    /**
     * 发票代码
     */
    private String invoiceCode;
    /**
     * 发票号码
     */
    private String invoiceNo;

}
