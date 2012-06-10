package com.hsuyucheng.telematics.playing;

import java.io.IOException;


import android.media.MediaPlayer;

public class PrepareState extends PlayerState{
	private MediaPlayer mPlayer;
	public PrepareState(MediaPlayer player) {
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
	}
	@Override
	public String nextState() {
		
		return "Play";
	}

}
