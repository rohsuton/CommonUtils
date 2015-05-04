/**
 * Title: LogUtil.java
 * Description:
 * Copyright: Copyright (c) 2013 luoxudong.com
 * Company: www.luoxudong.com
 * Author 罗旭东 (hi@luoxudong.com)
 * Date 2014-7-15 下午4:26:04
 * Version 1.0
 */
package com.luoxudong.app.commonutils;

import android.util.Log;

/** 
 * ClassName: LogUtil
 * Description:日志工具类
 * Create by 罗旭东
 * Date 2014-7-15 下午4:26:04
 */
public class LogUtil {
	private static boolean sIsLogEnable = false;

	/**
	 * 使日志有效
	 */
	public static void enableLog() {
		sIsLogEnable = true;
	}

	/**
	 * 使日志无效
	 */
	public static void disableLog() {
		sIsLogEnable = false;
	}

	public static void v(String tag, String msg) {
		if (sIsLogEnable) {
			Log.v(tag, getStackTraceMsg() + ": " + msg);
		}
	}
	
	public static void d(String tag, String msg) {
		if (sIsLogEnable) {
			Log.d(tag, getStackTraceMsg() + ": " + msg);
		}
	}

	public static void i(String tag, String msg) {
		if (sIsLogEnable) {
			Log.i(tag, getStackTraceMsg() + ": " + msg);
		}
	}

	public static void w(String tag, String msg) {
		if (sIsLogEnable) {
			Log.w(tag, getStackTraceMsg() + ": " + msg);
		}
	}
	
	public static void e(String tag, String msg) {
		if (sIsLogEnable) {
			Log.e(tag, getStackTraceMsg() + ": " + msg);
		}
	}
	
	/**
	 * 定位代码位置，只能在该类内部使用
	 */
	private static String getStackTraceMsg() {
		StackTraceElement stackTrace = Thread.currentThread().getStackTrace()[4];
		String fileInfo = stackTrace.getFileName() + "(" + stackTrace.getLineNumber() + ") " + stackTrace.getMethodName();
		return fileInfo;
	}
}
