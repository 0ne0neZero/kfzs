package com.wishare.finance.apps.converter.invoicereceipt;

import com.wishare.finance.apps.model.invoice.invoice.dto.InvoiceAndReceiptDto;
import com.wishare.finance.apps.model.invoice.invoice.dto.InvoiceAndReceiptStatisticsDto;
import com.wishare.finance.apps.model.invoice.invoice.vo.ChargeInvoiceAndReceiptV;
import com.wishare.finance.apps.model.invoice.invoice.vo.ContractInvoiceAndReceiptV;
import com.wishare.finance.apps.model.invoice.invoice.vo.InvoiceAndReceiptStatisticsV;
import com.wishare.finance.apps.model.reconciliation.vo.FlowInvoiceDetailV;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceReceiptE;
import com.wishare.starter.Global;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

/**
 * 账单orika字段映射
 *
 * @author yancao
 */
@Configuration
@AutoConfigureAfter(MapperFactory.class)
public class invoiceOrikaBeanMapper implements CommandLineRunner {

    @Override
    public void run(String... args) {

        //发票扩展字段1映射
        Global.mapperFactory.classMap(InvoiceAndReceiptDto.class, ContractInvoiceAndReceiptV.class).byDefault().customize(
                new CustomMapper<>() {
                    @Override
                    public void mapAtoB(InvoiceAndReceiptDto dto, ContractInvoiceAndReceiptV vo, MappingContext context) {
                        vo.setContractName(dto.getExtendFieldOne());
                    }
                }).register();

    }

}
