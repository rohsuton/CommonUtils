/**
 * Title: PackageUtil.java
 * Description:
 * Copyright: Copyright (c) 2013-2015 luoxudong.com
 * Company:  个人
 * Author 罗旭东 (hi@luoxudong.com)
 * Date 2014-7-30 下午4:12:39
 * Version 1.0
 */
package com.luoxudong.app.utils;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Locale;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.CallLog;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

/** 
 * ClassName: PackageUtil
 * Description:系统工具类
 * Create by 罗旭东
 * Date 2014-7-30 下午4:12:39
 */
public class PackageUtil {
	private static final String TAG = PackageUtil.class.getSimpleName();
	private static final int HONEYCOMB = 11;
	
	/**
	 * @description:获取系统版本号
	 * @return int 版本号
	 */
	public static int getVersionCode(Context context)
	{
		int verCode = 0;
		try {
			verCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
			Log.e(TAG, e.toString());
		}

		return verCode;
	}
	
	/**
	 * 获取应用程序的外部版本号
	 * @return 外部版本号
	 */
	public static String getVersionName(Context context) {
		String versionName = null;
		try {
			versionName = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			Log.e(TAG, e.toString());
		}

		return versionName;
	}

	/**
	 * 获取网卡地址
	 * @return 网卡地址
	 */
	public static String getLocalMacAddress(Context context) {
		WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = null;
		try {
			info = wifi.getConnectionInfo();
			return info.getMacAddress();
		} catch (Exception e) {
			LogUtil.e(TAG, e.toString());
		}

		return null;
	}
	
	/**
	 * 获取 string.xml 文件定义的字符串
	 * 
	 * @param resourceId 资源id
	 * @return string.xml 文件定义的字符串
	 */
	public static String getString(Context context, int resourceId) {
		Resources res = context.getResources();
		return res.getString(resourceId);
	}
	
	public static String getDeviceId(Context context) {
		String imei = getImei(context);
		String deviceId = getSerialId(context);
		
		if (!TextUtils.isEmpty(imei)){
			deviceId = imei + deviceId;
		}
		
		if (TextUtils.isEmpty(deviceId)) {
			deviceId = getLocalMacAddress(context);
		}
		
		return MD5.hexdigest(deviceId).toUpperCase();
	}

	/**
	 * 获取IMEI，MEID,ESN码或者IMSI
	 * @param context
	 * @return
	 */
	public static String getImei(Context context){
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String deviceId = null;
		try {
			deviceId = tm.getDeviceId();
			
			if (TextUtils.isEmpty(deviceId) || TextUtils.isEmpty(deviceId.replace("0", ""))){
				deviceId = tm.getSubscriberId();
			}
		} catch (Exception e) {
			LogUtil.e(TAG, e.toString());
		}
		
		return deviceId;
	}
	
	/**
	 * 根据Rom版本，制造商，CPU型号等生成ID
	 * @param context
	 * @return
	 */
	public static String getSerialId(Context context){
		String id = null;
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD){
			id = Build.SERIAL;
		}
		
		if (TextUtils.isEmpty(id)) {
			id = "build";
			id += Build.BRAND;//手机品牌
			id += Build.DEVICE;//采用的设备
			id += Build.HOST;
			id += Build.MANUFACTURER;//手机制造商
			id += Build.PRODUCT;
			id += Build.TYPE;
			id += Build.BOARD;//采用的主板
			id += Build.CPU_ABI;
			id += Build.DISPLAY;
			id += Build.ID;
			id += Build.MODEL;//手机型号
			id += Build.TAGS;
			id += Build.USER;
		}
		
		return MD5.hexdigest(id);
	}
	
	/**
	 * @return 获得手机型号
	 */
	public static String getDeviceType() {
		String deviceType = android.os.Build.MODEL;
		return deviceType;
	}

	/**
	 * 获得操作系统版本号
	 * @return 操作系统版本号
	 */
	public static String getSysVersion() {
		String sysVersion = android.os.Build.VERSION.RELEASE;
		return sysVersion;
	}
	
	/**
	 * 获取手机号码
	 * @param context 上下文
	 * @return 手机号码
	 */
	public static String getTelNumber(Context context)
	{
		String telNumber = CallLog.Calls.getLastOutgoingCall(context);
		
		return telNumber;
	}

	/**
	 * 读取manifest.xml中application标签下的配置项，如果不存在，则返回空字符串
	 * @param key 键名
	 * @return 字符串类型配置信息
	 */
	public static String getConfigString(Context context, String key) {
		String val = "";
		try {
			ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(
					context.getPackageName(), PackageManager.GET_META_DATA);
			val = appInfo.metaData.getString(key);
		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}
		return val;
	}

	/**
	 * 读取manifest.xml中application标签下的配置项
	 * @param key 键名
	 * @return 整形配置信息
	 */
	public static int getConfigInt(Context context, String key) {
		int val = 0;
		try {
			ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(
					context.getPackageName(), PackageManager.GET_META_DATA);
			val = appInfo.metaData.getInt(key);
		} catch (NameNotFoundException e) {
			Log.e(TAG, e.toString());
		}
		return val;
	}
	
	public static long getConfigLong(Context context, String key) {
		long val = 0;
		try {
			ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(
					context.getPackageName(), PackageManager.GET_META_DATA);
			val = appInfo.metaData.getLong(key);
		} catch (NameNotFoundException e) {
			Log.e(TAG, e.toString());
		}
		return val;
	}

	/**
	 * 读取manifest.xml中application标签下的配置项
	 * @param key 键名
	 * @return 布尔类型配置信息
	 */
	public static boolean getConfigBoolean(Context context, String key) {
		boolean val = false;
		try {
			ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(
					context.getPackageName(), PackageManager.GET_META_DATA);
			val = appInfo.metaData.getBoolean(key);
		} catch (NameNotFoundException e) {
			Log.e(TAG, e.toString());
		}
		return val;
	}
	
	/**
	 * 获取屏幕尺寸
	 * @return
	 */
	public static int[] getScreenSize(Context context){
		if (context == null){
			return new int[]{0, 0};
		}
		
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);
		return new int[]{dm.widthPixels, dm.heightPixels};
	}
	
	/**
	 * 获取屏幕宽度
	 * @param context 上下文
	 * @return 屏幕宽度
	 */
	public static int getScreenWidth(Context context)
	{
		return getScreenSize(context)[0];
	}
	
	/**
	 * 获取屏幕高度
	 * @param context 上下文
	 * @return 屏幕高度
	 */
	public static int getScreenHeight(Context context)
	{
		return getScreenSize(context)[1];
	}
	
	/**
	 * 获取屏幕密度
	 * @return
	 */
	public static String getScreenScale(Context context)
	{
		float scale = context.getResources().getDisplayMetrics().density;
		return String.valueOf(scale);
	}
	

	/**
	 * 指定的activity所属的应用，是否是当前手机的顶级
	 * @param context activity界面或者application
	 * @return 如果是，返回true；否则返回false
	 */
	public static boolean isTopApplication(Context context) {
		if (context == null) {
			return false;
		}

		try {
			String packageName = context.getPackageName();
			ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
			@SuppressWarnings("deprecation")
			List<RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
			if (tasksInfo.size() > 0) {
				// 应用程序位于堆栈的顶层
				if (packageName.equals(tasksInfo.get(0).topActivity.getPackageName())) {
					return true;
				}
			}
		} catch (Exception e) {
			Log.w(TAG, e.toString());
		}
		return false;
	}

	/**
	 * 判断APP是否已经打开
	 * @param context activity界面或者application
	 * @return true表示已经打开 false表示没有打开
	 */
	public static boolean isAppOpen(Context context) {
		ActivityManager mManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> mRunningApp = mManager.getRunningAppProcesses();
		int size = mRunningApp.size();
		for (int i = 0; i < size; i++) {
			if (context.getPackageName().equals(mRunningApp.get(i).processName)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 动态获取资源id
	 * 
	 * @param context
	 * 			activity界面或者application
	 * @param name
	 * 			资源名
	 * @param defType
	 * 			资源所属的类 drawable, id, string, layout等
	 * @return 资源id
	 */
	public static int getIdentifier(Context context, String name, String defType) {
		return context.getResources().getIdentifier(name, defType, context.getPackageName());
	}
	
    /**
     * 检测ActionBar是否有效
     * @return
     */
    public static boolean hasActionBar() {
        return Build.VERSION.SDK_INT >= HONEYCOMB;
    }
    
    /**
     * 根据APK路径获取版本信息
     * @param context
     * @param filePath
     * @return
     */
    public static PackageInfo getPackageInfoByApkPath(Context context, String filePath)
    {
    	PackageManager pm = context.getPackageManager();
		PackageInfo packageInfo = pm.getPackageArchiveInfo(filePath, PackageManager.GET_ACTIVITIES);
		
		return packageInfo;
    }
    
    /**
     * 根据包名获取版本信息
     * @param context
     * @param packageName
     * @return
     */
    public static PackageInfo getPackageInfoByPackageName(Context context, String packageName)
    {
    	PackageInfo packageInfo = null;
		try {
			packageInfo = context.getApplicationContext().getPackageManager().getPackageInfo(packageName, 0);
		} catch (NameNotFoundException e) {
		}
		return packageInfo;
    }
    
    /**
     * 获取位置信息
     * @param context 上下文
     * @return 位置
     */
    public static Location getLocation(Context context)
    {
    	Location location = null;
    	LocationManager locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
    	
    	Criteria criteria = new Criteria();
    	criteria.setCostAllowed(false);//设置位置服务免费 
    	criteria.setAccuracy(Criteria.ACCURACY_COARSE);//置水平位置精度
    	String providerName = locationManager.getBestProvider(criteria, true);
    	
    	if (providerName != null)
    	{
    		location = locationManager.getLastKnownLocation(providerName);
    	}
    	
    	return location;
    }
    
    public static void installApk(Context context, String filePath){
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.parse("file://" + filePath), "application/vnd.android.package-archive");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
    }
    
    public static void uninstallApk(Context context, String packageName){
    	Uri packageUri = Uri.parse("package:" + packageName);
    	Intent intent = new Intent(Intent.ACTION_DELETE, packageUri);
    	context.startActivity(intent);
    }
    
    public static boolean startAppByPackageName(Context context, String packageName){
		PackageInfo packageInfo = null;
		try {
			packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
		} catch (NameNotFoundException e) {
		}
		
		if (packageInfo == null){
			return false; 
		}
		
		Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
		resolveIntent.setPackage(packageInfo.packageName);
		
		List<ResolveInfo> resolveInfoList = context.getPackageManager().queryIntentActivities(resolveIntent, 0);
		ResolveInfo resolveInfo = resolveInfoList.iterator().next();
		
		if (resolveInfo != null){
			String activityPackageName = resolveInfo.activityInfo.packageName;
			String className = resolveInfo.activityInfo.name;
			
			Intent intent = new Intent(Intent.ACTION_MAIN);
			
			ComponentName componentName = new ComponentName(activityPackageName, className);
			intent.setComponent(componentName);
			context.startActivity(intent);
			return true;
		}
		
		return false;
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
}
