package com.jupiterframework.util;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.lang3.time.DateFormatUtils;


/**
 * 时间处理工具类
 * 
 * @author hesp
 *
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

    /**
     * 
     * Formats a date/time into a specific pattern
     * 
     * @param date the date to format, not null
     * @param pattern the pattern to use to format the date, not null
     * @return the formatted date
     */
    public static String format(Date date, String pattern) {
        return DateFormatUtils.format(date, pattern);
    }


    /**
     * Formats a date/time into a specific pattern in a locale.
     * 
     * @param date the date to format, not null
     * @param pattern the pattern to use to format the date, not null, not null
     * @param locale the locale to use, may be null
     * @return
     */
    public static String format(final Date date, final String pattern, final Locale locale) {
        return DateFormatUtils.format(date, pattern, null, locale);
    }


    /**
     * 获取当前时间
     * 
     * @return
     */
    public static Date currentTime() {
        return new Date(System.currentTimeMillis());
    }


    /**
     * 获取当前时间字符串 yyyy-MM-dd HH:mm:ss
     * 
     * @return
     */
    public static String currentFormatTime() {
        return DateFormatUtils.format(currentTime(), "yyyy-MM-dd HH:mm:ss");
    }


    /**
     * 根据参数格式获取当前时间字符串
     * 
     * @param pattern
     * @return
     */
    public static String currentFormatTime(String pattern) {
        return DateFormatUtils.format(currentTime(), pattern);
    }
    
    /**
	 * 日期去掉时分秒
	 * 
	 */
	public static Date truncateDate(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		// 将时分秒,毫秒域清零
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}
	
	/**
	 * 转换成unix的时间戳(10位)
	 * 
	 * @param date
	 * @return
	 */
	public static Long toUnixTimestamp(Date date) {
		if (date != null) {
			return date.getTime() / 1000;
		}
		return 0L;
	}
	
	/**
	 * unix的时间戳(10位)转换成Date
	 * 
	 * @param UnixTimestamp
	 * @return
	 */
	public static Date fromUnixTimestamp(Long unixTimestamp) {
		if (unixTimestamp > 0) {
			return new Date(unixTimestamp * 1000);
		}
		return null;
	}

	/**
	 * 转换时间时区(自动计算夏令时)
	 * 
	 * 时区值详见 TimeZone.getAvailableIDs()
	 * 
	 * @param sourceDate
	 *            源时间
	 * @param sourceTimeZone
	 *            源时区 如:TimeZone.getTimeZone("Asia/Shanghai") 或
	 *            TimeZone.getDefault()
	 * @param targetTimeZone
	 *            目标时区 如: TimeZone.getTimeZone("America/Los_Angeles")、GMT、UTC
	 * @return
	 */
	public static Date transferDateTimeZone(Date sourceDate, TimeZone sourceTimeZone, TimeZone targetTimeZone) {
		if (sourceDate == null) {
			return null;
		}

		long sourceDateTime = sourceDate.getTime(); // 获取源时间毫秒数
		int timeOffset = sourceTimeZone.getOffset(sourceDateTime) - targetTimeZone.getOffset(sourceDateTime); // 获取源时区与目标时区的偏移量

		return new Date(sourceDateTime - timeOffset); // 扣除差量产生新时间
	}
}
