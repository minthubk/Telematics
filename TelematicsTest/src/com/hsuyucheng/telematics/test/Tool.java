package com.hsuyucheng.telematics.test;

import java.io.File;

import android.os.Environment;

/**
 * Tool for IO test
 * 
 * @version 0.1
 * @author YuCheng Hsu
 * 
 */
public class Tool {
	public static File testFolderFile() {
		File path = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/TelematicsTest/");
		return path;
	}

	public static void createTestFolder() {
		File path = testFolderFile();
		if (!path.exists()) {
			path.mkdirs();
		}
	}

	public static void deleteTestFolder(File folder) {
		if (folder.exists()) {
			if (folder.isDirectory()) {
				File[] files = folder.listFiles();
				for (int i = 0; i < files.length; i++) {
					File curFile = files[i];
					// call to delete the current file/folder
					deleteTestFolder(curFile);
				}
			}
			// If a folder Deleted all contents
			// If a File Jump straight here
			folder.delete();
		}
	}
}
