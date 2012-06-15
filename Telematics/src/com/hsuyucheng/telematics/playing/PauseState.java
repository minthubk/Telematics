 package com.hsuyucheng.telematics.playing;

import android.media.MediaPlayer;

public class PauseState extends PlayerState {
	@Override
	public String nextState(PlayVideoHandler handler, MediaPlayer player) {
		if (!player.isPlaying()) {
			player.start();
			handler.setPlayerState(new PlayState());
		} 			
		return "Pause";
	}

}
