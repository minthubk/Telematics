package com.hsuyucheng.telematics.playing;

import android.media.MediaPlayer;

/** State Pattern
 * @author hsuyucheng
 * @Version 0.1
 * 
 */
public abstract class PlayerState {
	abstract public String nextState(PlayVideoHandler handler, MediaPlayer player);
}
