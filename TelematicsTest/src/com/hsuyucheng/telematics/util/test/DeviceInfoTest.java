package com.hsuyucheng.telematics.util.test;

import com.hsuyucheng.telematics.util.DeviceInfo;

import android.content.Context;
import android.test.AndroidTestCase;

/**
 * @version 0.2
 * @author YuCHeng Hsu
 *
 */
public class DeviceInfoTest extends AndroidTestCase {
	private Context mContext;
	protected void setUp() throws Exception {
		mContext = this.getContext();
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testCheckCameraHardware() {
		// if camera is exist
		assertEquals(true, DeviceInfo.checkCameraHardware(mContext));
		// if camera is not exist
		//assertEquals(false, DeviceInfo.checkCameraHardware(mContext));		
	}

	public void testIsWifiOrGPSEnable() {
		// enable GPS or Wifi fine location
		assertEquals(true, DeviceInfo.isWifiOrGPSEnable(mContext));		
		// disable GPS and Wifi fine location
		//assertEquals(false, DeviceInfo.isWifiOrGPSEnable(mContext));			
	}

}
