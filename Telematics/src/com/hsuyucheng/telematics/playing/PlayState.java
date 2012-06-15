package com.hsuyucheng.telematics.playing;

import android.media.MediaPlayer;

public class PlayState extends PlayerState{
	@Override
	public String nextState(PlayVideoHandler handler, MediaPlayer player) {
		if (player.isPlaying()) {
			player.pause();
			handler.setPlayerState(new PauseState());
		}
		return "Resume";
	}

	
}
