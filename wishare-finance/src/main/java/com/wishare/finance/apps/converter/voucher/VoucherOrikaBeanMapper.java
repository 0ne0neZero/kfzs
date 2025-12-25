package com.wishare.finance.apps.converter.voucher;

import com.wishare.finance.domains.voucher.entity.VoucherRule;
import com.wishare.starter.Global;
import ma.glasnost.orika.MapperFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Configuration;

@Configuration
@AutoConfigureAfter(MapperFactory.class)
public class VoucherOrikaBeanMapper implements CommandLineRunner {

    @Override
    public void run(String... args) {
        registerReconciliationInvoice();
    }

    /**
     * 注册对账开票值对象映射
     */
    private void registerReconciliationInvoice(){

        Global.mapperFactory.classMap(VoucherRule.class, VoucherRule.class)
                .mapNulls(false)
                .byDefault()
                .register();

    }

}
