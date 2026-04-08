package cn.exrick.manager.isearch.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cn.exrick.manager.isearch.util.common.Convert;

public class DateUtil {

	public static final String DATE_FORMAT = "yyyy-MM-dd";
	public static final String HOUR_FORMAT = "yyyy-MM-dd HH";
	public static final String MINUTE_FORMAT = "yyyy-MM-dd HH:mm";
	public static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	public static String long2Str(long l, String format) {
		return date2Str(new Date(l), format);
	}
	
	public static String date2Str(Date date, String format) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(date);
	}

	public static Date getDate(String s) {
		Date date = null;
		if (s == null)
			return new Date();
		try {
			if (s.length() > 10) {
				date = new SimpleDateFormat(TIME_FORMAT).parse(s);
			} else {
				date = new SimpleDateFormat(DATE_FORMAT).parse(s);
			}
		} catch (Exception e) {

		}
		return date;
	}

	public static Date getDate(String s, String dateFormat) {
		Date date = null;
		if (s == null)
			return null;
		try {
			date = new SimpleDateFormat(dateFormat).parse(s);
		} catch (Exception e) {

		}
		return date;
	}

	/**
	 * 获得时间点已清零的 long 时间
	 */
	public static long getClearedTime(int offset) {
		return getClearedTime(null, offset);
	}

	/**
	 * 获得时间点已清零的 long 时间
	 */
	public static long getClearedTime(String s, int offset) {
		Date date = getDate(s);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		c.set(Calendar.DATE, c.get(Calendar.DATE) - offset);
		return c.getTimeInMillis();
	}

	/**
	 * 获得String型当前时间
	 */
	public static String getCurrDate(String dateFormat) {
		return new SimpleDateFormat(dateFormat).format(new Date());
	}

	/**
	 * SearchSystem 检索查询时间
	 */
	public static long[] getSearchTime(String period) {
		if (period == null || period.length() == 0)
			period = "30";
		long[] time = new long[2];
		if (period.indexOf(",") != -1) {
			String[] array = Convert.split(period, ",");
			SimpleDateFormat dateFormat = new SimpleDateFormat(TIME_FORMAT);
			try {
				time[1] = Long.parseLong(array[1]);
			} catch (Exception e) {
				try {
					time[1] = dateFormat.parse(array[1]).getTime();
				} catch (Exception ex) {
					time[1] = new Date().getTime();
				}
			}
			try {
				time[0] = Long.parseLong(array[0]);
			} catch (Exception e) {
				try {
					time[0] = dateFormat.parse(array[0]).getTime();
				} catch (Exception ex) {
					time[0] = time[1] - 3600 * 1000l * 24 * 3;
				}
			}
		} else if ("30".equals(period) || "7".equals(period) || "3".equals(period) || "1".equals(period)||"90".equals(period)||"180".equals(period)) {
			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			time[1] = c.getTimeInMillis();
			c.set(Calendar.DATE, c.get(Calendar.DATE) - Convert.getInt(period));
			time[0] = c.getTimeInMillis();
		}
		return time;
	}

	

}
