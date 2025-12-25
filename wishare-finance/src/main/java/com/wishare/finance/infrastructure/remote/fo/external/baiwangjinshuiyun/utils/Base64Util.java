package com.wishare.finance.infrastructure.remote.fo.external.baiwangjinshuiyun.utils;

import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;

public class Base64Util {

	private static final String ENCODING = "UTF-8";


	public static String encode(String input) {
		return encode(input,ENCODING);
	}

	public static String encode(String input,String charsetName) {
		try {
			input = new String (Base64.encodeBase64(input.getBytes(charsetName)),charsetName);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			input = "";
		}
		return input;
	}

	public static String encode(byte[] input) {
		String ouput="";
		try {
			ouput = new String (Base64.encodeBase64(input),ENCODING);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			ouput = "";
		}
		return ouput;
	}

	public static String decode(String input) {
		return decode(input,ENCODING);
	}

	public static String decode(String input,String charsetName) {
		String ouput = "";
		try {
			byte[] decodeBase64 = Base64.decodeBase64(input.getBytes(charsetName));
			ouput = new String(decodeBase64, charsetName);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return ouput;
	}

	public static byte[] decodeArray(String input) {
		byte[] decodeBase64=new byte[0];
		try {
			decodeBase64 = Base64.decodeBase64(input.getBytes(ENCODING));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return decodeBase64;
	}
}
