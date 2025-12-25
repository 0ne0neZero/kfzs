package com.wishare.finance.infrastructure.support.yuanyang;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 远洋科目配置模块
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/3/16
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "wishare.finance.yuanyang.rate")
public class YuanYangTaxRateProperties {

    /**
     * 增值税税率编码
     */
    private String taxCategoryCode = "0011";

}
