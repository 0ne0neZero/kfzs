package com.wishare.finance.domains.configure.subject.strategy;

import com.wishare.finance.domains.configure.subject.consts.enums.AssisteItemTypeEnum;
import com.wishare.finance.domains.configure.subject.entity.AssisteItemOBV;
import com.wishare.finance.infrastructure.remote.clients.base.OrgClient;
import com.wishare.finance.infrastructure.remote.vo.org.MerchantRv;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 客商辅助核算策略
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/4/11
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MerchantAssisteItemStrategy implements AssisteItemStrategy {

    private final OrgClient orgClient;

    @Override
    public AssisteItemTypeEnum type() {
        return AssisteItemTypeEnum.客商;
    }

    @Override
    public List<AssisteItemOBV> list(String name, String code, String sbId) {
        List<MerchantRv> merchants = orgClient.getMerchantByCodeAndName(code, name);
        return CollectionUtils.isNotEmpty(merchants) ? merchants.stream()
                .map(item -> toAssisteItem(type(), item.getCode(), item.getName()))
                .collect(Collectors.toList()) : new ArrayList<>();
    }

    @Override
    public AssisteItemOBV getById(String aiId) {
        MerchantRv merchant = null;
        if (StringUtils.isNotBlank(aiId)){
            merchant = orgClient.getMerchantById(Long.valueOf(aiId));
        }
        if (Objects.isNull(merchant)) {
            log.info("制作凭证：未找到对应客商，客商id：{}", aiId);
        }
        return Objects.isNull(merchant) ? toAssisteItem(type(), null, "")
                : toAssisteItem(type(), merchant.getCode(), merchant.getName());
    }

}
