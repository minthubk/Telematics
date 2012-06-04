package com.hsuyucheng.telematics.recording;

import java.io.IOException;

import java.util.List;

import com.hsuyucheng.telematics.util.Storage;
import com.hsuyucheng.telematics.util.TimeTool;

import android.hardware.Camera.Size;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.util.Log;
import android.view.SurfaceHolder;

/**
 * This class is used to control real camera of device. The Camera class
 * contains android.hardware.Camera and MediaRecorder, so you can record 
 * video by below steps. 
 * Record Video Step: 
 * 1. call openResource() to use camera device. 
 *    I recommend you to put openResource() in onResume() of Activity. 
 * 2. call setPreView() to preview of camera 
 *    It needs SurfaceHolder, so I recommend you to put setPreView() 
 *    in SurfaceCreated() or SurfaceChanged() to preview image. 
 * 3. call setVideoQuality() if you want change quality
 * 4. call stratRecording() to start recording video.
 * 		If return "FAIL" mean record video error
 * 5. call stopRecording() to stop recording video.
 * 5. call releaseResource() to release camera device. 
 *    I recommend you to put releaseResource() in onPause() of Activity. 
 * 
 * @version 0.2
 * @author YuCheng Hsu
 */

public class Camera {
	private int mVideoQuality = CamcorderProfile.QUALITY_HIGH;
	private android.hardware.Camera mCamera = null;
	private Size mPreviewSize = null;
	private MediaRecorder mMedRecorder = null;
	private SurfaceHolder mSurHolder = null;

	public Camera() {
		openResource();
	}

	
	/**
	 * @return 1: succeed 0:failure
	 */
	public int openResource() {
		int result = 0;
		try {
			// attempt to get a Camera instance
			if (mCamera == null) {
				mCamera = android.hardware.Camera.open();
			}
		} catch (Exception e) {
			// Camera is not available (in use or does not exist)
			Log.d("Camera", "Camera is not available" + e.getMessage());
		}

		result = 1;
		return result;
	}

	/**
	 * release resource for other application You need to call releaseRescoure()
	 * when progress terminal.
	 */
	public void releaseResources() {
		// release MediaRecorder
		if (mMedRecorder != null) {
			releaseMediaRecorder();
		}

		// release Camera
		if (mCamera != null) {
			mCamera.stopPreview();
			mCamera.release();
			mCamera = null;
		}
	}

	/**
	 * record video quality
	 * 
	 * @param videoQuality
	 *            is CamcorderProfile.QUALITY_HIGH or
	 *            CamcorderProfile.QUALITY_LOW, otherwise throw
	 *            IllegalArgumentException()
	 */
	public void setVideoQuality(int videoQuality) {
		if (videoQuality != CamcorderProfile.QUALITY_HIGH
				&& videoQuality != CamcorderProfile.QUALITY_LOW) {
			throw new IllegalArgumentException();
		}
		mVideoQuality = videoQuality;
	}


	/**
	 * Combine camera preview and UI
	 * 
	 * @param surHolder
	 *            is a SurfaceHolder of SurfaceView
	 */
	public void setPreview(SurfaceHolder surHolder) {
		mSurHolder = surHolder;
		
		if (mCamera == null) {
			openResource();
		}
		setPreviewSize();
		try {
			mCamera.setPreviewDisplay(mSurHolder);
			mCamera.startPreview();
		} catch (Exception e) {
			Log.d("Camera", "Error Camera preview: " + e.getMessage());

		}
	}


	/**
	 * startRecording() will control camera to record video.
	 * @return video name
	 */
	public String startRecording() {
		String name = prepareRecording();
		if (!(name.equals("FAIL"))) {
			mMedRecorder.start();
		}
		return name;
	}


	/**
	 * stopRecording() will release MediaRecorder and lock camera for other application
	 */
	public void stopRecording() {
		releaseMediaRecorder();
		
		if (mCamera != null) {
			mCamera.lock();
		}
	}

	/**
	 * release MediaRecorder instance and reconnect camera
	 */
	private void releaseMediaRecorder() {
		if (mMedRecorder != null) {
			mMedRecorder.stop();
			mMedRecorder.reset();
			mMedRecorder.release();
			try {
				mCamera.reconnect();
			} catch (IOException e) {
				Log.d("Camera", "Error Camera reconnect"
						+ "in release MediaRecorder: " + e.getMessage());
			}
			mMedRecorder = null;
		}
	}



	/**
	 * @param path
	 * 			video full path
	 * @return 0: no camera 1: suceed
	 */
	private int setRecorder(String path) {
		if (mCamera == null) {
			return 0;
		}
		mMedRecorder.setCamera(mCamera);
		mMedRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
		mMedRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
		mMedRecorder.setProfile(CamcorderProfile.get(mVideoQuality));
		mMedRecorder.setOutputFile(path + ".mp4");
		
		if (mSurHolder != null) {
			mMedRecorder.setPreviewDisplay(mSurHolder.getSurface());
		}
		return 1;
	}

	/**
	 * @return name: succeed, "FAIL" mean prepare fail
	 */
	private String prepareRecording() {
		mMedRecorder = new MediaRecorder();

		if (mCamera != null) {
			// unlock camera to let MediaRecorder instance to use
			mCamera.unlock();
		}
		String name = getVideoName(); 
		int result = setRecorder(Storage.getVideoLocation() + name);
		
		if (result == 0) {
			return "FAIL";
		}
		
		try {
			mMedRecorder.prepare();
		} catch (IOException e) {
			Log.d("Camera", "Error prepareRecording: " + e.getMessage());
		}
		
		return name;
	}

	/**
	 * @return name
	 *  		   return string like this format: VID_20111005_102340 
	 */
	private String getVideoName() {
		final String format = "yyyyMMdd_HHmmss";
		String videoName = "VID_" + TimeTool.getTimeToString(format);
		return videoName;
	}

	private void setPreviewSize() {
		final int WIDTH = 1024;
		final int HEIGHT = 768;
		
		List<Size> mSupportedPreviewSizes = mCamera.getParameters()
				.getSupportedPreviewSizes();

		// check camera of android device can modify setting
		if (mSupportedPreviewSizes != null && mPreviewSize == null) {
			mPreviewSize = getOptimalPreviewSize(mSupportedPreviewSizes, WIDTH,
					HEIGHT);
		}
		if (mPreviewSize != null) {
			android.hardware.Camera.Parameters parameters = mCamera
					.getParameters();
			parameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
			mCamera.setParameters(parameters);
		}
	}

	/**
	 * calculate suitable view size
	 * 
	 * @param sizes
	 *            android.hardware.Camera camera; 
	 *            size = camera.getParameters().getSupportedPreviewSizes();
	 * @param width
	 * @param height
	 * @return android.hardware.Camera.Size object
	 */
	public Size getOptimalPreviewSize(List<Size> sizes, int width, int height) {

		if (sizes == null) {
			return null;
		}

		final double ASPECT_TOLERANCE = 0.1;
		double targetRatio = (double) (width / height);

		Size optimalSize = null;
		double minDiff = Double.MAX_VALUE;

		int targetHeight = height;

		// try to find an size match aspect ratio and size
		for (Size size : sizes) {
			double ratio = (double) size.width / size.height;
			if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
				continue;
			if (Math.abs(size.height - targetHeight) < minDiff) {
				optimalSize = size;
				minDiff = Math.abs(size.height - targetHeight);
			}
		}

		// cannot find the one match the aspect ratio, ignore the requirement
		if (optimalSize == null) {
			minDiff = Double.MAX_VALUE;
			for (Size size : sizes) {
				if (Math.abs(size.height - targetHeight) < minDiff) {
					optimalSize = size;
					minDiff = Math.abs(size.height - targetHeight);
				}
			}
		}
		
		return optimalSize;
	}

}