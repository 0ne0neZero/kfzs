package com.wishare.finance.domains.bill.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.domains.bill.dto.AdvanceBillRefundDto;
import com.wishare.finance.domains.bill.dto.ReceivableBillRefundDto;
import com.wishare.finance.domains.bill.dto.TempChargeBillRefundDto;
import com.wishare.finance.domains.bill.entity.BillDeductionE;
import com.wishare.finance.domains.bill.entity.BillRefundE;
import com.wishare.finance.domains.bill.repository.BillDeductionRepository;
import com.wishare.finance.domains.bill.repository.BillRefundRepository;
import com.wishare.starter.beans.PageF;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author xujian
 * @date 2022/9/8
 * @Description:
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BillDeductionDomainService {

    private final BillDeductionRepository billDeductionRepository;

    /**
     *
     * @param
     * @return
     */
    public Integer getByBillIds(List<Long> billIds) {
        return billDeductionRepository.getByBillIds(billIds);
    }

    /**
     *
     * @param billId
     * @return
     */
    public List<BillDeductionE> getList(Long billId) {
        return billDeductionRepository.getList(billId);
    }

}
