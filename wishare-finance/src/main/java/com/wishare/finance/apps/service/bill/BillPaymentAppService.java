package com.wishare.finance.apps.service.bill;

import com.wishare.finance.domains.bill.command.ReceivableBillPreTransactionCommand;
import com.wishare.finance.domains.bill.command.ReceivableBillTransactionConfirmCommand;
import com.wishare.finance.domains.bill.dto.ConfirmTransactDto;
import com.wishare.finance.domains.bill.service.BillPaymentDomainService;
import com.wishare.finance.infrastructure.event.EventTransactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 账单支付应用服务
 *
 * @Author dxclay
 * @Date 2022/12/19
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BillPaymentAppService {

    private final BillPaymentDomainService billPaymentDomainService;

    /**
     * 应收账单预支付
     * @param billPreTransactionCommand 预支付信息
     * @return {@link String}
     */
    public String preTransact(ReceivableBillPreTransactionCommand billPreTransactionCommand){
        return billPaymentDomainService.preTransact(billPreTransactionCommand);
    }

    /**
     * 预支付确认
     * @param transactionConfirmCommand 预支付确认参数
     * @return {@link ConfirmTransactDto}
     */
    public ConfirmTransactDto confirmTransact(ReceivableBillTransactionConfirmCommand transactionConfirmCommand){
        return billPaymentDomainService.confirmTransaction(transactionConfirmCommand);
    }


}
