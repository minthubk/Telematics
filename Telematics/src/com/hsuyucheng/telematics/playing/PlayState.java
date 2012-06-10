package com.hsuyucheng.telematics.playing;

import java.io.IOException;

import android.media.MediaPlayer;



public class PlayState extends PlayerState{
	private MediaPlayer mPlayer;
	public PlayState(MediaPlayer player) {
		mPlayer = player;
		try {
			mPlayer.prepare();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mPlayer.start();
	}
	public PlayState() {
		
		
	}
	
	@Override
	public String nextState() {
		return null;
	}

}
