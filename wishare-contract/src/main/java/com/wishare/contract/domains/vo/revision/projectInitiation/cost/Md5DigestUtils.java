package com.wishare.contract.domains.vo.revision.projectInitiation.cost;

import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author jd-dev
 * @Date 2022/6/13 1:35 下午
 * @Desc Md5DigestUtils
 */
@Slf4j
public class Md5DigestUtils {

    public static String getMD5Str(String str) {
        if (null == str || "".equals(str.trim())) {
            return null;
        }
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            log.error("Md5Digest Exception", e);
            throw new RuntimeException(e);
        }
        byte[] byteArray = messageDigest.digest();
        StringBuilder md5StrBuilder = new StringBuilder();
        for (int i =0; i < byteArray.length; i++) {
            if(Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
                md5StrBuilder.append("0").append(
                        Integer.toHexString(0xFF & byteArray[i]));
            } else {
                md5StrBuilder.append(Integer.toHexString(0xFF & byteArray[i]));
            }
        }
        return md5StrBuilder.toString();
    }
}
