package com.wishare.finance.domains.configure.taxrate.service;

import com.wishare.finance.domains.configure.taxrate.entity.NccTaxRate;
import com.wishare.finance.domains.configure.taxrate.repository.NccTaxRateRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: pgq
 * @since: 2023/2/28 13:38
 * @version: 1.0.0
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class NccTaxRateDomainService {

    private final NccTaxRateRepository nccTaxRateRepository;

    /**
     * 批量插入
     * @param rates
     */
    public void batchInsert(List<NccTaxRate> rates) {
        nccTaxRateRepository.saveBatch(rates);

    }

    /**
     * 查询所有税率数据
     * @return
     */
    public List<NccTaxRate> listRate() {
        return nccTaxRateRepository.list();
    }
}
