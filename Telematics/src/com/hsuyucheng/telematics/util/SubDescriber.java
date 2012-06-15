package com.hsuyucheng.telematics.util;

/**
 * SubDescriber is used to help SubCreator produce subtitle
 * @version 0.1
 * @author YuCheng Hsu
 *
 */
public class SubDescriber {
	public static final int INTERVAL_MILLISECONDS = 5000;
	public static String getSubType() {
		return ".sub";
	}

	private String mSubName = null;
	
	public SubDescriber() {
		super();
	}
	
	public String getName() {
		return mSubName;
	}
	
	public void setName(String name) {
		mSubName = name;
	}
	
	/**
	 * @return time string.
	 *        return string like this format: Time: 2011/10/05 10:23:40 
	 */
	public String getSubTime() {
		final String format = "yyyy/MM/dd hh:mm:ss";
		return "Time: " + TimeTool.getTimeToString(format);
	};
	
	public String getSubLocation(Double latitude, Double longitude) {
		String str = "Latitude: " + Double.toString(latitude)
				+ System.getProperty("line.separator") + "Longitude: " + Double.toString(longitude);
		return str;
	}
}
