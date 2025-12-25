package com.wishare.finance.infrastructure.remote.fo.external.baiwangjinshuiyun.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {
	
	public static final String MD5 = "MD5";
	public static final String ENCODING = "UTF-8";

	/**
	 * md5 计算签名值
	 * @param args
	 */
	public static void main(String[] args) {
		// appid + secret + content + serviceid
		String s = "appid"+"secret" + "content内容base编码后的字符串" +  "serviceid";
		System.out.println(getMd5(s));
	}
	/**
	 * 生成MD5
	 * 
	 * @param plainText
	 * @return
	 */
	public static String getMd5(String plainText) {
		try {
			MessageDigest md = MessageDigest.getInstance(MD5);
			md.update(plainText.getBytes(ENCODING));
			byte b[] = md.digest();

			int i;

			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			// 32位加密
			return buf.toString();
			// 16位的加密
			// return buf.toString().substring(8, 24);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}
	

}
