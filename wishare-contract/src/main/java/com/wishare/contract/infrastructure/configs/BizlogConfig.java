package com.wishare.contract.infrastructure.configs;

//import com.wishare.amp.finance.infrastructure.support.ApiData;
//import com.wishare.amp.finance.infrastructure.support.AppIdentifierFactory;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.wishare.bizlog.logger.BizLogConfiguration;
import com.wishare.bizlog.plugins.logid.LoggerIdBizLoggerInterceptor;
import com.wishare.bizlog.plugins.logid.LoggerIdentifierHandler;
import com.wishare.bizlog.plugins.tenent.TenantBizLogInterceptor;
import com.wishare.bizlog.starter.mybatisplus.config.EnableBizLogAutoConfiguration;
import com.wishare.contract.domains.consts.contractset.ContractConcludeFieldConst;
import com.wishare.owl.enhance.IOwlApiBase;
import com.wishare.starter.helpers.UidHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 业务日志配置
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/1/9
 */
@EnableBizLogAutoConfiguration
@Configuration
public class BizlogConfig implements IOwlApiBase {

    @Bean
    public TenantBizLogInterceptor tenantBizLogInterceptor(BizLogConfiguration bizLogConfiguration){
        //租户插件
        TenantBizLogInterceptor tenantBizLogInterceptor = new TenantBizLogInterceptor(() -> tenantId());
        bizLogConfiguration.addInterceptor(tenantBizLogInterceptor);
        return tenantBizLogInterceptor;
    }

    @Bean
    public LoggerIdBizLoggerInterceptor loggerIdBizLoggerInterceptor(){
        return new LoggerIdBizLoggerInterceptor(new LoggerIdentifierHandler() {
            @Override
            public String generateLogId() {
                return UidHelper.nextIdStr("bizlog");
            }
        });
    }

}
