package com.wishare.finance.apps.service.bill.accounthand;

import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: pgq
 * @since: 2022/9/26 19:18
 * @version: 1.0.0
 */
@Service
@Slf4j
public class ReceivableAccountHandAppService extends AccountHandAppService {

    @Override
    public BillTypeEnum getBillType() {
        return BillTypeEnum.应收账单;
    }

    @Override
    public Boolean invoiceReversal(Long id) {
        getOneBill(id, getBillType());
        super.invoiceReversal(id, getBillType().getCode());
        return Boolean.TRUE;
    }
}
