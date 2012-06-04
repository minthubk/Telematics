package com.hsuyucheng.telematics.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;

/** This class is a tool to check device information.
 * @version 0.1
 * @author YuCheng Hsu
 */
public class DeviceInfo {
	
    /** Checking hardware has camera.
     * @param context
     * @return boolean; return true when camera exist
     */
    public static  boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }
    
	public static boolean isWifiOrGPSEnable(Context context) {
		boolean result = true;
		LocationManager locManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);

		if (!locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
				|| !locManager
						.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
			result = false;
		}

		return result;
	}

}
