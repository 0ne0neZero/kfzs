package com.wishare.contract;

import com.wishare.starter.StarterApplication;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableFeignClients({"com.wishare.component.imports.task.client", "com.wishare.contract.apps.remote.clients"})
@MapperScan(value = "com.wishare.contract.domains.mapper")
@SpringBootApplication
@EnableAsync
public class WishareContractApplication extends StarterApplication {

    public static void main(String[] args) {
        SpringApplication.run(WishareContractApplication.class, args);
    }

}
