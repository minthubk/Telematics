package com.hsuyucheng.telematics.recording;

import java.util.Timer;
import java.util.TimerTask;

import com.google.android.maps.GeoPoint;
import com.hsuyucheng.telematics.util.DeviceInfo;
import com.hsuyucheng.telematics.util.Storage;
import com.hsuyucheng.telematics.util.SubCreator;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.SurfaceHolder;

/** Handle recording event.
 * You should pass Application context to RecordVideoHanlder().
 * If you want Activity get message to do something, then you 
 * pass context and Handler instance of Activity.
 * Record video steps:
 * 1. call openResource() to get resource
 *    recommend you put openResource() in onResume()
 * 2. put setPreViewo() into SurfaceCreated() or SurfaceChanged()
 * 3. createSetting() if you want change setting
 * 		arguments attributes[RecordSetting.VIDEO_QUALITY]: video quality
 *                attributes[RecordSetting.VIDEO_TIME_INTERVAL]: video interval
 *                attributes[RecordSetting.SUBTITLE]: record subtitle 
 * 4. call statRecording() to record video
 * 5. call stopRecording() to stop recording.
 * 6. call releaseResource() to release resource
 *    recommend you put releaseResource() in onPause();
 * 
 * Message list
 *  ID_UI_CHANGE: notify UI change
 *  ID_NO_FINE_LOCATION: Wifi or GSP disable
 *  ID_RECORD_ERROR: can not record video
 *  ID_NOT_FIND_STORAGE: not find storage
 * Example:
 * Handler mHandler = new Handler() {...};
 * ...
 * RecordVideoHandler recHandler;
 * recHandler = new RecordVideoHandler(getApplicationContext()
 *  	, mHandler);
 *  
 * @version 0.4
 * @author YuCheng Hsu
 *
 */
public class RecordVideoHandler {
	// message id
	public static final int ID_UI_CHANGE = 0x11;
	public static final int ID_NOT_FINE_LOCATION = 0x22;
	public static final int ID_RECORD_ERROR = 0x33;
	public static final int ID_NOT_FIND_STORAGE = 0x44; 
	public static final int ID_LOCATION_CHANGE = 0x55;
	
	private Camera mCamera = null;
	private SubCreator mSubCreator = null;
	private Handler mHandler = null;
	private TelLocation mTelLocation = null;
	private RecordSetting mSetting = null;
	private Timer mTimer = null;
	private Context mContext = null;

	/** 
	 * @param context is an application context
	 */
	public RecordVideoHandler(Context context) {
		initConstructor(context);
	}
	
	/** Constructor for send message to UI
	 * @param context is an application context
	 * @param handler is UI handler. In this class, it can send 
	 *        message to notify UI change.
	 */
	public RecordVideoHandler(Context context, Handler handler) {
		mHandler = handler;
		initConstructor(context);
	}	
	
	private void initConstructor(Context context) {
		mContext = context;
		mCamera = new Camera();
		mSetting = new RecordSetting();
		mTelLocation = new TelLocation(mContext);
		mSubCreator = new SubCreator(mTelLocation);
		checkFineLocation();
	}

	public String getSubtitle() {
		String sub = "";
		if (mSetting.isSubEnable()) {
			sub = mSubCreator.getSubtitle();
		}
		return sub;
	}

	public GeoPoint getLocation() {
			return mTelLocation.getLocation();
	}

	/**
	 * create record setting
	 * @param attributes
	 * 			attributes[RecordSetting.VIDEO_QUALITY] = "VIDEO_QUALITY_HIGH"
	 * 													or "VIDEO_QUALITY_LOW"
	 * 			attributes[RecordSetting.TIME_INTERVAL] = "1" "15" "30" "0"
	 * 													units: minutes, "0" = no limit
	 *			attributes[RecordSetting.SUBTITLE] = "true" or "false" 
	 */	
	public void saveSetting(String[] attributes) {
		int[] values = new int[RecordSetting.NUMBER_ITEM];
		if (attributes[RecordSetting.VIDEO_QUALITY]
				.equals("VIDEO_QUALITY_HIGH")) {
			values[RecordSetting.VIDEO_QUALITY] = RecordSetting.VIDEO_QUALITY_HIGH;
		} else {
			values[RecordSetting.VIDEO_QUALITY] = RecordSetting.VIDEO_QUALITY_LOW;
		}

		if (attributes[RecordSetting.SUBTITLE].equals("true")) {
			values[RecordSetting.SUBTITLE] = RecordSetting.SUBTITLE_ENABLE;
			checkFineLocation();
		} else {
			values[RecordSetting.SUBTITLE] = RecordSetting.SUBTITLE_DISABLE;
		}

		values[RecordSetting.TIME_INTERVAL] = Integer
				.valueOf(attributes[RecordSetting.TIME_INTERVAL]);

		mSetting = new RecordSetting(values);

	}

	/**
	 * @param surHolder of SurfaceView of UI
	 */
	public void setPreview(SurfaceHolder surHolder) {
		mCamera.setPreview(surHolder);
	}

	/**
	 * open camera and recorder
	 */
	public void openResource() {
		mCamera.openResource();
	}
	
	/**
	 * release camera and recorder
	 */
	public void releaseResources() {
		mCamera.releaseResources();
		destoryTimer();
	}
	
	/**
	 * start recording video
	 * 
	 * @return name for test
	 */
	public String startRecording() {
		if (!Storage.isExternalStorageExist()) {
			sendMessage(ID_NOT_FIND_STORAGE);
			return "storageError";
		}

		mCamera.setVideoQuality(mSetting.getVideoQuality());
		String videoName;
		videoName = mCamera.startRecording();

		if (videoName != "FAIL") {
			// check subtitle is enable
			if (mSetting.isSubEnable()) {
				mSubCreator.startRecording(videoName);
			}

			initTimer();
		} else {
			sendMessage(ID_RECORD_ERROR);
		}
		return videoName;
	}
	
	
	public void stopRecording() {
		mCamera.stopRecording();
		destoryTimer();
	}
	
	public void enableMap(Handler handler) {
		mTelLocation.register(handler);
	}
	
	public void disableMap() {
		mTelLocation.unRegister();
	}
	
	/**
	 * notify Activity
	 */
	private void sendMessage(int id) {
		if (mHandler != null) {
			Message message = mHandler.obtainMessage(id);
			mHandler.sendMessage(message);
		}
	}
	
	private void checkFineLocation() {
		boolean result = DeviceInfo.isWifiOrGPSEnable(mContext);
		if (!result) {
			sendMessage(ID_NOT_FINE_LOCATION);
		}
	}

	/**
	 *  create timer to produce interval video file and notify Activity
	 */
	private void initTimer() {
		// check video time interval is not limit
		if (mTimer == null) {
			mTimer = new Timer();
		}

		final int UI_PERIOD = 5000;
		mTimer.schedule(new TimerTask() {

			@Override
			public void run() {
				sendMessage(ID_UI_CHANGE);
			}
		}, 0, UI_PERIOD);
		
		initVideoTimer();
	}

	private void initVideoTimer() {
		boolean isVideoTimer = mSetting.getTimeInterval() != RecordSetting.TIME_NO_LIMIT;

		// video timer
		if (isVideoTimer) {
			// 1 minutes = 60000 milliseconds
			final int PERIOD_UNIT = 60 * 1000;
			int period = mSetting.getTimeInterval() * PERIOD_UNIT;
			mTimer.schedule(new TimerTask() {

				@Override
				public void run() {
					mCamera.stopRecording();
					startRecording();
				}
			}, period, period);
		}
	}
	
	private void destoryTimer() {
		if (mTimer != null) {
			mTimer.cancel();
			mTimer = null;
		}
	}
	
}
