package com.hsuyucheng.telematics.playing.test;

import com.hsuyucheng.telematics.playing.Video;

import junit.framework.TestCase;

public class VideoTest extends TestCase {
	private String mPath =  "/mnt/sdcard/TelematicsTest/test.mp4";
	protected void setUp() throws Exception {
		super.setUp();
		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testToString() {
		Video video1 = new Video(mPath);
		assertEquals(mPath, video1.toString());
		Video video2 = new Video("/mnt/qqqqqqqq.mp4");
		assertNull(video2.toString());
		Video video3 = new Video("/mnt/");
		assertNull(video3.toString());		
	}

}
