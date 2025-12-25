package com.wishare.finance.infrastructure.configs;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.wishare.finance.domains.enums.CacheConst;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
public class CacheConfig {

    @Bean
    public CaffeineCacheManager caffeineCacheManager() {
        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager();
        caffeineCacheManager.setCaffeine(
                Caffeine.newBuilder()
                        .expireAfterAccess(20, TimeUnit.SECONDS)
                        .initialCapacity(20)
                        .weakValues()
                        .maximumSize(100)
        );
        caffeineCacheManager.setCacheLoader(key -> null);
        caffeineCacheManager.setCacheNames(List.of(
                CacheConst.COMMUNITY_LIST
        ));
        return caffeineCacheManager;
    }
}