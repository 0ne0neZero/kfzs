package com.wishare.finance.apps.service.strategy.receipt;

import com.wishare.finance.domains.invoicereceipt.consts.enums.ReceiptTemplateStyleEnum;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.ReceiptTemplateE;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy
public class ReceiptTenantNHW extends AbReceiptTenant {



    /**
     * 获取模板路劲
     *
     * @param templateE
     * @return
     */
    @Override
    public String getReceiptTemplatePath(ReceiptTemplateE templateE) {
        return ReceiptTemplateStyleEnum.样式三.getTemplatePath();
    }

}
