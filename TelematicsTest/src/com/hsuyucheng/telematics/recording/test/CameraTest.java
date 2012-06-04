package com.hsuyucheng.telematics.recording.test;

import java.io.File;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.hardware.Camera.Size;
import com.hsuyucheng.telematics.recording.*;
import com.hsuyucheng.telematics.util.Storage;

import android.test.InstrumentationTestCase;

/**
 * @version 0.2
 * @author YuCHeng Hsu
 *
 */
public class CameraTest extends InstrumentationTestCase {
	Camera mCamera;
	android.hardware.Camera androidCamera;
	List<Size> mSupportedPreviewSizes;

	protected void setUp() throws Exception {
		androidCamera = android.hardware.Camera.open();
		mSupportedPreviewSizes = androidCamera.getParameters()
				.getSupportedPreviewSizes();
		androidCamera.release();
		mCamera = new Camera();
		super.setUp();
	}

	protected void tearDown() throws Exception {
		mCamera.releaseResources();
		super.tearDown();
	}

	public void testOpenResource() {
		mCamera.releaseResources();
		assertEquals(1, mCamera.openResource());
		mCamera.releaseResources();
		assertEquals(1, mCamera.openResource());
	}

	/**
	 * test video recording
	 */
	public void testStartRecording() {

		mCamera.openResource();
		String name = mCamera.startRecording();
		int TWO_SECONDS = 2000;
		Timer kk = new Timer();
		kk.schedule(new TimerTask() {
			@Override
			public void run() {
				mCamera.stopRecording();
			}
		}, TWO_SECONDS);

		File file = new File(Storage.getVideoLocation() + name + ".mp4");
		assertNotNull(file);
		assertTrue(file.delete());

	}
	
	public void testStopRecording() {
		// call many to check fail
		for (int ii = 0; ii < 5 ; ii++) {
			mCamera.stopRecording();
		}
	}

	public void testGetOptimalPreviewSize() {
		// 10 inch android device
		int WIDTH = 1024;
		int HEIGHT = 768;
		Size target = mCamera.getOptimalPreviewSize(mSupportedPreviewSizes,
				WIDTH, HEIGHT);
		int ANSWER_WIDTH = 1280;
		int ANSWER_HEIGHT = 720;
		assertEquals(ANSWER_WIDTH, target.width);
		assertEquals(ANSWER_HEIGHT, target.height);

		WIDTH = 640;
		HEIGHT = 480;
		target = mCamera.getOptimalPreviewSize(mSupportedPreviewSizes, WIDTH,
				HEIGHT);
		ANSWER_WIDTH = 640;
		ANSWER_HEIGHT = 480;
		assertEquals(ANSWER_WIDTH, target.width);
		assertEquals(ANSWER_HEIGHT, target.height);
	}

}
