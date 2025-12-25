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
 * 部门辅助核算策略
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/4/11
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class OrgAssisteItemStrategy implements AssisteItemStrategy {

    private final OrgClient orgClient;

    private final AssisteOrgDeptRepository assisteOrgDeptRepository;

    @Override
    public AssisteItemTypeEnum type() {
        return AssisteItemTypeEnum.部门;
    }

    @Override
    public List<AssisteItemOBV> list(String name, String code, String sbId) {
        //获取法定单位
        if (Objects.isNull(sbId)){
            return new ArrayList<>();
        }
        OrgFinanceRv orgFinance = orgClient.getOrgFinanceById(Long.valueOf(sbId));
        return Objects.nonNull(orgFinance) ? assisteOrgDeptRepository.getAssisteItems(name, code, orgFinance.getCode()) : new ArrayList<>();
    }

    @Override
    public AssisteItemOBV getById(String aiId) {
        List<AssisteItemOBV> list = list("", "", aiId);
        return CollectionUtils.isEmpty(list) ? toAssisteItem(type(), null, "") : list.get(0);
    }

}
