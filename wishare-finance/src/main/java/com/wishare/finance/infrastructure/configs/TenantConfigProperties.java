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
@ConfigurationProperties(prefix = TenantConfigProperties.PREFIX)
public class TenantConfigProperties {

    public static final String PREFIX = "tenant.tag";

//    private String name = EnvConst.HUIXIANGYUN;
    /** 只有慧享云研发环境才会有 用来模拟多环境 */
    private String nameTest = "";


    private String baffleCutting = "off";





}
