/**
 * Title: BytesUtil.java
 * Description:
 * Copyright: Copyright (c) 2013 luoxudong.com
 * Company:www.luoxudong.com
 * Author 罗旭东 (hi@luoxudong.com)
 * Date 2014-8-4 下午5:28:36
 * Version 1.0
 */
package com.luoxudong.app.commonutils;

import java.io.UnsupportedEncodingException;

/** 
 * ClassName: BytesUtil
 * Description:字节操作工具类
 * Create by 罗旭东
 * Date 2014-8-4 下午5:28:36
 */
public class BytesUtil {
	/**
	 * 双字节转字节数组
	 * @param word 
	 * @return byte[]
	 */
	public static byte[] word2Bytes(char word){
		byte h = (byte)(word >>> 8);//高位
		byte l = (byte)word;//低位
		return new byte[]{h,l};
	}
	
	public static char bytes2Word(byte[] bytes){
		if (bytes == null){
			return 0;
		}
		
		char ret = 0;
		ret = (char)((ret | (bytes[0] & 0xFF)) << 8);
		ret = (char)(ret | (bytes[1] & 0xFF));
		return ret;
	}
	
	public static byte[] dword2Bytes(int dWord){
		byte h1 = (byte)(dWord >>> 24);//高位
		byte h2 = (byte)(dWord >>> 16);//高位
		byte l1 = (byte)(dWord >>> 8);//高位
		byte l2 = (byte)dWord;//低位
		return new byte[]{h1, h2, l1, l2};
	}
	
	public static int bytes2DWord(byte[] bytes){
		if (bytes == null){
			return 0;
		}
		
		int ret = 0;
		ret = (ret | (bytes[0] & 0xFF)) << 24;
		ret = (ret | (bytes[1] & 0xFF)) << 16;
		ret = (ret | (bytes[2] & 0xFF)) << 8;
		ret = (ret | (bytes[3] & 0xFF));
		return ret;
	}
	
	public static int bytes2DWord(byte[] bytes, int offset){
		if (bytes == null){
			return 0;
		}
		
		int ret = 0;
		ret = (ret | (bytes[offset + 0] & 0xFF)) << 8;
		ret = (ret | (bytes[offset + 1] & 0xFF)) << 8;
		ret = (ret | (bytes[offset + 2] & 0xFF)) << 8;
		ret = (ret | (bytes[offset + 3] & 0xFF));
		
		return ret;
	}
	
	public static byte[] str2Bytes(String str, String charset){
		if (str == null){
			return null;
		}
		
		try {
			return str.getBytes(charset);
		} catch (UnsupportedEncodingException e) {
		}
		
		return null;
	}
	
	public static String bytes2Str(byte[] bytes, String charset){
		if (bytes == null){
			return null;
		}
		
		try {
			return new String(bytes, charset);
		} catch (UnsupportedEncodingException e) {
		}
		
		return null;
	}
	
	/**
	 * 字节数组转16进制字符串
	 * @param buffer 字节数组
	 * @return String 16进制字符串
	 */
	public static String bytesToHexStr(byte[] buffer){
		StringBuilder builder = new StringBuilder();
		if (buffer != null){
			for (byte b : buffer){
				String letter = Integer.toHexString(b & 0xFF).toUpperCase();
				if (letter.length() == 1){
					letter = "0" + letter;
				}
				builder.append(letter).append(" ");
			}
		}
		
		return builder.toString();
	}
}
