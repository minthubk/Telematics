package com.hsuyucheng.telematics.playing.test;

import com.hsuyucheng.telematics.playing.Subtitle;

import junit.framework.TestCase;

public class SubtitleTest extends TestCase {
	private Subtitle mSubtitle;
	private String mSubPath = "/mnt/sdcard/TelematicsTest/test.sub"; 
	protected void setUp() throws Exception {
		super.setUp();
		mSubtitle = new Subtitle(mSubPath);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		mSubtitle = null;
	}

	public void testGetSub() {
		int MINISECONDS = 1000;
		assertTrue(mSubtitle.getSub(3 * MINISECONDS).contains("09:03:09"));
		assertFalse(mSubtitle.getSub(3 * MINISECONDS).contains("09:03:12"));
		assertTrue(mSubtitle.getSub(7 * MINISECONDS).contains("09:03:14"));
		assertFalse(mSubtitle.getSub(7 * MINISECONDS).contains("09:03:17"));		
	}


	public void testGetSubtitlePath() {
		String video = "/mnt/sdcard/TelematicsTest/test.mp4";
		assertEquals(mSubPath, Subtitle.getSubtitlePath(video));
		video = new String("/mnt/sdcard/TelematicsTest/test.mp3");
		assertNull(Subtitle.getSubtitlePath(video));
		video = new String("/mnt/sdcard/TelematicsTest/test");
		assertNull(Subtitle.getSubtitlePath(video));
		video = new String("");
		assertNull(Subtitle.getSubtitlePath(video));
	}

}
