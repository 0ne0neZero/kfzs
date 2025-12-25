package com.wishare.finance.infrastructure.remote.clients.base;

import com.wishare.finance.apps.pushbill.vo.FinanceCommonD;
import com.wishare.finance.apps.pushbill.vo.PaymentMethodV;
import com.wishare.finance.infrastructure.remote.vo.common.FinanceV;
import com.wishare.starter.annotations.OpenFeignClient;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 财务云字典
 */
@OpenFeignClient(name = "financial-cloud", serverName = "财务云服务", url="${financial.cloud:http://10.35.58.87:6601/ESB/API/ChannelZJFW/YXDMC/}")
public interface FinancialCloudClient {

    @PostMapping("/QueryFinanceDictData")
    @ApiOperation(value = "查询财务云数据", notes = "查询财务云数据")
    FinanceCommonD<PaymentMethodV> queryFinanceDictData(@RequestBody FinanceV financeV);
}
