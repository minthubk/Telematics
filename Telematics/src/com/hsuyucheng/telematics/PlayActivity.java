package com.hsuyucheng.telematics;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

/**Play Video
 * @author hsuyucheng
 * @version 0.1
 */
public class PlayActivity extends Activity implements SurfaceHolder.Callback{
	private SurfaceView mSurfaceView;
	private SurfaceHolder mSurfaceHolder;
	// mPlay has two state: resume and pause
	private Button mPlay;
	private Button mBack;
	private Button mReset;
	private SeekBar mSeekBar;
	private TextView mTextView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.playing);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		// init view data
		mSeekBar = (SeekBar)findViewById(R.id.videoSeekBar);
		mTextView = (TextView)findViewById(R.id.videoTextView);
		initSurface();
		initButton();
		
	}
	
	private void initSurface() {
		mSurfaceView = (SurfaceView)findViewById(R.id.videoSurView);
		mSurfaceHolder = mSurfaceView.getHolder();
		mSurfaceHolder.addCallback(this);	
		// data from other, setType is deprecated in Android 4.0
		mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}
	
	private void initButton(){
		mPlay = (Button)findViewById(R.id.videoPlayButton);
		mBack = (Button)findViewById(R.id.videoBackButton);
		mReset = (Button)findViewById(R.id.videoSeekBar);		
	} 
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}
}
