package com.hsuyucheng.telematics.recording;

import android.media.CamcorderProfile;

/**
 * This class records "record setting". You can use default Constructor to
 * create default setting, or use RecordSetting(int[] values) to create your
 * setting.
 * 
 * Default setting: 
 * Video Quality: high 
 * Time Interval: 15 minutes 
 * Subtiele: disable
 * 
 * Example: int[] values = new int[RecordSetting.NUMBER_ITEM];
 * values[RecordSetting.VIDEO_QUALITY] = RecordSetting.VIDEO_QUALITY_HIGH;
 * values[RecordSetting.TIME_INTERVAL] = RecordSetting.TIME_YOUTUBE_MINUTES;
 * values[RecordSetting.SUBTITLE] = RecordSetting.SUBTITLE_DISABLE;
 * RecordSetting setting = new RecordSetting[values];
 * 
 * @version 0.3
 * @author YuCHeng Hsu
 * 
 */
public class RecordSetting {
	public static final int VIDEO_QUALITY = 0;
	public static final int TIME_INTERVAL = 1;
	public static final int SUBTITLE = 2;
	
	public static final int VIDEO_QUALITY_HIGH = CamcorderProfile.QUALITY_HIGH;
	public static final int VIDEO_QUALITY_LOW = CamcorderProfile.QUALITY_LOW;
	
	// Time unit: minutes
	public static final int TIME_DEMO = 1;
	public static final int TIME_YOUTUBE_MINUTES = 15;
	public static final int TIME_30_MINUTES = 30;
	public static final int TIME_NO_LIMIT = 0;	
	
	
	public static final int SUBTITLE_ENABLE = 1;
	public static final int SUBTITLE_DISABLE = 0;
	
	// number of item of Setting
	public static final int NUMBER_ITEM = 3;
	
	private int mVideoQuality = VIDEO_QUALITY_HIGH;
	// Time Interval unit: minutes
	private int mTimeInterval = TIME_YOUTUBE_MINUTES; 
	private boolean mIsSubEnable = false;
	
	public RecordSetting(int[] values) {
		mVideoQuality = values[VIDEO_QUALITY];
		mTimeInterval = values[TIME_INTERVAL];
		
		// check time interval
		switch(mTimeInterval) {
		case TIME_DEMO:
		case TIME_YOUTUBE_MINUTES:
		case TIME_30_MINUTES:
		case TIME_NO_LIMIT:
			break;
		default:
			throw new IllegalArgumentException();
		}
		
		if (values[SUBTITLE] == SUBTITLE_ENABLE) {
			mIsSubEnable = true; 	
		} else {
			mIsSubEnable = false;
		}
	}

	public RecordSetting() {
		super();
	}

	public int getVideoQuality() {
		return mVideoQuality;
	}

	public int getTimeInterval() {
		return mTimeInterval;
	}

	public boolean isSubEnable() {
		return mIsSubEnable;
	}


}
