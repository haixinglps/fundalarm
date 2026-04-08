package cn.exrick.common.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;

//import com.polyinfo.library.utils.DateTimeTool;

/**
 * <p>
 * Title:日期工具类DateTimeTools
 * </p>
 * <p>
 * Description: 提供日期API
 * </p>
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * 
 * @author sunhj
 * @createDate 2018-08-01
 * @version 1.0
 */
public class DateTimeTool {

	/**
	 * 获得日期的天数，比如"2006-5-1",返回值为1
	 * 
	 * @param date
	 *            String
	 * @return String
	 */
	public static String getDay(String date) {
		if (date == null) {
			throw new IllegalArgumentException("DateTimeTools.getDay:入参date为空指针");
		}
		int pos = date.lastIndexOf("-") + 1;
		String day = date.substring(pos, date.length());
		return day;
	}

	/**
	 * 获得年份
	 * 
	 * @param date
	 *            String
	 * @return String
	 */
	public static String getYear(String date) {
		if (date == null) {
			throw new IllegalArgumentException("DateTimeTools.getYear:入参date为空指针");
		}
		String[] v = date.split("-");
		return v[0];
	}

	/**
	 * 获得月份
	 * 
	 * @param date
	 *            String
	 * @return String
	 */
	public static String getMonth(String date) {
		if (date == null) {
			throw new IllegalArgumentException("DateTimeTools.getMonth:入参date为空指针");
		}
		String[] v = date.split("-");
		String month = v[1];
		if (month.startsWith("0") && month.length() == 2) {
			month = month.charAt(1) + "";
		}
		return month;
	}

	/**
	 * 得到今天的日期,比如"2007-1-23"
	 * 
	 * @return String
	 */
	public static String getToday() {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat dateFormatter = getDateFormatter(); // 获得时间样式
		Date today = calendar.getTime(); // 今天的日期
		String theDay = dateFormatter.format(today); // 对今天的日期进行格式化
		return theDay;

	}

	/**
	 * 求出两个时间的差值，单位为秒
	 * 
	 * @param startTime
	 *            String
	 * @param endTime
	 *            String
	 * @return int 阳艳添加于2007-7-25
	 */
	public static long sencondsBetweenDate(String beginDate, String endDate) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String beginTime = formatMethod(beginDate);
		String endTime = formatMethod(endDate);
		Date beginDay = formatter.parse(beginTime);
		Date endDay = formatter.parse(endTime);
		long beginDateMs = beginDay.getTime();
		long endDateMs = endDay.getTime();
		long interval = Math.abs((endDateMs - beginDateMs) / 1000);
		return interval;
	}

	private static String formatMethod(String beginDate) {
		String beginTime = beginDate;
		if (beginDate.length() == 15) {
			beginTime = beginDate + "0:00";
		} else if (beginDate.length() == 13) {
			beginTime = beginDate + ":00:00";
		} else if (beginDate.length() == 10) {
			beginTime = beginDate + " 00:00:00";
		} else if (beginDate.length() == 4) {
			beginTime = "2007-09-09 " + beginDate + "0:00";
		}
		return beginTime;
	}

	/**
	 * 获得标准的时间样式，即"yyyy-MM-dd"
	 * 
	 * @return SimpleDateFormat
	 */
	private static SimpleDateFormat getDateFormatter() {
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd"); // 日期样式
		return dateFormatter;
	}

	/**
	 * 获得标准的日期时间样式，即"yyyy-MM-dd HH:mm:ss"
	 * 
	 * @return SimpleDateFormat
	 */
	private static SimpleDateFormat getDateTimeFormatter() {
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 日期样式
		return dateFormatter;
	}

	/**
	 * 得到当前的日期及时间,如"2007-4-17 20:30:40"
	 * 
	 * @return String 当前的时间
	 */
	public static String getNow() {
		Date now = new Date();
		SimpleDateFormat dateFormatter = getDateTimeFormatter();
		String dateTime = dateFormatter.format(now);
		return dateTime;
	}

	/**
	 * 得到当前的日期及时间,如"2007417203040"
	 * 
	 * @return String 当前的时间
	 */
	public static String getTimeNow() {
		Date now = new Date();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyyMMddHHmmss");
		String dateTime = dateFormatter.format(now);
		return dateTime;
	}

	/**
	 * 得到7天前的时间 如：2007-12-11 12:25:59
	 * 
	 * @return String
	 */
	public static String getDateOneWeekBefore() {
		Date today = new Date(); // 今天的日期
		long time = (today.getTime() / 1000) - 7 * 24 * 60 * 60;
		Date halfHour = new Date();
		halfHour.setTime(time * 1000);
		// DateFormat dateFormatter = DateFormat.getDateTimeInstance();
		SimpleDateFormat dateFormatter = getDateTimeFormatter(); // 获得日期-时间样式
		String dateTime = dateFormatter.format(halfHour); // 格式化日期
		return dateTime;
	}

	/**
	 * 获得指定的日期所在周的周一的日期，比如输入为2007-4-10,输出为2007-4-9
	 * 
	 * @param date
	 *            String
	 * @return String
	 */
	public static String getDateOfMonday(String date) {
		if (date == null) {
			throw new IllegalArgumentException("DateTimeTools.getDateOfMonday():入参date为空指针");
		}

		Calendar calendar = Calendar.getInstance();
		// 对日期进行分割成年，月，日
		String[] dateArray = date.split("-");
		int year = Integer.parseInt(dateArray[0]); // 年份
		int month = Integer.parseInt(dateArray[1]); // 月份
		int day = Integer.parseInt(dateArray[2]); // 日
		calendar.set(year, month - 1, day);
		int dayOfWeek = getDayOfWeek(year, month, day); // 取得周日的星期数（1－7）
		calendar.add(Calendar.DAY_OF_MONTH, -(dayOfWeek - 1)); // 取得星期一的日期
		SimpleDateFormat formatter = getDateFormatter(); // 格式化日期
		return formatter.format(calendar.getTime());
	}

	/**
	 * 根据日期，获得该天为星期几，星期一到星期日分别对应1-7
	 * 
	 * @param year
	 *            int
	 * @param month
	 *            int
	 * @param day
	 *            int
	 * @return int
	 */
	private static int getDayOfWeek(int year, int month, int day) {
		if (month < 3) {
			month += 12;
			--year;
		}
		int dayOfWeek = (day + 1 + 2 * month + 3 * (month + 1) / 5 + year + (year >> 2) - year / 100 + year / 400) % 7;
		if (dayOfWeek == 0) {
			dayOfWeek = 7;
		}
		return dayOfWeek;
	}

	/**
	 * 比较两个时间的大小，时间的合法格式是"2006-4-17 20:30:50"
	 * 
	 * @param firstDateTime
	 *            String 第一个时间
	 * @param secondDateTime
	 *            String 第二个时间
	 * @return int
	 */
	public static int compareDateTime(String firstDateTime, String secondDateTime) throws ParseException {
		if (firstDateTime == null) {
			throw new IllegalArgumentException("DateTools.compareDate:入参beginDate为空指针");
		}
		if (secondDateTime == null) {
			throw new IllegalArgumentException("DateTools.compareDate:入参endDate为空指针");
		}
		// SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat formatter = getDateTimeFormatter(); // 获得日期-时间样式
		Date start = null;
		Date end = null;
		try {
			start = formatter.parse(firstDateTime); // 起始时间
			end = formatter.parse(secondDateTime); // 结束时间
		} catch (ParseException ex) {
			throw ex;
		}
		long diff = end.getTime() - start.getTime(); // 获得差值
		if (diff == 0) { // 两者相等
			return 0;
		} else if (diff > 0) { // 结束时间大于起始时间
			return 1;
		} else { // 结束时间小于起始时间
			return -1;
		}
	}

	/**
	 * 比较时间，t1是否早于t2，不能等于 日期格式：yyyy-MM-dd HH:mm:ss
	 * 
	 * @param t1
	 * @param t2
	 * @return
	 */
	public static boolean compareTo(String t1, String t2) {
		int i = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date time1 = sdf.parse(t1);
			Date time2 = sdf.parse(t2);
			i = time1.compareTo(time2);
		} catch (Exception e) {
			i = 0;
		}
		if (i < 0) {
			return true;
		} else {
			// 包括t1等于t2
			return false;
		}
	}

	/**
	 * 比较时间，t1是否早于t2，不能等于 日期格式：yyyy-MM-dd
	 * 
	 * @param t1
	 * @param t2
	 * @return
	 */
	public static boolean compareToSrc(String t1, String t2) {
		int i = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date time1 = sdf.parse(t1 + " 00:00:00");
			Date time2 = sdf.parse(t2 + " 00:00:00");
			i = time1.compareTo(time2);
		} catch (Exception e) {
			i = 0;
		}
		if (i < 0) {
			return true;
		} else {
			// 包括t1等于t2
			return false;
		}
	}

	/**
	 * 格式时间 格式时间值为空或者转化错误，则返回当前时间的格式
	 * 
	 * @param basicDate
	 *            格式时间
	 * @param strFormat
	 * @return
	 */
	public static String formatDate(Date basicDate, String strFormat) {
		SimpleDateFormat df = new SimpleDateFormat(strFormat);
		return df.format(basicDate);
	}

	public static String formatDate(String strFormat) {
		return formatDate(new Date(), strFormat);
	}

	public static String formatDate(String basicDate, String strFormat) {
		SimpleDateFormat df = new SimpleDateFormat(strFormat);
		Date tmpDate = null;
		try {
			tmpDate = df.parse(basicDate);
		} catch (Exception e) {
			// 日期型字符串格式错误
			tmpDate = new Date();
		}
		return df.format(tmpDate);
	}

	/**
	 * 计算两个日期相隔的天数
	 * 
	 * @param firstDate
	 * @param secondDate
	 * @return
	 */
	public static int daysBetweenTwoDate(Date firstDate, Date secondDate) {
		int days = (int) ((secondDate.getTime() - firstDate.getTime()) / (24 * 60 * 60 * 1000));
		return days;
	}

	public static long secondsBetweenTwoData(Date firstDate, Date secondDate) {
		return secondDate.getTime() - firstDate.getTime();
	}

	public static int daysBetweenTwoDate(String firstString, String secondString) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date firstDate = null;
		Date secondDate = null;
		try {
			firstDate = df.parse(firstString);
			secondDate = df.parse(secondString);
			return daysBetweenTwoDate(firstDate, secondDate);
		} catch (Exception e) {
			// 日期型字符串格式错误
			return 0;
		}
	}

	/**
	 * 取得System.currentTimeMillis()的值
	 * 
	 * @return long型
	 */
	public static long getLongTime() {
		return System.currentTimeMillis();
	}

	/**
	 * 取得System.currentTimeMillis()的值
	 * 
	 * @return long型
	 */
	public static long getFormatTime(Date date, String format) {
		if (null == date)
			date = new Date();
		DateFormat simpleFormat = new SimpleDateFormat(format);
		String times = simpleFormat.format(date);

		return strToDateFormat(times, format).getTime();
	}

	/**
	 * 格式化日期yyyy-MM-dd HH:mm:ss
	 * 
	 * @param date
	 * @return
	 */
	public static String dateToStr(Date date) {
		if (date == null)
			return "";

		return formatDate(date, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 格式化日期yyyy-MM-dd HH:mm:ss
	 * 
	 * @param date
	 * @return
	 */
	public static String dateToFormat(Date date) {
		if (date == null)
			return "";

		return formatDate(date, "yyyy-MM-dd");
	}

	/**
	 * 格式化日期，格式出错返回当前时间
	 * 
	 * @param str
	 * @return
	 */
	public static Date strToDate(String str) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = df.parse(str);
		} catch (Exception e) {
			e.printStackTrace();
			date = new Date();
		}
		return date;
	}

	/**
	 * 格式化日期，格式出错返回当前时间
	 * 
	 * @param str
	 * @return
	 */
	public static Date strToDateFormat(String str, String format) {
		DateFormat df = new SimpleDateFormat(format);
		Date date = null;
		try {
			date = df.parse(str);
		} catch (Exception e) {
			e.printStackTrace();
			date = new Date();
		}
		return date;
	}

	/**
	 * 格式化日期，格式出错返回当前时间
	 * 
	 * @param str
	 * @return
	 */
	public static Date strToDateFormat(String str) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = df.parse(str);
		} catch (Exception e) {
			e.printStackTrace();
			date = new Date();
		}
		return date;
	}

	/**
	 * 计算日期
	 * 
	 * @param date
	 * @param dateNum
	 * @param hourNum
	 * @param minuteNum
	 * @param secondNum
	 * @return
	 */
	public static String calDateTime(String date, int dateNum, int hourNum, int minuteNum, int secondNum) {
		String result = "";
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		try {
			cal.setTime(sd.parse(date));
			cal.add(Calendar.DATE, dateNum);
			cal.add(Calendar.HOUR, hourNum);
			cal.add(Calendar.MINUTE, minuteNum);
			cal.add(Calendar.SECOND, secondNum);
			result = sd.format(cal.getTime());
		} catch (Exception e) {
			result = date;
		}
		return result;
	}

	public static String intToDatestring(String date, int hourNum) {
		String result = "";
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		try {
			cal.setTime(sd.parse(date));
			cal.add(Calendar.HOUR, hourNum);
			result = sdString.format(cal.getTime());
		} catch (Exception e) {
			result = date;
		}
		return result;
	}

	public static String calDateTime(int minuteNum) {
		return calDateTime(getLocalDate(), 0, 0, minuteNum, 0);
	}

	/**
	 * 日期计算
	 * 
	 * @param date
	 *            起始日期
	 * @param yearNum
	 *            年数
	 * @param monthNum
	 *            月数
	 * @param dateNum
	 *            日数
	 * 
	 * @return
	 */
	public static String calDate(String date, int yearNum, int monthNum, int dateNum) {
		String result = "";
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		try {
			cal.setTime(sd.parse(date));
			cal.add(Calendar.MONTH, monthNum);
			cal.add(Calendar.YEAR, yearNum);
			cal.add(Calendar.DATE, dateNum);
			result = sd.format(cal.getTime());
		} catch (Exception e) {
			result = date;
		}
		return result;
	}

	public static String calDate(int yearNum, int monthNum, int dateNum) {
		return calDate(getLocalDate(), yearNum, monthNum, dateNum);
	}

	public static String calDate(int monthNum, int dateNum) {
		return calDate(getLocalDate(), 0, monthNum, dateNum);
	}

	public static String calDate(int dateNum) {
		return calDate(getLocalDate(), 0, 0, dateNum);
	}

	/**
	 * 返回当前时间，格式'yyyy-mm-dd HH:mm:ss'
	 * 
	 * @return
	 */
	public static String getLocalDate() {
		return formatDate("yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 返回当前时间的前n分钟时间i
	 * 
	 * @return
	 */
	public static String getNDate(int n) {
		Date date = new Date(System.currentTimeMillis() - n * 60 * 1000);
		SimpleDateFormat sp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sp.format(date);
	}
	/**
	 * 返回当前时间的后n分钟时间
	 * 2019年2月14日 下午3:50:47
	 * Administrator:sunhj
	 * @param n
	 * @return
	 */
	public static String getAfterNDate(int n) {
		Date date = new Date(System.currentTimeMillis() + n*60 * 60 * 1000);
		SimpleDateFormat sp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sp.format(date);
	}

	/**
	 * 返回当前时间的前n分钟时间
	 * 
	 * @return
	 */
	public static String getNDate(int n, String strFormat) {
		strFormat = null == strFormat ? "HH:mm:ss" : strFormat;
		Date date = new Date(System.currentTimeMillis() - n * 60 * 1000);
		SimpleDateFormat sp = new SimpleDateFormat(strFormat);
		return sp.format(date);
	}

	/**
	 * 返回当前时间，格式'yyyyMMddHHmmss'
	 * 
	 * @return
	 */
	public static String getSimpleDate() {
		return formatDate("yyyyMMddHHmmss");
	}

	/**
	 * 返回格式：2004年9月13日
	 * 
	 * @param localDate
	 * @return
	 */
	public static String parseCnDate(String localDate) {
		String sYear = localDate.substring(0, 4);
		String sMonth = delFrontZero(localDate.substring(5, 7));
		String sDay = delFrontZero(localDate.substring(8, 10));
		return sYear + "年" + sMonth + "月" + sDay + "日";
	}

	/**
	 * 返回格式：9.13
	 * 
	 * @param localDate
	 * @return
	 */
	public static String parsePointDate(String localDate) {
		String sMonth = delFrontZero(localDate.substring(5, 7));
		String sDay = delFrontZero(localDate.substring(8, 10));
		return sMonth + "." + sDay;
	}

	/**
	 * 除去字串前面的0
	 * 
	 * @param mord
	 * @return
	 */
	public static String delFrontZero(String mord) {
		int i = 0;
		try {
			i = Integer.parseInt(mord);
			return String.valueOf(i);
		} catch (Exception e) {
			return mord;
		}
	}

	/**
	 * 当前月份后一个月
	 * 
	 * @param mord
	 * @return
	 */
	public static String nextMouth(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");// 格式化对象
		Calendar calendar = Calendar.getInstance();// 日历对象
		calendar.setTime(date);// 设置当前日期
		calendar.add(Calendar.MONTH, +2);// 月份加二
		return sdf.format(calendar.getTime());// 输出格式
	}

	/**
	 * 当前日的前n天
	 * 
	 * @param mord
	 * @return
	 */
	public static String nextDate(int n) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");// 格式化对象
		Calendar calendar = Calendar.getInstance();// 日历对象
		calendar.setTime(new Date());// 设置当前日期
		calendar.add(Calendar.DATE, -n);// 月份加二
		return sdf.format(calendar.getTime());// 输出格式
	}

	/**
	 * 获取从某个时间开始到某个时间结束 中间时间集合
	 * 
	 * @param mord
	 * @return
	 */
	public static List<String> startAndEndStringList(String start, String end) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");// 格式化对象
		Date startdate = null;
		Date enddate = null;
		List<String> daysList = new ArrayList<String>();
		try {
			startdate = sdf.parse(start);
			enddate = sdf.parse(end);
			int nday = daysBetweenTwoDate(startdate, enddate);

			for (int i = 0; i <= nday; i++) {
				Calendar calendar = Calendar.getInstance();// 日历对象
				calendar.setTime(startdate);// 设置当前日期
				calendar.add(Calendar.DATE, i);// 月份加二
				daysList.add(sdf.format(calendar.getTime()));// 输出格式
			}
		} catch (ParseException e) {

			e.printStackTrace();
		}
		return daysList;
	}

	/**
	 * 当前月份后一个月
	 * 
	 * @param mord
	 * @return
	 */
	public static String getYesterday() {
		Date date = new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 24);
		SimpleDateFormat sp = new SimpleDateFormat("yyyy-MM-dd");
		return sp.format(date);// 获取昨天日期
	}

	/**
	 * 获得日期字符串数组
	 * 
	 * @param calendarType
	 *            日期跨度的类型，
	 */

	public static Date[] getDateArrays(String start, String end) {
		ArrayList<Date> ret = new ArrayList<Date>();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(DateTimeTool.strToDate(start));
		Date tmpDate = calendar.getTime();
		long endTime = DateTimeTool.strToDate(end).getTime();
		int count = 0;
		while (tmpDate.before(DateTimeTool.strToDate(end)) || tmpDate.getTime() == endTime) {
			ret.add(calendar.getTime());
			calendar.add(Calendar.DAY_OF_YEAR, 1);
			tmpDate = calendar.getTime();
			count++;
			if (count >= 60)
				break;
		}

		Date[] dates = new Date[ret.size()];
		return ret.toArray(dates);
	}

	/**
	 * 获得日期字符串数组
	 * 
	 * @param calendarType
	 *            日期跨度的类型，
	 */

	// public static Collection<String> dateToArrays(String start,String end)
	// {
	// ArrayList<String> ret = new ArrayList<String>();
	// Calendar calendar = Calendar.getInstance();
	// calendar.setTime(DateUtil.strToDate(start));
	// Date tmpDate = calendar.getTime();
	// long endTime = DateUtil.strToDate(end).getTime();
	//
	// while(tmpDate.before(DateUtil.strToDate(end)) || tmpDate.getTime() ==
	// endTime){
	// ret.add(formatDate(calendar.getTime(),"yyyy-MM-dd"));
	// calendar.add(Calendar.DAY_OF_YEAR,1);
	// tmpDate = calendar.getTime();
	// }
	//
	// return ret.size() >= 60 ? ret.subList(ret.size()-61, ret.size()):ret;
	// }

	/**
	 * 获得一段时间内，有多少个周日
	 * 
	 * @param calendarType
	 *            日期跨度的类型，
	 */

	// public static List<String> getWeekListSrc(String start,String end){
	// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//格式化对象
	// ArrayList<String> ret = new ArrayList<String>();
	// Calendar calendar = Calendar.getInstance();
	// calendar.setTime(DateUtil.strToDate(start));
	// Date tmpDate = calendar.getTime();
	// long endTime = DateUtil.strToDate(end).getTime();
	//
	// int count = 0;
	// while(tmpDate.before(DateUtil.strToDate(end)) || tmpDate.getTime() ==
	// endTime){
	//
	// if(calendar.get(Calendar.DAY_OF_WEEK) == 1){
	// ret.add(sdf.format(calendar.getTime()));
	// count ++;
	// if(count >= 50) break;
	// }
	//
	// calendar.add(Calendar.DAY_OF_YEAR,1);
	// tmpDate = calendar.getTime();
	// }
	// return ret;
	// }
	/**
	 * 获得一段时间内，有多少个周日
	 * 
	 * @param calendarType
	 *            日期跨度的类型，
	 */

	public static List<String> getmonthListSrc(String start, String end) {
		ArrayList<String> ret = new ArrayList<String>();
		String s = getMothlastDate(start, 0);
		String e = getMothlastDate(end, 0);
		ret.add(s);
		while (compareToSrc(s, e)) {
			s = getMothlastDate(s, 1);
			ret.add(s);
		}
		return ret;
	}

	/**
	 * 获得一段时间内，有多少个周日
	 * 
	 * @param calendarType
	 *            日期跨度的类型，
	 */

	// public static Date[] getWeekArrays(String start,String end){
	// ArrayList<Date> ret = new ArrayList<Date>();
	// Calendar calendar = Calendar.getInstance();
	// calendar.setTime(DateUtil.strToDate(start));
	// Date tmpDate = calendar.getTime();
	// long endTime = DateUtil.strToDate(end).getTime();
	//
	// int count = 0;
	// while(tmpDate.before(DateUtil.strToDate(end)) || tmpDate.getTime() ==
	// endTime){
	//
	// if(calendar.get(Calendar.DAY_OF_WEEK) == 1){
	// ret.add(calendar.getTime());
	// count ++;
	// if(count >= 50) break;
	// }
	//
	// calendar.add(Calendar.DAY_OF_YEAR,1);
	// tmpDate = calendar.getTime();
	// }
	//
	// Date[] dates = new Date[ret.size()];
	// return ret.toArray(dates);
	// }

	/**
	 * 返回某月最后一天 0当月,-1上个月，-2上第二个月，-3上第三个月
	 * 
	 * @param calendarType
	 *            日期跨度的类型，
	 */
	public static String getMonthNextDay(int month) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		Calendar lastDate = Calendar.getInstance();
		lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
		lastDate.add(Calendar.MONTH, month + 1);// 加一个月，变为下月的1号
		lastDate.add(Calendar.DATE, -1);// 减去一天，变为当月最后一天

		return sdf.format(lastDate.getTime());
	}

	/**
	 * 返回某月第一天 0当月,-1上个月，-2上第二个月，-3上第三个月
	 * 
	 * @param calendarType
	 *            日期跨度的类型，
	 */
	public static String getMonthFirstDay(int month) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		Calendar lastDate = Calendar.getInstance();
		lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
		lastDate.add(Calendar.MONTH, month);// 减一个月，变为下月的1号

		return sdf.format(lastDate.getTime());
	}

	/**
	 * @param s
	 *            为日期串 yyyy-MM-dd HH:mm:ss
	 * @param flag
	 *            1 为yyyy-MM 第一天 flag 0为 最后一天
	 * @param i
	 *            =0 当前月 -1 前一个月
	 * @return yyyy-MM-dd
	 */
	public static String getMothfirstDate(String s, int i) {
		String[] ss = s.split("-");
		if (ss.length >= 2) {
			Calendar cal = Calendar.getInstance();
			// 不加下面2行，就是取当前时间前一个月的第一天及最后一天
			cal.set(Calendar.YEAR, Integer.parseInt(ss[0]));
			cal.set(Calendar.MONTH, Integer.parseInt(ss[1]));
			cal.add(Calendar.MONTH, i);
			cal.set(Calendar.DAY_OF_MONTH, 1);
			cal.add(Calendar.DAY_OF_MONTH, -1);
			cal.set(Calendar.DAY_OF_MONTH, 1);
			Date firstDate = cal.getTime();
			return dateToFormat(firstDate);
		} else {
			return null;
		}
	}

	/**
	 * @param s
	 *            为日期串 yyyy-MM-dd HH:mm:ss
	 * @param flag
	 *            1 为yyyy-MM 第一天 flag 为 最后一天
	 * @return yyyy-MM-dd
	 */
	public static String getMothlastDate(String s, int i) {
		String[] ss = s.split("-");
		if (ss.length >= 2) {
			Calendar cal = Calendar.getInstance();
			// 不加下面2行，就是取当前时间前一个月的第一天及最后一天
			cal.set(Calendar.YEAR, Integer.parseInt(ss[0]));
			cal.set(Calendar.MONTH, Integer.parseInt(ss[1]));
			cal.add(Calendar.MONTH, i);
			cal.set(Calendar.DAY_OF_MONTH, 1);
			cal.add(Calendar.DAY_OF_MONTH, -1);
			Date lastDate = cal.getTime();
			return dateToFormat(lastDate);
		} else {
			return null;
		}
	}

	/**
	 * 返回某周第一天 0本周,-1上周，-2上第二个周，-3上第三个周
	 * 
	 * @param
	 */
	public static String getFirstWeekDay(int week) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, week * 7);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);// 想周几，这里就传几Calendar.MONDAY（TUESDAY...）
		return sdf.format(cal.getTime());
	}

	/**
	 * 返回某周最后一天 0本周,-1上周，-2上第二个周，-3上第三个周
	 * 
	 * @param
	 */
	public static String getNextWeekDay(int week) {
		week++;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, week * 7);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);// 想周几，这里就传几Calendar.MONDAY（TUESDAY...）
		return sdf.format(cal.getTime());
	}

	/**
	 * 将毫秒 格式化日期yyyy-MM-dd
	 * 
	 * @param date
	 * @return
	 */
	public static String dateToStr(long vtime) {
		return formatDate(new Date(vtime), "yyyy-MM-dd");
	}

	/**
	 * 将毫秒 格式化日期yyyy-MM-dd
	 * 
	 * @param date
	 * @return
	 */
	public static String dateToStr(long vtime, String format) {
		return formatDate(new Date(vtime), format);
	}

	/**
	 * 将毫秒 格式化日期yyyy-MM-dd
	 * 
	 * @param date
	 * @return
	 */
	public static String dateToStr(Date vtime, String format) {
		return formatDate(vtime, format);
	}

	/**
	 * 返回日期的后n小时时间
	 * 
	 * @return
	 */
	public static String getNHour(String str, int n) {
		Date date = new Date(strToDateFormat(str).getTime() + n * 60 * 60 * 1000);
		SimpleDateFormat sp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sp.format(date);
	}

	/**
	 * 返回日期的后n分钟时间
	 * 
	 * @return
	 */
	public static String getNMin(String str, int n) {
		Date date = new Date(strToDateFormat(str).getTime() + n * 60 * 1000);
		SimpleDateFormat sp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sp.format(date);
	}

	/**
	 * 数据库时间比本地少八小时，28800000毫秒
	 * 
	 * @return
	 */
	public static String findMinute(int n) {
		Date date = new Date(System.currentTimeMillis() - 28800000 - n * 60000);
		SimpleDateFormat sp = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return sp.format(date);
	}

	public static String beforeOneHour() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - 1);
		SimpleDateFormat df = new SimpleDateFormat("HH");
		return df.format(calendar.getTime());
	}

	/**
	 * 返回某日期所在周的第一天
	 * 
	 * @param
	 */

	public static String getWeekFirstDay(String daySrc) {
		Date time = strToDate(daySrc);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // 设置时间格式
		Calendar cal = Calendar.getInstance();
		cal.setTime(time);
		// 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
		int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
		if (1 == dayWeek) {
			cal.add(Calendar.DAY_OF_MONTH, -1);
		}
		// System.out.println("要计算日期为:" + sdf.format(cal.getTime())); // 输出要计算日期
		cal.setFirstDayOfWeek(Calendar.MONDAY);// 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
		int day = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
		cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);// 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
		// String imptimeBegin = sdf.format(cal.getTime());
		// System.out.println("所在周星期一的日期：" + imptimeBegin);
		// cal.add(Calendar.DATE, 2);
		// String imptimeMi = sdf.format(cal.getTime());
		// System.out.println("所在周星期三的日期：" + imptimeMi);
		// cal.add(Calendar.DATE, 2);
		// String imptimeEnd = sdf.format(cal.getTime());
		// System.out.println("所在周星期五的日期：" + imptimeEnd);

		return sdf.format(cal.getTime());
	}

	/**
	 * 返回某周第一天
	 * 
	 * @param
	 */

	public static String getLastWeekEndDay(String daySrc) {
		Date time = strToDate(daySrc);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // 设置时间格式
		Calendar cal = Calendar.getInstance();
		cal.setTime(time);
		// 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
		int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
		if (1 == dayWeek) {
			cal.add(Calendar.DAY_OF_MONTH, -1);
			return daySrc;
		}
		// System.out.println("要计算日期为:" + sdf.format(cal.getTime())); // 输出要计算日期
		cal.setFirstDayOfWeek(Calendar.SUNDAY);// 设置一个星期的第一天
		int day = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
		cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);// 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
		// String imptimeBegin = sdf.format(cal.getTime());
		// System.out.println("所在周星期一的日期：" + imptimeBegin);
		// cal.add(Calendar.DATE, 2);
		// String imptimeMi = sdf.format(cal.getTime());
		// System.out.println("所在周星期三的日期：" + imptimeMi);
		// cal.add(Calendar.DATE, 2);
		// String imptimeEnd = sdf.format(cal.getTime());
		// System.out.println("所在周星期五的日期：" + imptimeEnd);

		return sdf.format(cal.getTime());
	}

	/**
	 * 获取上个月开始时间，结束时间
	 * 
	 * @param args
	 */

	// public static StimeEtime getLastMonthStimeEtime(){
	// StimeEtime setime = new StimeEtime();
	// try {
	// new StimeEtime();
	// SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );
	// Calendar calendar = Calendar.getInstance();
	// calendar.add(Calendar.MONTH, -1);
	// calendar.set(Calendar.DAY_OF_MONTH, 1);
	// String mounthDateF = sdf.format(calendar.getTime())+" 00:00:00";
	// sdf = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
	// Date date = sdf.parse( mounthDateF );
	// setime.setStime( sdf.format(date));
	// sdf = new SimpleDateFormat( "yyyy-MM-dd" );
	// calendar = Calendar.getInstance();
	// calendar.set(Calendar.DAY_OF_MONTH, 0);
	// mounthDateF = sdf.format(calendar.getTime())+" 23:59:59";
	// sdf = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
	// setime.setEtime(sdf.format(sdf.parse( mounthDateF )));
	// } catch (ParseException e) {
	// e.printStackTrace();
	// setime =null;
	// }
	// return setime;
	//
	// }

	/**
	 * 获取上一年的开始时间结束时间
	 * 
	 * @param args
	 */
	// public static StimeEtime getLastYear(){
	// StimeEtime stimeEtime = new StimeEtime();
	// Calendar calendar = Calendar.getInstance();
	// SimpleDateFormat sp = new SimpleDateFormat("yyyy");
	// Integer nowYear = Integer.valueOf(sp.format(new Date()));
	// sp = new SimpleDateFormat("yyyy");
	// nowYear = Integer.valueOf(sp.format(new Date()));
	// calendar.clear();
	// calendar.set(Calendar.YEAR, nowYear - 1);
	// calendar.roll(Calendar.DAY_OF_YEAR, -1);
	// Date currYearLast = calendar.getTime();
	// stimeEtime =new StimeEtime();
	// stimeEtime.setStime(nowYear - 1 + "-01-01 00:00:00");
	// sp = new SimpleDateFormat("yyyy-MM-dd");
	// stimeEtime.setEtime(sp.format(currYearLast)+" 23:59:59");
	// return stimeEtime;
	// }

	/**
	 * 获得日期字符串数组
	 * 
	 * @param calendarType
	 *            日期跨度的类型，
	 */

	public static List<String> dateToArrays(String start, String end) {
		ArrayList<String> ret = new ArrayList<String>();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(DateTimeTool.strToDate(start));
		Date tmpDate = calendar.getTime();
		long endTime = DateTimeTool.strToDate(end).getTime();

		while (tmpDate.before(DateTimeTool.strToDate(end)) || tmpDate.getTime() == endTime) {
			ret.add(formatDate(calendar.getTime(), "yyyy-MM-dd"));
			calendar.add(Calendar.DAY_OF_YEAR, 1);
			tmpDate = calendar.getTime();
		}

		return ret.size() >= 60 ? ret.subList(ret.size() - 61, ret.size()) : ret;
	}

	/**
	 * 获得指定日期的前一天
	 * 
	 * @param specifiedDay
	 *            指定日期
	 * @author liuyongli
	 */
	public static String getSpecifiedDayBefore(String specifiedDay) {
		// SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		Date date = null;
		try {
			date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		c.setTime(date);
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day - 1);

		String dayBefore = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
		return dayBefore;
	}

	/**
	 * 获得指定日期的后一天
	 * 
	 * @param specifiedDay
	 *            指定日期
	 * @author liuyongli
	 */
	public static String getSpecifiedDayAfter(String specifiedDay) {
		Calendar c = Calendar.getInstance();
		Date date = null;
		try {
			date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		c.setTime(date);
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day + 1);

		String dayAfter = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
		return dayAfter;
	}

	/**
	 * 获得timerange(时刻的第几个半秒,从0开始)
	 * 
	 * @param time(HH:mm:ss)
	 * @author liuyongli
	 */
	public static int getTimeRange(String time) {
		int hours = Integer.valueOf(time.substring(0, 2));
		int minutes = Integer.valueOf(time.substring(3, 5));
		int seconds = Integer.valueOf(time.substring(6, 8));
		int left;
		if (seconds < 30) {// 不足半秒
			left = 1;
		} else {// 超过半秒
			left = 2;
		}
		return hours * 120 + minutes * 2 + left - 1;
	}

	/**
	 * 获得一个日期的时分秒部分
	 * 
	 * @param date
	 * @author liuyongli
	 */
	public static String getTime(String date) {
		Date strToDateFormat = strToDateFormat(date);
		// SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		DateFormat df3 = DateFormat.getTimeInstance();// 只显示出时分秒
		String str = df3.format(strToDateFormat);
		if (str.substring(0, str.indexOf(":")).length() == 2) {
			return df3.format(strToDateFormat);
		} else {
			return "0" + df3.format(strToDateFormat);
		}
	}

	/**
	 * 将0-2879(半分钟数)的一个数字转换成时间(HH:mm:ss)
	 * 
	 * @param halfMinute
	 * @author liuyongli
	 */
	public static String getTime(int halfMinute) {
		int sconds = (halfMinute) * 30;
		int hours = sconds / 3600;
		int minutes = sconds % 3600 / 60;
		int seconds = sconds % 3600 % 60;
		String hour;
		String minute;
		String second;
		if (String.valueOf(hours).length() == 1) {
			hour = "0" + String.valueOf(hours);
		} else {
			hour = String.valueOf(hours);
		}
		if (String.valueOf(minutes).length() == 1) {
			minute = "0" + String.valueOf(minutes);
		} else {
			minute = String.valueOf(minutes);
		}
		if (seconds < 30) {
			second = "00";
		} else {
			second = "30";
		}
		String result = hour + ":" + minute + ":" + second;
		return result;
	}

	/**
	 * 根据两个时间计算中间有几个月 com.polyinfo.library.utils:DateTimeTool.java
	 * 
	 * @author sunhj 2018年2月5日 下午2:05:53 integer
	 * @throws ParseException
	 */
	public static Integer getMonthBetweenDate(String startTime, String endTime) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		// 创建日历对象
		Calendar bef = Calendar.getInstance();
		Calendar aft = Calendar.getInstance();
		bef.setTime(sdf.parse(startTime));
		aft.setTime(sdf.parse(endTime));
		int result = aft.get(Calendar.MONTH) - bef.get(Calendar.MONTH);
		int month = (aft.get(Calendar.YEAR) - bef.get(Calendar.YEAR)) * 12;
		return Math.abs(month + result);
	}

	// 获取当前时间N天以后的日期
	public static LocalDate getNowTimePlusNDay(Integer N) {
		LocalDate now = LocalDate.now();
		return now.plus(N, ChronoUnit.DAYS);
	}

	/**
	 * 获取当前日期的后N天的日期 2019年1月3日 下午4:13:49 Administrator:sunhj
	 * 
	 * @param specifiedDay
	 * @param n
	 * @return
	 */
	public static Date getSpecifiedDayAfterN(String specifiedDay, int n) {
		Date date = null;
		Calendar c = Calendar.getInstance();
		try {
			date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		c.setTime(date);
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day + n);
		return c.getTime();
	}

	public static long getTomorrowZeroSeconds() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, 1);
		// 改成这样就好了
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return (cal.getTimeInMillis() - System.currentTimeMillis()) / 1000;
	}

	public static Date getOneYearLater() {
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.YEAR, 1);
		return cal.getTime();
	}
}