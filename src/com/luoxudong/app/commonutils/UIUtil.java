/**
 * Title: UIUtil.java
 * Description:
 * Copyright: Copyright (c) 2013 luoxudong.com
 * Company: www.luoxudong.com
 * Author 罗旭东 (hi@luoxudong.com)
 * Date 2014-7-15 下午6:03:09
 * Version 1.0
 */
package com.luoxudong.app.commonutils;

import android.app.AlertDialog;
import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/** 
 * ClassName: UIUtil
 * Description:UI工具类
 * Create by 罗旭东
 * Date 2014-7-15 下午6:03:09
 */
public class UIUtil {
	/**
	 * 弹出警告框
	 * @param context 上下文
	 * @param title 标题
	 * @param text 提示信息
	 */
	public static void showAlert(Context context, String title, String text) {
		if (context != null){
			new AlertDialog.Builder(context).setTitle(title).setMessage(text).create().show();
		}
	}

	/**
	 * 弹出警告框
	 * @param context 上下文
	 * @param titleId 标题资源ID
	 * @param textId 提示信息资源ID
	 */
	public static void showAlert(Context context, int titleId, int textId) {
		if (context != null){
			showAlert(context, context.getString(titleId),
					context.getString(textId));
		}
	}

	/**
	 * 提示toast信息
	 * @param context 上下文
	 * @param resId 内容资源id
	 * @param duration 显示时间
	 */
	public static void showToast(Context context, int resId, int duration) {
		if (context != null)
			Toast.makeText(context, resId, duration).show();
	}

	/**
	 * 提示toast信息
	 * @param context 上下文
	 * @param text 提示内容
	 * @param duration 显示时间
	 */
	public static void showToast(Context context, CharSequence text, int duration) {
		if (context != null)
			Toast.makeText(context, text, duration).show();
	}

	/**
	 * 在中间位置显示toast
	 * @param context 上下文
	 * @param resId 内容资源ID
	 * @param duration 时间
	 */
	public static void showToastInCenter(Context context, int resId, int duration) {
		if (context != null) {
			Toast toast = Toast.makeText(context, resId, duration);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}
	}
}
