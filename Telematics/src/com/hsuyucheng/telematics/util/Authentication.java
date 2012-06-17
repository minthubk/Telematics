package com.hsuyucheng.telematics.util;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.util.Log;

public class Authentication {
	AccountManager accountManager;
	Account[] accounts;
	
	public String getAccount(Context context) {
  	  accountManager = AccountManager.get(context);
  	  accounts = accountManager.getAccountsByType("com.google");
  	  Log.d("account name", accounts[0].name);
  	  return accounts[0].name;
  }
  
  
}
