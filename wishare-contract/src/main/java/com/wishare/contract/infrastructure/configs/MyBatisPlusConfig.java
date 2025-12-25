package com.wishare.contract.infrastructure.configs;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author 永遇乐 yeoman<76164451@.qq.com>
 * @line --------------------------------
 * @Date 2022/04/02
 */
@Configuration
@MapperScan("com.wishare.**.**.repository.**")
public class MyBatisPlusConfig {
    //分页插件——新的分页插件，旧版本PaginationInterceptor失效了
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }
}
