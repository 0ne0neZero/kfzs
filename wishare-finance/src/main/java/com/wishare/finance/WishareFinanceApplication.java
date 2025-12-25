package com.wishare.finance;

import com.wishare.starter.StarterApplication;
import com.wishare.starter.handlers.DefaultMybatisObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableFeignClients
@SpringBootApplication
@EnableRetry
@EnableAsync
@ComponentScan(excludeFilters= {@ComponentScan.Filter(type= FilterType.ASSIGNABLE_TYPE, value= {DefaultMybatisObjectHandler.class})})
@Slf4j
@EnableTransactionManagement
public class WishareFinanceApplication extends StarterApplication {

    public static void main(String[] args) {
        SpringApplication.run(WishareFinanceApplication.class, args);
    }

}
