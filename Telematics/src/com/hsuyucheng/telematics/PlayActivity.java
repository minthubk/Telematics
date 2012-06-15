package com.hsuyucheng.telematics;

import com.hsuyucheng.telematics.playing.PlayVideoHandler;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**Play Video
 * @author hsuyucheng
 * @version 0.2
 */
public class PlayActivity extends Activity implements SurfaceHolder.Callback{
	private SurfaceView mSurfaceView;
	private SurfaceHolder mSurfaceHolder;
	// mPlay has two state: resume and pause
	private Button mPlayButton;
	private Button mBackButton;
	private Button mResetButton;
	private TextView mTextView;
	private PlayVideoHandler mPlayHandler;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.playing);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		// init view data
		mTextView = (TextView)findViewById(R.id.videoTextView);
		initSurface();
		initButton();
		mPlayHandler = new PlayVideoHandler("", mHandler);
	}
	

	
	private void initSurface() {
		mSurfaceView = (SurfaceView)findViewById(R.id.videoSurView);
		mSurfaceHolder = mSurfaceView.getHolder();
		mSurfaceHolder.addCallback(this);	
		// Android 2.0 needs setType
		//mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	private void initButton() {
		mPlayButton = (Button) findViewById(R.id.videoPlayButton);
		mBackButton = (Button) findViewById(R.id.videoBackButton);
		mResetButton = (Button) findViewById(R.id.videoResetButton);

		mPlayButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String state = mPlayHandler.handle();
				mPlayButton.setText(state);
			}
		});

		mBackButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mPlayHandler.releaseResource();
				finish();
				
			}
		});
		
		mResetButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mPlayHandler.reset();	
			}
		});
	}
	
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		mPlayHandler.startPlaying(holder);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		mPlayHandler.startPlaying(holder);
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		
	}
	
	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case PlayVideoHandler.CHANGE_SUB:
				String sub = mPlayHandler.getSub();
				mTextView.setText(sub);
				break;
			}
			super.handleMessage(msg);
		}
	};


}
