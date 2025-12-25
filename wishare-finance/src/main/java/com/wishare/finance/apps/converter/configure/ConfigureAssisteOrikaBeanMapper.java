package com.wishare.finance.apps.converter.configure;

import com.wishare.finance.apps.model.configure.chargeitem.vo.BusinessTypeV;
import com.wishare.finance.domains.configure.subject.entity.AssisteBizType;
import com.wishare.starter.Global;
import ma.glasnost.orika.MapperFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Configuration;

/**
 * 辅助核算orika字段映射
 *
 * @author yancao
 */
@Configuration
@AutoConfigureAfter(MapperFactory.class)
public class ConfigureAssisteOrikaBeanMapper implements CommandLineRunner {

    @Override
    public void run(String... args) {
        Global.mapperFactory.classMap(AssisteBizType.class, BusinessTypeV.class)
                .field("parentCode", "fcode")
                .field("orgId", "pkOrg")
                .byDefault()
                .register();
    }

}
