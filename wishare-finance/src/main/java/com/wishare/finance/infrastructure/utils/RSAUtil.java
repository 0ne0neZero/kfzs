package com.wishare.finance.infrastructure.utils;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * RSA签名工具类
 *
 * @Author dxclay
 * @Date 2022/12/27
 * @Version 1.0
 */
public class RSAUtil {

    private static final int SIZE = 2048;
    private static final String ALGORITHM = "RSA";
    private static final String ALGORITHM_SHA256 = "SHA256withRSA";
    private static final String TRANSFORMATION = "RSA";

    /**
     * 生成秘钥对
     *
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
        keyPairGenerator.initialize(SIZE, new SecureRandom());
        return keyPairGenerator.generateKeyPair();
    }

    /**
     * 根据秘钥对获取公钥
     *
     * @param keyPair
     * @return
     */
    public static String getPublicKey(KeyPair keyPair) {
        return base64Encode(keyPair.getPublic().getEncoded());
    }

    /**
     * 根据秘钥对获取私钥
     *
     * @param keyPair
     * @return
     */
    public static String getPrivateKey(KeyPair keyPair) {
        return base64Encode(keyPair.getPrivate().getEncoded());
    }

    /**
     * 获取RSA公钥
     *
     * @param publicKey 公钥字符串
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static PublicKey getPublicKey(String publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] byteKey = Base64.getDecoder().decode(publicKey);
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(byteKey);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        return keyFactory.generatePublic(x509EncodedKeySpec);
    }

    /**
     * 获取RSA私钥
     *
     * @param privateKey 私钥字符串
     * @return
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     */
    public static PrivateKey getPrivateKey(String privateKey) throws InvalidKeySpecException, NoSuchAlgorithmException {
        byte[] byteKey = Base64.getDecoder().decode(privateKey);
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(byteKey);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        return keyFactory.generatePrivate(pkcs8EncodedKeySpec);
    }


    public static String sign256(byte[] signData, String privateKeyStr) throws Exception {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(base64Decode(privateKeyStr));
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        Signature signature = Signature.getInstance(ALGORITHM_SHA256);
        signature.initSign(privateKey);
        signature.update(signData);
        return base64Encode(signature.sign());
    }

    /**
     * RSA公钥加密
     *
     * @param publicKeyStr 公钥字符串
     * @param data         待加密的文本
     * @return 加密后的文本
     */
    public static String encryptByPublicKey(String publicKeyStr, String data) throws Exception {
        X509EncodedKeySpec x509EncodedKeySpec2 = new X509EncodedKeySpec(base64Decode(publicKeyStr));
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec2);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return base64Encode(cipher.doFinal(data.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * RSA公钥解密
     *
     * @param publicKeyStr 公钥
     * @param data         待解密的信息
     * @return 解密后的文本
     */
    public static String decryptByPublicKey(String publicKeyStr, String data) throws Exception {
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(base64Decode(publicKeyStr));
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        return new String(cipher.doFinal(base64Decode(data)), StandardCharsets.UTF_8);
    }

    /**
     * RSA私钥加密
     *
     * @param privateKeyStr 私钥
     * @param data          待加密的信息
     * @return 加密后的文本
     */
    public static String encryptByPrivateKey(String privateKeyStr, String data) throws Exception {
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(base64Decode(privateKeyStr));
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        return base64Encode(cipher.doFinal(data.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * RSA私钥解密
     *
     * @param privateKeyStr 私钥
     * @param data          待解密的文本
     * @return 解密后的文本
     */
    public static String decryptByPrivateKey(String privateKeyStr, String data) throws Exception {
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec5 = new PKCS8EncodedKeySpec(base64Decode(privateKeyStr));
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec5);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return new String(cipher.doFinal(base64Decode(data)));
    }

    /**
     * BASE64加密
     *
     * @param data
     * @return
     */
    public static String base64Encode(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }

    /**
     * BASE64解密
     *
     * @param data
     * @return
     */
    public static byte[] base64Decode(String data) {
        return Base64.getDecoder().decode(data);
    }

}
