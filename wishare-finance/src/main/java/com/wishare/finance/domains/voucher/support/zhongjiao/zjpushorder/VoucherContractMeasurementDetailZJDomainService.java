package com.wishare.finance.domains.voucher.support.zhongjiao.zjpushorder;


import com.wishare.finance.domains.voucher.support.zhongjiao.entity.VoucherContractInvoiceZJ;
import com.wishare.finance.domains.voucher.support.zhongjiao.entity.VoucherContractMeasurementDetailZJ;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.VoucherContractInvoiceZJRepository;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.VoucherContractMeasurementDetailZJRepository;
import com.wishare.finance.infrastructure.remote.clients.base.ContractClient;
import com.wishare.finance.infrastructure.remote.clients.base.ExternalClient;
import com.wishare.finance.infrastructure.remote.clients.base.UserClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class VoucherContractMeasurementDetailZJDomainService {
    private final UserClient userClient;
    private final ExternalClient externalClient;
    private final ContractClient contractClient;
    private final VoucherContractMeasurementDetailZJRepository measurementDetailZJRepository;
    private final VoucherContractInvoiceZJRepository contractInvoiceZJRepository;

    public VoucherContractMeasurementDetailZJ getById(Long id) {
        return measurementDetailZJRepository.getById(id);
    }

    public void deleteByContractInvoiceId(Long  contractInvoiceId) {
        measurementDetailZJRepository.deleteByContractInvoiceId(contractInvoiceId);
    }

    public List<VoucherContractMeasurementDetailZJ> queryByContractInvoiceId(Long  contractInvoiceId) {
       return measurementDetailZJRepository.getByContractInvoiceId(contractInvoiceId);
    }

    public void insert(VoucherContractMeasurementDetailZJ  measurementDetailZJ) {
        measurementDetailZJRepository.save(measurementDetailZJ);
    }

}

