package com.hsuyucheng.telematics.util.test;

import android.app.Instrumentation;
import android.content.Context;
import android.test.InstrumentationTestCase;

import com.hsuyucheng.telematics.util.Authentication;

public class AuthenticationTest extends InstrumentationTestCase {
	private Authentication mAuth;
	private String mUser = "eps1422@gmail.com";
	private Context mContext;
	private Instrumentation mInst;
	
	protected void setUp() throws Exception {
		super.setUp();
		mAuth = new Authentication();
		mInst = getInstrumentation();
		mContext = mInst.getContext();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testGetAccount() {
		assertEquals(mUser, mAuth.getAccount(mContext));
		String tt = "qq@gmail.com";
		assertFalse(mAuth.getAccount(mContext).equals(tt));
		tt = new String("asd@yahoo.com");
		assertFalse(mAuth.getAccount(mContext).equals(tt));		
		
	}

}
