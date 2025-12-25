package com.wishare.finance.apps.model.invoice.invoice.dto;

import com.wishare.finance.domains.invoicereceipt.aggregate.ReceiptA;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.ReceiptTemplateE;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

/**
 * @author xujian
 * @date 2022/9/28
 * @Description:
 */
@Getter
@Setter
@ApiModel("收据详情")
public class ReceiptDetailDtoV1 extends ReceiptDetailDto{

    private ReceiptA receiptA;

    private ReceiptTemplateE receiptTemplateE;
}
