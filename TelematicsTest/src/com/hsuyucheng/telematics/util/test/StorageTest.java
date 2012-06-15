package com.hsuyucheng.telematics.util.test;

import com.hsuyucheng.telematics.util.Storage;

import junit.framework.TestCase;

/**
 * @version 0.2
 * @author YuCHeng Hsu
 *
 */
public class StorageTest extends TestCase {
	String mLoc = "/mnt/sdcard/Telematics/";
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testGetVideoLocation() {
		assertEquals(mLoc, Storage.getVideoLocation());
		String kk = "/sdcard/Telematics/";
		assertNotSame(kk, Storage.getVideoLocation());
	}
	
	public void testIsExternalStorageExist() {
		// when external storage exist
		assertTrue(Storage.isExternalStorageExist());
		// no external storage
		// assertFalse(Storage.isExternalStorageExist());
	}
	
	public void testIsFileExist() {
		assertFalse(Storage.isFileExist(""));
		assertFalse(Storage.isFileExist("/qq"));
		assertFalse(Storage.isFileExist("qq"));
		assertTrue(Storage.isFileExist(mLoc));
		assertTrue(Storage.isFileExist("/mnt/sdcard"));		
	}
}
