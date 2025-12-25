package com.wishare.finance.domains.configure.subject.strategy;

import com.wishare.finance.domains.configure.subject.consts.enums.AssisteItemTypeEnum;
import com.wishare.finance.domains.configure.subject.entity.AssisteItemOBV;
import com.wishare.finance.domains.configure.subject.repository.AssisteOrgDeptRepository;
import com.wishare.finance.infrastructure.remote.clients.base.OrgClient;
import com.wishare.finance.infrastructure.remote.vo.org.OrgFinanceCostRv;
import com.wishare.finance.infrastructure.remote.vo.org.OrgFinanceRv;
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
 * 项目辅助核算策略
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/4/11
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ProjectAssisteItemStrategy implements AssisteItemStrategy {

    private final OrgClient orgClient;

    @Override
    public AssisteItemTypeEnum type() {
        return AssisteItemTypeEnum.项目;
    }

    @Override
    public List<AssisteItemOBV> list(String name, String code, String sbId) {
        List<OrgFinanceCostRv> costRvs = orgClient.getOrgFinanceCostByCodeAndName(code, name);
        return CollectionUtils.isNotEmpty(costRvs) ? costRvs.stream()
                .map(item -> toAssisteItem(AssisteItemTypeEnum.项目, item.getCostCode(), item.getNameCn()))
                .collect(Collectors.toList()) : new ArrayList<>();
    }

    @Override
    public AssisteItemOBV getById(String aiId) {
        OrgFinanceRv orgFinance = orgClient.getOrgFinanceById(Long.valueOf(aiId));
        if (Objects.isNull(orgFinance)) {
            log.info("制作凭证：未找到对应项目，项目id：{}", aiId);
        }
        return Objects.isNull(orgFinance) ? toAssisteItem(type(), null, "") :
                toAssisteItem(type(), orgFinance.getCode(), orgFinance.getNameCn());
    }

}
