package com.hsuyucheng.telematics.util;

import java.io.File;

import android.os.Environment;
import android.os.StatFs;

public class Storage {
	public static void createFolder() {
		File Path = new File(getVideoLocation());
		// make the folder "Telematics" if this folder doesn't exist
		if (!Path.exists()) {
			Path.mkdirs();
		}
	}

	public static boolean isFileExist(String path) {
		File file = new File(path);
		return file.exists();
	}
	
	public static String getVideoLocation() {
		final String FOLDER = "/Telematics/";
		return Environment.getExternalStorageDirectory().getAbsolutePath()
				+ FOLDER;
	}

	public static boolean isExternalStorageExist() {
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			return true;
		}
		return false;
	}

	public static boolean isExternalStorageFull() {
		boolean isFull = false;
		File external = Environment.getExternalStorageDirectory();

		// if external storage is available
		if (isExternalStorageExist()) {
			StatFs statfs = new StatFs(external.getPath());

			long blockSize = statfs.getBlockSize();
			long availableBlocks = statfs.getAvailableBlocks();

			// available space (MB)
			int UNIT = 1024;
			long freeSize = availableBlocks * blockSize / UNIT / UNIT;
			
			int MINI_SIZE_MB = 50;
			if (freeSize <= MINI_SIZE_MB) {
				isFull = true;
			} else {
				isFull = false;
			}

		}
		return isFull;
	}
}
