/**
 * Title: Utility.java
 * Description:
 * Copyright: Copyright (c) 2013-2015 luoxudong.com
 * Company:  个人
 * Author 罗旭东 (hi@luoxudong.com)
 * Date 2014-7-15 下午5:09:07
 * Version 1.0
 */
package com.luoxudong.app.utils;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import java.util.UUID;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.text.TextUtils;

/** 
 * ClassName: Utility
 * Description:基础工具类
 * Create by 罗旭东
 * Date 2014-7-15 下午5:09:07
 */
public class Utility {
	/** 中文编码 */
	private static final String DEFAULT_CHARSET = "UTF-8";

	/**
	 * url编码
	 * @param url
	 * @return Bundle
	 */
	public static Bundle parseUrl(String url) {
		try {
			URL u = new URL(url);
			Bundle b = decodeUrl(u.getQuery());
			b.putAll(decodeUrl(u.getRef()));
			return b;
		} catch (MalformedURLException e) {
		}
		return new Bundle();
	}

	/**
	 * URL参数编码
	 * @param s url参数字符串
	 * @return Bundle
	 */
	public static Bundle decodeUrl(String s) {
		Bundle params = new Bundle();
		if (s != null) {
			String[] array = s.split("&");
			for (String parameter : array) {
				String[] v = parameter.split("=");
				try {
					params.putString(URLDecoder.decode(v[0], DEFAULT_CHARSET),
							URLDecoder.decode(v[1], DEFAULT_CHARSET));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		}
		return params;
	}

	/** 是否为中文环境 */
	public static boolean isChineseLocale(Context context) {
		try {
			Locale locale = context.getResources().getConfiguration().locale;
			if ((Locale.CHINA.equals(locale)) || (Locale.CHINESE.equals(locale)) || (Locale.SIMPLIFIED_CHINESE.equals(locale)) || (Locale.TAIWAN.equals(locale))){
				return true;
			}
		} catch (Exception e) {
			return true;
		}
		return false;
	}

	/** 获取随机UUID */
	public static String generateGUID() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	/**
	 * 获取APK包签名
	 * @param context
	 * @param pkgName
	 * @return
	 * @return String
	 */
	public static String getSign(Context context, String pkgName) {
		PackageInfo packageInfo;
		try {
			packageInfo = context.getPackageManager().getPackageInfo(pkgName, PackageManager.GET_RESOLVED_FILTER);
		} catch (NameNotFoundException e) {
			return null;
		}
		
		if (packageInfo.signatures == null){
			return null;
		}
		
		for (int nIndex = 0; nIndex < packageInfo.signatures.length; nIndex++) {
			byte[] buffer = packageInfo.signatures[nIndex].toByteArray();
			if (buffer != null) {
				try {
					return MD5.hexdigest(buffer);
				} catch (NoSuchAlgorithmException e) {
					return null;
				}
			}
		}
		
		return null;
	}

	/**
	 * 字符串为null时转换成空字符串
	 * @param orignal 输入字符串
	 */
	public static String safeString(String orignal) {
		return TextUtils.isEmpty(orignal) ? "" : orignal;
	}
}
