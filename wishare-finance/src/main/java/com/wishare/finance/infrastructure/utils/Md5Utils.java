package com.wishare.finance.infrastructure.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

/**
 * @author fengxiaolin
 * @date 2023/6/17
 */
public class Md5Utils {

    private static final int HEX_VALUE_COUNT = 16;

    public Md5Utils() {
    }

    public static String getMD5(byte[] bytes) {
        char[] hexDigits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        char[] str = new char[32];

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(bytes);
            byte[] tmp = md.digest();
            int k = 0;

            for(int i = 0; i < 16; ++i) {
                byte byte0 = tmp[i];
                str[k++] = hexDigits[byte0 >>> 4 & 15];
                str[k++] = hexDigits[byte0 & 15];
            }
        } catch (Exception var8) {
            var8.printStackTrace();
        }

        return new String(str);
    }

    public static String getMD5(String value, String encode) {
        String result = "";

        try {
            result = getMD5(value.getBytes(encode));
        } catch (UnsupportedEncodingException var4) {
            var4.printStackTrace();
        }

        return result;
    }

    public static void main(String[] args) {
        String accessToken = "7dc8fc7fb19db456f4aff6cb8a9d0615";
        String appId = "c60dfdc06c8a460dbfd47f7c7d5c4f71";
        String appSecret = "bc4464444b0a0ec8";
        String timeStamp = System.currentTimeMillis() + "";
        System.out.println("timeStamp = " + timeStamp);
        String sign = Md5Utils.getMD5(
                timeStamp
                        + accessToken
                        + appId
                        + appSecret, "utf-8");

        System.out.println(sign);
    }
}
