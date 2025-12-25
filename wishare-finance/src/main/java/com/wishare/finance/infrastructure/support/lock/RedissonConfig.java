package com.wishare.finance.infrastructure.support.lock;

import org.redisson.Redisson;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @NAME: RedissonConfig
 * @Author: xujian
 * @Date: 2021/10/27
 */
@Configuration
public class RedissonConfig {

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private String redisPort;

    @Value("${spring.redis.password}")
    private String redisPassword;

    @Bean
    public Redisson redisson() {
        Config config = new Config();
        //此示例是单机的，可以是主从、sentinel、集群等模式
        SingleServerConfig singleServerConfig = config.useSingleServer()
                .setAddress("redis://" + redisHost + ":" + redisPort);
        singleServerConfig.setPassword(redisPassword);//设置密码
        return (Redisson) Redisson.create(config);
    }
}


