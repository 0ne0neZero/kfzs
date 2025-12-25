package com.wishare.finance.infrastructure.configs;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import lombok.Data;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.TimeZone;

/**
 * @author: Linitly
 * @date: 2023/6/25 17:06
 * @descrption:
 */
@Data
@Configuration
public class RestTemplateConfig {

    // 连接池最大连接数，0代表不限
    private Integer maxConnection = 200;

    // 同路由并发数
    private Integer maxPreRoute = 200;

    // 服务器返回数据(response)的时间，超过该时间抛出read timeout
    private Integer socketTimeout = 120000;

    // 连接上服务器(握手成功)的时间，超出该时间抛出connect timeout
    private Integer connectTimeout = 120000;

    // 从连接池中获取连接的超时时间，超过该时间未拿到可用连接，会抛出org.apache.http.conn.ConnectionPoolTimeoutException: Timeout waiting for connection from pool
    private Integer connectRequestTimeout = 1000;

    // 重试次数
    private Integer retryCount = 3;

    // 是否开启重试
    private boolean retryEnable = false;

    @Bean
    public RestTemplate restTemplate() {
//        System.out.println("rest");
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new BufferingClientHttpRequestFactory(httpRequestFactory()));
        List<HttpMessageConverter<?>> converterList = restTemplate.getMessageConverters();

        for (HttpMessageConverter<?> item : converterList) {
//            System.out.println("rest converterList");
            if (item instanceof StringHttpMessageConverter) {
//                System.out.println("StringHttpMessageConverter");
                StringHttpMessageConverter messageConverter = (StringHttpMessageConverter) item;
                messageConverter.setWriteAcceptCharset(false);
                messageConverter.setDefaultCharset(StandardCharsets.UTF_8);
            }
            if (item instanceof MappingJackson2HttpMessageConverter) {
//                System.out.println("set jackson");
                MappingJackson2HttpMessageConverter httpMessageConverter = (MappingJackson2HttpMessageConverter) item;
                ObjectMapper objectMapper = httpMessageConverter.getObjectMapper();
                // 转下划线
                objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
                // 未知属性不报错
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                objectMapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));
            }
        }
        // 添加拦截器
//        List<ClientHttpRequestInterceptor> interceptors = restTemplate.getInterceptors();
//        interceptors.add(new LoggingClientHttpRequestInterceptor());
//        restTemplate.setInterceptors(interceptors);
        return restTemplate;
    }

    @Bean
    public ClientHttpRequestFactory httpRequestFactory() {
        return new HttpComponentsClientHttpRequestFactory(httpClient());
    }

    @Bean
    public PoolingHttpClientConnectionManager poolingHttpClientConnectionManager() {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(maxConnection);
        connectionManager.setDefaultMaxPerRoute(maxPreRoute);
        return connectionManager;
    }

    @Bean
    public HttpClient httpClient() {
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(socketTimeout)
                .setConnectTimeout(connectTimeout)
                .setConnectionRequestTimeout(connectRequestTimeout)
                .build();

        return HttpClientBuilder.create()
                .setDefaultRequestConfig(requestConfig)
                .setConnectionManager(poolingHttpClientConnectionManager())
                .setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy())
                .setRetryHandler(new DefaultHttpRequestRetryHandler(retryCount, retryEnable))
                .build();
    }
}
