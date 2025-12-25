package com.wishare.finance.domains.voucher.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.wishare.finance.apps.model.yuanyang.fo.ProcessBankInfoF;
import com.wishare.finance.apps.model.yuanyang.fo.ProcessBankPayInfoF;
import com.wishare.finance.domains.bill.consts.enums.BillTransactStateEnum;
import com.wishare.finance.domains.bill.entity.TransactionOrder;
import com.wishare.finance.domains.bill.repository.TransactionOrderRepository;
import com.wishare.finance.domains.voucher.entity.BusinessBill;
import com.wishare.finance.domains.voucher.entity.BusinessBillDetail;
import com.wishare.finance.domains.voucher.repository.BusinessBillDetailRepository;
import com.wishare.finance.domains.voucher.repository.BusinessBillRepository;
import com.wishare.starter.exception.BizException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author luzhonghe
 * @version 1.0
 * @since 2023/8/1
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BusinessBillDomainService {

    private final BusinessBillRepository businessBillRepository;
    private final BusinessBillDetailRepository businessBillDetailRepository;
    private final TransactionOrderRepository transactionOrderRepository;

    public void saveBillAndDetails(BusinessBill businessBill, List<BusinessBillDetail> businessBillDetails) {
        BusinessBill temp = businessBillRepository.getOne(new LambdaQueryWrapper<BusinessBill>()
                .eq(BusinessBill::getBusinessId, businessBill.getBusinessId()));
        if (Objects.nonNull(temp)) {
            ProcessBankInfoF bankInfo = temp.getBankInfo();
            boolean allFail = false;
            List<String> payIds = new ArrayList<>();
            if (Objects.nonNull(bankInfo)) {
                List<ProcessBankPayInfoF> payBankInfos = bankInfo.getPayBankInfos();
                payIds = payBankInfos.stream().map(ProcessBankPayInfoF::getPayId).collect(Collectors.toList());
                List<TransactionOrder> transactionOrders = transactionOrderRepository.getByBizTransactionNos(payIds);
                allFail = transactionOrders.stream().allMatch(order -> BillTransactStateEnum.交易失败.equalsByCode(order.getTransactState()));
            }
            if (allFail) {
                businessBillRepository.update(businessBill, new LambdaUpdateWrapper<BusinessBill>()
                        .eq(BusinessBill::getBusinessId, businessBill.getBusinessId()));
                businessBillDetailRepository.remove(new LambdaQueryWrapper<BusinessBillDetail>()
                        .eq(BusinessBillDetail::getBillId, temp.getBillId()));
                businessBillDetails.forEach(detail -> {
                    detail.init();
                    detail.setBillId(temp.getBillId());
                });
                businessBillDetailRepository.saveBatch(businessBillDetails);
                transactionOrderRepository.remove(
                        new LambdaQueryWrapper<TransactionOrder>()
                                .in(TransactionOrder::getBizTransactionNo, payIds));
                return;
            } else {
                throw BizException.throw400("请勿重复请求");
            }
        }
        businessBill.init();
        businessBillRepository.save(businessBill);
        businessBillDetails.forEach(detail -> {
            detail.init();
            detail.setBillId(businessBill.getBillId());
        });
        businessBillDetailRepository.saveBatch(businessBillDetails);
    }

    public BusinessBill getByBusinessId(String businessId) {
        return businessBillRepository.getOne(new LambdaQueryWrapper<BusinessBill>()
                .eq(BusinessBill::getBusinessId, businessId));
    }

    public void update(BusinessBill businessBill, List<BusinessBillDetail> details) {
        businessBillRepository.updateById(businessBill);
        businessBillDetailRepository.updateBatchById(details);
    }

    public BusinessBill getByVoucherId(String voucherId) {
        return businessBillRepository.getByVoucherId(voucherId);
    }

    public List<BusinessBillDetail> getDetailsByBusinessBillId(Long billId) {
        return businessBillDetailRepository.list(new LambdaQueryWrapper<BusinessBillDetail>()
                .eq(BusinessBillDetail::getBillId, billId));
    }

}
