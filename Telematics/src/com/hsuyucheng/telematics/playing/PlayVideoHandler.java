package com.hsuyucheng.telematics.playing;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;

public class PlayVideoHandler {
	private Handler mHandler;
	private String path;
	private MediaPlayer mMediaPlayer;
	private Context mContext;
	
	public PlayVideoHandler(Context context) {
		mContext = context;
	}
	
	
}
