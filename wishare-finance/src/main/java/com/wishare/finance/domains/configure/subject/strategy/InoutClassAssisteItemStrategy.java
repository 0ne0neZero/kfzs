package com.wishare.finance.domains.configure.subject.strategy;

import com.wishare.finance.domains.configure.subject.consts.enums.AssisteItemTypeEnum;
import com.wishare.finance.domains.configure.subject.entity.AssisteItemOBV;
import com.wishare.finance.domains.configure.subject.repository.AssisteInoutclassRepository;
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
 * 收支项目辅助核算策略
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/4/11
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class InoutClassAssisteItemStrategy implements AssisteItemStrategy {

    private final AssisteInoutclassRepository assisteInoutclassRepository;

    @Override
    public AssisteItemTypeEnum type() {
        return AssisteItemTypeEnum.收支项目;
    }

    @Override
    public List<AssisteItemOBV> list(String name, String code, String sbId) {
        return assisteInoutclassRepository.getAssisteItems(name, code);
    }

    @Override
    public AssisteItemOBV getById(String aiId) {
        List<AssisteItemOBV> list = list("", aiId, null);
        return CollectionUtils.isEmpty(list) ? toAssisteItem(AssisteItemTypeEnum.收支项目, null, "") : list.get(0);
    }

}
