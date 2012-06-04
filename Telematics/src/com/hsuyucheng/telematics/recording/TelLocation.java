package com.hsuyucheng.telematics.recording;

import com.google.android.maps.GeoPoint;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/** 
 * Get location latitude and longitude form GPS or Wifi
 * Telematics() needs to pass context to request location provider.
 * Warring: checks GPS or network location enable of setting of 
 *          android devices.
 * @version 0.1
 * @author YuCHeng Hsu
 * 
 */
public class TelLocation implements LocationListener{
	private Context mContext;
	private LocationManager mLocManager;
	private Handler mHandler;

	private double mLongitude;
	private double mLatitude;
	
	public TelLocation(Context context) {
		mContext = context;
		mLocManager = (LocationManager) mContext
				.getSystemService(Context.LOCATION_SERVICE);
		
		initLocation();
		requestLocProvider();
	}
	
	public double getLongitude() {
		return mLongitude;
	}
	
	public double getLatitude() {
		return mLatitude;
	}
	
	/**
	 * @return GeoPoint instance.
	 * 		   GeoPoint can control map location and mark map.
	 */
	public GeoPoint getLocation() {
		GeoPoint point = new GeoPoint((int) (mLatitude * 1E6), 
				(int)(mLongitude * 1E6));
		return point;
	}
	
	private void setLatitudeAndLongitude(Location location) {
		mLatitude = location.getLatitude();
		mLongitude = location.getLongitude();		
	}
 	
	private void initLocation() {
		Location location = 
				mLocManager.getLastKnownLocation(
						LocationManager.NETWORK_PROVIDER);
		if (location != null) {
			setLatitudeAndLongitude(location);
		}
	}
	
	/** request location provider
	 *  provider contains network and GPS
	 */
	private void requestLocProvider() {
		// location provider init data
		// TIME: unit millisecond 
		final int TIME = 10000;
		// DISATANCE: unit meter
		final float DISTANCE = 50.0f; 
		
		mLocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
				TIME, DISTANCE, this);
		mLocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				TIME, DISTANCE, this);
	}
	
	private void sendMessage() {
		if (mHandler != null) {
			Message message = mHandler.obtainMessage(RecordVideoHandler.ID_LOCATION_CHANGE);
			mHandler.sendMessage(message);
		}
	}
	
	public void register(Handler handler) {
		mHandler = handler;
	}
	
	public void unRegister() {
		mHandler = null;
	}
	
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		setLatitudeAndLongitude(location);
		sendMessage();
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		// do nothing
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		// do nothing
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

}