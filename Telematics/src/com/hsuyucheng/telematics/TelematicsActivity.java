package com.hsuyucheng.telematics;

import com.hsuyucheng.telematics.util.Storage;

import android.app.Activity;
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
				//intent.setClass(TelematicsActivity.this, VideoActivity.class);
				intent.setClass(TelematicsActivity.this, PlayActivity.class);
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
    
}