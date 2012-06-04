package com.hsuyucheng.telematics.recording;

import java.util.ArrayList;

import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;


/**
 * You can use this class to mark map
 * Guide: 
 * 1. get Overlay from MapView List<Overlay>
 *    mMapOverlays = mMapView.getOverlays(); 
 * 2. select icon LocationOverlay
 *    mOverlay = new LocationOverlay(icon_object); 
 * 3. set location
 *    OverlayItem overlayitem = new OverlayItem(GeoPoint, "", "");
 *         mOverlay.addOverlay(overlayitem); 
 * 4. add into MapView
 *    mMapOverlays.add(mOverlay);
 * @version 0.1
 * @author YuCheng Hsu 
 *
 */

public class LocationOverlay extends ItemizedOverlay<OverlayItem> {

	
	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();

	
	/**
	 * @param defaultMarker
	 *            In order for the Drawable to actually get drawn, it must have
	 *            its bounds defined. And we want the center-point at the bottom
	 *            of the image to be the point at which it's attached to the map
	 *            coordinates.
	 */    
	public LocationOverlay(Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
	}

	public void addOverlay(OverlayItem overlay) {
		mOverlays.add(overlay);
		populate();
	}

	@Override
	protected OverlayItem createItem(int i) {
		return mOverlays.get(i);
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return mOverlays.size();
	}

}
