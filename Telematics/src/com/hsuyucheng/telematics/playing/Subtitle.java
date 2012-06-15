package com.hsuyucheng.telematics.playing;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.hsuyucheng.telematics.util.Storage;

/** Subtitle read sub file from storage to create sub
 *  Before create, you should check file is exist,
 *  that you can use getSubtitlePath to check file exist.
 *  
 * @author hsuyucheng
 * @Version 0.1
 */
public class Subtitle {
	private List<String> subs;
	public Subtitle(String path) {
		init(path);
	}
	
	/**
	 * @param video_position
	 * @return string
	 */
	public String getSub(int video_position) {
		int MINISECONDS = 1000;
		int SUB_TIME_UNIT = 5;
		int index = video_position / MINISECONDS / SUB_TIME_UNIT;
		if (index < subs.size()) {
			Log.d("getSub", subs.get(index));
			return subs.get(index);
		} else {
			return null;
		}		
	}
	
	private void init(String path) {
		subs = new ArrayList<String>();
		Log.d("sub", "init" + path);
		BufferedReader buf;
		try {
			buf = new BufferedReader(new FileReader(path));

			while (true) {
				String sub = buf.readLine();
				if (sub == null) {
					break;
				}
				if (sub.contains(",")) {
					String stringTime, stringLa, stringLo;
					stringTime = buf.readLine();
					stringLa = buf.readLine();
					stringLo = buf.readLine();
					String target = stringTime + "\n" + stringLa + "\n"
							+ stringLo;
					subs.add(target);

				}
			}

			buf.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** use getSubtitlePath to create Subtitle
	 * @param path
	 * @return subtitle path or null
	 */
	public static String getSubtitlePath(String path) {
		Log.d("path", path);
		String temp = path.replace("mp4", "sub");
		
		boolean isExist = Storage.isFileExist(temp);
		if (isExist) {
			return temp;
		} else { 
			return null;
		}
	}
}
