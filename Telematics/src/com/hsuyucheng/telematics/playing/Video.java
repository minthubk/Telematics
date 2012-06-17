package com.hsuyucheng.telematics.playing;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import com.hsuyucheng.telematics.util.Storage;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.provider.MediaStore.Video.Thumbnails;



/** Video Object for VideoList (UI element)
 *  Local video use Video(String path) creator
 *  Youtube video use Video(String title, String url, String thumbUrl)
 * @author hsuyucheng
 * @version 0.2
 */
public class Video {
	public static final int TYPE_LOCAL = 1;
	public static final int TYPE_YOUTUBE = 2;
	
	public Video(String path) {
		File temp = new File(path);
		if (temp.exists() && temp.isFile()) {
			mPath = path;
			mName = temp.getName();
			if (mPath.contains(Storage.TAG)) {
				Bitmap bmThumbnail;
		        bmThumbnail = ThumbnailUtils.createVideoThumbnail(mPath, Thumbnails.MICRO_KIND);
		        mIcon = new BitmapDrawable(null, bmThumbnail);
			}
			mType = TYPE_LOCAL;
		} else {
			mPath = null;
			mName = null;
			mIcon = null;
		}
	}
	
	public Video(String title, String url, String thumbUrl) {
		mPath = url;
		mName = title;
		try {
			mIcon = Drawable.createFromStream(((java.io.InputStream) new java.net.URL(thumbUrl).getContent()), "name");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		mType = TYPE_YOUTUBE;
	}

	public int tyep() {
		return mType;
	} 
	public String getPath() {
		return mPath;
	}

	public String getName() {
		return mName;
	}

	public Drawable getIcon() {
		return mIcon;
	}
	
	@Override
	public String toString() {
		return mPath;
	}

	private Drawable mIcon;
	private String mPath;
	private String mName;
	private int mType;
}
