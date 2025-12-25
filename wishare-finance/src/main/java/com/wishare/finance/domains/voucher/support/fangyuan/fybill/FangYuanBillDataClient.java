package com.wishare.finance.domains.voucher.support.fangyuan.fybill;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.wishare.finance.domains.voucher.support.fangyuan.FangYuanBillProperties;
import com.wishare.finance.domains.voucher.support.fangyuan.model.DelF;
import com.wishare.finance.infrastructure.remote.clients.base.ExternalClient;
import com.wishare.finance.infrastructure.remote.vo.fy.FYSendresultV;
import com.wishare.finance.infrastructure.utils.Md5Utils;
import com.wishare.starter.exception.BizException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@AllArgsConstructor
@EnableConfigurationProperties(FangYuanBillProperties.class)
public class FangYuanBillDataClient {
    private final ExternalClient externalClient;

    private final FangYuanBillProperties fangYuanBillProperties;
    private final ObjectMapper objectMapper;
    private final Integer SUCCESS_CODE = 0;


    public FYSendresultV delFYBill(DelF delF) {

        String xChangeServletUrl = fangYuanBillProperties.getFYUrl() + "/nc/ncCharge/delChargeFromWy";
        HttpRequest post = header(xChangeServletUrl);
        post.body(JSONObject.toJSONString(delF));
        HttpResponse execute = post.execute();
        FYSendresultV fangYuanResult = null;
        try {
            fangYuanResult = objectMapper.readValue(execute.body(), FYSendresultV.class);
            if (!SUCCESS_CODE.equals(fangYuanResult.getCode())) {
                log.error("方圆删单接口失败错误信息，msg：{}", fangYuanResult.getMsg());
                throw BizException.throw300("方圆删单接口失败错误信息，msg：{}"
                        + fangYuanResult.getMsg());
            }
        } catch (JsonProcessingException e) {
            log.error("方圆删单接口数据解析错误，msg：{}", e.getMessage());
            throw new RuntimeException(e);
        }
        return fangYuanResult;
    }

    public FYSendresultV getXml(String toXml) {
        String xChangeServletUrl = fangYuanBillProperties.getFYUrl() + "/nc/ncCharge/sendWYChargeToNc";
        HttpRequest post = header(xChangeServletUrl);
        log.info("方圆请求信息 ：{}", JSON.toJSONString(post));
        post.body(toXml);
        HttpResponse execute = post.execute();
        String body = execute.body();
        FYSendresultV fySendresultV = JSONObject.parseObject(body, FYSendresultV.class);
        log.info("方圆推单返回信息 ：{}", JSON.toJSONString(fySendresultV));
        return fySendresultV;
    }

    private HttpRequest header(String xChangeServletUrl) {
        String accessToken = externalClient.getAccessToken();
        String timestamp = System.currentTimeMillis() + "";
        HttpRequest post = HttpUtil.createPost(xChangeServletUrl);
        post.header("timestamp", timestamp);
        post.header("appId", fangYuanBillProperties.getAppid());
        post.header("accessToken", accessToken);
        post.header("sign", Md5Utils.getMD5(
                timestamp
                        + accessToken
                        + fangYuanBillProperties.getAppid()
                        + fangYuanBillProperties.getAppSecret(), "utf-8"));
        log.info("方圆Appid信息 ：{}", fangYuanBillProperties.getAppid());
        log.info("方圆AppSecret信息 ：{}", fangYuanBillProperties.getAppSecret());
        log.info("方圆accessToken信息 ：{}", accessToken);
        return post;
    }
}
