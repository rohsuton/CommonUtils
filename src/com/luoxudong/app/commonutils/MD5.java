/**
 * Title: MD5.java
 * Description:
 * Copyright: Copyright (c) 2013 luoxudong.com
 * Company: www.luoxudong.com
 * Author 罗旭东 (hi@luoxudong.com)
 * Date 2014-7-15 上午9:55:08
 * Version 1.0
 */
package com.luoxudong.app.commonutils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/** 
 * ClassName: MD5
 * Description:TODO(这里用一句话描述这个类的作用)
 * Create by 罗旭东
 * Date 2014-7-15 上午9:55:08
 */
public class MD5 {
	private static final char[] HEX_DIGITS = { 48, 49, 50, 51, 52, 53, 54, 55,56, 57, 97, 98, 99, 100, 101, 102 };

	public static String hexdigest(String content) {
		if (content == null){
			return null;
		}
		
		try {
			return hexdigest(content.getBytes());
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}

	public static String hexdigest(byte[] input) throws NoSuchAlgorithmException {
		MessageDigest messageDigest = MessageDigest.getInstance("MD5");
		messageDigest.update(input);
		byte[] buffer = messageDigest.digest();
		char[] resultBuffer = new char[32];
		int i = 0;
		int j = 0;
		while (true) {
			if (i >= 16)
				return new String(resultBuffer);
			int k = buffer[i];
			int m = j + 1;
			resultBuffer[j] = HEX_DIGITS[(0xF & k >>> 4)];
			j = m + 1;
			resultBuffer[m] = HEX_DIGITS[(k & 0xF)];
			i++;
		}
	}
}
