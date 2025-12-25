package com.wishare.finance.infrastructure.configs;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 短信配置
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = SmsConfigProperties.PREFIX)
public class SmsConfigProperties {

    public static final String PREFIX = "sms";


    //中交收据作废通知模板
    private Long receiptTemplateVoidId = 167326536666010l;



}
