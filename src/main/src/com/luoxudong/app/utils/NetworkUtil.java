
/**
 * Title: NetworkUtil.java
 * Description:
 * Copyright: Copyright (c) 2013-2015 luoxudong.com
 * Company:  个人
 * Author 罗旭东 (hi@luoxudong.com)
 * Date 2014-7-15 下午5:39:26
 * Version 1.0
 */
package com.luoxudong.app.utils;

import java.util.List;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.DetailedState;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

/** 
 * ClassName: NetworkUtil
 * Description:网络工具类
 * Create by 罗旭东
 * Date 2014-7-15 下午5:39:26
 */
public class NetworkUtil {
	/**
	 * 是否有访问网络的权限
	 * @param context 上下文
	 * @return boolean true:有权限,false:无权限
	 */
	public static boolean hasInternetPermission(Context context) {
		if (context != null) {
			return context.checkCallingOrSelfPermission("android.permission.INTERNET") == PackageManager.PERMISSION_GRANTED;
		}

		return true;
	}

	/**
	 * 网络是否可用
	 * @param context 上下文
	 * @return boolean true:网络可用 ,false:网络不可用
	 */
	public static boolean isNetworkAvailable(Context context) {
		if (context != null) {
			NetworkInfo info = getActiveNetworkInfo(context);
			return (info != null) && (info.isConnected());
		}

		return false;
	}

	/**
	 * WIFI是否有效
	 * @param context 上下文
	 * @return boolean true:WIFI可用，false:WIFI不可用
	 */
	public static boolean isWifiValid(Context context) {
		if (context != null) {
			NetworkInfo info = getActiveNetworkInfo(context);
			return (info != null) && (ConnectivityManager.TYPE_WIFI == info.getType()) && (info.isConnected());
		}

		return false;
	}

	/**
	 * 是否使用手机网络
	 * @param context 上下文
	 * @return boolean true:使用手机网络,false:不是手机网络
	 */
	public static boolean isMobileNetworkValid(Context context) {
		if (context != null) {
			NetworkInfo info = getActiveNetworkInfo(context);

			if (info == null) {
				return false;
			}

			return (info != null) && (info.getType() == ConnectivityManager.TYPE_MOBILE) && (info.isConnected());
		}

		return false;
	}

	/**
	 * 获取代表联网状态的NetWorkInfo对象
	 * @param context 山下问
	 * @return NetworkInfo 网络信息
	 */
	public static NetworkInfo getActiveNetworkInfo(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		try {
			return connectivity.getActiveNetworkInfo();
		} catch (Exception e) {
			LogUtil.e("tsp", e.toString());
		}
		return null;
	}

	/**
	 * 获取指定网络类型的NetWorkInfo对象
	 * @param context 上下文
	 * @param networkType 网络类型
	 * @return NetworkInfo 网络对象
	 */
	public static NetworkInfo getNetworkInfo(Context context, int networkType) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		return connectivityManager.getNetworkInfo(networkType);
	}

	/**
	 * 获取当前网络类型
	 * @param context 上下文
	 * @return int 网络类型
	 */
	public static int getNetworkType(Context context) {
		if (context != null) {
			NetworkInfo info = getActiveNetworkInfo(context);

			return info == null ? -1 : info.getType();
		}

		return -1;
	}

	/**
	 * WIFI状态
	 * @param context 上下文
	 * @return int WIFI状态
	 */
	public static int getWifiState(Context context) {
		WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

		if (wifi == null) {
			return WifiManager.WIFI_STATE_UNKNOWN;
		}

		return wifi.getWifiState();
	}

	/**
	 * 获取wifi链接状态
	 * @param context 上下文
	 * @return DetailedState 连接状态
	 */
	public static DetailedState getWifiConnectivityState(
			Context context) {
		NetworkInfo networkInfo = getNetworkInfo(context, ConnectivityManager.TYPE_WIFI);
		return networkInfo == null ? NetworkInfo.DetailedState.FAILED
				: networkInfo.getDetailedState();
	}

	/**
	 * 连接指定wifi
	 * @param context 上下文
	 * @param wifiSSID WIFI名称
	 * @param password 密码
	 * @return boolean true:连接成功，false:连接失败
	 */
	public static boolean wifiConnection(Context context, String wifiSSID, String password) {
		boolean isConnection = false;
		WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		String strQuotationSSID = "\"" + wifiSSID + "\"";

		WifiInfo wifiInfo = wifi.getConnectionInfo();
		if ((wifiInfo != null) && ((wifiSSID.equals(wifiInfo.getSSID())) || (strQuotationSSID.equals(wifiInfo.getSSID())))) {
			isConnection = true;
		} else {
			List<ScanResult> scanResults = wifi.getScanResults();
			if ((scanResults != null) && (scanResults.size() != 0)) {
				for (int nAllIndex = scanResults.size() - 1; nAllIndex >= 0; nAllIndex--) {
					String strScanSSID = ((ScanResult)scanResults.get(nAllIndex)).SSID;
					if ((wifiSSID.equals(strScanSSID)) || (strQuotationSSID.equals(strScanSSID))) {
						WifiConfiguration config = new WifiConfiguration();
						config.SSID = strQuotationSSID;
						config.preSharedKey = ("\"" + password + "\"");
						config.status = WifiConfiguration.Status.ENABLED;

						int nAddWifiId = wifi.addNetwork(config);
						isConnection = wifi.enableNetwork(nAddWifiId, false);
						break;
					}
				}
			}
		}

		return isConnection;
	}
}
