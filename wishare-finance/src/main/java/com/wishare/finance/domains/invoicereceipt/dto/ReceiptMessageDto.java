package com.wishare.finance.domains.invoicereceipt.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author luzhonghe
 * @version 1.0
 * @since 2023/4/10
 */
@Getter
@Setter
public class ReceiptMessageDto {

    /**
     * 发票收据主表id
     */
    private Long invoiceReceiptId;
    /**
     * pdf url
     */
    private String receiptUrl;

}
