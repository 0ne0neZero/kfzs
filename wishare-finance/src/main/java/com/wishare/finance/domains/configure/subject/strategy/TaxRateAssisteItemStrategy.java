package com.wishare.finance.domains.configure.subject.strategy;

import com.alibaba.fastjson.JSON;
import com.wishare.finance.domains.configure.chargeitem.entity.TaxRateE;
import com.wishare.finance.domains.configure.chargeitem.repository.TaxRateRepository;
import com.wishare.finance.domains.configure.subject.consts.enums.AssisteItemTypeEnum;
import com.wishare.finance.domains.configure.subject.entity.AssisteItemOBV;
import com.wishare.finance.domains.configure.subject.repository.AssisteOrgDeptRepository;
import com.wishare.finance.infrastructure.remote.clients.base.OrgClient;
import com.wishare.finance.infrastructure.remote.vo.org.OrgFinanceRv;
import com.wishare.starter.utils.ThreadLocalUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 增值税税率辅助核算策略
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/4/11
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class TaxRateAssisteItemStrategy implements AssisteItemStrategy {

    private final TaxRateRepository taxRateRepository;

    @Override
    public AssisteItemTypeEnum type() {
        return AssisteItemTypeEnum.增值税税率;
    }

    @Override
    public List<AssisteItemOBV> list(String name, String code, String sbId) {
        List<TaxRateE> taxRates = taxRateRepository.getByCodeAndName(code, name);
        return CollectionUtils.isNotEmpty(taxRates) ? taxRates.stream()
                .map(item -> toAssisteItem(type(), item.getCode(), item.getRate().toString() + "%"))
                .collect(Collectors.toList()) : new ArrayList<>();
    }

    @Override
    public AssisteItemOBV getById(String aiId) {
        TaxRateE taxRate = taxRateRepository.getById(aiId);
        if (Objects.isNull(taxRate)) {
            log.info("制作凭证：未找到对应增值税税率，税率id：{}", aiId);
        }
        return Objects.isNull(taxRate) ? toAssisteItem(type(), null, "") :
                toAssisteItem(type(), taxRate.getCode(), taxRate.getRate().toString() + "%");
    }

}
