package com.wishare.finance.infrastructure.configs;

import com.wishare.bizlog.logger.BizLogConfiguration;
import com.wishare.bizlog.plugins.logid.LoggerIdBizLoggerInterceptor;
import com.wishare.bizlog.plugins.logid.LoggerIdentifierHandler;
import com.wishare.bizlog.plugins.tenent.TenantBizLogInterceptor;
import com.wishare.bizlog.starter.mybatisplus.config.EnableBizLogAutoConfiguration;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import com.wishare.finance.infrastructure.support.ApiData;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 业务日志配置
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/1/9
 */
@Configuration
@EnableBizLogAutoConfiguration
public class BizlogConfig {

    @Bean
    public TenantBizLogInterceptor tenantBizLogInterceptor(BizLogConfiguration bizLogConfiguration){
        //租户插件
        TenantBizLogInterceptor tenantBizLogInterceptor = new TenantBizLogInterceptor(() -> ApiData.API.getTenantId().get());
        bizLogConfiguration.addInterceptor(tenantBizLogInterceptor);
        return tenantBizLogInterceptor;
    }

    @Bean
    public LoggerIdBizLoggerInterceptor loggerIdBizLoggerInterceptor(){
        return new LoggerIdBizLoggerInterceptor(new LoggerIdentifierHandler() {
            @Override
            public String generateLogId() {
                return IdentifierFactory.getInstance().generateStrIdentifier("bizlog");
            }
        });
    }

}
