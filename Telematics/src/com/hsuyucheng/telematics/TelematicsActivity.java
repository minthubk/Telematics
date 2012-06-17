package com.hsuyucheng.telematics;


import com.hsuyucheng.telematics.util.DeviceInfo;
import com.hsuyucheng.telematics.util.Storage;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/** This is a Telematics activity. When user touch application
 *  name "Telematics", this activity will be called.
 *  
 * @version 0.1
 * @author YuCheng Hsu
 *
 */
public class TelematicsActivity extends Activity {
    /** Called when the activity is first created. */
	private Button mRecEntry;
	private Button mVideoEntry;
	private Button mExitEntry;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		Storage.createFolder();
		initButton();
		checkInternet();
	}
    
	private void initButton() {
		mRecEntry = (Button) findViewById(R.id.recEntry);
		mVideoEntry = (Button) findViewById(R.id.playEntry);
		mExitEntry = (Button) findViewById(R.id.exitEnrty);

		mRecEntry.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(TelematicsActivity.this, RecActivity.class);
				startActivity(intent);
			}
		});

		mVideoEntry.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(TelematicsActivity.this, VideoListActivity.class);
				startActivity(intent);				
			}
		});

		mExitEntry.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
    }
    
	private void checkInternet() {
		if (!DeviceInfo.isWifiEnable(this.getApplicationContext())) {
			Builder warnAlertDialog = new AlertDialog.Builder(this);
			warnAlertDialog.setTitle("Warrning");
			warnAlertDialog.setMessage("No Internet");
			DialogInterface.OnClickListener OkClick = new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
				}

			};
			warnAlertDialog.setNeutralButton("I got it.", OkClick);
			warnAlertDialog.show();
		}

	}
}