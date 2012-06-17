package com.hsuyucheng.telematics.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Timer;
import java.util.TimerTask;

import com.hsuyucheng.telematics.recording.TelLocation;

import android.util.Log;

/**
 * @version 0.3
 * @author YuCHeng Hsu
 *
 */
public class SubCreator {
	private long mStartMilliseconds = 0;
	private String mLastSubtitle = "";
	private TelLocation mLocation = null;
	private SubDescriber mSubDescriber = null;
	private Timer mSubTimer = null;
	
	public SubCreator(TelLocation location) {
		mLocation = location;
		mSubDescriber = new SubDescriber();
	}
	
	public String getSubtitle() {
		return mLastSubtitle;
	}
	
	/**
	 * @param name
	 * 			file name
	 */
	public void startRecording(String name) {
		mStartMilliseconds = System.currentTimeMillis();
		mSubDescriber.setName(name);
		createSub();
		initTimer();
	}
	
	public void stopRecording() {
		stopTimer();
		mLastSubtitle = "";
	}
	
	private void initTimer() {
		if (mSubTimer == null) {
			mSubTimer = new Timer();
			mSubTimer.schedule(new TimerTask() {

				@Override
				public void run() {
					createSub();
				}

			}, SubDescriber.INTERVAL_MILLISECONDS,
					SubDescriber.INTERVAL_MILLISECONDS);
		}
	}
	
	private void stopTimer() {
		if (mSubTimer != null) {
			mSubTimer.cancel();
			mSubTimer = null;
		}
	}
	
	/**
	 * create sub like this:
	 * 				hh:mm:ss.SSS,hh:mm:ss.SSS
	 * 				Time: 2011/10/05 10:23:40
	 * 				Latitude: 21.049689
	 * 				Longitude: 122.5050568
	 */
	private void createSub() {
		long milliseconds = System.currentTimeMillis();
		long startTime = milliseconds - mStartMilliseconds;
		String timeStart = TimeTool.millisecondsToString(startTime);
		long endTime = startTime + SubDescriber.INTERVAL_MILLISECONDS;
		String timeEnd = "," + TimeTool.millisecondsToString(endTime);

		String subTime = mSubDescriber.getSubTime();
		String subLocation = mSubDescriber.getSubLocation(
				mLocation.getLatitude(), mLocation.getLongitude());
		String newLine = System.getProperty("line.separator");
		
		mLastSubtitle = timeStart + timeEnd + newLine + subTime + newLine
				+ subLocation + newLine + newLine;
		
		// not assign return value
		writeSubtitle(mLastSubtitle, Storage.getVideoLocation()
				+ mSubDescriber.getName() + SubDescriber.getSubType());
	}
	
	/**
	 * write contexts to file
	 * @param contents
	 *          
	 * @param path
	 * @return 1: succeed 0: failure for test
	 */
	public int writeSubtitle(String contents, String path) {
		int result = 0;
		File file = new File(path);
		// append to an existing file or create file
		PrintWriter out;
		try {
			out = new PrintWriter(new FileWriter(file, true));
			out.print(contents);
			out.close();
		} catch (IOException e) {
			Log.d("SubCreator", "Error write string to file:" + e.getMessage());
			return result;
		}
		result = 1;
		return result;
	}

}
