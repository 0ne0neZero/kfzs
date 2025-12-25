package com.wishare.finance.infrastructure.remote.clients.base;

import javax.validation.constraints.NotBlank;

import com.wishare.finance.infrastructure.remote.vo.payment.PaymentOrderDetailV;
import com.wishare.starter.annotations.OpenFeignClient;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author ℳ๓采韵
 * @project wishare-finance
 * @title PayClient
 * @date 2024.03.22  10:38
 * @description wishare-payment
 */
@OpenFeignClient(name = "wishare-payment", serverName = "payment服务", path = "/payment")
public interface PayClient {

    @GetMapping("/transaction/orderByMchOrderNo")
    @ApiOperation(value = "根据商户订单号查询支付单", notes = "根据商户订单号查询支付单")
    PaymentOrderDetailV getDetailByMchOrderNo(@Validated @RequestParam("mchOrderNo") @ApiParam("商户订单号") @NotBlank(message = "商户订单号不能为空") String mchOrderNo);
}
