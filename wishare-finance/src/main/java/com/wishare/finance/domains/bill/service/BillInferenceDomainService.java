package com.wishare.finance.domains.bill.service;

import com.wishare.finance.domains.bill.command.BatchAddBillInferenceCommand;
import com.wishare.finance.domains.bill.command.BatchDelBillInferenceCommand;
import com.wishare.finance.domains.bill.entity.BillInferenceE;
import com.wishare.finance.domains.bill.repository.BillInferenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: pgq
 * @since: 2022/10/26 9:39
 * @version: 1.0.0
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BillInferenceDomainService {

    private final BillInferenceRepository billInferenceRepository;

    /**
     * 批量新增账单推凭记录
     * @param command
     * @return
     */
    public List<Long> batchInsertInference(BatchAddBillInferenceCommand command) {
        List<BillInferenceE> list = new ArrayList<>();
        for (Long billId : command.getBillIds()) {
            BillInferenceE billInference = new BillInferenceE();
            billInference.setBillId(billId);
            billInference.setEventType(command.getEventType());
            billInference.setBillType(command.getBillType());
            billInference.setInferenceStatus(0);
            list.add(billInference);
        }
        billInferenceRepository.saveBatch(list);
        return list.stream().map(BillInferenceE::getId).collect(Collectors.toList());
    }

    /**
     * 批量删除账单推凭记录
     * @param map
     * @return
     */
    public boolean batchDeleteInference(BatchDelBillInferenceCommand map) {
        return billInferenceRepository.batchDeleteInference(map.getInferIds());
    }
}
