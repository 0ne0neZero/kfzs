package com.wishare.finance.infrastructure.remote.vo.payment;

import com.wishare.tools.starter.vo.FileVo;
import lombok.Getter;
import lombok.Setter;

/**
 * 支付宝支付配置信息
 *
 * @Author dxclay
 * @Date 2022/11/22
 * @Version 1.0
 */
@Getter
@Setter
public class AlipayConfigOBV  {

    /**
     * 商户号
     */
    private String mchNo;

    /**
     * 商户应用id
     */
    private String mchAppId;

    /**
     * 应用公钥
     */
    private String publicKey;

    /**
     * 应用私钥
     */
    private String privateKey;

    /**
     * 接口签名方式(推荐使用RSA2)，默认RSA2
     */
    private String signType = "RSA2";

    /**
     * 应用公钥证书文件（.crt格式）
     */
    private String appCertPublicKey;

    /**
     * 应用公钥证书文件 FileVo
     */
    private FileVo appCertPublicKeyFileVo;

    /**
     * 支付宝公钥证书文件（.crt格式）
     */
    private String alipayCertPrivateKey;

    /**
     * 支付宝公钥证书文件 FileVo
     */
    private FileVo alipayCertPrivateKeyFileVo;

    /**
     * 支付宝根证书文件（.crt格式）
     */
    private String alipayRootCert;

    /**
     * 支付宝根证书文件 FileVo
     */
    private FileVo alipayRootCertFileVo;

    /**
     * 是否使用证书
     */
    private boolean useCert;


}
