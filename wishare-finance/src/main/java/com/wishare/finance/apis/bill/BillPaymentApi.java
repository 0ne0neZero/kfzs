package com.wishare.finance.apis.bill;

import com.wishare.finance.apps.model.bill.vo.ReceivableBillPreTransactionV;
import com.wishare.finance.apps.model.bill.vo.ReceivableBillTransactionConfirmV;
import com.wishare.finance.apps.service.bill.BillPaymentAppService;
import com.wishare.finance.domains.bill.command.ReceivableBillPreTransactionCommand;
import com.wishare.finance.domains.bill.command.ReceivableBillTransactionConfirmCommand;
import com.wishare.finance.domains.bill.dto.ConfirmTransactDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 账单支付相关接口
 *
 * @Author dxclay
 * @Date 2022/12/19
 * @Version 1.0
 */
@Api(tags = {"账单支付"})
@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class BillPaymentApi {

    private final BillPaymentAppService billPaymentAppService;

    @PostMapping("/preTransact")
    @ApiOperation(value = "应收账单预支付下单")
    public ReceivableBillPreTransactionV transact(@Validated @RequestBody ReceivableBillPreTransactionCommand receivableBillPreTransactionCommand){
        String preTradeId = billPaymentAppService.preTransact(receivableBillPreTransactionCommand);
        return new ReceivableBillPreTransactionV(preTradeId);
    }

    @PostMapping("/confirmTransact")
    @ApiOperation(value = "预支付确认", notes = "预支付确认")
    public ReceivableBillTransactionConfirmV confirmTransact(@Validated @RequestBody ReceivableBillTransactionConfirmCommand command){
        ConfirmTransactDto confirmTransactDto = billPaymentAppService.confirmTransact(command);
        return new ReceivableBillTransactionConfirmV(String.valueOf(confirmTransactDto.getGatherBillId()),
                confirmTransactDto.getBillIds(), true);
    }


}
