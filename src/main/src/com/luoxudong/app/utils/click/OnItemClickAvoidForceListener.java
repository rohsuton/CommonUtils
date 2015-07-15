/**
 * Title: OnItemClickAvoidForceListener.java
 * Description:
 * Copyright: Copyright (c) 2013-2015 luoxudong.com
 * Company: 个人
 * Author 罗旭东 (hi@luoxudong.com)
 * Date 2013-8-16 上午10:27:12
 * Version 1.0 
 */
package com.luoxudong.app.utils.click;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

/** 
 * ClassName: OnItemClickAvoidForceListener
 * Description:Item的防爆力点击事件
 * Create by 罗旭东
 * Date 2013-8-16 上午10:27:12
 */
public abstract class OnItemClickAvoidForceListener implements OnItemClickListener{
	/**
	 * 点击响应间隔时间,小于0为默认值
	 */
	private long lockTime = -1;
	
	private ClickEffectUtil util = new ClickEffectUtil();
	
	/**
	 * 点击响应间隔时间
	 */
	public void setLockTime(long lockTime)
	{
		this.lockTime = lockTime;
	}
	
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		// 如果是正常点击
		if (util.isSmoothClick(lockTime))
		{
			// 播放动画并触发事件
			util.playAnimation(view);
			onItemClickAvoidForce(parent, view, position, id);
		}
		else
		{
			util.slowDownDialog(view.getContext());
		}
	}

	public abstract void onItemClickAvoidForce(AdapterView<?> parent, View view, int position, long id);
}
