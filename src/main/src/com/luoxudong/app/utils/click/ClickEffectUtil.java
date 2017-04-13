/**
 * Title: ClickEffectUtil.java
 * Description:
 * Copyright: Copyright (c) 2013-2015 luoxudong.com
 * Company: 个人
 * Author 罗旭东 (hi@luoxudong.com)
 * Date 2013-8-16 上午10:22:32
 * Version 1.0 
 */
package com.luoxudong.app.utils.click;

import android.content.Context;
import android.view.View;

/** 
 * ClassName: ClickEffectUtil
 * Description:连续点击处理
 * Create by 罗旭东
 * Date 2013-8-16 上午10:22:32
 */
public class ClickEffectUtil {
	/**
	 * 默认点击响应间隔时间
	 */
	private final long LOCK_TIME = 1000;
	
	/**
	 * 上次响应点击事件的时间
	 */
	private long lastOnClickTime = 0;
	
	/**
	 * 判断是否是合理点击
	 * 
	 * @return boolean
	 */
	public boolean isSmoothClick(long lockTime)
	{
		boolean isSmooth = true;
		long current = System.currentTimeMillis();
		
		if (lockTime < 0)
		{
			lockTime = LOCK_TIME;
		}
		
		// 如果是合理点击
		if (0 == lastOnClickTime || Math.abs(current - lastOnClickTime) > lockTime)
		{
			// 设置时间
			lastOnClickTime = current;
			// 播放动画并触发事件
			isSmooth = true;
		}
		else
		{
			isSmooth = false;
		}
		return isSmooth;
	}
	
	/**
	 * 
	 * @description:定义点击时的动画
	 * @param view
	 * @return void
	 * @throws
	 */
	public void playAnimation(View view)
	{
		
	}
	
	/**
	 * 
	 * @description:无效点击时的提示信息
	 * @param context
	 * @return void
	 * @throws
	 */
	public void slowDownDialog(Context context)
	{
		
	}
}
