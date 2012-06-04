package com.hsuyucheng.telematics.util.test;


import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import com.hsuyucheng.telematics.recording.TelLocation;
import com.hsuyucheng.telematics.util.Storage;
import com.hsuyucheng.telematics.util.SubCreator;

import android.test.AndroidTestCase;

/**
 * @version 0.3
 * @author YuCHeng
 *
 */
public class SubCreatorTest extends AndroidTestCase {
	private SubCreator mSubCreator;
	private Timer mTimer;
	protected void setUp() throws Exception {
		TelLocation telLocation = new TelLocation(this.getContext());
		mSubCreator = new SubCreator(telLocation);
		mTimer = new Timer();
		super.setUp();
	}

	protected void tearDown() throws Exception {
		mSubCreator = null;
		mTimer.cancel();
		mTimer = null;
		super.tearDown();
	}

	public void testWriteSubtitle() {
		String pathNull = "/dev/null";
		String pathNotExist = "/sdcard/TelematicsTest/KK";
		String message = "test";
		String message2 = "sadhj 43rg s123 ********";
		assertEquals(1, mSubCreator.writeSubtitle(message, pathNull));
		assertEquals(1, mSubCreator.writeSubtitle(message2, pathNull));
		assertEquals(0, mSubCreator.writeSubtitle(message2, pathNotExist));
	}
	
	public void testStartRecording() {
		String name = "ttt";
		mSubCreator.startRecording(name);
		int ONE_SECONDS = 1000;
		mTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				mSubCreator.stopRecording();
			}
		}, ONE_SECONDS);
		
		File file = new File(Storage.getVideoLocation() + name + ".sub");
		assertNotNull(file);
		assertTrue(file.delete());
	}
	
	public void testStopRecording() {
		// calls many stopRecording to check failure
		for (int ii = 0; ii < 5; ii++) {
			mSubCreator.stopRecording();
		}
	}

}
