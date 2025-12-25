package com.wishare.finance.domains.configure.subject.strategy;

import com.wishare.finance.domains.configure.organization.entity.StatutoryBodyAccountE;
import com.wishare.finance.domains.configure.organization.repository.StatutoryBodyAccountRepository;
import com.wishare.finance.domains.configure.subject.consts.enums.AssisteItemTypeEnum;
import com.wishare.finance.domains.configure.subject.entity.AssisteItemOBV;
import com.wishare.finance.domains.configure.subject.repository.AssisteOrgDeptRepository;
import com.wishare.finance.infrastructure.remote.clients.base.OrgClient;
import com.wishare.finance.infrastructure.remote.vo.org.OrgFinanceRv;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 银行账户辅助核算策略
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/4/11
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BankAccountAssisteItemStrategy implements AssisteItemStrategy {

    private final StatutoryBodyAccountRepository statutoryBodyAccountRepository;

    @Override
    public AssisteItemTypeEnum type() {
        return AssisteItemTypeEnum.银行账户;
    }

    @Override
    public List<AssisteItemOBV> list(String name, String code, String sbId) {
        List<StatutoryBodyAccountE> accounts = statutoryBodyAccountRepository.getByCodeAndName(code, name);
        return CollectionUtils.isNotEmpty(accounts) ? accounts.stream()
                .map(item -> toAssisteItem(type(), item.getBankAccount(), item.getName()))
                .collect(Collectors.toList()) : new ArrayList<>();
    }

    @Override
    public AssisteItemOBV getById(String aiId) {
        StatutoryBodyAccountE statutoryBodyAccount = statutoryBodyAccountRepository.getById(aiId);
        if (Objects.isNull(statutoryBodyAccount)){
            statutoryBodyAccount = new StatutoryBodyAccountE();
        }
        return toAssisteItem(type(), statutoryBodyAccount.getBankAccount(), statutoryBodyAccount.getBankName());
    }

}
