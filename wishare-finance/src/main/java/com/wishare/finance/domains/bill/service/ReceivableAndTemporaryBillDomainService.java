package com.wishare.finance.domains.bill.service;

import com.wishare.finance.domains.bill.entity.ReceivableBill;
import com.wishare.finance.domains.bill.repository.ReceivableAndTemporaryBillRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author fengxiaolin
 * @date 2023/5/23
 */
@Slf4j
@Service
public class ReceivableAndTemporaryBillDomainService extends
        BillDomainServiceImpl<ReceivableAndTemporaryBillRepository, ReceivableBill> {

}
