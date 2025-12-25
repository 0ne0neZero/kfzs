package com.wishare.finance.domains.invoicereceipt.support.baiwang;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.wishare.finance.infrastructure.remote.fo.external.baiwang.request.*;
import com.wishare.finance.infrastructure.remote.fo.external.baiwang.response.*;
import com.wishare.starter.exception.BizException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;

/**
 * @author: Linitly
 * @date: 2023/6/25 9:23
 * @descrption:
 */
@Slf4j
@Service
@RefreshScope
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BaiwangSupport {

    private final RestTemplate restTemplate;

    private static final SerializeConfig SERIALIZE_CONFIG = new SerializeConfig();

    @Value("${baiwang.openUrl:demo}")
    private String url;

    static {
        SERIALIZE_CONFIG.setPropertyNamingStrategy(PropertyNamingStrategy.SnakeCase);
    }

    @Async
    public void asyncFormatCreate(CommonRequestParam param, FormatCreateReqF reqF) {
        URI uri = getComplateUri(param);
        HttpHeaders headers = getCommonHeaders();
        HttpEntity<FormatCreateReqF> entity = new HttpEntity<>(reqF, headers);
        ParameterizedTypeReference<CommonResV<FormatCreateResV>> typeRef = new ParameterizedTypeReference<>() {};
        log.info("百望异步版式生成入参：【param】：{}，【entity】：{}", param,
                JSON.toJSONString(entity, SERIALIZE_CONFIG, SerializerFeature.WriteMapNullValue));
        ResponseEntity<CommonResV<FormatCreateResV>> response = this.exchangePost(uri, entity, typeRef);
        this.handleResponse(response);
    }

    /**
     * 版式生成
     */
    public CommonResV<FormatCreateResV> formatCreate(CommonRequestParam param, FormatCreateReqF reqF) {
        URI uri = getComplateUri(param);
        HttpHeaders headers = getCommonHeaders();
        HttpEntity<FormatCreateReqF> entity = new HttpEntity<>(reqF, headers);
        ParameterizedTypeReference<CommonResV<FormatCreateResV>> typeRef = new ParameterizedTypeReference<>() {};
        log.info("百望版式生成入参：【param】：{}，【entity】：{}", param,
                JSON.toJSONString(entity, SERIALIZE_CONFIG, SerializerFeature.WriteMapNullValue));
        ResponseEntity<CommonResV<FormatCreateResV>> response = exchangePost(uri, entity, typeRef);
        return handleResponse(response);
    }

    /**
     * 全电发票查询
     */
    public CommonResV<List<EInvoiceSearchResModelV>> qdInvoiceSearch(CommonRequestParam param, InvoiceSearchReqF reqF) {
        URI uri = getComplateUri(param);
        HttpHeaders headers = getCommonHeaders();
        HttpEntity<InvoiceSearchReqF> entity = new HttpEntity<>(reqF, headers);
        ParameterizedTypeReference<CommonResV<List<EInvoiceSearchResModelV>>> typeRef = new ParameterizedTypeReference<>() {};
        log.info("百望全电查询入参：【param】：{}，【entity】：{}", param,
                JSON.toJSONString(entity, SERIALIZE_CONFIG, SerializerFeature.WriteMapNullValue));
        ResponseEntity<CommonResV<List<EInvoiceSearchResModelV>>> response = exchangePost(uri, entity, typeRef);
        return handleResponse(response);

    }

    /**
     * 红字确认单查询
     */
    public CommonResV<List<RedConfirmSearchResV>> redSearch(CommonRequestParam param, RedConfirmSearchDataF reqF) {
        URI uri = getComplateUri(param);
        HttpHeaders headers = getCommonHeaders();
        HttpEntity<RedConfirmSearchDataF> entity = new HttpEntity<>(reqF, headers);
        ParameterizedTypeReference<CommonResV<List<RedConfirmSearchResV>>> typeRef = new ParameterizedTypeReference<>() {};
        log.info("百望红字查询入参：【param】：{}，【entity】：{}", param,
                JSON.toJSONString(entity, SERIALIZE_CONFIG, SerializerFeature.WriteMapNullValue));
        ResponseEntity<CommonResV<List<RedConfirmSearchResV>>> response = exchangePost(uri, entity, typeRef);
        return handleResponse(response);
    }

    /**
     * 红字确认单申请
     */
    public CommonResV<List<RedConfirmResV>> redConfirm(CommonRequestParam param, RedConfirmReqF reqF) {
        URI uri = getComplateUri(param);
        HttpHeaders headers = getCommonHeaders();
        HttpEntity<RedConfirmReqF> entity = new HttpEntity<>(reqF, headers);
        ParameterizedTypeReference<CommonResV<List<RedConfirmResV>>> typeRef = new ParameterizedTypeReference<>() {};
        log.info("百望红字确认入参：【param】：{}，【entity】：{}", param,
                JSON.toJSONString(entity, SERIALIZE_CONFIG, SerializerFeature.WriteMapNullValue));
        ResponseEntity<CommonResV<List<RedConfirmResV>>> response = exchangePost(uri, entity, typeRef);
        return handleResponse(response);
    }

    /**
     * 发票开具接口
     */
    public CommonResV<InvoiceResModelV> invoiceIssue(CommonRequestParam param, InvoiceReqF invoiceReqF) {
        URI uri = getComplateUri(param);
        HttpHeaders headers = getCommonHeaders();
        HttpEntity<InvoiceReqF> entity = new HttpEntity<>(invoiceReqF, headers);
        ParameterizedTypeReference<CommonResV<InvoiceResModelV>> typeRef = new ParameterizedTypeReference<>() {};
        log.info("百望开票入参：【param】：{}，【entity】：{}", param,
                JSON.toJSONString(entity, SERIALIZE_CONFIG, SerializerFeature.WriteMapNullValue));
        ResponseEntity<CommonResV<InvoiceResModelV>> response = exchangePost(uri, entity, typeRef);
        return handleResponse(response);
    }

    /**
     * 发票查询接口
     */
    public CommonResV<List<InvoiceSearchResModelV>> invoiceSearch(CommonRequestParam param, InvoiceSearchReqF invoiceSearchReqF) {
        URI uri = getComplateUri(param);
        HttpHeaders headers = getCommonHeaders();
        HttpEntity<InvoiceSearchReqF> entity = new HttpEntity<>(invoiceSearchReqF, headers);
        ParameterizedTypeReference<CommonResV<List<InvoiceSearchResModelV>>> typeRef = new ParameterizedTypeReference<>() {};
        log.info("百望查询入参：【param】：{}，【entity】：{}", param,
                JSON.toJSONString(entity, SERIALIZE_CONFIG, SerializerFeature.WriteMapNullValue));
        ResponseEntity<CommonResV<List<InvoiceSearchResModelV>>> response = exchangePost(uri, entity, typeRef);
        return handleResponse(response);
    }

    /**
     * 发票监控信息查询接口
     */
    public CommonResV<MonitorResV> monitorSearch(CommonRequestParam param, InvoiceReqF reqF) {
        URI uri = getComplateUri(param);
        HttpHeaders headers = getCommonHeaders();
        HttpEntity<InvoiceReqF> entity = new HttpEntity<>(reqF, headers);
        ParameterizedTypeReference<CommonResV<MonitorResV>> typeRef = new ParameterizedTypeReference<>() {};
        log.info("百望监控信息查询入参：【param】：{}，【entity】：{}", param,
                JSON.toJSONString(entity, SERIALIZE_CONFIG, SerializerFeature.WriteMapNullValue));
        ResponseEntity<CommonResV<MonitorResV>> response = exchangePost(uri, entity, typeRef);
        return handleResponse(response);
    }

    private <V> CommonResV<V> handleResponse(ResponseEntity<CommonResV<V>> response) {
        if (Objects.isNull(response.getBody())) {
            throw BizException.throw400("请求百望出错，响应信息为空");
        }
        if (response.getBody().getSuccess() == Boolean.FALSE) {
            log.error("请求百望出错，响应信息为：" + JSON.toJSONString(response.getBody()));
            // 读取响应信息的错误信息
            String resErrorMsg = this.getResErrorMsg(response.getBody());
            throw BizException.throw400(resErrorMsg);
        }
        log.info("百望返回结果：{}", JSON.toJSONString(response.getBody()));
        return response.getBody();
    }

    /**
     * 获取百望响应的错误信息
     * @param errorResBody
     * @return
     */
    private String getResErrorMsg(CommonResV<?> errorResBody) {
        // 读取响应信息的错误信息
        StringBuilder errorMsg = new StringBuilder("请求百望出错");
        ErrMessageResV errMessageResV = errorResBody.getMessage();
        if (Objects.isNull(errMessageResV) || StringUtils.isBlank(errMessageResV.getErrorMessage())) {
            errorMsg.append("，响应信息为：");
            errorMsg.append(JSON.toJSONString(errorResBody));
            return errorMsg.toString();
        }
        errorMsg.append("：");
        errorMsg.append(errMessageResV.getErrorMessage());
        return errorMsg.toString();
    }

    /**
     * post执行
     * @param uri
     * @param httpEntity
     * @param typeRef
     */
    private <T> ResponseEntity<T> exchangePost(URI uri, HttpEntity<?> httpEntity, ParameterizedTypeReference<T> typeRef) {
        ResponseEntity<T> response = null;
        try {
            response = restTemplate.exchange(uri, HttpMethod.POST, httpEntity, typeRef);
        } catch (RestClientException e) {
            log.error("请求百望报错", e);
            throw BizException.throw400("请求百望连接异常或超时，请检查网络和防火墙");
        }
        return response;
    }

    private URI getComplateUri(CommonRequestParam param) {
        String uri = url + "?" +
                "method=" +
                param.getMethod() +
                "&" +
                "version=" +
                param.getVersion() +
                "&" +
                "request_id=" +
                param.getRequestId();
        try {
            return new URI(uri);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            throw BizException.throw400("百望封装URI出错，请求参数为：" + param);
        }
    }

    private HttpHeaders getCommonHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

//    private void setGBK(RestTemplate restTemplate) {
//        List<HttpMessageConverter<?>> httpMessageConverters = restTemplate.getMessageConverters();
//        httpMessageConverters.forEach(httpMessageConverter -> {
//            if (httpMessageConverter instanceof StringHttpMessageConverter) {
//                StringHttpMessageConverter messageConverter = (StringHttpMessageConverter) httpMessageConverter;
//                messageConverter.setDefaultCharset(Charset.forName("GBK"));
//            }
//        });
//    }
}
