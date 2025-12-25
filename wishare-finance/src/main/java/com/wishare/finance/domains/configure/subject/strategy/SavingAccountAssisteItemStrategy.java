package com.wishare.finance.domains.configure.subject.strategy;

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

/**
 * 存款账户性质辅助核算策略
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/4/11
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SavingAccountAssisteItemStrategy implements AssisteItemStrategy {


    @Override
    public AssisteItemTypeEnum type() {
        return AssisteItemTypeEnum.存款账户性质;
    }

    @Override
    public List<AssisteItemOBV> list(String name, String code, String sbId) {
        return List.of(toAssisteItem(type(), "01", "活期"), toAssisteItem(AssisteItemTypeEnum.存款账户性质, "02", "定期"));
    }

    @Override
    public AssisteItemOBV getById(String aiId) {
        return "01".equals(aiId) ? toAssisteItem(type(), "01", "活期") : toAssisteItem(type(), "02", "定期");
    }

}
