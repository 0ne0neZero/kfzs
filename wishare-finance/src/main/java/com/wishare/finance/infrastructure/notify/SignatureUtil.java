package com.wishare.finance.infrastructure.notify;

import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 签名加密相关工具类
 *
 * @Author dxclay
 * @Date 2022/11/28
 * @Version 1.0
 */
public class SignatureUtil {
    private static final Logger log = LoggerFactory.getLogger(SignatureUtil.class);

    private static final String CHARSET = "UTF-8";
    private static final String RANDOM_SIGN_CHAR = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    /**
     * Base64 加密
     * @param value
     * @return
     */
    public static String base64Encode(byte[] value){
        return new String(Base64.getEncoder().encode(value));
    }

    /**
     * Base64 解密
     * @param value
     * @return
     */
    public static String base64Decode(byte[] value){
        return new String(Base64.getDecoder().decode(value));
    }

    /**
     * sha256-RSA 加密后针对结果进行BASE64加密
     * @param keyPair
     * @param value
     * @return
     * @throws NoSuchAlgorithmException
     * @throws SignatureException
     * @throws InvalidKeyException
     */
    public static String sha256RSAWithBase64(KeyPair keyPair, String value) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        return base64Encode(sha256WithRSA(keyPair, value));
    }

    /**
     * sha256-RSA 加密
     * @param keyPair
     * @param value
     * @return
     * @throws NoSuchAlgorithmException
     * @throws SignatureException
     * @throws InvalidKeyException
     */
    public static byte[] sha256WithRSA(KeyPair keyPair, String value) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        if (value == null || "".equals(value)){
            throw new IllegalArgumentException("sign value must not be null.");
        }
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(keyPair.getPrivate());
        signature.update(value.getBytes(StandardCharsets.UTF_8));
        return signature.sign();
    }


    /**
     * 签名
     * @param key
     * @param map
     * @return
     */
    public static String signHmacSha256WithSort(String key, Map<String, Object> map){
        return hmacSha256ToStr(key, sortSign(map));
    }


    /**
     * 签名
     * @param key
     * @param data
     * @return
     */
    public static String signHmacSha256WithSort(String key, Object data, String ...ignore){
        return hmacSha256ToStr(key, sortSign(beanToMap(data), ignore));
    }


    /**
     * 按照字符ASCII码排序
     * @param map
     * @return
     */
    public static String sortSign(Map<String, Object> map, String ...ignore){
        Object value;
        List<String> signKeyValueList = new ArrayList<>();
        ArrayList<String> igs = ignore == null ? new ArrayList<>() : new ArrayList<>(Arrays.asList(ignore));
        for(Map.Entry<String, Object> entry : map.entrySet()){
            value = entry.getValue();
            if (value != null && !"".equals(value) && !igs.contains(entry.getKey())){
                signKeyValueList.add(entry.getKey() + "=" + value + "&");
            }
        }
        //排序
        String[]  signKeyValues = signKeyValueList.toArray(new String[signKeyValueList.size()]);
        Arrays.sort(signKeyValues, String.CASE_INSENSITIVE_ORDER);
        StringBuilder signBuffer = new StringBuilder();
        for (String signKeyValue : signKeyValues) {
            signBuffer.append(signKeyValue);
        }
        return signBuffer.substring(0, signBuffer.length()-1);
    }

    /**
     * hmacSha156 加密
     * @param key
     * @param data
     * @return
     */
    public static byte[] hmacSha256(String key, String data){
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            byte[] secretByte = key.getBytes(CHARSET);
            byte[] dataBytes = data.getBytes(CHARSET);
            SecretKey secret = new SecretKeySpec(secretByte, "HMACSHA256");
            mac.init(secret);
            return mac.doFinal(dataBytes);
        } catch (Exception e) {
            log.info("本地支付签名异常", e);
        }
        return null;
    }

    /**
     * hmacSha156 加密
     * @param key
     * @param data
     * @return
     */
    public static String hmacSha256ToStr(String key, String data){
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            byte[] secretByte = key.getBytes(CHARSET);
            byte[] dataBytes = data.getBytes(CHARSET);
            SecretKey secret = new SecretKeySpec(secretByte, "HMACSHA256");
            mac.init(secret);
            byte[] doFinal = mac.doFinal(dataBytes);
            byte[] hexB = Hex.encode(doFinal);
            return new String(hexB);
        } catch (Exception e) {
            log.info("本地支付签名异常", e);
        }
        return null;
    }

    /**
     * sha256编码
     * @param data
     * @return
     */
    public static String sha256(String data){
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(data.getBytes(StandardCharsets.UTF_8));
            return byteToHex(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * byte数组转16进制字符
     * @param bytes
     * @return
     */
    public static String byteToHex(byte[] bytes){
        StringBuffer sb = new StringBuffer();
        String hexStr = null;
        for (byte aByte : bytes) {
            hexStr = Integer.toHexString(aByte);
            if (hexStr.length() == 1){ //一位补0
                sb.append("0");
            }
            sb.append(hexStr);
        }
        return sb.toString();
    }


    /**
     * 获取随机串
     * @param length
     * @return
     */
    public static String randomStr(int length){
        StringBuffer signRes = new StringBuffer();
        Random random = new Random();
        int defCharLength = RANDOM_SIGN_CHAR.length();
        for (int i = 0; i < length; i++) {
            signRes.append(RANDOM_SIGN_CHAR.charAt(random.nextInt(defCharLength)));
        }
        return signRes.toString();
    }

    /**
     * 获取秒级时间戳
     * @return
     */
    public static String getSecondTimeStamp(){
        return String.valueOf(System.currentTimeMillis() / 1000);
    }


    /**
     * 对象转Map
     * @param object
     * @return
     * @throws IllegalAccessException
     */
    public static Map beanToMap(Object object) {
        Map<String, Object> map = new HashMap<String, Object>();
        Field[] fields = object.getClass().getDeclaredFields();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                map.put(field.getName(), field.get(object));
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return map;
    }

    /**
     * map转bean
     * @param map
     * @param beanClass
     * @return
     * @param <T>
     * @throws Exception
     */
    public static <T> T mapToBean(Map map, Class<T> beanClass) throws Exception {
        T object = beanClass.newInstance();
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            int mod = field.getModifiers();
            if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                continue;
            }
            field.setAccessible(true);
            if (map.containsKey(field.getName())) {
                field.set(object, map.get(field.getName()));
            }
        }
        return object;
    }


    public static void main(String[] args) {
        System.out.println(randomStr(32));
        System.out.println(System.currentTimeMillis());
    }

}
