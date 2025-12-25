package com.wishare.finance.infrastructure.support;

import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 应用信息
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/4/5
 */
@Configuration
public class Application {

    public static String name;
    public static String service;

    @Value("${wishare.application.service:${spring.application.name}}")
    public void setService(String service) {
        Application.service = StrUtil.trim(service);
    }

    @Value("${wishare.application.name:财务中台}")
    public void setName(String name) {
        Application.name = StrUtil.trim(name);
    }
}
