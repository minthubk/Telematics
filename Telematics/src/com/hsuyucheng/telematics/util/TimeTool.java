package com.hsuyucheng.telematics.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TimeTool {
	/**
	 * @param format
	 *            Example: format = "yyyy/MM/dd hh:mm:ss" yyyy: year, MM: month,
	 *            dd: day ...
	 * @return timeString
	 */
	public static String getTimeToString(String format) {
		SimpleDateFormat sdFormat = new SimpleDateFormat(format);
		Date date = new Date();
		String strDate = sdFormat.format(date);
		return strDate;
	};

	/**
	 * if carryValue of time > 0 then modify time
	 * 
	 * @param carryValue
	 * @param time
	 * @param unit
	 *            time unit
	 * @return modified time
	 */
	public static long fixTimeValue(long carryValue, long time, long unit) {
		if (carryValue > 0 && time > unit && unit > 0) {
			time = time - carryValue * unit;
		}
		return time;
	}

	/**
	 * convert millisecond to string 
	 * example: milliseconds = 889000 then return "00:14:49.000"
	 * 
	 * @param timeSeconds
	 *            seconds of time, max is Interger.MAX_VALUE minimum value is 0
	 * @return string
	 */
	public static String millisecondsToString(long milliseconds) {
		final int HOUR_SECONDS = 3600;
		final int MINUTE_SECONDS = 60;
		long seconds = TimeUnit.SECONDS.convert(milliseconds,
				TimeUnit.MILLISECONDS);
		long hours = TimeUnit.HOURS.convert(seconds, TimeUnit.SECONDS);
		seconds = fixTimeValue(hours, seconds, HOUR_SECONDS);
		long minutes = TimeUnit.MINUTES.convert(seconds, TimeUnit.SECONDS);
		seconds = fixTimeValue(minutes, seconds, MINUTE_SECONDS);

		// check negative time
		if (milliseconds <= 0) {
			hours = minutes = seconds = 0;
		}

		String strTime = String.format("%02d", hours) + ":"
				+ String.format("%02d", minutes) + ":"
				+ String.format("%02d", seconds) + "."
				+ String.format("%03d", 0);
		return strTime;

	}
}
