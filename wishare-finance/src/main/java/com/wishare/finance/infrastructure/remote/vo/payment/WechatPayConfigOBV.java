package com.wishare.finance.infrastructure.remote.vo.payment;

import com.wishare.tools.starter.vo.FileVo;
import lombok.Getter;
import lombok.Setter;

/**
 * 微信支付配置信息
 *
 * @Author dxclay
 * @Date 2022/11/22
 * @Version 1.0
 */
@Getter
@Setter
public class WechatPayConfigOBV {

    /**
     * 服务商户id
     */
    private String spMchId;

    /**
     * 服务商应用id
     */
    private String spAppId;

    /**
     * 商户号
     */
    private String mchNo;

    /**
     * 商户应用id
     */
    private String mchAppId;

    /**
     * 开发者密码（AppSecret）
     */
    private String appSecret;

    /**
     * APIv2秘钥
     */
    private String apiKeyV2;

    /**
     * APIv3秘钥
     */
    private String apiKeyV3;

    /**
     * 微信api版本, V2或V3
     */
    private String apiVersion;

    /**
     * 证书pkcs12格式文件（apiclient_cert.p12）
     */
    private String apiClientCertP12;

    /**
     * 证书pkcs12格式文件FileVo
     */
    private FileVo apiClientCertP12FileVo;

    /**
     * 证书pem格式文件（apiclient_cert.pem）
     */
    private String apiClientCertPem;

    /**
     * 证书pem格式文件FileVo
     */
    private FileVo apiClientCertPemFileVo;

    /**
     * 证书密钥pem格式（apiclient_key.pem）
     */
    private String apiClientKey;

    /**
     * 证书密钥pem格式FileVo
     */
    private FileVo apiClientKeyFileVo;

    /**
     * 商户API证书的证书序列号
     */
    private String clientSerialNum;


}
