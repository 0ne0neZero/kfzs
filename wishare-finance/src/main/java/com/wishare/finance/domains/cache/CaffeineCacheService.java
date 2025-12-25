package com.wishare.finance.domains.cache;

import com.alibaba.fastjson.JSON;
import com.wishare.finance.domains.enums.CacheConst;
import com.wishare.finance.infrastructure.remote.clients.base.SpaceClient;
import com.wishare.finance.infrastructure.remote.vo.space.CommunityShortRV;
import com.wishare.starter.beans.IdentityInfo;
import com.wishare.starter.utils.ThreadLocalUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CaffeineCacheService {

    private final SpaceClient spaceFeignClient;
    private final CaffeineCacheManager caffeineCacheManager;


    /**
     * 获取项目map(key --> 项目名称， value --> CommunityShortV)
     *
     * @return 项目map
     */
    public Map<String, CommunityShortRV> getCommunityNameMap() {
        List<CommunityShortRV> communityShortVS = getCommunityList();
        return communityShortVS.stream().collect(Collectors.toMap(CommunityShortRV::getName, Function.identity()));
    }

    /**
     * 获取项目List
     *
     * @return 项目list
     */
    public List<CommunityShortRV> getCommunityList() {
        IdentityInfo identityInfo = ThreadLocalUtil.curIdentityInfo();
        Cache cache = caffeineCacheManager.getCache(CacheConst.COMMUNITY_LIST);
        return Optional.ofNullable(Objects.requireNonNull(cache)
                        .get(ThreadLocalUtil.curIdentityInfo().getTenantId(),
                                () -> {
                                    log.info(JSON.toJSONString(identityInfo));
                                    List<CommunityShortRV> list = spaceFeignClient.getCommunityByTenantId(identityInfo.getTenantId());
                                    log.info(String.valueOf(list.size()));
                                    return list;
                                }))
                .orElse(List.of());
    }

}
