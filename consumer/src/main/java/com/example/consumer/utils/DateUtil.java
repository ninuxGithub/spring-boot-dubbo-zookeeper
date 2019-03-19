package com.example.consumer.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateUtil {

	private static final Logger logger = LoggerFactory.getLogger(DateUtil.class);

	public static final ThreadLocal<SimpleDateFormat> localDateFormat = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		};
	};
	public static final ThreadLocal<SimpleDateFormat> ymdhm = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm");
		};
	};
	public static final ThreadLocal<SimpleDateFormat> ymdDateFormat = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd");
		};
	};
	
	
	//3月15日 23:04
	public static final ThreadLocal<SimpleDateFormat> mdhmDateFormat = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("MM月dd日 HH:mm");
		};
	};

	public static final int DATE = Calendar.DATE;
	public static final int HOUR = Calendar.HOUR;
	public static final int MINUTE = Calendar.MINUTE;
	public static final int SECOND = Calendar.SECOND;
	
	/**
	 * 
	 * @param date 日期对象
	 * @return 日期格式：3月15日 23:04
	 */
	public static String mdhmDateFormat(Date date) {
		return mdhmDateFormat.get().format(date);
	}
	public static String localDateFormatStr(Date date) {
		return localDateFormat.get().format(date);
	}
	
	
	/**
	 * 返回yyyy-MM-dd 格式的日期字符串
	 * @param date
	 * @return
	 */
	public static String ymdDateStr(Date date) {
		return ymdDateFormat.get().format(date);
	}
	
	public static String ymdhmDateStr(Date date) {
		return ymdhm.get().format(date);
	}
	
	
	public static Date ymdStr2Date(String str)  {
		Date date = null;
		try {
			date = ymdDateFormat.get().parse(str);
		} catch (ParseException e) {
			logger.info("日期转换异常{}",e.getMessage());
		}
		return date;
		
	}
	
	/**
	 * convert Date to cron ,eg. "0 07 10 15 1 ? 2016"
	 * 
	 * @param date
	 * @return
	 */
	public static String getCronExpression(Date date) {
		String dateFormat = "ss mm HH dd MM ? yyyy";
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		String formatTimeStr = null;
		if (date != null) {
			formatTimeStr = sdf.format(date);
		}
		return formatTimeStr;
	}

	
	
	
	/**
	 * @param date
	 *            日期
	 * @return 该日期对应的秒：从1970开始到现在的秒
	 */
	public static int getSecond(Date date) {
		if (null == date) {
			return 0;
		}
		String timestamp = String.valueOf(date.getTime() / 1000);
		return Integer.valueOf(timestamp);
	}

	/**
	 * @param date
	 *            目标日期
	 * @param type
	 *            增加的日期类型：日， 时，分，秒
	 * @param num
	 *            增加的数量
	 * @return
	 */
	public static Date addDateByType(Date date, int type, int num) {
		SimpleDateFormat format = localDateFormat.get();
		if (date == null) {
			throw new RuntimeException("addDateHour 的参数date 不能为null");
		}
		logger.debug("原始时期:" + format.format(date)); // 显示输入的日期
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(type, num);// 24小时制
		date = cal.getTime();
		logger.debug("修改后的日期:" + format.format(date)); // 显示更新后的日期
		return cal.getTime();

	}

	/**
	 * 获取入参日期的时间戳： 默认是日期的23:59:59秒
	 * @param date 入参日期
	 * @return
	 */
	public static long getTimeStamp(Date date, String hms) {
		SimpleDateFormat format = localDateFormat.get();
		if (date == null) {
			throw new RuntimeException("addDateHour 的参数date 不能为null");
		}
		String dateStr = ymdDateFormat.get().format(date)+ (hms == null?" 23:59:59":hms);
		logger.debug("原始时期:{} , 获取的时间戳的截止时间:{}" , format.format(date), dateStr); // 显示输入的日期
		Calendar cal = Calendar.getInstance();
		try {
			cal.setTime(format.parse(dateStr));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return Long.valueOf(String.valueOf(cal.getTime().getTime() / 1000));
	}

	public static int getTimeStamp() {
		return getSecond(new Date());
	}

}
