package com.wishare.finance.apps.converter.reconciliation;

import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceReceiptE;
import com.wishare.finance.domains.reconciliation.entity.ReconcileRuleE;
import com.wishare.finance.domains.reconciliation.entity.ReconciliationInvoiceDetailOBV;
import com.wishare.starter.Global;
import ma.glasnost.orika.MapperFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Configuration;

@Configuration
@AutoConfigureAfter(MapperFactory.class)
public class ReconciliationOrikaBeanMapper implements CommandLineRunner {

    @Override
    public void run(String... args) {
        registerReconciliationInvoice();
    }

    /**
     * 注册对账开票值对象映射
     */
    private void registerReconciliationInvoice(){
        Global.mapperFactory.classMap(InvoiceReceiptE.class, ReconciliationInvoiceDetailOBV.class)
                .field("invoiceReceiptNo", "invoiceNo")
                .field("priceTaxAmount", "invoiceAmount")
                .field("priceTaxAmount", "receiptAmount")
                .field("type", "invoiceType")
                .field("billingTime", "invoiceTime")
                .byDefault()
                .register();

        Global.mapperFactory.classMap(ReconcileRuleE.class, ReconcileRuleE.class)
                .mapNulls(false)
                .byDefault()
                .register();

    }

}
