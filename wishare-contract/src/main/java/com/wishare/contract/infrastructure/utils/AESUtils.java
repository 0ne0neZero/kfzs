package com.wishare.contract.infrastructure.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AESUtils {

    @Value("${aescbc.key.name:Remain2017150705}")
    private String AESCBC_KEY_NAME;

    @Value("${aescbc.key.content:201707Remain1505}")
    private String AESCBC_KEY_CONTENT;

    /**
     *
     * @param qrCode 待加密信息
     * @param codeType 标识
     * @param codeClass 标识
     * @param versionId 版本/暂未使用
     * @return
     */
    public String encryptQrCode(String qrCode, String codeType, String codeClass, String versionId){
        String qrCodeOri = "scantype="+codeType+"&class="+codeClass+"&"+qrCode;
        String sKey = AESCBC_KEY_NAME;
        String ivParameter = AESCBC_KEY_CONTENT;
        AESOperator aesUtil = new AESOperator(sKey, ivParameter);
        try{
            qrCode = aesUtil.encryptCBC(qrCodeOri, "UTF-8");
            qrCode = "uama_" + qrCode;
        }catch (Exception e) {
            String errMsg = "二维码内容AES的加密错误, content:" + qrCodeOri +", sKey:" + sKey +", ivParameter:" + ivParameter;
            log.error("二维码生成失败, 错误信息: {}", errMsg);
        }
        return qrCode;
    }

    public static void main(String[] args) {
        String qrCodeOri = "scantype=1"+"&class=7"+"&111664150";
        String sKey = "Remain2017150705";
        String ivParameter = "201707Remain1505";
        AESOperator aesUtil = new AESOperator(sKey, ivParameter);
        String qrCode = null;
        try{
            qrCode = aesUtil.encryptCBC(qrCodeOri, "UTF-8");
            qrCode = "uama_" + qrCode;
        }catch (Exception e) {
            String errMsg = "二维码内容AES的加密错误, content:" + qrCodeOri +", sKey:" + sKey +", ivParameter:" + ivParameter;
            log.error("二维码生成失败, 错误信息: {}", errMsg);
        }
        System.out.println(qrCode);
    }
}
