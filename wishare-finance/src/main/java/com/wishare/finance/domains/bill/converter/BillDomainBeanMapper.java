package com.wishare.finance.domains.bill.converter;

import com.wishare.finance.apps.model.bill.vo.AdvanceBillDetailV;
import com.wishare.finance.domains.bill.dto.ReceivableBillMoreInfoDto;
import com.wishare.finance.domains.bill.dto.TempChargeBillMoreInfoDto;
import com.wishare.finance.domains.bill.entity.AdvanceBill;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.BillOjv;
import com.wishare.starter.Global;
import ma.glasnost.orika.MapperFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Configuration;

/**
 * 账单orika字段映射
 *
 * @author yancao
 */
@Configuration
@AutoConfigureAfter(MapperFactory.class)
public class BillDomainBeanMapper implements CommandLineRunner {

    @Override
    public void run(String... args) {

        Global.mapperFactory.classMap(BillOjv.class, ReceivableBillMoreInfoDto.class)
                .field("billType", "type")
                .byDefault()
                .register();

        Global.mapperFactory.classMap(BillOjv.class, TempChargeBillMoreInfoDto.class)
                .field("billType", "type")
                .byDefault()
                .register();


        Global.mapperFactory.classMap(AdvanceBillDetailV.class, AdvanceBill.class)
                .field("billType", "type")
                .byDefault()
                .register();

    }

}
