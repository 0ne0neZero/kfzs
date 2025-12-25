package com.wishare.finance.apps.converter.expensereport;

import com.wishare.finance.apps.model.invoice.invoice.dto.InvoiceAndReceiptDto;
import com.wishare.finance.apps.model.invoice.invoice.vo.ContractInvoiceAndReceiptV;
import com.wishare.finance.domains.expensereport.entity.ExpenseReportDetailE;
import com.wishare.finance.infrastructure.remote.fo.external.kingdee.KingDeeRecBillEntry;
import com.wishare.finance.infrastructure.utils.AmountUtils;
import com.wishare.starter.Global;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Configuration;

@Configuration
@AutoConfigureAfter(MapperFactory.class)
public class ExpenseReportOrikaBeanMapper implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        Global.mapperFactory.classMap(ExpenseReportDetailE.class, KingDeeRecBillEntry.class).byDefault().customize(
                new CustomMapper<>() {
                    @Override
                    public void mapAtoB(ExpenseReportDetailE expenseReportDetail, KingDeeRecBillEntry entry, MappingContext context) {
                        entry.setTotamount(AmountUtils.toStringAmount(expenseReportDetail.getTotalAmount()));
                        entry.setAmount(AmountUtils.toStringAmount(expenseReportDetail.getAmount()));
                        entry.setTaxamount(AmountUtils.toStringAmount(expenseReportDetail.getTaxAmount()));
                        entry.setTaxrate(expenseReportDetail.getTaxRate());
                        entry.setFeeitem(expenseReportDetail.getChargeItemCode());
                        entry.setYewubk(expenseReportDetail.getBusinessSegmentCode());
                        entry.setTaxprice(entry.getTotamount());
                        entry.setPrice(entry.getAmount());
                    }
                }).register();
    }
}
