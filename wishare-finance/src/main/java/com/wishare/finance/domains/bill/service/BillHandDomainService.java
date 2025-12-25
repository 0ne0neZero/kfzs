package com.wishare.finance.domains.bill.service;

import com.wishare.finance.apps.model.bill.vo.BillHandV;
import com.wishare.finance.domains.bill.repository.BillHandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @description: 交账
 * @author: pgq
 * @since: 2022/10/9 10:32
 * @version: 1.0.0
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BillHandDomainService {

    private final BillHandRepository handRepository;

    // TODO 处理代码结构
    public List<BillHandV> listByBillIds(List<Long> billIds) {
        return Collections.emptyList();
    }
}
