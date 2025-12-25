package com.wishare.finance.domains.bill.service;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wishare.finance.domains.bill.entity.BillContractE;
import com.wishare.finance.domains.bill.repository.BillContractRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ℳ๓采韵
 * @project wishare-finance
 * @title BillContractDomainService
 * @date 2024.07.03  16:10
 * @description
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BillContractDomainService {

    private final BillContractRepository billContractRepository;


    /**
     * 批量新增记录
     * @param result
     * @return
     */
    public Boolean batchAdd(List<BillContractE> result) {
        return billContractRepository.saveBatch(result);
    }


    /**
     * 根据账单ID获取其对应的合同编号信息
     * @param billId 账单ID
     * @return
     */
    public BillContractE getByBillId(Long billId) {
        return billContractRepository.getOne(new LambdaQueryWrapper<BillContractE>().eq(BillContractE::getBillId,billId)
                .eq(BillContractE::getDeleted,0));
    }
}
