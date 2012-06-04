package com.hsuyucheng.telematics.util.test;

import com.hsuyucheng.telematics.util.TimeTool;

import junit.framework.TestCase;

/**
 * @version 0.2
 * @author YuCHeng Hsu
 *
 */
public class TimeToolTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testGetTimeToString() {
		String format1 = "yyyy/MM/dd hh:mm:ss";
		String format2 = "yyyyMMdd_HHmmss";
		String get1 = TimeTool.getTimeToString(format1);
		String get2 = TimeTool.getTimeToString(format2);
		assertNotNull(get1);
		int SLASH = 4;
		int UNDER_LINE = 8;
		assertEquals(SLASH, get1.indexOf("/"));
		assertNotNull(get2);
		assertEquals(UNDER_LINE, get2.indexOf("_"));
	}

	public void testFixTimeValue() {
		long carryValue = 2;
		long time = 120;
		long time2 = 130;
		long unit = 60;
		long unit2 = 0;
		assertEquals(0, TimeTool.fixTimeValue(carryValue, time, unit));
		assertEquals(time, TimeTool.fixTimeValue(0, time, unit));
		assertEquals(time, TimeTool.fixTimeValue(carryValue, time, unit2));
		long answer = 10;
		assertEquals(answer, TimeTool.fixTimeValue(carryValue, time2, unit));

	}
	
	public void testMillisecondsToString() {
		String kk = "00:14:49.000";
		int time = 889000;
		assertEquals(kk, TimeTool.millisecondsToString(time));
		kk = "00:00:00.000";
		assertEquals(kk, TimeTool.millisecondsToString(0));		
	}
}
