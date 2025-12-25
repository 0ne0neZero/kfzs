package com.wishare.finance.infrastructure.utils;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * web请求工具类
 *
 * @Author dxclay
 * @Date 2022/11/29
 * @Version 1.0
 */
public class WebUtil {

    private static Logger log = LoggerFactory.getLogger(WebUtil.class);
    private static final String CHARSET = "UTF-8";
    private static final String JSON_ACCEPT = "application/json";
    private static final String JSON_CONTENT = "application/json;charset=utf-8";
    private static final String XML_CONTENT = "application/xml";
    private static HttpClientBuilder httpBuilder = null;
    private static RequestConfig requestConfig = null;
    private static PoolingHttpClientConnectionManager connectionManager = null;

    /**
     * 最大连接连接数量
     */
    private static int MAX_CONNECTION = 200;
    /**
     * 最大并发请求数量
     */
    private static int DEFAULT_MAX_ROUTE = 200;

    static {
        requestConfig = RequestConfig.custom()
                .setSocketTimeout(120000)
                .setConnectTimeout(120000)
                .setConnectionRequestTimeout(10000)
                .build();
        connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(MAX_CONNECTION);
        connectionManager.setDefaultMaxPerRoute(DEFAULT_MAX_ROUTE);
        httpBuilder = HttpClients.custom();
        httpBuilder.setConnectionManager(connectionManager);
    }

    /**
     * 获取http客户端连接
     *
     * @return http客户端连接
     */
    public static CloseableHttpClient getConnection() {
        return httpBuilder.build();
    }

    /**
     * POST Query参数请求
     * @param url      请求url
     * @param params   请求参数
     * @return
     */
    public static String doPost(String url, Map<String, String> params) throws IOException {
        return doPostWithClient(getConnection(), url, params);
    }

    /**
     * POST Query参数请求
     * @param url      请求url
     * @param params   请求参数
     * @param headers  请求头部信息
     * @return
     */
    public static String doPost(String url, Map<String, String> params, Map<String, String> headers) throws IOException {
        return doPostWithClient(getConnection(), url, params, headers);
    }

    /**
     * POST JSON参数请求
     *
     * @param url        请求url
     * @param body       请求体参数JSON字符串
     * @return
     * @throws Exception 异常
     */
    public static String doPost(String url, String body) throws IOException {
        return doPostWithClient(getConnection(), url, body);
    }

    /**
     * POST JSON参数请求
     *
     * @param url        请求url
     * @param body       请求体参数JSON字符串
     * @return
     * @throws Exception 异常
     */
    public static String doPost(String url, String body, Map<String, String> headers) throws IOException {
        return doPostWithClient(getConnection(), url, body, headers);
    }

    /**
     * POST Query参数请求
     * @param url      请求url
     * @param params   请求参数
     * @return
     */
    public static String doPostWithClient(CloseableHttpClient httpClient, String url, Map<String, String> params) throws IOException {
        return EntityUtils.toString(doPostWithClientToResponse(httpClient, url, params).getEntity());
    }

    /**
     * POST Query参数请求
     * @param url      请求url
     * @param params   请求参数
     * @param headers  请求头部信息
     * @return
     */
    public static String doPostWithClient(CloseableHttpClient httpClient, String url, Map<String, String> params, Map<String, String> headers) throws IOException {
        return EntityUtils.toString(doPostWithClientToResponse(httpClient, url, params, headers).getEntity());
    }

    /**
     * POST JSON参数请求
     *
     * @param url        请求url
     * @param body       请求体参数JSON字符串
     * @return
     * @throws Exception 异常
     */
    public static String doPostWithClient(CloseableHttpClient httpClient, String url, String body) throws IOException {
        return EntityUtils.toString(doPostWithClientToResponse(httpClient, url, body).getEntity());
    }

    /**
     * POST JSON参数请求
     *
     * @param url        请求url
     * @param body       请求体参数JSON字符串
     * @return
     * @throws Exception 异常
     */
    public static String doPostWithClient(CloseableHttpClient httpClient, String url, String body, Map<String, String> headers) throws IOException {
        return EntityUtils.toString(doPostWithClientToResponse(httpClient, url, body, headers).getEntity());
    }

    /**
     * POST Query参数请求
     * @param url      请求url
     * @param params   请求参数
     * @return
     */
    public static HttpResponse doPostWithClientToResponse(CloseableHttpClient httpClient, String url, Map<String, String> params) throws IOException {
        List<NameValuePair> reqParams = new ArrayList<>();
        params.forEach((k, v) -> reqParams.add(new BasicNameValuePair(k,v)));
        HttpUriRequest postMethod = RequestBuilder.post().setUri(url)
                .addParameters(reqParams.toArray(new BasicNameValuePair[params.size()]))
                .setConfig(requestConfig).build();
        return httpClient.execute(postMethod);
    }

    /**
     * POST Query参数请求
     * @param url      请求url
     * @param params   请求参数
     * @param headers  请求头部信息
     * @return
     */
    public static HttpResponse doPostWithClientToResponse(CloseableHttpClient httpClient, String url, Map<String, String> params, Map<String, String> headers) throws IOException {
        List<NameValuePair> reqParams = new ArrayList<>();
        params.forEach((k, v) -> reqParams.add(new BasicNameValuePair(k,v)));
        RequestBuilder requestBuilder = RequestBuilder.post().setUri(url)
                .addParameters(reqParams.toArray(new BasicNameValuePair[params.size()]))
                .setConfig(requestConfig);
        //添加请求头部
        if (Objects.nonNull(headers)){
            headers.forEach((k,v) -> requestBuilder.addHeader(k, v));
        }
        return httpClient.execute(requestBuilder.build());
    }

    /**
     * POST JSON参数请求
     *
     * @param url        请求url
     * @param body       请求体参数JSON字符串
     * @return
     * @throws Exception 异常
     */
    public static HttpResponse doPostWithClientToResponse(CloseableHttpClient httpClient, String url, String body) throws IOException {
        HttpUriRequest postMethod = RequestBuilder.post().setUri(url)
                .setHeader("Content-Type", JSON_CONTENT)
                .setHeader("Accept", JSON_ACCEPT)
                .setEntity(new StringEntity(body, Charset.forName(CHARSET)))
                .setConfig(requestConfig).build();
        return httpClient.execute(postMethod);
    }

    /**
     * POST JSON参数请求
     *
     * @param url        请求url
     * @param body       请求体参数JSON字符串
     * @return
     * @throws Exception 异常
     */
    public static HttpResponse doPostWithClientToResponse(CloseableHttpClient httpClient, String url, String body, Map<String, String> headers) throws IOException {
        RequestBuilder requestBuilder = RequestBuilder.post().setUri(url)
                .setHeader(HttpHeaders.CONTENT_TYPE, getContentType(headers))
                .setHeader(HttpHeaders.ACCEPT, getAccept(headers))
                .setEntity(new StringEntity(body, Charset.forName(CHARSET)))
                .setConfig(requestConfig);
        //添加请求头部
        if (Objects.nonNull(headers)){
            headers.forEach((k,v) -> requestBuilder.addHeader(k, v));
        }
        return httpClient.execute(requestBuilder.build());
    }



    /**
     * GET 无参请求
     *
     * @param url        请求url
     * @return
     * @throws IOException
     */
    public static String doGet(String url) throws IOException {
        return doGetWithClient(getConnection(), url);
    }

    /**
     * GET 有参请求
     *
     * @param url        请求url
     * @param params  请求参数
     * @return
     * @throws Exception 异常
     */
    public static String doGet(String url, Map<String, String> params) throws Exception {
       return doGetWithClient(getConnection(), url, params);
    }

    /**
     * GET 有参请求
     *
     * @param url        请求url
     * @param params  请求参数
     * @return
     * @throws IOException
     */
    public static String doGet(String url, Map<String, String> params, Map<String, String> headers) throws IOException {
        return doGetWithClient(getConnection(), url, params, headers);
    }


    /**
     * GET 无参请求
     *
     * @param url        请求url
     * @return
     * @throws IOException
     */
    public static String doGetWithClient(CloseableHttpClient httpClient, String url) throws IOException {
        return EntityUtils.toString(doGetWithClientToResponse(httpClient, url).getEntity());
    }

    /**
     * GET 有参请求
     *
     * @param url        请求url
     * @param params  请求参数
     * @return
     * @throws Exception 异常
     */
    public static String doGetWithClient(CloseableHttpClient httpClient, String url, Map<String, String> params) throws Exception {
        return EntityUtils.toString(doGetWithClientToResponse(httpClient, url, params).getEntity());
    }

    /**
     * GET 有参请求
     *
     * @param url        请求url
     * @param params  请求参数
     * @return
     * @throws IOException
     */
    public static String doGetWithClient(CloseableHttpClient httpClient, String url, Map<String, String> params, Map<String, String> headers) throws IOException {
        return EntityUtils.toString(doGetWithClientToResponse(httpClient, url, params, headers).getEntity());
    }

    /**
     * GET 无参请求
     *
     * @param url        请求url
     * @return
     * @throws IOException
     */
    public static HttpResponse doGetWithClientToResponse(CloseableHttpClient httpClient, String url) throws IOException {
        HttpUriRequest getMethod = RequestBuilder.get().setUri(url)
                .setConfig(requestConfig).build();
        return httpClient.execute(getMethod);
    }

    /**
     * GET 有参请求
     *
     * @param url        请求url
     * @param params  请求参数
     * @return
     * @throws Exception 异常
     */
    public static HttpResponse doGetWithClientToResponse(CloseableHttpClient httpClient, String url, Map<String, String> params) throws Exception {
        List<NameValuePair> reqParams = new ArrayList<>();
        params.forEach((k, v) -> reqParams.add(new BasicNameValuePair(k, v)));
        HttpUriRequest getMethod = RequestBuilder.get().setUri(url)
                .addParameters(reqParams.toArray(new BasicNameValuePair[reqParams.size()]))
                .setConfig(requestConfig).build();
        return httpClient.execute(getMethod);
    }

    /**
     * GET 有参请求
     *
     * @param url        请求url
     * @param params  请求参数
     * @return
     * @throws IOException
     */
    public static HttpResponse doGetWithClientToResponse(CloseableHttpClient httpClient, String url, Map<String, String> params, Map<String, String> headers) throws IOException {
        List<NameValuePair> reqParams = new ArrayList<>();
        RequestBuilder requestBuilder = RequestBuilder.get().setUri(url)
                .addParameters(reqParams.toArray(new BasicNameValuePair[reqParams.size()]))
                .setConfig(requestConfig);
        params.forEach((k, v) -> reqParams.add(new BasicNameValuePair(k, v)));
        if (Objects.nonNull(headers)){
            headers.forEach((k,v) -> requestBuilder.addHeader(k, v));
        }
        return httpClient.execute(requestBuilder.build());
    }


    /**
     *
     * @param url
     * @param xmlBody
     * @param headers
     * @return
     * @throws IOException
     */
    public static String doPostXml(String url, String xmlBody, Map<String, String> headers) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        List<HttpMessageConverter<?>> httpMessageConverters = restTemplate.getMessageConverters();
        httpMessageConverters.stream()
                .forEach(httpMessageConverter -> {
                    if(httpMessageConverter instanceof StringHttpMessageConverter) {
                        StringHttpMessageConverter messageConverter = (StringHttpMessageConverter) httpMessageConverter;
                        messageConverter.setDefaultCharset(Charset.forName("GBK"));
                    }
                });
        restTemplate.setMessageConverters(httpMessageConverters);
        org.springframework.http.HttpHeaders httpHeaders = new org.springframework.http.HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_XML);
        HttpEntity entity = new HttpEntity(xmlBody, httpHeaders);
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
        return response.getBody();
    }

    public static String getCharset(Map<String, String> headers){
        if (headers == null || headers.isEmpty()){
            return CHARSET;
        }
        String charset = headers.get(HttpHeaders.ACCEPT_CHARSET);
        return charset == null ? CHARSET : charset;
    }

    public static String getContentType(Map<String, String> headers){
        if (headers == null || headers.isEmpty()){
            return JSON_CONTENT;
        }
        String contentType = headers.get(HttpHeaders.CONTENT_TYPE);
        return contentType == null ? JSON_CONTENT : contentType;
    }

    public static String getAccept(Map<String, String> headers){
        if (headers == null || headers.isEmpty()){
            return JSON_ACCEPT;
        }
        String accept = headers.get(HttpHeaders.ACCEPT);
        return accept == null ? JSON_ACCEPT : accept;
    }

}
