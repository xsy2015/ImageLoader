package com.water.jxwz.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;

import android.content.Context;
import android.text.TextUtils;

import com.water.jxwz.R;


/**
 * 时间日期处理类
 * 
 * @author qiaocbao
 * @version 2015-3-31 下午6:42:14
 */
public class DateUtil {

	private static final String FORMAT = "yyyy-MM-dd HH:mm:ss";
	private static Random random = new Random();

	/**
	 * @Description 指定年月的最后一天日期
	 * @param year
	 * @param month
	 * @param simpleDateFormat
	 * @return String
	 */
	public static String getMonthLastDayDate(int year, int month,
			SimpleDateFormat simpleDateFormat) {
		if (year < 0)
			return null;
		Calendar cal = Calendar.getInstance();
		if (month < 0) {
			cal.set(Calendar.YEAR, year - 1);
			cal.set(Calendar.MONTH, 12 + month % 11);
		} else if (month > 11) {
			cal.set(Calendar.YEAR, year + 1);
			cal.set(Calendar.MONTH, month % 11);
		} else {
			cal.set(Calendar.YEAR, year);
			cal.set(Calendar.MONTH, month);
		}
		cal.set(Calendar.DAY_OF_MONTH,
				cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		String date = simpleDateFormat.format(cal.getTime());
		return date;
	}

	/**
	 * @Description 指定年月的第一天日期
	 * @param year
	 * @param month
	 * @param simpleDateFormat
	 * @return String
	 */
	public static String getMonthFirstDayDate(int year, int month,
			SimpleDateFormat simpleDateFormat) {
		if (year < 0)
			return null;
		Calendar cal = Calendar.getInstance();

		if (month < 0) {
			cal.set(Calendar.YEAR, year - 1);
			cal.set(Calendar.MONTH, 12 + month);
		} else if (month > 11) {
			cal.set(Calendar.YEAR, year + 1);
			cal.set(Calendar.MONTH, month % 11);
		} else {
			cal.set(Calendar.YEAR, year);
			cal.set(Calendar.MONTH, month);
		}
		cal.set(Calendar.DAY_OF_MONTH, 1);
		String date = simpleDateFormat.format(cal.getTime());
		return date;
	}

	// 指定日期当周第一天或最后一天
	public static String getFirstLastWeek(Calendar cal, boolean flag) {
		// flag true周第一天 false 周最后一天

		int dw = cal.get(Calendar.DAY_OF_WEEK);
		if (!flag)
			cal.setTimeInMillis(cal.getTimeInMillis() + (7 - dw) * 24 * 60 * 60
					* 1000);
		else
			cal.setTimeInMillis(cal.getTimeInMillis() - (dw - 1) * 24 * 60 * 60
					* 1000);

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

		String showTime = formatter.format(cal.getTime());
		return showTime.toString();
	}

	// 指定日期当月第一天或最后一天
	public static String getFirstLastMoonth(Calendar cal, boolean flag) {
		// flag true月第一天 false 月最后一天
		cal.set(Calendar.DAY_OF_MONTH, 1);
		if (!flag) {
			cal.roll(Calendar.DAY_OF_MONTH, -1);
		}
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String showTime = formatter.format(cal.getTime());
		return showTime.toString();
	}

	// 指定日期string格式
	public static String getDateString(Calendar cal) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String showTime = formatter.format(cal.getTime());
		return showTime.toString();
	}

	/**
	 * 获取当前星期几
	 * 
	 * @return
	 */
	public static String getWeekTime(Context context) {
		String weekly = "";
		Calendar c = Calendar.getInstance();
		c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
		int mWay = c.get(Calendar.DAY_OF_WEEK);
		switch (mWay) {
		case 1:
			weekly = context.getString(R.string.sunday);
			break;
		case 2:
			weekly = context.getString(R.string.monday);
			break;
		case 3:
			weekly = context.getString(R.string.tuesday);
			break;
		case 4:
			weekly = context.getString(R.string.wednesday);
			break;
		case 5:
			weekly = context.getString(R.string.thursday);
			break;
		case 6:
			weekly = context.getString(R.string.friday);
			break;
		case 7:
			weekly = context.getString(R.string.saturday);
			break;
		default:
			break;
		}
		return weekly;
	}

	/**
	 * 在日期的日和月前加0
	 * 
	 * @return
	 */
	public static String getSimpleDate(String date) {
		if (!TextUtils.isEmpty(date) && !date.equals("")) {
			String[] dates = date.split("-");
			int month = Integer.valueOf(dates[1]);
			int day = Integer.valueOf(dates[2]);
			return dates[0] + "-" + getZeroDate(month) + "-" + getZeroDate(day);

		}
		return null;
	}

	/**
	 * 显示双倍数，单位数前加0
	 * 
	 * @param temp
	 * @return
	 */
	public static String getZeroDate(int temp) {
		if (temp < 10) {
			return "0" + temp;
		}
		return temp+"";
	}

	public static Date str2Date(String str) {
		return str2Date(str, null);
	}

	public static Date str2Date(String str, String format) {
		if (str == null || str.length() == 0) {
			return null;
		}
		if (format == null || format.length() == 0) {
			format = FORMAT;
		}
		Date date = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			date = sdf.parse(str);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;

	}

	public static Calendar str2Calendar(String str) {
		return str2Calendar(str, null);

	}

	public static Calendar str2Calendar(String str, String format) {

		Date date = str2Date(str, format);
		if (date == null) {
			return null;
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);

		return c;

	}

	public static String date2Str(Calendar c) {// yyyy-MM-dd HH:mm:ss
		return date2Str(c, null);
	}

	public static String date2Str(Calendar c, String format) {
		if (c == null) {
			return null;
		}
		return date2Str(c.getTime(), format);
	}

	public static String date2Str(Date d) {// yyyy-MM-dd HH:mm:ss
		return date2Str(d, null);
	}

	public static String date2Str(Date d, String format) {// yyyy-MM-dd HH:mm:ss
		if (d == null) {
			return null;
		}
		if (format == null || format.length() == 0) {
			format = FORMAT;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String s = sdf.format(d);
		return s;
	}

	public static String getCurDateStr() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		return c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-"
				+ c.get(Calendar.DAY_OF_MONTH) + "-"
				+ c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE)
				+ ":" + c.get(Calendar.SECOND);
	}

	/**
	 * 获得当前日期的字符串格式
	 * 
	 * @param format
	 * @return
	 */
	public static String getCurDateStr(String format) {
		Calendar c = Calendar.getInstance();
		return date2Str(c, format);
	}

	// 格式到秒
	public static String getSecond(long time) {

		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(time);

	}

	// 格式到天
	public static String getDay(long time) {

		return new SimpleDateFormat("yyyy-MM-dd").format(time);

	}
	// 格式到天
	public static String getFormatDay(long time) {

		return new SimpleDateFormat("yyyy年-MM月-dd日").format(time);

	}

	// 格式到毫秒
	public static String getSMillon(long time) {
		return new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(time);
	}

	/**
	 * 根据时间生成msgId
	 * 
	 * @return
	 */
	public static String getMsgId() {
		return "" + System.nanoTime() + "" + random.nextInt(25);
	}

	/**
	 * 计算两个时间差
	 * 
	 * @param curTime
	 * @param beforeTime
	 * @param format
	 * @return
	 */
	public static long dateComm(String curTime, String beforeTime, String format) {
		Date curDate = str2Date(curTime, format);
		Date beforeDate = str2Date(beforeTime, format);
		long diff = curDate.getTime() - beforeDate.getTime();// 这样得到的差值是微秒级别

		long days = diff / (1000 * 60 * 60 * 24);
		long hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
		long minutes = (diff - days * (1000 * 60 * 60 * 24) - hours
				* (1000 * 60 * 60))
				/ (1000 * 60);
		return minutes;
	}

	/**
	 * 当前时间推迟几个小时的时间
	 * 
	 * @param hour
	 * @param simpleDateFormat
	 * @return date
	 */
	public static String delayHourTime(int hour,
			SimpleDateFormat simpleDateFormat) {
		Calendar cal = Calendar.getInstance();
		int mCurrentHour;
		mCurrentHour = cal.get(Calendar.HOUR_OF_DAY);
		cal.set(Calendar.HOUR_OF_DAY, mCurrentHour + hour);
		String date = simpleDateFormat.format(cal.getTime());
		return date;
	}

	/**
	 * 获取指定日期中的时间
	 * 
	 * @param dateTime
	 * @return
	 */
	public static String getHourAndMinute(String dateTime, String format) {
		String hourMinute = "";
		SimpleDateFormat houeMinFormat = new SimpleDateFormat("HH:mm",
				Locale.US);
		try {
			Date date = new SimpleDateFormat(FORMAT, Locale.US).parse(dateTime);
			hourMinute = houeMinFormat.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (!SharedMothed.isEmpty(hourMinute)) {
			return hourMinute;
		}
		return dateTime;
	}

	/**
	 * 获取日期
	 * 
	 * @param cal
	 *            当前时间
	 * @param format
	 *            时间显示格式
	 * @return
	 */
	public static String getDate(String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format,
				Locale.getDefault());
		String showTime = formatter.format(Calendar.getInstance(
				Locale.getDefault()).getTime());
		return showTime.toString();
	}

	/**
	 * 根据星期int类型数组获取星期String类型
	 * 
	 * @param context
	 * @param weekInt
	 * @return
	 */
	public static String getIntToStringWeek(Context context, String weekInt) {
		StringBuffer weekString = new StringBuffer();
		if (!SharedMothed.isEmpty(weekInt)) {
			if (weekInt.indexOf(",") != -1) {
				String[] weeks = weekInt.split(",");
				if (weeks.length > 0) {
					for (int i = 0; i < weeks.length; i++) {
						String week = weeks[i];
						int w = Integer.parseInt(week);
						switch (w) {
						case 1:
							weekString.append(context
									.getString(R.string.sunday));
							break;
						case 2:
							weekString.append(context
									.getString(R.string.monday));
							break;
						case 3:
							weekString.append(context
									.getString(R.string.tuesday));
							break;
						case 4:
							weekString.append(context
									.getString(R.string.wednesday));
							break;
						case 5:
							weekString.append(context
									.getString(R.string.thursday));
							break;
						case 6:
							weekString.append(context
									.getString(R.string.friday));
							break;
						case 7:
							weekString.append(context
									.getString(R.string.saturday));
							break;
						default:
							break;
						}
						weekString.append("、");
					}
					weekString.deleteCharAt(weekString.length() - 1);
				}
			}
		}
		return weekString.toString();
	}

	/**
	 * 把指定格式时间转换为秒
	 * 
	 * @param curTime
	 * @param beforeTime
	 * @param format
	 * @return
	 */
	public static long getDateTimeToSecond(String curTime, String format) {
		if (SharedMothed.isEmpty(format)) {
			format = "HH:mm:ss";
		}
		long second = 0;
		Date curDate = str2Date(curTime, format);
		if (curDate != null) {
			second = (curDate.getHours() * (60 * 60))
					+ (curDate.getMinutes() * 60) + curDate.getSeconds();
		}
		return second;
	}

	/**
	 * 获取当前时间秒
	 * 
	 * @return
	 */
	public static long getCurrentSecond() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.HOUR_OF_DAY) * 3600 + cal.get(Calendar.MINUTE)
				* 60 + cal.get(Calendar.SECOND);
	}
	
	public static String millisToStringDate(long millis, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern,
                Locale.getDefault());
        return format.format(new Date(millis));
    }
	
	public static String getWeek(String time) {
		
		
		  String Week = "星期";


		  SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		  Calendar c = Calendar.getInstance();
		  try {

		   c.setTime(format.parse(time));

		  } catch (ParseException e) {
		  
		   e.printStackTrace();
		  }
		  if (c.get(Calendar.DAY_OF_WEEK) == 1) {
		   Week += "天";
		  }
		  if (c.get(Calendar.DAY_OF_WEEK) == 2) {
		   Week += "一";
		  }
		  if (c.get(Calendar.DAY_OF_WEEK) == 3) {
		   Week += "二";
		  }
		  if (c.get(Calendar.DAY_OF_WEEK) == 4) {
		   Week += "三";
		  }
		  if (c.get(Calendar.DAY_OF_WEEK) == 5) {
		   Week += "四";
		  }
		  if (c.get(Calendar.DAY_OF_WEEK) == 6) {
		   Week += "五";
		  }
		  if (c.get(Calendar.DAY_OF_WEEK) == 7) {
		   Week += "六";
		  }	 

		  return Week;
		
	}
}
