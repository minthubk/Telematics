package com.hsuyucheng.telematics.recording.test;

import java.util.Timer;

import android.app.Instrumentation;
import android.test.InstrumentationTestCase;

import com.hsuyucheng.telematics.recording.RecordVideoHandler;



/**
 * RecordVideoHandler has timer, be careful!
 * @version 0.1
 * @author YuCheng Hsu
 *
 */
public class RecordVideoHandlerTest extends InstrumentationTestCase {
	private RecordVideoHandler mRecHandler;
	private Instrumentation mInst;
	private final int NUMBER = 20;
	private Timer mTimer;
	protected void setUp() throws Exception {
		mInst = getInstrumentation();
		mRecHandler = new RecordVideoHandler(mInst.getContext());
		mTimer = new Timer();
		super.setUp();
	}

	protected void tearDown() throws Exception {
		mTimer.cancel();
		mTimer = null;
		super.tearDown();
	}

	public void testOpenResource() {
		for (int ii = 0; ii < NUMBER; ii++) {
			mRecHandler.openResource();
		}
	}

	public void testReleaseResources() {
		for (int ii = 0; ii < NUMBER; ii++) {
			mRecHandler.releaseResources();
		}
	}

	public void testStartRecording() {;
		String name = mRecHandler.startRecording();
		assertEquals("FAIL", name);
	}

	public void testStopRecording() {
		for (int ii = 0; ii < NUMBER; ii++) {
			mRecHandler.releaseResources();
		}
	}

}
