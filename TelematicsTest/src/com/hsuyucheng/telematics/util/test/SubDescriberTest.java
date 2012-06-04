package com.hsuyucheng.telematics.util.test;

import com.hsuyucheng.telematics.util.SubDescriber;

import junit.framework.TestCase;

/**
 * @version 0.2
 * @author YuCHeng Hsu
 *
 */
public class SubDescriberTest extends TestCase {
	private SubDescriber mSubDescriber;
	protected void setUp() throws Exception {
		mSubDescriber = new SubDescriber();
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testGetSubTime() {
		// format: Time: 2011/10/05 10:23:40
		String kk = mSubDescriber.getSubTime();
		int FIRST_SLASH = 10;
		int FIRST_COLONE = 4;
		assertEquals(FIRST_SLASH, kk.indexOf("/"));
		assertEquals(FIRST_COLONE, kk.indexOf(":"));
	}

}
