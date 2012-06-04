package com.hsuyucheng.telematics;

import java.util.List;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.hsuyucheng.telematics.recording.LocationOverlay;
import com.hsuyucheng.telematics.recording.RecordSetting;
import com.hsuyucheng.telematics.recording.RecordVideoHandler;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/** This is a recording video activity. 
 * @version 0.4
 * @author YuCheng Hsu
 *
 */
public class RecActivity extends MapActivity implements SurfaceHolder.Callback {
	private SurfaceHolder mSurHolder = null;
	private SurfaceView mSurView = null;
	private RecordVideoHandler mRecHandler = null;
	private Button mRecButton = null;
	private Button mMapButton = null;
	private Button mBackButton = null;
	private Button mSettingButton = null;
	private TextView mTextView = null;
	// change button behavior
	private boolean mIsRecMode = false;
	private boolean mIsMapShow = false;
	private MapView mMapView = null;
	private MapController mMapController = null;
	// get notify from SettingActivity
	private static final int SETTING = 0x67;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recording);

		// rotate the screen to landspace
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

		initView();
		initButtonEvent();
		initMap();

		mRecHandler = new RecordVideoHandler(this.getApplicationContext(),
				mHandler);
	}

	private void initView() {
		mSurView = (SurfaceView) findViewById(R.id.recView);
		// assign Surface instance and assign to SurfaceHolder
		mSurHolder = mSurView.getHolder();
		mSurHolder.addCallback(this);
		// Android 2.3 needs setType. Android 4.0 can delete this line.
		mSurHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		mTextView = (TextView) findViewById(R.id.recTextView);
	}

	private void initButtonEvent() {
		mRecButton = (Button) findViewById(R.id.recButton);
		mMapButton = (Button) findViewById(R.id.recMapButton);
		mBackButton = (Button) findViewById(R.id.recBackButton);
		mSettingButton = (Button) findViewById(R.id.recSettingButton);
		// set event
		mBackButton.setOnClickListener(new View.OnClickListener() {
			// exit RecActivity
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		mRecButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mIsRecMode = !mIsRecMode;
				if (mIsRecMode) {
					mRecHandler.startRecording();
					mRecButton.setText(R.string.Stop);
				} else {
					mRecHandler.stopRecording();
					mRecButton.setText(R.string.Rec);
					mTextView.setText("");
				}
			}
		});

		mSettingButton.setOnClickListener(new View.OnClickListener() {
			// call setting activity
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(RecActivity.this, SettingActivity.class);
				startActivityForResult(intent, SETTING);
			}
		});

		mMapButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mIsMapShow = !mIsMapShow;
				if (mIsMapShow) {
					mMapView.setVisibility(View.VISIBLE);
					mRecHandler.enableMap(mHandler);
				} else {
					mMapView.setVisibility(View.INVISIBLE);
					mRecHandler.disableMap();
				}
			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case SETTING:
			// SettingActivity is close, so createSetting();
			createSetting();
		}
	}
	
	private void initMap() {
		mMapView = (MapView) findViewById(R.id.recMapView);
		mMapController = mMapView.getController();
		final int ZOOM = 13;
		mMapController.setZoom(ZOOM);
	}

	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case RecordVideoHandler.ID_UI_CHANGE:
				String sub = mRecHandler.getSubtitle();
				mTextView.setText(sub);
				break;
			case RecordVideoHandler.ID_NOT_FINE_LOCATION:
				warnMessage("GPS and Wif not enable fine location");
				break;
			case RecordVideoHandler.ID_RECORD_ERROR:
				warnMessage("Record video error");
				break;
			case RecordVideoHandler.ID_NOT_FIND_STORAGE:
				mRecButton.setText(R.string.Rec);
				warnMessage("No external storage exist");
				break;
			case RecordVideoHandler.ID_LOCATION_CHANGE:
				setMap();
			}
			super.handleMessage(msg);
		}
	};

	private void warnMessage(String context) {
		Builder warnAlertDialog = new AlertDialog.Builder(this);
		warnAlertDialog.setTitle("Warrning");
		warnAlertDialog.setMessage(context);
		DialogInterface.OnClickListener OkClick = new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {

			}

		};
		warnAlertDialog.setNeutralButton("I got it.", OkClick);
		warnAlertDialog.show();
	}

	private void setMap() {
		GeoPoint geoPoint = mRecHandler.getLocation();
			mMapController.animateTo(geoPoint);
			// mark map
			List<Overlay> overlays = mMapView.getOverlays();
			Drawable mark = getResources().getDrawable(R.drawable.car);
			mark.setBounds(0, 0, mark.getIntrinsicWidth(),
					mark.getIntrinsicHeight());
			LocationOverlay locOverlay = new LocationOverlay(mark);
			OverlayItem overlayitem = new OverlayItem(geoPoint, "", "");
			locOverlay.addOverlay(overlayitem);
			overlays.add(locOverlay);
	}

	/**
	 * create record setting
	 */
	private void createSetting() {
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());

		String[] attributes = new String[RecordSetting.NUMBER_ITEM];
		attributes[RecordSetting.VIDEO_QUALITY] = settings.getString(
				"VIDEO_QUALITY", "VIDEO_QUALITY_HIGH");
		attributes[RecordSetting.TIME_INTERVAL] = settings.getString(
				"TIME_INTERVAL", "15");
		attributes[RecordSetting.SUBTITLE] = Boolean.toString(settings
				.getBoolean("SUBTITLE", true));
		mRecHandler.saveSetting(attributes);
	}

	@Override
	public void onPause() {
		super.onPause();
		// release and reset camera
		mRecHandler.releaseResources();
	}

	@Override
	public void onResume() {
		super.onResume();
		// connect camera
		mRecHandler.openResource();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// This method produced by SurfaceHolder.Callback
		mRecHandler.setPreview(holder);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// The Surface has been created, now tell the camera where to draw the
		// preview.
		mRecHandler.setPreview(holder);
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// This method produced by SurfaceHolder.Callback
		// release resource is moved into onPause()
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

}
