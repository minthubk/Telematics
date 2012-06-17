package com.hsuyucheng.telematics.youtube.test;

import java.util.List;

import com.hsuyucheng.telematics.playing.Video;
import com.hsuyucheng.telematics.youtube.YouTubeClient;

import junit.framework.TestCase;

public class YouTubeClientTest extends TestCase {
	YouTubeClient mYClient;
	String mName = "eps1422";
	protected void setUp() throws Exception {
		super.setUp();
		mYClient = new YouTubeClient();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testGetYouTubeData() {
		String video0 = "VID_20120611_085049";
		String video1 = "VID_20120611_090309";
		List<Video> videos = mYClient.getYouTubeData(mName);
		assertEquals(video0, videos.get(0).getName());
		assertEquals(video1, videos.get(1).getName());
	}

}
