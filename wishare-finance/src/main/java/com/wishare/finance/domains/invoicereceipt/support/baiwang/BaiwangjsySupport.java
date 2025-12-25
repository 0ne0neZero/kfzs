package com.wishare.finance.domains.invoicereceipt.support.baiwang;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wishare.finance.domains.invoicereceipt.dto.BaiwangjsyConfigDTO;
import com.wishare.finance.domains.invoicereceipt.repository.ExternalConfigRepository;
import com.wishare.finance.infrastructure.remote.fo.external.baiwangjinshuiyun.enums.MethodEnum;
import com.wishare.finance.infrastructure.remote.fo.external.baiwangjinshuiyun.request.BlueInvoiceDataReqF;
import com.wishare.finance.infrastructure.remote.fo.external.baiwangjinshuiyun.request.InvoiceReqF;
import com.wishare.finance.infrastructure.remote.fo.external.baiwangjinshuiyun.request.RedInvoiceDataReqF;
import com.wishare.finance.infrastructure.remote.fo.external.baiwangjinshuiyun.response.CommonResV;
import com.wishare.finance.infrastructure.remote.fo.external.baiwangjinshuiyun.utils.Base64Util;
import com.wishare.finance.infrastructure.remote.fo.external.baiwangjinshuiyun.utils.HMACSHA256Util;
import com.wishare.finance.infrastructure.remote.fo.external.baiwangjinshuiyun.utils.MD5Util;
import com.wishare.finance.infrastructure.utils.WebUtil;
import com.wishare.starter.exception.BizException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import java.util.Objects;


/**
 * 发票外部申请接口-百望金税云实现
 * @author dongpeng
 * @date 2023/10/30 11:57
 */
@Slf4j
@Service
@RefreshScope
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BaiwangjsySupport {

    private final ExternalConfigRepository externalConfigRepository;

    /**
     * 百望金税云接口请求地址
     */
    @Value("${invoice.baiwangjsy.url:demo}")
    private String url;


    /**
     * 全电发票开具接口
     */
    public void qdInvoiceIssue(MethodEnum methodEnum, BlueInvoiceDataReqF dataReq, String tenantId) {
        String result = this.doPost(methodEnum, JSONUtil.toJsonStr(dataReq), tenantId);
        CommonResV commonResV = JSON.parseObject(result, CommonResV.class);
        if(!"0".equals(commonResV.getCode())){
            throw BizException.throw400("百望金税云开票失败:" + commonResV.getMessage() + ifNotNull(commonResV.getRenson()));
        }
    }



    /**
     * 全电发票查询
     * @return commonResV
     */
    public CommonResV qdInvoiceSearch(MethodEnum methodEnum, String content, String tenantId) {
        String result = this.doPost(methodEnum, content,tenantId);
        CommonResV commonResV = JSON.parseObject(result, CommonResV.class);
        if(StringUtils.isBlank(commonResV.getData())){
            return commonResV;
        }
        String data = Base64Util.decode(commonResV.getData());
        commonResV.setData(data);
        return commonResV;
    }


    /**
     * 全电红冲申请单
     */
    public CommonResV qdRedInvoice(MethodEnum methodEnum, RedInvoiceDataReqF dataReq, String tenantId) {
        String result = this.doPost(methodEnum, JSONUtil.toJsonStr(dataReq), tenantId);
        CommonResV commonResV = JSON.parseObject(result, CommonResV.class);
        if(!"0".equals(commonResV.getCode())){
            throw BizException.throw400("百望金税云开票失败:" + commonResV.getMessage() + ifNotNull(commonResV.getRenson()));
        }
        return commonResV;
    }

    /**
     * 全电红冲申请单查询
     */
    public CommonResV qdRedInvoiceSearch(MethodEnum methodEnum,String content,String tenantId) {
        String result = this.doPost(methodEnum, content, tenantId);
        CommonResV commonResV = JSON.parseObject(result, CommonResV.class);
        if (StringUtils.isNotBlank(commonResV.getData())) {
            String data = Base64Util.decode(commonResV.getData());
            commonResV.setData(data);
        }
        return commonResV;
    }



    /**
     * 向百望金税云发送post请求
     * @return
     */
    @SneakyThrows
    private String doPost(MethodEnum methodEnum, String param, String tenantId){
        String configJson = externalConfigRepository.getConfig(tenantId, "baiwangjsy", methodEnum.getValue());
        BaiwangjsyConfigDTO configDTO = JSON.parseObject(configJson, BaiwangjsyConfigDTO.class);
        InvoiceReqF req = new InvoiceReqF();
        String openUrlHost = url;
        if (StringUtils.isNotBlank(configDTO.getTempHost())) {
            openUrlHost = configDTO.getTempHost();
        }
        req.setAppid(configDTO.getAppId());
        req.setSignType(configDTO.getSignType());
        req.setServiceid(methodEnum.getValue());
        req.setContent(Base64Util.encode(param));
        String signature = "";
        switch (configDTO.getSignType()) {
            case "0":
                signature = HMACSHA256Util.sha256_HMAC("appid=" + configDTO.getAppId() + "&" + "content=" + Base64Util.encode(param) + "&" + "serviceid=" + methodEnum.getValue(), configDTO.getSecret());
                break;
            case "1":
                signature = MD5Util.getMd5(configDTO.getAppId() + configDTO.getSecret() + Base64Util.encode(param) + methodEnum.getValue());
                break;
            case "3":
                signature = "";
                break;
            default:
                throw new RuntimeException("签名类型错误");
        }
        req.setSignature(signature);
        try {
            log.info("百望金税云开票入参：【param】：{} , data : {}, hashCode:{}", JSONUtil.toJsonStr(req), param, param.hashCode());
            String result = WebUtil.doPost(openUrlHost, JSONUtil.toJsonStr(req));
            CommonResV commonResV = JSON.parseObject(result, CommonResV.class);
            log.info("百望金税云开票反参：【result】：{} , data : {}, hashCode:{}", result , Base64Util.decode(ifNotNull(commonResV.getData())), param.hashCode());
            return result;
        }catch (Exception e){
            log.error("请求百望金税云开票失败",e);
            throw BizException.throw400("请求百望连接异常或超时，请检查网络和防火墙");
        }
    }

    private String ifNotNull(String reason){
        if(StringUtils.isBlank(reason)){
            return "";
        }
        return reason;
    }
}
