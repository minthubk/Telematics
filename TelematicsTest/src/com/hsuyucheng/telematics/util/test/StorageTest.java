package com.hsuyucheng.telematics.util.test;

import com.hsuyucheng.telematics.util.Storage;

import junit.framework.TestCase;

/**
 * @version 0.1
 * @author YuCHeng Hsu
 *
 */
public class StorageTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testGetVideoLocation() {
		String kk = "/mnt/sdcard/Telematics/";
		assertEquals(kk, Storage.getVideoLocation());
		kk = "/sdcard/Telematics/";
		assertNotSame(kk, Storage.getVideoLocation());
	}
	
	public void testIsExternalStorageExist() {
		// when external storage exist
		assertTrue(Storage.isExternalStorageExist());
		// no external storage
		// assertFalse(Storage.isExternalStorageExist());
	}
}
