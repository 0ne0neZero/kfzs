package com.wishare.contract.infrastructure.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class AESOperator {
    private String sKey;
    private String ivParameter;

    private AESOperator() {
    }

    public AESOperator(String sKey, String ivParameter) {
        this.sKey = sKey;
        this.ivParameter = ivParameter;
    }

    public String encrypt(String sSrc, String encodingFormat) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        byte[] raw = this.sKey.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        cipher.init(1, skeySpec);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes(encodingFormat));
        return Base64.getEncoder().encodeToString(encrypted);
    }

    public String decrypt(String sSrc, String encodingFormat) throws Exception {
        byte[] raw = this.sKey.getBytes("ASCII");
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(2, skeySpec);
        byte[] encrypted1 = Base64.getDecoder().decode(sSrc);
        byte[] original = cipher.doFinal(encrypted1);
        String originalString = new String(original, encodingFormat);
        return originalString;
    }

    public String encryptCBC(String sSrc, String encodingFormat) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] raw = this.sKey.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        IvParameterSpec iv = new IvParameterSpec(this.ivParameter.getBytes());
        cipher.init(1, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes(encodingFormat));
        return Base64.getEncoder().encodeToString(encrypted);
    }

    public String decryptCBC(String sSrc, String encodingFormat) throws Exception {
        byte[] raw = this.sKey.getBytes("ASCII");
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec iv = new IvParameterSpec(this.ivParameter.getBytes());
        cipher.init(2, skeySpec, iv);
        byte[] encrypted1 = Base64.getDecoder().decode(sSrc);
        byte[] original = cipher.doFinal(encrypted1);
        String originalString = new String(original, encodingFormat);
        return originalString;
    }

    public static void main(String[] args) throws Exception {
        String sKey = "8218210076300UR4";
        String ivParameter = "0392039203920300";
        AESOperator aes = new AESOperator(sKey, ivParameter);
        String cSrc = "scantype=3&class=1&PSN=LCZF2017080200130";
        System.out.println("加密前的字串是：" + cSrc);
        String enCBCString = aes.encryptCBC(cSrc, "utf-8");
        System.out.println("CBC的加密:" + enCBCString);
        String enString = aes.encrypt(cSrc, "utf-8");
        System.out.println("加密后的字串是：" + enString);
        System.out.println("yXVUkR45PFz0UfpbDB8/ew==".equals(enString));
        String DeString = aes.decrypt(enString, "utf-8");
        System.out.println("解密后的字串是：" + DeString);
    }
}
