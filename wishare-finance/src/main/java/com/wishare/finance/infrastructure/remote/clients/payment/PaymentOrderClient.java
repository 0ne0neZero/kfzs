package com.wishare.finance.infrastructure.remote.clients.payment;

import com.wishare.finance.infrastructure.remote.fo.payment.RefundRequestF;
import com.wishare.finance.infrastructure.remote.fo.payment.TransactionF;
import com.wishare.finance.infrastructure.remote.vo.payment.MerchantAppDetailV;
import com.wishare.finance.infrastructure.remote.vo.payment.PaymentOrderDetailRv;
import com.wishare.finance.infrastructure.remote.vo.payment.RefundV;
import com.wishare.finance.infrastructure.remote.vo.payment.TransactionV;
import com.wishare.starter.annotations.OpenFeignClient;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotBlank;

/**
 * @author xujian
 * @date 2022/11/23
 * @Description:
 */
@OpenFeignClient(name = "${wishare.feignClients.payment.name:wishare-payment}", serverName = "支付中心", path = "${wishare.feignClients.payment.context-path:/payment}", contextId = "PaymentOrderClient")
public interface PaymentOrderClient {

    @PostMapping("/transaction/trade")
    @ApiOperation(value = "支付下单", notes = "支付下单")
    TransactionV trade(@RequestBody TransactionF transactionF);

    @GetMapping("/transaction/order")
    @ApiOperation(value = "根据支付单号查询支付单", notes = "根据支付单号查询支付单")
    PaymentOrderDetailRv getDetailByPayNo(@Validated @RequestParam("payNo") @ApiParam("支付单号") @NotBlank(message = "支付单号不能为空") String payNo);

    @GetMapping("/merchant/detail/outMch")
    @ApiOperation(value = "根据外部商户编号查询商户应用详情", notes = "根据外部商户编号查询商户应用详情")
    MerchantAppDetailV getDetailByOutMchNo(@Validated @RequestParam("outMchNo")@ApiParam("外部商户编号")
                                           @NotBlank(message = "外部商户编号不能为空") String outMchNo,
                                           @RequestParam("mchType") @ApiParam("商户类型：0普通商户，1特约商户") @NotBlank(message = "商户类型不能为空") Integer mchType);

    @GetMapping("/transaction/orderByMchOrderNo")
    @ApiOperation(value = "根据支付单号查询支付单", notes = "根据支付单号查询支付单")
    PaymentOrderDetailRv getDetailByMchOrderNo(@Validated @RequestParam("mchOrderNo") @ApiParam("商户订单号") @NotBlank(message = "商户订单号不能为空") String mchOrderNo);

    @PostMapping("/refund/order")
    @ApiOperation(value = "订单退款", notes = "订单退款")
     RefundV refund(@RequestBody RefundRequestF refundRequestF);


}
