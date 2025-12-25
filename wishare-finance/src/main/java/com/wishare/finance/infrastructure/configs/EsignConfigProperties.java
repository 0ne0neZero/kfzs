package com.wishare.finance.infrastructure.configs;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 *
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = EsignConfigProperties.PREFIX)
public class EsignConfigProperties {

    public static final String PREFIX = "esign";

    /**
     * 默认的主体企业的法定单位ID
     */
    private String orgFinanceId= "123456";

    /**
     * 默认的主体企业的法定单位名称
     */
    private String defaultOrgFinanceName = "123456";



}
