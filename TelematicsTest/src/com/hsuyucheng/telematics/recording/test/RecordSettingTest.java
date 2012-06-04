package com.hsuyucheng.telematics.recording.test;

import com.hsuyucheng.telematics.recording.RecordSetting;

import junit.framework.TestCase;

/**
 * @version 0.1
 * @author YuCHeng Hsu
 *
 */
public class RecordSettingTest extends TestCase {
	RecordSetting setting;
	int[] attributes;

	protected void setUp() throws Exception {
		attributes = new int[RecordSetting.NUMBER_ITEM];
		attributes[RecordSetting.VIDEO_QUALITY] = RecordSetting.VIDEO_QUALITY_HIGH;
		attributes[RecordSetting.SUBTITLE] = RecordSetting.SUBTITLE_DISABLE;
		super.setUp();
	}

	protected void tearDown() throws Exception {
		attributes = null;
		super.tearDown();
	}

	public void testRecordSettting() {
		attributes[RecordSetting.TIME_INTERVAL] = RecordSetting.TIME_YOUTUBE_MINUTES;
		setting = new RecordSetting(attributes);
		assertNotNull(setting);
		attributes[RecordSetting.TIME_INTERVAL] = RecordSetting.TIME_NO_LIMIT;
		setting = new RecordSetting(attributes);
		assertNotNull(setting);		
		// if puts illegal value om attributes[TIME_INTERVAL], it
		// will throw IllegalArgumentException
	}

}
