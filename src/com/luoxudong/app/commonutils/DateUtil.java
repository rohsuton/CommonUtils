/**
 * Title: DateUtil
 * Description: 时间日期工具类
 * Copyright: Copyright (c) 2008
 * Company:深圳彩讯科技有限公司
 *
 * @author 罗旭东
 * @CreateDate 2013-5-28 下午3:52:11
 * @version 1.0
 */
package com.luoxudong.app.commonutils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DateUtil {
	/**
	 * 格式 yyyy-MM-dd HH:mm:ss
	 */
	public static final String DATE_FORMAT_DEFAULT = "yyyy-MM-dd HH:mm:ss";
	
	/**
	 * 格式 yyyyMMddHHmmss
	 */
	public static final String DATE_FORMAT_1 = "yyyyMMddHHmmss";
	
	/**
	 * 格式 yyyy-MM-dd HH:mm
	 */
	public static final String DATE_FORMAT_2 = "yyyy-MM-dd HH:mm";
	
	/**
	 * 格式 yyyy-MM-dd
	 */
	public static final String DATE_FORMAT_3 = "yyyy-MM-dd";
	
	/**
	 * 格式 yyyy年MM月dd日
	 */
	public static final String DATE_FORMAT_4 = "yyyy年MM月dd日";
	
	/**
	 * 格式 HH:mm:ss
	 */
	public static final String DATE_FORMAT_5 = "HH:mm:ss";
	
	public static final String DATE_FORMAT_6 = "yyyyMMdd.hhmmss";
	
	public static final String DATE_FORMAT_7 = "yyyy/MM/dd HH:mm";
	
	public static final String DATE_FORMAT_8 = "yyyy/MM/dd HH:mm:ss";
    
	/**
	 * 年月日时分秒
	 */
	public static final SimpleDateFormat sdf1 = new SimpleDateFormat(DATE_FORMAT_1, Locale.getDefault());
	
	public static ThreadLocal<DateFormat> threadLocal = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat(DATE_FORMAT_DEFAULT);
        }
    };
    
    public static ThreadLocal<DateFormat> threadLocal1 = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat(DATE_FORMAT_1);
        }
    };
    
    public static ThreadLocal<DateFormat> threadLocal3 = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat(DATE_FORMAT_3);
        }
    };
    
	
    /**
	 * 
	 * getDate 获取当前时间
	 * 
	 */
	public static Date getDate()
	{
		return new Date();
	}
	
	/**
	 * 
	 * getSystemCalendar 获取当前Calendar
	 */
	public static Calendar getSystemCalendar()
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(getDate());
		return cal;
	}
	
	/**
	 * 
	 * getDateString 获取字符串形式的当前时间
	 * 
	 */
	public static String getSystemDateTimeString()
	{
		Date date = getDate();
		return threadLocal.get().format(date);
	}

	/**
	 * 
	 * getDateString 获取日期字符串
	 * 
	 */
	public static String getDateString(Date date)
	{
		if (date == null)
		{
			return null;
		}
		return threadLocal.get().format(date);
	}
	
	public static String getThisDateToString(String format)
	{
		Date date = getDate();
		
		if (format == null)
		{
			format = DATE_FORMAT_DEFAULT;
		}
		
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);

		return dateFormat.format(date);
	}

	/**
	 * 
	 * getDateToString 根据指定日期格式获取对应字符串
	 * 
	 */
	public static String getDateToString(Date date, String format)
	{
		if (date == null)
		{
			return null;
		}
		
		if (format == null)
		{
			format = DATE_FORMAT_DEFAULT;
		}
		
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);

		return dateFormat.format(date);

	}
	
	public static Date getDefaultDateByParse(String dateTime)
	{
		Date date = null;
		
		try
		{
			date = threadLocal.get().parse(dateTime);
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		return date;
	}
	
	public static Date getDateByParse(String dateTime, String format)
	{
		Date date = null;
		
		try
		{
			SimpleDateFormat s = new SimpleDateFormat(format, Locale.getDefault());
			date = s.parse(dateTime);
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		return date;
	}
	
	/**
	 * 
	 * getDayOfWeek(这里用一句话描述这个方法的作用)
	 * 计算今天是星期几
	 */
	public static int getDayOfWeek()
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(getDate());
		return calendar.get(Calendar.DAY_OF_WEEK);
	}
	
	/**
	 * 
	 * getBeginAndEndDateThisWeek(这里用一句话描述这个方法的作用)
	 * 获取本周的起始截止日期
	 */
	public static List<Date> getBeginAndEndDateThisWeek()
	{
		List<Date> ret = new ArrayList<Date>();
		Calendar calendar = Calendar.getInstance();
		int weekday = calendar.get(7) - 2;
		calendar.add(5, -weekday);
		ret.add(calendar.getTime());
		calendar.add(5, 6);
		ret.add(calendar.getTime());
		return ret;
	}
	
	/**
	 * 获取相对于今天的日期
	 * @param rel
	 * @return
	 */
	public static Date getRelativeDate(int num)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(getDate());
		calendar.set(Calendar.DATE,calendar.get(Calendar.DATE) + num);  
		return calendar.getTime();
	}
	
	/**
	 * 获取相对于指定日期的天数
	 * @param relDate
	 * @param num
	 * @return
	 */
	public static Date getRelativeDate(Date relDate, int num)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(relDate);
		calendar.set(Calendar.DATE,calendar.get(Calendar.DATE) + num);  
		return calendar.getTime();
	}
	
	/**
	 * 计算时间差
	 * [Summary]
	 *       getTimeDiffDays 请用一句话描述这个方法的作用
	 *
	 */
	public static int getTimeDiffDays(Date data1, Date date2){
		Calendar calendar1 = Calendar.getInstance();  
		calendar1.setTime(data1);  
		calendar1.set(Calendar.HOUR_OF_DAY, 0);  
		calendar1.set(Calendar.MINUTE, 0);  
		calendar1.set(Calendar.SECOND, 0);  
		calendar1.set(Calendar.MILLISECOND, 0);  
  
        Calendar calendar2 = Calendar.getInstance();  
        calendar2.setTime(date2);  
        calendar2.set(Calendar.HOUR_OF_DAY, 0);  
        calendar2.set(Calendar.MINUTE, 0);  
        calendar2.set(Calendar.SECOND, 0);  
        calendar2.set(Calendar.MILLISECOND, 0);  
        
		long time1 = calendar1.getTime().getTime();
		long time2 = calendar2.getTime().getTime();
		
		int days = Math.abs((int)((time1 - time2)/(1000 * 60 * 60 * 24)));
        return days;  
	}
}
