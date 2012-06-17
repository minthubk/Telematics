package com.hsuyucheng.telematics.playing;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.view.SurfaceHolder;

/** Play Video.
 *  player state is state pattern
 *  
 *  Play Step:
 *  1. startPlaying(): that set UI shown Video and start play
 *  2. use handle() to switch player state
 *  3. releaseResource when exit 
 *  
 * @author hsuyucheng
 * @version 0.2
 */
public class PlayVideoHandler {
	public static final int CHANGE_SUB = 0x11;
	public static final int RESTATE = 0x12;
	private Handler mHandler = null;
	private String mPath;
	private MediaPlayer mMPlayer = null;
	private PlayerState mState = null;
	private Subtitle mSub = null;
	private Timer mTimer = null;
	
	public PlayVideoHandler(String path, Handler handler) {
		
		mHandler = handler;
		mPath = path; 
		String subPath = Subtitle.getSubtitlePath(this.mPath);
		if (subPath != null) {
			mSub = new Subtitle(subPath);
		}
	}
	
	/** start play video
	 * @param surHolder
	 */
	public void startPlaying(SurfaceHolder surHolder) {
		if (mMPlayer == null) {
			mMPlayer = new MediaPlayer();
			mMPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		} else {
			mMPlayer.reset();
		}
		
		mMPlayer.setDisplay(surHolder);
		
		try {
			mMPlayer.setDataSource(mPath);
			mMPlayer.prepare();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		play();
	}
	
	private void play() {
		mMPlayer.start();
		mState = new PlayState();
		initTimer();
	}
	
	/**
	 * @param video_position
	 * @return sub, is no sub return null
	 */
	public String getSub() {
		return mSub.getSub(mMPlayer.getCurrentPosition());
	}
	
	public MediaPlayer getPlayer() {
		return mMPlayer;
	}
	
	public void reset() {
		destoryTimer();
		if (mMPlayer != null) {
			mMPlayer.stop();
			try {
				mMPlayer.prepare();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mMPlayer.seekTo(0);
			play();
		}
	}
	
	public String handle() {
		return mState.nextState(this, mMPlayer);
	}
	
	
	public void setPlayerState(PlayerState state) {
		mState = state;
	}
	
	private void destoryTimer() {
		if (mTimer != null) {
			mTimer.cancel();
			mTimer = null;
		}		
	}
	
	public void releaseResource() {
		destoryTimer();
		if (mMPlayer != null) {
			//mMPlayer.stop();
			mMPlayer.release();
		}
	}
	
	private void initTimer() {
		if (mSub != null) {
			if (mTimer == null) {
				mTimer = new Timer();
			}

			final int PERIOD = 5000;
			mTimer.schedule(new TimerTask() {
				@Override
				public void run() {
					sendMessage(CHANGE_SUB);
				}
			}, 0, PERIOD);
		}
	
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
}
