package com.wishare.finance.apis.health;

import com.wishare.starter.enums.PromptInfo;
import io.swagger.annotations.Api;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Copyright (C), huishen
 * <author>          <time>                    <desc>
 * huishen           2023/12/28 22:43          健康检查接口
 */
@RestController
@Validated
@Api(tags = {"健康检测"})
@RequestMapping("/health")
public class HealthInfoApi {
    @GetMapping("/info")
    public String info() {
        return PromptInfo.OK.info;
    }

    @GetMapping("/updateInfo")
    public String updateInfo() {
        return "20240118-21:47";
    }
}

